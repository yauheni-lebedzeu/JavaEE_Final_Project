package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.ItemDescription;
import com.gmail.yauheniylebedzeu.service.converter.ItemConverter;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemConverterImplTest {

    private final ItemConverter itemConverter;

    public ItemConverterImplTest() {
        itemConverter = new ItemConverterImpl();
    }

    @Test
    void shouldConvertItemDTOToItemAndGetNotNullObject() {
        ItemDTO itemDTO = new ItemDTO();
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertNotNull(item);
    }

    @Test
    void shouldConvertItemDTOToItemAndGetRightName() {
        ItemDTO itemDTO = new ItemDTO();
        String name = "Item";
        itemDTO.setName(name);
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertEquals(name, item.getName());
    }

    @Test
    void shouldConvertItemDTOToItemAndGetNotNullDescription() {
        ItemDTO itemDTO = new ItemDTO();
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertNotNull(item.getItemDescription());
    }

    @Test
    void shouldConvertItemDTOToItemAndGetRightDescription() {
        ItemDTO itemDTO = new ItemDTO();
        String description = "Description";
        itemDTO.setDescription(description);
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertEquals(description, item.getItemDescription().getDescription());
    }

    @Test
    void shouldConvertItemDTOToItemAndGetRightPrice() {
        ItemDTO itemDTO = new ItemDTO();
        BigDecimal price = new BigDecimal("10.15");
        itemDTO.setPrice(price);
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertEquals(price, item.getPrice());
    }

    @Test
    void shouldConvertItemDTOToItemAndGetRightQuantityInStock() {
        ItemDTO itemDTO = new ItemDTO();
        Integer quantityInStock = 50;
        itemDTO.setQuantityInStock(quantityInStock);
        Item item = itemConverter.convertItemDTOToItem(itemDTO);
        assertEquals(quantityInStock, item.getQuantityInStock());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetNotNullObject() {
        Item item = new Item();
        item.setItemDescription(new ItemDescription());
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertNotNull(itemDTO);
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightId() {
        Item item = new Item();
        item.setItemDescription(new ItemDescription());
        long id = 1L;
        item.setId(id);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(id, itemDTO.getUniqueNumber());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightUuid() {
        Item item = new Item();
        String uuid = UUID.randomUUID().toString();
        item.setUuid(uuid);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(uuid, itemDTO.getUuid());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightName() {
        Item item = new Item();
        String name = "Item";
        item.setName(name);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(name, itemDTO.getName());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightPrice() {
        Item item = new Item();
        BigDecimal price = new BigDecimal("100.99");
        item.setPrice(price);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(price, itemDTO.getPrice());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightQuantityInStock() {
        Item item = new Item();
        Integer quantityInStock = 50;
        item.setQuantityInStock(quantityInStock);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(quantityInStock, itemDTO.getQuantityInStock());
    }

    @Test
    void shouldConvertItemToItemDTOAndGetRightIsDeleted() {
        Item item = new Item();
        boolean isDeleted = false;
        item.setIsDeleted(isDeleted);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        assertEquals(isDeleted, itemDTO.getIsDeleted());
    }

    @Test
    void shouldConvertItemToItemDTOWithDescriptionAndGetNotNullObject() {
        Item item = new Item();
        item.setItemDescription(new ItemDescription());
        ItemDTO itemDTO = itemConverter.convertItemToItemDTOWithDescription(item);
        assertNotNull(itemDTO);
    }

    @Test
    void shouldConvertItemToItemDTOWithDescriptionAndGetRightDescription() {
        Item item = new Item();
        ItemDescription itemDescription = new ItemDescription();
        String description = "description";
        itemDescription.setDescription(description);
        item.setItemDescription(itemDescription);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTOWithDescription(item);
        assertEquals(description, itemDTO.getDescription());
    }

    @Test
    void shouldConvertEmptyItemListAndGetEmptyItemDTOList() {
        List<Item> items = Collections.emptyList();
        List<ItemDTO> itemDTOs = itemConverter.convertItemListToItemDTOList(items);
        assertTrue(itemDTOs.isEmpty());
    }

    @Test
    void shouldConvertEmptyItemListAndGetItemDTOList() {
        Item item = new Item();
        List<Item> items = Collections.singletonList(item);
        List<ItemDTO> itemDTOs = itemConverter.convertItemListToItemDTOList(items);
        assertEquals(1, itemDTOs.size());
    }

}
