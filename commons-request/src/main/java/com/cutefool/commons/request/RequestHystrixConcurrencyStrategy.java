///*
// *
// */
//package com.cutefool.commons.request;
//
//import com.netflix.hystrix.strategy.HystrixPlugins;
//import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
//import com.cutefool.commons.core.BizException;
//import com.cutefool.commons.core.page.ResponseCode;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.concurrent.Callable;
//
///**
// * 熔断隔离级别类
// *
// * @author 271007729@qq.com
// * @date 2019/10/12 3:02 PM
// */
//@SuppressWarnings("WeakerAccess")
//@Component
//public class RequestHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
//
//    public RequestHystrixConcurrencyStrategy() {
//        HystrixPlugins.reset();
//        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
//    }
//
//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        return new WrappedCallable<>(callable, requestAttributes);
//    }
//
//    static class WrappedCallable<T> implements Callable<T> {
//
//        private final Callable<T> target;
//        private final RequestAttributes requestAttributes;
//
//        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes) {
//            this.target = target;
//            this.requestAttributes = requestAttributes;
//        }
//
//        @Override
//        public T call() {
//            try {
//                RequestContextHolder.setRequestAttributes(requestAttributes);
//                try {
//                    return target.call();
//                } catch (Exception e) {
//                    throw new BizException(ResponseCode.SERVICE_IS_DOWN.getCode(), e.getMessage());
//                }
//            } finally {
//                RequestContextHolder.resetRequestAttributes();
//            }
//        }
//    }
//}
