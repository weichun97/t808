package com.itran.fgoc.server.netty.var;

import com.itran.fgoc.common.core.api.Enums;

/**
 * @author chun
 * @date 2021/8/18 11:08
 */
public interface MessageVar {

    String NULL = "-1";
    String REGISTER = "0100";

    Enums SCOPE = Enums.build()
            .add(NULL, "空消息")
            .add(REGISTER, "注册")
            ;
}
