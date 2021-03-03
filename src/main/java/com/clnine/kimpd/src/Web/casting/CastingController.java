package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.casting.models.*;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.GetUserRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CastingController {
    private final JwtService jwtService;
    private final CastingService castingService;
    private final CastingProvider castingProvider;
    private final UserInfoProvider userInfoProvider;

    /**
     * [2021.02.05] 16-1 캐스팅 상태 건수 조회 API
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/castings-count")
    public BaseResponse<CastingCountRes> getCastingsCount() {
        int userIdx;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        CastingCountRes castingCountRes;
        try {
            castingCountRes = castingProvider.getCastingCount(userIdx);
            return new BaseResponse<>(SUCCESS, castingCountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 캐스팅 받은 횟수 조회 API
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/received-castings-count")
    public BaseResponse<CastingCountRes> getReceivedCastingsCount() {
        int userIdx;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        CastingCountRes castingCountRes;
        try {
            castingCountRes = castingProvider.getReceivedCastingCount(userIdx);
            return new BaseResponse<>(SUCCESS, castingCountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * [2021.02.07] 16. 캐스팅 리스트 조회 API
     *
     * @param castingStatus
     * @param duration
     * @param page
     * @return
     */
    @ResponseBody
    @GetMapping("/castings")
    public BaseResponse<List<GetMyCastingRes>> getCastings(@RequestParam(value = "castingStatus", required = false) Integer castingStatus,
                                                           @RequestParam(value = "duration", required = false) Integer duration,
                                                           @RequestParam(value = "page", required = true) int page) {
        int userIdx;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        List<GetMyCastingRes> getMyCastingResList;
        try {
            getMyCastingResList = castingProvider.getMyCastingRes(userIdx, duration, castingStatus, page, 6);
            return new BaseResponse<>(SUCCESS, getMyCastingResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 받은 캐스팅 리스트 조회 API
     * @param castingStatus
     * @param duration
     * @param page
     * @return
     */
    @ResponseBody
    @GetMapping("/received-castings")
    public BaseResponse<List<GetMyReceivedCastingRes>> getReceivedCastings(@RequestParam(value = "castingStatus", required = false) Integer castingStatus,
                                                                           @RequestParam(value = "duration", required = false) Integer duration,
                                                                           @RequestParam(value = "page", required = true) int page) {
        int userIdx;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        if(castingStatus!=null){
            if(castingStatus!=1 || castingStatus!=2 || castingStatus!=3 || castingStatus!=4){
                return new BaseResponse<>(WRONG_CASTING_STATUS_SEARCH);
            }
        }
        if(duration!=null){
            if(duration!=1 || duration!=2){
                return new BaseResponse<>(WRONG_DURATION);
            }
        }
        List<GetMyReceivedCastingRes> getMyReceivedCastingResList;
        try {
            getMyReceivedCastingResList = castingProvider.getMyReceivedCastingRes(userIdx, duration, castingStatus, page, 6);
            return new BaseResponse<>(SUCCESS, getMyReceivedCastingResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 섭외 수락, 섭외 거절, 작업 완료 API
     * @param castingStatus
     * @param patchCastingStatusReq
     * @return
     */
    @ResponseBody
    @PatchMapping("/received-castings/response")
    public BaseResponse<String> patchCastingStatus(@RequestParam(value = "castingStatus",required = true) int castingStatus,
                                                   @RequestBody PatchCastingStatusReq patchCastingStatusReq) {
        int userIdx;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        if(castingStatus<2 || castingStatus>4){
            return new BaseResponse<>(WRONG_CASTING_STATUS);
        }
        try {
            castingService.patchCastingStatus(castingStatus, patchCastingStatusReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 받은 섭외 상세 조회 API
     * @param castingIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/received-castings/{castingIdx}")
    public BaseResponse<GetCastingRes> getReceivedCastingResByCastingIdx (@PathVariable(required = true,value = "castingIdx")int castingIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetCastingRes getCastingRes  = castingProvider.getCastingRes(castingIdx);
            return new BaseResponse<>(SUCCESS,getCastingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
    /**
     * 섭외 요청하기  API
     * @param postCastingReq
     * @return
     */
    @ResponseBody
    @PostMapping("/experts/{userIdx}/castings")
    public BaseResponse<String> postCasting(@RequestBody PostCastingReq postCastingReq,
                                            @PathVariable(required = true,value = "userIdx")int expertIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
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
            if(postCastingReq.getProjectEndDate()==null || postCastingReq.getProjectEndDate().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_END_DATE);
            }
            if(postCastingReq.getProjectDescription()==null || postCastingReq.getProjectDescription().length()==0){
                return new BaseResponse<>(EMPTY_PROJECT_DESCRIPTION);
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
            try{
                castingService.PostCasting(postCastingReq,userIdx,expertIdx);
                return new BaseResponse<>(SUCCESS);
            }catch (BaseException exception){
                return new BaseResponse<>(exception.getStatus());
            }
        }else{
            /**
             * 프로젝트를 불러오는 경우
             */
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
            try{
                castingService.PostCastingByProjectLoaded(postCastingReq,userIdx,expertIdx);
                return new BaseResponse<>(SUCCESS);
            }catch (BaseException exception){
                return new BaseResponse<>(exception.getStatus());
            }
        }
    }

    /**
     * 섭외 요청 상세내역 조회 API
     * @param castingIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/castings/{castingIdx}")
    public BaseResponse<GetCastingRes> getCastingResByCastingIdx (@PathVariable(required = true,value = "castingIdx")int castingIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetCastingRes getCastingRes  = castingProvider.getCastingRes(castingIdx);
            return new BaseResponse<>(SUCCESS,getCastingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 재섭외 요청 API
     * @param castingIdx
     * @return
     */
    @ResponseBody
    @PatchMapping("/castings/{castingIdx}/re-casting")
    public BaseResponse<String> patchCastingStatus(@PathVariable(required = true, value = "castingIdx")int castingIdx,
                                                   @RequestBody(required = true)PatchCastingReq patchCastingReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            castingService.patchCasting(castingIdx,patchCastingReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
