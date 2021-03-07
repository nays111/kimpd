package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.user.models.Certification;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface CertificateRepository extends CrudRepository <Certification,Integer>{

    Certification findTopByUserPhoneNumAndCreatedAtBetweenOrderByCertificationIdxDesc(String phoneNum, Date now, Date end);
    List<Certification> findAllByUserPhoneNum(String phoneNum);

}
