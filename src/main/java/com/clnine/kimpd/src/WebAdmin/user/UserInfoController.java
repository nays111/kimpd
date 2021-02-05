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
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;

    @Autowired
    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService, JwtService jwtService) {
        this.userInfoProvider = userInfoProvider;
        this.userInfoService = userInfoService;
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
    public BaseResponse<GetUserListRes> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUserRes> getUserResList = userInfoProvider.retrieveUserInfoList(word);
            GetUserListRes userInfo = new GetUserListRes(getUserResList);
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
    public BaseResponse<GetUserRes> getUser(@PathVariable Integer userId) {
        if (userId == null || userId <= 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }

        try {
            GetUserRes getUserRes = userInfoProvider.retrieveUserInfo(userId);
            return new BaseResponse<>(SUCCESS_READ_USER, getUserRes);
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
    public BaseResponse<PostAdminUserRes> postUsers(@RequestBody PostAdminUserReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Post UserInfo
        try {
            PostAdminUserRes postAdminUserRes = userInfoService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, postAdminUserRes);
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
    public BaseResponse<PatchUserPwRes> patchUsers(@RequestBody PatchUserPwReq parameters) {


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
            return new BaseResponse<>(SUCCESS_PATCH_USER, userInfoService.updateUserInfo(parameters));
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
    public BaseResponse<PostAdminLoginRes> login(@RequestBody PostAdminLoginReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Login
        try {
            PostAdminLoginRes postAdminLoginRes = userInfoProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, postAdminLoginRes);
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
}