package com.sky.controller.user;


import com.sky.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("user/shop")
@Slf4j
@AllArgsConstructor
public class ShopController {

    private final String key = "SHOP_STATUS";

    private  RedisTemplate<String, Object> redisTemplate;


    @GetMapping("status")
    public Result<Integer> getStatus() {
        return Result.success((Integer) redisTemplate.opsForValue().get(key));
    }
}
