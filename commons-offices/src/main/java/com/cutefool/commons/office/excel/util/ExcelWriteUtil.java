/*
 *  
 */
package com.cutefool.commons.office.excel.util;

import com.cutefool.commons.office.excel.pojo.ExcelCellType;
import com.cutefool.commons.office.excel.pojo.ExcelWriteData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * excel 写数据工具类
 *
 * @author 271007729@qq.com
 * @date 2019/9/25 10:39
 */
@SuppressWarnings("unused")
public class ExcelWriteUtil {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ExcelWriteUtil.class);


    /**
     * 填充单元格数据
     *
     * @param workbook 工作薄
     * @param index    需要读取的sheet【0表示第1个sheet】
     * @param cellList 数据
     */
    public static void fillCell(Workbook workbook, Integer index, List<ExcelWriteData> cellList) {
        if (Objects.isNull(workbook) || CollectionUtils.isEmpty(cellList)) {
            return;
        }
        Optional<Sheet> optional = ExcelReadUtil.getSheet(workbook, index);
        if (!optional.isPresent()) {
            return;
        }
        Sheet sheet = optional.get();

        for (ExcelWriteData data : cellList) {
            Integer rowBegin = data.getRowBegin();
            Integer column = data.getColumn();
            if (Objects.isNull(rowBegin) || Objects.isNull(column)) {
                continue;
            }
            Integer rowEnd = Optional.ofNullable(data.getRowEnd()).orElse(rowBegin);
            // 合并行单元格
            if (rowEnd > rowBegin) {
                sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowEnd, column, column));
            }

            Cell cell = ExcelReadUtil.getPresentCell(ExcelReadUtil.getPresentRow(sheet, rowBegin), column);

            Optional.ofNullable(data.getType()).ifPresent(type -> {
                if (Objects.equals(type.getWrapped(), true)) {
                    CellStyle cellStyle = cell.getCellStyle();
                    cellStyle.setWrapText(true);
                }
            });

            cell.setCellValue(data.getCellValue());
        }
    }


    /**
     * 获取一个空的单元对象
     *
     * @param dataList 所有单元格对象容器
     * @return 新单元格对象
     */
    public static ExcelWriteData getAndAddCell(List<ExcelWriteData> dataList, ExcelCellType type) {
        ExcelWriteData excelCell = new ExcelWriteData(type);
        dataList.add(excelCell);
        return excelCell;
    }


}
