package com.clnine.kimpd.src.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
        private final JwtService jwtService;

    @Autowired
    public UserInfoProvider(UserInfoRepository userInfoRepository, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
    }

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        // 1. DB에서 id로 UserInfo 조회
        UserInfo userInfo = retrieveUserInfoById(postLoginReq.getId());


        // 2. UserInfo에서 password 추출
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

        // 3. 비밀번호 일치 여부 확인
        if (!postLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }

        // 3. Create JWT
        String jwt = jwtService.createJwt(userInfo.getUserIdx());

        // 4. PostLoginRes 변환하여 return
        int userIdx = userInfo.getUserIdx();
        return new PostLoginRes(userIdx, jwt);
    }


    /**
     * 회원 조회
     * @param userIdx
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByUserIdx(int userIdx) throws BaseException {
        // 1. DB에서 UserInfo 조회
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userIdx).orElse(null);
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
     * ID로 회원 조회
     * @param id
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoById(String id) throws BaseException {
        // 1. id를 이용해서 UserInfo DB 접근
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByIdAndStatus(id, "ACTIVE");
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


    public Boolean isIdUsable(String id) {
        return !userInfoRepository.existsByIdAndStatus(id,"ACTIVE");
    }

    public Boolean isNicknameUsable(String nickname){
        return !userInfoRepository.existsByNicknameAndStatus(nickname,"ACTIVE");
    }




    /**
     *
     */
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException{
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
}
