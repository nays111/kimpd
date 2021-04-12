package com.clnine.kimpd.src.Web.alarm;

import com.clnine.kimpd.src.Web.alarm.models.Alarm;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlarmRepository extends CrudRepository<Alarm,Integer> {
    List<Alarm> findAllByUserInfoAndStatusOrderByAlarmIdxDesc(UserInfo userInfo,String status);
}
