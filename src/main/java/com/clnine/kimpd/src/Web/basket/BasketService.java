package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.PostBasketCastingReq;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.ProjectProvider;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final CastingRepository castingRepository;
    private final UserInfoProvider userInfoProvider;
    private final ProjectProvider projectProvider;

    /**
     * 장바구니 담기
     */
    @Transactional
    public void postBasket(int userIdx, PostBasketReq postBasketReq) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        List<Integer> expertIdxList = postBasketReq.getUserIdx();
        int projectIdx = postBasketReq.getProjectIdx();
        Project project = projectProvider.retrieveProjectByProjectIdx(projectIdx);

        for(int i=0;i<expertIdxList.size();i++){
            int expertIdx = expertIdxList.get(i);
            UserInfo expertInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
            Casting casting = new Casting(userInfo,expertInfo,project,0);
            try{
                castingRepository.save(casting);
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.FAILED_TO_SAVE_BASKETS);
            }
        }
    }

    /**
     * 장바구니에 있는 걸로 섭외 요청하기
     */
    @Transactional
    public void postBasketCasting(PostBasketCastingReq postBasketCastingReq) throws BaseException{
        List<Integer> castingIdxList = postBasketCastingReq.getCastingIdx();
        List<Casting> castingList = new ArrayList<>();
        for(int i=0;i<castingIdxList.size();i++){
            Casting casting;
            try{
                casting = castingRepository.findAllByCastingIdxAndStatus(castingIdxList.get(i),"ACTIVE");
            }catch (Exception ignored){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_CASTING);
            }
            if(casting==null){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_CASTING);
            }
            String castingStartDate = casting.getCastingStartDate();
            String castingEndDate = casting.getCastingEndDate();
            String castingPrice = casting.getCastingPrice();
            String castingMessage = casting.getCastingMessage();
            String castingWork = casting.getCastingWork();
            String castingPriceDate = casting.getCastingPriceDate();
            if(castingMessage==null || castingWork==null || castingPriceDate==null || castingStartDate==null || castingEndDate==null || castingPrice==null){
                throw new BaseException(BaseResponseStatus.DID_NOT_INSERT_CASTING_CONDITION);
            }else {
                castingList.add(casting);
            }
        }

        for(int i=0;i<castingList.size();i++) {
            Casting casting = castingList.get(i);
            casting.setCastingStatus(1);
            try{
                castingRepository.save(casting);
            }catch (Exception ignored){
                throw new BaseException(BaseResponseStatus.FAILED_TO_POST_CASTING);
            }
        }
    }
}
