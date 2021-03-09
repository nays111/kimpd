package com.clnine.kimpd.src.Web.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.ArrayList;


@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    //필수 컬럼
    private int userType;
    private String id;
    private String password;
    private String confirmPassword;
    private String email;
    private String name;
    private Integer agreeAdvertisement;
    private String phoneNum;
    private String city;

    @Nullable
    private String address;
    @Nullable
    private String privateBusinessName;//개인사업자명
    @Nullable
    private String businessNumber; //사업자 등록번호
    @Nullable
    private String businessImageURL; //사업자 등록증
    @Nullable
    private String corporationBusinessName; //법인 사업자명
    @Nullable
    private String corporationBusinessNumber;//법인 등록번호
    @Nullable
    private String nickname;
    @Nullable
    private ArrayList<Integer> genreCategoryIdx; //장르 카테고리 인덱스
    @Nullable
    private ArrayList<ArrayList<Integer>> jobCategoryIdx;
    @Nullable
    private Integer agreeShowDB;
}
