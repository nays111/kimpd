package com.clnine.kimpd.src.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.casting.models.Casting;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.NOT_FOUND_CASTING;

@Service
@RequiredArgsConstructor
public class CastingProvider {
    private final CastingRepository castingRepository;
    public Casting retrieveCastingInfoByUserExpertProject(UserInfo user, UserInfo expert, Project project) throws BaseException{
        List<Casting> existsCastingList;
        try{
            existsCastingList = castingRepository.findByUserInfoAndExpertAndProjectAndStatus(user,expert,project,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        Casting casting;
        if(existsCastingList!=null && existsCastingList.size()>0){
            casting = existsCastingList.get(0);
        }else{
            throw new BaseException(NOT_FOUND_CASTING);
        }
        return casting;
    }
}
