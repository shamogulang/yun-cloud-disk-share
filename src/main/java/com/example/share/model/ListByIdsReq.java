package com.example.share.model;

import lombok.Data;

import java.util.List;

@Data
public class ListByIdsReq {

    private List<Long> fileIds;
    private Long userId;

    public ListByIdsReq(List<Long> fileIds, Long userId) {
        this.fileIds = fileIds;
        this.userId = userId;
    }
}
