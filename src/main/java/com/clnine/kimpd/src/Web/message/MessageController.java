package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.message.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MessageController {
    private final MessageService messageService;
    private final JwtService jwtService;
    private final MessageProvider messageProvider;

    @ResponseBody
    @PostMapping("/messages/{userIdx}")
    @Operation(summary="쪽지 전송  API",description = "토큰이 필요합니다 .(userIdx : 쪽지를 보낼 사람)")
    public BaseResponse<String> postMessage(@PathVariable(required = true,value = "userIdx")int userIdx,
                                            @RequestBody PostMessageReq postMessageReq){
        if(postMessageReq.getDescription()==null || postMessageReq.getDescription().length()==0){
            return new BaseResponse<>(EMPTY_MESSAGE_DESCRIPTION);
        }
        if(postMessageReq.getDescription().length()>1000){
            return new BaseResponse<>(TOO_LONG_MESSAGE_DESCRIPTION);
        }
        try{
            Integer senderIdx = jwtService.getUserIdx();
            /**
             * 자기 자신에게 쪽지는 보낼 수 없음
             */
            if(senderIdx==userIdx){
                return new BaseResponse<>(CANNOT_SEND_MESSAGE_TO_YOURSELF);
            }
            messageService.postMessage(senderIdx,userIdx,postMessageReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/messages/status")
    @Operation(summary = "쪽지 삭제 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> deleteMessage(@RequestBody PatchMessageStatusReq patchMessageStatusReq){
        try{
            Integer userIdx = jwtService.getUserIdx();
            messageService.deleteMessage(patchMessageStatusReq,userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/messages/{messageIdx}")
    @Operation(summary = "쪽지 상세 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMessageRes> getMessage(@PathVariable(required = true,value = "messageIdx")int messageIdx){
        try{
            Integer userIdx = jwtService.getUserIdx();
            GetMessageRes getMessageRes = messageProvider.getMessage(messageIdx,userIdx);
            return new BaseResponse<>(SUCCESS,getMessageRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/messages")
    @Operation(summary = "쪽지함 리스트 조회 API",description = "토큰이 필요합니다, page 번호를 입력해주세요")
    public BaseResponse<GetMessagesRes> getMessages(@RequestParam(value="page",required = true)Integer page){
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try{
            Integer userIdx = jwtService.getUserIdx();
            GetMessagesRes getMessagesRes = messageProvider.getMessages(userIdx,page,3);
            return new BaseResponse<>(SUCCESS, getMessagesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
