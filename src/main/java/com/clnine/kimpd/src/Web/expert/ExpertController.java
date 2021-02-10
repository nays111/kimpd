package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.expert.models.GetExpertRes;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.GetProjectRes;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.GetUsersRes;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @GetMapping("/experts")
    public BaseResponse<List<GetUsersRes>> getExperts(@RequestParam(required = false) String word,
                                                      @RequestParam(required = false) Integer jobCategoryParentIdx,
                                                      @RequestParam(required = false) Integer jobCategoryChildIdx,
                                                      @RequestParam(required = false) Integer genreCategoryIdx,
                                                      @RequestParam(required = false) String city){
        try{
            List<GetUsersRes> getUsersResList = userInfoProvider.findExperts(word,jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city);
            return new BaseResponse<>(SUCCESS,getUsersResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
