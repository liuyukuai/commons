package com.cutefool.commons.office.excel.util;

import com.cutefool.commons.office.excel.pojo.ExcelCheckResult;
import com.cutefool.commons.office.excel.pojo.ExcelMergeResult;
import com.cutefool.commons.core.util.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * poi 校验工具类，持续增加中...
 *
 * @author 271007729@qq.com
 * @date 2020/2/14 11:04
 */
@SuppressWarnings("unused")
public class ExcelCheckUtil {

    /**
     * 校验器
     */
    private final static Validator VALIDATOR;

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ExcelCheckUtil.class);

    /**
     * 常用提示信息
     */
    public static String MSG_NOT_BLANK = "%s不能为空";
    public static String MSG_NOT_EXIST = "%s【%s】不存在";
    public static String MSG_EXIST = "%s【%s】已存在";
    public static String MSG_EXIST_RELATION = "%s『%s』下已存在%s【%s】";
    public static String MSG_NOT_EXIST_RELATION = "%s『%s』下不存在%s【%s】";
    public static String MSG_NOT_EXIST_RELATION_INDIRECT = "%s『%s』关联『%s』下不存在%s【%s】";


    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }


    /**
     * 校验对象的合法性，返回错误信息map，key=变量名，value=错误信息
     *
     * @param obj 被校验对象
     * @return Map OR null
     */
    public static Map<String, String> validation(Object obj) {
        if (Objects.isNull(obj)) {
            return new HashMap<>(1);
        }
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(obj);
        if (validate != null && validate.size() > 0) {
            return getValidateErrMsg(validate);
        }
        return new HashMap<>(1);
    }


    /**
     * 获取校验信息，返回错误信息map，key=变量名，value=错误信息
     *
     * @param validate 必须非空
     * @return HashMap
     */
    private static Map<String, String> getValidateErrMsg(Set<ConstraintViolation<Object>> validate) {
        Map<String, String> msgMap = new LinkedHashMap<>();
        validate.forEach(o -> msgMap.put(o.getPropertyPath().toString(), o.getMessage()));
        return msgMap;
    }


    /**
     * 校验最大长度
     *
     * @param cellValue 单元格的值
     * @param maxLen    最大长度
     * @return 校验失败的文案
     */
    public static String checkLen(String cellValue, int maxLen) {
        String msg = null;
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else if (cellValue.length() > maxLen) {
            msg = "不能超过 " + maxLen + " 位";
        }
        return msg;
    }


    /**
     * 校验最大长度，并设置错误样式
     *
     * @param workbook  工作薄
     * @param row       行
     * @param index     操作的单元格下标
     * @param cellValue 单元格的值
     * @param maxLen    最大长度
     * @return 校验失败的文案
     */
    public static String checkLen(Workbook workbook, Row row, int index, String cellValue, int maxLen) {
        String msg = null;
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else if (cellValue.length() > maxLen) {
            msg = "不能超过 " + maxLen + " 位";
        }
        if (Objects.nonNull(msg)) {
            ExcelCheckUtil.setErrorCellStyle(workbook, row, index);
        }
        return msg;
    }


    /**
     * 校验非空和最大长度
     *
     * @param cellValue 单元格的值
     * @param maxLen    最大长度
     * @return 校验失败的文案
     */
    public static String checkBlankAndLen(String cellValue, int maxLen) {
        String msg = null;
        if (StringUtils.isBlank(cellValue)) {
            msg = "不能为空";
        } else if (cellValue.length() > maxLen) {
            msg = "不能超过 " + maxLen + " 位";
        }
        return msg;
    }


    /**
     * 校验非空和最大长度，并设置错误样式
     *
     * @param workbook  工作薄
     * @param row       行
     * @param index     操作的单元格下标
     * @param cellValue 单元格的值
     * @param maxLen    最大长度
     * @return 校验失败的文案
     */
    public static String checkBlankAndLen(Workbook workbook, Row row, int index, String cellValue, int maxLen) {
        String msg = null;
        if (StringUtils.isBlank(cellValue)) {
            msg = "不能为空";
        } else if (cellValue.length() > maxLen) {
            msg = "不能超过 " + maxLen + " 位";
        }
        if (Objects.nonNull(msg)) {
            ExcelCheckUtil.setErrorCellStyle(workbook, row, index);
        }
        return msg;
    }


    /**
     * 设置单元格 错误信息提示 的样式
     *
     * @param cell  单元格
     * @param style 样式
     */
    public static void setErrorCellStyle(Cell cell, CellStyle style) {
        if (Objects.isNull(cell)) {
            return;
        }
        cell.setCellStyle(style);
    }


    /**
     * 设置单元格 错误信息提示 的样式
     *
     * @param workbook Excel工作薄
     * @param cell     单元格
     */
    public static void setErrorCellStyle(Workbook workbook, Cell cell) {
        if (Objects.isNull(workbook) || Objects.isNull(cell)) {
            return;
        }
        cell.setCellStyle(defWarningStyle(workbook));
    }


    /**
     * 设置单元格 错误信息提示 的样式
     *
     * @param workbook  Excel工作薄
     * @param row       sheet 的 行
     * @param cellIndex 0 based column number
     */
    public static void setErrorCellStyle(Workbook workbook, Row row, int cellIndex) {
        if (Objects.isNull(workbook) || Objects.isNull(row)) {
            return;
        }
        Cell cell = ExcelReadUtil.getPresentCell(row, cellIndex);
        CellStyle cellStyle = defWarningStyle(workbook);
        cell.setCellStyle(cellStyle);
    }


    /**
     * 设置单元格 错误信息提示 的样式
     *
     * @param row       sheet 的 行
     * @param cellIndex 0 based column number
     * @param style     样式
     */
    public static void setErrorCellStyle(Row row, int cellIndex, CellStyle style) {
        if (Objects.isNull(row)) {
            return;
        }
        Cell cell = ExcelReadUtil.getPresentCell(row, cellIndex);
        cell.setCellStyle(style);
    }


    /**
     * 恢复单元格 默认 的样式
     *
     * @param workbook Excel工作薄
     * @param row      sheet 的 行
     * @param cellNum  0 based column number
     */
    public static void setDefCellStyle(Workbook workbook, Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        // 空单元格忽略即可
        if (Objects.nonNull(cell)) {
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setLocked(false);
            cell.setCellStyle(cellStyle);
        }
    }


    /**
     * 默认的警告样式
     *
     * @param workbook Excel工作薄
     * @return CellStyle
     */
    public static CellStyle defWarningStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setLocked(false);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        /// 设置字体颜色
        Font font = workbook.createFont();
        cellStyle.setFont(font);
        font.setColor(IndexedColors.BLACK.index);
        return cellStyle;
    }


    /**
     * 设置错误信息
     *
     * @param result 校验结果
     */
    public static void setErrorMsg(ExcelCheckResult result) {
        if (Objects.isNull(result)) {
            return;
        }
        ExcelCheckUtil.setErrorMsg(result.getWorkbook(), result.getIndex(), result.getMapErrorMsg());
    }


    /**
     * 设置错误信息
     *
     * @param result    校验结果
     * @param cellStyle 错误样式
     */
    public static void setErrorMsg(ExcelCheckResult result, CellStyle cellStyle) {
        if (Objects.isNull(result)) {
            return;
        }
        Workbook workbook = result.getWorkbook();
        if (Objects.isNull(cellStyle)) {
            cellStyle = ExcelCheckUtil.defWarningStyle(workbook);
        }
        ExcelCheckUtil.setErrorMsg(workbook, result.getIndex(), result.getRowColumnErrorMsg(), cellStyle);
    }


    /**
     * 设置错误信息-多sheet
     *
     * @param result    校验结果
     * @param cellStyle 错误样式
     */
    public static void setMoreSheetErrorMsg(ExcelCheckResult result, CellStyle cellStyle) {
        if (Objects.isNull(result)) {
            return;
        }
        Workbook workbook = result.getWorkbook();
        if (Objects.isNull(cellStyle)) {
            cellStyle = ExcelCheckUtil.defWarningStyle(workbook);
        }
        ExcelCheckUtil.setMoreSheetErrorMsg(workbook, result.getSheetColumnErrorMsg(), cellStyle);
    }


    /**
     * 设置错误信息
     *
     * @param workbook 工作薄
     * @param index    需要读取的sheet【0表示第1个sheet】
     * @param mapError 校验错误信息 Map<excel行号, List<String>> 0表示第一行
     */
    public static void setErrorMsg(Workbook workbook, Integer index, Map<Integer, List<String>> mapError) {
        if (Objects.isNull(workbook) || CollectionUtils.isEmpty(mapError)) {
            return;
        }
        Optional<Sheet> sheetOptional = ExcelReadUtil.getSheet(workbook, index);
        if (!sheetOptional.isPresent()) {
            return;
        }
        Sheet sheet = sheetOptional.get();

        for (Map.Entry<Integer, List<String>> entry : mapError.entrySet()) {
            Integer line = entry.getKey();
            Row row = ExcelReadUtil.getPresentRow(sheet, line);
            ExcelReadUtil.getPresentCell(row, 0).setCellValue(String.join("；", entry.getValue()));
        }
    }


    private static void setErrorMsg(Workbook workbook, int index, Map<Integer, Map<Integer, List<String>>> errorMsg, @NotNull CellStyle cellStyle) {
        if (Objects.isNull(workbook)) {
            return;
        }
        Optional<Sheet> sheetOptional = ExcelReadUtil.getSheet(workbook, index);
        if (!sheetOptional.isPresent()) {
            return;
        }
        Sheet sheet = sheetOptional.get();

        errorMsg.forEach((rowIndex, columnMsg) -> {
            Row row = sheet.getRow(rowIndex);
            if (Objects.nonNull(row)) {
                List<String> msgList = new ArrayList<>();
                for (Map.Entry<Integer, List<String>> entry : columnMsg.entrySet()) {
                    List<String> value = entry.getValue();
                    if (CollectionUtils.isEmpty(value)) {
                        continue;
                    }
                    msgList.addAll(value);
                    Optional.ofNullable(row.getCell(entry.getKey())).ifPresent(cell -> cell.setCellStyle(cellStyle));
                }
                if (Lists.iterable(msgList)) {
                    ExcelReadUtil.getPresentCell(row, 0).setCellValue(String.join("；", msgList));
                }
            }
        });
    }


    /**
     * 设置错误信息-多sheet
     *
     * @param workbook 工作薄
     * @param errorMsg 工作簿+行+列表 校验错误信息 Map<工作簿下标, Map<行下标, Map<列下标, List<提示信息>>>>  0表示第1工作簿或第1行或第1列
     * @param cellStyle 错误标记样式，null时使用默认样式
     */
    private static void setMoreSheetErrorMsg(Workbook workbook, Map<Integer, Map<Integer, Map<Integer, List<String>>>> errorMsg, @NotNull CellStyle cellStyle) {
        if (Objects.isNull(workbook)) {
            return;
        }
        errorMsg.forEach((sheetIndex, rowMsg) -> {
            Optional<Sheet> sheetOptional = ExcelReadUtil.getSheet(workbook, sheetIndex);
            if (!sheetOptional.isPresent()) {
                return;
            }
            Sheet sheet = sheetOptional.get();
            rowMsg.forEach((rowIndex, columnMsg) -> {
                Row row = sheet.getRow(rowIndex);
                if (Objects.nonNull(row)) {
                    List<String> msgList = new ArrayList<>();
                    for (Map.Entry<Integer, List<String>> entry : columnMsg.entrySet()) {
                        List<String> value = entry.getValue();
                        if (CollectionUtils.isEmpty(value)) {
                            continue;
                        }
                        msgList.addAll(value);
                        Optional.ofNullable(row.getCell(entry.getKey())).ifPresent(cell -> cell.setCellStyle(cellStyle));
                    }
                    if (Lists.iterable(msgList)) {
                        ExcelMergeResult mergeResult = isMergedRow(sheet, rowIndex);
                        if(mergeResult.isFlag()){
                            String cellValue = ExcelReadUtil.getPresentCell(sheet.getRow(mergeResult.getRowBegin()), 0).getStringCellValue();
                            if(StringUtils.isNotBlank(cellValue)){
                                msgList.add(0,cellValue);
                            }
                            ExcelReadUtil.getPresentCell(sheet.getRow(mergeResult.getRowBegin()), 0).setCellValue(String.join("；", msgList));
                        }else {
                            ExcelReadUtil.getPresentCell(row, 0).setCellValue(String.join("；", msgList));
                        }
                    }
                }
            });
        });
    }



    /**
     * 验证实体对象是否为空，如果对象属性为 null 或 "" 或 0 ，则该对象为空。
     *
     * @param entity java对象
     * @return true:空，false:非空
     */
    public static boolean isEmpty(Object entity) {
        if (Objects.isNull(entity)) {
            return true;
        }
        PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(entity);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            String name = origDescriptor.getName();
            if ("class".equalsIgnoreCase(name)) {
                continue;
            }
            if (PropertyUtils.isReadable(entity, name)) {
                Object value = null;
                try {
                    value = PropertyUtils.getSimpleProperty(entity, name);
                } catch (IllegalAccessException e) {
                    log.warn("属性没有访问权限", e);
                } catch (InvocationTargetException e) {
                    log.warn("调用目标异常", e);
                } catch (NoSuchMethodException e) {
                    log.warn("调用方法不存在", e);
                }
                if (Objects.isNull(value)) {
                    continue;
                }
                if (value instanceof String) {
                    if (((String) value).length() > 0) {
                        return false;
                    }
                } else if (value instanceof Number) {
                    if (Math.abs(Double.parseDouble(String.valueOf(value))) != 0) {
                        return false;
                    }
                } else if (value instanceof Collection) {
                    if (!CollectionUtils.isEmpty((Collection<?>) value)) {
                        return false;
                    }
                } else if (value instanceof Map) {
                    if (!CollectionUtils.isEmpty((Map<?, ?>) value)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 判断指定的行是否是合并行以及最大行值
     *
     * @param sheet 工作簿
     * @param row   行下标
     * @return
     */
    private static ExcelMergeResult isMergedRow(Sheet sheet, int row) {
        ExcelMergeResult result = new ExcelMergeResult();
        result.setFlag(false);
        // 得到一个 sheet 中有多少个合并单元格
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            // 获取合并后的单元格
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow && firstColumn == 0 && lastColumn == 0) {
                result.setFlag(true);
                result.setRowBegin(firstRow);
                result.setRowEnd(lastRow);
                return result;
            }
        }
        return result;
    }

}
