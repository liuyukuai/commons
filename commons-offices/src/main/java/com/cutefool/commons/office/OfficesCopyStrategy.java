/*
 *  
 */
package com.cutefool.commons.office;

import com.cutefool.commons.office.word.WordContext;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.word.OfficeExpression;
import com.cutefool.commons.office.word.WordTables;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 合并策略
 *
 * @author 271007729@qq.com
 * @date 8/5/21 2:29 PM
 */
@FunctionalInterface
public interface OfficesCopyStrategy {


    /**
     * 拷贝配置
     *
     * @return
     */
    int rows();


    /**
     * 可执行接口
     * <p>
     *
     * @param table       table
     * @param row         row
     * @param rowIndex    rowIndex
     * @param context     context
     * @param expressions expressions
     */
    default void execute(XWPFTable table, XWPFTableRow row, AtomicInteger rowIndex, String expressions, WordContext context) {
        expressions = expressions.substring(0, expressions.lastIndexOf("[_index]")).replace("_index", "0");
        ArrayList<?> config = Expressions.parse(context.getData(), expressions + Expressions.SUFFIX, ArrayList.class);

        int rows = rows();
        int currentRows = rowIndex.get();

        // 如果小于等于0，说明不拷贝
        if (Lists.isEmpty(config) || rows <= 0) {
            return;
        }
        int times = config.size() - 1;
        for (int i = 0; i < times; i++) {
            List<XWPFTableRow> newRows = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                int destIndex;
                // 但j=0时候拷贝当前行
                if (j == 0) {
                    destIndex = currentRows + rows * (i + 1);
                    newRows.add(this.copy(table, row, destIndex, i + 1));
                } else {
                    XWPFTableRow xwpfTableRow = table.getRow(currentRows + j);
                    destIndex = (currentRows + j) + rows * (i + 1);
                    newRows.add(this.copy(table, xwpfTableRow, destIndex, i + 1));
                }
            }

            List<XWPFTableRow> rows1 = table.getRows();
            Lists.empty(newRows)
                    .forEach(e -> {
                        int index = rows1.indexOf(e);
                        OfficeExpression.doRemoveTables(new AtomicInteger(index), e, e.getTableCells(), context);
                    });

        }
    }

    /**
     * 拷贝
     *
     * @param table     table
     * @param row       被拷贝的行
     * @param destIndex 目标位置
     * @param loop      第几个
     */
    default XWPFTableRow copy(XWPFTable table, XWPFTableRow row, int destIndex, int loop) {
        // 在表格中指定的位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(destIndex);
        //复制行对象
        targetRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
        //或许需要复制的行的列
        List<XWPFTableCell> copyCells = row.getTableCells();
        //复制列对象
        XWPFTableCell target;
        for (int i = 0; i < copyCells.size(); i++) {

            target = targetRow.addNewTableCell();
            XWPFTableCell source = copyCells.get(i);
            target.getCTTc().setTcPr(source.getCTTc().getTcPr());
            // 列属性
            // 删除段落
            for (int pos = 0; pos < target.getParagraphs().size(); pos++) {
                target.removeParagraph(pos);
            }
            // 添加段落
            for (XWPFParagraph sp : source.getParagraphs()) {
                XWPFParagraph targetP = target.addParagraph();
                WordTables.copyParagraph(targetP, sp);
            }

            if (Objects.equals(OfficeConstants.INDEX_PREFIX, target.getText().trim())) {
                WordTables.setCell(target, String.valueOf(loop + 1), "");
            }

            //如果包含_index
            //如果包含_index
            String text = source.getText();
            int count = this.count(text, OfficeConstants.INDEX_PREFIX);
            if (StringUtils.isNotBlank(text) && count > 1) {
                WordTables.setCell(source, text.replaceFirst(OfficeConstants.INDEX_PREFIX, "0"), null);
            }

            //如果包含_index
            text = target.getText();
            if (StringUtils.isNotBlank(text)) {
                WordTables.setCell(target, text.replaceFirst(OfficeConstants.INDEX_PREFIX, String.valueOf(loop)), null);
            }

            CTTcPr tcPr = source.getCTTc().getTcPr();
            CTVMerge vMerge = tcPr.getVMerge();
            if (Objects.nonNull(vMerge)) {
                CTVMerge ctvMerge = target.getCTTc().getTcPr().getVMerge();
                ctvMerge.setVal(STMerge.CONTINUE);
            }
        }
        return targetRow;
    }

    default int count(String string, String subString) {
        int count = 0;
        while (string.contains(subString)) {
            count++;
            int index = string.indexOf(subString);
            //将长的那个字符串做截取
            string = string.substring(index + 1);
        }
        return count;
    }
}
