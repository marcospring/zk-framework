package com.zhangk.datasource.matadata.param;

public interface CustomSQL {
	CustomSQL cloumn(String column);

	CustomSQL operator(ESQLOperator operator);

	CustomSQL v(Object value);

}
