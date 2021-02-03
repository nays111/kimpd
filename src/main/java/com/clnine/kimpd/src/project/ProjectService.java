package com.clnine.kimpd.src.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.project.models.PostProjectReq;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.UserInfoRepository;
import com.clnine.kimpd.src.user.models.UserInfo;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        String projectDescription = postProjectReq.getProjectDescription();
        String projectFileURL = postProjectReq.getProjectFileURL();
        String projectBudget = postProjectReq.getProjectBudget();

        Project project = new Project(userInfo, projectName, projectMaker, projectStartDate, projectEndDate, projectFileURL, projectBudget, projectDescription);
        System.out.println(userInfo.getUserIdx());
        try {
            projectRepository.save(project);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_PROJECT);
        }
    }
}
