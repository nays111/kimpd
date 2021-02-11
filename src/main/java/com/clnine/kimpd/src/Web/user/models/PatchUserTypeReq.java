package com.clnine.kimpd.src.Web.user.models;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PatchUserTypeReq {
    private final List<Integer> jobParentCategoryIdx;
    private final List<Integer> jobChildCategoryIdx;
    private final List<Integer> genreCategoryIdx;
    private final int agreeShowDB;
}
