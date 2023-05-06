/*
 *
 */
package com.cutefool.commons.ci.maven;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.Lists;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * maven的pom文件操作类
 *
 * @author 271007729@qq.com
 * @date 5/31/21 4:28 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Pom {

    public static Model load(File pomXml) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try (FileInputStream fis = new FileInputStream(pomXml)) {
            return reader.read(fis);
        } catch (Exception e) {
            throw new BizException(e, ResponseCode.DATA_NOT_EXISTS.getCode(), "home is error");
        }
    }

    private static void write(File pomXml, Model model) {
        MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
        try (FileWriter fileWriter = new FileWriter(pomXml)) {
            mavenXpp3Writer.write(fileWriter, model);
        } catch (Exception e) {
            throw new BizException(e, ResponseCode.DATA_NOT_EXISTS.getCode(), "home is error");
        }
    }

    public static void write(File pomXml, Function<Model, Model> function) {
        Model model = load(pomXml);
        if (Objects.nonNull(function)) {
            model = function.apply(model);
            write(pomXml, model);
        }
    }

    public static String version(File pomXml) {
        Model model = load(pomXml);
        return model.getVersion();
    }

    public static List<String> modules(File pomXml) {
        Model model = load(pomXml);
        List<String> modules = Lists.empty(model.getModules());
        List<String> children = Lists.empty(modules)
                .stream()
                .map(e -> {
                    Model load = load(new File(pomXml.getParent(), e + "/pom.xml"));
                    return load.getModules();

                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        modules.addAll(children);
        return modules;
    }


    public static void main(String[] args) {
        Model model = Pom.load(new File("/Users/liuyukuai/gits/commons/pom.xml"));
        List<String> modules = model.getModules();
        System.out.println(modules);


    }

}
