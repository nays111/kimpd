package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.UserJobCategoryRepository;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
import com.clnine.kimpd.src.Web.review.models.review;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    //전문가이면서
    public List<GetUsersRes> getUserInfoList(String word) throws BaseException {
        List<UserInfo> userInfoList;


        //userInfo를 가지고 userJobCategory 리스트
        //userJobCategory를 가지고 jobCategory 에 가서 jobCategoryName;
        try {
            if (word == null) {
                userInfoList = userInfoRepository.findByStatusAndUserType("ACTIVE", 2);
            } else {
                userInfoList = userInfoRepository.findByUserTypeAndStatusAndNicknameIsContainingOrIntroduceIsContaining(2, "ACTIVE", word, word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        ArrayList<GetUsersRes> getUsersResList = new ArrayList<>();
        for (int i = 0; i < userInfoList.size(); i++) {
            UserInfo userInfo = userInfoList.get(i);
            int userIdx = userInfo.getUserIdx();
            String nickname = userInfo.getNickname();
            String introduce = userInfo.getIntroduce();
            String profileImageURL = userInfo.getProfileImageURL();
            List<UserJobCategory> userJobCategoryList = userJobCategoryRepository.findByUserInfo(userInfo);
            ArrayList<String> jobCategoryParentNameList = new ArrayList<>();
            for (int j = 0; j < userJobCategoryList.size(); j++) {
                String categoryName = userJobCategoryList.get(j).getJobCategoryParent().getJobCategoryName();
                jobCategoryParentNameList.add(categoryName);
            }

            //UserInfo 있으니깐 UserInfo 로 JobCategoryParent를 찾아냄
            //jobCategoryParent가  찾아냈으니깐 거기서 parentName가져온다.

            Integer count = reviewRepository.countAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
            if (count == null) {
                count = 0;
            }
            List<review> reviewList = reviewRepository.findAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
            double sum = 0;
            for (int j = 0; j < reviewList.size(); j++) {
                sum += reviewList.get(j).getStar();
            }

            //double average = Math.round((sum/count)*100)/100.0;

            //GetUsersRes getUsersRes = new GetUsersRes(userIdx, profileImageURL, nickname, introduce, average, count);
            //getUsersResList.add(getUsersRes);
        }

        return getUsersResList;
    }

    public List<GetUsersRes> findExperts(String word,Integer jobCategoryParentIdx,Integer jobCategoryChildIdx, Integer genreCategoryIdx, String city) throws BaseException{

//        try{
            List<Object[]> objects = userInfoRepository.findExpert(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word);
            System.out.println(objects.size());

            List<GetUsersRes> getUsersResList = objects.stream().map(getUsersRes-> new GetUsersRes(
                    (Integer) getUsersRes[0],
                    (String) getUsersRes[1],
                    (String) getUsersRes[2],
                    (String) getUsersRes[3],
                    (BigDecimal) getUsersRes[4],
                    (Integer) getUsersRes[5]
            )).collect(Collectors.toList());

            return getUsersResList;
//        }catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_GET_USER);
//        }
    }
}
