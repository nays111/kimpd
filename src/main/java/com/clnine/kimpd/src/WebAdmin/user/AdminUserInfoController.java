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
    @GetMapping("/users") // (GET) 127.0.0.1:9000/web-admin/users
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetUserListRes> getUsers(@RequestParam(required = false) String word) {
        try {
            List<AdminGetUserRes> adminGetUserResList = adminUserInfoProvider.retrieveUserInfoList(word);
            AdminGetUserListRes userInfo = new AdminGetUserListRes(adminGetUserResList);
            if (word == null) {
                return new BaseResponse<>(SUCCESS_READ_USERS, userInfo);
            } else {
                return new BaseResponse<>(SUCCESS_READ_SEARCH_USERS, userInfo);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 조회 API
     * [GET] /users/:userId
     * @PathVariable userId
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/users/{userId}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetUserRes> getUser(@PathVariable Integer userId) {
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        try {
            AdminGetUserRes adminGetUserRes = adminUserInfoProvider.retrieveUserInfo(userId);
            return new BaseResponse<>(SUCCESS_READ_USER, adminGetUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원가입 API
     * [POST] /sign-up
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("/sign-up")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPostUserRes> postUsers(@RequestBody AdminPostUserReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Post UserInfo
        try {
            AdminPostUserRes adminPostUserRes = adminUserInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, adminPostUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 수정 API
     * [PATCH] /password
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @ResponseBody
    @PatchMapping("/password")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminPatchUserPwRes> patchUsers(@RequestBody AdminPatchUserPwReq parameters) {


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
            return new BaseResponse<>(SUCCESS_PATCH_USER, adminUserInfoService.updateUserInfo(parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API
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
}