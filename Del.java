package cn.edu.swufe.eatingbar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Del extends AppCompatActivity implements Runnable{
    ProductItem car;
    ImageButton back;
    TextView fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del);
        Thread t = new Thread(this); // 创建新线程
        t.start();
        findViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencar();
            }
        });
    }

    private void findViews() {
        back =(ImageButton) findViewById(R.id.back);
        fh = (TextView) findViewById(R.id.fh);
    }

    public void openCar(View btn){
        //打开一个页面Activity
        opencar();
    }

    public void opencar(){
        Intent login = new Intent(this,ShoppingCar.class);
        //startActivity(config);
        startActivityForResult(login,2);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            Toast.makeText(Del.this,text,Toast.LENGTH_LONG).show();
        }
    };

    public void run(){
        int idd2 = getIntent().getIntExtra("id",0);
        Car car1 = new Car();
        String str = car1.deleteCar(idd2);

        Message msg = handler.obtainMessage();

        msg.obj = str;
        handler.sendMessage(msg);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
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
            Toast.makeText(Del.this,"退出登录",Toast.LENGTH_LONG).show();
        }else if (item.getItemId()==R.id.login){
            Intent login = new Intent(this,Login.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        findViews();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
    }
}
