package com.yyy.fuzhuangpad.color;

import android.content.Context;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class ColorUtil {
    public static List<FormColumn> getCustomer(Context context) {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(context.getString(R.string.color_form_num), 0.5f, true));
        list.add(new FormColumn(context.getString(R.string.color_form_value), true));
        list.add(new FormColumn(context.getString(R.string.color_form_name), true));
        list.add(new FormColumn(context.getString(R.string.color_form_type), true));
        list.add(new FormColumn(context.getString(R.string.color_form_remark), 3));
        return list;
    }
}
