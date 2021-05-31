package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ItemRepository;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.ItemConverter;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Item copiedItem = getSafeItem(uuid);
        copiedItem.incrementCopyNumber();
        itemRepository.merge(copiedItem);
        Item itemCopy = getSafeItem(uuid);
        itemRepository.detach(itemCopy);
        String name = itemCopy.getName();
        String copyName = name + "[" + copiedItem.getCopyNumber() + "]";
        itemCopy.setName(copyName);
        itemCopy.setCopyNumber(0);
        itemCopy.setId(null);
        itemCopy.setUuid(null);
        itemRepository.persist(itemCopy);
        return itemConverter.convertItemToItemDTO(itemCopy);
    }

    @Override
    @Transactional
    public Item getSafeItem(String itemUuid) {
        Optional<Item> optionalItem = itemRepository.findByUuid(itemUuid);
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new ItemNotFoundModuleException(String.format("An item with uuid %s was not found", itemUuid));
        }
    }

    @Override
    @Transactional
    public ItemDTO restore(String itemUuid) {
        Item item = getSafeItem(itemUuid);
        item.setIsDeleted(false);
        itemRepository.merge(item);
        return itemConverter.convertItemToItemDTO(item);
    }
}