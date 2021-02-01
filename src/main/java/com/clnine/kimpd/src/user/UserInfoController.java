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
        // 1. Body Parameter Validation
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
//        if(!isRegexPhoneNumber(parameters.getPhoneNum())){
//            return new BaseResponse<>(INVALID_PHONE_NUMBER);
//        }

        // 2. Post UserInfo
        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /users/:userId
     * @PathVariable userId
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<PatchUserRes> patchUsers(@PathVariable Integer userId, @RequestBody PatchUserReq parameters) {
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }

        try {
            return new BaseResponse<>(SUCCESS_PATCH_USER, userInfoService.updateUserInfo(userId, parameters));
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
     * [2021.01.30] 7.로그인 API
     * [POST] /users/login
     * @RequstBody PostLoginReq
     * @return PostLoginRes
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters){
        if(parameters.getId()==null||parameters.getId().length()==0){
            //아이디를 입력안했을 경우
            return new BaseResponse<>(EMPTY_ID);
        }else if(parameters.getPassword()==null||parameters.getPassword().length()==0){
            //비밀번호를 입력안했을 경우
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        // 2. Login
        try {
            PostLoginRes postLoginRes = userInfoProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/phone-auth")
    public BaseResponse<String> phoneAuth(@RequestParam(value="phoneNum")String phoneNum) throws IOException, ParseException {
        if(phoneNum==null || phoneNum.length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        else if (!isRegexPhoneNumber(phoneNum)) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        String secureCode="1234";
        Map<String,Object> m = sendMessage(secureCode,phoneNum);
//        GetUserPhoneCertification getUserPhoneCertification = new GetUserPhoneCertification(phoneNum,secureCode);
//        Map<String, Object>successMap = SmsService.sendSecureCode(getUserPhoneCertification);
        return new BaseResponse<>(SUCCESS_CHECK_ID);

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
            return new BaseResponse<GetNewPasswordRes>(SUCCESS_PATCH_USER,getNewPasswordRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/:userId
     * @PathVariable userId
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{userId}")
    public BaseResponse<Void> deleteUsers(@PathVariable Integer userId) {
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        try {
            userInfoService.deleteUserInfo(userId);
            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * JWT 검증 API
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
}