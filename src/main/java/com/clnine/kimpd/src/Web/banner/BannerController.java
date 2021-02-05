package com.clnine.kimpd.src.Web.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.banner.models.GetBannersRes;
import com.clnine.kimpd.src.Web.banner.models.GetHomeRes;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.category.models.GetJobCategoryParentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS_GET_HOME_RES;

@RestController
@RequestMapping(value="/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerProvider bannerProvider;
    private final CategoryProvider categoryProvider;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetHomeRes> getBanners() throws BaseException{
        List<GetBannersRes> getBannersResList;
        List<GetJobCategoryParentRes> getJobCategoryParentResList;

        try{
            getJobCategoryParentResList = categoryProvider.getJobCategoryParentResList();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            getBannersResList  = bannerProvider.getBannerList();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        GetHomeRes getHomeRes = new GetHomeRes(getBannersResList,getJobCategoryParentResList);
        return new BaseResponse<>(SUCCESS_GET_HOME_RES,getHomeRes);


    }

}
