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
     * [GET] /reviews/:reviewIdx
     * @PathVariable reviewIdx
     * @return BaseResponse<AdminGetReviewRes>
     */
    @ResponseBody
    @GetMapping("/reviews/{castingIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetReviewRes> getReview(@PathVariable Integer castingIdx) {
        if (castingIdx == null || castingIdx <= 0) {
            return new BaseResponse<>(EMPTY_REVIEW_IDX);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetReviewRes adminGetReviewRes = adminReviewProvider.retrieveReviewInfo(castingIdx);
            return new BaseResponse<>(SUCCESS_READ_REVIEWS, adminGetReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
