package com.cutefool.commons.office.excel.service;

import com.cutefool.commons.office.excel.pojo.ExcelCheckResult;
import com.cutefool.commons.office.excel.pojo.ExcelRelyInput;
import com.cutefool.commons.office.excel.pojo.ExcelRelyRead;
import com.cutefool.commons.office.excel.util.ExcelReadUtil;
import com.cutefool.commons.core.util.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Excel 读取/解析 接口定义
 *
 * @author 271007729@qq.com
 * @date 2020/2/14 9:27
 */
public interface ReadExcelService<T, P> {

    /**
     * excel 解析 约定的数据分隔符
     */
    String SPLIT_UNDER_LINE = "_";


    /**
     * 初始化依赖参数
     *
     * @param param 初始化数据可选参数
     * @return ExcelParams
     */
    ExcelRelyRead<P> initParam(ExcelRelyInput param);


    /**
     * 读取 Excel 中 sheet 页中的一行
     *
     * @param workbook 工作薄
     * @param row      Excel的一行数据
     * @param msgList  错误描述信息，空表示没有错误
     * @param params   通用参数【临时参数】
     * @return T 解析出的对象
     */
    default T readLine(Workbook workbook, Row row, List<String> msgList, ExcelRelyRead<P> params) {
        return null;
    }


    /**
     * Excel文件转换成实体对象
     *
     * @param param      数据解析参数
     * @param resultList 【非null】结果集接收器
     * @return Optional<CheckResult> 【校验结果 和 Workbook】
     */
    default Optional<ExcelCheckResult> sheetToEntity(ExcelRelyInput param, List<T> resultList) {
        MultipartFile file = param.getFile();
        Workbook workbook = ExcelReadUtil.getWorkbook(file);
        if (Objects.isNull(workbook)) {
            return Optional.empty();
        }
        // 0表示第1个sheet
        Integer sheetIndex = param.getSheetIndex();
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (Objects.isNull(sheet)) {
            return Optional.empty();
        }

        // 获取sheet页 数据行数
        int rowNum = sheet.getLastRowNum() + 1;
        ExcelRelyRead<P> params = this.initParam(param);
        // 表头
        params.setMapTitle(this.loadTitle(sheet));
        // Map<excel行下标, List<String>> 0表示第1行
        Map<Integer, List<String>> mapMsg = new HashMap<>(64);
        params.setMapMsg(mapMsg);
        params.setWorkbook(workbook);

        Integer rowIndex = param.getRowIndex();
        for (int i = rowIndex; i < rowNum; i++) {
            //遍历每行的单元格
            Row row = sheet.getRow(i);
            // 空行跳过
            if (Objects.isNull(row)) {
                continue;
            }
            List<String> msgList = new ArrayList<>();
            // 读取 每行数据
            T excel = readLine(workbook, row, msgList, params);
            // 数据为空，即空行跳过
            if (Objects.isNull(excel)) {
                continue;
            }
            if (Lists.iterable(msgList)) {
                mapMsg.put(i, msgList);
            }
            resultList.add(excel);
        }

        if (CollectionUtils.isEmpty(mapMsg)) {
            return Optional.empty();
        }

        ExcelCheckResult result = ExcelCheckResult.builder()
                .workbook(workbook)
                .fileName(ExcelReadUtil.getErrorName(file, workbook))
                .mapErrorMsg(mapMsg)
                .build();

        return Optional.of(result);
    }


    /**
     * 加载 excel 表头名称【按需实现】
     *
     * @param sheet Sheet
     * @return Map<Integer, String>
     */
    default Map<Integer, String> loadTitle(Sheet sheet) {
        return new HashMap<>(1);
    }


}
