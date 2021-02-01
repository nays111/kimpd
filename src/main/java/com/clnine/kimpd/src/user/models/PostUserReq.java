package com.clnine.kimpd.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private int userType;
    private String id;
    private String password;
    private String confirmPassword;
    private String email;
    private int agreeAdvertisement;
    private String phoneNum;
}
