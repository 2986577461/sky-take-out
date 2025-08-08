package com.xiaoyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.constant.MessageConstant;
import com.xiaoyan.context.BaseContext;
import com.xiaoyan.enumeration.StatusType;
import com.xiaoyan.exception.ParameterException;
import com.xiaoyan.exception.ProjectException;
import com.xiaoyan.mapper.DonationProjectMapper;
import com.xiaoyan.mapper.PersonalDonationMapper;
import com.xiaoyan.mapper.UserMapper;
import com.xiaoyan.pojo.DonationProject;
import com.xiaoyan.pojo.PersonalDonation;
import com.xiaoyan.pojo.User;
import com.xiaoyan.service.PersonalDonationService;
import com.xiaoyan.vo.DonationRecordVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonalDonationServiceImpl extends ServiceImpl<PersonalDonationMapper, PersonalDonation>
        implements PersonalDonationService {

    private UserMapper userMapper;

    private PersonalDonationMapper personalDonationMapper;

    private DonationProjectMapper donationProjectMapper;

    @Override
    public void donate(PersonalDonation personalDonation) {
        Long projectId = personalDonation.getProjectId();

        DonationProject donationProject = donationProjectMapper.selectById(projectId);
        if (donationProject == null)
            throw new ParameterException(MessageConstant.PARAMETER_ERROR);

        Integer amount = personalDonation.getAmount();
        Integer currentFund = donationProject.getCurrentFund();
        Integer fundTarget = donationProject.getFundTarget();

        if (currentFund >= fundTarget) {
            donationProjectMapper.setStatusById(projectId, StatusType.CLOSE);
            throw new ProjectException(MessageConstant.PROJECT_ALREADY_COMPLITED);
        }

        if (currentFund + amount > fundTarget) {
            throw new ProjectException(MessageConstant.PROJECT_ALREADY_COMPLITED);
        }

        User user = userMapper.selectById(BaseContext.getCurrentId());
        LocalDateTime now = LocalDateTime.now();
        personalDonation.setDonator(user.getNickName());
        personalDonation.setDonatorId(user.getId());
        personalDonation.setCreateDateTime(now);
        boolean isSaved = this.save(personalDonation);
        if (!isSaved)
            throw new ParameterException(MessageConstant.PARAMETER_ERROR);


        donationProjectMapper.updateById(DonationProject.builder().
                id(projectId).
                currentFund(donationProject.getCurrentFund() + amount).
                numberOfPeople(donationProject.getNumberOfPeople() + 1).
                build());
    }

    @Override
    public List<DonationRecordVO> getAllrecord() {
        List<PersonalDonation> personalDonations = personalDonationMapper.selectList(null);
        List<DonationRecordVO> donationRecordVOS = new ArrayList<>();

        for (PersonalDonation personalDonation : personalDonations) {
            DonationRecordVO donationRecordVO = new DonationRecordVO();
            BeanUtils.copyProperties(personalDonation, donationRecordVO);
            donationRecordVOS.add(donationRecordVO);
        }
        return donationRecordVOS;
    }
}
