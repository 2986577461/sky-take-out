package com.xiaoyan.controller;


import com.xiaoyan.dto.DonationProjectDTO;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.DonationProjectService;
import com.xiaoyan.vo.DonationProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("donation-project")
@AllArgsConstructor
@Slf4j
@Tag(name = "捐赠项目")
public class DonationProjectController {

    private DonationProjectService donationProjectService;

    @PostMapping("create")
    @Operation(summary = "创建捐赠项目")
    public Result<String> createProject(@RequestBody @Valid DonationProjectDTO projectDTO) {
        log.info("创建捐赠项目：{}", projectDTO);
        donationProjectService.createProject(projectDTO);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改捐赠项目")
    public Result<String> update(@RequestBody DonationProjectDTO projectDTO) {
        log.info("修改捐赠项目：{}", projectDTO);
        donationProjectService.update(projectDTO);
        return Result.success();
    }

    @GetMapping("all")
    @Operation(summary = "获取所有捐赠项目")
    public Result<List<DonationProjectVO>> getAll() {
        log.info("获取所有捐赠项目");
        List<DonationProjectVO> vos = donationProjectService.getAll();
        return Result.success(vos);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除一条捐赠项目，需本人")
    public Result<String> delete(@PathVariable Long id) {
        log.info("删除项目");
        donationProjectService.delete(id);
        return Result.success();
    }

    @GetMapping("excel")
    public ResponseEntity<byte[]> downloadExcel(Long id) throws IOException {
        return donationProjectService.downloadExcel(id);
    }
}
