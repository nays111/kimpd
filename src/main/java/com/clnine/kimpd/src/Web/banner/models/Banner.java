package com.clnine.kimpd.src.Web.banner.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Banner")
public class Banner extends BaseEntity {
    @Id @Column(name="bannerIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bannerIdx;

    @Column(name="bannerImageURL")
    private String bannerImageURL;

    @Column(name="bannerLink")
    private String bannerLink;

    private String status="ACTIVE";

    public Banner(int bannerIdx, String bannerImageURL, String bannerLink) {
        this.bannerIdx = bannerIdx;
        this.bannerImageURL = bannerImageURL;
        this.bannerLink = bannerLink;
    }
}
