package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import com.clnine.kimpd.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminUserInfoService {
    private final AdminUserInfoRepository adminUserInfoRepository;
    private final WebAdminInfoRepository webAdminInfoRepository;
    private final AdminUserInfoProvider adminUserInfoProvider;
    private final MailService mailService;
    private final JwtService jwtService;

    @Autowired
    public AdminUserInfoService(AdminUserInfoRepository userInfoRepository, WebAdminInfoRepository webAdminInfoRepository,
                                AdminUserInfoProvider adminUserInfoProvider, JwtService jwtService, MailService mailService) {
        this.adminUserInfoRepository = userInfoRepository;
        this.webAdminInfoRepository = webAdminInfoRepository;
        this.adminUserInfoProvider = adminUserInfoProvider;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    /**
     * 회원가입
     *
     * @param postAdminUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public AdminPostAdminRes createAdminInfo(AdminPostAdminReq postAdminUserReq) throws BaseException {
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
        String jwt = jwtService.createWebAdminJwt(admin.getAdminIdx());

        // 5. UserInfoLoginRes로 변환하여 return
        String webAdminId = admin.getId();
        return new AdminPostAdminRes(webAdminId, jwt);
    }

    /**
     * 회원 등록 API
     *
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
     */
    public AdminPostUserRes createUserInfo(AdminPostUserReq postUserReq) throws BaseException {
        AdminUserInfo existUserInfo = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existUserInfo = adminUserInfoProvider.retrieveUserInfoByEmail(postUserReq.getEmail());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existUserInfo != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        String newPassword = mailService.sendPwFindMail(postUserReq.getEmail());
        String hashPassword;
        try {
            hashPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }

        int userType = postUserReq.getUserType();
        String id = postUserReq.getId();
        String email = postUserReq.getEmail();
        String phoneNum = postUserReq.getPhoneNum();
        String name = postUserReq.getName();
        String city = postUserReq.getCity();
        String nickname = postUserReq.getNickname();
        String profileImageURL = postUserReq.getProfileImageURL();
        String introduce = postUserReq.getIntroduce();
        String career = postUserReq.getCareer();
        String etc = postUserReq.getEtc();
        int minimumCastingPrice = postUserReq.getMinimumCastingPrice();
        String privateBusinessName = postUserReq.getPrivateBusinessName();
        String businessNumber = postUserReq.getBusinessNumber();
        String businessImageURL = postUserReq.getBusinessImageURL();
        String corporationBusinessName = postUserReq.getCorporationBusinessName();
        String castingPossibleStartDate = postUserReq.getCastingPossibleStartDate();
        String castingPossibleEndDate = postUserReq.getCastingPossibleEndDate();
        int agreeShowDB = 0;
        if (userType == 4 || userType == 5 || userType == 6) {
            agreeShowDB = 1;
        }
        AdminUserInfo userInfo = new AdminUserInfo(userType, id, hashPassword, email, phoneNum, name, city, nickname, profileImageURL,
                introduce, career, etc, minimumCastingPrice, privateBusinessName, businessNumber, businessImageURL, corporationBusinessName,
                castingPossibleStartDate, castingPossibleEndDate, agreeShowDB, "ACTIVE");

        // 3. 유저 정보 저장
        try {
            userInfo = adminUserInfoRepository.save(userInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
        }

        return new AdminPostUserRes(userInfo.getId(), newPassword);
    }

    /**
     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
     *
     * @param adminPatchUserReq
     * @return void
     * @throws BaseException
     */
    public void updateUserInfo(AdminPatchUserReq adminPatchUserReq) throws BaseException {
        AdminUserInfo adminUserInfo = null;
        int userType = 0;

        adminUserInfo = adminUserInfoProvider.retrieveUserInfoByUserId(adminPatchUserReq.getUserIdx());
        if(adminUserInfo == null){
            throw new BaseException(FAILED_TO_PATCH_USER);
        }

        if (!adminPatchUserReq.getId().equals(adminUserInfo.getId())) {
            if (adminUserInfoProvider.isIdUseable(adminPatchUserReq.getId()) == false) {
                throw new BaseException(DUPLICATED_USER);
            }
        }

        if (!adminPatchUserReq.getPhoneNum().equals(adminUserInfo.getPhoneNum())) {
            if (adminUserInfoProvider.isPhoneNumUseable(adminPatchUserReq.getPhoneNum()) == false) {
                throw new BaseException(DUPLICATED_PHONE_NUMBER);
            }
        }

        if (!adminPatchUserReq.getEmail().equals(adminUserInfo.getEmail())) {
            if (adminUserInfoProvider.isEmailUseable(adminPatchUserReq.getEmail()) == false) {
                throw new BaseException(DUPLICATED_EMAIL);
            }
        }

        if (!adminPatchUserReq.getNickname().equals(adminUserInfo.getNickname())) {
            if (adminUserInfoProvider.isNicknameUseable(adminPatchUserReq.getNickname()) == false) {
                throw new BaseException(DUPLICATED_NICKNAME);
            }
        }

        if (adminPatchUserReq.getUserType().equals("클라이언트")) {
            userType = 1;
        } else if (adminPatchUserReq.getUserType().equals("제작사-개인")) {
            userType = 2;
        } else if (adminPatchUserReq.getUserType().equals("제작사-법인")) {
            userType = 3;
        } else if (adminPatchUserReq.getUserType().equals("전문가-클라이언트")) {
            userType = 4;
        } else if (adminPatchUserReq.getUserType().equals("전문가-개인")) {
            userType = 5;
        } else if (adminPatchUserReq.getUserType().equals("전문가-법인")) {
            userType = 6;
        }

        adminUserInfo.setUserType(userType);
        adminUserInfo.setId(adminPatchUserReq.getId());
        adminUserInfo.setEmail(adminPatchUserReq.getEmail());
        adminUserInfo.setPhoneNum(adminPatchUserReq.getPhoneNum());
        adminUserInfo.setName(adminPatchUserReq.getName());
        if (adminPatchUserReq.getCity() == null || adminPatchUserReq.getCity().length() == 0) {
            adminUserInfo.setCity(null);
        } else {
            adminUserInfo.setCity(adminPatchUserReq.getCity());
        }

        if (adminPatchUserReq.getNickname() == null || adminPatchUserReq.getNickname().length() == 0)
            adminUserInfo.setNickname(null);
        else
            adminUserInfo.setNickname(adminPatchUserReq.getNickname());

        if (adminPatchUserReq.getProfileImageURL() == null || adminPatchUserReq.getProfileImageURL().length() == 0)
            adminUserInfo.setProfileImageURL(null);
        else
            adminUserInfo.setProfileImageURL(adminPatchUserReq.getProfileImageURL());

        if (adminPatchUserReq.getIntroduce() == null || adminPatchUserReq.getIntroduce().length() == 0)
            adminUserInfo.setIntroduce(null);
        else
            adminUserInfo.setIntroduce(adminPatchUserReq.getIntroduce());

        if (adminPatchUserReq.getCareer() == null || adminPatchUserReq.getCareer().length() == 0)
            adminUserInfo.setCareer(null);
        else
            adminUserInfo.setCareer(adminPatchUserReq.getCareer());

        if (adminPatchUserReq.getEtc() == null || adminPatchUserReq.getEtc().length() == 0)
            adminUserInfo.setEtc(null);
        else
            adminUserInfo.setEtc(adminPatchUserReq.getEtc());

        adminUserInfo.setMinimumCastingPrice(adminPatchUserReq.getMinimumCastingPrice());

        if (adminPatchUserReq.getPrivateBusinessName() == null || adminPatchUserReq.getPrivateBusinessName().length() == 0)
            adminUserInfo.setPrivateBusinessName(null);
        else
            adminUserInfo.setPrivateBusinessName(adminPatchUserReq.getPrivateBusinessName());

        if (adminPatchUserReq.getBusinessNumber() == null || adminPatchUserReq.getBusinessNumber().length() == 0)
            adminUserInfo.setBusinessNumber(null);
        else
            adminUserInfo.setBusinessNumber(adminPatchUserReq.getBusinessNumber());

        if (adminPatchUserReq.getBusinessImageURL() == null || adminPatchUserReq.getBusinessImageURL().length() == 0)
            adminUserInfo.setBusinessImageURL(null);
        else
            adminUserInfo.setBusinessImageURL(adminPatchUserReq.getBusinessImageURL());

        if (adminPatchUserReq.getCorporationBusinessName() == null || adminPatchUserReq.getCorporationBusinessName().length() == 0)
            adminUserInfo.setCorporationBusinessName(null);
        else
            adminUserInfo.setCorporationBusinessName(adminPatchUserReq.getCorporationBusinessName());

        if (adminPatchUserReq.getCastingPossibleStartDate() == null || adminPatchUserReq.getCastingPossibleStartDate().length() == 0) {
            adminUserInfo.setCastingPossibleStartDate(null);
        } else {
            adminUserInfo.setCastingPossibleStartDate(adminPatchUserReq.getCastingPossibleStartDate());
        }

        if (adminPatchUserReq.getCastingPossibleEndDate() == null || adminPatchUserReq.getCastingPossibleEndDate().length() == 0) {
            adminUserInfo.setCastingPossibleEndDate(null);
        } else {
            adminUserInfo.setCastingPossibleEndDate(adminPatchUserReq.getCastingPossibleEndDate());
        }

        adminUserInfo.setStatus(adminPatchUserReq.getStatus());
        adminUserInfoRepository.save(adminUserInfo);
        return;
    }

    /**
     * user 비밀번호 초기화 (POST uri 가 겹쳤을때의 예시 용도)
     *
     * @param adminPatchUserPwReq
     * @return void
     * @throws BaseException
     */
    public void updateUserPw(AdminPatchUserPwReq adminPatchUserPwReq) throws BaseException {
        AdminUserInfo existsUserInfo = null;

        try {
            //존재한다면
            existsUserInfo = adminUserInfoProvider.retrieveUserInfoByEmail(adminPatchUserPwReq.getEmail());
            if (existsUserInfo == null) {
                throw new BaseException(NOT_FOUND_USER);
            }
            String newPassword = mailService.sendPwFindMail(adminPatchUserPwReq.getEmail());
            String hashedPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
            existsUserInfo.setPassword(hashedPassword);
            adminUserInfoRepository.save(existsUserInfo);
            return;
        } catch (BaseException exception) {
            throw new BaseException(NOT_FOUND_USER);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    /**
     * admin 비밀번호 수정 (POST uri 가 겹쳤을때의 예시 용도)
     *
     * @param adminPatchAdminPwReq
     * @return PatchUserRes
     * @throws BaseException
     */
    public AdminPatchAdminPwRes updateAdminInfo(AdminPatchAdminPwReq adminPatchAdminPwReq) throws BaseException {
        WebAdmin existsWebAdminInfo = null;
        String password = null;

        existsWebAdminInfo = adminUserInfoProvider.retrieveUserInfoByWebAdminId(adminPatchAdminPwReq.getId());

        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(adminPatchAdminPwReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }

        if (!password.equals(existsWebAdminInfo.getPassword()))
            throw new BaseException(NOT_MATCH_PRESENT_PASSWORD);
        else {
            try {
                password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(adminPatchAdminPwReq.getNewPassword());
            } catch (Exception ignored) {
                throw new BaseException(FAILED_TO_PATCH_USER);
            }

            existsWebAdminInfo.setPassword(password);
            webAdminInfoRepository.save(existsWebAdminInfo);
        }
        return new AdminPatchAdminPwRes(adminPatchAdminPwReq.getId());

    }
}