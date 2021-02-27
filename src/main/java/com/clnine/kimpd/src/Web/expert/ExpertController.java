package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.expert.models.*;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;

import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;


@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class ExpertController {
    private final ExpertProvider expertProvider;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;
    private final ExpertService expertService;

    /**
     * [2021.02.07] 전문가 상세 조회 API
     * @param userIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/experts/{userIdx}")
    public BaseResponse<GetExpertRes> getExpert(@PathVariable(required = true,value = "userIdx")int userIdx){
        try{
            GetExpertRes getExpertRes = expertProvider.getExpertRes(userIdx);
            return new BaseResponse<>(SUCCESS,getExpertRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.02.11] 전문가 리스트 조회(검색) API
     */
    @PostMapping("/experts")
    @ResponseBody
    public BaseResponse<GetExpertsRes> getExperts(@RequestBody PostExpertsReq postExpertsReq){
        //todo validation 추가
        try{
            GetExpertsRes getUsersResList = expertProvider.findExperts(postExpertsReq);
            return new BaseResponse<>(SUCCESS,getUsersResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/users/{userIdx}/profile")
    @ResponseBody
    public BaseResponse<String> patchMyProfileForExpert(@RequestBody PatchMyExpertReq patchMyExpertReq,
                                                        @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(patchMyExpertReq.getIntroduce().length()>500){

        }
        if(patchMyExpertReq.getCareer().length()>500){

        }
        if(patchMyExpertReq.getEtc().length()>500){

        }


        try{
            expertService.patchMyExpert(patchMyExpertReq,userIdx);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/users/{userIdx}/profile")
    @ResponseBody
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
