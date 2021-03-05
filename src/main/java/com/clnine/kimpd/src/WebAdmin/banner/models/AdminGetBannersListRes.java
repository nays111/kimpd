package com.clnine.kimpd.src.WebAdmin.banner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetBannersListRes {
    List<AdminGetBannersRes> bannerList;
}