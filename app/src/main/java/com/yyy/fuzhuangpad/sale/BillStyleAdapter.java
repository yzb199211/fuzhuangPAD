package com.yyy.fuzhuangpad.sale;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.util.ImageLoaderUtil;

import java.util.List;

public class BillStyleAdapter extends RecyclerView.Adapter<BillStyleAdapter.VH> {
    Context context;
    List<BillStyle> list;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BillStyleAdapter(Context context, List<BillStyle> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_style, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setIsRecyclable(false);
        BillStyle style = list.get(position);
        holder.tvNo.setText(style.getsStyleNo());
        holder.tvClass.setText(style.getsClassName());
//        holder.tvPrice.setText("零售：" + style.getfCostPrice());
        SpannableString spannableString = new SpannableString("零售：¥" + style.getfCostPrice());
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)), 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPrice.setText(spannableString);
        ImageLoaderUtil.loadDrawableImg(holder.ivLogo, R.mipmap.default_style);
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        ImageView ivLogo;
        TextView tvNo;
        TextView tvClass;
        TextView tvPrice;
        ImageView ivChecked;

        public VH(View v) {
            super(v);
            ivLogo = v.findViewById(R.id.iv_logo);
            tvNo = v.findViewById(R.id.tv_style_no);
            tvClass = v.findViewById(R.id.tv_style_class);
            tvPrice = v.findViewById(R.id.tv_style_price);
            ivChecked = v.findViewById(R.id.iv_checked);
        }
    }
}
