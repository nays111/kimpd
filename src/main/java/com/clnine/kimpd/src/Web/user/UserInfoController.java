package com.clnine.kimpd.src.Web.user;

import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.CorpState;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.config.secret.Secret.barbobillCorpNum;
import static com.clnine.kimpd.config.secret.Secret.barobillCertyKey;
import static com.clnine.kimpd.utils.ValidationRegex.*;


@RestController @CrossOrigin @RequestMapping(value = "/users") @RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final BarobillApiService barobillApiService;

    @ResponseBody @PostMapping("")
    @Operation(summary = "회원가입 API")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
        if (parameters.getUserType() > 6 || parameters.getUserType()==0) {
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
        if(parameters.getCity()==null || parameters.getCity().length()==0){
            return new BaseResponse<>(EMPTY_CITY);
        }
        if(parameters.getName()==null || parameters.getName().length()==0){
            return new BaseResponse<>(EMPTY_NAME);
        }
        if(!isRegexName(parameters.getName())){
            return new BaseResponse<>(INVALID_NAME);
        }
        if (parameters.getNickname().length() == 0 || parameters.getNickname()==null) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if (!isRegexNickname(parameters.getNickname())) {
            return new BaseResponse<>(INVALID_NICKNAME);
        }
        if(parameters.getAgreeAdvertisement()==null){
            return new BaseResponse<>(INVALID_AGREE_ADVERTISEMENT_CHECK);
        }
        if(parameters.getUserType()==2 || parameters.getUserType()==5){
            if (parameters.getPrivateBusinessName().length() == 0) {
                    return new BaseResponse<>(EMPTY_PRIVATE_BUSINESS_NAME);
            }
            if (parameters.getBusinessNumber().length() == 0) {
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
            if(parameters.getBusinessNumber().length()!=10){
                return new BaseResponse<>(INVALID_BUSINESS_NUMBER);
            }
            if (parameters.getBusinessImageURL().length() == 0) {
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }
        if(parameters.getUserType()==3 || parameters.getUserType()==6){
            if (parameters.getCorporationBusinessName().length() == 0) {
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NAME);
            }
            if (parameters.getCorporationBusinessNumber().length() == 0) {
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NUMBER);
            }
            if(parameters.getCorporationBusinessNumber().length()!=10){
                return new BaseResponse<>(INVALID_CORP_BUSINESS_NUMBER);
            }
            if (parameters.getBusinessImageURL().length() == 0) {
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }
        if(parameters.getUserType()==4 || parameters.getUserType()==5 || parameters.getUserType()==6){
            if (parameters.getJobParentCategoryIdx().size() == 0) {
                return new BaseResponse<>(NO_SELECT_JOB_PARENT_CATEGORY);
            }
            if (parameters.getJobChildCategoryIdx().size() == 0) {
                return new BaseResponse<>(NO_SELECT_JOB_CHILD_CATEGORY);
            }
            if (parameters.getGenreCategoryIdx().size() == 0) {
                return new BaseResponse<>(NO_SELECT_GENRE_CATEGORY);
            }
            if(parameters.getAgreeShowDB()==null){
                return new BaseResponse<>(NO_SELECT_AGREE_SHOW_DB);
            }
        }
        try {
            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody @GetMapping("/duplicated-id")
    @Operation(summary = "아이디 중복 체크 API")
    public BaseResponse<Void> checkIdDuplicate(@RequestParam(value = "id") String id) {
        if (id == null || id.length() == 0) {
            return new BaseResponse<>(EMPTY_ID);
        }
        if (!isRegexId(id)) {
            return new BaseResponse<>(INVALID_ID);
        }
        if (userInfoProvider.isIdUsable(id) == true) {
            return new BaseResponse<>(SUCCESS);
        } else {
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }
    @ResponseBody @GetMapping("/duplicated-nickname")
    @Operation(summary = "닉네임 중복 체크 API")
    public BaseResponse<String> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        if (nickname == null || nickname.length() == 0) {
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if (!isRegexNickname(nickname)) {
            return new BaseResponse<>(INVALID_NICKNAME);
        }
        if (userInfoProvider.isNicknameUsable(nickname) == true) {
            return new BaseResponse<>(SUCCESS);
        } else {
            return new BaseResponse<>(DUPLICATED_USER);
        }
    }
    @ResponseBody @GetMapping("/duplicated-info")
    @Operation(summary="이메일, 휴대폰 중복 체크 API")
    public BaseResponse<String> checkEmailAndPhoneNumDuplicate(@RequestParam(value="phoneNum")String phoneNum,
                                                               @RequestParam(value="email")String email){
        if(phoneNum==null || phoneNum.length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        if(email==null || email.length()==0){
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        if(!isRegexEmail(email)){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if(!isRegexPhoneNumber(phoneNum)){
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        if(userInfoProvider.isEmailUsable(email)==false){
            return new BaseResponse<>(DUPLICATED_EMAIL);
        }else if(userInfoProvider.isPhoneNumUsable(phoneNum)==false){
            return new BaseResponse<>(DUPLICATED_PHONE_NUMBER);
        }else{
            return new BaseResponse<>(SUCCESS);
        }
    }

    @GetMapping("/phone-auth")
    @Operation(summary = "휴대폰 인증 API",description = "휴대폰 번호는 -를 빼고 입력하세요.")
    public BaseResponse<String> phoneAuth(@RequestParam(value = "phoneNum") String phoneNum) throws IOException, ParseException {
        if (phoneNum == null || phoneNum.length() == 0) {
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        } else if (!isRegexPhoneNumber(phoneNum)) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        int rand = (int) (Math.random() * 899999) + 100000;
        try {
            userInfoService.postSecureCode(rand, phoneNum);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/phone-auth")
    @Operation(summary="휴대폰 인증번호 검사 API",description = "인증번호는 3분 이내의 가장 최신 것만 유효합니다.")
    public BaseResponse<Void> phoneAuthCheck(@RequestBody PostCertificationCodeReq postCertificationCodeReq) throws BaseException {
        if (postCertificationCodeReq.getPhoneNum() == null || postCertificationCodeReq.getPhoneNum().length() == 0) {
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }
        if (!isRegexPhoneNumber(postCertificationCodeReq.getPhoneNum())) {
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        if (postCertificationCodeReq.getCode() == null) {
            return new BaseResponse<>(EMPTY_CODE);
        }
        try {
            int code = userInfoProvider.checkPhoneNumCode(postCertificationCodeReq.getPhoneNum());
            if(code== postCertificationCodeReq.getCode()){
                userInfoProvider.deletePhoneNumCertCode(postCertificationCodeReq.getPhoneNum());
                return new BaseResponse<>(SUCCESS);
            }else{
                return new BaseResponse<>(WRONG_SECURE_CODE);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody @PostMapping("/login")
    @Operation(summary="로그인 API",description = "JWT를 반환합니다")
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

    @GetMapping("/jwt") @ResponseBody
    @Operation(summary="JWT 검사 API",description = "유저에 대한 요약 정보를 반환합니다.")
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

    @GetMapping("/id") @ResponseBody
    @Operation(summary="아이디 찾기 API",description = "입력한 휴대폰 번호로 이전에 가입했던 ID를 보냅니다. (휴대폰 번호는 -를 빼고 입력해주세요.)")
    public BaseResponse<String> lostId(@RequestParam(value="phoneNum")String phoneNumber,
                                       @RequestParam(value="name")String name){
        if(phoneNumber==null || phoneNumber.length()==0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }else if(!isRegexPhoneNumber(phoneNumber)){
            return new BaseResponse<>(INVALID_PHONE_NUMBER);
        }
        if(name==null || name.length()==0){
            return new BaseResponse<>(EMPTY_NAME);
        }
        if(!isRegexName(name)){
            return new BaseResponse<>(INVALID_NAME);
        }
        try{
            userInfoProvider.sendId(phoneNumber,name);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/password") @ResponseBody
    @Operation(summary="비밀번호 찾기 API",description = "입력했던 메일로 새로운 랜덤한 비밀번호를 발급합니다.")
    public BaseResponse<String> lostPassword(@RequestParam(value="email") String userEmail){
        if(userEmail==null || userEmail.length()==0){
            return new BaseResponse<>(EMPTY_EMAIL);
        }else if(!isRegexEmail(userEmail)){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        try {
            userInfoService.patchUserPassword(userEmail);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/corp-auth") @ResponseBody
    @Operation(summary="사업자 인증 API",description = "사업자 번호는 10자리입니다 (-를 빼고 입력해주세요.)")
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

    @GetMapping("/{userIdx}") @ResponseBody
    @Operation(summary="마이페이지 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMyUserInfoRes> getMyUserInfo(@PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){ return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX); }
        try{
            GetMyUserInfoRes getMyUserInfoRes = userInfoProvider.getMyInfo(userIdx);
            return new BaseResponse<>(SUCCESS, getMyUserInfoRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{userIdx}") @ResponseBody
    @Operation(summary="회원 정보 수정 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchMyUserInfo(@PathVariable(required = true,value="userIdx")int userIdx,
                                                @RequestBody PatchMyUserInfoReq patchMyUserInfoReq){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){ return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX); }
        //todo requestbody validation
        try{
            userInfoService.patchMyUserInfo(userIdx,patchMyUserInfoReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @PatchMapping("/{userIdx}/type") @ResponseBody
    @Operation(summary="전문가 전환 API",description = "일반->전문가, 일반(개인사업자)->전문가(개인사업자), 일반(법인사업자)->전문가(법인사업자), 토큰이 필요합니다.")
    public BaseResponse<String> changeUserTypeToExpert(@RequestBody PatchUserTypeReq patchUserTypeReq,
                                                       @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){ return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX); }

        try{
            userInfoService.changeUserTypeToExpert(userIdx,patchUserTypeReq);
            return new BaseResponse<String>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("{userIdx}/password") @ResponseBody
    @Operation(summary="비밀번호 수정 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> patchMyPassword(@RequestBody PatchUserPasswordReq patchUserPasswordReq,
                                                @PathVariable(required = true,value = "userIdx")int userIdx){
        int userIdxJWT;
        try{
            userIdxJWT = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(userIdxJWT!=userIdx){ return new BaseResponse<>(DIFFERENT_JWT_AND_USERIDX); }

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