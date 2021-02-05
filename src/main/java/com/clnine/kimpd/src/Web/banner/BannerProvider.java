package com.clnine.kimpd.src.Web.banner;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.banner.models.GetBannersRes;
import com.clnine.kimpd.src.Web.banner.models.Banner;
import com.clnine.kimpd.src.Web.category.JobCategoryParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_BANNERS;

@Service
public class BannerProvider {

    @Autowired
    BannerRepository bannerRepository;
    JobCategoryParentRepository jobCategoryParentRepository;

    BannerProvider(BannerRepository bannerRepository,JobCategoryParentRepository jobCategoryParentRepository){
        this.bannerRepository = bannerRepository;
        this.jobCategoryParentRepository = jobCategoryParentRepository;
    }
//    public GetBannersRes getBannerListAndParentJobCategory(){
//        List<Banner> bannerList;
//        try{
//            bannerList = bannerRepository.findByStatus("ACTIVE");
//        }catch(Exception ignored){
//            throw new BaseException(FAILED_TO_GET_BANNERS);
//        }
//        List<JobCategoryParent> jobCategoryParentList;
//        try{
//            jobCategoryParentList = jobCategoryParentRepository.findAll();
//        }catch(Exception ignored){
//            throw new BaseException(FAILED_TO_GET_PARENT_JOB_CATEGORIES);
//        }
//    }
    public List<GetBannersRes> getBannerList() throws BaseException{
        List<Banner> bannerList;
        try{
            bannerList = bannerRepository.findAllByStatus("ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_BANNERS);
        }
        return bannerList.stream().map(Banner ->{
            int bannerIdx = Banner.getBannerIdx();
            String bannerLink = Banner.getBannerLink();
            String bannerImageURL = Banner.getBannerImageURL();
            return new GetBannersRes(bannerIdx,bannerLink,bannerImageURL);
        }).collect(Collectors.toList());
    }


}
