package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class GetMessagesRes {
    private final int totalCount;
    private final List<GetMessagesDTO> getMessagesList;
}
