package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;

public interface ItemService {

    PageDTO<ItemDTO> getItemPage(int pageNumber, int pageSize, String sortParameter);

    ItemDTO findByUuid(String uuid);

    void removeByUuid(String uuid);
}
