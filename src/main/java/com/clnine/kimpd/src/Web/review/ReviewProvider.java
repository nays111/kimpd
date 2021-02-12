package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.review.models.GetMyReviewsRes;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewProvider {
    private final UserInfoProvider userInfoProvider;
    private final CastingRepository castingRepository;
    private final CategoryProvider categoryProvider;
    private final ReviewRepository reviewRepository;


    public List<GetMyReviewsRes> getMyReviewsRes(int casterIdx,Integer duration,Integer reviewStatus,int page,int size) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);
        System.out.println(userInfo.getUserIdx());
        List<Casting> castingList = null;
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"castingIdx"));
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        String now = formatter.format(cal.getTime());
        cal2.add(Calendar.MONTH,-3);
        cal3.add(Calendar.MONTH,-6);
        String end3 = formatter.format(cal2.getTime());
        String end6 = formatter.format(cal3.getTime());
        Timestamp end1 = Timestamp.valueOf(end3);
        Timestamp end2 = Timestamp.valueOf(end6);
        Timestamp now1 = Timestamp.valueOf(now);

        /**
         * 프로젝트 완료된것만 조회
         */
        if(duration==null){
            if(reviewStatus==null){ //전체 기간 -> 전체 조회
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,4,"ACTIVE",pageable);
            }else if(reviewStatus==1){ //전체 기간 -> 평가 대기 조회
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullOrderByCastingIdxDesc(userInfo,4,"ACTIVE",pageable);
            }else if(reviewStatus==2){ //전체 기간 -> 평가 완료 조회
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullOrderByCastingIdxDesc(userInfo,4,"ACTIVE",pageable);
            }
        }else if(duration==1){
            if(reviewStatus==null){
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end1,now1,pageable);
            }else if(reviewStatus==1){
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end1,now1,pageable);
            }else if(reviewStatus==2){
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end1,now1,pageable);
            }
        }else if(duration==2){
            if(reviewStatus==null){ //전체 기간 -> 6개월 조회
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end2,now1,pageable);
            }else if(reviewStatus==1){
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end2,now1,pageable);
            }else if(reviewStatus==2){
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end2,now1,pageable);
            }

        }

        List<GetMyReviewsRes> getMyReviewsResList = new ArrayList<>();

        for(int i=0;i<castingList.size();i++){
            Casting casting = castingList.get(i);
            UserInfo expertInfo = casting.getExpert();
            Project project = casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = expertInfo.getUserIdx();
            String nickname = expertInfo.getNickname();
            String categoryJobName = categoryProvider.getMainJobCategoryChild(expertInfo);
            String profileImageURL = expertInfo.getProfileImageURL();
            if(profileImageURL==null){
                profileImageURL="프로필 사진 없음";
            }
            String introduce = expertInfo.getIntroduce();
            if(introduce==""){
                introduce="소개 없음";
            }
            String castingStartDate = "20"+casting.getCastingStartDate();
            String castingEndDate = "20"+casting.getCastingEndDate();
            String castingTerm = castingStartDate+"~"+castingEndDate;
            castingTerm = castingTerm.replace("/",".");
            String projectName = project.getProjectName();
            System.out.println(projectName);
            String castingPrice = casting.getCastingPrice();
            String reviewState = null;
            float star = 0;
            if(casting.getReview()==null){
                reviewState = "평가대기";
                star = 0;
            }else{
                reviewState = "평가완료";
                star = casting.getReview().getStar();
            }

            GetMyReviewsRes getMyReviewsRes = new GetMyReviewsRes(userIdx,nickname,
                    profileImageURL,categoryJobName,introduce,castingIdx,castingTerm,projectName,castingPrice,reviewState,star);

            getMyReviewsResList.add(getMyReviewsRes);
        }
        return getMyReviewsResList;
    }
}
