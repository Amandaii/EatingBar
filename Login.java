package cn.edu.swufe.eatingbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private CustomVideoView videoview;
    private String name = "";
    private EditText username;
    private EditText password;
    private ImageButton login;
    private TextView no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        //加载数据
        initView();
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            Toast.makeText(Login.this,text,Toast.LENGTH_LONG).show();
        }
    };

    private void findViews() {
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        login=(ImageButton) findViewById(R.id.btn_login);
        no=(TextView) findViewById(R.id.no);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.btn_login){
                    name=username.getText().toString();
                    System.out.println(name);
                    String pass=password.getText().toString();
                    System.out.println(pass);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String longstr = DBUtils.login(name,pass);
                            Message message = handler.obtainMessage();
                            if (longstr != null){
                                message.obj = longstr;
                                if (longstr == "用户登录成功"){
                                    LoginState loginState = new LoginState();
                                    loginState.setUsername(name);
                                    openconfig();
                                }else if (longstr == "管理员登录成功"){
                                    openreg();
                                }
                            }
                            handler.sendMessage(message);
                        }
                    }).start();

                    }


            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.no){
                    Log.i(TAG, "onClick: ");
                    openlogin();
                }
            }
        });
    }


    private void initView() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    public void openOne(View btn){
        //打开一个页面Activity
        openconfig();
    }

    public void openconfig(){
        Intent config = new Intent(this,MainActivity.class);
        LoginState l = new LoginState();
        l.setUsername(name);

        //startActivity(config);
        startActivityForResult(config,1);
    }
    public void openreg(){
        Intent conreg = new Intent(this,Main2Activity.class);
        LoginState l = new LoginState();
        l.setUsername(name);
        startActivity(conreg);
    }

    public void openLogin(View btn){
        //打开一个页面Activity
        openlogin();
    }

    public void openlogin(){
        Intent login = new Intent(this,Register.class);
        login.putExtra("username",name);

        Log.i(TAG, "openOne: username"+name);

        //startActivity(config);
        startActivityForResult(login,2);
    }


    //返回重启加载
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }

}
