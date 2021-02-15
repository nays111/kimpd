package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.models.*;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.category.UserJobCategoryRepository;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.project.models.Project;
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

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.NOT_FOUND_CASTING;

@Service
@RequiredArgsConstructor
public class CastingProvider {
    private final CastingRepository castingRepository;
    private final UserInfoProvider userInfoProvider;
    private final CategoryProvider categoryProvider;

    public Casting retrieveCastingInfoByUserExpertProject(UserInfo user, UserInfo expert, Project project) throws BaseException{
        List<Casting> existsCastingList;
        try{
            existsCastingList = castingRepository.findByUserInfoAndExpertAndProjectAndStatus(user,expert,project,"ACTIVE");
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
     * 섭외 요청 - 섭외 횟수 조회 (섭외중:3건, 섭외완료3건, 섭외거절3건, 프로젝트완료3건)
     * @param userIdx
     * @return
     * @throws BaseException
     */
    public CastingCountRes getCastingCount(int userIdx)throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        int castingGoing = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,1,"ACTIVE");
        int castingAccepted = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,2,"ACTIVE");
        int castingRejected = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,3,"ACTIVE");
        int projectFinished = castingRepository.countAllByUserInfoAndCastingStatusAndStatus(userInfo,4,"ACTIVE");

        CastingCountRes castingCountRes = new CastingCountRes(castingGoing,castingAccepted,castingRejected,projectFinished);
        return castingCountRes;
    }

    /**
     * 섭외 받은 횟수 조회
     * @param userIdx
     * @return
     * @throws BaseException
     */
    public CastingCountRes getReceivedCastingCount(int userIdx)throws BaseException{
        UserInfo userinfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        int castingGoing = castingRepository.countAllByExpertAndCastingStatusAndStatus(userinfo,1,"ACTIVE");
        int castingAccepted = castingRepository.countAllByExpertAndCastingStatusAndStatus(userinfo,2,"ACTIVE");
        int castingRejected = castingRepository.countAllByExpertAndCastingStatusAndStatus(userinfo,3,"ACTIVE");
        int projectFinished = castingRepository.countAllByExpertAndCastingStatusAndStatus(userinfo,4,"ACTIVE");
        CastingCountRes castingCountRes = new CastingCountRes(castingGoing,castingAccepted,castingRejected,projectFinished);
        return castingCountRes;

    }


    /**
     * 내가 섭외 요청 보낸 리스트 조회 API
     * @param casterIdx
     * @param duration
     * @param castingStatus
     * @param page
     * @param size
     * @return
     * @throws BaseException
     */
    public List<GetMyCastingRes> getMyCastingRes(int casterIdx,Integer duration,Integer castingStatus,int page,int size) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);
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


        if(duration==null){ //전체기간 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusOrderByCastingIdxDesc(userInfo,"ACTIVE",pageable);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",pageable);
            }
        }else if(duration==1){ //최근 3개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",end1,now1,pageable);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1,pageable);
            }
        }else if(duration==2){ //최근 6개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",end2,now1,pageable);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1,pageable);
            }
        }
        List<GetMyCastingRes> getMyCastingResList = new ArrayList<>();

        for(int i=0;i<castingList.size();i++){
            Casting casting = castingList.get(i);
            UserInfo userInfo1 = casting.getExpert(); //전문가
            Project project=casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = userInfo1.getUserIdx();
            String nickname = userInfo1.getNickname();
            if(nickname==null){
                nickname="닉네임 없음";
            }

            String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(userInfo1);

            String profileImageURL = userInfo1.getProfileImageURL();
            if(profileImageURL==null){
                profileImageURL="프로필 사진 없음";
            }
            String introduce = userInfo1.getIntroduce();
            if(introduce==""){
                introduce="소개 없음";
            }
            int castingStatus1 = casting.getCastingStatus();

            String castingStatusString = null;
            if(castingStatus1==1){
                castingStatusString="섭외중";
            }else if(castingStatus1==2){
                castingStatusString="섭외완료";
            }else if(castingStatus1==3){
                castingStatusString="섭외거절";
            }else if(castingStatus1==4){
                castingStatusString="프로젝트 완료";
            }

            String castingStartDate = "20"+casting.getCastingStartDate();
            String castingEndDate = "20"+casting.getCastingEndDate();
            String castingTerm = castingStartDate+"~"+castingEndDate;
            castingTerm = castingTerm.replace("/",".");
            String projectName = project.getProjectName();
            String castingPrice = casting.getCastingPrice();


            GetMyCastingRes getMyCastingRes = new GetMyCastingRes(userIdx,nickname,profileImageURL,userMainJobCategoryChildName,
                    introduce,castingIdx,castingStatusString,castingTerm,projectName,castingPrice);

            getMyCastingResList.add(getMyCastingRes);
        }

        return getMyCastingResList;
    }

    /**
     * 받은 섭외 요청 리스트 조회 API (보낸 것과 다름)
     * @param expertIdx
     * @param duration
     * @param castingStatus
     * @param page
     * @param size
     * @return
     * @throws BaseException
     */
    public List<GetMyReceivedCastingRes> getMyReceivedCastingRes(int expertIdx,Integer duration,Integer castingStatus,int page,int size) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
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


        if(duration==null){ //전체기간 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusOrderByCastingIdxDesc(userInfo,"ACTIVE",pageable);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",pageable);
            }
        }else if(duration==1){ //최근 3개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",end1,now1,pageable);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end1,now1,pageable);
            }
        }else if(duration==2){ //최근 6개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByExpertAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,"ACTIVE",end2,now1,pageable);
            }else{
                castingList = castingRepository.findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(userInfo,castingStatus,"ACTIVE",end2,now1,pageable);
            }
        }
        List<GetMyReceivedCastingRes> getMyReceivedCastingResList = new ArrayList<>();

        for(int i=0;i<castingList.size();i++){
            Casting casting = castingList.get(i);
            UserInfo userInfo1 = casting.getUserInfo(); //섭외 요청을 보낸곳
            Project project=casting.getProject();

            int castingIdx = casting.getCastingIdx();
            int userIdx = userInfo1.getUserIdx();
            String nickname = userInfo1.getNickname();//todo 제작사가 들어가야함
            if(nickname==null){
                nickname="닉네임 없음";
            }


            String profileImageURL = userInfo1.getProfileImageURL();
            if(profileImageURL==null){
                profileImageURL="프로필 사진 없음";
            }
            String introduce = userInfo1.getIntroduce();
            if(introduce==""){
                introduce="소개 없음";
            }
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

            String castingStartDate = "20"+casting.getCastingStartDate();
            String castingEndDate = "20"+casting.getCastingEndDate();
            String castingTerm = castingStartDate+"~"+castingEndDate;
            castingTerm = castingTerm.replace("/",".");
            String projectName = project.getProjectName();
            String castingPrice = casting.getCastingPrice();
            Date createdAt = casting.getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yyyy.MM.dd");
            String castingDate = sDate.format(createdAt);//4



            GetMyReceivedCastingRes getMyReceivedCastingRes = new GetMyReceivedCastingRes(userIdx,nickname,profileImageURL,projectName,castingIdx,castingStatusString,castingTerm,castingDate,castingPrice);

            getMyReceivedCastingResList.add(getMyReceivedCastingRes);
        }

        return getMyReceivedCastingResList;
    }





    /**
     * 섭외 상세내역 조회
     * @param castingIdx
     * @return
     * @throws BaseException
     */
    public GetCastingRes getCastingRes(int castingIdx) throws BaseException{
        Casting casting;
        try{
            casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        int projectIdx = casting.getProject().getProjectIdx();
        String projectName = casting.getProject().getProjectName();
        String projectMaker = casting.getProject().getProjectMaker();
        String projectStartDate = casting.getProject().getProjectStartDate();
        String projectEndDate = casting.getProject().getProjectEndDate();
        String projectManager = casting.getProject().getProjectManager();
        String projectDescription = casting.getProject().getProjectDescription();
        String projectFileURL = casting.getProject().getProjectFileURL();
        if(projectFileURL==null){
            projectFileURL="업로드한 프로젝트 파일이 없습니다.";
        }
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
