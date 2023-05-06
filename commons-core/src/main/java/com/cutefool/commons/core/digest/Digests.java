package com.cutefool.commons.core.digest;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 签名
 */
@SuppressWarnings("unused")
public enum Digests implements DigestsInvoker {

    DEFAULTS(new DigestsTimeInvoker());


    final DigestsInvoker invoker;


    Digests(DigestsInvoker invoker) {
        this.invoker = invoker;
    }

    public static Digests byType(String type) {
        return Stream.of(Digests.values()).filter(e -> Objects.equals(type, e.name())).findAny().orElse(Digests.DEFAULTS);
    }

    @Override
    public boolean validate(String s, long expirationTimes) {
        return this.invoker.validate(s, expirationTimes);
    }

    @Override
    public String getValue(String s, long expirationTimes) {
        return this.invoker.getValue(s, expirationTimes);
    }

    @Override
    public DigestLibs load(String s) {
        return this.invoker.load(s);
    }
}
