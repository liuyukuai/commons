package com.cutefool.commons.office.excel.util;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * poi 校验工具类，持续增加中...
 *
 * @author 271007729@qq.com
 * @date 2020/2/14 11:04
 */
@SuppressWarnings("unused")
public class ExcelReadUtil {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ExcelReadUtil.class);


    /**
     * 获取单元格 字符串 值
     *
     * @param cell 单元格
     * @return 【非空】String OR null
     */
    public static String getStringCellValue(@NotNull Cell cell) {
        if (Objects.isNull(cell)) {
            return null;
        }
        String value = null;
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                // 处理数字
                double numericCellValue = cell.getNumericCellValue();
                BigDecimal bdValue = BigDecimal.valueOf(numericCellValue);
                // 去掉后面无用的零  如小数点后面全是零则去掉小数点
                value = removeEndZero(bdValue.toPlainString());
                break;
            default:
        }
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }


    /**
     * 去掉数值后面无用的零，如小数点后面全是零则去掉小数点
     *
     * @param num 数值
     * @return 处理后的数值
     */
    private static String removeEndZero(String num) {
        if (StringUtils.isBlank(num)) {
            return num;
        }
        if (num.contains(ExcelConstant.SPLIT_POINT_EN)) {
            if (num.endsWith(ExcelConstant.SPLIT_POINT_EN)) {
                num = num.substring(0, num.length() - 1);
                return num;
            }
            if (num.endsWith(ExcelConstant.STR_ZERO)) {
                num = num.substring(0, num.length() - 1);
            } else {
                return num;
            }
            num = ExcelReadUtil.removeEndZero(num);
        }
        return num;
    }


    /**
     * 获取单元格 字符串 值
     *
     * @param row Excel的行
     * @param i   该行第i个单元格
     * @return 【非空】String OR null
     */
    public static String getStringCellValue(@NotNull Row row, int i) {
        Cell cell = row.getCell(i);
        return getStringCellValue(cell);
    }


    /**
     * 获取单元格 电话号码【固话+手机】 值
     *
     * @param row Excel的行
     * @param i   该行第i个单元格
     * @return 【非空】String OR null
     */
    public static String getTelCellValue(@NotNull Row row, int i) {
        String cellValue = getStringCellValue(row, i);
        if (cellValue == null) {
            return null;
        }
        // TO.DO 可优化 判断-的位置是否正确
        return cellValue
                .replace(" ", "")
                .replace("-", "")
                .replace("_", "");
    }


    /**
     * 获取单元格 日期 值
     *
     * @param row       Excel的行
     * @param i         该行第i个单元格
     * @param typeCheck 长度为1的非空boolean数组,typeCheck[0]==false表示输入的日期格式错误, 默认初始值是 typeCheck[0] = true
     * @return Date OR null
     */
    public static Date getDateCellValue(@NotNull Row row, int i, Boolean[] typeCheck) {
        typeCheck[0] = true;
        Cell cell = row.getCell(i);
        if (Objects.isNull(cell)) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getDateCellValue();
            case BLANK:
                return null;
            case STRING:
                String dateStr = cell.getStringCellValue();
                Date date = parseAllDate(dateStr);
                if (Objects.nonNull(date)) {
                    return date;
                }
            default:
                // 标志 日期 字段输入有误
                typeCheck[0] = false;
                return null;
        }
    }


    /**
     * 获取单元格 日期-时间 值
     *
     * @param row       Excel的行
     * @param i         该行第i个单元格
     * @param typeCheck 长度为1的非空boolean数组,typeCheck[0]==false表示输入的日期格式错误, 默认初始值是 typeCheck[0] = true
     * @return Date OR null
     */
    public static Date getDateTimeCellValue(@NotNull Row row, int i, Boolean[] typeCheck) {
        typeCheck[0] = true;
        Cell cell = row.getCell(i);
        if (Objects.isNull(cell)) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getDateCellValue();
            case BLANK:
                return null;
            case STRING:
                String dateStr = cell.getStringCellValue();
                Date date = parseAllDateTime(dateStr);
                if (Objects.nonNull(date)) {
                    return date;
                }
            default:
                // 标志 日期 字段输入有误
                typeCheck[0] = false;
                return null;
        }
    }


    /**
     * 获取单元格 Integer 值，向下取整
     *
     * @param row Excel的行
     * @param i   该行第i个单元格
     * @return Date OR null
     */
    public static Integer getIntegerCellValue(@NotNull Row row, int i) {
        Double doubleVal = getDoubleCellValue(row, i);
        if (Objects.isNull(doubleVal)) {
            return null;
        }
        return doubleVal.intValue();
    }


    /**
     * 获取单元格 Double 值
     *
     * @param row Excel的行
     * @param i   该行第i个单元格
     * @return Date OR null
     */
    public static Double getDoubleCellValue(@NotNull Row row, int i) {
        Cell cell = row.getCell(i);
        if (Objects.isNull(cell)) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.valueOf(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    log.error("“{}”不能转换成数值!", cell.getStringCellValue(), e);
                    return null;
                }
            default:
                return null;
        }
    }


    /**
     * 获取单元格 Decimal 值
     *
     * @param row Excel的行
     * @param i   该行第i个单元格
     * @return Date OR null
     */
    public static BigDecimal getBigDecimalCellValue(@NotNull Row row, int i) {
        Double doubleVal = getDoubleCellValue(row, i);
        if (null == doubleVal) {
            return null;
        }
        return new BigDecimal(doubleVal.toString());
    }


    /**
     * 获取工作薄 对象 兼容2003和2007
     *
     * @param file 文件对象
     * @return Workbook  【非null】Excel工作薄
     */
    public static Workbook getWorkbook(MultipartFile file) {
        if (Objects.isNull(file)) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        boolean invalidName = StringUtils.isBlank(fileName)
                || (!fileName.matches(ExcelConstant.EXCEL_03_SUFFIX_REGEX) && !fileName.matches(ExcelConstant.EXCEL_07_SUFFIX_REGEX));
        if (invalidName) {
            throw new BizException(ResponseCode.API_PARAM_ERROR.getCode(), "上传的Excel文件格式不正确,文件名：" + fileName);
        }
        //判断Excel文件的版本，2003和2007及其以上版本
        Workbook workbook;
        try {
            InputStream inputStream = file.getInputStream();
            if (fileName.matches(ExcelConstant.EXCEL_07_SUFFIX_REGEX)) {
                // Excel 2007 及其以上版本
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // Excel 2003  版本
                workbook = new HSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            // 此处不能 catch (IOException e) {  如果上传的Excel文件损坏，IOException 不能捕获该异常
            log.error("获取文件失败,文件名：" + fileName, e);
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), "获取文件失败,文件名：" + fileName);
        }
        return workbook;
    }


    /**
     * 加载 2007及其以上版本 excel模板文件
     *
     * @param template 文件地址
     * @return Workbook【非null】
     */
    public static Workbook getPresentWorkbook07(String template) {
        Workbook workbook;
        try {
            // 模板都是2007及其以上版本
            workbook = new XSSFWorkbook(new ClassPathResource(template).getInputStream());
        } catch (IOException e) {
            log.warn("加载 " + template + " 文件失败", e);
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), template + " 模板生成失败");
        }
        return workbook;
    }


    /**
     * 加载 Workbook 的 Sheet
     *
     * @param workbook 工作薄
     * @param index    需要读取的sheet【0表示第1个sheet】
     * @return Optional<Sheet>
     */
    public static Optional<Sheet> getSheet(Workbook workbook, Integer index) {
        if (Objects.isNull(workbook)) {
            return Optional.empty();
        }

        index = Optional.ofNullable(index).filter(e -> e >= 0).orElse(0);

        Sheet sheet = null;
        try {
            sheet = workbook.getSheetAt(index);
        } catch (Exception e) {
            log.warn("加载sheet失败");
        }
        return Optional.ofNullable(sheet);
    }


    /**
     * 获取行
     *
     * @param sheet Sheet
     * @param index 下标
     * @return Row
     */
    public static Row getPresentRow(@NotNull Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        if (Objects.isNull(row)) {
            row = sheet.createRow(index);
        }
        return row;
    }


    /**
     * 获取单元格
     *
     * @param row   Row
     * @param index index
     * @return Cell
     */
    public static Cell getPresentCell(@NotNull Row row, int index) {
        Cell cell = row.getCell(index);
        if (Objects.isNull(cell)) {
            cell = row.createCell(index);
        }
        return cell;
    }


    /**
     * 万能日期解析
     *
     * @param dateStr 日期字符串
     * @return Date or null
     */
    public static Date parseAllDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelConstant.FORMAT_30);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e1) {
            sdf = new SimpleDateFormat(ExcelConstant.FORMAT_31);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e2) {
                sdf = new SimpleDateFormat(ExcelConstant.FORMAT_32);
                try {
                    date = sdf.parse(dateStr);
                } catch (ParseException e3) {
                    sdf = new SimpleDateFormat(ExcelConstant.FORMAT_33);
                    try {
                        date = sdf.parse(dateStr);
                    } catch (ParseException e4) {
                        sdf = new SimpleDateFormat(ExcelConstant.FORMAT_38);
                        try {
                            date = sdf.parse(dateStr);
                        } catch (ParseException e5) {
                            log.error("---------------日期【" + dateStr + "】解析失败！");
                        }
                    }
                }
            }
        }
        return date;
    }


    /**
     * 万能日期时间解析
     *
     * @param dateStr 日期时间字符串
     * @return Date or null
     */
    public static Date parseAllDateTime(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelConstant.FORMAT_00);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            sdf = new SimpleDateFormat(ExcelConstant.FORMAT_01);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e1) {
                sdf = new SimpleDateFormat(ExcelConstant.FORMAT_02);
                try {
                    date = sdf.parse(dateStr);
                } catch (ParseException e2) {
                    sdf = new SimpleDateFormat(ExcelConstant.FORMAT_03);
                    try {
                        date = sdf.parse(dateStr);
                    } catch (ParseException e3) {
                        sdf = new SimpleDateFormat(ExcelConstant.FORMAT_04);
                        try {
                            date = sdf.parse(dateStr);
                        } catch (ParseException e4) {
                            date = parseAllDate(dateStr);
                        }
                    }
                }
            }
        }
        return date;
    }


    /**
     * 获取当前时间
     *
     * @param pattern 时间格式
     * @return 当前时间字符串
     */
    public static String getNow(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(new Date());
        } catch (Exception e) {
            log.error("---------------日期 " + pattern + " 解析失败！");
        }
        return "";
    }


    /**
     * Date转LocalDate
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    /**
     * 生成新的excel文件名
     *
     * @param file     原文件
     * @param workbook 工作薄
     * @param subName  自定义部分的名称
     * @return 文件名
     */
    public static String getErrorName(MultipartFile file, Workbook workbook, String... subName) {
        String nameTemp;
        if (Objects.nonNull(subName) && subName.length > 0) {
            nameTemp = "_" + subName[0];
        } else {
            nameTemp = "_异常";
        }
        // 保存错误的 Excel
        String originalFilename = file.getOriginalFilename();
        String name = "";
        if (StringUtils.isNotBlank(originalFilename) && originalFilename.lastIndexOf(ExcelConstant.SPLIT_POINT_EN) > 0) {
            name = originalFilename.substring(0, originalFilename.lastIndexOf(ExcelConstant.SPLIT_POINT_EN));
        }
        //  Excel 2003  版本
        if (workbook instanceof HSSFWorkbook) {
            return name + "-" + ExcelReadUtil.getNow(ExcelConstant.FORMAT_39) + nameTemp + ".xls";
        }
        return name + "-" + ExcelReadUtil.getNow(ExcelConstant.FORMAT_39) + nameTemp + ".xlsx";
    }


}
