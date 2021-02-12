package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.message.models.PatchMessageStatusReq;
import com.clnine.kimpd.src.Web.message.models.PostMessageReq;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserInfoRepository userInfoRepository;
    private final MessageRepository messageRepository;
    public void postMessage(int senderIdx, int receiverIdx, PostMessageReq postMessageReq) throws BaseException {
        UserInfo senderInfo;
        try {
            senderInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(senderIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        UserInfo receiverInfo;
        try {
            receiverInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(receiverIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        Message message = new Message(senderInfo,receiverInfo,postMessageReq.getDescription());
        messageRepository.save(message);
    }

    public void deleteMessage(List<PatchMessageStatusReq> patchMessageStatusReqList) throws BaseException{
        for(int i=0;i<patchMessageStatusReqList.size();i++){
            int messageIdx = patchMessageStatusReqList.get(i).getMessageIdx();
            Message message;
            try{
                message = messageRepository.findByMessageIdxAndStatus(messageIdx,"ACTIVE");
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE);
            }
            message.setStatus("INACTIVE");
            messageRepository.save(message);
        }
    }
}
