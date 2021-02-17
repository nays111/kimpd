package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.message.models.GetMessageRes;
import com.clnine.kimpd.src.Web.message.models.GetMessagesRes;
import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageProvider {
    private final MessageRepository messageRepository;
    private final UserInfoRepository userInfoRepository;
    private final CategoryProvider categoryProvider;

    /**
     * 쪽지 상세 조회
     * @param messageIdx
     * @return
     * @throws BaseException
     */
    public GetMessageRes getMessage(int messageIdx) throws BaseException {
        Message message;
        try {
            message = messageRepository.findByMessageIdxAndStatus(messageIdx, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE);
        }
        message.setReadStatus(1);
        messageRepository.save(message);
        String senderNickname = message.getSender().getNickname();
        String description = message.getDescription();
        Date sendTimeDateForm = message.getCreatedAt();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd[hh:mm]");
        String sendTime = simpleDateFormat.format(sendTimeDateForm);

        GetMessageRes getMessageRes = new GetMessageRes(messageIdx, senderNickname, sendTime, description);
        return getMessageRes;
    }


    /**
     * 쪽지 리스트 조회
     * @param userIdx
     * @return
     * @throws BaseException
     */

    public List<GetMessagesRes> getMessages(int userIdx,int page,int size) throws BaseException{
        UserInfo receiverInfo; //쪽지 받은 사람은 userIdx
        try {
            receiverInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        List<GetMessagesRes> getMessagesResList = new ArrayList<>();

        //String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(userInfo);
        List<Message> messageList = messageRepository.findByReceiverAndStatus(receiverInfo,"ACTIVE",pageable);
        for(int i=0;i<messageList.size();i++){
            UserInfo sender = messageList.get(i).getSender();
            Message message = messageList.get(i);
            int messageIdx = message.getMessageIdx();
            String senderNickname = sender.getNickname(); //쪽지 보낸 사람 닉네임
            String senderJobName = categoryProvider.getMainJobCategoryChild(sender);//쪽지 보낸 사람 직종 이름
            Date sendTimeDateForm = message.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd[hh:mm]");
            String sendTime = simpleDateFormat.format(sendTimeDateForm); //쪽지 보낸 시간
            String description = message.getDescription();
            int readStatus = message.getReadStatus();
            GetMessagesRes getMessagesRes = new GetMessagesRes(messageIdx,senderNickname,senderJobName,sendTime,description,readStatus);
            getMessagesResList.add(getMessagesRes);
        }

        return getMessagesResList;
    }
}
