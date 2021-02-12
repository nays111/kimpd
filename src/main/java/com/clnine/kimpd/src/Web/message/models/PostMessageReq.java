package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;

@Getter
@NoArgsConstructor
public class PostMessageReq {
    private  String description;
}
