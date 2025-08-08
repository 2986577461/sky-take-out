package com.xiaoyan.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherVO implements Serializable {

    private String windPower;

    private String windDirectionName;

    private String weather;

    private String degree;
}
