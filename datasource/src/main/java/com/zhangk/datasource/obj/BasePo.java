package com.zhangk.datasource.obj;

import com.zhangk.utils.common.UUIDProvider;
import com.zhangk.datasource.constant.Constants;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhangk
 * @Date 16/4/27
 */
public class BasePo implements Serializable{
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    /** 唯一标识  */
    private int id;
    public static String FIELD_ID = "id";
    /** 唯一标识uuid  */
    private String uuid;
    public static String FIELD_UUID = "uuid";
    /** 创建人 */
    private int createUser = 0;
    public static String FIELD_CREATE_USER = "createUser";
    /** 创建时间 */
    private Date createTime;
    public static String FIELD_CREATE_TIME = "createTime";
    /** 修改人 */
    private int updateUser = 0;
    public static String FIELD_UPDATE_USER = "updateUser";
    /** 修改人时间  */
    private Date updateTime;
    public static String FIELD_UPDATE_TIME = "updateTime";
    /** 是否启用 */
    private Integer disabled = 0;
    public static String FIELD_DISABLED = "disabled";
    /** 描述 */
    private String remark ="";
    public static String FIELD_REMARK = "remark";
    /** 排序 */
    private int orderBy;
    public static String FIELD_ORDER_BY = "orderBy";


     public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public int getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(int updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public static <T> T getInstance(Class<T> clazz) {
        BasePo o = null;
        try {
            o = (BasePo) clazz.newInstance();
            o.setUuid(UUIDProvider.uuid());
            o.setDisabled(Constants.DeleteStatus.ENABLED);
            o.setCreateTime(new Date());
            o.setUpdateTime(new Date());
            return clazz.cast(o);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
