package com.yyy.fuzhuangpad.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.view.form.FormColumn;
import com.yyy.fuzhuangpad.view.form.FormRow;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.VH> {
    List<List<FormColumn>> list;
    Context context;
    OnItemClickListener onItemClickListener;

    public FormAdapter(List<List<FormColumn>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(new FormRow(context));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setIsRecyclable(false);
        list.get(position).get(0).setText(position + 1 + "");
        ((FormRow) holder.itemView).setColumns(list.get(position)).addDefaultParams().build().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        public VH(View v) {
            super(v);
        }
    }
}
