package com.zhangk.datasource.matadata.param;

import java.util.Map;

public interface Param {
	Map<String, Object> get();

	Param add(NVPair nvPair);

	Param add(NVPair... pair);

	Param add(Map<String, Object> params);

	Param clean();
}
