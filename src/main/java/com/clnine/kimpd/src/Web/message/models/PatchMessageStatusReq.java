package com.clnine.kimpd.src.Web.message.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PatchMessageStatusReq {
    List<Integer> messageIdx;
}
