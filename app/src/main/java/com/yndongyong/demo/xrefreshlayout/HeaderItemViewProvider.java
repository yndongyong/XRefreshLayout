package com.yndongyong.demo.xrefreshlayout;

import com.yndongyong.widget.multiitem.ItemViewProvider;
import com.yndongyong.widget.multiitem.SimpleViewHolder;

/**
 * Created by dongzhiyong on 2017/6/14.
 */

public class HeaderItemViewProvider extends ItemViewProvider<String> {

    @Override
    public int getLayoutId() {
        return R.layout.item_header;
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, String entity) {
        holder.setText(R.id.tv_header_name, entity);
    }
}
