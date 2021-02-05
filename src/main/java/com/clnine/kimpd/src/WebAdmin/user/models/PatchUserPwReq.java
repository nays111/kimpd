package com.softsquared.template.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PatchUserPwReq {
    private String userId;
    private String password;
    private String newPassword;
    private String confirmPassword;
}
