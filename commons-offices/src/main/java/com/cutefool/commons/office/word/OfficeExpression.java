/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Expressions;
import com.cutefool.commons.office.*;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:10 PM
 */
@SuppressWarnings("unused")
public interface OfficeExpression {

    /**
     * 解析表格
     *
     * @param rowIndex   rowIndex
     * @param row        row
     * @param cellIndex  cellIndex
     * @param cell       cell
     * @param context    context
     * @param strategies strategies
     */
    void parse(AtomicInteger rowIndex, XWPFTableRow row, int cellIndex, XWPFTableCell cell, WordContext context, List<OfficesStrategy> strategies);

    /**
     * 解析表格
     *
     * @param table      table
     * @param rows       rows
     * @param strategies strategies
     */
    default void doStrategy(XWPFTable table, List<XWPFTableRow> rows, List<OfficesStrategy> strategies) {
        Lists.empty(strategies).forEach(e -> e.execute(table, rows));
    }

    /**
     * 解析表格
     *
     * @param rowIndex rowIndex
     * @param row      row
     * @param cells    cells
     * @param context  context
     */
    static void doTables(AtomicInteger rowIndex, XWPFTableRow row, List<XWPFTableCell> cells, WordContext context) {
        for (int i = 0; i < cells.size(); i++) {
            XWPFTableCell cell = cells.get(i);
            String text = cell.getText();

            // 动态列，后边列取值
            if (text.contains(OfficeConstants.MULTIPLE_COLS_PREFIX) && text.contains(OfficeConstants.TABLE_SUFFIX)) {
                new WordMultColsExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text));
            }
            // 动态列，后边列取值
            else if (text.contains(OfficeConstants.MULTIPLE_ROWS_PREFIX) && text.contains(OfficeConstants.TABLE_SUFFIX)) {
                // 获取站位符
                List<OfficesStrategy> officesStrategies = context.getStrategies().get(text);
                if (Lists.isEmpty(officesStrategies)) {
                    if (text.contains(OfficeConstants.INDEX_PREFIX)) {
                        int index = text.indexOf(OfficeConstants.TABLE_SUFFIX);
                        int last = text.indexOf(OfficeConstants.TABLE_SUFFIX, index + 1);
                        String substring = text.substring(0, last + 1);
                        officesStrategies = context.getStrategies().get(substring);
                    }
                }
                new WordMultRowsExpression().parse(rowIndex, row, i, cell, context, officesStrategies);
            }

