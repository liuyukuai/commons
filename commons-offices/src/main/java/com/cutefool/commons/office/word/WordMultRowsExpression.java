/*
 *  
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.Office;
import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.office.OfficesStrategy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多列处理类
 *
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
public class WordMultRowsExpression implements OfficeExpression {

    @Override
    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies) {
        XWPFTable table = row.getTable();
        String text = cell.getText();
        // [rows*gasItems]
        // 获取多行表达式的对象，用于动态算行数
        String expressions = text.substring(text.indexOf(OfficeConstants.MULTIPLE_ROWS_PREFIX) + OfficeConstants.MULTIPLE_ROWS_PREFIX.length(), text.indexOf(OfficeConstants.TABLE_SUFFIX));
        expressions = expressions.replace("[_index", "");

        if (!expressions.contains(Strings.POINT)) {
            List offices = Expressions.parse(context.getData(), Expressions.PREFIX + expressions + Expressions.SUFFIX, ArrayList.class);
            this.setCell(table, rowIndex, row, cellIndex, cell, text, context, offices, strategies);
        } else {
            int firstIndex = expressions.indexOf(Strings.POINT);
            String objExpression = expressions.substring(0, firstIndex);
            Office data = context.getData();
            List offices = Expressions.parse(data, Expressions.PREFIX + objExpression + Expressions.SUFFIX, ArrayList.class);
            this.setCell(table, rowIndex, row, cellIndex, cell, text, context, offices, strategies);
        }

    }

    private void setCell(XWPFTable table, AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, String colsExpression, WordContext context, List offices, List<OfficesStrategy> strategies) {

        colsExpression = OfficeExpression.doExpression(colsExpression, OfficeConstants.TABLE_PREFIX, OfficeConstants.TABLE_SUFFIX);
        if (Objects.nonNull(offices) && !offices.isEmpty()) {
            List<XWPFTableRow> rows = new ArrayList<>();
            rows.add(row);
            // 遍历集合
            for (int i = 1; i < offices.size(); i++) {
                XWPFTableRow xwpfTableRow = WordTables.insertRow(table, row, rowIndex.incrementAndGet(), cellIndex, i, context.getData(), colsExpression, offices.get(i));
                OfficeExpression.doTables(rowIndex, xwpfTableRow, xwpfTableRow.getTableCells(), context);
                rows.add(xwpfTableRow);

            }
            // 处理_index
            WordTables.doIndex(row);
            this.doStrategy(row.getTable(), rows, strategies);
            // 处理第一个单元格式值
            if (colsExpression.contains(Strings.POINT)) {
                colsExpression = colsExpression.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(0));
                WordTables.setCell(cell, context.getData(), colsExpression);

            } else {
                WordTables.setCell(cell, offices.get(0), colsExpression);
            }
        } else {
            WordTables.setCell(cell, "", "");
        }
    }
}
