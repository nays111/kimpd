package com.clnine.kimpd.src.WebAdmin.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.faq.*;
import com.clnine.kimpd.src.WebAdmin.faq.models.*;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminGetReviewListRes;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminGetReviewRes;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminGetReviewsRes;
import com.clnine.kimpd.src.WebAdmin.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewProvider adminReviewProvider;
    private final AdminFaqService adminFaqService;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * 평가 전체 조회 API
     * [GET] /reviews
     * @return BaseResponse<AdminGetReviewListRes>
     */
//    @ResponseBody
//    @GetMapping("/reviews")
//    @CrossOrigin(origins = "*")
//    public BaseResponse<AdminGetReviewListRes> getReviews() throws BaseException{
//        List<AdminGetReviewsRes> getReviewsResList;
//
//        try{
//            if(adminUserInfoProvider.checkJWT() == false){
//                return new BaseResponse<>(INVALID_JWT);
//            }
//
//            getReviewsResList = adminReviewProvider.getReviewList();
//            AdminGetReviewListRes reviewList = new AdminGetReviewListRes(getReviewsResList);
//            return new BaseResponse<>(SUCCESS_READ_FAQS, reviewList);
//        }catch(BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

    /**
     * 평가 상세 조회 API
     * [GET] /reviews/:reviewIdx
     * @PathVariable reviewIdx
     * @return BaseResponse<AdminGetReviewRes>
     */
    @ResponseBody
    @GetMapping("/reviews/{reviewIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetReviewRes> getFaq(@PathVariable Integer reviewIdx) {
        if (reviewIdx == null || reviewIdx <= 0) {
            return new BaseResponse<>(EMPTY_REVIEW_IDX);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetReviewRes adminGetReviewRes = adminReviewProvider.retrieveReviewInfo(reviewIdx);
            return new BaseResponse<>(SUCCESS_READ_REVIEWS, adminGetReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
