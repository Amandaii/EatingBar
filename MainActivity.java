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

public class MainActivity extends ListActivity implements Runnable, AdapterView.OnItemClickListener {

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
                    myAdapter = new MyAdapter(MainActivity.this, R.layout.product_item, retList);
                    setListAdapter(myAdapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
    }

    private void initListView() {
        ArrayList listItems = new ArrayList<ProductItem>();
        for (int i = 0; i < 10; i++) {
            ProductItem item = new ProductItem();
            item.setName("正在加载");
            item.setPrice("");
            item.setIntro("");
            listItems.add(item);
        }
        // 生成适配器的Item和动态数组对应的元素
        myAdapter = new MyAdapter(this, R.layout.product_item, listItems);
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

        //从ListView中获取选中数据
        /**HashMap<String,String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);**/

        //从View中获取选中数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否加入购物车").setPositiveButton("是",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                ProductItem car = new ProductItem();
                TextView idd = (TextView) view.findViewById(R.id.number);
                int idd2 = Integer.parseInt((String) idd.getText());
                Intent cart = new Intent(MainActivity.this,Cart.class);
                cart.putExtra("id",idd2);
                startActivity(cart);

                /**Products products = new Products();
                car = products.getInfoByName(idd2);
                Car orderform = new Car();
                String str = orderform.addProduct(car);
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();**/
            }

        }).setNegativeButton("否",null);
        builder.create().show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.first){
            Intent first = new Intent(this,MainActivity.class);
            startActivity(first);
        }else if (item.getItemId()==R.id.car){
            //打开列表窗口
            LoginState lo = new LoginState();
            Log.i(TAG, "onOptionsItemSelected: logname = " + lo.getUsername());
            if(lo.getUsername().length()>0){
            Intent car = new Intent(this,ShoppingCar.class);
            startActivity(car);}
            else {Toast.makeText(this,"请先登录",Toast.LENGTH_LONG).show();}
        }else if (item.getItemId()==R.id.logout){
            LoginState state = new LoginState();
            state.setUsername("");
            Toast.makeText(MainActivity.this,"退出登录",Toast.LENGTH_LONG).show();
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
