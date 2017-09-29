package com.yndongyong.demo.xrefreshlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yndongyong.widget.multiitem.Items;
import com.yndongyong.widget.multiitem.SimpleAdapter;
import com.yndongyong.xrefreshlayout.XRefreshLayout;


public class MainActivity extends AppCompatActivity implements XRefreshLayout.RefreshListener {

    private RecyclerView recyclerview;
    private Items items = new Items();
    private XRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        items.add("头部1");
        items.add(new CategoryEntry("http://pic.58pic.com/58pic/14/27/45/71r58PICmDM_1024.jpg", "风景图片1"));
        items.add(new CategoryEntry("http://img0.imgtn.bdimg.com/it/u=1610953019,3012342313&fm=214&gp=0.jpg", "风景图片2"));
        items.add(new CategoryEntry("http://scimg.jb51.net/allimg/150819/14-150QZ9194K27.jpg", "风景图片3"));


        items.add("头部2");
        items.add(new CategoryEntry("http://pic.58pic.com/58pic/14/27/45/71r58PICmDM_1024.jpg", "风景图片4"));
        items.add(new CategoryEntry("http://img0.imgtn.bdimg.com/it/u=1610953019,3012342313&fm=214&gp=0.jpg", "风景图片5"));
        items.add(new CategoryEntry("http://scimg.jb51.net/allimg/150819/14-150QZ9194K27.jpg", "风景图片6"));


        items.add("头部3");
        items.add(new CategoryEntry("http://pic.58pic.com/58pic/14/27/45/71r58PICmDM_1024.jpg", "风景图片4"));
        items.add(new CategoryEntry("http://img0.imgtn.bdimg.com/it/u=1610953019,3012342313&fm=214&gp=0.jpg", "风景图片5"));
        items.add(new CategoryEntry("http://scimg.jb51.net/allimg/150819/14-150QZ9194K27.jpg", "风景图片6"));


        items.add("头部4");
        items.add(new CategoryEntry("http://pic.58pic.com/58pic/14/27/45/71r58PICmDM_1024.jpg",
                "风景图片4", 2));
        items.add(new CategoryEntry("http://img0.imgtn.bdimg.com/it/u=1610953019,3012342313&fm=214&gp=0.jpg",
                "风景图片5", 2));
        items.add(new CategoryEntry("http://scimg.jb51.net/allimg/150819/14-150QZ9194K27.jpg",
                "风景图片6", 2));

        items.add("头部5");
        items.add(new CategoryEntry("http://pic.58pic.com/58pic/14/27/45/71r58PICmDM_1024.jpg", "风景图片4"));
        items.add(new CategoryEntry("http://img0.imgtn.bdimg.com/it/u=1610953019,3012342313&fm=214&gp=0.jpg", "风景图片5"));
        items.add(new CategoryEntry("http://scimg.jb51.net/allimg/150819/14-150QZ9194K27.jpg", "风景图片6"));

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        SimpleAdapter simpleAdapter = SimpleAdapter.create(this)
                .addNewData(items)
                .register(CategoryEntry.class, new Category4EntryItemViewProvider())
                .register(String.class, new HeaderItemViewProvider())
                .attachToRecyclerView(recyclerview);
        simpleAdapter.notifyDataSetChanged();

        refresh_layout = (XRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "开始刷新。。。", Toast.LENGTH_SHORT).show();
        refresh_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_layout.refreshComplete();
            }
        },2000);
    }
}
