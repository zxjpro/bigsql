package com.xiaojiezhu.bigsql.model.construct;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/22 15:59
 * 说明 ...
 */
public class LikeField {
    private String realField;

    private String field;

    private LikeMode likeMode;


    public LikeField() {
    }

    public LikeField(String realField, String field, LikeMode likeMode) {
        this.realField = realField;
        this.field = field;
        this.likeMode = likeMode;
    }

    public String getRealField() {
        return realField;
    }

    public void setRealField(String realField) {
        this.realField = realField;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public LikeMode getLikeMode() {
        return likeMode;
    }

    public void setLikeMode(LikeMode likeMode) {
        this.likeMode = likeMode;
    }

    @Override
    public String toString() {
        return "LikeMode{" +
                "realField='" + realField + '\'' +
                ", field='" + field + '\'' +
                ", likeMode=" + likeMode +
                '}';
    }
}
