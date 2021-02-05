package com.clnine.kimpd.src.Web.banner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBannersRes {
    private final int bannerIdx;
    private final String bannerImageURL;
    private final String bannerLink;

}
