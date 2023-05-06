/*
 *
 */
package com.cutefool.commons.office;

import java.io.IOException;

/**
 * @author 271007729@qq.com
 * @date 7/30/21 10:52 AM
 */
public interface OfficesTemplate<T>  {
    /**
     * open document
     *
     * @return Document
     * @throws IOException e
     */
    T open() throws IOException;

    /**
     * write document
     *
     * @param document document
     * @throws IOException e
     */
    void doWrite(T document) throws IOException;



}
