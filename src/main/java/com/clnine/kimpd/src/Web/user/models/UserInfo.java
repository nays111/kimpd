package com.clnine.kimpd.src.Web.user.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity @Data @Table(name="UserInfo") @NoArgsConstructor
public class UserInfo extends BaseEntity {

    @Id
    @Column(name="userIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(name="userType",nullable = false)
    private int userType;

    @Column(name="id",nullable = false,length = 20)
    private String id;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="phoneNum",nullable = false)
    private String phoneNum;

    @Column(name="city")
    private String city;

    @Column(name="address")
    private String address;

    @Column(name="agreeAdvertisement")
    private Integer agreeAdvertisement;

    @Column(name="privateBusinessName")
    private String privateBusinessName;//개인 사업자명
    @Column(name="businessNumber")
    private String businessNumber;//사업자 등록번호
    @Column(name="businessImageURL")
    private String businessImageURL;//사업자 등록 이미지
    @Column(name="corporationBusinessName")
    private String corporationBusinessName;//법인 사업자명
    @Column(name="corporationBusinessNumber")
    private String corporationBusinessNumber;//법인 사업자번호

    @Column(name="nickname")
    private String nickname="";//닉네임

    /**
     * 전문가 전용 컬럼
     */
    @Column(name="profileImageURL")
    private String profileImageURL;//프로필이미지

    @Column(name="introduce")
    private String introduce="";

    @Column(name="career")
    private String career;

    @Column(name="etc")
    private String etc;

    @Column(name="minimumCastingPrice")
    private Integer minimumCastingPrice=0;

    //섭외 가능 시작 날짜
    private String castingPossibleStartDate="";

    //섭외 가능 종료 잘짜
    private String castingPossibleEndDate="";


    @Column(name="agreeShowDB")
    private Integer agreeShowDB;

    @Column(name="status")
    private String status="ACTIVE";

    public UserInfo(int userType, String id, String password,
                    String email, String name, String phoneNum, String city,String address,
                    Integer agreeAdvertisement, String privateBusinessName,
                    String businessNumber, String businessImageURL,
                    String corporationBusinessName, String corporationBusinessNumber,
                    String nickname,Integer agreeShowDB) {
        this.userType = userType;
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.city = city;
        this.address = address;
        this.agreeAdvertisement = agreeAdvertisement;
        this.privateBusinessName = privateBusinessName;
        this.businessNumber = businessNumber;
        this.businessImageURL = businessImageURL;
        this.corporationBusinessName = corporationBusinessName;
        this.corporationBusinessNumber = corporationBusinessNumber;
        this.nickname = nickname;
        this.agreeShowDB = agreeShowDB;
    }


}
