package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.project.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
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

    @ResponseBody
    @GetMapping("/projects/{projectIdx}")
    @Operation(summary="프로젝트 상세 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMyProjectRes> getMyProject(@PathVariable(value="projectIdx")int projectIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetMyProjectRes getMyProjectRes = projectProvider.getMyProject(projectIdx,userIdx);
            return new BaseResponse<>(SUCCESS,getMyProjectRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @PostMapping("/projects")
    @Operation(summary="프로젝트 추가 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> postProject(@RequestBody PostProjectReq postProjectReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
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
            projectService.postProject(postProjectReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/projects/{projectIdx}")
    @Operation(summary="프로젝트 수정 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> updateProject(@PathVariable(required = true,value="projectIdx")int projectIdx,
                                              @RequestBody(required = true) PatchProjectReq patchProjectReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            projectService.updateProject(patchProjectReq,projectIdx,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/projects/{projectIdx}/status")
    @Operation(summary="프로젝트 삭제 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> deleteProject(@PathVariable(required = true,value="projectIdx")int projectIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            projectService.deleteProject(projectIdx,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/projects")
    @Operation(summary="내 프로젝트 리스트 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetProjectsRes> getProjects(@RequestParam Integer page,
                                                    @RequestParam(value = "duration", required = false) Integer duration,
                                                    @RequestParam(value = "sort", required = false)Integer sort){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(sort!=null){
            if(sort!=1){
                return new BaseResponse<>(WRONG_SORT_OPTION);
            }
        }
        if(duration!=null){
            if(duration!=1 || duration!=2){
                return new BaseResponse<>(WRONG_DURATION);
            }
        }
        if(page==null){ return new BaseResponse<>(EMPTY_PAGE); }
        try{
            GetProjectsRes getProjectsRes = projectProvider.getProjectsResList(userIdx,sort,duration,page,9);
            return new BaseResponse<>(SUCCESS,getProjectsRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody @GetMapping("/project-list")
    @Operation(summary="프로젝트 리스트 조회 API(섭외신청할 때, projectIdx,projectName만 리턴)",description = "토큰이 필요합니다.")
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

    @ResponseBody @GetMapping("/project-list/{projectIdx}")
    @Operation(summary="프로젝트 불러오기 API(섭외신청할 떄)",description = "토큰이 필요합니다.")
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
