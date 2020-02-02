package com.yyy.fuzhuangpad.style;

import android.content.Context;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class StyleUtil {
    public static List<FormColumn> getTitles(Context context) {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(context.getString(R.string.style_form_num), 0.5f, true));
        list.add(new FormColumn(context.getString(R.string.style_form_code), true));
        list.add(new FormColumn(context.getString(R.string.style_form_name), true));
        list.add(new FormColumn(context.getString(R.string.style_form_type), true));
        list.add(new FormColumn(context.getString(R.string.style_form_sizes), true));
        list.add(new FormColumn(context.getString(R.string.style_form_customer), true));
        list.add(new FormColumn(context.getString(R.string.style_form_year), true));
        list.add(new FormColumn(context.getString(R.string.style_form_sale_price), true));
        list.add(new FormColumn(context.getString(R.string.style_form_wholesale_price), true));
        return list;
    }
}
