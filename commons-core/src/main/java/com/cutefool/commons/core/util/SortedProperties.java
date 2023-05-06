package com.cutefool.commons.core.util;

import lombok.EqualsAndHashCode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * 可排序的properties
 *
 * @author 271007729@qq.com
 * @date 2022/09/13 12:55 PM
 */
@EqualsAndHashCode
@SuppressWarnings("unused")
public class SortedProperties extends Properties {

    static final String EOL = System.lineSeparator();

    private static final Comparator<Object> keyComparator = Comparator.comparing(String::valueOf);

    private static final Comparator<Entry<Object, Object>> entryComparator = Entry.comparingByKey(keyComparator);

    private final boolean omitComments;

    public SortedProperties(boolean omitComments) {
        this.omitComments = omitComments;
    }

    public SortedProperties(Properties properties, boolean omitComments) {
        this(omitComments);
        putAll(properties);
    }

    @Override
    public void store(OutputStream out, String comments) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        super.store(baos, (this.omitComments ? null : comments));
        String contents = baos.toString(StandardCharsets.ISO_8859_1.name());
        for (String line : contents.split(EOL)) {
            if (!(this.omitComments && line.startsWith("#"))) {
                out.write((line + EOL).getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }

    @Override
    public void store(Writer writer, String comments) throws IOException {
        StringWriter stringWriter = new StringWriter();
        super.store(stringWriter, (this.omitComments ? null : comments));
        String contents = stringWriter.toString();
        for (String line : contents.split(EOL)) {
            if (!(this.omitComments && line.startsWith("#"))) {
                writer.write(line + EOL);
            }
        }
    }

    @Override
    public void storeToXML(OutputStream out, String comments) throws IOException {
        super.storeToXML(out, (this.omitComments ? null : comments));
    }

    @Override
    public void storeToXML(OutputStream out, String comments, String encoding) throws IOException {
        super.storeToXML(out, (this.omitComments ? null : comments), encoding);
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keySet());
    }

    @Override
    public Set<Object> keySet() {
        Set<Object> sortedKeys = new TreeSet<>(keyComparator);
        sortedKeys.addAll(super.keySet());
        return Collections.synchronizedSet(sortedKeys);
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        Set<Entry<Object, Object>> sortedEntries = new TreeSet<>(entryComparator);
        sortedEntries.addAll(super.entrySet());
        return Collections.synchronizedSet(sortedEntries);
    }

}
