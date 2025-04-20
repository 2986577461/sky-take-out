package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/setmeal")
@Slf4j
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        this.setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }


}
