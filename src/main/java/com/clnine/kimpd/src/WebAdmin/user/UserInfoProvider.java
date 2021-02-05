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
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final WebAdminInfoRepository webAdminInfoRepository;
    private final JwtService jwtService;

    @Autowired
    public UserInfoProvider(UserInfoRepository userInfoRepository, WebAdminInfoRepository webAdminInfoRepository, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.webAdminInfoRepository = webAdminInfoRepository;
        this.jwtService = jwtService;
    }

    /**
     * 전체 회원 조회
     * @return List<UserInfoRes>
     * @throws BaseException
     */
    public List<GetUserRes> retrieveUserInfoList(String word) throws BaseException {
        // 1. DB에서 전체 UserInfo 조회
        List<UserInfo> userInfoList;
        try {
            if (word == null) { // 전체 조회
                userInfoList = userInfoRepository.findByStatus("ACTIVE");
            } else { // 검색 조회
                userInfoList = userInfoRepository.findByStatusAndNicknameIsContaining("ACTIVE", word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. UserInfoRes로 변환하여 return
        return userInfoList.stream().map(userInfo -> {
            int id = userInfo.getId();
            String email = userInfo.getEmail();
            String nickname = userInfo.getNickname();
            String phoneNumber = userInfo.getPhoneNumber();
            return new GetUserRes(id, email, nickname, phoneNumber);
        }).collect(Collectors.toList());
    }

    /**
     * 회원 조회
     * @param userId
     * @return UserInfoDetailRes
     * @throws BaseException
     */
    public GetUserRes retrieveUserInfo(int userId) throws BaseException {
        // 1. DB에서 userId로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoByUserId(userId);

        // 2. UserInfoRes로 변환하여 return
        int id = userInfo.getId();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String phoneNumber = userInfo.getPhoneNumber();
        return new GetUserRes(id, email, nickname, phoneNumber);
    }

    /**
     * 로그인
     * @param postAdminLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostAdminLoginRes login(PostAdminLoginReq postAdminLoginReq) throws BaseException {
        // 1. DB에서 email로 UserInfo 조회
        WebAdmin webAdmin = retrieveUserInfoByWebAdminId(postAdminLoginReq.getId());

        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(webAdmin.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!postAdminLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createWebAdminJwt(webAdmin.getId());

        // 4. PostLoginRes 변환하여 return
        String id = webAdmin.getId();
        return new PostAdminLoginRes(id, jwt);
    }

    /**
     * 회원 조회
     * @param userId
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByUserId(int userId) throws BaseException {
        // 1. DB에서 UserInfo 조회
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userId).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 회원인지 확인
        if (userInfo == null || !userInfo.getStatus().equals("ACTIVE")) {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    /**
     * 회원 조회
     * @param email
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException {
        // 1. email을 이용해서 UserInfo DB 접근
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByEmailAndStatus(email, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        // 2. 존재하는 UserInfo가 있는지 확인
        UserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }

        // 3. UserInfo를 return
        return userInfo;
    }

    //
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
