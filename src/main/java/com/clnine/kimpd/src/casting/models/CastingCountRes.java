package com.clnine.kimpd.src.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CastingCountRes {
    private int castingGoing;
    private int castingAccepted;
    private int castingRejected;
    private int projectFinished;
}
