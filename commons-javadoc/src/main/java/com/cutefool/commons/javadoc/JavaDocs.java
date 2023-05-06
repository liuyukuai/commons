/*
 *
 */
package com.cutefool.commons.javadoc;

import lombok.extern.slf4j.Slf4j;

/**
 * java comment
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 09:52
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class JavaDocs {
//
//    public static List<ClsComment> comments() {
////        ClassDoc[] classes = DocInstance.get().classes();
////        return Stream.of(classes).map(c -> {
////            ClsComment clsComment = new ClsComment();
////            clsComment.setName(c.toString());
////            clsComment.setAuthor(Comments.author(c.getRawCommentText()));
////            clsComment.setComment(c.commentText());
////            clsComment.setMethodComments(methods(c, c.methods(false)));
////            clsComment.setFieldComments(fields(c.fields(false)));
////            return clsComment;
////        }).collect(Collectors.toList());
//        return Collections.emptyList();
//    }
//
//    /**
//     * 获取所有的方法注释
//     *
//     * @param c    类
//     * @param docs 方法文档
//     * @return 方法文档注释
//     */
//    private static List<MethodComment> methods(ClassDoc c, MethodDoc[] docs) {
//        return Stream.of(docs).map(m -> {
//            MethodComment comment = new MethodComment();
//            comment.setId(params(c.qualifiedTypeName(), m.name(), m.parameters()));
//            comment.setComment(m.commentText());
//            comment.setRawComment(m.getRawCommentText());
//            comment.setName(m.name());
//            comment.setApiId(Comments.id(m.getRawCommentText()));
//            return comment;
//        }).collect(Collectors.toList());
//    }
//
//
//    /**
//     * 获取所有的属性文档注释
//     *
//     * @param docs 属性注释
//     * @return 属性文档注释
//     */
//
//    private static List<FieldComment> fields(FieldDoc[] docs) {
//        return Stream.of(docs).map(m -> {
//            FieldComment comment = new FieldComment();
//            comment.setComment(m.commentText());
//            comment.setName(m.name());
//            return comment;
//        }).collect(Collectors.toList());
//
//    }
//
//
//    private static String params(String clsName, String name, Parameter[] parameters) {
//        name = clsName + name + Stream.of(parameters)
//                .map(m -> StringUtils.join(m.type().simpleTypeName()))
//                .collect(Collectors.joining());
//        String id = Md5Utils.digestMd5(name);
//        log.debug("name : [{}]  id = [{}]", name, id);
//        return id;
//    }
//
//
//    public static List<ClsComment> execute(String packages, File dir) {
//        return execute(packages, Optional.ofNullable(dir).map(Collections::singletonList).orElse(Collections.emptyList()));
//    }
//
//    public static List<ClsComment> execute(String packages, List<File> dirs) {
//        log.info("packages : [{}] ,dir : [{}]", packages, dirs);
//
//        List<File> files = DirScanner.scan(System.getProperty("user.dir"), "java")
//                .stream()
//                .filter(e -> e.getAbsolutePath().contains("src/main/java"))
//                .collect(Collectors.toList());
//
//
//        String sources = Stream.concat(files.stream(), Lists.empty(dirs).stream()).map(File::getAbsolutePath).collect(Collectors.joining(":"));
//        List<String> params = new ArrayList<>();
//        params.add("-verbose");
//        params.add("-doclet");
//        params.add(DocInstance.class.getName());
//        params.add("-sourcepath");
//        params.add(sources);
//        params.add("-subpackages");
//        params.add(packages);
//        log.info("execute command : javadoc {}", StringUtils.join(params, " "));
//        Main.execute(params.toArray(new String[]{}));
//        return comments();
//    }

}
