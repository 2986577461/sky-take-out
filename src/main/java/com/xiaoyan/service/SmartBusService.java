package com.xiaoyan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaoyan.vo.SmartBusVO;

import java.util.List;

public interface SmartBusService {
    List<SmartBusVO> getAll() throws JsonProcessingException;

    List<SmartBusVO> refreshAllBusLinesCache() throws JsonProcessingException;
}
