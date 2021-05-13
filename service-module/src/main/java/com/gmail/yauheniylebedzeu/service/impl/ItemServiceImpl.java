package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ItemRepository;
import com.gmail.yauheniylebedzeu.repository.model.Item;
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
        if (pageNumber > countOfPages && countOfPages != 0) {
            pageNumber = countOfPages;
        }
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Item> items = itemRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ItemDTO> itemDTOs = itemConverter.convertItemListToItemDTOList(items);
        List<ItemDTO> itemsOnPage = page.getObjects();
        itemsOnPage.addAll(itemDTOs);
        return page;
    }

    @Override
    @Transactional
    public ItemDTO findByUuid(String uuid) {
        Optional<Item> optionalItem = itemRepository.findByUuid(uuid);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            return itemConverter.convertItemToItemDTOWithDescription(item);
        } else {
            throw new ItemNotFoundException(String.format("An item with uuid %s was not found in the database",
                    uuid));
        }
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Optional<Item> optionalItem = itemRepository.findByUuid(uuid);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            itemRepository.remove(item);
        } else {
            throw new ItemNotFoundException(String.format("An item with uuid %s was not found in the database",
                    uuid));
        }
    }
}
