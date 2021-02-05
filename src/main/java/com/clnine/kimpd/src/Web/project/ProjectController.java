package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.project.models.GetProjectListRes;
import com.clnine.kimpd.src.Web.project.models.GetProjectsRes;
import com.clnine.kimpd.src.Web.project.models.PostProjectReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProjectController {
    private final JwtService jwtService;
    private final ProjectService projectService;
    private final ProjectProvider projectProvider;

    /**
     * [2021.02.01] 24. 프로젝트 추가 API
     * [POST] /projects
     * @param postProjectReq
     * @return
     */
    @ResponseBody
    @PostMapping("/projects")
    public BaseResponse<String> postProject(@RequestBody PostProjectReq postProjectReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        //todo 프로젝트 request validation 추가
        if(postProjectReq.getProjectName()==null || postProjectReq.getProjectName().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_NAME);
        }
        if(postProjectReq.getProjectMaker()==null || postProjectReq.getProjectMaker().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_MAKER);
        }
        if(postProjectReq.getProjectStartDate()==null||postProjectReq.getProjectStartDate().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_START_DATE);
        }
        if(postProjectReq.getProjectEndDate()==null||postProjectReq.getProjectEndDate().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_END_DATE);
        }
        //todo 첨부파일 처리 어떻게 할지?


        if(postProjectReq.getProjectDescription()==null||postProjectReq.getProjectDescription().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_DESCRIPTION);
        }
        if(postProjectReq.getProjectBudget()==null||postProjectReq.getProjectBudget().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_BUDGET);
        }

        try{
            projectService.PostProject(postProjectReq,userIdx);
            return new BaseResponse<>(SUCCESS_POST_PROJECT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @GetMapping("/projects")
    public BaseResponse<List<GetProjectsRes>> getProjects(

                                                          @RequestParam int page
                                                          ){
        //jwt 검증
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            List<GetProjectsRes> getProjectsResList = projectProvider.getProjectsResList(userIdx,page+1,9);
            return new BaseResponse<>(SUCCESS,getProjectsResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }


    }
    @ResponseBody
    @GetMapping("/project-list")
    public BaseResponse<List<GetProjectListRes>> getProjectList(){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            List<GetProjectListRes> getProjectsResList = projectProvider.getProjectListRes(userIdx);
            return new BaseResponse<>(SUCCESS,getProjectsResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    


}
