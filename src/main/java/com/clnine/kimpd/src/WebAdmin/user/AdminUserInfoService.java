package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

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
        String jwt = jwtService.createWebAdminJwt(admin.getId());

        // 5. UserInfoLoginRes로 변환하여 return
        String webAdminId = admin.getId();
        return new AdminPostAdminRes(webAdminId, jwt);
    }

    /**
     * 회원 등록 API
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
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int index;
        char password;

        //랜덤한 임시 비밀번호 생성
        char[] charPw=new char[] {
                '0','1','2','3','4','5','6','7','8','9',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };

        char[] charSpPw=new char[] {
                '!','@','#','$','%','^','&','*','?','~','_'
        };

        for(int i=0;i<8;i++) {
            index = random.nextInt(charPw.length);
            password = charPw[index];
            buffer.append(password);
        }
        for(int i=0;i<2;i++) {
            index = random.nextInt(charSpPw.length);
            password = charSpPw[index];
            buffer.append(password);
        }
        String newPassword = buffer.toString();
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
        String city = postUserReq.getCity();
        String nickname = postUserReq.getNickname();
        String profileImageURL = postUserReq.getProfileImageURL();
        String introduce = postUserReq.getIntroduce();
        String career = postUserReq.getCareer();
        String etc = postUserReq.getEtc();
        String minimumCastingPrice = postUserReq.getMinimumCastingPrice();
        String privateBusinessName = postUserReq.getPrivateBusinessName();
        String businessNumber = postUserReq.getBusinessNumber();
        String businessImageURL = postUserReq.getBusinessImageURL();
        String corporationBusinessName = postUserReq.getCorporationBusinessName();
        String corporationBusinessNumber = postUserReq.getCorporationBusinessNumber();
        int agreeShowDB = 0;
        if(userType == 4 || userType == 5 || userType == 6){
            agreeShowDB = 1;
        }
        AdminUserInfo userInfo = new AdminUserInfo(userType, id, hashPassword, email, phoneNum, city, nickname, profileImageURL,
                introduce, career, etc, minimumCastingPrice, privateBusinessName, businessNumber, businessImageURL, corporationBusinessName,
                corporationBusinessNumber, agreeShowDB, "ACTIVE");

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
     * @param adminPatchUserReq
     * @return void
     * @throws BaseException
     */
    public void updateUserInfo(AdminPatchUserReq adminPatchUserReq) throws BaseException {
        AdminUserInfo adminUserInfo = null;
        int userType = 0;
        int agreeShowDB = 0;

        try {
            adminUserInfo = adminUserInfoProvider.retrieveUserInfoByUserId(adminPatchUserReq.getUserIdx());

            if(!adminPatchUserReq.getId().equals(adminUserInfo.getId())){
                if (adminUserInfoProvider.isIdUseable(adminPatchUserReq.getId()) == false) {
                    throw new BaseException(DUPLICATED_USER);
                }
            }
            if (adminPatchUserReq.getUserType().equals("일반")) {
                userType = 1;
            }
            else if(adminPatchUserReq.getUserType().equals("제작사-개인")){
                userType = 2;
            }
            else if(adminPatchUserReq.getUserType().equals("제작사-법인")){
                userType = 3;
            }
            else if(adminPatchUserReq.getUserType().equals("전문가-일반")){
                userType = 4;
                agreeShowDB = 1;
            }
            else if(adminPatchUserReq.getUserType().equals("전문가-개인")){
                userType = 5;
                agreeShowDB = 1;
            }
            else if(adminPatchUserReq.getUserType().equals("전문가-법인")){
                userType = 6;
                agreeShowDB = 1;
            }

            adminUserInfo.setUserType(userType);
            adminUserInfo.setAgreeShowDB(agreeShowDB);
            adminUserInfo.setId(adminPatchUserReq.getId());
            adminUserInfo.setEmail(adminPatchUserReq.getEmail());
            adminUserInfo.setPhoneNum(adminPatchUserReq.getPhoneNum());
            if(adminPatchUserReq.getCity() == null || adminPatchUserReq.getCity().length() == 0) {
                adminUserInfo.setCity(null);
            }
            else {
                adminUserInfo.setCity(adminPatchUserReq.getCity());
            }

            if(adminPatchUserReq.getNickname() == null || adminPatchUserReq.getNickname().length() == 0)
                adminUserInfo.setNickname(null);
            else
                adminUserInfo.setNickname(adminPatchUserReq.getNickname());

            if(adminPatchUserReq.getProfileImageURL() == null || adminPatchUserReq.getProfileImageURL().length() == 0)
                adminUserInfo.setProfileImageURL(null);
            else
                adminUserInfo.setProfileImageURL(adminPatchUserReq.getProfileImageURL());

            if(adminPatchUserReq.getIntroduce() == null || adminPatchUserReq.getIntroduce().length() == 0)
                adminUserInfo.setIntroduce(null);
            else
                adminUserInfo.setIntroduce(adminPatchUserReq.getIntroduce());

            if(adminPatchUserReq.getCareer() == null || adminPatchUserReq.getCareer().length() == 0)
                adminUserInfo.setCareer(null);
            else
                adminUserInfo.setCareer(adminPatchUserReq.getCareer());

            if(adminPatchUserReq.getEtc() == null || adminPatchUserReq.getEtc().length() == 0)
                adminUserInfo.setEtc(null);
            else
                adminUserInfo.setEtc(adminPatchUserReq.getEtc());

            if(adminPatchUserReq.getMinimumCastingPrice() == null || adminPatchUserReq.getMinimumCastingPrice().length() == 0)
                adminUserInfo.setMinimumCastingPrice(null);
            else
                adminUserInfo.setMinimumCastingPrice(adminPatchUserReq.getMinimumCastingPrice());

            if(adminPatchUserReq.getPrivateBusinessName() == null || adminPatchUserReq.getPrivateBusinessName().length() == 0)
                adminUserInfo.setPrivateBusinessName(null);
            else
                adminUserInfo.setPrivateBusinessName(adminPatchUserReq.getPrivateBusinessName());

            if(adminPatchUserReq.getBusinessNumber() == null || adminPatchUserReq.getBusinessNumber().length() == 0)
                adminUserInfo.setBusinessNumber(null);
            else
                adminUserInfo.setBusinessNumber(adminPatchUserReq.getBusinessNumber());

            if(adminPatchUserReq.getBusinessImageURL() == null || adminPatchUserReq.getBusinessImageURL().length() == 0)
                adminUserInfo.setBusinessImageURL(null);
            else
                adminUserInfo.setBusinessImageURL(adminPatchUserReq.getBusinessImageURL());

            if(adminPatchUserReq.getCorporationBusinessName() == null || adminPatchUserReq.getCorporationBusinessName().length() == 0)
                adminUserInfo.setCorporationBusinessName(null);
            else
                adminUserInfo.setCorporationBusinessName(adminPatchUserReq.getCorporationBusinessName());


            if(adminPatchUserReq.getCorporationBusinessNumber() == null || adminPatchUserReq.getCorporationBusinessNumber().length() == 0)
                adminUserInfo.setCorporationBusinessNumber(null);
            else
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