package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import com.clnine.kimpd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/web-admin")
public class AdminUserInfoController {
    private final AdminUserInfoProvider adminUserInfoProvider;
    private final AdminUserInfoService adminUserInfoService;
    private final JwtService jwtService;

    @Autowired
    public AdminUserInfoController(AdminUserInfoProvider adminUserInfoProvider, AdminUserInfoService adminUserInfoService, JwtService jwtService) {
        this.adminUserInfoProvider = adminUserInfoProvider;
        this.adminUserInfoService = adminUserInfoService;
        this.jwtService = jwtService;
    }

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

        if(parameters.getNickname() == null){
            return new BaseResponse<>(EMPTY_NICKNAME);
        }

        try {
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


        if (parameters.getUserId() == null || parameters.getUserId().length() <= 0){
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

        if(parameters.getUserType() == 3 || parameters.getUserType() == 6){
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

        if (adminUserInfoProvider.isIdUseable(parameters.getId()) == false) {
            return new BaseResponse<>(DUPLICATED_ID);
        }

        if (adminUserInfoProvider.isEmailUseable(parameters.getEmail()) == false) {
            return new BaseResponse<>(DUPLICATED_PHONE_NUMBER);
        }

        if (adminUserInfoProvider.isPhoneNumUseable(parameters.getPhoneNum()) == false) {
            return new BaseResponse<>(DUPLICATED_EMAIL);
        }

        if(parameters.getNickname() == null){
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        else{
            if (adminUserInfoProvider.isNicknameUseable(parameters.getNickname()) == false) {
                return new BaseResponse<>(DUPLICATED_NICKNAME);
            }
        }

        // 2. Post UserInfo
        try {
            AdminPostUserRes adminPostUserRes = adminUserInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, adminPostUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}