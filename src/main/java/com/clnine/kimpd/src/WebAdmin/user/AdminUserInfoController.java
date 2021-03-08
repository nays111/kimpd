package com.clnine.kimpd.src.WebAdmin.user;

import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.CorpState;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import com.clnine.kimpd.utils.BarobillService;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.config.secret.Secret.*;

@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminUserInfoController {
    private final AdminUserInfoProvider adminUserInfoProvider;
    private final AdminUserInfoService adminUserInfoService;
    private final BarobillApiService barobillApiService;
    private final JwtService jwtService;

//    @Autowired
//    public AdminUserInfoController(AdminUserInfoProvider adminUserInfoProvider, AdminUserInfoService adminUserInfoService,
//                                   JwtService jwtService, BarobillApiService barobillApiService) {
//        this.adminUserInfoProvider = adminUserInfoProvider;
//        this.adminUserInfoService = adminUserInfoService;
//        this.jwtService = jwtService;
//        this.barobillApiService = barobillApiService;
//    }

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     * @return BaseResponse<GetUserListRes>
     */
    @ResponseBody
    @GetMapping("/users") // (GET) 127.0.0.1:8080/web-admin/users
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetUsersListRes> getUsers(@RequestParam(required = false) String word) {
        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }
            List<AdminGetUsersRes> adminGetUsersResList = adminUserInfoProvider.retrieveUserInfoList(word);
            AdminGetUsersListRes userInfo = new AdminGetUsersListRes(adminGetUsersResList);

            return new BaseResponse<>(SUCCESS_READ_USERS, userInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 조회 API
     * [GET] /users/:userIdx
     * @PathVariable userIdx
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/users/{userIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetUserRes> getUser(@PathVariable Integer userIdx) {
        if (userIdx == null || userIdx <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }
            AdminGetUserRes adminGetUserRes = adminUserInfoProvider.retrieveUserInfo(userIdx);
            return new BaseResponse<>(SUCCESS_READ_USER, adminGetUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * admin 회원가입 API
     * [POST] /sign-up
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("/sign-up")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPostAdminRes> postUsers(@RequestBody AdminPostAdminReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Post UserInfo
        try {
            AdminPostAdminRes adminPostAdminRes = adminUserInfoService.createAdminInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, adminPostAdminRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /users/{userIdx}
     * @RequestBody PatchUserReq
     * * @PathVariable userIdx
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/users")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchUsers(@RequestBody AdminPatchUserReq parameters) {
        if (parameters.getUserType() == null || parameters.getUserType().length() <= 0){
            return new BaseResponse<>(EMPTY_USER_TYPE);
        }

        if (parameters.getId() == null || parameters.getId().length() <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        if (parameters.getEmail() == null || parameters.getEmail().length() <= 0) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }

        if(parameters.getUserType().equals("제작사-개인") || parameters.getUserType().equals("전문가-개인")){
            if(parameters.getPrivateBusinessName() == null || parameters.getPrivateBusinessName().length() == 0){
                return new BaseResponse<>(EMPTY_PRIVATE_BUSINESS_NAME);
            }
            if(parameters.getBusinessNumber() == null || parameters.getBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
            if(parameters.getBusinessImageURL() == null || parameters.getBusinessImageURL().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }

        if(parameters.getUserType().equals("제작사-법인") || parameters.getUserType().equals("전문가-법인")){
            if(parameters.getCorporationBusinessName() == null || parameters.getCorporationBusinessName().length() == 0){
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NAME);
            }
            if(parameters.getBusinessNumber() == null || parameters.getBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
            if(parameters.getCorporationBusinessNumber() == null || parameters.getCorporationBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NUMBER);
            }
            if(parameters.getBusinessImageURL() == null || parameters.getBusinessImageURL().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }

        if(parameters.getName() == null || parameters.getName().length() == 0){
            return new BaseResponse<>(EMPTY_NAME);
        }

        if(parameters.getNickname() == null || parameters.getNickname().length() == 0){
            return new BaseResponse<>(EMPTY_NICKNAME);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminUserInfoService.updateUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 사용자 비밀번호 수정 API
     * [PATCH] /user-password
     * @RequestBody AdminPatchUserPwReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/user-password")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchUsersPassword(@RequestBody AdminPatchUserPwReq parameters) {

        if (parameters.getUserIdx() <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        System.out.println(parameters.getEmail());
        if (parameters.getEmail() == null || parameters.getEmail().length() <= 0) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminUserInfoService.updateUserPw(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_USER_PASSWORD);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * admin 비밀번호 수정 API
     * [PATCH] /admin-password
     * @RequestBody AdminPatchAdminPwReq
     * @return BaseResponse<AdminPatchAdminPwRes>
     */
    @ResponseBody
    @PatchMapping("/admin-password")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPatchAdminPwRes> patchUsers(@RequestBody AdminPatchAdminPwReq parameters) {


        if (parameters.getId() == null || parameters.getId().length() <= 0){
            return new BaseResponse<>(EMPTY_USERID);
        }

        if (parameters.getPassword() == null || parameters.getPassword().length() <= 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }

        if (parameters.getNewPassword() == null || parameters.getNewPassword().length() <= 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }

        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() <= 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }

        if (!parameters.getNewPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            return new BaseResponse<>(SUCCESS_PATCH_USER, adminUserInfoService.updateAdminInfo(parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * admin 로그인 API
     * [POST] /sign-in
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/sign-in")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPostLoginRes> login(@RequestBody AdminPostLoginReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Login
        try {
            AdminPostLoginRes adminPostLoginRes = adminUserInfoProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, adminPostLoginRes);
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
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminUserInfoService.deleteUserInfo(userId);
            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 등록 API
     * [POST] /users
     * @RequestBody PostNormalUserReq
     * @return BaseResponse<PostNormalUserRes>
     */
    @ResponseBody
    @PostMapping("/users")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPostUserRes> postNormalUsers(@RequestBody AdminPostUserReq parameters) {
        // 1. Body Parameter Validation
        if(parameters.getUserType() > 6 || parameters.getUserType() < 0){
            return new BaseResponse<>(INVALID_USER_TYPE);
        }
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        if(parameters.getEmail() == null || parameters.getEmail().length() == 0){
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        if(parameters.getPhoneNum() == null || parameters.getPhoneNum().length() == 0){
            return new BaseResponse<>(EMPTY_PHONE_NUMBER);
        }

        if(parameters.getUserType() == 2 || parameters.getUserType() == 5){
            if(parameters.getPrivateBusinessName() == null || parameters.getPrivateBusinessName().length() == 0){
                return new BaseResponse<>(EMPTY_PRIVATE_BUSINESS_NAME);
            }
            if(parameters.getBusinessNumber() == null || parameters.getBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
            if(parameters.getBusinessImageURL() == null || parameters.getBusinessImageURL().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }

        if (parameters.getUserType() == 3 || parameters.getUserType() == 6){
            if (parameters.getCorporationBusinessName() == null || parameters.getCorporationBusinessName().length() == 0){
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NAME);
            }
            if (parameters.getBusinessNumber() == null || parameters.getBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_NUMBER);
            }
            if (parameters.getCorporationBusinessNumber() == null || parameters.getCorporationBusinessNumber().length() == 0){
                return new BaseResponse<>(EMPTY_CORP_BUSINESS_NUMBER);
            }
            if (parameters.getBusinessImageURL() == null || parameters.getBusinessImageURL().length() == 0){
                return new BaseResponse<>(EMPTY_BUSINESS_IMAGE);
            }
        }

        if (adminUserInfoProvider.isIdUseable(parameters.getId()) == false) {
            return new BaseResponse<>(DUPLICATED_ID);
        }

        if (adminUserInfoProvider.isEmailUseable(parameters.getEmail()) == false) {
            return new BaseResponse<>(DUPLICATED_PHONE_NUMBER);
        }

        if (adminUserInfoProvider.isPhoneNumUseable(parameters.getPhoneNum()) == false) {
            return new BaseResponse<>(DUPLICATED_EMAIL);
        }

        if(parameters.getName() == null || parameters.getName().length() == 0){
            return new BaseResponse<>(EMPTY_NAME);
        }

        if(parameters.getNickname() == null || parameters.getNickname().length() == 0){
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        else{
            if (adminUserInfoProvider.isNicknameUseable(parameters.getNickname()) == false) {
                return new BaseResponse<>(DUPLICATED_NICKNAME);
            }
        }

        // 2. Post UserInfo
        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminPostUserRes adminPostUserRes = adminUserInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, adminPostUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 사업자 인증 API
     * [GET] /corp-auth
     * @RequestBody PostNormalUserReq
     * @return BaseResponse<PostNormalUserRes>
     */
    @ResponseBody
    @GetMapping("/corp-auth")
    @CrossOrigin(origins = "*")
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

}