package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.DELETE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REPLICATE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.SELLER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserRoleName;

@Controller
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

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
    public String delItem(@PathVariable String sourcePageNumber,
                          @PathVariable String uuid) {
        itemService.removeByUuid(uuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = ITEMS_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String getItem(@PathVariable String uuid,
                          @PathVariable String sourcePageNumber, Model model) {
        ItemDTO item = itemService.findByUuid(uuid);
        model.addAttribute("page", sourcePageNumber);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ITEMS_CONTROLLER_URL
            + "/{uuid}" + REPLICATE_CONTROLLER_URL + "/{sourcePageNumber}")
    public String replicateItem(@PathVariable String uuid, @PathVariable String sourcePageNumber) {
        itemService.replicate(uuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }
}