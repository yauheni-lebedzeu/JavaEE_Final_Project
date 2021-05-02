package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "reviews/{pageNumber}/{pageSize}")
    public String getReviews(@PathVariable int pageNumber,
                             @PathVariable int pageSize,
                             Model model) {
        Long countOfReviews = reviewService.getCountOfReviews();
        int startPosition = (pageNumber - 1) * pageSize;
        List<ReviewDTO> reviews = reviewService.findAll(startPosition, pageSize, "additionDate desc");
        model.addAttribute("reviews", reviews);
        int countOfPages = (int) (Math.ceil(countOfReviews / (double) pageSize));
        model.addAttribute("countOfPages", countOfPages);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO user = userLogin.getUser();
            RoleDTOEnum roleEnum = user.getRole();
            String roleName = roleEnum.name();
            model.addAttribute("role", roleName);
        }
        return "reviews";
    }

    @PostMapping(value = "reviews/del/{uuid}")
    public String delReviews(@PathVariable String uuid) {
        System.out.println(uuid);
        reviewService.removeByUuid(uuid);
        return "redirect:/reviews/1/10";
    }

    @PostMapping(value = "reviews/change-visibility")
    public String changeVisibility(@RequestParam(required = false) List<String> uuids) {
        if (uuids != null) {
            uuids.forEach(reviewService::changeVisibilityByUuid);
        }
        return "redirect:/reviews/1/10";
    }
}
