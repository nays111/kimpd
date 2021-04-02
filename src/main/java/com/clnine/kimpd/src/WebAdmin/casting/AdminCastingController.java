package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.casting.models.*;
import com.clnine.kimpd.src.WebAdmin.user.AdminUserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminCastingController {
    private final AdminCastingProvider adminCastingProvider;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * 섭외 전체 조회 API
     * [GET] /castings
     * @return BaseResponse<AdminGetCastingsListRes>
     */
    @ResponseBody
    @GetMapping("/castings")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetCastingsListRes> getCastings() throws BaseException {
        List<AdminGetCastingsRes> getCastingList;

        try{
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            getCastingList = adminCastingProvider.getCastingList();
            AdminGetCastingsListRes castingList = new AdminGetCastingsListRes(getCastingList);
            return new BaseResponse<>(SUCCESS, castingList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 섭외 상세 조회 API
     * [GET] /castings/:castingIdx
     * @PathVariable castingIdx
     * @return BaseResponse<AdminGetCastingRes>
     */
    @ResponseBody
    @GetMapping("/castings/{castingIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetCastingRes> getCasting(@PathVariable Integer castingIdx) {
        if (castingIdx == null || castingIdx <= 0) {
            return new BaseResponse<>(EMPTY_INQUIRY_IDX);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetCastingRes adminGetCastingRes = adminCastingProvider.retrieveCastingInfo(castingIdx);
            return new BaseResponse<>(SUCCESS, adminGetCastingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
