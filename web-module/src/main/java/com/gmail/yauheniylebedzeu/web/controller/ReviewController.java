package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.ReviewService;
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

    @GetMapping(value = "/reviews/admin/{pageNumber}/{pageSize}")
    public String getReviews(@PathVariable int pageNumber,
                             @PathVariable int pageSize,
                             Model model) {
        Long countOfReviews = reviewService.getCountOfReviews();
        int startPosition = (pageNumber - 1) * pageSize;
        List<ReviewDTO> reviews = reviewService.getReviewList(startPosition, pageSize, "additionDate desc");
        model.addAttribute("reviews", reviews);
        int countOfPages = (int) (Math.ceil(countOfReviews / (double) pageSize));
        model.addAttribute("countOfPages", countOfPages);
        return "admin-reviews";
    }

    @PostMapping(value = "/reviews/admin/del/{uuid}")
    public String delReviews(@PathVariable String uuid) {
        System.out.println(uuid);
        reviewService.removeByUuid(uuid);
        return "redirect:/reviews/admin/1/10";
    }

    @PostMapping(value = "/reviews/admin/change-visibility")
    public String changeVisibility(@RequestParam(required = false) List<String> uuids) {
        if (uuids != null) {
            uuids.forEach(reviewService::changeVisibilityByUuid);
        }
        return "redirect:/reviews/admin/1/10";
    }

    @GetMapping(value = "/reviews/{pageNumber}/{pageSize}")
    public String getVisibleReviews(@PathVariable int pageNumber,
                                    @PathVariable int pageSize,
                                    Model model) {
        Long countOfReviews = reviewService.getCountOfVisible();
        int startPosition = (pageNumber - 1) * pageSize;
        List<ReviewDTO> reviews = reviewService.findAllVisible(startPosition, pageSize, "additionDate desc");
        model.addAttribute("reviews", reviews);
        int countOfPages = (int) (Math.ceil(countOfReviews / (double) pageSize));
        model.addAttribute("countOfPages", countOfPages);
        return "reviews";
    }

}
