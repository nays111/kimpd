package com.clnine.kimpd.src.WebAdmin.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import static com.clnine.kimpd.config.BaseResponseStatus.*;
import com.clnine.kimpd.src.WebAdmin.banner.models.*;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminGetUserRes;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminPatchUserReq;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminPostUserReq;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminPostUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminBannerController {
    private final AdminBannerProvider adminBannerProvider;
    private final AdminBannerService adminBannerService;

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

    /**
     * 광고 등록 API
     * [POST] /users
     * @RequestBody AdminPostBannerReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PostMapping("/banners")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> postBanners(@RequestBody AdminPostBannerReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getBannerLink() == null || parameters.getBannerLink().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_LINK);
        }

        if (parameters.getStartDate() == null || parameters.getStartDate().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_START_DATE);
        }

        if (parameters.getEndDate() == null || parameters.getEndDate().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_END_DATE);
        }

        if (parameters.getBannerImageURL() == null || parameters.getBannerImageURL().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_IMAGE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(parameters.getStartDate());
            date2 = sdf.parse(parameters.getEndDate());
        }catch(ParseException e){
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        if(compare > 0){
            return new BaseResponse<>(NOT_VALID_START_DATE);
        }

        // 2. Post Banner
        try {
            adminBannerService.createBanners(parameters);
            return new BaseResponse<>(SUCCESS_POST_BANNERS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 광고 수정 API
     * [PATCH] /banners
     * @RequestBody AdminPatchBannerReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/banners")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchBanners(@RequestBody AdminPatchBannerReq parameters) {
        if (parameters.getBannerLink() == null || parameters.getBannerLink().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_LINK);
        }

        if (parameters.getStartDate() == null || parameters.getStartDate().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_START_DATE);
        }

        if (parameters.getEndDate() == null || parameters.getEndDate().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_END_DATE);
        }

        if (parameters.getBannerImageURL() == null || parameters.getBannerImageURL().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_IMAGE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(parameters.getStartDate());
            date2 = sdf.parse(parameters.getEndDate());
        }catch(ParseException e){
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        if(compare > 0){
            return new BaseResponse<>(NOT_VALID_START_DATE);
        }
        
        try {
            adminBannerService.updateBanner(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_BANNERS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
