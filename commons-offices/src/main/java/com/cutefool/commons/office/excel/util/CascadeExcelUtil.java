/*
 *
 */
package com.cutefool.commons.office.excel.util;

import com.cutefool.commons.office.excel.pojo.ExcelWriteData;
import com.cutefool.commons.office.excel.pojo.ExcelRecursionData;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 级联 Excel 数据填充工具
 *
 * @author 271007729@qq.com
 * @date 2022/5/14 22:48
 */
@SuppressWarnings("unused")
public class CascadeExcelUtil {

    /**
     * 支持的最大级联层级数
     */
    public static int max_cascade = 100;


    /**
     * 业务数据转换成excel单元格数据
     *
     * @param dataList      业务数据
     * @param beginRowIndex 开始写数据的行下标【0表示第1行】
     */
    public static List<ExcelWriteData> toExcelCell(List<ExcelRecursionData> dataList, int beginRowIndex) {
        // 结果集：所有单元格数据集合
        List<ExcelWriteData> cellList = new ArrayList<>();
        if (isEmpty(dataList)) {
            return cellList;
        }
        // 数据预处理
        pretreatment(dataList);
        // 开始写数据的行下标【0表示第1行】
        Integer[] begin = new Integer[max_cascade + 1];
        // 结束写数据的行下标【0表示第1行】
        Integer[] end = new Integer[max_cascade + 1];
        // 填充默认值
        Arrays.fill(begin, beginRowIndex);
        Arrays.fill(end, beginRowIndex);
        // 数据转换
        toExcelCell(dataList, begin, end, cellList);

        return cellList;
    }


    /**
     * 数据转换
     *
     * @param dataList 业务数据
     * @param begin    填充数据开始行下标【0表示第1行】
     * @param end      填充数据结束行下标【0表示第1行】
     * @param cellList 结果集接收器
     */
    private static void toExcelCell(List<ExcelRecursionData> dataList, Integer[] begin, Integer[] end, List<ExcelWriteData> cellList) {
        if (isEmpty(dataList)) {
            return;
        }
        for (ExcelRecursionData data : dataList) {
            // 下级数据处理会修改 end 的值，这里只是给个初始值
            end[data.getLevel()] = begin[data.getLevel()];
            // 递归处理子集数据
            recursion(data, begin, end, cellList);
            // 处理自己的数据
            toCell(data, begin[data.getLevel()], end[data.getLevel()], cellList);
            // 本级下次开始行 = 本级这次结束行 + 1
            begin[data.getLevel()] = end[data.getLevel()] + 1;
        }
    }


    /**
     * 【私有内部方法】数据预处理，设置递归层级，统一最大列数，补充空值
     *
     * @param dataList 业务数据集合
     */
    private static void pretreatment(List<ExcelRecursionData> dataList) {
        // 数据递归层级从 1 开始
        int level = 1;
        Integer[] max = new Integer[max_cascade + 1];
        Integer[] begin = new Integer[max_cascade + 1];
        pretreatment(dataList, level, max, begin);
    }


    /**
     * 【私有内部方法】数据预处理，设置递归层级，统一最大列数，补充空值
     *
     * @param dataList 业务数据集合
     * @param level    所在递归层级
     * @param max      递归层级填充数据的列数
     * @param begin    递归层级填充数据开始的列号【0表示第1列】
     */
    private static void pretreatment(List<ExcelRecursionData> dataList, int level, Integer[] max, Integer[] begin) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (ExcelRecursionData data : dataList) {
            data.setLevel(level);
            Integer value = max[level];
            if (Objects.isNull(value)) {
                max[level] = data.getColumnNum();
                begin[level] = data.getBeginColumn();
            } else {
                data.setColumnNum(max[level]);
                data.setBeginColumn(begin[level]);
            }

            int maxColumn = data.getColumnNum();
            List<String> values = data.getValues();
            if (Objects.isNull(values)) {
                values = new ArrayList<>();
            }
            int size = values.size();
            if (size < maxColumn) {
                for (int i = size; i < maxColumn; i++) {
                    // 填充默认值，达到 size == maxColumn
                    values.add(null);
                }
            }
            // 递归处理子集的数据
            pretreatment(data.getChildren(), level + 1, max, begin);
        }
    }


    /**
     * 数据转换
     *
     * @param data     业务数据
     * @param begin    填充数据开始行下标【0表示第1行】
     * @param end      填充数据结束行下标【0表示第1行】
     * @param cellList 结果集接收器
     */
    private static void recursion(ExcelRecursionData data, Integer[] begin, Integer[] end, List<ExcelWriteData> cellList) {
        List<ExcelRecursionData> children = data.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        // 初始化开始行下标，等于上级的结束行【结束行实时更新，不是真实的结束】
        begin[data.getLevel() + 1] = end[data.getLevel()];
        toExcelCell(children, begin, end, cellList);
        // 更新上级本次结束行 = 本级下次开始行 - 1
        end[data.getLevel()] = begin[data.getLevel() + 1] - 1;
    }


    /**
     * 数据转换
     *
     * @param data     业务数据
     * @param rowBegin 填充数据开始行下标【0表示第1行】
     * @param rowEnd   填充数据结束行下标【0表示第1行】
     * @param cellList 结果集接收器
     */
    private static void toCell(ExcelRecursionData data, Integer rowBegin, Integer rowEnd, List<ExcelWriteData> cellList) {
        List<String> values = data.getValues();
        int size = values.size();
        for (int i = 0; i < size; i++) {
            ExcelWriteData cell = ExcelWriteUtil.getAndAddCell(cellList, data.getType());
            cell.setRowBegin(rowBegin);
            cell.setRowEnd(rowEnd);
            cell.setColumn(data.getBeginColumn() + i);
            cell.setCellValue(values.get(i));
        }
    }


    /**
     * 【私有内部方法】集合判空，减少外部依赖
     *
     * @param collection 集合
     * @return true：为空
     */
    private static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
