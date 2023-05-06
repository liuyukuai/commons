/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Excel 解析的入参
 *
 * @author 271007729@qq.com
 * @date 2022/3/21 15:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRelyInput {

    /**
     * 导入的文件
     */
    @NotNull
    private MultipartFile file;

    /**
     * Excel文件中要读取的sheet【0表示第1个sheet，为空或非法默认读取第1个】
     */
    private Integer sheetIndex;

    /**
     * 开始读取数据的行下标【0表示第1行，为空或非法默认读取第2行】
     */
    private Integer rowIndex;

    /**
     * 通用数据过滤参数【暂时没有想到好的实现方案】
     * Map<参数名, 参数值>
     */
    private Map<String, Object> filter;


    /**
     * 默认读取第 2 行
     *
     * @return 读取的行
     */
    public Integer getRowIndex() {
        // 遍历Excel的行，第1行是标题，从第2行读取数据，1表示第2行
        return (Objects.isNull(rowIndex) || rowIndex < 0) ? 1 : rowIndex;
    }


    /**
     * 默认读取第 1 个sheet
     *
     * @return 读取的sheet
     */
    public Integer getSheetIndex() {
        // 0表示第1个sheet
        return (Objects.isNull(sheetIndex) || sheetIndex < 0) ? 0 : sheetIndex;
    }

}
