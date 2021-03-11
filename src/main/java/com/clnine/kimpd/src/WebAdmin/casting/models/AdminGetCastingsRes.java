package com.clnine.kimpd.src.WebAdmin.casting.models;

import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetCastingsRes {
    private final int castingIdx;
    private final String userNickname;
    private final String expertNickname;
    private final String projectName;
    private final String castingWork;
    private final String castingStatus;
    private final int reviewIdx;
    private final String reviewStatus;
    private final String status;
}
