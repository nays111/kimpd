package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.project.models.GetProjectsRes;
import com.clnine.kimpd.src.Web.project.models.Project;
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




}
