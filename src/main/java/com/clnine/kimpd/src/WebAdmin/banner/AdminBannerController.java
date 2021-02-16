package com.clnine.kimpd.src.WebAdmin.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import static com.clnine.kimpd.config.BaseResponseStatus.*;
import com.clnine.kimpd.src.WebAdmin.banner.models.*;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminGetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminBannerController {
    private final AdminBannerProvider adminBannerProvider;

    /**
     * 광고 전체 조회 API
     * [GET] /banners/
     * @return BaseResponse<AdminGetBannersListRes>
     */
    @ResponseBody
    @GetMapping("/banners")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetBannersListRes> getBanners() throws BaseException{
        List<AdminGetBannersRes> getBannersResList;

        try{
            getBannersResList = adminBannerProvider.getBannerList();
            AdminGetBannersListRes bannerList = new AdminGetBannersListRes(getBannersResList);
            return new BaseResponse<>(SUCCESS_READ_BANNERS, bannerList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 광고 상세 조회 API
     * [GET] /banners/:bannerIdx
     * @PathVariable bannerIdx
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/banners/{bannerIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetBannersRes> getUser(@PathVariable Integer bannerIdx) {
        if (bannerIdx== null || bannerIdx <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_IDX);
        }

        try {
            AdminGetBannersRes adminGetBannersRes = adminBannerProvider.retrieveBannerInfo(bannerIdx);
            return new BaseResponse<>(SUCCESS_READ_BANNERS, adminGetBannersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
