package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final int userId;
    private final String email;
}
