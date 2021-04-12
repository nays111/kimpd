package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.models.*;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
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
import java.util.Date;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CastingProvider {
    private final CastingRepository castingRepository;
    private final UserInfoProvider userInfoProvider;
    private final CategoryProvider categoryProvider;
    private final ReviewRepository reviewRepository;

    /**
     * 섭외 보낸 사람, 섭외 받은 전문가, 프로젝트 정보로 casting 찾기
     */
    public Casting retrieveCastingInfoByUserExpertProject(UserInfo user, UserInfo expert, Project project) throws BaseException{
        List<Casting> existsCastingList;
        try{
            existsCastingList = castingRepository.findByUserInfoAndExpertAndProjectAndCastingStatusNotAndStatus(user,expert,project,0,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        Casting casting;
        if(existsCastingList!=null && existsCastingList.size()>0){
            casting = existsCastingList.get(0);
        }else{
            throw new BaseException(NOT_FOUND_CASTING);
        }
        return casting;
    }

    /**
     * 섭외 인덱스로 casting 찾기
     */
    public Casting retrieveCastingByCastingIdx(int castingIdx) throws BaseException{
        Casting casting;
        try{
            casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(NOT_FOUND_CASTING);
        }
        if(casting==null){ throw new BaseException(NOT_FOUND_CASTING); }
        return casting;
    }

    /**
     * 클라이언트 회원이 섭외 보낸 횟수 조회
     */
    public CastingCountRes getCastingCount(int userIdx)throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        /**
         * castingGoing (섭외중) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 아직 섭외에 대한 응답을 안한 상태
         * castingAccepted (섭외 승인) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 섭외를 승인한 상태
         * castingRejected (섭외 거절) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 섭외를 거절한 상태
         * projectFinished (작업완료) : 클라이언트가 전문가에게 섭외 신창을 보냄 / 전문가가 작업 완료한 상태
         */
        int castingGoing = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,1,"ACTIVE");
        int castingAccepted = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,2,"ACTIVE");
        int castingRejected = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,3,"ACTIVE");
        int projectFinished = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,4,"ACTIVE");

        CastingCountRes castingCountRes = new CastingCountRes(castingGoing,castingAccepted,castingRejected,projectFinished);
        return castingCountRes;
    }


    /**
     * 전문가가 섭외 받은 횟수 조회
     */
    public CastingCountRes getReceivedCastingCount(int userIdx)throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        /**
         * 전문가가 아닌 경우
         */
        if(userInfo.getUserType()==1 || userInfo.getUserType()==2 || userInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }
        /**
         * castingGoing (섭외중) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 아직 섭외에 대한 응답을 안한 상태
         * castingAccepted (섭외 승인) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 섭외를 승인한 상태
         * castingRejected (섭외 거절) : 클라이언트가 전문가에게 섭외 신청을 보냄 / 전문가가 섭외를 거절한 상태
         * projectFinished (작업완료) : 클라이언트가 전문가에게 섭외 신창을 보냄 / 전문가가 작업 완료한 상태
         */
        int castingGoing = castingRepository.countAllByExpertAndCastingStatusAndStatus(userInfo,1,"ACTIVE");
        int castingAccepted = castingRepository.countAllByExpertAndCastingStatusAndStatus(userInfo,2,"ACTIVE");
        int castingRejected = castingRepository.countAllByExpertAndCastingStatusAndStatus(userInfo,3,"ACTIVE");
        int projectFinished = castingRepository.countAllByExpertAndCastingStatusAndStatus(userInfo,4,"ACTIVE");

        CastingCountRes castingCountRes = new CastingCountRes(castingGoing,castingAccepted,castingRejected,projectFinished);
        return castingCountRes;
    }


    /**
     * 내가 섭외 요청 보낸 리스트 조회 API
     */
    public GetMyCastingsRes getMyCastingRes(int casterIdx, Integer duration, Integer castingStatus, int page, int size) throws BaseException{

        /**
         * castedIdx : 섭외 요청을 보낸 사람
         */
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);

        List<Casting> castingList = null;
        int totalCount = 0;

        /**
         * 최신순 검색을 위한 paging (castingIdx 기준으로 내림차순 검색)
         */
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"castingIdx"));

        /**
         * 3달전, 6달전 필터링 검색을 위해 시간 형식 통일
         */
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


        if(duration==0){ //전체기간 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCastingStatusNotOrderByCastingIdxDesc(userInfo,"ACTIVE",0,pageable);
                totalCount = castingRepository.countAllByUserInfoAndStatusAndCastingStatusNotOrderByCastingIdxDesc(userInfo,"ACTIVE",0);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE");
            }
        }else if(duration==1){ //최근 3개월 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end1,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end1,now1);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1);
            }
        }else if(duration==2){ //최근 6개월 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end2,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end2,now1);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1,pageable);
                totalCount = castingRepository.countAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1);
            }
        }
        List<GetMyCastingDTO> getMyCastingResList = new ArrayList<>();

        for(int i=0;i<castingList.size();i++){
            Casting casting = castingList.get(i);
            UserInfo userInfo1 = casting.getExpert(); //전문가
            Project project=casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = userInfo1.getUserIdx();
            String nickname = userInfo1.getNickname();

            String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(userInfo1);

            String profileImageURL = userInfo1.getProfileImageURL();

            String introduce = userInfo1.getIntroduce();


            /**
             * 섭외 상태에 따른 반환 형식 지정
             */
            int castingStatus1 = casting.getCastingStatus();
            String castingStatusString = null;
            if(castingStatus1==1){
                castingStatusString="섭외중";
            }else if(castingStatus1==2){
                castingStatusString="섭외완료";
            }else if(castingStatus1==3){
                castingStatusString="섭외거절";
            }else if(castingStatus1==4){
                castingStatusString="작업완료";
            }

            String castingStartDate = casting.getCastingStartDate();
            String castingEndDate = casting.getCastingEndDate();
            String castingTerm = castingStartDate+"~"+castingEndDate;
            castingTerm = castingTerm.replace("/",".");
            String projectName = project.getProjectName();
            String castingPrice = casting.getCastingPrice();


            GetMyCastingDTO getMyCastingRes = new GetMyCastingDTO(userIdx,nickname,profileImageURL,userMainJobCategoryChildName,
                    introduce,castingIdx,castingStatusString,castingTerm,projectName,castingPrice);

            getMyCastingResList.add(getMyCastingRes);
        }
        GetMyCastingsRes getMyCastingsRes = new GetMyCastingsRes(totalCount,getMyCastingResList);
        return getMyCastingsRes;
    }

    /**
     * 받은 섭외 요청 리스트 조회 (보낸 것과 다름) ->전문가만 사용
     */
    public GetMyReceivedCastingsRes getMyReceivedCastingRes(int expertIdx, Integer duration, Integer castingStatus, int page, int size) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
        List<Casting> castingList = null;
        int totalCount = 0;

        /**
         * 최신순 검색을 위한 paging (castingIdx 기준으로 내림차순 검색)
         */
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"castingIdx"));

        /**
         * 3달전, 6달전 필터링 검색을 위해 시간 형식 통일
         */
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

        if(duration==0){ //전체기간 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusAndCastingStatusNotOrderByCastingIdxDesc(userInfo,"ACTIVE",0,pageable);
                totalCount = castingRepository.countAllByExpertAndStatusAndCastingStatusNotOrderByCastingIdxDesc(userInfo,"ACTIVE",0);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",pageable);
                totalCount = castingRepository.countAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE");
            }
        }else if(duration==1){ //최근 3개월 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end1,now1,pageable);
                totalCount = castingRepository.countAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end1,now1);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1,pageable);
                totalCount = castingRepository.countAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1);
            }
        }else if(duration==2){ //최근 6개월 조회
            if(castingStatus==5){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end2,now1,pageable);
                totalCount =castingRepository.countAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",0,end2,now1);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1,pageable);
                totalCount = castingRepository.countAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1);
            }
        }
        List<GetMyReceivedCastingDTO> getMyReceivedCastingDTOList = new ArrayList<>();

        for(int i=0;i<castingList.size();i++){
            Casting casting = castingList.get(i);
            UserInfo userInfo1 = casting.getUserInfo(); //섭외 요청을 보낸곳
            Project project=casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = userInfo1.getUserIdx();
            String nickname = userInfo1.getNickname();
            String profileImageURL = userInfo1.getProfileImageURL();


            /**
             * 섭외 상태에 따른 반환 형식 지정
             */
            int castingStatus1 = casting.getCastingStatus();

            String castingStatusString = null;
            if(castingStatus1==1){
                castingStatusString="미확인 승인요청";
            }else if(castingStatus1==2){
                castingStatusString="섭외승인";
            }else if(castingStatus1==3){
                castingStatusString="섭외거절";
            }else if(castingStatus1==4){
                castingStatusString="진행/완료내역";
            }
            String castingStartDate = casting.getCastingStartDate();
            String castingEndDate = casting.getCastingEndDate();
            String castingTerm = castingStartDate+"~"+castingEndDate;

            String projectName = project.getProjectName();
            String castingPrice = casting.getCastingPrice();

            Date createdAt = casting.getUpdatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yyyy.MM.dd");
            String castingDate = sDate.format(createdAt);//4


            /**
             * reviweStatus : 평가를 했는지 / 안했는지를 구분하기 위함
             * 전문가가 일반인에게 평가를 한 경우
             * 전문가 : expertIdx(userInfo), 일반인 : userInfo1
             * castingStatus = 4 일때, (작업완료 상태인 경우에만) reviewStatus에 값 삽입 (아닐때는 null)
             */
            String reviewStatus = null;
            if(castingStatus1==4){
                /**
                 * 전문가(usrInfo)가 일반인(userInfo1)에게 평가를 한경우 -> 전문가 : evaluateUserInfo, 일반인 : evaluatedUserInfo
                 */
                Review review = reviewRepository.findByEvaluatedUserInfoAndEvaluateUserInfoAndCastingAndStatus(userInfo1,userInfo,casting,"ACTIVE");
                if(review==null){
                    reviewStatus = "평가대기";
                }else{
                    reviewStatus = "평가완료";
                }
            }
            GetMyReceivedCastingDTO getMyReceivedCastingDTO = new GetMyReceivedCastingDTO(userIdx,nickname,profileImageURL,projectName,castingIdx,castingStatusString,reviewStatus,castingTerm,castingDate,castingPrice);
            getMyReceivedCastingDTOList.add(getMyReceivedCastingDTO);
        }
        GetMyReceivedCastingsRes getMyReceivedCastingsRes = new GetMyReceivedCastingsRes(totalCount,getMyReceivedCastingDTOList);
        return getMyReceivedCastingsRes;
    }


    /**
     * 섭외 상세내역 조회
     */
    public GetCastingRes getCastingRes(int castingIdx,int userIdx) throws BaseException{

        Casting casting  = retrieveCastingByCastingIdx(castingIdx);

        /**
         * 조회하려는 섭외 내용이 본인과 연관없는 경우 조회 불가
         */
        if(casting.getUserInfo().getUserIdx()!=userIdx && casting.getExpert().getUserIdx()!=userIdx){
            throw new BaseException(NO_CASTING);
        }

        /**
         * 프로젝트 정보
         */
        int projectIdx = casting.getProject().getProjectIdx();
        String projectName = casting.getProject().getProjectName();
        String projectMaker = casting.getProject().getProjectMaker();
        String projectStartDate = casting.getProject().getProjectStartDate();
        String projectEndDate = casting.getProject().getProjectEndDate();
        String projectManager = casting.getProject().getProjectManager();
        String projectDescription = casting.getProject().getProjectDescription();
        String projectFileURL = casting.getProject().getProjectFileURL();

        /**
         * 섭외 정보 불러오기
         */
        String castingPrice = casting.getCastingPrice();
        String castingStartDate = casting.getCastingStartDate();
        String castingEndDate = casting.getCastingEndDate();
        String castingPriceDate = casting.getCastingPriceDate();
        String castingWork = casting.getCastingWork();
        String castingMessage = casting.getCastingMessage();

        GetCastingRes getCastingRes = new GetCastingRes(castingIdx,projectIdx,projectName,projectMaker,projectStartDate,projectEndDate,projectManager,projectDescription,projectFileURL,castingPrice,castingStartDate,castingEndDate,castingPriceDate,castingWork,castingMessage);
        return getCastingRes;
    }
}
