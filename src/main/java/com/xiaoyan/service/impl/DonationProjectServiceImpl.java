package com.xiaoyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.constant.MessageConstant;
import com.xiaoyan.context.BaseContext;
import com.xiaoyan.dto.DonationProjectDTO;
import com.xiaoyan.exception.PositionException;
import com.xiaoyan.exception.ProjectException;
import com.xiaoyan.mapper.DonationProjectMapper;
import com.xiaoyan.mapper.PersonalDonationMapper;
import com.xiaoyan.mapper.UserMapper;
import com.xiaoyan.pojo.DonationProject;
import com.xiaoyan.pojo.User;
import com.xiaoyan.service.DonationProjectService;
import com.xiaoyan.vo.DonationProjectVO;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DonationProjectServiceImpl extends ServiceImpl<DonationProjectMapper, DonationProject>
        implements DonationProjectService {

    private DonationProjectMapper projectMapper;

    private PersonalDonationMapper donationMapper;

    private UserMapper userMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void createProject(DonationProjectDTO projectDTO) {
        DonationProject donationProject = new DonationProject();
        BeanUtils.copyProperties(projectDTO, donationProject, "id");

        User user = userMapper.selectById(BaseContext.getCurrentId());
        donationProject.setIssuerId(user.getId());
        donationProject.setIssuer(user.getNickName());

        LocalDateTime now = LocalDateTime.now();
        donationProject.setCreateDateTime(now);
        donationProject.setCurrentFund(0);
        donationProject.setNumberOfPeople(0);

        projectMapper.insert(donationProject);
    }

    @Override
    public void update(DonationProjectDTO projectDTO) {
        DonationProject project = projectMapper.selectById(projectDTO.getId());
        if (project == null)
            throw new ProjectException(MessageConstant.PROJECT_NO_FOUND_EXCEPTION);

        User user = userMapper.selectById(BaseContext.getCurrentId());
        if (!project.getIssuerId().equals(user.getId()))
            throw new PositionException(MessageConstant.POSITION_EXCEPTION);

        DonationProject donationProject = new DonationProject();
        BeanUtils.copyProperties(projectDTO, donationProject);

        projectMapper.updateById(donationProject);
    }

    @Override
    public List<DonationProjectVO> getAll() {
        List<DonationProject> donationProjects = projectMapper.selectList(null);
        List<DonationProjectVO> list = new ArrayList<>();

        for (DonationProject donationProject : donationProjects) {
            DonationProjectVO donationProjectVO = new DonationProjectVO();
            BeanUtils.copyProperties(donationProject, donationProjectVO);
            list.add(donationProjectVO);
        }
        return list;
    }

    @Override
    public void delete(Long id) {
        DonationProject donationProject = projectMapper.selectById(id);
        if (donationProject == null)
            throw new ProjectException(MessageConstant.PROJECT_NO_FOUND_EXCEPTION);

        User user = userMapper.selectById(BaseContext.getCurrentId());
        if (!donationProject.getIssuerId().equals(user.getId()))
            throw new PositionException(MessageConstant.POSITION_EXCEPTION);

        projectMapper.deleteById(id);
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel(Long id) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        CellStyle style = xssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);         XSSFRow row2 = sheet.createRow(0);
        XSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("爱心捐赠项目情况统计");
        cell2.setCellStyle(style);

        for (int i = 0; i <= 9; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        XSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue("项目名称");
        row.createCell(1).setCellValue("项目类型");
        row.createCell(2).setCellValue("项目介绍");
        row.createCell(3).setCellValue("发起人");
        row.createCell(4).setCellValue("已筹备资金");
        row.createCell(5).setCellValue("目标资金");
        row.createCell(6).setCellValue("创建时间");
        row.createCell(7).setCellValue("截止时间");
        row.createCell(8).setCellValue("捐赠人次");
        row.createCell(9).setCellValue("项目状态");

        XSSFRow row1 = sheet.createRow(2);
        DonationProject donationProject = projectMapper.selectById(id);
        row1.createCell(0).setCellValue(donationProject.getName());
        row1.createCell(1).setCellValue(donationProject.getType());
        row1.createCell(2).setCellValue(donationProject.getProjectContent());
        row1.createCell(3).setCellValue(donationProject.getIssuer());
        row1.createCell(4).setCellValue(donationProject.getCurrentFund());
        row1.createCell(5).setCellValue(donationProject.getFundTarget());

        XSSFDataFormat dataFormat = xssfWorkbook.createDataFormat();
        CellStyle dateCellStyle = xssfWorkbook.createCellStyle();
        dateCellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

        XSSFCell cell = row1.createCell(6);
        cell.setCellValue(donationProject.getCreateDateTime());
        cell.setCellStyle(dateCellStyle);

        XSSFCell cell1 = row1.createCell(7);
        cell1.setCellValue(donationProject.getDeadline());
        cell1.setCellStyle(dateCellStyle);
        row1.createCell(8).setCellValue(donationProject.getNumberOfPeople());
        row1.createCell(9).setCellValue(
                donationProject.getStatus() == 0 ? "已完成" : "筹备中");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        xssfWorkbook.write(bos);
        xssfWorkbook.close();

        byte[] excelBytes = bos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml" +
                ".sheet"));

                String fileName = donationProject.getName()+"统计数据.xlsx";
                        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");         String contentDisposition =
                "attachment; filename=\"" + encodedFileName + "\"; filename*=utf-8''" + encodedFileName;

        headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

                headers.setContentLength(excelBytes.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
}
