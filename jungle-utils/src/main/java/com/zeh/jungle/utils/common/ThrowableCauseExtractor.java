package com.zeh.jungle.utils.common;

/**
 * Interface for handlers extracting the cause out of a specific {@link Throwable} type.
 * 
 * @author allen
 * @version $Id: ThrowableCauseExtractor.java, v 0.1 2016年2月28日 上午7:13:17 allen Exp $
 */
public interface ThrowableCauseExtractor {
    /**
     * Extracts the cause from the provided <code>Throwable</code>.
     * 
     * @param throwable the <code>Throwable</code>
     * @return the extracted cause (maybe <code>null</code>)
     * 
     * @throws IllegalArgumentException if <code>throwable</code> is <code>null</code> 
     * or otherwise considered invalid for the implementation
     */
    Throwable extractCause(Throwable throwable);
}
