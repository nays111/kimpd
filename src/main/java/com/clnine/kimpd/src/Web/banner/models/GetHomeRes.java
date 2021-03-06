package com.clnine.kimpd.src.Web.banner.models;

import com.clnine.kimpd.src.Web.category.models.GetJobCategoryParentRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetHomeRes {
    List<GetBannersRes> getBannersResList;
    List<GetJobCategoryParentRes> getJobCategoryParentResList;

    public GetHomeRes(List<GetBannersRes> getBannersResList, List<GetJobCategoryParentRes> getJobCategoryParentResList) {
        this.getBannersResList = getBannersResList;
        this.getJobCategoryParentResList = getJobCategoryParentResList;
    }
}
