package com.gmail.yauheniylebedzeu.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.UploadService;
import com.gmail.yauheniylebedzeu.service.exception.FileUploadingException;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class UploadServiceImpl implements UploadService {

    private final XmlMapper xmlMapper;
    private final ItemService itemService;

    @Override
    @Transactional
    public List<ItemDTO> uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            List<ItemDTO> itemsFromFile = xmlMapper.readValue(bytes, new TypeReference<>() {
            });
            List<ItemDTO> items = new ArrayList<>();
            for (ItemDTO itemFromFile : itemsFromFile) {
                ItemDTO itemDTO = itemService.add(itemFromFile);
                items.add(itemDTO);
            }
            return items;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileUploadingException(String.format("Failed to upload file named \"%s\" (%s)",
                    file.getOriginalFilename(), e.getMessage()));
        }
    }
}