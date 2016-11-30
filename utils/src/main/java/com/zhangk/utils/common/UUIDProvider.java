package com.zhangk.utils.common;

import java.util.UUID;

/**
 * UUID生产器
 * @ClassName:  UUIDProvider
 * @Description:TODO
 * @author: zhangk
 * @date:   2015年11月5日 下午5:13:01
 *
 */
public class UUIDProvider {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

    public static void main(String[] args) {
        System.out.println(UUIDProvider.uuid());
    }
}
