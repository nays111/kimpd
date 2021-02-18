package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchAdminPwReq {
    private String userId;
    private String password;
    private String newPassword;
    private String confirmPassword;
}
