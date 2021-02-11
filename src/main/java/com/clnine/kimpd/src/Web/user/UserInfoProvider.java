package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.UserJobCategoryRepository;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
import com.clnine.kimpd.src.Web.review.models.review;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.SmsService.sendMessage;

@Service
public class UserInfoProvider {
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;
    private final ReviewRepository reviewRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public UserInfoProvider(UserInfoRepository userInfoRepository,
                            JwtService jwtService,
                            ReviewRepository reviewRepository,
                            UserJobCategoryRepository userJobCategoryRepository,
                            CertificateRepository certificateRepository) {
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
        this.reviewRepository = reviewRepository;
        this.userJobCategoryRepository = userJobCategoryRepository;
        this.certificateRepository = certificateRepository;
    }

    /**
     * 로그인
     *
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
        if (userType == 1 || userType==2 || userType==3) {
            stringUserType = "일반회원";
        } else if (userType == 4 || userType ==5 || userType==6) {
            stringUserType = "전문가회원";
        }
        GetUserRes getUserRes = new GetUserRes(userIdx, nickname, profileImageURL, stringUserType);
        return getUserRes;
    }


    /**
     * 회원 조회
     *
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
     *
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

    public int checkPhoneNumCode(String phoneNum) throws BaseException {
        //phoneNum으로 certificatin 객체 찾기
        //3분 이내의 것들 중에 가장 최근 것것
       Certification certification;
       int code;

//       String today = null;
//       String day3before = null;
//       Date date = new Date();
//       SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
//
//       Calendar cal = Calendar.getInstance();
//       cal.setTime(date);
//       today = formatter.format(cal.getTime());
//
//       cal.add(Calendar.MINUTE,-3);
//       day3before = formatter.format(cal.getTime());
//
//       Date TODAY =  formatter.parse(today);
//       Date DAY3BEFORE =  formatter.parse(day3before);
        LocalDateTime now = LocalDateTime.now();
        Timestamp t1 = new Timestamp(System.currentTimeMillis());
        Timestamp t2 = new Timestamp(System.currentTimeMillis() - 180000); //3분
        System.out.println(t1);
        System.out.println(t2);

        long l = (t1.getTime() - t2.getTime());
        long minute = (l / 1000 ) /60 ; //minute
        long second = (l / 1000 ) % 60 ; //second
        try{
//더 예전시간이 앞쪽 파라미터로 가야함
            code = certificateRepository.findTopByUserPhoneNumAndCreatedAtBetweenOrderByCertificationIdxDesc(phoneNum,t2,t1).getSecureCode();
            //code = certificateRepository.findTopByUserPhoneNumOrderByCertificationIdxDesc(phoneNum).getSecureCode();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_SECURE_CODE);
        }
        System.out.println(code);
        return code;

    }

    /**
     *
     */
    public UserInfo retrieveUserInfoByEmail(String email) throws BaseException {
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
            //getMyUserInfoRes = new GetMyUserInfoRes(userIdx,profileImageURL,id,nickname,phoneNum,email);
        //}
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImagerURL(profileImageURL)
                    .id(id).nickname(nickname)
                    .phoneNum(phoneNum)
                    .email(email).build();
        } else if (userInfo.getUserType() == 2 || userInfo.getUserType() == 5) {
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImagerURL(profileImageURL)
                    .id(id).nickname(nickname)
                    .phoneNum(phoneNum)
                    .email(email)
                    .privateBusinessName(privateBusinessName)
                    .businessNumber(businessNumber)
                    .businessImageURL(businessImageURL).build();
        } else if (userInfo.getUserType() == 3 || userInfo.getUserType() == 6) {
            getMyUserInfoRes = GetMyUserInfoRes.builder().userIdx(userIdx)
                    .profileImagerURL(profileImageURL)
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
