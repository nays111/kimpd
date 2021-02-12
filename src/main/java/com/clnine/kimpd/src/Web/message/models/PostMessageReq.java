package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Access;

@Getter
@AllArgsConstructor
public class PostMessageReq {
    private final String description;
}
