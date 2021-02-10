package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.project.models.*;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin
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

        if(postProjectReq.getProjectFileURL()!=null){
            //todo 첨부파일 정규식 처리
        }

        if(postProjectReq.getProjectManager()==null||postProjectReq.getProjectManager().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_MANAGER);
        }

        if(postProjectReq.getProjectDescription()==null||postProjectReq.getProjectDescription().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_DESCRIPTION);
        }
        if(postProjectReq.getProjectBudget()==null||postProjectReq.getProjectBudget().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_BUDGET);
        }

        try{
            projectService.PostProject(postProjectReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * [2021.02.10] 25.프로젝트 수정 API
     * @param projectIdx
     * @param patchProjectReq
     * @return
     */

    @ResponseBody
    @PatchMapping("/project/{projectIdx}")
    public BaseResponse<String> updateProject(@PathVariable(required = true,value="projectIdx")int projectIdx,
                                              @RequestBody(required = true) PatchProjectReq patchProjectReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            projectService.UpdateProject(patchProjectReq,projectIdx);
            return new BaseResponse<>(SUCCESS);
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

    /**
     * [2021.02.05] 14. 프로젝트 리스트 조회 API (섭외 신청할 떄)
     * projectIdx, projectName 만 리턴
     * @return
     */
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

    /**
     * [2021.02.06] 15. 프로젝트 불러오기 API (섭외 신청할 때)
     * @param projectIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/project-list/{projectIdx}")
    public BaseResponse<GetProjectRes> getProjectWhenCasting(@PathVariable(required = true,value = "projectIdx")int projectIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetProjectRes getProjectRes = projectProvider.getProjectRes(userIdx,projectIdx);
            return new BaseResponse<>(SUCCESS,getProjectRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    


}
