package cn.edu.swufe.eatingbar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main2Activity extends ListActivity implements Runnable, AdapterView.OnItemClickListener{

    private ArrayList listItems;
    private String TAG = "main";
    private int count = 0;
    Handler handler;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        initListView();
        //getListView().setOnItemClickListener(this);
        this.setListAdapter(myAdapter);
        Thread t = new Thread(this); // 创建新线程
        t.start(); // 开启线程

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    ArrayList<ProductItem> retList = (ArrayList<ProductItem>) msg.obj;
                    myAdapter = new MyAdapter(Main2Activity.this, R.layout.product_item, retList);
                    setListAdapter(myAdapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
        LoginState ll = new LoginState();
        String  lll = ll.getUsername();
        if (lll != null){
            getListView().setOnItemClickListener(this);
        }else {
            Toast.makeText(this, "请管理员登录",Toast.LENGTH_LONG).show();
        }

    }

    private void initListView() {
        ArrayList listItems = new ArrayList<ProductItem>();
        for (int i = 0; i < 10; i++) {
            ProductItem item = new ProductItem();
            item.setName("Name" + i);
            item.setPrice("");
            item.setIntro("");
            listItems.add(item);
        }
        // 生成适配器的Item和动态数组对应的元素
        myAdapter = new MyAdapter(this, R.layout.product_item, listItems);
        getListView().setOnItemClickListener(this);
    }

    public void run() {
        Log.i("thread", "run.....");
        ArrayList<ProductItem> rateList = new ArrayList<ProductItem>();
        Products products = new Products();
        rateList = products.getAll();

        Message msg = handler.obtainMessage();
        msg.what = 7;

        Log.i(TAG, "run: list.size=" + rateList.size());

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread", "sendMessage.....");
    }

    public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

        Log.i(TAG, "onItemClick: parent=" + parent);
        Log.i(TAG, "onItemClick: view=" + view);
        Log.i(TAG, "onItemClick: position=" + position);
        Log.i(TAG, "onItemClick: id=" + id);

        //从View中获取选中数据
        TextView idd = (TextView) view.findViewById(R.id.number);
        int idd2 = Integer.parseInt((String) idd.getText());

        //打开新的页面，传入参数
        Intent update = new Intent(this,Update.class);
        update.putExtra("id",idd2);
        startActivity(update);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.first){
            Intent first = new Intent(this,MainActivity.class);
            startActivity(first);
        }else if (item.getItemId()==R.id.car){
            //打开列表窗口
            Intent car = new Intent(this,ShoppingCar.class);
            startActivity(car);
        }else if (item.getItemId()==R.id.logout){
            LoginState state = new LoginState();
            state.setUsername("");
            Toast.makeText(Main2Activity.this,"退出登录",Toast.LENGTH_LONG).show();
        }else if (item.getItemId()==R.id.login){
            Intent login = new Intent(this,Login.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initListView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
    }


}
