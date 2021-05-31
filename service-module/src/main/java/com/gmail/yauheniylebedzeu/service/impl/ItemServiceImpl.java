package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ItemRepository;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.ItemDescription;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.ItemConverter;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItemDescription;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    @Override
    @Transactional
    public PageDTO<ItemDTO> getItemPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ItemDTO> page = new PageDTO<>();
        Long countOfArticles = itemRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfArticles, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Item> items = itemRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ItemDTO> itemDTOs = itemConverter.convertItemListToItemDTOList(items);
        page.addObjects(itemDTOs);
        return page;
    }

    @Override
    @Transactional
    public ItemDTO findByUuid(String uuid) {
        Item item = getSafeItem(uuid);
        return itemConverter.convertItemToItemDTOWithDescription(item);

    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Item item = getSafeItem(uuid);
        itemRepository.remove(item);

    }

    @Override
    @Transactional
    public List<ItemDTO> findAll() {
        List<Item> items = itemRepository.findAll();
        return itemConverter.convertItemListToItemDTOList(items);
    }

    @Override
    @Transactional
    public ItemDTO add(ItemDTO itemDTO) {
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        itemRepository.persist(item);
        return itemConverter.convertItemToItemDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO replicate(String uuid) {
        Item item = getSafeItem(uuid);
        Item itemCopy = getItemCopy(item);
        itemRepository.persist(itemCopy);
        return itemConverter.convertItemToItemDTO(item);
    }

    @Override
    @Transactional
    public Item getSafeItem(String userUuid) {
        Optional<Item> optionalItem = itemRepository.findByUuid(userUuid);
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new ItemNotFoundException(String.format("An item with uuid %s was not found", userUuid));
        }
    }

    private Item getItemCopy(Item item) {
        Item itemCopy = new Item();
        itemCopy.setName(item.getName());
        ItemDescription itemDescription = getItemDescription(item);
        ItemDescription copyItemDescription = new ItemDescription();
        copyItemDescription.setDescription(itemDescription.getDescription());
        copyItemDescription.setItem(itemCopy);
        itemCopy.setItemDescription(copyItemDescription);
        itemCopy.setPrice(item.getPrice());
        return itemCopy;
    }
}