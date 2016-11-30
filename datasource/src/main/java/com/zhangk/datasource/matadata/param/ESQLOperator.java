package com.zhangk.datasource.matadata.param;

public enum ESQLOperator {
	/** 等号 */
	EQ(" = "),
	/** 大于号 */
	GT(" > "),
	/** 小于号 */
	LT(" < "),
	/** 大于等于号 */
	GTEQ(">="),
	/** 小于等于号 */
	LTEQ(" <= "),
	/** 不等于 */
	NEQ(" <> "),
	/** and */
	AND(" AND "),
	/** or */
	OR(" OR "),
	/** in */
	IN(" IN "),
	/** not */
	NOT(" NOT "),
	/** null */
	NULL(" NULL "),
	/** is */
	IS(" IS "),
	/** between */
	BETWEEN(" BETWEEN "),
	/** exists */
	EXISTS(" EXISTS "),
	/** set */
	SET(" SET "),
	/** ( */
	LBRACKET(" ( "),
	/** ) */
	RBRACKET(" ) "),
	/** like */
	LIKE(" LIKE "),
	/** , */
	COMMA(" , "),
	/** LIMIT */
	LIMIT(" LIMIT "),
	/** ORDER BY */
	ORDER_BY(" ORDER BY "),
	/** ASC */
	ASC(" ASC "),
	/** DESC */
	DESC(" DESC ");
	private String value;

	ESQLOperator(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
