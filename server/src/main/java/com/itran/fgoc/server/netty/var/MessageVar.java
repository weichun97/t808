package com.itran.fgoc.server.netty.var;

import com.itran.fgoc.common.core.api.Enums;

/**
 * @author chun
 * @date 2021/8/18 11:08
 */
public interface MessageVar {

    /**
     * 消息 id
     */
    interface MessageId {
        String NULL = "-1";
        String REGISTER = "0100";
        String AUTHENTICA = "0102";
        String REGISTER_ANSWER = "8100";
        String SERVER_COMMON_ANSWER = "8001";
        String LOCATIONS_UPLOAD = "0704";

        Enums SCOPE = Enums.build()
                .add(NULL, "空消息")
                .add(REGISTER, "注册")
                .add(AUTHENTICA, "客户端鉴权")
                .add(REGISTER_ANSWER, "注册应答")
                .add(SERVER_COMMON_ANSWER, "平台通用应答")
                .add(LOCATIONS_UPLOAD, "定位数据批量上传")
                ;
    }

    /**
     * 加密方式
     */
    interface Encryption {
        String NULL = "000";
        String RSC = "001";

        Enums SCOPE = Enums.build()
                .add(NULL, "000")
                .add(RSC, "001")
                ;
    }

    /**
     * 保留字
     */
    interface Reserve {
        String NULL = "000";

        Enums SCOPE = Enums.build()
                .add(NULL, "000")
                ;
    }
}
