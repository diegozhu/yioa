package com.yioa.common.util;

/**
 * Created by tao on 2017-05-25.
 */
public final class PageUtil {
    private PageUtil() {
    }

    public static int getOffset(int current, int size) {
        int offset = 0;

        if (current < 1 || size < 1) {
            throw new RuntimeException("current and size  ( " + current + "" + size + ") ,show greeter than 0");
        }

        return (current -1) * size;

    }


}
