/*
 *
 */
package com.cutefool.commons.http;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.FilesUtils;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Numbers;
import com.cutefool.commons.http.progress.ProgressLibs;
import com.cutefool.commons.http.progress.ProgressListener;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 10/18/21 1:39 PM
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class Http {

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public static final RequestBody EMPTY = RequestBody.create(null, "");

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .hostnameVerifier((s, sslSession) -> true)
            .build();

    private final Request.Builder builder = new Request.Builder();

    public static Http newInstance() {
        return new Http();
    }

    public static Http newInstance(Consumer<Request.Builder> builderConsumer) {
        Http http = new Http();
        builderConsumer.accept(http.builder);
        return http;
    }

    public Http basic(String username, String password) {
        String credentials = Credentials.basic(username, password);
        this.builder.header("Authorization", credentials);
        return this;
    }

    /**
     * 执行Get
     *
     * @param uri URI
     * @return token
     */
    public String get(String uri) {
        return get(uri, Maps.hashMap(), String.class);
    }

    /**
     * 执行Get
     *
     * @param uri URI
     * @return token
     */
    public <T> T get(String uri, Class<T> cls) {
        return get(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行Get
     *
     * @param uri URI
     * @return token
     */
    public <T> T get(String uri, TypeReference<T> cls) {
        return get(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行Get
     *
     * @param uri URI
     * @return token
     */
    public <T> T get(String uri, Map<String, String> headers, Class<T> cls) {
        return execute(this.getBuilder(headers, uri), cls);
    }

    /**
     * 执行Get
     *
     * @param uri URI
     * @return token
     */
    public <T> T get(String uri, Map<String, String> headers, TypeReference<T> cls) {
        return execute(this.getBuilder(headers, uri), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public String post(String uri) {
        return post(uri, Maps.hashMap(), String.class);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Class<T> cls) {
        return post(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, TypeReference<T> cls) {
        return post(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Object body, Class<T> cls) {
        if (body instanceof String) {
            return this.post(uri, (String) body, cls);
        }
        return this.post(uri, JsonUtils.toJson(body), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, String body, Class<T> cls) {
        return post(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Object body, TypeReference<T> cls) {
        if (body instanceof String) {
            return this.post(uri, (String) body, cls);
        }
        return this.post(uri, JsonUtils.toJson(body), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, String body, TypeReference<T> cls) {
        return post(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Map<String, String> headers, Class<T> cls) {
        return post(uri, "", headers, cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Map<String, String> headers, TypeReference<T> cls) {
        return post(uri, "", headers, cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Object body, Map<String, String> headers, Class<T> cls) {
        if (body instanceof String) {
            return this.post(uri, (String) body, headers, cls);
        }
        return this.post(uri, JsonUtils.toJson(body), headers, cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, String body, Map<String, String> headers, Class<T> cls) {
        return execute(this.postBuilder(headers, body, uri), cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, Object body, Map<String, String> headers, TypeReference<T> cls) {
        if (body instanceof String) {
            return this.post(uri, (String) body, headers, cls);
        }
        return this.post(uri, JsonUtils.toJson(body), headers, cls);
    }

    /**
     * 执行Post
     *
     * @param uri URI
     * @return token
     */
    public <T> T post(String uri, String body, Map<String, String> headers, TypeReference<T> cls) {
        return execute(this.postBuilder(headers, body, uri), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public String put(String uri) {
        return this.put(uri, "", String.class);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Class<T> cls) {
        return this.put(uri, "", cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, TypeReference<T> cls) {
        return this.put(uri, "", cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Object body, Class<T> cls) {
        if (body instanceof String) {
            return this.put(uri, (String) body, cls);
        }
        return put(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, String body, Class<T> cls) {
        return put(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Object body, TypeReference<T> cls) {
        if (body instanceof String) {
            return this.put(uri, (String) body, cls);
        }
        return put(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, String body, TypeReference<T> cls) {
        return put(uri, body, Maps.hashMap(), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Map<String, String> headers, Class<T> cls) {
        return put(uri, "", headers, cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Map<String, String> headers, TypeReference<T> cls) {
        return put(uri, "", headers, cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Object body, Map<String, String> headers, Class<T> cls) {
        if (body instanceof String) {
            return this.put(uri, (String) body, headers, cls);
        }
        return put(uri, JsonUtils.toJson(body), headers, cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, String body, Map<String, String> headers, Class<T> cls) {
        return execute(this.putBuilder(headers, body, uri), cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, Object body, Map<String, String> headers, TypeReference<T> cls) {
        if (body instanceof String) {
            return this.put(uri, (String) body, headers, cls);
        }
        return put(uri, JsonUtils.toJson(body), headers, cls);
    }

    /**
     * 执行Put
     *
     * @param uri URI
     * @return token
     */
    public <T> T put(String uri, String body, Map<String, String> headers, TypeReference<T> cls) {
        return execute(this.putBuilder(headers, body, uri), cls);
    }


    /**
     * 执行delete
     *
     * @param uri URI
     * @return token
     */
    public String delete(String uri) {
        return this.delete(uri, String.class);
    }

    /**
     * 执行delete
     *
     * @param uri URI
     * @param cls cls
     * @return token
     */
    public <T> T delete(String uri, Class<T> cls) {
        return delete(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行delete
     *
     * @param uri URI
     * @param cls cls
     * @return token
     */
    public <T> T delete(String uri, TypeReference<T> cls) {
        return delete(uri, Maps.hashMap(), cls);
    }

    /**
     * 执行delete
     *
     * @param uri URI
     * @return token
     */
    public String delete(String uri, Map<String, String> headers) {
        return this.delete(uri, headers, String.class);
    }


    /**
     * 执行delete
     *
     * @param uri URI
     * @return token
     */
    public <T> T delete(String uri, Map<String, String> headers, Class<T> cls) {
        return this.execute(this.deleteBuilder(headers, uri), cls);
    }


    /**
     * 执行delete
     *
     * @param uri URI
     * @return token
     */
    public <T> T delete(String uri, Map<String, String> headers, TypeReference<T> cls) {
        return this.execute(this.deleteBuilder(headers, uri), cls);
    }

    /**
     * 执行下载
     *
     * @param uri URI
     */
    public void download(String uri, File dest) {
        this.download(uri, UUID.randomUUID().toString(), dest, libs -> {
        });
    }

    /**
     * 执行下载
     *
     * @param uri URI
     */
    public void download(String uri, String name, File dest, ProgressListener listener) {

        listener = Objects.nonNull(listener) ? listener : libs -> {
        };
        Request request = this.builder.url(uri).build();
        Response response = null;
        ResponseBody body = null;
        InputStream is = null;
        FileOutputStream os = null;
        try {
            response = this.execute(request);
            body = response.body();
            if (Objects.nonNull(body)) {
                is = body.byteStream();
                os = new FileOutputStream(dest);
                //开始事件
                listener.listen(ProgressLibs.start(name));
                long sum = 0L;
                long total = body.contentLength();
                byte[] buf = new byte[10 * 1024 * 1024];
                int n;
                while ((n = is.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    sum += n;
                    // 读取中事件
                    listener.listen(ProgressLibs.running(name, n, Numbers.percent(sum * 1.0 / total)));
                }
                // 结束事件
                listener.listen(ProgressLibs.end(name));
            }
        } catch (Exception e) {
            log.info("execute uri = {} body = {}", request.url(), JsonUtils.toJson(request.body()));
            log.error(e.getMessage(), e);
            listener.listen(ProgressLibs.error(name));
        } finally {
            FilesUtils.close(response, body, is, os);
        }
    }

    /**
     * 执行上传
     *
     * @param uri URI
     * @return token
     */
    @SuppressWarnings("ALL")
    public <T> T upload(String uri, File file, Class<T> cls) {
        Response response = null;
        try {
            response = this.upload(uri, file);
            return this.response(response, cls);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            FilesUtils.close(response);
        }
    }

    /**
     * 执行上传
     *
     * @param uri URI
     * @return token
     */
    @SuppressWarnings("ALL")
    public <T> T upload(String uri, File file, TypeReference<T> cls) {
        Response response = null;
        try {
            response = this.upload(uri, file);
            return this.response(response, cls);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            FilesUtils.close(response);
        }
    }

    /**
     * 执行下载
     *
     * @param uri URI
     * @return token
     */
    public Response upload(String uri, File file) throws IOException {
        return upload(uri, Collections.singletonMap("file", file));
    }

    /**
     * 执行下载
     *
     * @param uri URI
     * @return token
     */
    public <T> T upload(String uri, Map<String, Object> body, Class<T> cls) throws IOException {
        Response response = this.upload(uri, body);
        return this.response(response, cls);
    }

    /**
     * 执行下载
     *
     * @param uri URI
     * @return token
     */
    public <T> T upload(String uri, Map<String, Object> body, TypeReference<T> cls) throws IOException {
        Response response = this.upload(uri, body);
        return this.response(response, cls);
    }

    /**
     * 执行下载
     *
     * @param uri URI
     * @return token
     */
    public Response upload(String uri, Map<String, Object> body) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (Maps.iterable(body)) {
            body.forEach((k, v) -> {
                // 如果是文件
                if (v instanceof File) {
                    File file = (File) v;
                    builder.addFormDataPart(k, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                            .build();
                } else {
                    builder.addFormDataPart(k, String.valueOf(v));
                }
            });
        }
        return upload(uri, builder.build());
    }

    /**
     * 执行上传
     *
     * @param uri URI
     * @return token
     */
    public Response upload(String uri, MultipartBody multipartBody) throws IOException {
        // 发送请求
        Request.Builder builder = this.builder.post(multipartBody).url(uri);
        return this.execute(builder.build());
    }

    private static void doHeaders(Request.Builder builder, Map<String, String> headers) {
        log.info("headers = {}", headers);
        if (Maps.iterable(headers)) {
            headers.forEach(builder::header);
        }
    }

    @SuppressWarnings("ALL")
    private <T> T execute(Request.Builder builder, Class<T> cls) {
        Request request = builder.build();
        try (Response response = this.execute(request); ResponseBody body = response.body()) {
            log.info("execute uri = {} body = {} response = {}", request.url(), JsonUtils.toJson(request.body()), response);
            if (Objects.isNull(body)) {
                return null;
            }
            return JsonUtils.toBean(body.string(), cls).orElse(null);
        } catch (IOException e) {
            log.info("execute uri = {} body = {}", request.url(), request.body());
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @SuppressWarnings("ALL")
    private <T> T execute(Request.Builder builder, TypeReference<T> cls) {
        Request request = builder.build();
        try (Response response = this.execute(request); ResponseBody body = response.body()) {
            log.info("execute uri = {} body = {} response = {}", request.url(), JsonUtils.toJson(request.body()), response);
            if (Objects.isNull(body)) {
                return null;
            }
            return JsonUtils.toBean(body.string(), cls).orElse(null);
        } catch (IOException e) {
            log.info("execute uri = {} body = {}", request.url(), request.body());
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @SuppressWarnings("ALL")
    private <T> T response(Response response, Class<T> cls) {
        log.info("response = {} ", response);
        try (ResponseBody body = response.body()) {
            if (Objects.isNull(body)) {
                return null;
            }
            return JsonUtils.toBean(body.string(), cls).orElse(null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            FilesUtils.close(response);
        }
    }

    @SuppressWarnings("ALL")
    private <T> T response(Response response, TypeReference<T> cls) {
        log.info("response = {} ", response);
        try (ResponseBody body = response.body()) {
            if (Objects.isNull(body)) {
                return null;
            }
            return JsonUtils.toBean(body.string(), cls).orElse(null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            FilesUtils.close(response);
        }
    }

    private Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    private Request.Builder getBuilder(Map<String, String> headers, String uri) {
        log.info("execute method = GET uri = {} ", uri);
        Request.Builder builder = this.builder.get().url(uri);
        doHeaders(builder, headers);
        return builder;
    }

    private Request.Builder postBuilder(Map<String, String> headers, String body, String uri) {
        this.doLog(uri, body, "POST");
        RequestBody requestBody = StringUtils.isBlank(body) ? EMPTY : RequestBody.create(JSON, body);
        Request.Builder builder = this.builder.post(requestBody).url(uri);
        doHeaders(builder, headers);
        return builder;
    }

    private Request.Builder putBuilder(Map<String, String> headers, String body, String uri) {
        this.doLog(uri, body, "PUT");
        RequestBody requestBody = StringUtils.isBlank(body) ? EMPTY : RequestBody.create(JSON, body);
        Request.Builder builder = this.builder.put(requestBody).url(uri);
        doHeaders(builder, headers);
        return builder;
    }

    private Request.Builder deleteBuilder(Map<String, String> headers, String uri) {
        this.doLog(uri, "", "DELETE");
        Request.Builder builder = this.builder.delete().url(uri);
        doHeaders(builder, headers);
        return builder;
    }

    private void doLog(String uri, String body, String method) {
        log.info("execute method = {} uri = {} body = {} ", method, uri, body);
    }
}
