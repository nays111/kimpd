package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostAdminLoginReq {
    private String id;
    private String password;
}
