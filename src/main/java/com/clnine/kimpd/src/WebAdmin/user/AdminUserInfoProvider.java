package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.config.secret.Secret;
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
    public List<AdminGetUserRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<AdminUserInfo> adminUserInfoList;
        try {
            adminUserInfoList = adminUserInfoRepository.findByStatus("ACTIVE");

        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return adminUserInfoList.stream().map(adminUserInfo -> {
            int userIdx = adminUserInfo.getUserIdx();
            String userType = null;
            if(adminUserInfo.getUserType() == 1 || adminUserInfo.getUserType() == 2){
                userType = "일반";
            }
            else{
                userType = "전문가";
            }
            String id = adminUserInfo.getId();
            String email = adminUserInfo.getEmail();
            String phoneNum = adminUserInfo.getPhoneNum();
            String address = adminUserInfo.getAddress();
            return new AdminGetUserRes(userIdx, userType, id, email, phoneNum, address);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userId
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public AdminGetUserRes retrieveUserInfo(int userId) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        AdminUserInfo adminUserInfo = retrieveUserInfoByUserId(userId);
        // 2. UserInfoRes로 변환하여 return
        int userIdx = adminUserInfo.getUserIdx();
        String userType = null;
        if(adminUserInfo.getUserType() == 1 || adminUserInfo.getUserType() == 2){
            userType = "일반";
        }
        else{
            userType = "전문가";
        }
        String id = adminUserInfo.getId();
        String email = adminUserInfo.getEmail();
        String phoneNum = adminUserInfo.getPhoneNum();
        String address = adminUserInfo.getAddress();
        return new AdminGetUserRes(userIdx, userType, id, email, phoneNum, address);
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
     * @param userId
     * @return UserInfo
     * @throws BaseException
     */
    public AdminUserInfo retrieveUserInfoByUserId(int userId) throws BaseException {
        // 1. DB에서 UserInfo 조회
        AdminUserInfo adminUserInfo;
        try {
            adminUserInfo = adminUserInfoRepository.findById(userId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 회원인지 확인
        if (adminUserInfo == null || !adminUserInfo.getStatus().equals("ACTIVE")) {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return adminUserInfo;
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
}
