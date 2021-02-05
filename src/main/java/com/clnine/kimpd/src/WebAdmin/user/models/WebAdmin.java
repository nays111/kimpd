package com.clnine.kimpd.src.WebAdmin.user.models;

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
@Table(name = "WebAdmin") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class WebAdmin extends BaseEntity{
    /**
     * 어드민 ID
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 비밀번호
     */
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false, length = 10)
    private String status = "ACTIVE";

    public WebAdmin(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
