package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetUsersListRes {
    List<AdminGetUsersRes> userInfo;
}