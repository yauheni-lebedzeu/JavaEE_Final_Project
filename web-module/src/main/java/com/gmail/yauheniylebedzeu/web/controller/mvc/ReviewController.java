package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADMIN_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CHANGE_VISIBILITY_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.DELETE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REVIEWS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserUuid;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = ADMIN_CONTROLLER_URL + REVIEWS_CONTROLLER_URL)
    public String getReviews(@RequestParam(defaultValue = "1") int pageNumber,
                             @RequestParam(defaultValue = "10") int pageSize, Model model) {
        PageDTO<ReviewDTO> page = reviewService.getReviewPage(pageNumber, pageSize, "additionDateTime desc");
        model.addAttribute("page", page);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "admin-reviews";
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + REVIEWS_CONTROLLER_URL +
            DELETE_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String delReviews(@PathVariable String uuid, @PathVariable String sourcePageNumber) {
        reviewService.removeByUuid(uuid);
        return "redirect:" + ADMIN_CONTROLLER_URL + REVIEWS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + REVIEWS_CONTROLLER_URL +
            CHANGE_VISIBILITY_CONTROLLER_URL + "/{sourcePageNumber}")
    public String changeVisibility(@RequestParam(required = false) List<String> checkedUuids,
                                   @RequestParam(required = false) List<String> previouslyCheckedUuids,
                                   @PathVariable String sourcePageNumber) {
        reviewService.changeVisibilityByUuids(checkedUuids, previouslyCheckedUuids);
        return "redirect:" + ADMIN_CONTROLLER_URL + REVIEWS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = REVIEWS_CONTROLLER_URL)
    public String getVisibleReviews(@RequestParam(defaultValue = "1") int pageNumber,
                                    @RequestParam(defaultValue = "10") int pageSize, Model model) {
        PageDTO<ReviewDTO> page = reviewService.getVisibleReviewsPage(pageNumber, pageSize, "additionDateTime desc");
        model.addAttribute("page", page);
        Optional<UserDTO> userPrincipal = getUserPrincipal();
        if(userPrincipal.isPresent()) {
            UserDTO user = userPrincipal.get();
            RoleDTOEnum role = user.getRole();
            String roleName = role.name();
            model.addAttribute("role", roleName);
        }
        return "reviews";
    }

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + REVIEWS_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String getReviewForm(ReviewDTO reviewDTO){
        return "review-form";
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + REVIEWS_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String addReview(@Valid ReviewDTO reviewDTO, BindingResult errors){
        if (errors.hasErrors()) {
            return "review-form";
        }
        String userUuid = getLoggedUserUuid();
        reviewService.addReview(userUuid, reviewDTO);
        return "redirect:" + REVIEWS_CONTROLLER_URL;
    }
}