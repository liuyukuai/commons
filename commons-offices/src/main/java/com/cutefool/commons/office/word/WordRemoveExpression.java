/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.OfficesStrategy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
public class WordRemoveExpression {

    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies, AtomicBoolean isRemove) {
        String expressions = cell.getText();

        int index = expressions.indexOf(OfficeConstants.TABLE_REMOVE_PREFIX);
        expressions = expressions.substring(index + OfficeConstants.TABLE_REMOVE_PREFIX.length(), expressions.length() - 1);
        Object o = Expressions.parseObject(context.getData(), Expressions.PREFIX + expressions + Expressions.SUFFIX);

        XWPFTable table = row.getTable();

        // 如果是合并单元格

        CTTcPr tcPr = cell.getCTTc().getTcPr();

        int removeIndex = rowIndex.get();

        CTVMerge vMerge = tcPr.getVMerge();
        isRemove.set(this.remove(table, o, rowIndex));
        // 如果没有移除，去掉标签
        if (!isRemove.get()) {
            WordTables.setCell(cell, cell.getText().substring(0, index), "");
        }
        if (Objects.nonNull(vMerge)) {

            List<XWPFTableRow> rows = table.getRows();
            for (int i = 0; i < rows.size(); i++) {
                if (i > removeIndex) {
                    XWPFTableCell v = rows.get(removeIndex).getCell(cellIndex);
                    CTVMerge ctvMerge = v.getCTTc().getTcPr().getVMerge();

                    if (Objects.nonNull(ctvMerge)) {
                        if (Objects.equals(ctvMerge.getVal(), STMerge.RESTART)) {
                            return;
                        }
                        table.removeRow(removeIndex);
                    }
                }
            }
        }
    }


    private boolean remove(XWPFTable table, Object o, AtomicInteger rowIndex) {
        if (Objects.isNull(o)) {
            // 移除当前列
            table.removeRow(rowIndex.get());
            return true;
        } else if (o instanceof Collection) {
            if (((Collection) o).isEmpty()) {
                // 移除当前列
                table.removeRow(rowIndex.get());
                return true;
            }
        }
        return false;
    }
}
