package com.clnine.kimpd.src.Web.alarm;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.alarm.models.GetAlarmsRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlarmController {
    private final JwtService jwtService;
    private final AlarmProvider alarmProvider;

    @ResponseBody
    @GetMapping("/alarms")
    public BaseResponse<List<GetAlarmsRes>> getMyAlarmList(){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetAlarmsRes> getAlarmsResList = alarmProvider.getAlarmsResList(userIdx);
            return new BaseResponse<>(SUCCESS, getAlarmsResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
