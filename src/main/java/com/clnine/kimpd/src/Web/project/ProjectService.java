package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.project.models.PatchProjectReq;
import com.clnine.kimpd.src.Web.project.models.PostProjectReq;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_POST_PROJECT;

@Service @RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectProvider projectProvider;
    private final UserInfoProvider userInfoProvider;

    @Transactional
    public void postProject(PostProjectReq postProjectReq, int userIdx) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        String projectName = postProjectReq.getProjectName();
        String projectMaker = postProjectReq.getProjectMaker();
        String projectStartDate = postProjectReq.getProjectStartDate();
        String projectEndDate = postProjectReq.getProjectEndDate();
        String projectManager = postProjectReq.getProjectManager();
        String projectDescription = postProjectReq.getProjectDescription();
        String projectFileURL = postProjectReq.getProjectFileURL();
        String projectBudget = postProjectReq.getProjectBudget();
        Project project = new Project(userInfo, projectName, projectMaker, projectStartDate, projectEndDate, projectManager,projectFileURL, projectBudget, projectDescription);
        try {
            projectRepository.save(project);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
    }

    @Transactional
    public void updateProject(PatchProjectReq patchProjectReq,int projectIdx,int userIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        Project project = projectProvider.retrieveProjectByProjectIdx(projectIdx);
        if(project.getUserInfo()!=userInfo){
            throw new BaseException(BaseResponseStatus.NOT_USER_PROJECT);
        }
        project.setProjectName(patchProjectReq.getProjectName());
        project.setProjectMaker(patchProjectReq.getProjectMaker());
        project.setProjectStartDate(patchProjectReq.getProjectStartDate());
        project.setProjectEndDate(patchProjectReq.getProjectEndDate());
        project.setProjectManager(patchProjectReq.getProjectManager());
        project.setProjectDescription(patchProjectReq.getProjectDescription());
        project.setProjectFileURL(patchProjectReq.getProjectFileURL());
        project.setProjectBudget(patchProjectReq.getProjectBudget());
        try {
            projectRepository.save(project);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
    }

    @Transactional
    public void deleteProject(int projectIdx,int userIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        Project project = projectProvider.retrieveProjectByProjectIdx(projectIdx);
        if(project.getUserInfo()!=userInfo){
            throw new BaseException(BaseResponseStatus.NOT_USER_PROJECT);
        }
        project.setStatus("INACTIVE");
        try{
            projectRepository.save(project);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
    }
}
