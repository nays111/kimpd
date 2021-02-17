package com.clnine.kimpd.src.Web.alarm;

import com.clnine.kimpd.src.Web.alarm.models.Alarm;
import org.springframework.data.repository.CrudRepository;

public interface AlarmRepository extends CrudRepository<Alarm,Integer> {
}
