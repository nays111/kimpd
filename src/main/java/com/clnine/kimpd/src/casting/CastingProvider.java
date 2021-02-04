package com.clnine.kimpd.src.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.casting.models.Casting;
import com.clnine.kimpd.src.casting.models.CastingCountRes;
import com.clnine.kimpd.src.casting.models.GetMyCastingRes;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.UserInfoProvider;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<GetMyCastingRes> getMyCastingRes(int casterIdx,int duration,Integer castingStatus) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(casterIdx);
        List<Casting> castingList = null;
        if(duration==1){ //전체기간 조회
            if(castingStatus==null){//전체조회
                castingList = castingRepository.findAllByUserInfoAndStatus(userInfo,"ACTIVE");
            }else{
                castingList = castingRepository.findAllByUserInfoAndCastingStatusAndStatus(userInfo,castingStatus,"ACTIVE");
            }
        }else if(duration==2){ //최근 3개월 조회
            if(castingStatus==null){//전체조회

            }else{

            }
        }else if(duration==3){ //최근 6개월 조회
            if(castingStatus==null){//전체조회

            }else{

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
