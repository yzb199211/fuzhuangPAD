package com.yyy.fuzhuangpad.sale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnDeleteListener;
import com.yyy.fuzhuangpad.interfaces.OnEditQtyListener;
import com.yyy.fuzhuangpad.interfaces.OnModifyListener;

import java.util.List;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.VH> {
    Context context;
    List<BillDetailBean> list;
    OnDeleteListener onDeleteListener;
    OnEditQtyListener onEditQtyListener;
    OnModifyListener onModifyListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnEditQtyListener(OnEditQtyListener onEditQtyListener) {
        this.onEditQtyListener = onEditQtyListener;
    }

    public void setOnModifyListener(OnModifyListener onModifyListener) {
        this.onModifyListener = onModifyListener;
    }

    public BillDetailAdapter(Context context, List<BillDetailBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_detail, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setIsRecyclable(false);
        BillDetailBean item = list.get(position);
        holder.tvNum.setText(position + 1 + "");
        holder.tvStyleNo.setText(item.getsStyleNo());
        holder.tvStyleColor.setText(item.getsColorName());
        holder.tvStyleSize.setText(item.getsSizeName());
        holder.tvStyleQty.setText(item.getiSumQty() + "");
        holder.tvStylePrice.setText(item.getfPrice() + "");
        holder.tvStyleTotal.setText(item.getfTotal() + "");
        holder.tvStyleRemark.setText(item.getsRemark());
        holder.tvStyleOperate.setText(context.getString(R.string.common_delete));
        holder.tvStyleModify.setText(context.getString(R.string.common_modify));
        holder.tvStyleModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onModifyListener != null) {
                    onModifyListener.onModify(position);
                }
            }
        });
        holder.tvStyleQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditQtyListener != null) {
                    onEditQtyListener.onEdit(position);
                }
            }
        });
        holder.tvStyleOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvNum;
        TextView tvStyleNo;
        TextView tvStyleColor;
        TextView tvStyleSize;
        TextView tvStyleQty;
        TextView tvStylePrice;
        TextView tvStyleTotal;
        TextView tvStyleRemark;
        TextView tvStyleOperate;
        TextView tvStyleModify;

        public VH(View v) {
            super(v);
            tvNum = v.findViewById(R.id.tv_num);
            tvStyleNo = v.findViewById(R.id.tv_style_no);
            tvStyleColor = v.findViewById(R.id.tv_style_color);
            tvStyleSize = v.findViewById(R.id.tv_style_size);
            tvStyleQty = v.findViewById(R.id.tv_style_qty);
            tvStylePrice = v.findViewById(R.id.tv_style_price);
            tvStyleTotal = v.findViewById(R.id.tv_style_total);
            tvStyleRemark = v.findViewById(R.id.tv_style_remark);
            tvStyleOperate = v.findViewById(R.id.tv_style_operate);
            tvStyleModify = v.findViewById(R.id.tv_style_modify);
        }
    }
}
