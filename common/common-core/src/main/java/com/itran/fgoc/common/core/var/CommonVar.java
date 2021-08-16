package com.itran.fgoc.common.core.var;

import com.itran.fgoc.common.core.api.Enums;

/**
 * 公用常量
 */
public interface CommonVar {

    String SYSTEM = "系统";
    String CENTER = "center";
    long CURRENT_HARBOR_ID = 1;
    String CURRENT_HARBOR = "SK";
    String CURRENT_HARBOR_NAME = "蛇口";
    String DICT_TEXT_SUFFIX = "DictValue";
    String SYSTEM_CLIENT_ID = "fgoc";

    Enums SCOPE = Enums.build()
            .add(SYSTEM, "系统")
            .add(CENTER, "中心系统")
            .add(CURRENT_HARBOR_ID, "当前港口ID")
            .add(CURRENT_HARBOR, "当前港口代码")
            .add(CURRENT_HARBOR_NAME, "当前港口中文")
            .add(DICT_TEXT_SUFFIX, "字典字段后缀")
            .add(SYSTEM_CLIENT_ID, "主系统clientId")
            ;
}
