package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;

import java.util.List;

public interface ItemService {

    PageDTO<ItemDTO> getItemPage(int pageNumber, int pageSize, String sortParameter);

    ItemDTO findByUuid(String uuid);

    void removeByUuid(String uuid);

    List<ItemDTO> findAll();

    ItemDTO add(ItemDTO itemDTO);

    ItemDTO replicate(String uuid);

    Item getSafeItem(String itemUuid);

    ItemDTO restore(String itemUuid);
}
