package com.xiaoyan.controller;


import com.xiaoyan.context.BaseContext;
import com.xiaoyan.dto.PersonalDonateDTO;
import com.xiaoyan.pojo.PersonalDonation;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.PersonalDonationService;
import com.xiaoyan.vo.DonationRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("personal-donation")
@AllArgsConstructor
@Slf4j
@Tag(name = "个人捐赠")
public class PersonalDonationController {

    private PersonalDonationService donationService;

    @PostMapping("donate")
    @Operation(summary = "捐赠")
    public Result<String> doante(@RequestBody PersonalDonateDTO personalDonateDTO) {
        log.info("{}捐赠{}元给项目{}的{}", BaseContext.getCurrentId(),
                personalDonateDTO.getAmount(), personalDonateDTO.getProjectId(), personalDonateDTO.getType());

        PersonalDonation personalDonation = new PersonalDonation();
        BeanUtils.copyProperties(personalDonateDTO, personalDonation);
        donationService.donate(personalDonation);
        return Result.success();
    }

    @GetMapping("all")
    @Operation(summary = "获取所有捐赠记录")
    public Result<List<DonationRecordVO>> getAllRecord() {
        List<DonationRecordVO> list = donationService.getAllrecord();
        return Result.success(list);
    }
}
