/*
 *
 */
package com.cutefool.commons.ci.nexus;

import com.cutefool.commons.core.util.FilesUtils;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.http.Http;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Nexus 工具类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class Nexus {


    public static List<Component> components(NexusContext context) {
        Consumer<String> consumer = empty(context.getConsumer());

        String url = String.format(Constants.NEXUS_MAVEN_COMPONENTS_SEARCH_URL, context.getAddress(), context.getRepository(), context.getGroupId(), context.getVersion());

        try {
            NexusResponse response = Http.newInstance().basic(context.getUser(), context.getPassword())
                    .get(url, NexusResponse.class);
            return Objects.nonNull(response) ? response.getItems() : Collections.emptyList();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            consumer.accept(e.toString());
            return Collections.emptyList();
        }
    }


    public static boolean delete(NexusContext context) {
        List<Component> components = components(context);
        if (Lists.isEmpty(components)) {
            return true;
        }

        AtomicReference<Boolean> reference = new AtomicReference<>(true);
        Consumer<String> consumer = empty(context.getConsumer());
        try {
            for (Component component : components) {
                String delete = String.format(Constants.NEXUS_MAVEN_COMPONENTS_DELETE_URL, context.getAddress(), component.getId());
                consumer.accept(String.format(Messages.NEXUS_EXECUTING, " delete " + component.getGroup() + ":" + component.getName() + " \r\n"));

                String response = Http.newInstance().basic(context.getUser(), context.getPassword()).delete(delete);
                consumer.accept(String.format(Messages.NEXUS_RESPONSE, JsonUtils.toJson(response) + "\r\n"));
                reference.set(reference.get() && Objects.nonNull(response));
            }

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            consumer.accept(e.toString());
            return false;
        }
        return reference.get();
    }

    public static boolean upload(NexusContext context, File file) {
        Consumer<String> consumer = empty(context.getConsumer());
        String create = String.format(Constants.NEXUS_MAVEN_COMPONENTS_CREATE_URL, context.getAddress(), context.getRepository());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<FileSystemResource> entity = new HttpEntity<>(new FileSystemResource(file), headers);
        Map<String, Object> parts = new LinkedHashMap<>();
        parts.put("maven2.groupId", context.getGroupId());
        parts.put("maven2.artifactId", context.getArtifactId());
        parts.put("maven2.version", context.getVersion());
        parts.put("maven2.asset1", file);
        parts.put("maven2.asset1.extension", FilesUtils.suffix(file));

        try {
            String upload = Http.newInstance().basic(context.getUser(), context.getPassword())
                    .upload(create, parts, String.class);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            consumer.accept(e.toString());
            return false;
        }
    }

    public static Consumer<String> empty(Consumer<String> consumer) {
        return Objects.isNull(consumer) ? (s) -> {
        } : consumer;
    }
//
//    public static void main(String[] args) {
//
//
//        NexusContext build = NexusContext.builder().groupId("com.cutefool.fe")
//                .artifactId("frontends-admin")
//                .version("0.5.0")
//                .address("http://xxxx")
//                .repository("tsingyun-java-release")
//                .user("admin")
//                .password("Tsingyun_devops")
//                .consumer(System.out::println)
//                .build();
//
//        boolean delete = Nexus.delete(build);
//        boolean upload = Nexus.upload(build, new File("/Users/liuyukuai/Downloads/yezi.zip"));
//    }
}
