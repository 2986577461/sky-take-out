package com.xiaoyan.vo;


import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class DonationProjectVO {

    private Long id;

    private String type;

    private String name;

    private String issuer;

    private Long issuerId;

    private Integer currentFund;

    private Integer fundTarget;

    private Integer numberOfPeople;

    private Integer bookMoney;

    private Integer teachToolMoney;

    private Integer liveMoney;

    private Integer status;

    private Integer medicalMoney;

    private String projectContent;

    private LocalDateTime createDateTime;

    private LocalDateTime deadline;

}
