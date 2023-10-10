package com.crm.dpeda.utils;


import com.crm.dpeda.exceptions.ParamsException;

public class AssertUtil {

    /**
     * 判断条件是否满足
     *  如果条件满足，则抛出参数异常
     * @param flag
     * @param msg
     * @return void
     */
    public static void isTrue(Boolean flag, String msg) {
        //判断布尔类型如果为true
        if (flag) {
            // msg  是传进来的 参数，如果为空就抛出异常
            throw new ParamsException(msg);
        }
    }

}
