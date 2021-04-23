package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.src.Web.expert.models.ExpertCastingDate;
import com.clnine.kimpd.src.Web.expert.models.Portfolio;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExpertCastingDateRepository extends CrudRepository<ExpertCastingDate,Integer> {
    List<ExpertCastingDate> findAllByUserInfo(UserInfo userInfo);
}
