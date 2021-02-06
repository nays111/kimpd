package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetUserRes {
    private final int userIdx;
    private final String userType;
    private final String id;
    private final String email;
    private final String phoneNum;
    private final String address;
}