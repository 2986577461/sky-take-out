package com.xiaoyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.pojo.DonationProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DonationProjectMapper extends BaseMapper<DonationProject> {

    @Update("UPDATE donation_project SET status=#{status} WHERE id=#{id}")
    void setStatusById(Long projectId,Enum status);
}
