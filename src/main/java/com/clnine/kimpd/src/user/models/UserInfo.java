package com.clnine.kimpd.src.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo extends BaseEntity {
    /**
     * 유저 인덱스
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(name="userType",nullable = false)
    private int userType;

    @Column(name="id",nullable = false)
    private String id;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="phoneNum",nullable = false)
    private String phoneNum;

    @Column(name="address")
    private String address;

    @Column(name="agreeAdvertisement")
    private int agreeAdvertisement;

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
    private String nickname;//닉네임

    /**
     * 전문가 전용 컬럼
     */
    @Column(name="profileImageURL")
    private String profileImageURL;//프로필이미지
    @Column(name="introduce")
    private String introduce;
    @Column(name="career")
    private String career;
    @Column(name="etc")
    private String etc;
    @Column(name="agreeShowDB")
    private int agreeShowDB=1;


    private String status="ACTIVE";

    public UserInfo(int userType, String id, String password,
                    String email, String phoneNumber,String introduce) {
        this.userType = userType;
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNum=phoneNumber;
        this.introduce = introduce;
    }


}
