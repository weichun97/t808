package com.itran.fgoc.common.core.var;

import com.itran.fgoc.common.core.api.Enums;

/**
 * 通用正则表达式
 * @author chun
 * @date 2020/9/17 10:13
 */
public interface PatternVar {

    String WEEK_CYCLE = "^\\d(,\\d)*$";
    String PHONE = "^1\\d{10}$";
    String PASSWORD = "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,}$";

    Enums SCOPE = Enums.build()
            .add(WEEK_CYCLE, "周期")
            .add(PHONE, "手机号")
            .add(PASSWORD, "密码")
            ;
}
