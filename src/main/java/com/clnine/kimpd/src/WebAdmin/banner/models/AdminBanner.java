package com.clnine.kimpd.src.WebAdmin.banner.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="Banner")
public class AdminBanner extends BaseEntity {
    @Id @Column(name="bannerIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bannerIdx;

    @Column(name="bannerImageURL")
    private String bannerImageURL;

    @Column(name="bannerLink")
    private String bannerLink;

    @Column(name="startDate")
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;

    private String status="ACTIVE";

    public AdminBanner(String bannerImageURL, String bannerLink, Date startDate, Date endDate) {
        this.bannerImageURL = bannerImageURL;
        this.bannerLink = bannerLink;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
