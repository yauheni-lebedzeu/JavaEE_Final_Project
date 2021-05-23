package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;

import java.util.List;

public interface ItemConverter {

    Item convertItemDTOToItem(ItemDTO itemDTO);

    ItemDTO convertItemToItemDTO(Item item);

    ItemDTO convertItemToItemDTOWithDescription(Item item);

    List<ItemDTO> convertItemListToItemDTOList(List<Item> items);
}