package com.clnine.kimpd.src.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.casting.models.Casting;
import com.clnine.kimpd.src.casting.models.PostCastingReq;
import com.clnine.kimpd.src.project.ProjectRepository;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.UserInfoRepository;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CastingService {
    private final ProjectRepository projectRepository;
    private final UserInfoRepository userInfoRepository;
    private final CastingRepository castingRepository;
    private final CastingProvider castingProvider;
    @Transactional
    public void PostCasting(PostCastingReq postCastingReq,int userIdx,int expertIdx) throws BaseException {

        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        /**
         * 전문가 객체 검색
         */
        UserInfo expertInfo;
        try{
            expertInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(expertIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        /**
         * 프로젝트 저장
         */
        String projectName = postCastingReq.getProjectName();
        String projectMaker = postCastingReq.getProjectMaker();
        String projectStartDate = postCastingReq.getProjectStartDate();
        String projectEndDate = postCastingReq.getProjectEndDate();
        String projectDescription = postCastingReq.getProjectDescription();
        String projectFileURL = postCastingReq.getProjectFileURL();
        String projectBudget = postCastingReq.getProjectBudget();
        Project project = new Project(userInfo,projectName,projectMaker,projectStartDate,projectEndDate,projectFileURL,projectBudget,projectDescription);
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
    }


}
