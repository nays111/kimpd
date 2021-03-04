package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.category.models.GetGenreCategoryRes;
import com.clnine.kimpd.src.Web.category.models.GetJobCategoryChildRes;
import com.clnine.kimpd.src.Web.category.models.GetJobCategoryParentRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController @RequiredArgsConstructor @CrossOrigin
public class CategoryController {
    private final CategoryProvider categoryProvider;

    @ResponseBody @GetMapping("/job-categories")
    @Operation(summary="1차 직종 카테고리 조회 API")
    public BaseResponse<List<GetJobCategoryParentRes>> getJobCategoryParent()throws BaseException{
        List<GetJobCategoryParentRes> getJobCategoryParentResList;
        try{
            getJobCategoryParentResList = categoryProvider.getJobCategoryParentResList();
            return new BaseResponse<>(SUCCESS,getJobCategoryParentResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody @GetMapping("/genre-categories")
    @Operation(summary="장르 카테고리 조회 API")
    public BaseResponse<List<GetGenreCategoryRes>> getGenreCategory(){
        List<GetGenreCategoryRes> getGenreCategoryResList;
        try{
            getGenreCategoryResList = categoryProvider.getGenreCategory();
            return new BaseResponse<>(SUCCESS,getGenreCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody @GetMapping("/job-categories/{jobCategoryParentIdx}")
    @Operation(summary="2차 직종 카테고리 조회 API",description = "1차 직종 카테고리 인덱스가 필요합니다.")
    public BaseResponse<List<GetJobCategoryChildRes>> getJobCategoryChild(@PathVariable Integer jobCategoryParentIdx){
        if(jobCategoryParentIdx==null || jobCategoryParentIdx<=0){
            return new BaseResponse<>(EMPTY_JOB_CATEGORY_PARENT_IDX);
        }
        List<GetJobCategoryChildRes> getJobCategoryChildResList;
        try{
            getJobCategoryChildResList = categoryProvider.getJobCategoryChild(jobCategoryParentIdx);
            return new BaseResponse<>(SUCCESS,getJobCategoryChildResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
