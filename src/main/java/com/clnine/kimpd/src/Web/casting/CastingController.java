package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.casting.models.CastingCountRes;
import com.clnine.kimpd.src.Web.casting.models.GetCastingsRes;
import com.clnine.kimpd.src.Web.casting.models.GetMyCastingRes;
import com.clnine.kimpd.src.Web.casting.models.PostCastingReq;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.GetUserRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/castings")
@RequiredArgsConstructor
public class CastingController {
    private final JwtService jwtService;
    private final CastingService castingService;
    private final CastingProvider castingProvider;
    private final UserInfoProvider userInfoProvider;
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetCastingsRes>getCastings(@RequestParam(value="castingStatus",required = false)Integer castingStatus,
                                                   @RequestParam(value = "duration",required = false)Integer duration,
                                                   @RequestParam(value="page",required = true)int page){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        GetUserRes getUserRes;
        try{
            getUserRes = userInfoProvider.getUserRes(userIdx);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        CastingCountRes castingCountRes;
        try{
            castingCountRes = castingProvider.getCastingCount(userIdx);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        List<GetMyCastingRes> getMyCastingResList;
        try{
            getMyCastingResList = castingProvider.getMyCastingRes(userIdx,duration,castingStatus,page,6);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }


        GetCastingsRes getCastingsRes = new GetCastingsRes(getUserRes,castingCountRes,getMyCastingResList);
        return new BaseResponse<>(SUCCESS_READ_USER,getCastingsRes);

    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postCasting(@RequestBody PostCastingReq postCastingReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        int expertIdx = postCastingReq.getExpertIdx();

        if(postCastingReq.getProjectName()==null||postCastingReq.getProjectName().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_NAME);
        }
        if(postCastingReq.getProjectMaker()==null||postCastingReq.getProjectMaker().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_MAKER);
        }
        if(postCastingReq.getProjectStartDate()==null||postCastingReq.getProjectStartDate().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_START_DATE);
        }
        if(postCastingReq.getProjectEndDate()==null || postCastingReq.getProjectEndDate().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_END_DATE);
        }
        if(postCastingReq.getProjectDescription()==null || postCastingReq.getProjectDescription().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_DESCRIPTION);
        }
        if(postCastingReq.getProjectBudget()==null || postCastingReq.getProjectBudget().length()==0){
            return new BaseResponse<>(EMPTY_PROJECT_BUDGET);
        }
        //todo 첨부파일 처리
        if(postCastingReq.getProjectFileURL()!=null){
            if(postCastingReq.getProjectFileURL().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_FILE_URL);
            }
        }
        if(postCastingReq.getCastingPrice()==null || postCastingReq.getCastingPrice().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE);
        }
        if(postCastingReq.getCastingStartDate()==null || postCastingReq.getCastingStartDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_START_DATE);
        }
        if(postCastingReq.getCastingEndDate()==null || postCastingReq.getCastingEndDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_END_DATE);
        }
        if(postCastingReq.getCastingPriceDate()==null||postCastingReq.getCastingPriceDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE_DATE);
        }
        if(postCastingReq.getCastingWork()==null || postCastingReq.getCastingWork().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_WORK);
        }
        if(postCastingReq.getCastingMessage()==null || postCastingReq.getCastingMessage().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_MESSAGE);
        }
        /**
         * 프로젝트를 불러오지 않은 채로 수기로 입력하여 섭외 신청을 한 경우
         * 입력한 프로젝트 정보로 -> 프로젝트 생성
         * 프로젝트 생성해서 나온 객체를 섭외 엔티티에 저장
         * int PROJECT(USERIDX, PROJECT 해당) 함수
         * CASTING(USERIDX, EXPERTIDX, PROJECT, CASTING 해당) 함수수
        */
        try{
            castingService.PostCasting(postCastingReq,userIdx,expertIdx);
            return new BaseResponse<>(SUCCESS_POST_CASTING);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
