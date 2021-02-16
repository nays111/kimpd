package com.clnine.kimpd.src.WebAdmin.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.WebAdmin.banner.*;
import com.clnine.kimpd.src.WebAdmin.banner.models.*;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminPostUserReq;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminPostUserRes;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminBannerService {
    private final AdminBannerRepository adminBannerRepository;
    private final AdminBannerProvider adminBannerProvider;

    @Autowired
    public AdminBannerService(AdminBannerRepository adminBannerRepository, AdminBannerProvider adminBannerProvider) {
        this.adminBannerRepository = adminBannerRepository;
        this.adminBannerProvider = adminBannerProvider;
    }

    /**
     * 광고 등록 API
     * @param postBannerReq
     * @return AdminPostBannerReq
     * @throws BaseException
     */
    public void createBanners(AdminPostBannerReq postBannerReq) throws BaseException {

        String bannerImageURL = postBannerReq.getBannerImageURL();
        String bannerLink = postBannerReq.getBannerLink();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(postBannerReq.getStartDate());
            endDate = sdf.parse(postBannerReq.getEndDate());
        }catch(ParseException e){
            e.printStackTrace();
        }
        AdminBanner bannerInfo = new AdminBanner(bannerImageURL, bannerLink, startDate, endDate);

        // 3. 유저 정보 저장
        try {
            bannerInfo = adminBannerRepository.save(bannerInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_BANNER);
        }
        return;
    }

    /**
     * 광고 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchBannerReq
     * @return void
     * @throws BaseException
     */
    public void updateBanner(AdminPatchBannerReq adminPatchBannerReq) throws BaseException {
        AdminBanner adminBanner = null;

        try {
            adminBanner = adminBannerProvider.retrieveBannerByBannerIdx(adminPatchBannerReq.getBannerIdx());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            adminBanner.setBannerImageURL(adminPatchBannerReq.getBannerImageURL());
            adminBanner.setBannerLink(adminPatchBannerReq.getBannerLink());
            adminBanner.setStartDate(sdf.parse(adminPatchBannerReq.getStartDate()));
            adminBanner.setEndDate(sdf.parse(adminPatchBannerReq.getEndDate()));
            adminBanner.setStatus(adminPatchBannerReq.getStatus());
            adminBannerRepository.save(adminBanner);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_BANNER);
        }
    }

}