package com.clnine.kimpd.src.banner.models;

import com.clnine.kimpd.src.category.models.JobCategoryParent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.Banner;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetBannersRes {
    private final int bannerIdx;
    private final String bannerImageURL;
    private final String bannerLink;

}
