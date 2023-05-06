package com.cutefool.commons.office.excel.util;

import com.cutefool.commons.office.excel.pojo.ExcelCheckResult;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Excel 下载工具
 *
 * @author 271007729@qq.com
 * @date 2020/2/14 11:04
 */
public class ExcelDownloadUtil {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ExcelDownloadUtil.class);


    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param workbook 工作薄
     * @param response HttpServletResponse
     */
    public static void download(String fileName, Workbook workbook, HttpServletResponse response) {
        // 设置为下载application/x-download
        response.setContentType("application/octet-stream");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        // 前端要求加的值
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        OutputStream outStream;
        try {
            outStream = response.getOutputStream();
        } catch (IOException e) {
            log.error("getOutputStream()创建失败 ", e);
            return;
        }
        try {
            workbook.write(outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            log.error("文件下载失败,文件名:{} ", fileName, e);
        }
    }


    /**
     * 下载文件
     *
     * @param result   校验结果
     * @param response HttpServletResponse
     */
    public static void download(ExcelCheckResult result, HttpServletResponse response) {
        ExcelCheckUtil.setErrorMsg(result);
        ExcelDownloadUtil.download(result.getFileName(), result.getWorkbook(), response);
    }


    /**
     * 下载文件
     *
     * @param result    校验结果
     * @param response  HttpServletResponse
     * @param cellStyle 错误标记样式，null时使用默认样式
     */
    public static void download(ExcelCheckResult result, HttpServletResponse response, @Nullable CellStyle cellStyle) {
        ExcelCheckUtil.setErrorMsg(result, cellStyle);
        ExcelDownloadUtil.download(result.getFileName(), result.getWorkbook(), response);
    }

    /**
     * 下载文件-多sheet
     *
     * @param result   校验结果
     * @param response HttpServletResponse
     */
    public static void downloadMoreSheet(ExcelCheckResult result, HttpServletResponse response) {
        ExcelCheckUtil.setErrorMsg(result);
        ExcelDownloadUtil.download(result.getFileName(), result.getWorkbook(), response);
    }


    /**
     * 下载文件-多sheet
     *
     * @param result    校验结果
     * @param response  HttpServletResponse
     * @param cellStyle 错误标记样式，null时使用默认样式
     */
    public static void downloadMoreSheet(ExcelCheckResult result, HttpServletResponse response, @Nullable CellStyle cellStyle) {
        ExcelCheckUtil.setMoreSheetErrorMsg(result, cellStyle);
        ExcelDownloadUtil.download(result.getFileName(), result.getWorkbook(), response);
    }
}
