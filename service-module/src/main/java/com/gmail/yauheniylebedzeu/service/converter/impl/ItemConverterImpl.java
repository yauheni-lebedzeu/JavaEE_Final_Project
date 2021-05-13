package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.ItemDescription;
import com.gmail.yauheniylebedzeu.service.converter.ItemConverter;
import com.gmail.yauheniylebedzeu.service.exception.ItemDescriptionNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemConverterImpl implements ItemConverter {

    @Override
    public Item convertItemDTOToItem(ItemDTO itemDTO) {
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setDescription(itemDTO.getDescription());
        itemDescription.setItem(item);
        item.setItemDescription(itemDescription);
        return item;
    }

    @Override
    public ItemDTO convertItemToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUniqueNumber(item.getId());
        itemDTO.setUuid(item.getUuid());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

    @Override
    public ItemDTO convertItemToItemDTOWithDescription(Item item) {
        ItemDTO itemDTO = convertItemToItemDTO(item);
        ItemDescription itemDescription = item.getItemDescription();
        if (Objects.isNull(itemDescription)) {
            throw new ItemDescriptionNotReceivedException(String.format("Couldn't get the description from the database +" +
                    " for the item with id = %s", item.getId()));
        }
        itemDTO.setDescription(itemDescription.getDescription());
        return itemDTO;
    }

    @Override
    public List<ItemDTO> convertItemListToItemDTOList(List<Item> items) {
        return items.stream()
                .map(this::convertItemToItemDTO)
                .collect(Collectors.toList());
    }
}
