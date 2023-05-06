/*
 *  
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeBreak;
import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.office.OfficeExp;
import com.cutefool.commons.office.OfficeFile;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:04 PM
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
public class WordTables {

    public static void setCell(XWPFTableCell cell, Object value, String expressions) {

        XWPFParagraph newPara = new XWPFParagraph(cell.getCTTc().addNewP(), cell);
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        CTRPr cellPr = null;
        if (Lists.iterable(paragraphs)) {
            CTPPr pPr = paragraphs.get(0).getCTP().getPPr();
            newPara.getCTP().setPPr(pPr);

            List<XWPFRun> runs = paragraphs.get(0).getRuns();

            if (Lists.iterable(runs)) {
                cellPr = runs.get(0).getCTR().getRPr();
            }
        }

        if (value instanceof String) {
            setCell(newPara, cellPr, value, null);
        } else if (value instanceof OfficeFile) {
            OfficeFile file = (OfficeFile) value;
            setCell(newPara, cellPr, file, file.isHasBreak() ? XWPFRun::addBreak : null);
        } else if (value instanceof OfficeBreak) {
            OfficeBreak officeBreak = (OfficeBreak) value;
            setCell(newPara, cellPr, officeBreak.getText(), officeBreak.isBreaks() ? XWPFRun::addBreak : null);
        } else {
            try {
                if (StringUtils.isNotBlank(expressions)) {
                    log.info(expressions);
                    value = Expressions.parseObject(value, expressions);
                }
                setCell(newPara, cellPr, value, null);
            } catch (Throwable e) {
                log.warn(e.getMessage(), e);

                // 如果解析错误，多个表达式，或者不能转换成字符串
                // 重新解析表达式
                List<OfficeExp> officeExps = OfficeExpression.doExpression(expressions);
                for (OfficeExp officeExp : officeExps) {
                    boolean expression = officeExp.isExpression();
                    if (expression) {
                        try {
                            Object o = Expressions.parseObject(value, officeExp.getText());
                            setCell(newPara, cellPr, o, null);
                        } catch (Exception ex) {
                            log.warn(e.getMessage(), e);
                            setCell(newPara, cellPr, "", null);
                        }

                    } else {
                        setCell(newPara, cellPr, officeExp.getText(), null);
                    }
                }
            }
        }

        if (Lists.iterable(paragraphs)) {
            int size = paragraphs.size();
//            // 删除段落
            for (int pos = 0; pos < size; pos++) {
                cell.removeParagraph(0);
            }
        }
        cell.addParagraph(newPara);
    }


    public static void setCell(XWPFParagraph newPara, CTRPr cellPr, Object value, Consumer<XWPFRun> consumer) {
        if (Objects.isNull(value)) {
            return;
        }
        if (value instanceof String) {
            setCell(newPara, cellPr, (String) value, consumer);
        } else if (value instanceof OfficeFile) {
            OfficeFile file = (OfficeFile) value;
            setCell(newPara, cellPr, file, file.isHasBreak() ? XWPFRun::addBreak : null);
        } else if (value instanceof OfficeBreak) {
            OfficeBreak officeBreak = (OfficeBreak) value;
            setCell(newPara, cellPr, officeBreak.getText(), officeBreak.isBreaks() ? XWPFRun::addBreak : null);
        } else if (value instanceof Collection) {
            setCell(newPara, cellPr, (Collection) value);
        } else {
            XWPFRun run = newPara.createRun();
            run.setText(String.valueOf(value));
        }
    }


    public static void setCell(XWPFParagraph newPara, CTRPr cellPr, String value, Consumer<XWPFRun> consumer) {
        if (Objects.isNull(value)) {
            return;
        }
        XWPFRun run = newPara.createRun();
        if (Objects.nonNull(cellPr)) {
            run.getCTR().setRPr(cellPr);
        }

        run.setText(value);
        if (Objects.nonNull(consumer)) {
            consumer.accept(run);
        }

    }

    public static void setCell(XWPFParagraph newPara, CTRPr cellPr, OfficeFile value, Consumer<XWPFRun> consumer) {
        if (Objects.isNull(value)) {
            return;
        }
        XWPFRun run = newPara.createRun();
        try {
            if (Objects.nonNull(cellPr)) {
                run.getCTR().setRPr(cellPr);
            }
            run.setText(value.getName());
            File file = value.getFile();
            run.addPicture(new FileInputStream(file), value.getDocumentType(), "", Units.toEMU(value.getWidth()), Units.toEMU(value.getHeight()));
            if (Objects.nonNull(consumer)) {
                consumer.accept(run);
            }
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCell(XWPFParagraph newPara, CTRPr cellPr, Collection collections) {
        if (Objects.isNull(collections) || collections.isEmpty()) {
            return;
        }
        AtomicLong counter = new AtomicLong(0);
        for (Object o : collections) {
            if (counter.get() < collections.size() - 1) {
                setCell(newPara, cellPr, o, XWPFRun::addBreak);
            } else {
                setCell(newPara, cellPr, o, null);
            }
            counter.incrementAndGet();
        }
    }


    public static XWPFTableRow insertRow(XWPFTable table, XWPFTableRow copyRow, int destIndex, int cellIndex, int loop, Object value, String expressions, Object firstValue) {
        // 在表格中指定的位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(destIndex);
        //复制行对象
        targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());
        //或许需要复制的行的列
        List<XWPFTableCell> copyCells = copyRow.getTableCells();
        //复制列对象
        XWPFTableCell target;
        for (int i = 0; i < copyCells.size(); i++) {
            XWPFTableCell source = copyCells.get(i);

            target = targetRow.addNewTableCell();

            // 列属性
            target.getCTTc().setTcPr(source.getCTTc().getTcPr());
            // 删除段落
            for (int pos = 0; pos < target.getParagraphs().size(); pos++) {
                target.removeParagraph(pos);
            }
            // 添加段落
            for (XWPFParagraph sp : source.getParagraphs()) {
                XWPFParagraph targetP = target.addParagraph();
                copyParagraph(targetP, sp);
            }

//            if (Objects.equals(OfficeConstants.INDEX_PREFIX, source.getText().trim())) {
//                WordTables.setCell(source, String.valueOf(loop), "");
//            }

            if (Objects.equals(OfficeConstants.INDEX_PREFIX, target.getText().trim())) {
                WordTables.setCell(target, String.valueOf(loop + 1), "");
            }

            if (i == cellIndex) {
                if (expressions.contains(Strings.POINT)) {
                    expressions = expressions.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(loop));
                    WordTables.setCell(target, value, expressions);
                } else {
                    WordTables.setCell(target, firstValue, expressions);
                }
            } else {
                //如果包含_index
                String text = target.getText();
                if (StringUtils.isNotBlank(text)) {
                    WordTables.setCell(target, text.replace(OfficeConstants.INDEX_PREFIX, String.valueOf(loop)), expressions);
                }
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

    public static void copyParagraph(XWPFParagraph target, XWPFParagraph source) {

        // 设置段落样式
        target.getCTP().setPPr(source.getCTP().getPPr());

        // 移除所有的run
        for (int pos = target.getRuns().size() - 1; pos >= 0; pos--) {
            target.removeRun(pos);
        }

        // copy 新的run
        for (XWPFRun s : source.getRuns()) {
            XWPFRun targetRun = target.createRun();
            copyRun(targetRun, s);
        }
    }

    public static void copyRun(XWPFRun target, XWPFRun source) {
        CTRPr rPr = source.getCTR().getRPr();
        target.getCTR().setRPr(rPr);
        // 设置文本
        target.setText(source.text());
        target.setFontFamily(source.getFontFamily());
    }


    public static void copyCTR(XWPFTableCell source, XWPFTableCell target) {
        if (source == target) {
            return;
        }

        CTTc cttc = source.getCTTc();
        List<CTP> pList = cttc.getPList();
        if (Lists.iterable(pList)) {
            CTPPr pPr = pList.get(0).getPPr();
            if (Objects.nonNull(pPr) && Objects.nonNull(pPr.getJc())) {
                STJc.Enum val = pPr.getJc().getVal();
                // 拷贝对齐方式
                target.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(val);
                List<XWPFParagraph> sourceParagraphs = source.getParagraphs();
                List<XWPFParagraph> targetParagraphs = target.getParagraphs();
                if (Lists.iterable(sourceParagraphs) && Lists.iterable(targetParagraphs)) {
                    List<XWPFRun> sourceRuns = sourceParagraphs.get(0).getRuns();
                    List<XWPFRun> targetRuns = targetParagraphs.get(0).getRuns();
                    if (Lists.iterable(sourceRuns) && Lists.iterable(targetRuns)) {
                        CTRPr rPr = sourceRuns.get(0).getCTR().getRPr();
                        targetRuns.forEach(e -> e.getCTR().setRPr(rPr));
                    }
                }
            }
        }
    }

    public static void doIndex(XWPFTableRow row) {
        //或许需要复制的行的列
        List<XWPFTableCell> copyCells = row.getTableCells();
        for (XWPFTableCell source : copyCells) {
            if (Objects.equals(OfficeConstants.INDEX_PREFIX, source.getText().trim())) {
                WordTables.setCell(source, String.valueOf(1), "");
            }
        }
    }

}
