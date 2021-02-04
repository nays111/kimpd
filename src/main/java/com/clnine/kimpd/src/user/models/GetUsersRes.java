package com.clnine.kimpd.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final int userIdx;
    private final String nickname;
    private final String profileImageURL;
    private final ArrayList<String> jobCategoryParentName;
    private final String introduce;

    private final int reviewCount;
    private final double reviewAverage;
}
