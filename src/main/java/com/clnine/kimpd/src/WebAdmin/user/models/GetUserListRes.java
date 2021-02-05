package com.softsquared.template.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserListRes {
    List<GetUserRes> userInfo;
}