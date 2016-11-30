package com.zhangk.datasource.matadata.impl.common;


import com.zhangk.datasource.matadata.api.ReadAndWriteDataSessionFactory;
import com.zhangk.datasource.matadata.api.SqlSessionFactorySupport;
import com.zhangk.datasource.matadata.session.DataSession;

/**
 * 读写dataSessionFactory实现
 * @ClassName:  DefaultReadAndWriteDataSessionFactory   
 * @Description:TODO 
 * @author: zhangk  
 * @date:   2015年12月25日 下午4:37:08   
 *
 */
public class DefaultReadAndWriteDataSessionFactory implements ReadAndWriteDataSessionFactory {

	private SqlSessionFactorySupport support;


	private String defaultDataSource;
	private String readDataSource;
	private String writeDataSource;

	private DataSession readDataSession;
	private DataSession writeDataSession;

	@Override
	public DataSession getDaoByDataSourceName(String dataSourceName) {
		DataSession singleDataSession = new DefaultDataSession(support.getSqlSessionFacotry(dataSourceName));
		return singleDataSession;
	}

	@Override
	public DataSession getReadDataSession() {
		if (readDataSession != null)
			return readDataSession;
		readDataSession = new DefaultDataSession(support.getSqlSessionFacotry(readDataSource));
		return readDataSession;
	}

	@Override
	public DataSession getWriteDataSession() {
		if (writeDataSession != null)
			return writeDataSession;
		writeDataSession = new DefaultDataSession(support.getSqlSessionFacotry(writeDataSource));
		return writeDataSession;
	}

	public SqlSessionFactorySupport getSupport() {
		return support;
	}

	public void setSupport(SqlSessionFactorySupport support) {
		this.support = support;
	}

	public String getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(String defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public String getReadDataSource() {
		return readDataSource;
	}

	public void setReadDataSource(String readDataSource) {
		this.readDataSource = readDataSource;
	}

	public String getWriteDataSource() {
		return writeDataSource;
	}

	public void setWriteDataSource(String writeDataSource) {
		this.writeDataSource = writeDataSource;
	}

}
