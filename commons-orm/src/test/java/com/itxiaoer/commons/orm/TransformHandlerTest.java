package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.core.Exclude;
import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.core.Transform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 测试QueryHandler
 *
 * @author : liuyk
 */
public class TransformHandlerTest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQuery {
        private String name;
        private String id;
        @Exclude
        private String exclude;
        private String transform;
        @Transform("transform_1")
        private String transform1;
        @Transform(value = "transform_2", operator = Operator.IN)
        private String transform2;
        private String transform3;

        @Transform(ignoreEmpty = false)
        private String transform4;
        private String transform5;
    }


    private UserQuery nullable;
    private UserQuery empty;
    private UserQuery userQuery;


    @Before
    public void before() {

        this.nullable = null;
        // 空对象
        this.empty = new UserQuery();

        // 部分属性
        this.userQuery = UserQuery.builder().exclude("exclude").id("id")
                .name("name").transform("transform")
                .transform1("transform1").transform2("transform2")
                .transform3("transform3").transform4("transform4").build();


    }

    @Test
    public void transform() {

        // null
        Map<String, Transformation> fields = TransformHandler.fields(this.nullable);
        Assert.assertEquals(fields.size(), 0);

        // empty
        fields = TransformHandler.fields(this.empty);
        Assert.assertEquals(fields.size(), 8);

        // userQuery
        fields = TransformHandler.fields(this.userQuery);
        Assert.assertEquals(fields.size(), 8);

        // 判断属性的值
        Assert.assertEquals(new Transformation(new String[]{"id"}, "id", Operator.EQ, Operator.OR, true).toString(), fields.get("id").toString());
        Assert.assertEquals(new Transformation(new String[]{"name"}, "name", Operator.EQ, Operator.OR, true).toString(), fields.get("name").toString());
        Assert.assertEquals(new Transformation(new String[]{"transform"}, "transform", Operator.EQ, Operator.OR, true).toString(), fields.get("transform").toString());
        Assert.assertEquals(new Transformation(new String[]{"transform_1"}, "transform1", Operator.EQ, Operator.OR, true).toString(), fields.get("transform1").toString());
        Assert.assertEquals(new Transformation(new String[]{"transform_2"}, "transform2", Operator.IN, Operator.OR, true).toString(), fields.get("transform2").toString());
        Assert.assertEquals(new Transformation(new String[]{"transform3"}, "transform3", Operator.EQ, Operator.OR, true).toString(), fields.get("transform3").toString());
        Assert.assertEquals(new Transformation(new String[]{"transform4"}, "transform4", Operator.EQ, Operator.OR, false).toString(), fields.get("transform4").toString());
    }


}
