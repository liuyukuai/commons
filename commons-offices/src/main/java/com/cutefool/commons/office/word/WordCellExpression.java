/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.OfficesStrategy;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
public class WordCellExpression implements OfficeExpression {

    @Override
    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies) {
        String expressions = cell.getText();
        expressions = expressions.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(0));
        expressions = expressions.substring(1, expressions.length() - 1);

        if (!expressions.contains(Strings.POINT)) {
            List offices = Expressions.parse(context.getData(), Expressions.PREFIX + expressions + Expressions.SUFFIX, ArrayList.class);
            WordTables.setCell(cell, offices, expressions);
        } else {
            expressions = OfficeExpression.doExpression(expressions, OfficeConstants.TABLE_PREFIX, OfficeConstants.TABLE_SUFFIX);
            WordTables.setCell(cell, context.getData(), Expressions.PREFIX + expressions + Expressions.SUFFIX);
        }
        this.doStrategy(row.getTable(), Collections.singletonList(row), strategies);
    }
}
