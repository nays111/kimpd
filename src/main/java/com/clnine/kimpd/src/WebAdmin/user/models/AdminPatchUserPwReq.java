package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchUserPwReq {
    private int userIdx;
    private String email;
}
