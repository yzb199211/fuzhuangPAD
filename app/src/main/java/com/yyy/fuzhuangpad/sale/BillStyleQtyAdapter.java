package com.yyy.fuzhuangpad.sale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.NumSelectView;
import com.yyy.fuzhuangpad.view.sale.OnQtyChange;

import java.util.List;

public class BillStyleQtyAdapter extends RecyclerView.Adapter<BillStyleQtyAdapter.VH> {
    private Context context;
    private List<BillStyleQty> list;

    public BillStyleQtyAdapter(Context context, List<BillStyleQty> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_style_qty2, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setIsRecyclable(false);
        BillStyleQty item = list.get(position);
        holder.tvColor.setText(item.getsColorName());
        holder.tvSize.setText(item.getsSizeName());
        holder.tvStorage.setText(item.getiQty()+"");
        holder.nsvQty.setNum(item.getNum());
        holder.nsvQty.setOnQtyChange(new OnQtyChange() {
            @Override
            public void onQty(int qty) {
                item.setNum(qty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvColor;
        TextView tvSize;
        TextView tvStorage;
        NumSelectView nsvQty;

        public VH(View v) {
            super(v);
            tvColor = v.findViewById(R.id.tv_color);
            tvSize = v.findViewById(R.id.tv_size);
            tvStorage = v.findViewById(R.id.tv_storage);
            nsvQty = v.findViewById(R.id.nsv_qty);

        }
    }
}
