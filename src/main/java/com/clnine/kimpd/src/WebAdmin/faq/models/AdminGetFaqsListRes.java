package com.clnine.kimpd.src.WebAdmin.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetFaqsListRes {
    List<AdminGetFaqsRes> faqList;
}