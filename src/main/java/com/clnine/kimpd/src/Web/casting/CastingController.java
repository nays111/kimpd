package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.casting.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.ValidationRegex.isRegexDateType;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CastingController {
    private final JwtService jwtService;
    private final CastingService castingService;
    private final CastingProvider castingProvider;

    @ResponseBody @GetMapping("/castings-count")
    @Operation(summary="섭외 신청 횟수 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<CastingCountRes> getCastingsCount() {
        try {
            int userIdx = jwtService.getUserIdx();
            CastingCountRes castingCountRes= castingProvider.getCastingCount(userIdx);
            return new BaseResponse<>(SUCCESS, castingCountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody @GetMapping("/received-castings-count")
    @Operation(summary="받은 섭외 횟수 조회 API (전문가만 사용할 수 있습니다.)",description = "토큰이 필요합니다.")
    public BaseResponse<CastingCountRes> getReceivedCastingsCount() {
        try {
            int userIdx = jwtService.getUserIdx();
            CastingCountRes castingCountRes = castingProvider.getReceivedCastingCount(userIdx);
            return new BaseResponse<>(SUCCESS, castingCountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @GetMapping("/castings")
    @Operation(summary = "캐스팅 리스트 조회 API",description = "castingStatus(1:섭외중, 2:섭외완료, 3:섭외거절, 4:작업완료,5:전체), duration(0:전체,1:최근 3개월,2:최근 6개월), 토큰이 필요합니다.")
    public BaseResponse<GetMyCastingsRes> getCastings(@RequestParam(value = "castingStatus", required = true) Integer castingStatus,
                                                           @RequestParam(value = "duration", required = true) Integer duration,
                                                           @RequestParam(value = "page", required = true) Integer page) {
        if(castingStatus==null){
            return new BaseResponse<>(EMPTY_CASTING_STATUS);
        }
        if(castingStatus!=1 && castingStatus!=2 && castingStatus!=3 && castingStatus!=4 && castingStatus!=5){
            return new BaseResponse<>(WRONG_CASTING_STATUS_SEARCH);
        }
        if(duration==null){
            return new BaseResponse<>(EMPTY_DURATION);
        }
        if(duration!=1 && duration!=2 && duration!=0){
            return new BaseResponse<>(WRONG_DURATION);
        }
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try {
            int userIdx = jwtService.getUserIdx();
            GetMyCastingsRes getMyCastingsRes = castingProvider.getMyCastingRes(userIdx, duration, castingStatus, page, 6);
            return new BaseResponse<>(SUCCESS, getMyCastingsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @GetMapping("/received-castings")
    @Operation(summary = "받은 섭외 리스트 조회 API(전문가만 사용할 수 있습니다.)",description = "castingStatus(1:미확인 승인요청, 2:섭외승인, 3:섭외거절, 4:진행/완료내역,5:전체), duration(0:전체,1:최근 3개월,2:최근 6개월), 토큰이 필요합니다.")
    public BaseResponse<GetMyReceivedCastingsRes> getReceivedCastings(@RequestParam(value = "castingStatus", required = true) Integer castingStatus,
                                                                           @RequestParam(value = "duration", required = true) Integer duration,
                                                                           @RequestParam(value = "page", required = true) Integer page) {
        if(castingStatus==null){
            return new BaseResponse<>(EMPTY_CASTING_STATUS);
        }
        if(castingStatus!=1 && castingStatus!=2 && castingStatus!=3 && castingStatus!=4 && castingStatus!=5){
            return new BaseResponse<>(WRONG_CASTING_STATUS_SEARCH);
        }
        if(duration==null){
            return new BaseResponse<>(EMPTY_DURATION);
        }
        if(duration!=1 && duration!=2 && duration!=0){
            return new BaseResponse<>(WRONG_DURATION);
        }
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try {
            int userIdx = jwtService.getUserIdx();
            GetMyReceivedCastingsRes getMyReceivedCastingsRes  = castingProvider.getMyReceivedCastingRes(userIdx, duration, castingStatus, page, 6);
            return new BaseResponse<>(SUCCESS, getMyReceivedCastingsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @PatchMapping("/received-castings/response")
    @Operation(summary = "섭외 수락, 섭외 거절, 작업 완료 API")
    public BaseResponse<String> patchCastingStatus(@RequestParam(value = "castingStatus",required = true) int castingStatus,
                                                   @RequestBody PatchCastingStatusReq patchCastingStatusReq) throws IOException {
        /**
         *  2(섭외수락),3(섭외거절),4(작업완료)만 가능
         */
        if(castingStatus<2 && castingStatus>4){
            return new BaseResponse<>(WRONG_CASTING_STATUS);
        }
        try {
            int userIdx = jwtService.getUserIdx();
            castingService.patchCastingStatus(castingStatus, patchCastingStatusReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/received-castings/{castingIdx}")
    @Operation(summary="받은 섭외 상세내역 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetCastingRes> getReceivedCastingResByCastingIdx (@PathVariable(required = true,value = "castingIdx")int castingIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            GetCastingRes getCastingRes  = castingProvider.getCastingRes(castingIdx,userIdx);
            return new BaseResponse<>(SUCCESS,getCastingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/experts/{userIdx}/castings")
    @Operation(summary = "섭외 요청하기 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> postCasting(@RequestBody PostCastingReq postCastingReq,
                                            @PathVariable(required = true,value = "userIdx")int expertIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdx==expertIdx){
            return new BaseResponse<>(CANNOT_CASTING_YOURSELF);
        }
        /**
         * 프로젝트를 불러오지 않고 수기로 신규 신청하는 경우
         */
        if(postCastingReq.getProjectIdx()==null){
            if(postCastingReq.getProjectName()==null||postCastingReq.getProjectName().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_NAME);
            }
            if(postCastingReq.getProjectMaker()==null||postCastingReq.getProjectMaker().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_MAKER);
            }
            if(postCastingReq.getProjectStartDate()==null||postCastingReq.getProjectStartDate().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_START_DATE);
            }
            if (!isRegexDateType(postCastingReq.getProjectStartDate())) {
                return new BaseResponse<>(INVALID_PROJECT_START_DATE);
            }
            if(postCastingReq.getProjectEndDate()==null || postCastingReq.getProjectEndDate().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_END_DATE);
            }
            if(!isRegexDateType(postCastingReq.getCastingEndDate())){
                return new BaseResponse<>(INVALID_PROJECT_END_DATE);
            }
            if(postCastingReq.getProjectManager()==null || postCastingReq.getProjectManager().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_MANAGER);
            }
            if(postCastingReq.getProjectDescription()==null || postCastingReq.getProjectDescription().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_DESCRIPTION);
            }
            if(postCastingReq.getCastingPrice()==null || postCastingReq.getCastingPrice().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_PRICE);
            }
            if(postCastingReq.getCastingStartDate()==null || postCastingReq.getCastingStartDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_START_DATE);
            }
            if (!isRegexDateType(postCastingReq.getCastingStartDate())) {
                return new BaseResponse<>(INVALID_CASTING_START_DATE);
            }
            if(postCastingReq.getCastingEndDate()==null || postCastingReq.getCastingEndDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_END_DATE);
            }
            if(!isRegexDateType(postCastingReq.getCastingEndDate())){
                return new BaseResponse<>(INVALID_CASTING_END_DATE);
            }
            if(postCastingReq.getCastingPriceDate()==null||postCastingReq.getCastingPriceDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_PRICE_DATE);
            }
            if(!isRegexDateType(postCastingReq.getCastingPriceDate())){
                return new BaseResponse<>(INVALID_CASTING_PRICE_DATE);
            }
            if(postCastingReq.getCastingWork()==null || postCastingReq.getCastingWork().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_WORK);
            }
            if(postCastingReq.getCastingMessage()==null || postCastingReq.getCastingMessage().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_MESSAGE);
            }
            try{
                castingService.PostCasting(postCastingReq,userIdx,expertIdx);
                return new BaseResponse<>(SUCCESS);
            }catch (BaseException exception){
                return new BaseResponse<>(exception.getStatus());
            }
        }else{
            /**
             * 프로젝트를 불러오는 경우 => 섭외 정보만 입력하면 됨
             */
            if(postCastingReq.getCastingPrice()==null || postCastingReq.getCastingPrice().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_PRICE);
            }
            if(postCastingReq.getCastingStartDate()==null || postCastingReq.getCastingStartDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_START_DATE);
            }
            if (!isRegexDateType(postCastingReq.getCastingStartDate())) {
                return new BaseResponse<>(INVALID_CASTING_START_DATE);
            }
            if(postCastingReq.getCastingEndDate()==null || postCastingReq.getCastingEndDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_END_DATE);
            }
            if(!isRegexDateType(postCastingReq.getCastingEndDate())){
                return new BaseResponse<>(INVALID_CASTING_END_DATE);
            }
            if(postCastingReq.getCastingPriceDate()==null||postCastingReq.getCastingPriceDate().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_PRICE_DATE);
            }
            if(!isRegexDateType(postCastingReq.getCastingPriceDate())){
                return new BaseResponse<>(INVALID_CASTING_PRICE_DATE);
            }
            if(postCastingReq.getCastingWork()==null || postCastingReq.getCastingWork().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_WORK);
            }
            if(postCastingReq.getCastingMessage()==null || postCastingReq.getCastingMessage().length()==0){
                return new BaseResponse<>(EMPTY_CASTING_MESSAGE);
            }
            try{
                castingService.PostCastingByProjectLoaded(postCastingReq,userIdx,expertIdx);
                return new BaseResponse<>(SUCCESS);
            }catch (BaseException exception){
                return new BaseResponse<>(exception.getStatus());
            }
        }
    }


    @ResponseBody
    @GetMapping("/castings/{castingIdx}")
    @Operation(summary="섭외 요청 상세내역 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetCastingRes> getCastingResByCastingIdx (@PathVariable(required = true,value = "castingIdx")int castingIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            GetCastingRes getCastingRes  = castingProvider.getCastingRes(castingIdx,userIdx);
            return new BaseResponse<>(SUCCESS,getCastingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/castings/{castingIdx}/conditions")
    @Operation(summary = "섭외 조건 입력 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchCastingCondition(@PathVariable(required = true, value = "castingIdx")int castingIdx,
                                                      @RequestBody(required = true)PatchCastingReq patchCastingReq){
        if(patchCastingReq.getCastingPrice()==null || patchCastingReq.getCastingPrice().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE);
        }
        if(patchCastingReq.getCastingStartDate()==null || patchCastingReq.getCastingStartDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_START_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingStartDate())){
            return new BaseResponse<>(INVALID_CASTING_START_DATE);
        }
        if(patchCastingReq.getCastingEndDate()==null || patchCastingReq.getCastingEndDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_END_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingEndDate())){
            return new BaseResponse<>(INVALID_CASTING_END_DATE);
        }
        if(patchCastingReq.getCastingPriceDate()==null || patchCastingReq.getCastingPriceDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingPriceDate())){
            return new BaseResponse<>(INVALID_CASTING_PRICE_DATE);
        }
        try{
            int userIdx = jwtService.getUserIdx();
            castingService.postCastingCondition(castingIdx,patchCastingReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @PatchMapping("/castings/{castingIdx}/re-casting")
    @Operation(summary = "재섭외 요청 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchCastingStatus(@PathVariable(required = true, value = "castingIdx")int castingIdx,
                                                   @RequestBody(required = true)PatchCastingReq patchCastingReq){

        if(patchCastingReq.getCastingPrice()==null || patchCastingReq.getCastingPrice().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE);
        }
        if(patchCastingReq.getCastingStartDate()==null || patchCastingReq.getCastingStartDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_START_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingStartDate())){
            return new BaseResponse<>(INVALID_CASTING_START_DATE);
        }
        if(patchCastingReq.getCastingEndDate()==null || patchCastingReq.getCastingEndDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_END_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingEndDate())){
            return new BaseResponse<>(INVALID_CASTING_END_DATE);
        }
        if(patchCastingReq.getCastingPriceDate()==null || patchCastingReq.getCastingPriceDate().length()==0){
            return new BaseResponse<>(EMPTY_CASTING_PRICE_DATE);
        }
        if(!isRegexDateType(patchCastingReq.getCastingPriceDate())){
            return new BaseResponse<>(INVALID_CASTING_PRICE_DATE);
        }
        try{
            int userIdx = jwtService.getUserIdx();
            castingService.patchCasting(castingIdx,patchCastingReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
