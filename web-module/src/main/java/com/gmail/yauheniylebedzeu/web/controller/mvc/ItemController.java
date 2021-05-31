package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.exception.FileUploadingModuleException;
import com.gmail.yauheniylebedzeu.service.impl.UploadServiceImpl;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.web.controller.exception.ItemFileException;
import com.gmail.yauheniylebedzeu.web.validator.XmlValidator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.DELETE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REPLICATE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.RESTORE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.SELLER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.UPLOAD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserRoleName;

@Controller
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UploadServiceImpl uploadService;
    private final XmlValidator xmlValidator;

    @GetMapping(value = ITEMS_CONTROLLER_URL)
    public String getItems(@RequestParam(defaultValue = "1") int pageNumber,
                           @RequestParam(defaultValue = "10") int pageSize, Model model) {
        String roleName = getLoggedUserRoleName();
        model.addAttribute("role", roleName);
        PageDTO<ItemDTO> page = itemService.getItemPage(pageNumber, pageSize, "name asc");
        model.addAttribute("page", page);
        return "items";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ITEMS_CONTROLLER_URL + DELETE_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String deleteItem(@PathVariable String sourcePageNumber,
                             @PathVariable String uuid) {
        itemService.removeByUuid(uuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = ITEMS_CONTROLLER_URL + "/{uuid}")
    public String getItem(@PathVariable String uuid, Model model) {
        String roleName = getLoggedUserRoleName();
        model.addAttribute("role", roleName);
        ItemDTO item = itemService.findByUuid(uuid);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ITEMS_CONTROLLER_URL
            + "/{uuid}" + REPLICATE_CONTROLLER_URL + "/{sourcePageNumber}")
    public String replicateItem(@PathVariable String uuid, @PathVariable String sourcePageNumber) {
        itemService.replicate(uuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @SneakyThrows
    @PostMapping(value = SELLER_CONTROLLER_URL + ITEMS_CONTROLLER_URL + UPLOAD_CONTROLLER_URL)
    public String uploadItems(@RequestParam(required = false) MultipartFile file, Model model) {
        if (Objects.nonNull(file)) {
            try {
                xmlValidator.validateItemXMLFile(file);
                uploadService.uploadFile(file);
            } catch (ItemFileException | FileUploadingModuleException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return getItems(1, 10, model);
            }
        }
        model.addAttribute("message", "The file named \"" + file.getOriginalFilename() + "\" is uploaded successfully!");
        return getItems(1, 10, model);
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/{itemUuid}" + RESTORE_CONTROLLER_URL + "/{sourcePageNumber}" )
    public String restoreItem(@PathVariable String itemUuid,
                              @PathVariable String sourcePageNumber) {
        itemService.restore(itemUuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }
}