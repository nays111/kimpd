package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMessagesDTO {
    private final int messageIdx;

    private final int senderIdx;
    private final String senderNickName;
    private final String senderProfileImageUrl;
    private final String senderJobName;
    private final String sendTime;
    private final String description;
    private final int readStatus;
}
