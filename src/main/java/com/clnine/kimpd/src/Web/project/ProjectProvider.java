package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.project.models.*;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_POST_PROJECT;

@Service
@RequiredArgsConstructor
public class ProjectProvider {
    private final ProjectRepository projectRepository;
    private final UserInfoProvider userInfoProvider;

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
    @Transactional
    public GetMyProjectRes getMyProject(int projectIdx,int userIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        Project project = retrieveProjectByProjectIdx(projectIdx);

        if(project.getUserInfo()!=userInfo){
            throw new BaseException(BaseResponseStatus.NOT_USER_PROJECT);
        }

        String projectName = project.getProjectName();
        String projectMaker = project.getProjectMaker();
        String projectStartDate = project.getProjectStartDate();
        String projectEndDate = project.getProjectEndDate();
        String projectManager = project.getProjectManager();
        String projectDescription = project.getProjectDescription();
        String projectFileURL = project.getProjectFileURL();
        String projectBudget = project.getProjectBudget();

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();


        String currentTime = format.format(date);
        /**
         * 프로젝트를 조회했을 떄, 현재 날짜가 프로젝트 종료일자를 넘어가면 projectStatus 변경
         */
        if(currentTime.compareTo(project.getProjectEndDate()) > 0){
            project.setProjectStatus(0);
            projectRepository.save(project);
        }


        GetMyProjectRes getMyProjectRes = new GetMyProjectRes(projectIdx,projectName,projectMaker,
                projectStartDate,projectEndDate,projectManager,
                projectDescription,projectFileURL,projectBudget);
        return getMyProjectRes;
    }

    /**
     * 기간과 프로젝트 상태에 따른 프로젝트 리스트 조회
     */
    @Transactional(readOnly = true)
    public GetProjectsRes getProjectsResList(int userIdx, Integer sort, Integer duration,int projectStatus, int page, int size) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        Pageable pageable = PageRequest.of(page-1,size,Sort.by(Sort.Direction.DESC,"projectIdx"));//최신순
        Pageable pageable1 = PageRequest.of(page-1,size,Sort.by(Sort.Direction.ASC,"projectIdx"));//과거순

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        cal2.add(Calendar.MONTH,-3);
        cal3.add(Calendar.MONTH,-6);

        String threeMonthsAgoToday = formatter.format(cal2.getTime());
        String sixMonthsAgoToday = formatter.format(cal3.getTime());



        List<Project> projectList = null;
        int totalCount = 0;
        if(sort==0){//최신순
            if(duration==0){//전체 기간
                projectList = projectRepository.findByUserInfoAndProjectStatusAndStatus(userInfo,projectStatus,"ACTIVE",pageable);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatus(userInfo,projectStatus,"ACTIVE");
            }else if(duration==1){//프로젝트 시작일 기준 3개월 이내
                projectList = projectRepository.findAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",threeMonthsAgoToday,pageable);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",threeMonthsAgoToday);
            }else if(duration==2){//프로젝트 시작일 기준 6개월 이내
                projectList = projectRepository.findAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",sixMonthsAgoToday,pageable);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",sixMonthsAgoToday);
            }
        }else if(sort==1){//과거순
            if(duration==0){//전체 기간
                projectList = projectRepository.findByUserInfoAndProjectStatusAndStatus(userInfo,projectStatus,"ACTIVE",pageable1);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatus(userInfo,projectStatus,"ACTIVE");
            }else if(duration==1){//프로젝트 시작일 기준 3개월 이내
                projectList = projectRepository.findAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",threeMonthsAgoToday,pageable1);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",threeMonthsAgoToday);
            }else if(duration==2){//프로젝트 시작일 기준 6개월 이내
                projectList = projectRepository.findAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",sixMonthsAgoToday,pageable1);
                totalCount = projectRepository.countAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(userInfo,projectStatus,"ACTIVE",sixMonthsAgoToday);
            }
        }
        List<GetProjectsDTO> getProjectsList = new ArrayList<>();
        for(Project project : projectList){
            int projectIdx= project.getProjectIdx();
            String projectName = project.getProjectName();
            String projectDescription = project.getProjectDescription();

            String projectDate =null;
            if(project.getProjectStartDate()==null || project.getProjectEndDate()==null){
                projectDate = null;
            }else{
                projectDate = project.getProjectStartDate()+"~"+project.getProjectEndDate();
            }

            String projectBudget = project.getProjectBudget();
            GetProjectsDTO getProjectsDTO = new GetProjectsDTO(projectIdx,projectName,projectDescription,projectDate,projectBudget);
            getProjectsList.add(getProjectsDTO);
        }
        GetProjectsRes getProjectsRes = new GetProjectsRes(totalCount,getProjectsList);
        return  getProjectsRes;
    }

    /**
     * 장바구니에 담을 때 쓰이는 프로젝트 리스트 조회 (프로젝트 인덱스와 이름만)
     */
    @Transactional(readOnly = true)
    public List<GetProjectListRes> getProjectListRes(int userIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        List<Project> projectList;
        try{
            projectList = projectRepository.findByUserInfoAndProjectStatusAndStatus(userInfo,1,"ACTIVE");
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
     * 프로젝트 상세 조회
     */
    @Transactional
    public GetProjectRes getProjectRes(int userIdx,int projectIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        Project project = retrieveProjectByProjectIdx(projectIdx);
        if(project.getUserInfo()!=userInfo){
            throw new BaseException(BaseResponseStatus.NOT_USER_PROJECT);
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        String currentTime = format.format(date);
        /**
         * 프로젝트를 조회했을 떄, 현재 날짜가 프로젝트 종료일자를 넘어가면 projectStatus 변경
         * projectStatus 완료된거 -> 0, 완료되지 않으면 ->1
         */
        if(currentTime.compareTo(project.getProjectEndDate()) > 0){
            project.setProjectStatus(0);
            projectRepository.save(project);

        }

        GetProjectRes getProjectRes = new GetProjectRes(project.getProjectIdx(),project.getProjectName(),project.getProjectMaker(),project.getProjectDescription(),project.getProjectStartDate(),project.getProjectEndDate(),project.getProjectManager());
        return getProjectRes;
    }
}
