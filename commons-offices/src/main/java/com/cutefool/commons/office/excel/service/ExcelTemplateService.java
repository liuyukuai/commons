/*
 *  
 */
package com.cutefool.commons.office.excel.service;

import com.cutefool.commons.office.excel.pojo.ExcelCascade;
import com.cutefool.commons.office.excel.pojo.ExcelDict;
import com.cutefool.commons.office.excel.pojo.ExcelSheet;
import com.cutefool.commons.office.excel.util.ExcelReadUtil;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.office.excel.util.ExcelDownloadUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 插件服务：名称匹配、字典转换(映射)
 *
 * @author 271007729@qq.com
 * @date 2019/7/20 - 13:40
 */
@Slf4j
@Component
public class ExcelTemplateService {

    /**
     * 填充模板表头【子类按需实现】
     *
     * @param workbook Workbook
     */
    public void fillTitle(Workbook workbook) {
        // 按需实现，没有的忽略即可
    }


    /**
     * 下载 导入 模板
     *
     * @param template  模板地址
     * @param fileName  导出文件名
     * @param sheetList 填充的字典数据
     * @param response  HttpServletResponse
     */
    public void download(String template, String fileName, List<ExcelSheet> sheetList, HttpServletResponse response) {
        Workbook workbook;
        try {
            // 模板都是2007及其以上版本
            workbook = new XSSFWorkbook(new ClassPathResource(template).getInputStream());
        } catch (IOException e) {
            log.warn("加载" + fileName + "模板文件失败", e);
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), fileName + "模板生成失败");
        }

        // 动态生成表头，依赖各自的实现
        this.fillTitle(workbook);
        // 填充字典数据
        this.fillData(workbook, sheetList);

        // 导出模板统一使用 07 格式的
        ExcelDownloadUtil.download(fileName + ".xlsx", workbook, response);
    }


    /**
     * 数据填充
     *
     * @param workbook Workbook
     * @param dataList List<ExcelTemplateData>
     */
    private void fillData(Workbook workbook, List<ExcelSheet> dataList) {
        if (CollectionUtils.isEmpty(dataList) || Objects.isNull(workbook)) {
            return;
        }

        for (ExcelSheet template : dataList) {
            Sheet sheet = getTemplateSheet(workbook, template);
            this.fillDict(sheet, template);
            this.fillCascade(sheet, template);
        }
    }


    /**
     * 填充-动态字典
     *
     * @param sheet    Sheet
     * @param fillData ExcelFillData
     */
    private void fillDict(Sheet sheet, ExcelSheet fillData) {
        if (Objects.isNull(sheet) || Objects.isNull(fillData)) {
            return;
        }
        List<ExcelDict> dictList = fillData.getDictList();
        if (CollectionUtils.isEmpty(dictList)) {
            return;
        }

        // 数据填充-开始行下标【0表示第1行】
        int beginIndex = Optional.ofNullable(fillData.getRow()).filter(e -> e >= 0).orElse(0);

        for (ExcelDict data : dictList) {
            Integer column = data.getColumn();
            List<String> values = data.getValues();
            if (CollectionUtils.isEmpty(values)) {
                // 清空表头预设的示例数据
                ExcelReadUtil.getPresentCell(ExcelReadUtil.getPresentRow(sheet, beginIndex), column).setCellValue("");
                continue;
            }
            for (int i = 0; i < values.size(); i++) {
                ExcelReadUtil.getPresentCell(ExcelReadUtil.getPresentRow(sheet, i + beginIndex), column).setCellValue(values.get(i));
            }
        }
    }


    /**
     * 填充-级联字典
     *
     * @param sheet    Sheet
     * @param fillData ExcelFillData
     */
    private void fillCascade(Sheet sheet, ExcelSheet fillData) {
        if (Objects.isNull(sheet) || Objects.isNull(fillData)) {
            return;
        }
        List<ExcelCascade> dictCascade = fillData.getDictCascade();
        if (CollectionUtils.isEmpty(dictCascade)) {
            return;
        }

        // 数据填充-开始行下标【0表示第1行】
        int beginIndex = Optional.ofNullable(fillData.getRow()).filter(e -> e >= 0).orElse(0);

        for (int i = 0; i < dictCascade.size(); i++) {
            // 第2行开始处理，第1行是标题
            Row row = ExcelReadUtil.getPresentRow(sheet, beginIndex + i);

            // 设置【一级】数据
            ExcelCascade cascade = dictCascade.get(i);
            Cell cell = ExcelReadUtil.getPresentCell(row, 0);
            cell.setCellValue(cascade.getName());

            // 设置【二级】数据
            List<ExcelCascade> children = cascade.getChildren();
            for (int j = 0; j < children.size(); j++) {
                Cell cell2 = ExcelReadUtil.getPresentCell(row, j + 1);
                cell2.setCellValue(children.get(j).getName());
            }
        }
    }


    /**
     * 获取模板的字典sheet
     *
     * @param workbook 工作薄
     * @param template 模板数据
     * @return Sheet
     */
    private Sheet getTemplateSheet(@NotNull Workbook workbook, @NotNull ExcelSheet template) {
        Sheet sheet = null;
        String name = template.getSheetName();
        if (StringUtils.isNotBlank(name)) {
            sheet = workbook.getSheet(name);
        }
        if (Objects.isNull(sheet)) {
            Integer index = template.getSheetIndex();
            if (Objects.nonNull(index) && index >= 0) {
                sheet = workbook.getSheetAt(index);
            }
        }
        if (Objects.isNull(sheet)) {
            log.warn("获取字典sheet失败，sheet下标{}，sheet名称：{}", template.getSheetIndex(), template.getSheetName());
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), "模板填充字典失败");
        }
        return sheet;
    }

}

