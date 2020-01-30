package com.yyy.fuzhuangpad.sale;

import android.content.Context;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class BillingUtil {
    public static List<FormColumn> getTitles(Context context) {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_num), 0.5f, true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_type), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_code), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_shop), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_date), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_customer), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_saler), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_qty), true));
        list.add(new FormColumn(context.getString(R.string.sale_billing_form_sum), true));
        return list;
    }
}
