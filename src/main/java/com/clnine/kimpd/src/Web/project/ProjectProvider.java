package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.project.models.*;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;

@Service
@RequiredArgsConstructor
public class ProjectProvider {
    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;


    /**
     * 프로젝트 수정할 때 쓰는 프로젝트 상세 조회
     */
    public GetMyProjectRes getMyProject(int projectIdx) throws BaseException{
        Project project;
        try{
            project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
        }catch (Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        String projectName = project.getProjectName();
        String projectMaker = project.getProjectMaker();
        String projectStartDate = project.getProjectStartDate();
        String projectEndDate = project.getProjectEndDate();
        String projectManager = project.getProjectManager();
        String projectDescription = project.getProjectDescription();
        String projectFileURL = project.getProjectFileURL();
        String projectBudget = project.getProjectBudget();
        GetMyProjectRes getMyProjectRes = new GetMyProjectRes(projectIdx,projectName,projectMaker,
                projectStartDate,projectEndDate,projectManager,projectBudget,
                projectDescription,projectFileURL);
        return getMyProjectRes;
    }



    public List<GetProjectsRes> getProjectsResList(int userIdx,int page,int size) throws BaseException{
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"projectIdx"));


        List<Project> projectList;
        try{
            projectList = projectRepository.findByUserInfoAndStatus(userInfo,"ACTIVE",pageable);

        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        return projectList.stream().map(project -> {
            int projectIdx = project.getProjectIdx();
            String projectName = project.getProjectName();
            String projectDescription = project.getProjectDescription();
            String projectStartDate = project.getProjectStartDate();
            String projectEndDate = project.getProjectEndDate();
            String projectBudget = project.getProjectBudget();
            return new GetProjectsRes(projectIdx,projectName,projectDescription,projectStartDate,projectEndDate,projectBudget);
        }).collect(Collectors.toList());

    }
    public List<GetProjectListRes> getProjectListRes(int userIdx) throws BaseException{
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        List<Project> projectList;
        try{
            projectList = projectRepository.findByUserInfoAndStatus(userInfo,"ACTIVE");

        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        return projectList.stream().map(project -> {
            int projectIdx = project.getProjectIdx();
            String projectName = project.getProjectName();
            return new GetProjectListRes(projectIdx,projectName);
        }).collect(Collectors.toList());
    }


    /**
     * 섭외 신청할 때 쓰임
     * @param userIdx
     * @param projectIdx
     * @return
     * @throws BaseException
     */
    public GetProjectRes getProjectRes(int userIdx,int projectIdx) throws BaseException{
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        Project project;
        try{
            project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
        }catch (Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        GetProjectRes getProjectRes = new GetProjectRes(project.getProjectIdx(),project.getProjectName(),project.getProjectMaker(),project.getProjectDescription(),project.getProjectStartDate(),project.getProjectEndDate());
        return getProjectRes;
    }




}
