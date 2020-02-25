package com.yyy.fuzhuangpad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.view.recycle.RecyclerViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectDialog<T extends ISelectText> extends Dialog {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<T> list;
    Context context;
    SelectAdapter adapter;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SelectDialog(@NonNull Context context, @StyleRes int themeResId, List<T> list) {
        super(context,themeResId);
        this.context = context;
        this.list = list;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_select);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        initRecycle();

    }

    private void initRecycle() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerViewDivider(context, LinearLayout.VERTICAL));
        if (adapter == null) {
            initAdapter();
        }
        recyclerView.setAdapter(adapter);

    }

    private void initAdapter() {
        adapter = new SelectAdapter(context, list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, pos);
                    SelectDialog.this.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.bw_exit, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                this.dismiss();
                break;
            case R.id.tv_search:
                break;
        }
    }
}