            // 动态列，后边列取值
            else if (text.contains(OfficeConstants.TABLE_IMAGE_PREFIX) && text.contains(Expressions.SUFFIX)) {
                new WordImgExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text));
            }

            // 动态列，后边列取值
            else if (text.contains(OfficeConstants.TABLE_IMAGE_LIST_PREFIX) && text.contains(OfficeConstants.TABLE_SUFFIX)) {
                new WordImgExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text));
            }

            // 直接取值
            else if (text.contains(Expressions.PREFIX) && text.contains(Expressions.SUFFIX)) {
                new WordSimpleExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text));
            }

            // 普通cell取值
            else if (!text.contains(OfficeConstants.MULTIPLE_ROWS_PREFIX)
                    && !text.contains(OfficeConstants.MULTIPLE_COLS_PREFIX)
                    && text.contains(OfficeConstants.TABLE_PREFIX)
                    && text.contains(OfficeConstants.TABLE_SUFFIX)) {
                new WordCellExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text));
            }
        }

    }


    /**
     * 解析表格
     *
     * @param rowIndex rowIndex
     * @param row      row
     * @param cells    cells
     * @param context  context
     */
    static void doRemoveTables(AtomicInteger rowIndex, XWPFTableRow row, List<XWPFTableCell> cells, WordContext context) {
        AtomicBoolean isRemove = new AtomicBoolean(false);
        int size = cells.size();

        for (int i = 0; i < size; i++) {
            XWPFTableCell cell = cells.get(i);
            String text = cell.getText();


            // 移除
            if (text.contains(OfficeConstants.TABLE_REMOVE_PREFIX)) {
                new WordRemoveExpression().parse(rowIndex, row, i, cell, context, context.getStrategies().get(text), isRemove);
            }


            Map<String, List<OfficesCopyStrategy>> copyStrategies = context.getCopyStrategies();
            if (Maps.iterable(copyStrategies)) {
                List<OfficesCopyStrategy> officesCopyStrategies = copyStrategies.get(text);
                // 如果没有则通过表达式查找

                if (Lists.isEmpty(officesCopyStrategies)) {

                    officesCopyStrategies = copyStrategies.keySet()
                            .stream()
                            .filter(e -> {

                                try {
                                    return Pattern.matches(e, text);
                                } catch (Exception ex) {
                                    return false;
                                }
                            })
                            .findAny()
                            .map(copyStrategies::get)
                            .orElse(Collections.emptyList());
                }
                System.out.println("text = " + text + " index = " + rowIndex.get() + " d = " + officesCopyStrategies);
                Lists.empty(officesCopyStrategies)
                        .forEach(e -> e.execute(row.getTable(), row, rowIndex, text, context));
            }
        }

        if (!isRemove.get()) {
            rowIndex.incrementAndGet();
        }

    }

    /**
     * 解析表达式
     *
     * @param s     表单
     * @param begin begin
     * @param end   end
     * @return expression
     */
    static String doExpression(String s, String begin, String end) {
        s = s.replace(OfficeConstants.TABLE_IMAGE_LIST_PREFIX, "{");
        s = s.replace(OfficeConstants.TABLE_IMAGE_PREFIX, "{");
        s = s.replace(OfficeConstants.MULTIPLE_ROWS_PREFIX, "{");
        StringBuffer sb = new StringBuffer(s);
        char[] chars = s.toCharArray();

        List<Symbol> symbols = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];

            if (Objects.equals(String.valueOf(aChar), begin)) {
                symbols.add(new Symbol(true, false, begin, i));
            }

            if (Objects.equals(String.valueOf(aChar), end)) {
                symbols.add(new Symbol(false, true, end, i));
            }
        }

        Boolean isLeft = null;

        for (Symbol symbol : symbols) {

            if (Objects.isNull(isLeft) && symbol.isRight()) {
                int start = symbol.getIndex();
                sb.replace(start, start + 1, "}");
                isLeft = null;
                continue;
            }

            if (Objects.isNull(isLeft)) {
                isLeft = symbol.isLeft();
                continue;
            }

            if (isLeft && symbol.isRight()) {
                isLeft = null;
                continue;
            }

            if (!isLeft && symbol.isRight()) {
                int start = symbol.getIndex();
                sb.replace(start, start + 1, "}");
                isLeft = null;
            }
        }

        String exp = sb.toString();

        if (!exp.startsWith(Expressions.PREFIX) && !exp.contains(Expressions.PREFIX)) {
            exp = Expressions.PREFIX + exp;
        }

        if (!exp.endsWith(Expressions.SUFFIX) && !exp.contains(Expressions.SUFFIX)) {
            exp = exp + Expressions.SUFFIX;
        }
        return exp;
    }


    static List<OfficeExp> doExpression(String expression) {

        return Strings.split(expression, Expressions.SUFFIX)
                .stream()
                .map(e -> Strings.split(e, Expressions.PREFIX))
                .flatMap(List::stream)
                .map(e -> {
                    int index = expression.indexOf(e);
                    int first = index - 1;
                    int last = first + e.length() + 1;
                    if (first < 0 || last >= expression.length()) {
                        return new OfficeExp(false, e);
                    }
                    String first_ = String.valueOf(expression.charAt(first));
                    String last_ = String.valueOf(expression.charAt(last));
                    if (Objects.equals(Expressions.PREFIX, first_) && Objects.equals(Expressions.SUFFIX, last_)) {
                        return new OfficeExp(true, Expressions.PREFIX + e + Expressions.SUFFIX);
                    }
                    return new OfficeExp(false, e);
                }).collect(Collectors.toList());

    }


    public static void main(String[] args) {

//        System.out.println(Pattern.matches(".*names\\[_index].name}", "{records[_index].names[_index].name}"));

//        String s = "WEEEEE{s.d}    dfdf{s.c}11111";
//
//        List<OfficeExp> strings = doExpression("{signs[1].name}{signs[1].name}： {signs[1}.file}  {signs[1].value}");
//        strings.forEach(System.out::println);

//        int index = "[rows*signs[_index].text]".indexOf("]");
//        int index1 = "[rows*signs[_index].text]".indexOf("]", index + 1);
//
//        System.out.println("[rows*signs[_index].text]".substring(0, index1));
//
//        System.out.println("[rows*signs[_index].text]".indexOf("]", index + 1));


//        System.out.println(OfficeExpression.doExpression("[rows*signs[_index].name]{signs[_index].name}： {@signs[_index].file}  {signs[_index].value}", "[", "]"));
    }


}
