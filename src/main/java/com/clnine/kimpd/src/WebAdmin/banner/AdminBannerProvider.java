package com.clnine.kimpd.src.WebAdmin.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.banner.models.*;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminGetUserRes;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminBannerProvider {

    @Autowired
    private final AdminBannerRepository adminBannerRepository;

    AdminBannerProvider(AdminBannerRepository adminBannerRepository){
        this.adminBannerRepository = adminBannerRepository;

    }

    /**
     * 광고 전체 조회
     * @return List<AdminGetBannersRes>
     * @throws BaseException
     */
    public List<AdminGetBannersRes> getBannerList() throws BaseException{
        List<AdminBanner> bannerList;
        try{
            bannerList = adminBannerRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_BANNERS);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return bannerList.stream().map(Banner ->{
            int bannerIdx = Banner.getBannerIdx();
            String bannerLink = Banner.getBannerLink();
            String bannerImageURL = Banner.getBannerImageURL();
            String startDate = sdf.format(Banner.getStartDate());
            String endDate = sdf.format(Banner.getEndDate());
            String status = Banner.getStatus();
            return new AdminGetBannersRes(bannerIdx,bannerLink,bannerImageURL, startDate, endDate, status);
        }).collect(Collectors.toList());
    }

    /**
     * 광고 상세 조회
     * @param bannerIdx
     * @return AdminGetBannerRes
     * @throws BaseException
     */
    public AdminGetBannersRes retrieveBannerInfo(int bannerIdx) throws BaseException {
        // 1. DB에서 bannerIdx로 UserInfo 조회
        AdminBanner adminBanner = retrieveBannerByBannerIdx(bannerIdx);
        if(adminBanner == null){
            throw new BaseException(FAILED_TO_GET_BANNER);
        }
        // 2. AdminGetBannersRes로 변환하여 return
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String bannerImageURL = adminBanner.getBannerImageURL();
        String bannerLink = adminBanner.getBannerLink();
        String startDate = sdf.format(adminBanner.getStartDate());
        String endDate = sdf.format(adminBanner.getEndDate());
        String status = adminBanner.getStatus();

        return new AdminGetBannersRes(bannerIdx, bannerImageURL, bannerLink, startDate, endDate, status);
    }

    /**
     * 회원 조회
     * @param bannerIdx
     * @return AdminBanner
     * @throws BaseException
     */
    public AdminBanner retrieveBannerByBannerIdx(int bannerIdx) throws BaseException {
        // 1. DB에서 AdminBanner 조회
        AdminBanner adminBanner;
        try {
            adminBanner = adminBannerRepository.findById(bannerIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_BANNER);
        }

        // 2. AdminBanner를 return
        return adminBanner;
    }

}
