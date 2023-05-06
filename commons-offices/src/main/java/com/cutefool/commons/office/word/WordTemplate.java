/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.OfficeConstants;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.Office;
import com.cutefool.commons.office.OfficesContext;
import com.cutefool.commons.office.OfficesTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 271007729@qq.com
 * @date 7/30/21 10:52 AM
 */
@SuppressWarnings("unused")
public class WordTemplate implements OfficesTemplate<XWPFDocument> {


    private WordContext context;

    public WordTemplate(OfficesContext context) {
        this.context = new WordContext(context.getSource(), context.getDest());
        this.context.setStrategies(context.getStrategies());
        this.context.setCopyStrategies(context.getCopyStrategies());
        ProcessUtils.processObject(this.context, context);
    }

    public boolean write(Office office) {
        context.setData(office);
        try (XWPFDocument document = this.open()) {
            // 替换段落
            this.replace(document, office);
            // 处理表格
            this.doTables(document, office);
            // 处理表格
            this.doWrite(document);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public XWPFDocument open() throws IOException {
        File source = context.getSource();

        if (Objects.isNull(source)) {
            return new XWPFDocument();
        }

        try (FileInputStream is = new FileInputStream(source)) {
            return new XWPFDocument(is);
        }
    }

    @Override
    public void doWrite(XWPFDocument document) throws IOException {
        File dest = context.getDest();

        try (FileOutputStream is = new FileOutputStream(dest)) {
            document.write(is);
        }

    }

    private void replace(XWPFDocument document, Office office) {
        if (Objects.isNull(office)) {
            return;
        }
        // 替换段落
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        Lists.empty(paragraphs)
                .stream()
                // 注意手写时候分段不对
                .map(e -> this.doRuns(e, e.getRuns()))
                .flatMap(List::stream)
                .forEach(e -> {
                    String text = e.getText(e.getTextPosition());
                    if (StringUtils.isNotBlank(text)) {
                        String parse = Expressions.parseString(office, text);
                        e.setText(parse, 0);
                    }
                });
    }


    private List<XWPFRun> doRuns(XWPFParagraph paragraph, List<XWPFRun> runs) {
        List<XWPFRun> newRuns = new ArrayList<>();
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String text = run.getText(run.getTextPosition());
            boolean prefix = OfficeConstants.isPrefix(text);
            boolean suffix = OfficeConstants.isSuffix(text);
            // 如果都包含
            if (prefix && suffix) {
                newRuns.add(run);
                continue;
            }

            // 如果都不包含
            if (!prefix && !suffix) {
                newRuns.add(run);
                continue;
            }

            // 如果只包含其中一个
            if (prefix) {

                List<Integer> removePos = new ArrayList<>();
                // 遍历
                for (int j = i + 1; j < runs.size(); j++) {
                    XWPFRun suffixRun = runs.get(j);
                    String suffixText = suffixRun.getText(suffixRun.getTextPosition());
                    boolean isSuffix = OfficeConstants.isSuffix(suffixText);
                    run.setText(text + suffixText, 0);
                    text = run.getText(0);
                    removePos.add(j);
                    if (isSuffix) {
                        break;
                    }
                }
                newRuns.add(run);
                if (Lists.iterable(removePos)) {
                    Integer integer = removePos.get(0);
                    removePos.forEach(e -> paragraph.removeRun(integer));
                }
            }
        }

        return newRuns;
    }

    private void doTables(XWPFDocument document, Office office) {
        List<XWPFTable> tables = document.getTables();
        if (Objects.isNull(office)) {
            return;
        }

        AtomicInteger rowsCounter = new AtomicInteger(0);

        // 先处理需要移除的
        Lists.empty(tables)
                .stream()
                .map(XWPFTable::getRows)
                .flatMap(List::stream)
                .iterator()
                .forEachRemaining(e -> {
                    try {
                        OfficeExpression.doRemoveTables(rowsCounter, e, e.getTableCells(), this.context);
                    } catch (Exception ignore) {
                    }
                });

        rowsCounter.set(0);
        Lists.empty(tables)
                .stream()
                .map(XWPFTable::getRows)
                .flatMap(List::stream)
                .iterator()
                .forEachRemaining(e -> {
                    OfficeExpression.doTables(rowsCounter, e, e.getTableCells(), this.context);
                    rowsCounter.getAndIncrement();
                });

    }
}
