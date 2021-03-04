package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.expert.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController @RequiredArgsConstructor @RequestMapping @CrossOrigin
public class ExpertController {
    private final ExpertProvider expertProvider;
    private final JwtService jwtService;
    private final ExpertService expertService;

    @ResponseBody @GetMapping("/experts/{userIdx}")
    @Operation(summary="전문가 상세 조회 API")
    public BaseResponse<GetExpertRes> getExpert(@PathVariable(required = true,value = "userIdx")int userIdx){
        try{
            GetExpertRes getExpertRes = expertProvider.getExpertRes(userIdx);
            return new BaseResponse<>(SUCCESS,getExpertRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/experts") @ResponseBody
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

    @PatchMapping("/users/{userIdx}/profile") @ResponseBody
    @Operation(summary="전문가 프로필 수정 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchMyProfileForExpert(@RequestBody PatchMyExpertReq patchMyExpertReq,
                                                        @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(patchMyExpertReq.getIntroduce().length()>500){
            return new BaseResponse<>(TOO_LONG_INTRODUCE);
        }
        if(patchMyExpertReq.getCareer().length()>500){
            return new BaseResponse<>(TOO_LONG_CAREER);
        }
        if(patchMyExpertReq.getEtc().length()>500){
            return new BaseResponse<>(TOO_LONG_ETC);
        }
        try{
            expertService.patchMyExpert(patchMyExpertReq,userIdx);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/users/{userIdx}/profile") @ResponseBody
    @Operation(summary="전문가 프로필 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMyExpertRes> getMyExpertRes(@PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetMyExpertRes getMyExpertRes = expertProvider.getMyExpertRes(userIdx);
            return new BaseResponse<>(SUCCESS,getMyExpertRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
