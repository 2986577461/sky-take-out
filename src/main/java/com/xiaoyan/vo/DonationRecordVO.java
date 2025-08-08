package com.xiaoyan.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonationRecordVO {

    private String donator;

    private Integer amount;

    private LocalDateTime createDateTime;
}
