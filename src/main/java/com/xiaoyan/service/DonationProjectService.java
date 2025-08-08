package com.xiaoyan.service;

import com.xiaoyan.dto.DonationProjectDTO;
import com.xiaoyan.vo.DonationProjectVO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface DonationProjectService {
    void createProject(DonationProjectDTO projectDTO);

    void update(DonationProjectDTO projectDTO);

    List<DonationProjectVO> getAll();

    void delete(Long id);

    ResponseEntity<byte[]> downloadExcel(Long id) throws IOException;

}
