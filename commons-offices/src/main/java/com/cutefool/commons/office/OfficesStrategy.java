/*
 *  
 */
package com.cutefool.commons.office;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

/**
 * 策略
 *
 * @author 271007729@qq.com
 * @date 8/4/21 10:57 AM
 */
public interface OfficesStrategy {
    /**
     * 可执行接口
     * <p>
     *
     * @param table table
     * @param rows  rows
     */
    void execute(XWPFTable table, List<XWPFTableRow> rows);
}
