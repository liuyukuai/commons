package com.cutefool.commons.orm.mybatis.extensible.interceptor;

import com.cutefool.commons.orm.Extensible;
import com.cutefool.commons.orm.ExtensibleOperations;
import com.github.pagehelper.PageInterceptor;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.spring.SpiSpringContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class MybatisExtensibleInterceptor extends PageInterceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        this.initMappedStatement(statement);
        return super.intercept(invocation);
    }

    private void initMappedStatement(MappedStatement statement) throws Exception {

        String id = statement.getId();
        String clz = id.substring(0, id.lastIndexOf("."));

        Configuration configuration = statement.getConfiguration();
        Object oneSpi = SpiSpringContext.getOneSpi(Class.forName(clz));
        if (oneSpi instanceof ExtensibleOperations) {
            Map<String, String> extensibleMap = ((ExtensibleOperations) oneSpi).extensibleFields();
            Class<?> aClass = ((ExtensibleOperations) oneSpi).domainClass();
            if (Maps.iterable(extensibleMap) && Extensible.class.isAssignableFrom(aClass)) {
                // 获取所有resultMap
                List<ResultMap> resultMaps = Lists.empty(statement.getResultMaps())
                        .stream()
                        .map(e -> {
                            List<ResultMapping> resultMappings = e.getResultMappings();
                            List<ResultMapping> mappings = new ArrayList<>(resultMappings);
                            // 设置动态属性映射
                            List<ResultMapping> extensibleMapping = extensibleMap.entrySet()
                                    .stream()
                                    .map(c -> new ResultMapping.Builder(configuration, Extensible.FIELD_EXTENSIBLE + "." + c.getKey(), c.getValue(), String.class).build())
                                    .collect(Collectors.toList());
                            mappings.addAll(extensibleMapping);
                            return new ResultMap.Builder(configuration, e.getId(), e.getType(), mappings, e.getAutoMapping()).build();
                        }).collect(Collectors.toList());

                Field field = MappedStatement.class.getDeclaredField("resultMaps");
                field.setAccessible(true);
                field.set(statement, resultMaps);
            }
        }
    }
}
