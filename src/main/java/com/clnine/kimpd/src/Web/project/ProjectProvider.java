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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;

@Service
@RequiredArgsConstructor
public class ProjectProvider {
    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;

    public Project retrieveProjectByProjectIdx(int projectIdx) throws BaseException{
        Project project;
        try{
            project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        if(project==null){
            throw new BaseException(FAILED_TO_GET_PROJECTS);
        }
        return project;
    }


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
                projectStartDate,projectEndDate,projectManager,
                projectDescription,projectFileURL,projectBudget);
        return getMyProjectRes;
    }



    public List<GetProjectsRes> getProjectsResList(int userIdx,Integer duration,int page,int size) throws BaseException{
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        Pageable pageable = PageRequest.of(page-1,size,Sort.by(Sort.Direction.DESC,"projectIdx"));
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        String now = formatter.format(cal.getTime());
        cal2.add(Calendar.MONTH,-3);
        cal3.add(Calendar.MONTH,-6);
        String end3 = formatter.format(cal2.getTime());
        String end6 = formatter.format(cal3.getTime());
        Timestamp end1 = Timestamp.valueOf(end3);
        Timestamp end2 = Timestamp.valueOf(end6);
        Timestamp now1 = Timestamp.valueOf(now);

        List<Project> projectList = null;
        if(duration==null){
            projectList = projectRepository.findByUserInfoAndStatus(userInfo,"ACTIVE",pageable);
        }else if(duration==1){
            projectList = projectRepository.findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(userInfo,"ACTIVE",end1,now1,pageable);
        }else if(duration==2){
            projectList = projectRepository.findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(userInfo,"ACTIVE",end2,now1,pageable);
        }


        return projectList.stream().map(project -> {
            int projectIdx = project.getProjectIdx();
            String projectName = project.getProjectName();
            String projectDescription = project.getProjectDescription();

            String projectDate = "20"+project.getProjectStartDate()+"~"+"20"+project.getProjectEndDate();
            projectDate = projectDate.replace("/",".");
            String projectBudget = project.getProjectBudget()+"원";
            return new GetProjectsRes(projectIdx,projectName,projectDescription,projectDate,projectBudget);
        }).collect(Collectors.toList());

    }

    /**
     * 섭외 신청할 떄 프로젝트 리스트 조회 (인덱스랑 이름만 리턴)
     * @param userIdx
     * @return
     * @throws BaseException
     */
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
