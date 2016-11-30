package com.zhangk.datasource.matadata.param;

public enum ESQL {
	
	// system sql
	SAVE("save"), 
	SAVEBATCH("saveBatch"), 
	UPDATE("update"), 
	UPDATEBATCH("updateBatch"),
	LOGICDELETE("logicDelete"),
	PHYSICALDELETE("physicalDelete"),
	PHYSICALBATCHDELETE("physicalBatchDelete"),
	QUERYSINGLERESULTBYID("querySingleResultById"), 
	QUERYSINGLERESULTBYUUID("querySingleResultByUUID"),
	QUERYSINGLERESULTBYPARAMS("querySingleResultByParams"), 
	QUERYLISTRESULT("queryListResult"), 
	QUERYLISTRESULTCOUNT("queryListResultCount"),
	UPDATECUSTOMCOLUMNBYWHERE("updateCustomColumnByWhere"), 
	LOGICWHEREDELETE("logicWhereDelete"), 
	PHYSICALWHEREDELETE("physicalWhereDelete"), 
    QUERYLISTRESULTCOUNTBYWHERE("queryListResultCountByWhere"),
	QUERYLISTRESULTBYWHERE("queryListResultByWhere");

	private String sql;

	ESQL(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
}
