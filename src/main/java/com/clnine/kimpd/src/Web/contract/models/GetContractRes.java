package com.clnine.kimpd.src.Web.contract.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetContractRes {
    private final int contractIdx;
    private final String contractFileURL;
}
