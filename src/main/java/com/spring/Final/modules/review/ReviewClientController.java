package com.spring.Final.modules.review;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.review.projections.ReviewList;
import com.spring.Final.modules.shared.data.Countries;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class ReviewClientController extends ApiController {
    @Autowired
    private ReviewService service;

    @GetMapping("/my-reviews")
    public String listMyReviews(Model modelView,
                                Authentication authentication,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "8") int size) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int employeeId = (int) userDetails.getInformation().get("id");
        String role = (String) userDetails.getInformation().get("role");
        UserType userType = role.equals("employee") ? UserType.EMPLOYEE : UserType.EMPLOYER;

        PageImpl<ReviewList> list = this.service.listMyReviews(employeeId, userType, page, size);
        modelView.addAttribute("list", list);

        return "client/modules/reviews/my-reviews";
    }

    @GetMapping("/reviews")
    public String listOtherReviews(Model modelView,
                                   Authentication authentication,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "8") int size) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int employeeId = (int) userDetails.getInformation().get("id");

        PageImpl<ReviewList> list = this.service.listOtherReviews(employeeId, UserType.EMPLOYER, page, size);
        modelView.addAttribute("list", list);
        modelView.addAttribute("countries", Countries.getCountries());

        return "client/modules/reviews/reviews";
    }
}
