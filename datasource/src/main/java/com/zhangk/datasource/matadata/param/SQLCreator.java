package com.zhangk.datasource.matadata.param;


import com.zhangk.datasource.constant.Constants;
import com.zhangk.datasource.utils.SQLUtil;

/**
 * SQL生产器，根据类、查询类型或方法名称产生SQL
 *
 * @ClassName: SQLCretor
 * @Description:TODO
 * @author: zhangk
 * @date: 2015年11月4日 下午7:34:08
 */
public class SQLCreator {


    @SuppressWarnings("rawtypes")
    public static SQL set(Class clazz, ESQL template) {
        return new DefaultSQL(clazz.getSimpleName(), template.getSql());
    }


    @SuppressWarnings("rawtypes")
    public static SQL set(Class clazz, Throwable able) {
        return new DefaultSQL(clazz.getSimpleName(), getCurrentMethodName(able));
    }

    public static SQL set(String className, Throwable able) {
        return new DefaultSQL(className, getCurrentMethodName(able));
    }

    @SuppressWarnings("rawtypes")
    public static SQL set(Class clazz, String elementId) {
        return new DefaultSQL(clazz.getSimpleName(), elementId);
    }

    public static SQL set(String className, String elementId) {
        return new DefaultSQL(className, elementId);
    }

    public static String getCurrentMethodName(Throwable throwable) {
        if (throwable == null)
            return null;
        return throwable.getStackTrace()[0].getMethodName();
    }

    public static CustomSQL where() {
        return new DefaultCustomerSQL();
    }

    @SuppressWarnings("rawtypes")
    public static SQL set(Class clazz) {
        return new DefaultSQL(clazz.getSimpleName(), getCurrentMethodName());
    }

    private static String getCurrentMethodName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        int thisMethodIndex = 0;
        for (StackTraceElement element : elements) {
            thisMethodIndex++;
            if (element != null && element.getMethodName().equals("set"))
                break;
        }
        String methodName = elements[thisMethodIndex + 1].getMethodName();
        return methodName;
    }
}

class DefaultSQL implements SQL {
    private String className;
    private String toClassName;
    private String sqlName;

    public DefaultSQL(String className, String sqlName) {
        this.className = className;
        this.sqlName = sqlName;
    }

    public DefaultSQL(String className, String toClassNme, String sqlName) {
        this.className = className;
        this.toClassName = toClassNme;
        this.sqlName = sqlName;
    }

    public DefaultSQL(Class<?> clazz, Throwable able) {
        this.className = clazz.getName();
        this.sqlName = SQLCreator.getCurrentMethodName(able);
    }

    @Override
    public String get() {
        return new StringBuffer(className).append(Constants.Separator.DOT).append(sqlName).toString();
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getToClassName() {
        return toClassName;
    }

    public void setToClassName(String toClassName) {
        this.toClassName = toClassName;
    }

}

class DefaultCustomerSQL implements CustomSQL {
    StringBuilder builder = new StringBuilder();

    @Override
    public CustomSQL cloumn(String column) {
        if (builder == null)
            builder = new StringBuilder();
        if (!column.contains(Constants.Separator.UPDERLINE))
            column = SQLUtil.formateToColumn(column);
        builder.append(Constants.Separator.BLANK).append(column).append(Constants.Separator.BLANK);
        return this;
    }

    @Override
    public CustomSQL operator(ESQLOperator operator) {
        if (builder == null)
            throw new IllegalArgumentException("Operator can't be in the first!");
        builder.append(operator);
        return this;
    }

    @Override
    public CustomSQL v(Object value) {
        if (builder == null)
            throw new IllegalArgumentException("Value can't be in the first!");
        builder.append(Constants.Separator.BLANK).append(SQLUtil.get(value)).append(Constants.Separator.BLANK);
        return this;
    }

    @Override
    public String toString() {
        if (builder == null)
            throw new IllegalArgumentException("Where sql doesn't init!");
        String result = builder.toString();
//		builder = null;
        return result;
    }


}
