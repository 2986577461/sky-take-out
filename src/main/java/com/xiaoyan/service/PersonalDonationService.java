package com.xiaoyan.service;

import com.xiaoyan.pojo.PersonalDonation;
import com.xiaoyan.vo.DonationRecordVO;

import java.util.List;

public interface PersonalDonationService {

    void donate(PersonalDonation personalDonation);

    List<DonationRecordVO> getAllrecord();

}
