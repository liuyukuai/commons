/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeConstants;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.office.OfficesStrategy;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:13 PM
 */
@Slf4j
public class WordImgExpression implements OfficeExpression {

    @Override
    public void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies) {
        String text = cell.getText();
        String expressions = text.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(0));
        log.info(expressions);
        expressions = OfficeExpression.doExpression(expressions, OfficeConstants.TABLE_PREFIX, OfficeConstants.TABLE_SUFFIX);
        WordTables.setCell(cell, context.getData(), expressions);
        this.doStrategy(row.getTable(), Collections.singletonList(row), strategies);
    }
}
