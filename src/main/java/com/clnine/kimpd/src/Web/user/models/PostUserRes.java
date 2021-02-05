package com.clnine.kimpd.src.Web.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserRes {
    private final int userIdx;
    private final String jwt;
}
