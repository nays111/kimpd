package com.clnine.kimpd.src.Web.user.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchUserPasswordReq {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
