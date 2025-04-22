package com.sky.controller.admin;


import com.sky.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("admin/shop")
@Slf4j
@AllArgsConstructor
public class ShopController {

    private final String key = "SHOP_STATUS";

    private RedisTemplate<String, Object> redisTemplate;

    @PutMapping("{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为：{}", status == 1 ? "营业" : "打烊");
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }

    @GetMapping("status")
    public Result<Integer> getStatus() {
        return Result.success((Integer) redisTemplate.opsForValue().get(key));
    }
}
