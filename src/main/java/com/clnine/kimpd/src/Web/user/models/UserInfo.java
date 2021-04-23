package com.clnine.kimpd.src.Web.user.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name="UserInfo")
@NoArgsConstructor
public class UserInfo extends BaseEntity {

    @Id
    @Column(name="userIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(name="userType",nullable = false,length = 1)
    private int userType;

    @Column(name="id",nullable = false,length = 20)
    private String id;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email",nullable = false,length = 45)
    private String email;

    @Column(name="name",nullable = false,length = 10)
    private String name;

    @Column(name="phoneNum",nullable = false)
    private String phoneNum;

    @Column(name="city",length = 10)
    private String city;

    @Column(name="address")
    private String address;

    @Column(name="agreeAdvertisement",length = 1)
    private Integer agreeAdvertisement;

    @Column(name="privateBusinessName")
    private String privateBusinessName;//개인 사업자명

    @Column(name="businessNumber",length = 10)
    private String businessNumber;//사업자 등록번호 (개입 사업자 + 법인 사업자 공통)

    @Column(name="businessImageURL",columnDefinition = "TEXT")
    private String businessImageURL;//사업자 등록 이미지 (개인사업자 + 법인사업자 공통)

    @Column(name="corporationBusinessName")
    private String corporationBusinessName;//법인 사업자명

//    @Column(name="corporationBusinessNumber")
//    private String corporationBusinessNumber;//법인 사업자번호

    @Column(name="nickname",length = 10)
    private String nickname;//닉네임

    /**
     * 전문가 전용 컬럼
     */
    @Column(name="profileImageURL",columnDefinition = "TEXT")
    private String profileImageURL="";//프로필이미지

    @Column(name="introduce",length = 500)
    private String introduce="";

    @Column(name="career",length = 500)
    private String career;

    @Column(name="etc",length = 500)
    private String etc;

    @Column(name="minimumCastingPrice")
    private Integer minimumCastingPrice=0;

//    //섭외 가능 시작 날짜
//    private String castingPossibleStartDate="";
//
//    //섭외 가능 종료 잘짜
//    private String castingPossibleEndDate="";

    @Column(name="agreeShowDB",length = 1)
    private Integer agreeShowDB;

    @Column(name="status")
    private String status="ACTIVE";

    public UserInfo(int userType, String id, String password,
                    String email, String name, String phoneNum, String city,String address,
                    Integer agreeAdvertisement, String privateBusinessName,
                    String businessNumber, String businessImageURL,
                    String corporationBusinessName, String nickname,Integer agreeShowDB) {
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
        this.nickname = nickname;
        this.agreeShowDB = agreeShowDB;
    }


}
