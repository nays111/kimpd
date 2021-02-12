package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMessagesRes {
    private final int messageIdx;
    private final String senderNickName;
    private final String senderJobName;
    private final String sendTime;
    private final String description;
}
