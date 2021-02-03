package com.clnine.kimpd.src.banner;

import com.clnine.kimpd.src.banner.models.Banner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BannerRepository extends CrudRepository<Banner,Integer> {
    List<Banner> findAllByStatus(String status);
    List<Banner> findAll();
    List<Banner> findAllByBannerIdx(int bannerIdx);
    Banner findByBannerIdx(int bannerIdx);

}
