package com.clnine.kimpd.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final int userIdx;
    private final String nickname;
    private final String profileImageURL;
    private final String userType;

}