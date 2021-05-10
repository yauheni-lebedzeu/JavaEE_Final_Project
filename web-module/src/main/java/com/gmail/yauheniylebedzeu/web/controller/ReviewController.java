package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import lombok.AllArgsConstructor;
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

    @GetMapping(value = "/reviews/admin")
    public String getReviews(@RequestParam(defaultValue = "1") int pageNumber,
                             @RequestParam(defaultValue = "10") int pageSize,  Model model) {
        PageDTO<ReviewDTO> page = reviewService.getReviewPage(pageNumber, pageSize, "additionDate desc");
        model.addAttribute("page", page);
        return "admin-reviews";
    }

    @PostMapping(value = "/reviews/admin/del/{uuid}/{sourcePageNumber}")
    public String delReviews(@PathVariable String uuid, @PathVariable String sourcePageNumber) {
        reviewService.removeByUuid(uuid);
        return "redirect:/reviews/admin?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = "/reviews/admin/change-visibility/{sourcePageNumber}")
    public String changeVisibility(@RequestParam(required = false) List<String> checkedUuids,
                                   @RequestParam(required = false) List<String> previouslyCheckedUuids,
                                   @PathVariable String sourcePageNumber) {
        reviewService.changeVisibilityByUuids(checkedUuids, previouslyCheckedUuids);
        return "redirect:/reviews/admin?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = "/reviews")
    public String getVisibleReviews(@RequestParam(defaultValue = "1") int pageNumber,
                                    @RequestParam(defaultValue = "10") int pageSize,  Model model) {
        PageDTO<ReviewDTO> page = reviewService.getVisibleReviewsPage(pageNumber, pageSize, "additionDate desc");
        model.addAttribute("page", page);
        return "reviews";
    }

}
