package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CategoryProvider {

    private final JobCategoryParentRepository jobCategoryParentRepository;
    private final JobCategoryChildRepository jobCategoryChildRepository;
    private final GenreCategoryRepository genreCategoryRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;

    public List<GetJobCategoryParentRes> getJobCategoryParentResList() throws BaseException {
        List<JobCategoryParent> jobCategoryParentList;
        try{
            jobCategoryParentList = jobCategoryParentRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_PARENT_JOB_CATEGORIES);
        }
        return jobCategoryParentList.stream().map(jobCategoryParent -> {
            int jobCategoryParentIdx= jobCategoryParent.getJobCategoryParentIdx();
            String jobCategoryParentName = jobCategoryParent.getJobCategoryName();
            return new GetJobCategoryParentRes(jobCategoryParentIdx,jobCategoryParentName);
        }).collect(Collectors.toList());
    }

    public List<GetJobCategoryChildRes>getJobCategoryChild(int jobCategoryParentIdx) throws BaseException{
        List<JobCategoryChild> jobCategoryChildList = null;
        JobCategoryParent jobCategoryParent=null;
        //parentIdx가지고 parent객체를 찾는다.
        try{
            jobCategoryParent= jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobCategoryParentIdx);
        }catch(Exception ignored){
            new BaseException(FAILED_TO_GET_PARENT_JOB_CATEGORIES);
        }
        try {
            jobCategoryChildList = jobCategoryChildRepository.findAllByJobCategoryParent(jobCategoryParent);
        } catch (Exception ignored){
            new BaseException(FAILED_TO_GET_CHILD_JOB_CATEGORIES);
        }
        return jobCategoryChildList.stream().map(jobCategoryChild -> {
            int jobCategoryChildIdx = jobCategoryChild.getJobCategoryChildIdx();
            String jobCategoryChildName = jobCategoryChild.getJobCategoryChildName();
            return new GetJobCategoryChildRes(jobCategoryChildIdx,jobCategoryChildName);
        }).collect(Collectors.toList());
    }

    public List<GetGenreCategoryRes>getGenreCategory() throws BaseException{
        List<GenreCategory> genreCategoryList;
        try{
            genreCategoryList = genreCategoryRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_GENRE_CATEGORIES);
        }
        return genreCategoryList.stream().map(genreCategory -> {
            int genreCategoryIdx = genreCategory.getGenreCategoryIdx();
            String genreCategoryName = genreCategory.getGenreCategoryName();
            return new GetGenreCategoryRes(genreCategoryIdx,genreCategoryName);
        }).collect(Collectors.toList());
    }

    /**
     * 대표 2차 직종만 들고오기
     * @param userInfo
     * @return
     * @throws BaseException
     */
    public String getMainJobCategoryChild(UserInfo userInfo) throws BaseException{

        List<UserJobCategory> userJobCategoryList = new ArrayList<>();
        try{
            userJobCategoryList= userJobCategoryRepository.findByUserInfo(userInfo);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_CHILD_JOB_CATEGORIES);
        }
        String mainJobCategoryChildName = userJobCategoryList.get(0).getJobCategoryChild().getJobCategoryChildName();
        return mainJobCategoryChildName;
    }
}

