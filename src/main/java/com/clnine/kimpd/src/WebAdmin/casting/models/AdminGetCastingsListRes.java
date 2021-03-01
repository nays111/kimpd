package com.clnine.kimpd.src.WebAdmin.casting.models;

import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminGetInquiriesRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetCastingsListRes {
    List<AdminGetCastingsRes> castingList;
}