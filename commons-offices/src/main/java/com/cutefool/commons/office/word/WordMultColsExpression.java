/*
 *  
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.office.OfficesStrategy;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.*;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多列处理类
 *
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
public class WordMultColsExpression implements OfficeExpression {


    @Override
    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies) {
        String expressions = cell.getText();
        // gas.time
        expressions = expressions.substring(expressions.indexOf(OfficeConstants.MULTIPLE_COLS_PREFIX) + OfficeConstants.MULTIPLE_COLS_PREFIX.length(), expressions.lastIndexOf(OfficeConstants.TABLE_SUFFIX));

        expressions = expressions.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(0));

        // 查询当前列之后的cell
        List<XWPFTableCell> tableCells = row.getTableCells();

        if (!expressions.contains(Strings.POINT)) {
            List offices = Expressions.parse(context.getData(), Expressions.PREFIX + expressions + Expressions.SUFFIX, ArrayList.class);
            this.setCell(tableCells, cellIndex, cell, expressions, offices);
            return;
        }

        int firstIndex = expressions.indexOf(Strings.POINT);

        String colsExpression = expressions.substring(firstIndex + 1);
        String objExpression = expressions.substring(0, firstIndex);
        List offices = Expressions.parse(context.getData(), Expressions.PREFIX + objExpression + Expressions.SUFFIX, ArrayList.class);
        this.setCell(tableCells, cellIndex, cell, colsExpression, offices);
        this.doStrategy(row.getTable(), Collections.singletonList(row), strategies);
    }


    private void setCell(List<XWPFTableCell> tableCells, int cellIndex, XWPFTableCell cell, String expressions, List offices) {


        AtomicInteger index = new AtomicInteger(0);

        tableCells.stream()
                .skip(cellIndex)
                .forEach(e -> {
                    if (Objects.nonNull(offices) && !offices.isEmpty()) {
                        if (index.get() < offices.size()) {
                            Object o = offices.get(index.getAndIncrement());
                            WordTables.setCell(e, o, Expressions.PREFIX + expressions + Expressions.SUFFIX);
                        }
                    } else {
                        WordTables.setCell(e, "", "");
                    }
                    WordTables.copyCTR(cell, e);
                });
    }


}
