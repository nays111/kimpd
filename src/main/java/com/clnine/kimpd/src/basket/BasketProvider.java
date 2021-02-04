package com.clnine.kimpd.src.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.project.models.GetProjectListRes;
import com.clnine.kimpd.src.project.models.GetProjectsRes;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.UserInfoProvider;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS_LIST;

@Service
@RequiredArgsConstructor
public class BasketProvider {
    UserInfoProvider userInfoProvider;
    BasketRepository basketRepository;


    /**
     * 장바구니에 담긴 프로젝트 리스트 조회 (projectIdx,projectName)
     * @param userIdx
     * @return
     * @throws BaseException
     */
    public List<GetProjectListRes> GetProjectList(int userIdx) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        List<Project> projectList;
        try{
            projectList = basketRepository.findByUserInfoAndStatus(userInfo,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS_LIST);
        }
        return projectList.stream().map(project -> {
            int projectIdx = project.getProjectIdx();
            String projectName = project.getProjectName();
            return new GetProjectListRes(projectIdx,projectName);
        }).collect(Collectors.toList());

    }
}
