package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.casting.models.CastingCountRes;
import com.clnine.kimpd.src.Web.casting.models.GetMyCastingRes;
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
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.NOT_FOUND_CASTING;

@Service
@RequiredArgsConstructor
public class CastingProvider {
    private final CastingRepository castingRepository;
    private final UserInfoProvider userInfoProvider;
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

    public CastingCountRes getCastingCount(int userIdx)throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        int castingGoing = castingRepository.countAllByUserInfoAndAndCastingStatusAndStatus(userInfo,1,"ACTIVE");
        int castingAccepted = castingRepository.countAllByUserInfoAndAndCastingStatusAndStatus(userInfo,2,"ACTIVE");
        int castingRejected = castingRepository.countAllByUserInfoAndAndCastingStatusAndStatus(userInfo,3,"ACTIVE");
        int projectFinished = castingRepository.countAllByUserInfoAndAndCastingStatusAndStatus(userInfo,4,"ACTIVE");

        CastingCountRes castingCountRes = new CastingCountRes(castingGoing,castingAccepted,castingRejected,projectFinished);
        return castingCountRes;
    }

    public List<GetMyCastingRes> getMyCastingRes(int casterIdx,Integer duration,Integer castingStatus,int page,int size) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);
        List<Casting> castingList = null;
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        String now = null;
        String end3 = null;
        String end6 = null;
        now = formatter.format(cal.getTime());
        cal2.add(Calendar.MONTH,-3);
        cal3.add(Calendar.MONTH,-6);
        end3 = formatter.format(cal2.getTime());
        end6 = formatter.format(cal3.getTime());
        Timestamp end1 = Timestamp.valueOf(end3);
        Timestamp end2 = Timestamp.valueOf(end6);
        Timestamp now1 = Timestamp.valueOf(now);


        if(duration==null){ //전체기간 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatus(userInfo,"ACTIVE",pageable);
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatus(userInfo,castingStatus,"ACTIVE",pageable);
            }
        }else if(duration==1){ //최근 3개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCreatedAtBetween(userInfo,"ACTIVE",now1,end1,pageable);
            }else{
                castingList=castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetween(userInfo,castingStatus,"ACTIVE",now1,end1,pageable);
            }
        }else if(duration==2){ //최근 6개월 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatusAndCreatedAtBetween(userInfo,"ACTIVE",now1,end2,pageable);
            }else{
                castingList=castingRepository.findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetween(userInfo,castingStatus,"ACTIVE",now1,end1,pageable);
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



            String profileImageURL = userInfo1.getProfileImageURL();
            if(profileImageURL==null){
                profileImageURL="프로필 사진 없음";
            }
            String introduce = userInfo1.getIntroduce();
            if(introduce==null){
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


            GetMyCastingRes getMyCastingRes = new GetMyCastingRes(userIdx,nickname,profileImageURL,introduce,castingIdx,castingStatusString,castingTerm,projectName,castingPrice);
            getMyCastingResList.add(getMyCastingRes);
        }

        return getMyCastingResList;

    }
}
