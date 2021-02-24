package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.expert.models.GetExpertRes;
import com.clnine.kimpd.src.Web.expert.models.GetExpertsRes;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import lombok.RequiredArgsConstructor;
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
     * @param word
     * @param jobCategoryParentIdx
     * @param jobCategoryChildIdx
     * @param genreCategoryIdx
     * @param city
     * @param minimumCastingPrice
     * @param page
     * @param sort
     * @return
     */
    @GetMapping("/experts")
    public BaseResponse<GetExpertsRes> getExperts(@RequestParam(required = false) String word,
                                                      @RequestParam(required = false) List<Long> jobCategoryParentIdx,
                                                      @RequestParam(required = false) List<Long> jobCategoryChildIdx,
                                                      @RequestParam(required = false) List<Long> genreCategoryIdx,
                                                      @RequestParam(required = false) List<String> city,
                                                      @RequestParam(required = false) String minimumCastingPrice,
                                                      @RequestParam(required = true)int page,
                                                      @RequestParam(required = true)int sort){
        try{
            GetExpertsRes getUsersResList = expertProvider.findExperts(word,jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,minimumCastingPrice,page,sort);
            return new BaseResponse<>(SUCCESS,getUsersResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
