/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficesStrategy;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
public class WordSimpleExpression implements OfficeExpression {

    @Override
    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies) {
        String expressions = cell.getText();
        expressions = expressions.replace("_index", "0");
        if (Objects.nonNull(context.getData())) {
            WordTables.setCell(cell, context.getData(), expressions);
        } else {
            WordTables.setCell(cell, "", "");
        }
        this.doStrategy(row.getTable(), Collections.singletonList(row), strategies);
    }
}
