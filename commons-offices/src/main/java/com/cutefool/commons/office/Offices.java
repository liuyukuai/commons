/*
 *
 */
package com.cutefool.commons.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.cutefool.commons.expression.Expressions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * word操作类
 *
 * @author 271007729@qq.com
 * @date 2021/7/29 10:50 PM
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class Offices {


    public static OfficesContext newInstance(File dest) {
        return newInstance(null, dest);
    }

    public static OfficesContext newInstance(File source, File dest) {
        return OfficesContext.of(source, dest);
    }

    public static void main(String[] args) {
        Fire fire = new Fire();

        fire.setCode("sy001");
        fire.setWorkType("一级动火作业");
        fire.setProposerDepartment("清云智通");

        GasItem item = new GasItem();

        item.setAnalyst("2222");
        item.setLocation("北京");
        item.setResult("合格");
        item.setTime("202001");

        GasItem item2 = new GasItem();

        item2.setAnalyst("3333");
        item2.setLocation("上海");
        item2.setResult("合格");
        item2.setTime("202002");

        item.setGasValues(Arrays.asList("200", "300"));

        item2.setGasValues(Arrays.asList("500", "600"));

        fire.setGas(Arrays.asList(item, item2));
        fire.setGasItems(Arrays.asList("有毒", "有害"));

        Message m1 = new Message();
        m1.setName("消息1");
        m1.setValue("value1");
        Message m2 = new Message();

        m2.setName("消息2");
        m2.setValue("value2");


        Message m3 = new Message();
        m3.setName("其他安全措施1");
        m3.setValue("v1");
        Message m4 = new Message();

        m4.setName("其他安全措施2");
        m4.setValue("v2");

        Sign sign = new Sign();

        sign.setFile(new OfficeFile("hello", new File("/Users/liuyukuai/Documents/xx.png"), 25, 12, XWPFDocument.PICTURE_TYPE_PNG, false));
        sign.setName("签名：");
        sign.setValue("生成运营部1");
        sign.setText(new OfficeBreak(true, "我是text1111"));

        Sign sign2 = new Sign();

        sign2.setFile(new OfficeFile("hello2", new File("/Users/liuyukuai/Documents/xx.png"), 25, 12, XWPFDocument.PICTURE_TYPE_PNG, false));
        sign2.setName("签名：");
        sign2.setValue("生成运营部2");
        sign2.setText(new OfficeBreak(true, "我是text"));


        fire.setMessages(Arrays.asList(m1, m2));
        fire.setMessages2(Arrays.asList(m3, m4));
        fire.setMessages3(Arrays.asList(m1, m2));


        fire.setSigns(Arrays.asList(sign, sign2));


        String s = Expressions.parseString(fire, "{messages[0].name}");

        System.out.println(s);


        Record record = new Record();

        Msg msg1 = new Msg("维修记录1", Arrays.asList(new Name("1", "张三"), new Name("2", "李四")));
        Msg msg2 = new Msg("维修记录2", Arrays.asList(new Name("1", "张三"), new Name("2", "李四")));
        record.setRecords(Arrays.asList(msg1, msg2));

        Offices.newInstance(new File("/Users/liuyukuai/Documents/blank.docx"), new File("/Users/liuyukuai/Documents/temp.docx"))
                .bind("{records[_index].msg}", () -> 13)
                .bind(".*names\\[_index].name}", () -> 3)
                .word()
                .write(record);
    }


    @Data
    public static class Record implements Office {

        List<Msg> records;

    }

    @Data
    @AllArgsConstructor
    public static class Msg implements Office {

        String msg;

        List<Name> names;

    }

    @Data
    @AllArgsConstructor
    public static class Name implements Office {

        String id;

        private String name;

    }


    @lombok.Data
    public static class Fire implements Office {

        /**
         * 作业编号
         */
        private String code;

        /**
         * 作业名称
         */
        private String name;

        private OfficeFile dest = new OfficeFile("hello", new File("/Users/liuyukuai/Documents/xx.png"), 25, 12, XWPFDocument.PICTURE_TYPE_PNG, false);

        /**
         * 作业类型名称
         */
        private String workType;

        /**
         * 申请人名称
         */
        private String proposerName;

        /**
         * 申请人部门名称
         */
        private String proposerDepartment;

        /**
         * 作业票名称
         */
        private String ticketName;

        /**
         * 动火方式
         */
        private String fireWays;

        /**
         * 作业部门
         */
        private String department;

        /**
         * 作业地点
         */
        private String place;

        /**
         * 最后审批时间
         */
        private String lastApproveTime;

        /**
         * 计划结束时间
         */
        private String planEndTime;

        /**
         * 实际开始作业时间
         */
        private String realStartTime;

        /**
         * 实际完成作业时间
         */
        private String realEndTime;

        /**
         * 作业负责人
         */
        private String leader;

        /**
         * 特殊作业负责人
         */
        private String specialWorkers;

        /**
         * 一般作业人
         */
        private String normalWorkers;

        /**
         * 气体检测
         */
        private List<GasItem> gas;

        /**
         * 气体检测类型
         */
        private List<String> gasItems;

        /**
         * 消息
         */
        private List<Message> messages;

        /**
         * 消息
         */
        private List<Message> messages2;

        /**
         * 消息
         */
        private List<Message> messages3;

        /**
         * 消息
         */
        private List<Sign> signs;


        private List<OfficeFile> files = Arrays.asList(
                new OfficeFile("one", new File("/Users/liuyukuai/Documents/xx.png"), 25, 12, XWPFDocument.PICTURE_TYPE_PNG, false),
                new OfficeFile("two", new File("/Users/liuyukuai/Documents/xx.png"), 25, 12, XWPFDocument.PICTURE_TYPE_PNG, false)
        );


    }

    @Data
    public static class GasItem {
        /**
         * 检测时间
         */
        private String time;

        /**
         * 检测位置
         */
        private String location;

        /**
         * 气体检测名称
         */
        private List<String> gasValues;

        /**
         * 是否合格
         */
        private String result;

        /**
         * 分析人名称
         */
        private String analyst;


    }


    @Data
    public static class Message {

        private String name;

        private String value;
    }

    @Data
    public static class GasValue {
        private String name;

        private String value;
    }

    @Data
    public static class Sign {

        private OfficeBreak text;

        private String name;

        private String value;

        private OfficeFile file;


    }

}
