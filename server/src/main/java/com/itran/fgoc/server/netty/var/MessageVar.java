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
        String REGISTER_RESPONSE = "8100";
        String SERVER_COMMON_RESPONSE = "8001";
        String LOCATIONS_UPLOAD = "0704";

        Enums SCOPE = Enums.build()
                .add(NULL, "空消息")
                .add(REGISTER, "注册")
                .add(AUTHENTICA, "客户端鉴权")
                .add(REGISTER_RESPONSE, "注册应答")
                .add(SERVER_COMMON_RESPONSE, "平台通用应答")
                .add(LOCATIONS_UPLOAD, "定位数据批量上传")
                ;
    }

    /**
     * 加密方式
     */
    interface Encryption {
        String DEFAULT = "000";
        String RSC = "001";

        Enums SCOPE = Enums.build()
                .add(DEFAULT, "不加密")
                .add(RSC, "RSC加密")
                ;
    }

    /**
     * 保留字
     */
    interface Reserve {
        String DEFAULT = "000";

        Enums SCOPE = Enums.build()
                .add(DEFAULT, "默认")
                ;
    }

    /**
     * 平台通用应答结果
     */
    interface ServerCommonResponseCode {
        int SUCCESS = 0;
        int FAIL = 1;
        int WRONG = 2;
        int NOT_SUPPORT = 3;
        int ALARM_PROCESSING_CONFIRMATION = 4;

        Enums SCOPE = Enums.build()
                .add(SUCCESS, "成功")
                .add(FAIL, "失败")
                .add(WRONG, "消息错误")
                .add(NOT_SUPPORT, "不支持")
                .add(ALARM_PROCESSING_CONFIRMATION, "报警处理确认")
                ;
    }
}
