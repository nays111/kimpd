package com.clnine.kimpd.src.WebAdmin.banner;

import com.clnine.kimpd.src.WebAdmin.banner.models.AdminBanner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminBannerRepository extends CrudRepository<AdminBanner,Integer> {
    List<AdminBanner> findAll();

}
