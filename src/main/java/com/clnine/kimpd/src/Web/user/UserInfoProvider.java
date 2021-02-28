package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.category.UserJobCategoryRepository;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.SmsService.sendMessage;

@Service
@RequiredArgsConstructor
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;
    private final ReviewRepository reviewRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;
    private final CertificateRepository certificateRepository;
    private final CategoryProvider categoryProvider;

    /**
     * 로그인
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        UserInfo userInfo = retrieveUserInfoById(postLoginReq.getId());
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        if (!postLoginReq.getPassword().equals(password)) {
            throw new BaseException(WRONG_PASSWORD);
        }
        String jwt = jwtService.createJwt(userInfo.getUserIdx());
        int userIdx = userInfo.getUserIdx();
        return new PostLoginRes(userIdx, jwt);
    }

    public GetUserRes getUserRes(int userIdx) throws BaseException {
        UserInfo userInfo = retrieveUserInfoByUserIdx(userIdx);
        String nickname = userInfo.getNickname();
        if(nickname==null){
            nickname="닉네임 없음";
        }
        String profileImageURL = userInfo.getProfileImageURL();
        if(profileImageURL==null){
            profileImageURL="프로필 사진 없음";
        }
        int userType = userInfo.getUserType();
        String stringUserType = null;

        GetUserRes getUserRes=null;

        if (userType == 1 || userType==2 || userType==3) {
            stringUserType = "일반회원";
            getUserRes = new GetUserRes(userIdx, nickname, profileImageURL, stringUserType);
        } else if (userType == 4 || userType ==5 || userType==6) {
            stringUserType = "전문가회원";
            String jobCategoryChildName = categoryProvider.getMainJobCategoryChild(userInfo);
            getUserRes = new GetUserRes(userIdx,nickname,profileImageURL,stringUserType,jobCategoryChildName);
        }

        return getUserRes;
    }


    /**
     * 회원 조회
     * @param userIdx
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoByUserIdx(int userIdx) throws BaseException {
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        if (userInfo == null || !userInfo.getStatus().equals("ACTIVE")) {
            throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }


    /**
     * ID로 회원 조회
     * @param id
     * @return UserInfo
     * @throws BaseException
     */
    public UserInfo retrieveUserInfoById(String id) throws BaseException {
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByIdAndStatus(id, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        UserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }


    public Boolean isIdUsable(String id) {
        return !userInfoRepository.existsByIdAndStatus(id, "ACTIVE");
    }

    public Boolean isNicknameUsable(String nickname) {
        return !userInfoRepository.existsByNicknameAndStatus(nickname, "ACTIVE");
    }

    public UserInfo retrieveUserInfoByPhoneNum(String phoneNum) throws BaseException {
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByPhoneNumAndStatus(phoneNum, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        UserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }

    public int checkPhoneNumCode(String phoneNum) throws BaseException {
       Certification certification;
       int code;
        LocalDateTime now = LocalDateTime.now();
        Timestamp t1 = new Timestamp(System.currentTimeMillis());
        Timestamp t2 = new Timestamp(System.currentTimeMillis() - 180000); //3분
        System.out.println(t1);
        System.out.println(t2);

        long l = (t1.getTime() - t2.getTime());
        long minute = (l / 1000 ) /60 ;
        long second = (l / 1000 ) % 60 ;
        try{
            code = certificateRepository.findTopByUserPhoneNumAndCreatedAtBetweenOrderByCertificationIdxDesc(phoneNum,t2,t1).getSecureCode();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_SECURE_CODE);
        }
        System.out.println(code);
        return code;

    }

    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException {
        List<UserInfo> existsUserInfoList;
        try {
            existsUserInfoList = userInfoRepository.findByEmailAndStatus(email, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        UserInfo userInfo;
        if (existsUserInfoList != null && existsUserInfoList.size() > 0) {
            userInfo = existsUserInfoList.get(0);
        } else {
            throw new BaseException(NOT_FOUND_USER);
        }
        return userInfo;
    }

    public GetIdRes SendId(String phoneNum) throws BaseException {
        UserInfo userInfo;
        try {
            userInfo = retrieveUserInfoByPhoneNum(phoneNum);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        String id = userInfo.getId();
        String message = "김피디입니다. 회원님의 ID 는 [" + id + "] 입니다.";
        try {
            sendMessage(message, phoneNum);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_SEND_MESSAGE);
        }
        return new GetIdRes(id);
    }


    public GetMyUserInfoRes getMyInfo(int userIdx) throws BaseException {
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        /**
         * 공통 컬럼
         */
        String profileImageURL = userInfo.getProfileImageURL();
        String nickname = userInfo.getNickname();
        String id = userInfo.getId();
        String phoneNum = userInfo.getPhoneNum();
        String email = userInfo.getEmail();

        /**
         * 개인 사업자용
         */
        String privateBusinessName = userInfo.getPrivateBusinessName();
        String businessNumber = userInfo.getBusinessNumber();
        String businessImageURL = userInfo.getBusinessImageURL();

        /**
         * 법인 사업자용
         */
        String corpBusinessName = userInfo.getCorporationBusinessName();
        String corpBusinessNumber = userInfo.getCorporationBusinessNumber();
        GetMyUserInfoRes getMyUserInfoRes = null;
        if (userInfo.getUserType() == 1 || userInfo.getUserType() == 4) {
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImageURL(profileImageURL)
                    .id(id).nickname(nickname)
                    .phoneNum(phoneNum)
                    .email(email).build();
        } else if (userInfo.getUserType() == 2 || userInfo.getUserType() == 5) {
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImageURL(profileImageURL)
                    .id(id).nickname(nickname)
                    .phoneNum(phoneNum)
                    .email(email)
                    .privateBusinessName(privateBusinessName)
                    .businessNumber(businessNumber)
                    .businessImageURL(businessImageURL).build();
        } else if (userInfo.getUserType() == 3 || userInfo.getUserType() == 6) {
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImageURL(profileImageURL)
                    .id(id).nickname(nickname)
                    .phoneNum(phoneNum)
                    .email(email)
                    .corpBusinessName(corpBusinessName)
                    .corpBusinessNumber(corpBusinessNumber)
                    .businessImageURL(businessImageURL).build();
        }

        return getMyUserInfoRes;
    }

}
