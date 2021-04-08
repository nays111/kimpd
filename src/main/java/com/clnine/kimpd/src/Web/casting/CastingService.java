package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.alarm.AlarmRepository;
import com.clnine.kimpd.src.Web.alarm.models.Alarm;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.casting.models.PatchCastingReq;
import com.clnine.kimpd.src.Web.casting.models.PatchCastingStatusReq;
import com.clnine.kimpd.src.Web.casting.models.PostCastingReq;
import com.clnine.kimpd.src.Web.contract.ContractProvider;
import com.clnine.kimpd.src.Web.project.ProjectProvider;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.IOException;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CastingService {
    private final ProjectRepository projectRepository;
    private final CastingRepository castingRepository;
    private final CastingProvider castingProvider;
    private final AlarmRepository alarmRepository;
    private final UserInfoProvider userInfoProvider;
    private final ProjectProvider projectProvider;
    private final ContractProvider contractProvider;

    /**
     * 프로젝트를 입력하여 섭외 신청을 하는 경우 -> 프로젝트 동시 생성
     */
    @Transactional
    public void PostCasting(PostCastingReq postCastingReq,int userIdx,int expertIdx) throws BaseException {
        UserInfo userInfo=userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        UserInfo expertInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
        if(expertInfo.getUserType()==1 || expertInfo.getUserType()==2 || expertInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }
        /**
         * 프로젝트 저장
         */
        String projectName = postCastingReq.getProjectName();
        String projectMaker = postCastingReq.getProjectMaker();
        String projectStartDate = postCastingReq.getProjectStartDate();
        String projectEndDate = postCastingReq.getProjectEndDate();
        String projectManager = postCastingReq.getProjectManager();
        String projectDescription = postCastingReq.getProjectDescription();
        String projectFileURL = postCastingReq.getProjectFileURL();
        Project project = new Project(userInfo,projectName,projectMaker,projectStartDate,projectEndDate,projectManager,projectFileURL,projectDescription);
        try{
            projectRepository.save(project);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
        /**
         * 이미 해당 유저가 전문가에게 이 프로젝트 하자고 섭외신청을 한 경우
         */
        Casting existsCasting = null;
        try{
            existsCasting = castingProvider.retrieveCastingInfoByUserExpertProject(userInfo,expertInfo,project);
        }catch (BaseException exception){
            if(exception.getStatus()!= NOT_FOUND_CASTING){
                throw exception;
            }
        }
        if(existsCasting!=null){
            throw new BaseException(ALREADY_SEND_CASTING_TO_EXPERT_WITH_THIS_PROJECT);
        }

        String castingPrice = postCastingReq.getCastingPrice();
        String castingStartDate = postCastingReq.getCastingStartDate();
        String castingEndDate = postCastingReq.getCastingEndDate();
        String castingPriceDate = postCastingReq.getCastingPriceDate();
        String castingWork = postCastingReq.getCastingWork();
        String castingMessage = postCastingReq.getCastingMessage();

        Casting casting =new Casting(userInfo,expertInfo,project,
                castingPrice,castingStartDate,castingEndDate,castingWork,
                castingPriceDate,castingMessage);
        try{
            castingRepository.save(casting);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_CASTING);
        }

        String userNickname = userInfo.getNickname();
        String alarmMessage = userNickname+"님이 전문가님께 섭외 요청을 보내셨습니다.";
        Alarm alarm = new Alarm(expertInfo,alarmMessage);
        try{
            alarmRepository.save(alarm);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_ALARM);
        }
    }

    /**
     * 프로젝트 불러오기 한 이후 섭외 신청을 하는 경우
     */
    @Transactional
    public void PostCastingByProjectLoaded(PostCastingReq postCastingReq,int userIdx,int expertIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        /**
         * 전문가 객체 검색
         */
        UserInfo expertInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
        if(expertInfo.getUserType()==1 || expertInfo.getUserType()==2 || expertInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }

        /**
         * projectIdx로 Project 객체 찾아오기
         */
        Project project = projectProvider.retrieveProjectByProjectIdx(postCastingReq.getProjectIdx());
        if(project.getUserInfo().getUserIdx()!=userIdx){
            throw new BaseException(NOT_USER_PROJECT);
        }

        /**
         * 이미 이 전문가한테 이 프로젝트를 섭외 요청한 경우
         */
        Casting existsCasting=null;
        try{
            existsCasting = castingProvider.retrieveCastingInfoByUserExpertProject(userInfo,expertInfo,project);
        }catch (BaseException exception){
            if(exception.getStatus()!= NOT_FOUND_CASTING){
                throw exception;
            }
        }
        if(existsCasting!=null){
            throw new BaseException(ALREADY_SEND_CASTING_TO_EXPERT_WITH_THIS_PROJECT);
        }
        String castingPrice = postCastingReq.getCastingPrice();
        String castingStartDate = postCastingReq.getCastingStartDate();
        String castingEndDate = postCastingReq.getCastingEndDate();
        String castingPriceDate = postCastingReq.getCastingPriceDate();
        String castingWork = postCastingReq.getCastingWork();
        String castingMessage = postCastingReq.getCastingMessage();

        Casting casting =new Casting(userInfo,expertInfo,project,
                castingPrice,castingStartDate,castingEndDate,castingWork,
                castingPriceDate,castingMessage);
        try{
            castingRepository.save(casting);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_CASTING);
        }

        String userNickname = userInfo.getNickname();
        String alarmMessage = userNickname+"님이 전문가님께 섭외 요청을 보내셨습니다.";
        Alarm alarm = new Alarm(expertInfo,alarmMessage);
        try{
            alarmRepository.save(alarm);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_ALARM);
        }
    }


    /**
     * 재섭외 요청 하기
     * CASTING-STATUS 를 변경
     */
    public void patchCasting(int castingIdx, PatchCastingReq patchCastingReq,int userIdx) throws BaseException {
        Casting casting = castingProvider.retrieveCastingByCastingIdx(castingIdx);
        if(casting.getUserInfo().getUserIdx()!=userIdx){
            throw new BaseException(NO_CASTING);
        }
        String castingPrice = patchCastingReq.getCastingPrice();
        String castingStartDate = patchCastingReq.getCastingStartDate();
        String castingEndDate = patchCastingReq.getCastingEndDate();
        String castingPriceDate = patchCastingReq.getCastingPriceDate();
        String castingWork = patchCastingReq.getCastingWork();
        String castingMessage = patchCastingReq.getCastingMessage();

        casting.setCastingPrice(castingPrice);
        casting.setCastingStartDate(castingStartDate);
        casting.setCastingEndDate(castingEndDate);
        casting.setCastingPriceDate(castingPriceDate);
        casting.setCastingWork(castingWork);
        casting.setCastingMessage(castingMessage);
        casting.setCastingStatus(1); //재섭외 요청을 하게되면 다시 섭외중 상태로 변경됨
        try{
            castingRepository.save(casting);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_RECASTING);
        }

        String userNickname = casting.getUserInfo().getNickname();
        String alarmMessage = userNickname+"님이 전문가님께 섭외 요청을 보내셨습니다.";
        Alarm alarm = new Alarm(casting.getExpert(), alarmMessage);
        try{
            alarmRepository.save(alarm);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_ALARM);
        }
    }

    /**
     * 섭외수락 or 섭외거절 or 작업완료
     */
    @Transactional
    public void patchCastingStatus(int state, PatchCastingStatusReq patchCastingStatusReq,int userIdx) throws BaseException, IOException {
        Casting casting = castingProvider.retrieveCastingByCastingIdx(patchCastingStatusReq.getCastingIdx());
        if(casting.getExpert().getUserIdx()!=userIdx){
            throw new BaseException(NO_CASTING_FOR_YOU);
        }
        casting.setCastingStatus(state);
        try{
            castingRepository.save(casting);
        }catch (Exception ignored){
            throw new BaseException(FAILED_TO_UPDATE_CASTING_STATUS);
        }

        if(state==2){
            String expertNickname = casting.getExpert().getNickname();
            String alarmMessage = expertNickname+"전문가님께서 섭외 요청을 승인하셨습니다.";
            Alarm alarm = new Alarm(casting.getUserInfo(), alarmMessage);


            String contractUrl = contractProvider.makepdf(casting,12);
            casting.setContractFileUrl(contractUrl);
            try{
                castingRepository.save(casting);
            }catch(Exception ignored){
                throw new BaseException(FAILED_TO_SEND_ALARM);
            }

            try{
                alarmRepository.save(alarm);
            }catch(Exception ignored){
                throw new BaseException(FAILED_TO_SEND_ALARM);
            }
        }else if(state==3){
            String expertNickname = casting.getExpert().getNickname();
            String alarmMessage = expertNickname+"전문가님께서 섭외 요청을 거절하셨습니다.";
            Alarm alarm = new Alarm(casting.getUserInfo(), alarmMessage);
            try{
                alarmRepository.save(alarm);
            }catch(Exception ignored){
                throw new BaseException(FAILED_TO_SEND_ALARM);
            }
        }
    }
}
