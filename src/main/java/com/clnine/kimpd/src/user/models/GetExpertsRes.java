package com.clnine.kimpd.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GetExpertsRes {


    int total;
    ArrayList<GetUsersRes> getUsersRes;
}
