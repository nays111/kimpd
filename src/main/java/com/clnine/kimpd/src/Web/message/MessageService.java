package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.alarm.AlarmRepository;
import com.clnine.kimpd.src.Web.alarm.models.Alarm;
import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.message.models.PatchMessageStatusReq;
import com.clnine.kimpd.src.Web.message.models.PostMessageReq;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final AlarmRepository alarmRepository;
    private final UserInfoProvider userInfoProvider;

    @Transactional
    public void postMessage(int senderIdx, int receiverIdx, PostMessageReq postMessageReq) throws BaseException {
        UserInfo senderInfo = userInfoProvider.retrieveUserInfoByUserIdx(senderIdx);
        UserInfo receiverInfo = userInfoProvider.retrieveUserInfoByUserIdx(receiverIdx);

        String description = postMessageReq.getDescription();
        Message message = new Message(senderInfo,receiverInfo,description);
        try{
            messageRepository.save(message);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_MESSAGE);
        }
        String alarmMessage = "받은 쪽지함에 새로운 쪽지가 있습니다.";
        Alarm alarm = new Alarm(receiverInfo, alarmMessage);
        try{
            alarmRepository.save(alarm);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_ALARM);
        }
    }

    @Transactional
    public void deleteMessage(PatchMessageStatusReq patchMessageStatusReq,int userIdx) throws BaseException{
        for(int i=0;i<patchMessageStatusReq.getMessageIdx().size();i++){
            int messageIdx = patchMessageStatusReq.getMessageIdx().get(i);

            Message message = messageRepository.findByMessageIdxAndStatus(messageIdx,"ACTIVE");
            if(message==null){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE);
            }
            if(message.getReceiver().getUserIdx()!=userIdx){
                throw new BaseException(NOT_USER_MESSAGE);
            }
            message.setStatus("INACTIVE");
            messageRepository.save(message);
        }
    }
}
