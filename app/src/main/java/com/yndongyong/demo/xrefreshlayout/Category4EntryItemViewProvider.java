package com.yndongyong.demo.xrefreshlayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yndongyong.widget.multiitem.ItemViewProvider;
import com.yndongyong.widget.multiitem.SimpleViewHolder;

/**
 * Created by dongzhiyong on 2017/6/14.
 */

public class Category4EntryItemViewProvider extends ItemViewProvider<CategoryEntry> {

    @Override
    public int getLayoutId() {
        return R.layout.item_category_1;
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, CategoryEntry entity) {
        ImageView icon = holder.getView(R.id.iv_icon);
        Glide.with(icon).load(entity.getUrl()).into(icon);
        holder.setText(R.id.tv_category_name, entity.getDescription());
        holder.setOnClickListener(R.id.iv_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.getContext(),  "click image position:" + holder.getAdapterPosition() + ";", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
