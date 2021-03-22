package com.clnine.kimpd.src.Web.alarm;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.alarm.models.Alarm;
import com.clnine.kimpd.src.Web.alarm.models.GetAlarmsRes;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_MY_ALARMS;

@Service
@RequiredArgsConstructor
public class AlarmProvider {

    private final AlarmRepository alarmRepository;
    private final UserInfoProvider userInfoProvider;

    public List<GetAlarmsRes> getAlarmsResList(int userIdx) throws BaseException {
        UserInfo userinfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        List<Alarm> alarmList;
        try{
            alarmList = alarmRepository.findAllByUserInfoAndStatus(userinfo,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_MY_ALARMS);
        }
        List<GetAlarmsRes> getAlarmsResList = new ArrayList<>();
        for(int i=0;i<alarmList.size();i++){
            int alarmIdx = alarmList.get(i).getAlarmIdx();

            Date createdAt = alarmList.get(i).getCreatedAt();

            String profileImageURL = alarmList.get(i).getUserInfo().getProfileImageURL();
            int readStatus = alarmList.get(i).getReadStatus();
            String message = alarmList.get(i).getAlarmMessage();

            SimpleDateFormat day = new SimpleDateFormat("yy.MM.dd (E)");
            SimpleDateFormat time = new SimpleDateFormat("HH: mm");

            String alarmDay = day.format(createdAt);
            String alarmTime = time.format(createdAt);

            GetAlarmsRes getAlarmsRes = new GetAlarmsRes(alarmIdx,alarmDay,profileImageURL,readStatus,message,alarmTime);
            getAlarmsResList.add(getAlarmsRes);
        }
        return getAlarmsResList;

    }
}
