package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.user.models.Certification;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.Date;

public interface CertificateRepository extends CrudRepository <Certification,Integer>{

    Certification findTopByUserPhoneNumAndCreatedAtBetweenOrderByCertificationIdxDesc(String phoneNum, Date now, Date end);
    Certification findTopByUserPhoneNumOrderByCertificationIdxDesc(String phoneNum);

}
