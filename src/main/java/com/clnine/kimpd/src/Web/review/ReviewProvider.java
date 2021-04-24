package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.review.models.GetMyReviewsListDTO;
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


    /**
     * 평가내역 리스트 조회 : 내가 평가한것들을 조회 (즉, 내가 섭외 요청한 것들)
     * 섭외 요청 한 사람 : casting -> userInfo
     *  - userInfo 기준에서 review 작성
     *  - userInfo 기준에서 review 작성 X
     *  (review 작성한 사람 : review -> evaluateUserInfo)
     *  : 만약 review 테이블에 ev
     * 섭외 요청 받은 사람 : casting -> expertInfo
     *
     */
    public GetMyReviewsRes getMyReviewsRes(int casterIdx, Integer duration, Integer reviewStatus, int page, int size) throws BaseException {
        //섭외 요청 보낸 사람
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);

        List<Casting> castingList = null;
        int totalCount = 0;

        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"castingIdx"));
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
         * 작업 완료된것만 조회
         */
        if(duration==0){

            if(reviewStatus==0){ //전체 기간 -> 전체 조회

                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,4,"ACTIVE",pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,4,"ACTIVE");

            }else if(reviewStatus==1){ //전체 기간 -> 평가 대기 조회

                castingList = castingRepository.findCastingNotWriteReview(userInfo,4,"ACTIVE",pageable);
                totalCount = castingRepository.countCastingNotWriteReview(userInfo,4,"ACTIVE");

            }else if(reviewStatus==2){ //전체 기간 -> 평가 완료 조회

                castingList = castingRepository.findCastingWriteReview(userInfo,4,"ACTIVE",pageable);
                totalCount = castingRepository.countCastingWriteReview(userInfo,4,"ACTIVE");

            }

        }else if(duration==1){ // 3개월 이내

            if(reviewStatus==0){

                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end1,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end1,now1);

            }else if(reviewStatus==1){

                castingList = castingRepository.findCastingNotWriteReviewInNMonth(userInfo,4,"ACTIVE",end1,now1,pageable);
                totalCount = castingRepository.countCastingNotWriteReviewInNMonth(userInfo,4,"ACTIVE",end1,now1);

            }else if(reviewStatus==2){

                castingList = castingRepository.findCastingWriteReviewInNMonth(userInfo,4,"ACTIVE",end1,now1,pageable);
                totalCount = castingRepository.countCastingWriteReviewInNMonth(userInfo,4,"ACTIVE",end1,now1);

            }

        }else if(duration==2){ // 6개월 이내

            if(reviewStatus==0){

                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end2,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,4,"ACTIVE",end2,now1);

            }else if(reviewStatus==1){

                castingList = castingRepository.findCastingNotWriteReviewInNMonth(userInfo,4,"ACTIVE",end2,now1,pageable);
                totalCount = castingRepository.countCastingNotWriteReviewInNMonth(userInfo,4,"ACTIVE",end2,now1);

            }else if(reviewStatus==2){

                castingList = castingRepository.findCastingWriteReviewInNMonth(userInfo,4,"ACTIVE",end2,now1,pageable);
                totalCount = castingRepository.countCastingWriteReviewInNMonth(userInfo,4,"ACTIVE",end2,now1);

            }
        }

        List<GetMyReviewsListDTO> getMyReviewsListDTOList = new ArrayList<>();

        for(Casting casting : castingList){
            UserInfo expert = casting.getExpert();
            Project project = casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = expert.getUserIdx();
            String nickname = expert.getNickname();
            String categoryJobName = categoryProvider.getMainJobCategoryChild(expert);
            String profileImageURL = expert.getProfileImageURL();
            String introduce = expert.getIntroduce();
            String castingTerm = casting.getCastingStartDate()+"~"+casting.getCastingEndDate();
            String projectName = project.getProjectName();
            String castingPrice = casting.getCastingPrice();
            String reviewState = null;
            float star = 0;

            Review review = reviewRepository.findByEvaluateUserInfoAndCastingAndStatus(userInfo,casting,"ACTIVE");
            if(review==null){
                reviewState="평가대기";
                star = 0;
            }else{
                reviewState = "평가완료";
                star = review.getStar();
            }
            GetMyReviewsListDTO getMyReviewsListDTO = new GetMyReviewsListDTO(userIdx,nickname,
                    profileImageURL,categoryJobName,introduce,castingIdx,castingTerm,projectName,castingPrice,reviewState,star);

            getMyReviewsListDTOList.add(getMyReviewsListDTO);
        }

        GetMyReviewsRes getMyReviewsRes = new GetMyReviewsRes(totalCount,getMyReviewsListDTOList);
        return getMyReviewsRes;
    }
}
