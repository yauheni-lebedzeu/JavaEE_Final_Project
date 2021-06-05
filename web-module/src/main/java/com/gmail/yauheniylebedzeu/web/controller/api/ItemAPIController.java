package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.BindingResultConverter;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;

@RestController
@RequestMapping(value = API_CONTROLLER_URL)
@AllArgsConstructor
public class ItemAPIController {

    private final ItemService itemService;
    private final BindingResultConverter bindingResultConverter;

    @GetMapping(value = ITEMS_CONTROLLER_URL)
    public List<ItemDTO> getItems() {
        return itemService.findAll();
    }

    @GetMapping(value = ITEMS_CONTROLLER_URL + "/{uuid}")
    public ItemDTO getItem(@PathVariable String uuid) {
        return itemService.findByUuid(uuid);
    }

    @DeleteMapping(value = ITEMS_CONTROLLER_URL + "/{uuid}")
    public ResponseEntity<Void> deleteItem(@PathVariable String uuid) {
        itemService.removeByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = ITEMS_CONTROLLER_URL)
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemDTO item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsDTO errors = bindingResultConverter.convertBindingResultToErrorsDTO(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        itemService.add(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}