package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class AdminGetUsersRes {
    private final int userIdx;
    private final String userType;
    private final String id;
    private final String email;
    private final String phoneNum;
    private final String nickname;
    private final String status;
}
