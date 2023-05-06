/*
 *
 */
package com.cutefool.commons.expression;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Maps;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author 271007729@qq.com
 * @date 7/21/21 12:00 PM
 */
@Slf4j
@SuppressWarnings({"all"})
public class Expressions {

    /**
     * 默认前缀
     */
    public static final String PREFIX = "{";

    /**
     * 默认后缀
     */
    public static final String SUFFIX = "}";

    public static <T> String parseString(Object v, String s) {
        return parse(v, s, PREFIX, SUFFIX, String.class);
    }

    public static <T> String parseString(Map<String, Object> v, String s) {
        return parse(v, s, PREFIX, SUFFIX, String.class);
    }

    public static Object parseObject(Object v, String s) {
        return parse(v, s, PREFIX, SUFFIX);
    }

    public static Object parseObject(Map<String, Object> v, String s) {
        return parse(v, s, PREFIX, SUFFIX);
    }

    public static <T> T parse(Object v, String s, Class<T> cls) {
        return parse(v, s, PREFIX, SUFFIX, cls);
    }

    public static <T> T parse(Map<String, Object> v, String s, Class<T> cls) {
        return parse(v, s, PREFIX, SUFFIX, cls);
    }

    public static <T> T parse(Object v, String s, String prefix, String suffix, Class<T> cls) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        TemplateParserContext templateParserContext = new TemplateParserContext(prefix, suffix);
        Expression expression = expressionParser.parseExpression(s, templateParserContext);
        return expression.getValue(v, cls);
    }

    public static Object parse(Object v, String s, String prefix, String suffix) {
        log.info(" s = {}, prefix = {}, suffix = {}", s, prefix, suffix);
        ExpressionParser expressionParser = new SpelExpressionParser();
        TemplateParserContext templateParserContext = new TemplateParserContext(prefix, suffix);
        Expression expression = expressionParser.parseExpression(s, templateParserContext);
        return expression.getValue(v);

    }

    public static Object parse(Map<String, Object> v, String s, String prefix, String suffix) {
        StandardEvaluationContext context = new StandardEvaluationContext(v);
        context.addPropertyAccessor(new MapAccessor());
        ParserContext parserContext = new ParserContext() {

            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return prefix;
            }

            @Override
            public String getExpressionSuffix() {
                return suffix;
            }
        };
        Expression expression = new SpelExpressionParser().parseExpression(s, parserContext);
        return expression.getValue(context);
    }

    public static <T> T parse(Map<String, Object> v, String s, String prefix, String suffix, Class<T> cls) {
        StandardEvaluationContext context = new StandardEvaluationContext(v);
        context.addPropertyAccessor(new MapAccessor());
        ParserContext parserContext = new ParserContext() {

            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return prefix;
            }

            @Override
            public String getExpressionSuffix() {
                return suffix;
            }
        };
        Expression expression = new SpelExpressionParser().parseExpression(s, parserContext);
        return expression.getValue(context, cls);
    }

    public static void main(String[] args) {

        Map<String, Object> maps = Maps.linkedHashMap();
        maps.put("x", "是");
        maps.put("v", "5");

        String s = Expressions.parseString(maps, "{x=='是'?v:0}");
        System.out.println(s);

        // x<95
        // 95<<x<100 y0
        // x>100
        //

//        String s2 = "${x<95?0:(95<<x<100 ?(x-95)/0.05*Y0:100)}";
        String s2 = "{x<95?0:(95<=x && x<100 ?((x-95)/5.0*Y):100)}";

        // case1 x<95  v;
        // case2 95<=x<100

        Map<String, Object> maps2 = Maps.linkedHashMap();
        maps2.put("x", 98);
        maps2.put("Y", 3);
        // "{95<=x && x<100}


        System.out.println(Expressions.parseString(maps2, s2));

    }

}
