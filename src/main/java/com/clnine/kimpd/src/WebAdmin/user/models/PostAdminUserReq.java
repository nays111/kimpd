package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostAdminUserReq {
    private String id;
    private String password;
}
