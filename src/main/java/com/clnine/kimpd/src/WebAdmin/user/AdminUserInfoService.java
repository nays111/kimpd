package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminUserInfoService {
    private final AdminUserInfoRepository adminUserInfoRepository;
    private final WebAdminInfoRepository webAdminInfoRepository;
    private final AdminUserInfoProvider adminUserInfoProvider;
    private final JwtService jwtService;

    @Autowired
    public AdminUserInfoService(AdminUserInfoRepository userInfoRepository, WebAdminInfoRepository webAdminInfoRepository, AdminUserInfoProvider adminUserInfoProvider, JwtService jwtService) {
        this.adminUserInfoRepository = userInfoRepository;
        this.webAdminInfoRepository = webAdminInfoRepository;
        this.adminUserInfoProvider = adminUserInfoProvider;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입
     * @param postAdminUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public AdminPostUserRes createUserInfo(AdminPostUserReq postAdminUserReq) throws BaseException {
        WebAdmin existsWebAdminInfo = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsWebAdminInfo = adminUserInfoProvider.retrieveUserInfoByWebAdminId(postAdminUserReq.getId());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsWebAdminInfo != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String id = postAdminUserReq.getId();
        String password;

        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postAdminUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        WebAdmin admin = new WebAdmin(id, password);

        // 3. 유저 정보 저장
        try {
            admin = webAdminInfoRepository.save(admin);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
        }

        // 4. JWT 생성
        String jwt = jwtService.createWebAdminJwt(admin.getId());

        // 5. UserInfoLoginRes로 변환하여 return
        String webAdminId = admin.getId();
        return new AdminPostUserRes(webAdminId, jwt);
    }

    /**
     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchUserReq
     * @return void
     * @throws BaseException
     */
    public void updateUserInfo(AdminPatchUserReq adminPatchUserReq) throws BaseException {
        AdminUserInfo adminUserInfo = null;
        int userType = 0;

        try {
            adminUserInfo = adminUserInfoProvider.retrieveUserInfoByUserId(adminPatchUserReq.getUserIdx());
            if (!adminPatchUserReq.getUserType().equals("전문가")) {
                userType = 1;
            }
            else{
                userType = 3;
            }
            adminUserInfo.setUserType(userType);
            adminUserInfo.setId(adminPatchUserReq.getId());
            adminUserInfo.setEmail(adminPatchUserReq.getEmail());
            adminUserInfo.setPhoneNum(adminPatchUserReq.getPhoneNum());
            adminUserInfo.setCity(adminPatchUserReq.getCity());
            adminUserInfo.setNickname(adminPatchUserReq.getNickname());
            adminUserInfo.setProfileImageURL(adminPatchUserReq.getProfileImageURL());
            adminUserInfo.setIntrouduce(adminPatchUserReq.getIntroduce());
            adminUserInfo.setCareer(adminPatchUserReq.getCareer());
            adminUserInfo.setEtc(adminPatchUserReq.getEtc());
            adminUserInfo.setMinimumCastingPrice(adminPatchUserReq.getMinimumCastingPrice());
            adminUserInfo.setPrivateBusinessName(adminPatchUserReq.getPrivateBusinessName());
            adminUserInfo.setBusinessNumber(adminPatchUserReq.getBusinessNumber());
            adminUserInfo.setBusinessImageURL(adminPatchUserReq.getBusinessImageURL());
            adminUserInfo.setCorporationBusinessName(adminPatchUserReq.getCorporationBusinessName());
            adminUserInfo.setCorporationBusinessNumber(adminPatchUserReq.getCorporationBusinessNumber());
            adminUserInfo.setStatus(adminPatchUserReq.getStatus());
            adminUserInfoRepository.save(adminUserInfo);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /**
     * admin 비밀번호 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchUserPwReq
     * @return PatchUserRes
     * @throws BaseException
     */
    public AdminPatchUserPwRes updateAdminInfo(AdminPatchUserPwReq adminPatchUserPwReq) throws BaseException {
        WebAdmin existsWebAdminInfo = null;
        String password;

        try {
            existsWebAdminInfo = adminUserInfoProvider.retrieveUserInfoByWebAdminId(adminPatchUserPwReq.getUserId());

            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(adminPatchUserPwReq.getPassword());
            System.out.println(password);
            if(!password.equals(existsWebAdminInfo.getPassword()))
                throw new BaseException(FAILED_TO_PATCH_USER);
            else{
                password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(adminPatchUserPwReq.getNewPassword());
                existsWebAdminInfo.setPassword(password);
                webAdminInfoRepository.save(existsWebAdminInfo);
            }
            return new AdminPatchUserPwRes(adminPatchUserPwReq.getUserId());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @throws BaseException
     */
    public void deleteUserInfo(int userId) throws BaseException {
        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        AdminUserInfo adminUserInfo = adminUserInfoProvider.retrieveUserInfoByUserId(userId);

        // 2-1. 해당 UserInfo를 완전히 삭제
//        try {
//            userInfoRepository.delete(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 UserInfo의 status를 INACTIVE로 설정
        adminUserInfo.setStatus("INACTIVE");
        try {
            adminUserInfoRepository.save(adminUserInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }
}