package com.xiaoyan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class SmartBusVO implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "目的地")
    private String destination;

    @Schema(description = "巴士数量")
    private Integer car_count;

    @Schema(description = "每辆巴士的信息")
    private List<LineItem> lineItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LineItem implements Serializable{

        @Schema(description = "线路名")
        private String lineName;

        @Schema(description = "终点站")
        private String endDestination;

        @Schema(description = "巴士id")
        private String busId;

        @Schema(description = "到达时间")
        private LocalDateTime reachTime;

        @Schema(description = "剩余时间")
        private String  remainingTime;

        @Schema(description = "剩余车站数量")
        private String  surplus;

    }

}
