package cn.edu.swufe.eatingbar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingCar extends ListActivity implements Runnable,AdapterView.OnItemClickListener {

    private ArrayList listItems;
    private String TAG = "main";
    private int count = 0;
    int idd2;
    Handler handler;
    MyAdapter1 myAdapter;

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
                    ArrayList<CarItem> retList = (ArrayList<CarItem>) msg.obj;
                    myAdapter = new MyAdapter1(ShoppingCar.this, R.layout.product_item, retList);
                    setListAdapter(myAdapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position=" + position);
        //删除操作
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                //从View中获取选中数据
                TextView idd = (TextView) view.findViewById(R.id.number);
                idd2 = Integer.parseInt((String) idd.getText());
                opendel();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
    }

    public void opendel(){
        Intent conreg = new Intent(this,Del.class);
        conreg.putExtra("id",idd2);
        startActivity(conreg);
    }

    public void openDel(View btn){
        //打开一个页面Activity
        opendel();
    }

    private void initListView() {
        ArrayList listItems = new ArrayList<CarItem>();
        for (int i = 0; i < 10; i++) {
            CarItem item = new CarItem();
            item.setName("正在加载");
            item.setPrice(0);
            item.setIntro("");
            listItems.add(item);
        }
        // 生成适配器的Item和动态数组对应的元素
        myAdapter = new MyAdapter1(this, R.layout.product_item, listItems);
    }

    public void run() {
        Log.i("thread", "run.....");
        ArrayList<CarItem> rateList = new ArrayList<CarItem>();
        Car car = new Car();
        rateList = car.getAll();

        Message msg = handler.obtainMessage();
        msg.what = 7;

        Log.i(TAG, "run: list.size=" + rateList.size());

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread", "sendMessage.....");
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
