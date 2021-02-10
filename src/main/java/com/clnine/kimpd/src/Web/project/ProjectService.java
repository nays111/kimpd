package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.project.models.PatchProjectReq;
import com.clnine.kimpd.src.Web.project.models.PostProjectReq;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_POST_PROJECT;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;

    public void PostProject(PostProjectReq postProjectReq, int userIdx) throws BaseException {

        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        System.out.println("userIdx:" + userInfo.getUserIdx());

        String projectName = postProjectReq.getProjectName();
        String projectMaker = postProjectReq.getProjectMaker();
        String projectStartDate = postProjectReq.getProjectStartDate();
        String projectEndDate = postProjectReq.getProjectEndDate();
        String projectManager = postProjectReq.getProjectManager();
        String projectDescription = postProjectReq.getProjectDescription();
        String projectFileURL = postProjectReq.getProjectFileURL();
        String projectBudget = postProjectReq.getProjectBudget();

        Project project = new Project(userInfo, projectName, projectMaker, projectStartDate, projectEndDate, projectManager,projectFileURL, projectBudget, projectDescription);
        System.out.println(userInfo.getUserIdx());
        try {
            projectRepository.save(project);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
    }

    public void UpdateProject(PatchProjectReq patchProjectReq,int projectIdx) throws BaseException{
        Project project;
        try{
            project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
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
}
