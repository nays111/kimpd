package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostAdminLoginRes {
    private final String userId;
    private final String jwt;
}
