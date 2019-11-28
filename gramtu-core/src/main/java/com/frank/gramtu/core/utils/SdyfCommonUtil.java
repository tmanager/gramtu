package com.frank.gramtu.core.utils;

import java.util.UUID;

/**
 * Copyright(C) ShanDongYinFang 2019.
 * <p>
 * 公共方法类.
 *
 * @author 张孝党 2019/06/03.
 * @version V0.0.2.
 * <p>
 * 更新履历： V0.0.1 2019/06/03 张孝党 创建.
 */
public class SdyfCommonUtil {

    /**
     * 获取UUID.
     *
     * @return 32位uuid.
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
