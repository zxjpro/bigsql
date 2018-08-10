package com.xiaojiezhu.bigsql.sql.resolve.field;

import java.util.List;

/**
 * time 2018/8/10 19:31
 *
 * @author xiaojie.zhu <br>
 */
public class MultipartValueField extends ValueField {
    public static final String GREAT = ">";
    public static final String GREAT_AND = ">=";

    public static final String LESS = "<";
    public static final String LESS_AND = "<=";

    public MultipartValueField(String name) {
        super(name);
    }

    public MultipartValueField(String name, List<Object> values) {
        super(name, values);
    }
}
