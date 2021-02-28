package com.clnine.kimpd.src.Web.user;

import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.CorpState;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.config.secret.Secret.barbobillCorpNum;
import static com.clnine.kimpd.config.secret.Secret.barobillCertyKey;
import static com.clnine.kimpd.utils.ValidationRegex.*;


@RestController
@CrossOrigin
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final BarobillApiService barobillApiService;

    /**
     * 1. 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     * @RequestBody PostUserReq
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
        if (parameters.getUserType() == 0) {
            return new BaseResponse<>(INVALID_USER_TYPE);
        }
        if (parameters.getUserType() > 6) {
            return new BaseResponse<>(INVALID_USER_TYPE);
        }
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_ID);
        }
        if (!isRegexId(parameters.getId())) {
            return new BaseResponse<>(INVALID_ID);
        }
        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        if (!isRegexEmail(parameters.getEmail())) {
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        if (!isRegexPassword(parameters.getPassword())) {
            return new BaseResponse<>(INVALID_PASSWORD);
        }
        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }
        if (parameters.getPhoneNum() == null || parameters.getPhoneNum().length() == 0) {
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        if (!isRegexPhoneNumber(parameters.getPhoneNum())) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        /**
         * 광고 동의 여부 처리
         */
//        if(parameters.getAgreeAdvertisement()!=0 || parameters.getAgreeAdvertisement()!=1){
//            return new BaseResponse<>(INVALID_AGREE_ADVERTISEMENT_CHECK);
//        }
        if(parameters.getCity()==null || parameters.getCity().length()==0){
            return new BaseResponse<>(EMPTY_CITY);
        }
        if (parameters.getAddress() == null || parameters.getAddress().length() == 0) {
            return new BaseResponse<>(EMPTY_ADDRESS);
        }
        //todo 주소 형식 처리
        /**
         * 필수 X 입력 아닌사항
         */
        if (parameters.getPrivateBusinessName() != null) {
            if (parameters.getPrivateBusinessName().length() == 0) {
                return new BaseResponse<>(EMPTY_PRIVATE_BUSINESS_NAME);
            }
        }
        if (parameters.getBusinessNumber() != null) {
            if (parameters.getBusinessNumber().length() == 0) {
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
        }
        //todo 사업자 등록 번호 양식 검사, 이미지 업로드 양식 검사
        if (parameters.getBusinessImageURL() != null) {
            if (parameters.getBusinessImageURL().length() == 0) {
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
//            if (!isRegexImageType(parameters.getBusinessImageURL())) {
//                return new BaseResponse<>(INVALID_IMAGE_TYPE);
//            }
        }
        if (parameters.getCorporationBusinessName() != null) {
            if (parameters.getCorporationBusinessName().length() == 0) {
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NAME);
            }
        }
        //todo 법인 사업자명 형식 검사
        if (parameters.getCorporationBusinessNumber() != null) {
            if (parameters.getCorporationBusinessNumber().length() == 0) {
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NUMBER);
            }
        }
        //todo 법인 등록 번호 형식 검사
        if (parameters.getNickname() != null) {
            if (parameters.getNickname().length() == 0) {
                return new BaseResponse<>(EMPTY_NICKNAME);
            }
            if (!isRegexNickname(parameters.getNickname())) {
                return new BaseResponse<>(INVALID_NICKNAME);
            }
        }
        if (parameters.getJobParentCategoryIdx() != null) {
            if (parameters.getJobParentCategoryIdx().size() == 0) {
                //카테고리 선택을 하나도 안했을 경우
                return new BaseResponse<>(NO_SELECT_JOB_PARENT_CATEGORY);
            }
        }
        if (parameters.getJobChildCategoryIdx() != null) {
            if (parameters.getJobChildCategoryIdx().size() == 0) {
                //카테고리 선택을 하나도 안했을 경우
                return new BaseResponse<>(NO_SELECT_JOB_CHILD_CATEGORY);
            }
        }
        if (parameters.getGenreCategoryIdx() != null) {
            if (parameters.getGenreCategoryIdx().size() == 0) {
                //카테고리 선택을 하나도 안했을 경우
                return new BaseResponse<>(NO_SELECT_GENRE_CATEGORY);
            }
        }
        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.01.31] 2.아이디 중복확인 API
     * [GET] /users/duplicated-id?id=
     *
     * @param id
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("/duplicated-id")
    public BaseResponse<Void> checkIdDuplicate(@RequestParam(value = "id") String id) {
        if (id == null || id.length() == 0) {
            return new BaseResponse<>(EMPTY_ID);
        }
        if (userInfoProvider.isIdUsable(id) == true) {
            return new BaseResponse<>(SUCCESS);
        } else {
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }

    /**
     * [2020.01.31] 3.닉네임 중복확인 API
     * [GET] /users/duplicated-nickname?nickname=
     *
     * @param nickname
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("/duplicated-nickname")
    public BaseResponse<String> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        if (nickname == null || nickname.length() == 0) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if (userInfoProvider.isNicknameUsable(nickname) == true) {
            return new BaseResponse<>(SUCCESS);
        } else {
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }

    /**
     * [2021.02.01] 4. 휴대폰 인증 번호 전송 API
     * [GET] /users/phone-auth?phoneNum=
     */
    @PostMapping("/phone-auth")
    public BaseResponse<String> phoneAuth(@RequestParam(value = "phoneNum") String phoneNum) throws IOException, ParseException {
        if (phoneNum == null || phoneNum.length() == 0) {
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        } else if (!isRegexPhoneNumber(phoneNum)) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        /**
         * 랜덤한 인증코드 여섯 자리를 생성후 db에 저장하고 메시지 전송
         */
        int rand = (int) (Math.random() * 899999) + 100000;
        try {
            userInfoService.PostSecureCode(rand, phoneNum);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.02.11] 5. 휴대폰 인증 번호 검증 API
     * @param phoneNum
     * @param getCertificationCodeReq
     * @return
     * @throws BaseException
     */

    @ResponseBody
    @GetMapping("/phone-auth")
    public BaseResponse<Void> phoneAuthCheck(@RequestParam(value = "phoneNum") String phoneNum, @RequestBody GetCertificationCodeReq getCertificationCodeReq) throws BaseException {
        //전송된 휴대폰 번호로 Certifiacte 테이블 조회
        if (getCertificationCodeReq.getCode() == null) {
            return new BaseResponse<>(EMPTY_CODE);
        }
        System.out.println(getCertificationCodeReq.getCode());

        try {
            int code = userInfoProvider.checkPhoneNumCode(phoneNum);
            if(code==getCertificationCodeReq.getCode()){
                return new BaseResponse<>(SUCCESS);
            }else{
                return new BaseResponse<>(WRONG_SECURE_CODE);
            }
        } catch (BaseException exception) {
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
            return new BaseResponse<>(SUCCESS, postLoginRes);
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
    public BaseResponse<GetUserRes> jwt() {
        GetUserRes getUserRes;
        try {
            int userIdx = jwtService.getUserIdx();
            getUserRes = userInfoProvider.getUserRes(userIdx);
            return new BaseResponse<>(SUCCESS,getUserRes);
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
            GetIdRes getIdRes = userInfoProvider.SendId(phoneNumber);
            return new BaseResponse<>(SUCCESS,getIdRes);
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
            return new BaseResponse<GetNewPasswordRes>(SUCCESS,getNewPasswordRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 사업자 인증 API
     * @param corpNum
     * @return
     * @throws RemoteException
     */
    @GetMapping("/corp-auth")
    public BaseResponse<String> getCorpState(@RequestParam(value="corpNum",required = true)String corpNum) throws RemoteException {
        if(corpNum==null || corpNum.length()==0){
            return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
        }
        if(corpNum.length()>13 || corpNum.length()<10){
            return  new BaseResponse<>(WRONG_CORP_NUM);
        }
        CorpState corpState = barobillApiService.corpState.getCorpState(barobillCertyKey,barbobillCorpNum,corpNum);
        int state = corpState.getState();
        if(state>=1){
            return new BaseResponse<>(SUCCESS);
        }else{
            return new BaseResponse<>(FAILED_TO_GET_CORP_AUTHENTICATION);
        }
    }


    /**
     * [2021.02.11] 마이페이지 조회 API
     * @return
     */
    @GetMapping("/{userIdx}")
    public BaseResponse<GetMyUserInfoRes> getMyUserInfo(@PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetMyUserInfoRes getMyUserInfoRes = userInfoProvider.getMyInfo(userIdx);
            return new BaseResponse<>(SUCCESS, getMyUserInfoRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [2021.02.12] 전문가 전환 API
     * @param patchUserTypeReq
     * @return
     */
    @PatchMapping("/{userIdx}/type")
    @ResponseBody
    public BaseResponse<String> changeUserTypeToExpert(@RequestBody PatchUserTypeReq patchUserTypeReq,
                                                       @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            userInfoService.changeUserTypeToExpert(userIdx,patchUserTypeReq);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("{userIdx}/password")
    @ResponseBody
    public BaseResponse<String> patchMyPassword(@RequestBody PatchUserPasswordReq patchUserPasswordReq,
                                                @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(patchUserPasswordReq.getCurrentPassword()==null || patchUserPasswordReq.getCurrentPassword().length()==0){
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        if(patchUserPasswordReq.getNewPassword()==null || patchUserPasswordReq.getNewPassword().length()==0){
            return new BaseResponse<>(EMPTY_NEW_PASSWORD);
        }
        if (!isRegexPassword(patchUserPasswordReq.getNewPassword())) {
            return new BaseResponse<>(INVALID_PASSWORD);
        }
        if(patchUserPasswordReq.getConfirmNewPassword()==null || patchUserPasswordReq.getConfirmNewPassword().length()==0){
            return new BaseResponse<>(EMPTY_NEW_CONFIRM_PASSWORD);
        }
        if (!patchUserPasswordReq.getNewPassword().equals(patchUserPasswordReq.getConfirmNewPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }
        try{
            userInfoService.patchMyPassword(userIdx, patchUserPasswordReq);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }




}