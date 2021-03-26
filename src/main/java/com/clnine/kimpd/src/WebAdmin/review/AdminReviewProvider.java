package com.clnine.kimpd.src.WebAdmin.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.casting.*;
import com.clnine.kimpd.src.WebAdmin.casting.AdminCastingProvider;
import com.clnine.kimpd.src.WebAdmin.casting.models.AdminCasting;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminGetReviewRes;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;


import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminReviewProvider {

    @Autowired
    private final AdminReviewRepository adminReviewRepository;
    private final AdminCastingRepository adminCastingRepository;
    private final AdminCastingProvider adminCastingProvider;

    AdminReviewProvider(AdminReviewRepository adminReviewRepository, AdminCastingRepository adminCastingRepository,
                        AdminCastingProvider adminCastingProvider){
        this.adminReviewRepository = adminReviewRepository;
        this.adminCastingRepository = adminCastingRepository;
        this.adminCastingProvider = adminCastingProvider;
    }

    /**
     * 평가 상세 조회
     * @param castingIdx
     * @return AdminGetReviewRes
     * @throws BaseException
     */
    public AdminGetReviewRes retrieveReviewInfo(int castingIdx) throws BaseException {
        // 1. DB에서 reviewIdx AdminReview 조회
        AdminCasting adminCasting = adminCastingProvider.retrieveCastingByCastingIdx(castingIdx);
        if(adminCasting == null){
            throw new BaseException(FAILED_TO_GET_REVIEWS);
        }
        if(adminCasting.getReview() == null){
            throw new BaseException(EMPTY_REVIEW);
        }
        // 2. AdminGetReviewRes 변환하여 return
        String projectName = adminCasting.getAdminProject().getProjectName();
        String evaluateUserNickname = adminCasting.getReview().getEvaluateUserInfo().getNickname();
        String evaluatedUserNickname = adminCasting.getReview().getEvaluatedUserInfo().getNickname();
        String startDate = adminCasting.getCastingStartDate();
        String endDate = adminCasting.getCastingEndDate();
        Float star = adminCasting.getReview().getStar();
        String description = adminCasting.getReview().getDescription();
        String status = adminCasting.getReview().getStatus();

        return new AdminGetReviewRes(projectName, evaluateUserNickname, evaluatedUserNickname, startDate, endDate, star, description, status);
    }

    /**
     * 평가 조회
     * @param reviewIdx
     * @return AdminReview
     * @throws BaseException
     */
    public AdminReview retrieveReviewByReviewIdx(int reviewIdx) throws BaseException {
        // 1. DB에서 AdminReview 조회
        AdminReview adminReview;
        try {
            adminReview = adminReviewRepository.findById(reviewIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEWS);
        }

        // 2. AdminReview return
        return adminReview;
    }
}
