package com.clnine.kimpd.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final int userIdx;
    private final String profileImageURL;
    //private final String jobCategoryName;
    private final String introduce;
    private final double star;
    private final int reviewCount;
}
