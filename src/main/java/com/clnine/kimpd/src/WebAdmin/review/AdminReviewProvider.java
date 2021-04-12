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
     * @param reviewIdx
     * @return AdminGetReviewRes
     * @throws BaseException
     */
    public AdminGetReviewRes retrieveReviewInfo(int reviewIdx) throws BaseException {
        // 1. DB에서 reviewIdx AdminReview 조회
        AdminReview adminReview = retrieveReviewByReviewIdx(reviewIdx);
        if(adminReview == null){
            throw new BaseException(FAILED_TO_GET_REVIEWS);
        }

        // 2. AdminGetReviewRes 변환하여 return
        String projectName = adminReview.getAdminCasting().getAdminProject().getProjectName();
        String evaluateUserNickname = adminReview.getEvaluateUserInfo().getNickname();
        String evaluatedUserNickname = adminReview.getEvaluatedUserInfo().getNickname();
        String startDate = adminReview.getAdminCasting().getCastingStartDate();
        String endDate = adminReview.getAdminCasting().getCastingEndDate();
        Float star = adminReview.getStar();
        String description = adminReview.getDescription();
        String status = adminReview.getStatus();

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
