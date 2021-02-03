package com.clnine.kimpd.src.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.user.models.*;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import com.clnine.kimpd.utils.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.SmsService.sendMessage;
import static com.clnine.kimpd.utils.ValidationRegex.*;


@RestController
@RequestMapping(value = "/users")
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final MailService mailService;

    @Autowired
    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService, JwtService jwtService, MailService mailService) {
        this.userInfoProvider = userInfoProvider;
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }
    /**
     * 회원가입 API
     * [POST] /users
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {

        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        if (!isRegexEmail(parameters.getEmail())){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        if(!isRegexPassword(parameters.getPassword())){
            return new BaseResponse<>(INVALID_PASSWORD);
        }
        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }
        if(parameters.getPhoneNum()==null || parameters.getPhoneNum().length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        if(!isRegexPhoneNumber(parameters.getPhoneNum())){
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
//        if(parameters.getAgreeAdvertisement() || parameters.getAgreeAdvertisement()>1){
//            return new BaseResponse<>(INVALID_AGREE_ADGERTISEMENT_CHECK);
//        }

        /**
         * 필수 X 입력 아닌사항
         */
        if(parameters.getBusinessNumber()!=null){
            if(parameters.getBusinessNumber().length()==0){
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
        }

        if(parameters.getBusinessImageURL()!=null){
            if(parameters.getBusinessImageURL().length()==0) {
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
            //todo url 파일 형식 검사
        }
        if(parameters.getCorporationBusinessName()!=null){
            if(parameters.getCorporationBusinessName().length()==0){
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NAME);
            }
        }

        if(parameters.getCorporationBusinessNumber()!=null){
            if(parameters.getCorporationBusinessNumber().length()==0){
                    return new BaseResponse<>(EMPTY_CORP_BUSINESS_NUMBER);
            }
        }

        if(parameters.getNickname()!=null){
            if(parameters.getNickname().length()==0){
                return new BaseResponse<>(EMPTY_NICKNAME);
            }
        }
        if(parameters.getGenreCategoryIdx().size()==0){
            //카테고리 선택을 하나도 안했을 경우
            return new BaseResponse<>(NO_SELECT_GENRE_CATEGORY);
        }

        // 2. Post UserInfo
        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.01.31] 2.아이디 중복확인 API
     * [GET] /users/duplicated-id?id=
     * @param id
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("/duplicated-id")
    public BaseResponse<Void> checkIdDuplicate(@RequestParam(value="id") String id){
        if(id==null || id.length()==0){
            return new BaseResponse<>(EMPTY_ID);
        }
        if(userInfoProvider.isIdUsable(id)==true){
            return new BaseResponse<>(SUCCESS_CHECK_ID);
        }else{
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }

    /**
     * [2020.01.31] 3.닉네임 중복확인 API
     * [GET] /users/duplicated-nickname?nickname=
     * @param nickname
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("/duplicated-nickname")
    public BaseResponse<String> checkNicknameDuplicate(@RequestParam(value="nickname")String nickname){
        if(nickname==null || nickname.length()==0) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if(userInfoProvider.isNicknameUsable(nickname)==true){
            return new BaseResponse<>(SUCCESS_CHECK_NICKNAME);
        }else{
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }
    /**
     * [2021.02.01] 4. 휴대폰 인증 번호 전송 API
     * [GET] /users/phone-auth?phoneNum=
     */
    @PostMapping("/phone-auth")
    public BaseResponse<String> phoneAuth(@RequestParam(value="phoneNum")String phoneNum) throws IOException, ParseException {
        if(phoneNum==null || phoneNum.length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        else if (!isRegexPhoneNumber(phoneNum)) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        /**
         * 랜덤한 인증코드 여섯 자리를 생성후 db에 저장하고 메시지 전송
         */
        int rand = (int) (Math.random() * 899999) + 100000;
        try{
            userInfoService.PostSecureCode(rand,phoneNum);
            return new BaseResponse<>(SUCCESS_SEND_MESSAGE);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * [2021.01.30] 7.로그인 API
     * [POST] /users/login
     * @RequstBody PostLoginReq
     * @return PostLoginRes
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters){
        if(parameters.getId()==null||parameters.getId().length()==0){
            return new BaseResponse<>(EMPTY_ID);
        }else if(parameters.getPassword()==null||parameters.getPassword().length()==0){
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        try {
            PostLoginRes postLoginRes = userInfoProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * [2021.01.31] 7-1. JWT 검증 API
     * [GET] /users/jwt
     * @return BaseResponse<Void>
     */
    @GetMapping("/jwt")
    public BaseResponse<Void> jwt() {
        try {
            int userIdx = jwtService.getUserIdx();
            userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
            return new BaseResponse<>(SUCCESS_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.02.03] 8. 아이디 찾기 API
     * [GET] /users/id?id=
     * phoneNumber로 userInfo 에서 id 조회 -> id를 입력받은 phoneNumber로 sendMessage
     * @param phoneNumber
     * @return
     */
    @GetMapping("/id")
    public BaseResponse<GetIdRes> lostId(@RequestParam(value="phoneNum")String phoneNumber){
        if(phoneNumber==null || phoneNumber.length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }else if(!isRegexPhoneNumber(phoneNumber)){
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        try{
            //todo 아래 부분만 수정
            GetIdRes getIdRes = userInfoProvider.SendId(phoneNumber);
            return new BaseResponse<>(SUCCESS_SEND_ID_MESSAGE,getIdRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * [2021.01.30] 9.비밀번호 찾기 API
     * [GET] /users/password?email=
     * @param userEmail
     * @return
     */
    @GetMapping("/password")
    public BaseResponse<GetNewPasswordRes> lostPassword(@RequestParam(value="email") String userEmail){
        if(userEmail==null || userEmail.length()==0){
            return new BaseResponse<>(EMPTY_EMAIL);
        }else if(!isRegexEmail(userEmail)){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        try {
            GetNewPasswordRes getNewPasswordRes = userInfoService.patchUserPassword(userEmail);
            return new BaseResponse<GetNewPasswordRes>(SUCCESS_PATCH_USER_PASSWORD,getNewPasswordRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


}