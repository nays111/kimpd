package com.clnine.kimpd.src.WebAdmin.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminGetReviewRes;
import com.clnine.kimpd.src.WebAdmin.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewProvider adminReviewProvider;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * 평가 상세 조회 API
     * [GET] /reviews/:castingIdx
     * @PathVariable reviewIdx
     * @return BaseResponse<AdminGetReviewRes>
     */
    @ResponseBody
    @GetMapping("/reviews/{reviewIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetReviewRes> getReview(@PathVariable Integer reviewIdx) {
        if (reviewIdx == null || reviewIdx < 0) {
            return new BaseResponse<>(EMPTY_REVIEW_IDX);
        }

        if (reviewIdx == 0){
            return new BaseResponse<>(EMPTY_REVIEW);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetReviewRes adminGetReviewRes = adminReviewProvider.retrieveReviewInfo(reviewIdx);
            return new BaseResponse<>(SUCCESS, adminGetReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
