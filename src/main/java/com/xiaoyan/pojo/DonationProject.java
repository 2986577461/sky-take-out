package com.xiaoyan.pojo;


import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName donation_project
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationProject implements Serializable {


    @NotNull(message = "[]不能为空")
    private Long id;

    @NotBlank(message = "[]不能为空")
    @Size(max = 20, message = "编码长度不能超过20")
    @Length(max = 20, message = "编码长度不能超过20")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String type;

    @NotBlank(message = "[]不能为空")
    @Size(max = 50, message = "编码长度不能超过20")
    @Length(max = 50, message = "编码长度不能超过20")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @NotBlank(message = "[]不能为空")
    @Size(max = 20, message = "编码长度不能超过20")
    @Length(max = 20, message = "编码长度不能超过20")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String issuer;

    @NotBlank(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long issuerId;

    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer status;

    @NotNull(message = "[]不能为空")
    private Integer currentFund;

    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer fundTarget;

    @NotNull(message = "[]不能为空")
    private Integer numberOfPeople;

    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer bookMoney;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer teachToolMoney;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer liveMoney;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer medicalMoney;
    /**
     *
     */
    @NotBlank(message = "[]不能为空")
    @Size(max = 5000, message = "编码长度不能超过5000")
    @Length(max = 5000, message = "编码长度不能超过5,000")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String projectContent;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    private LocalDateTime createDateTime;
    /**
     *
     */
    @NotNull(message = "[]不能为空")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime deadline;

    /**
     *
     */
}
