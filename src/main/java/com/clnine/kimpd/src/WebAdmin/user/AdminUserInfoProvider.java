package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminUserInfoProvider {
    private final AdminUserInfoRepository adminUserInfoRepository;
    private final WebAdminInfoRepository webAdminInfoRepository;
    private final JwtService jwtService;

    @Autowired
    public AdminUserInfoProvider(AdminUserInfoRepository adminUserInfoRepository, WebAdminInfoRepository webAdminInfoRepository, JwtService jwtService) {
        this.adminUserInfoRepository = adminUserInfoRepository;
        this.webAdminInfoRepository = webAdminInfoRepository;
        this.jwtService = jwtService;
    }

    /**
     * 전체 회원 조회
     * @return List<UserInfoRes>
     * @throws BaseException
     */
    public List<AdminGetUsersRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<AdminUserInfo> adminUserInfoList;
        try {
            adminUserInfoList = adminUserInfoRepository.findAll();

        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return adminUserInfoList.stream().map(adminUserInfo -> {
            int userIdx = adminUserInfo.getUserIdx();
            String userType = null;
            if(adminUserInfo.getUserType() == 1){
                userType = "일반";
            }
            else if(adminUserInfo.getUserType() == 2){
                userType = "제작사-개인";
            }
            else if(adminUserInfo.getUserType() == 3){
                userType = "제작사-법인";
            }
            else if(adminUserInfo.getUserType() == 4){
                userType = "전문가-일반";
            }
            else if(adminUserInfo.getUserType() == 5){
                userType = "전문가-개인";
            }
            else if(adminUserInfo.getUserType() == 6){
                userType = "전문가-법인";
            }

            String id = adminUserInfo.getId();
            String email = adminUserInfo.getEmail();
            String phoneNum = adminUserInfo.getPhoneNum();
            String city = adminUserInfo.getCity();
            String status = adminUserInfo.getStatus();
            return new AdminGetUsersRes(userIdx, userType, id, email, phoneNum, city, status);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userIdx
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public AdminGetUserRes retrieveUserInfo(int userIdx) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        AdminUserInfo adminUserInfo = retrieveUserInfoByUserId(userIdx);
        // 2. UserInfoRes로 변환하여 return
        String userType = null;
        if(adminUserInfo.getUserType() == 1){
            userType = "일반";
        }
        else if(adminUserInfo.getUserType() == 2){
            userType = "제작사-개인";
        }
        else if(adminUserInfo.getUserType() == 3){
            userType = "제작사-법인";
        }
        else if(adminUserInfo.getUserType() == 4){
            userType = "전문가-일반";
        }
        else if(adminUserInfo.getUserType() == 5){
            userType = "전문가-개인";
        }
        else if(adminUserInfo.getUserType() == 6){
            userType = "전문가-법인";
        }
        String id = adminUserInfo.getId();
        String email = adminUserInfo.getEmail();
        String phoneNum = adminUserInfo.getPhoneNum();
        String city = adminUserInfo.getCity();
        String nickname = adminUserInfo.getNickname();
        String profileImageURL = adminUserInfo.getProfileImageURL();
        String introduce = adminUserInfo.getIntroduce();
        String career = adminUserInfo.getCareer();
        String etc = adminUserInfo.getEtc();
        String minimumCastingPrice = adminUserInfo.getMinimumCastingPrice();
        String privateBusinessName = adminUserInfo.getPrivateBusinessName();
        String businessNumber = adminUserInfo.getBusinessNumber();
        String businessImageURL = adminUserInfo.getBusinessImageURL();
        String corporationBusinessName = adminUserInfo.getCorporationBusinessName();
        String corporationBusinessNumber = adminUserInfo.getCorporationBusinessNumber();
        String status = adminUserInfo.getStatus();

        return new AdminGetUserRes(userIdx, userType, id, email, phoneNum, city, nickname, profileImageURL,
                introduce, career, etc, minimumCastingPrice, privateBusinessName, businessNumber,
                businessImageURL, corporationBusinessName, corporationBusinessNumber, status);
    }

    /**
     * 로그인
     * @param adminPostLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public AdminPostLoginRes login(AdminPostLoginReq adminPostLoginReq) throws BaseException {
        // 1. DB에서 email로 UserInfo 조회
        WebAdmin webAdmin = retrieveUserInfoByWebAdminId(adminPostLoginReq.getId());

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(webAdmin.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!adminPostLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createWebAdminJwt(webAdmin.getId());

        // 4. PostLoginRes 변환하여 return
        String id = webAdmin.getId();
        return new AdminPostLoginRes(id, jwt);
    }

    /**
     * 회원 조회
     * @param userIdx
     * @return UserInfo
     * @throws BaseException
     */
    public AdminUserInfo retrieveUserInfoByUserId(int userIdx) throws BaseException {
        // 1. DB에서 UserInfo 조회
        AdminUserInfo adminUserInfo;
        try {
            adminUserInfo = adminUserInfoRepository.findById(userIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. UserInfo를 return
        return adminUserInfo;
    }

    /**
     * 회원 조회
     * @param email
     * @return UserInfo
     * @throws BaseException
     */
    public AdminUserInfo retrieveUserInfoByEmail(String email) throws BaseException {
        List<AdminUserInfo> existsUserInfoList;
        try {
            existsUserInfoList = adminUserInfoRepository.findByEmailAndStatus(email, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 UserInfo가 있는지 확인
        AdminUserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    public WebAdmin retrieveUserInfoByWebAdminId(String userId) throws BaseException {
        // 1. DB에서 UserInfo 조회
        WebAdmin adminInfo;
        try {
            adminInfo = webAdminInfoRepository.findById(userId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 회원인지 확인
        if (adminInfo == null || !adminInfo.getStatus().equals("ACTIVE")) {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return adminInfo;
    }

    public Boolean isIdUseable(String id) {
        return !adminUserInfoRepository.existsByIdAndStatus(id, "ACTIVE");
    }

    public Boolean isEmailUseable(String email) {
        return !adminUserInfoRepository.existsByEmailAndStatus(email, "ACTIVE");
    }

    public Boolean isPhoneNumUseable(String phoneNum) {
        return !adminUserInfoRepository.existsByPhoneNumAndStatus(phoneNum, "ACTIVE");
    }

    public Boolean isNicknameUseable(String nickname) {
        return !adminUserInfoRepository.existsByNicknameAndStatus(nickname, "ACTIVE");
    }
}
