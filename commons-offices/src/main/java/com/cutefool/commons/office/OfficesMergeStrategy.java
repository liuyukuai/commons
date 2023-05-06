/*
 *
 */
package com.cutefool.commons.office;

import com.cutefool.commons.core.util.Lists;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.List;

/**
 * 合并策略
 *
 * @author 271007729@qq.com
 * @date 8/5/21 2:29 PM
 */
@FunctionalInterface
public interface OfficesMergeStrategy extends OfficesStrategy {

    /**
     * 需要合并的列
     *
     * @return 合并的列
     */
    List<Integer> cols();

    /**
     * 可执行接口
     * <p>
     *
     * @param table table
     * @param rows  rows
     */
    @Override
    default void execute(XWPFTable table, List<XWPFTableRow> rows) {
        List<Integer> cols = this.cols();

        for (int j = 0; j < Lists.empty(rows).size(); j++) {
            XWPFTableRow row = rows.get(j);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int i = 0; i < cells.size(); i++) {
                if (cols.contains(i)) {
                    XWPFTableCell cell = cells.get(i);
                    CTTcPr tcPr = cell.getCTTc().getTcPr();
                    CTVMerge ctvMerge = tcPr.addNewVMerge();
                    if (j == 0) {
                        ctvMerge.setVal(STMerge.RESTART);
                    } else {
                        ctvMerge.setVal(STMerge.CONTINUE);
                    }
                }
            }
        }
    }
}
