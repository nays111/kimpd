package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.expert.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class ExpertController {
    private final ExpertProvider expertProvider;
    private final JwtService jwtService;
    private final ExpertService expertService;

    @GetMapping("/experts/{userIdx}")
    @ResponseBody
    @Operation(summary="전문가 상세 조회 API")
    public BaseResponse<GetExpertRes> getExpert(@PathVariable(required = true,value = "userIdx")int userIdx){
        try{
            GetExpertRes getExpertRes = expertProvider.getExpertRes(userIdx);
            return new BaseResponse<>(SUCCESS,getExpertRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/experts")
    @ResponseBody
    @Operation(summary="전문가 리스트 조회(검색) API")
    public BaseResponse<GetExpertsRes> getExperts(@RequestBody PostExpertsReq postExpertsReq){
        if(postExpertsReq.getPage()==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        if(postExpertsReq.getSort()==null){
            return new BaseResponse<>(EMPTY_SORT_OPTION);
        }
        try{
            GetExpertsRes getUsersResList = expertProvider.findExperts(postExpertsReq);
            return new BaseResponse<>(SUCCESS,getUsersResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/users/{userIdx}/profile")
    @ResponseBody
    @Operation(summary="전문가 프로필 수정 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchMyProfileForExpert(@RequestBody PatchMyExpertReq patchMyExpertReq,
                                                        @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){ return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX); }
        if(patchMyExpertReq.getIntroduce().length()>500){
            return new BaseResponse<>(TOO_LONG_INTRODUCE);
        }
        if(patchMyExpertReq.getCareer().length()>500){
            return new BaseResponse<>(TOO_LONG_CAREER);
        }
        if(patchMyExpertReq.getEtc().length()>500){
            return new BaseResponse<>(TOO_LONG_ETC);
        }
        //todo validation 추가 필요
        try{
            expertService.patchMyExpert(patchMyExpertReq,userIdx);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/users/{userIdx}/profile")
    @ResponseBody
    @Operation(summary="전문가 프로필 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMyExpertRes> getMyExpertRes(@PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){
            return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX);
        }
        try{
            GetMyExpertRes getMyExpertRes = expertProvider.getMyExpertRes(userIdx);
            return new BaseResponse<>(SUCCESS,getMyExpertRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/users/{userIdx}/schedules")
    @ResponseBody
    @Operation(summary = "전문가 일정 조회 API",description = "토큰이 필요합니다. year와 month를 꼭 입력해주세요.(1~9월의 경우 01~09로 보내주세요.")
    public BaseResponse<GetMyExpertSchedulesManage> getMyExpertSchedule(@PathVariable(required = true,value = "userIdx")int userIdx,
                                                                              @RequestParam(required = true)String year,
                                                                              @RequestParam(required = true)String month){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){
            return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX);
        }
        if(year==null){
            return new BaseResponse<>(EMPTY_YEAR);
        }
        if(month==null){
            return new BaseResponse<>(EMPTY_MONTH);
        }
        try{
            GetMyExpertSchedulesManage getMyExpertSchedulesManage = expertProvider.getMyExpertSchedule(userIdx,year,month);
            return new BaseResponse<>(SUCCESS,getMyExpertSchedulesManage);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
