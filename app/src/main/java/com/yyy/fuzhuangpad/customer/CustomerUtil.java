package com.yyy.fuzhuangpad.customer;

import android.content.Context;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class CustomerUtil {
    public static List<FormColumn> getTitles(Context context) {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(context.getString(R.string.customer_form_num), 0.5f, true));
        list.add(new FormColumn(context.getString(R.string.customer_form_code), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_cus_name), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_cus_saler), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_type), true));
        list.add(new FormColumn(context.getString(R.string.customer_detail_contacts), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_phone), true));
        return list;
    }
    public static List<FormColumn> getSelectTitles(Context context) {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(context.getString(R.string.customer_form_num), 0.5f, true));
        list.add(new FormColumn(context.getString(R.string.customer_form_code), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_cus_name), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_cus_saler), true));
        list.add(new FormColumn(context.getString(R.string.customer_form_type), true));
        list.add(new FormColumn(context.getString(R.string.customer_detail_contacts), true));
        return list;
    }
}
