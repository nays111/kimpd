package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.category.models.GetGenreCategoryRes;
import com.clnine.kimpd.src.Web.category.models.GetJobCategoryChildRes;
import com.clnine.kimpd.src.Web.category.models.GetJobCategoryParentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryProvider categoryProvider;

    @ResponseBody
    @GetMapping("/job-categories")
    public BaseResponse<List<GetJobCategoryParentRes>> getJobCategoryParent()throws BaseException{
        List<GetJobCategoryParentRes> getJobCategoryParentResList;
        try{
            getJobCategoryParentResList = categoryProvider.getJobCategoryParentResList();
            return new BaseResponse<>(SUCCESS_GET_PARENT_JOB_CATEGORY,getJobCategoryParentResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody
    @GetMapping("/genre-categories")
    public BaseResponse<List<GetGenreCategoryRes>> getGenreCategory(){
        List<GetGenreCategoryRes> getGenreCategoryResList;
        try{
            getGenreCategoryResList = categoryProvider.getGenreCategory();
            return new BaseResponse<>(SUCCESS_GET_GENRE_CATEGORY,getGenreCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody
    @GetMapping("/job-categories/{jobCategoryParentIdx}")
    public BaseResponse<List<GetJobCategoryChildRes>> getJobCategoryChild(@PathVariable Integer jobCategoryParentIdx){
        if(jobCategoryParentIdx==null || jobCategoryParentIdx<=0){
            return new BaseResponse<>(EMPTY_JOB_CATEGORY_PARENT_IDX);
        }
        List<GetJobCategoryChildRes> getJobCategoryChildResList;
        try{
            getJobCategoryChildResList = categoryProvider.getJobCategoryChild(jobCategoryParentIdx);
            return new BaseResponse<>(SUCCESS_GET_CHILD_JOB_CATEGORY,getJobCategoryChildResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
