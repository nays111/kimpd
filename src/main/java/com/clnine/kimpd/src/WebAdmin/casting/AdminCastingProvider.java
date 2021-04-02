package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.casting.models.*;
import com.clnine.kimpd.src.WebAdmin.review.AdminReviewRepository;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminReview;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminCastingProvider {

    @Autowired
    private final AdminCastingRepository adminCastingRepository;
    private final AdminReviewRepository adminReviewRepository;

    AdminCastingProvider(AdminCastingRepository adminCastingRepository, AdminReviewRepository adminReviewRepository){
        this.adminCastingRepository = adminCastingRepository;
        this.adminReviewRepository = adminReviewRepository;

    }

    /**
     * casting 전체 조회
     * @return List<AdminGetCastingsRes>
     * @throws BaseException
     */
    public List<AdminGetCastingsRes> getCastingList() throws BaseException{
        List<AdminCasting> castingList;

        try{
            castingList = adminCastingRepository.findAll();

        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_CASTINGS);
        }

        return castingList.stream().map(Casting ->{
            int castingIdx = Casting.getCastingIdx();
            String userNickname = Casting.getAdminUserInfo().getNickname();
            String expertNickname = Casting.getAdminExpertInfo().getNickname();
            String projectName = Casting.getAdminProject().getProjectName();
            String castingWork = Casting.getCastingWork();
            String castingStatus = null;
            if(Casting.getCastingStatus() == 1){
                castingStatus = "섭외중";
            }
            else if(Casting.getCastingStatus() == 2){
                castingStatus = "섭외완료";
            }
            else if(Casting.getCastingStatus() == 3){
                castingStatus = "섭외거절";
            }
            else if(Casting.getCastingStatus() == 4){
                castingStatus = "작업완료";
            }

            AdminReview adminReview = null;
            String reviewStatus = null;
            int reviewIdx = 0;
            adminReview = adminReviewRepository.findAdminReviewByAdminCastingAndStatus(Casting, "ACTIVE");
            if(adminReview == null){
                reviewIdx = 0;
                reviewStatus = "평가 대기";
            }
            else{
                reviewIdx = adminReview.getReviewIdx();
                reviewStatus = "평가 완료";
            }

            String status = Casting.getStatus();
          return new AdminGetCastingsRes(castingIdx, userNickname, expertNickname, projectName, castingWork, castingStatus, reviewIdx, reviewStatus, status);
        }).collect(Collectors.toList());
    }

    /**
     * Casting 상세 조회
     * @param castingIdx
     * @return AdminGetCastingRes
     * @throws BaseException
     */
    public AdminGetCastingRes retrieveCastingInfo(int castingIdx) throws BaseException {
        // 1. DB에서 castingIdx AdminCasting 조회
        AdminCasting adminCasting = retrieveCastingByCastingIdx(castingIdx);

        if(adminCasting == null){
            throw new BaseException(FAILED_TO_GET_CASTINGS);
        }

        String castingStatus = null;
        if(adminCasting.getCastingStatus() == 1){
            castingStatus = "섭외중";
        }
        else if(adminCasting.getCastingStatus() == 2){
            castingStatus = "섭외완료";
        }
        else if(adminCasting.getCastingStatus() == 3){
            castingStatus = "섭외거절";
        }
        else if(adminCasting.getCastingStatus() == 4){
            castingStatus = "프로젝트 완료";
        }

        // 2. AdminGetCastingRes 변환하여 return
        return new AdminGetCastingRes(adminCasting.getAdminProject().getProjectName(), adminCasting.getAdminProject().getProjectMaker(),
                adminCasting.getAdminProject().getProjectManager(), adminCasting.getAdminProject().getProjectDescription(),
                adminCasting.getAdminProject().getProjectFileURL(), adminCasting.getAdminUserInfo().getNickname(),
                adminCasting.getAdminExpertInfo().getNickname(), adminCasting.getCastingPrice(), adminCasting.getCastingPriceDate(),
                adminCasting.getCastingMessage(), adminCasting.getCastingStartDate(), adminCasting.getCastingEndDate(), adminCasting.getCastingWork(),
                castingStatus, adminCasting.getStatus());
    }

    /**
     * Casting 조회
     * @param castingIdx
     * @return AdminCasting
     * @throws BaseException
     */
    public AdminCasting retrieveCastingByCastingIdx(int castingIdx) throws BaseException {
        // 1. DB에서 AdminCasting 조회
        AdminCasting adminCasting;
        try {
            adminCasting = adminCastingRepository.findById(castingIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_CASTINGS);
        }

        // 2. AdminCasting return
        return adminCasting;
    }
}
