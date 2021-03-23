package com.clnine.kimpd.src.WebAdmin.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class AdminUserInfo extends BaseEntity {
    /**
     * 유저 ID
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    /**
     * 유저 타입
     */
    @Column(name = "userType", nullable = false)
    private int userType;

    /**
     * 아이디
     */
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 비밀번호
     */
    @Column(name="password",nullable = false)
    private String password;

    /**
     * 이메일
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 전화번호
     */
    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;

    /**
     * 전화번호
     */
    @Column(name = "name", nullable = false)
    private String name;


    /**
     * 도시
     */
    @Column(name = "city")
    private String city;

    /**
     * 별명
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 프로필 이미지
     */
    @Column(name = "profileImageURL")
    private String profileImageURL;

    /**
     * 전문가 소개
     */
    @Column(name = "introuduce")
    private String introduce;

    /**
     * 주요 경력
     */
    @Column(name = "career")
    private String career;

    /**
     * 기타 요청사항
     */
    @Column(name = "etc")
    private String etc;

    /**
     * 최소 섭외 비용
     */
    @Column(name = "minimumCastingPrice")
    private String minimumCastingPrice;

    /**
     * 개인 사업명
     */
    @Column(name = "privateBusinessName")
    private String privateBusinessName;

    /**
     * 사업자 등록번호
     */
    @Column(name = "businessNumber")
    private String businessNumber;

    /**
     * 사업자 등록 이미지
     */
    @Column(name = "businessImageURL")
    private String businessImageURL;

    /**
     * 법인 사업자명
     */
    @Column(name = "corporationBusinessName")
    private String corporationBusinessName;

    /**
     * 법인 사업자 번호
     */
    @Column(name = "corporationBusinessNumber")
    private String corporationBusinessNumber;

    @Column(name="agreeAdvertisement")
    private int agreeAdvertisement=1;

    @Column(name="agreeShowDB")
    private int agreeShowDB=0;

    /**
     * 상태
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status = "ACTIVE";

    public AdminUserInfo(int userType, String id, String password, String email, String phoneNum, String name, String city,
                         String nickname, String profileImageURL, String introduce, String career, String etc,
                         String minimumCastingPrice, String privateBusinessName, String businessNumber,
                         String businessImageURL, String corporationBusinessName, int agreeShowDB, String status) {
        this.userType = userType;
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.name = name;
        this.city = city;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
        this.introduce = introduce;
        this.career = career;
        this.etc = etc;
        this.minimumCastingPrice = minimumCastingPrice;
        this.privateBusinessName = privateBusinessName;
        this.businessNumber = businessNumber;
        this.businessImageURL = businessImageURL;
        this.corporationBusinessName = corporationBusinessName;
        this.agreeShowDB = agreeShowDB;
        this.status = status;
    }
}
