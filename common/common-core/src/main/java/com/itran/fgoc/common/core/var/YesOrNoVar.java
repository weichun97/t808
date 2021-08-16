package com.itran.fgoc.common.core.var;

import com.itran.fgoc.common.core.api.Enums;

/**
 * @author cocoyang
 * @date 2020/9/21
 * @description
 */
public interface YesOrNoVar {

    /**
     * 是否
     */
    int NO = 0;
    int YES = 1;


    Enums SCOPE = Enums.build()
            .add(YES, "是")
            .add(NO, "否");
}
