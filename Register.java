package cn.edu.swufe.eatingbar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText logname;
    EditText register_password;
    EditText register_password1;
    EditText address;
    EditText phone;
    EditText realname;
    ImageButton btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();

        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        String text = (String) msg.obj;
                        Toast.makeText(Register.this,text,Toast.LENGTH_LONG).show();
                    }
                };
                String name=logname.getText().toString().trim();
                String pass=register_password.getText().toString().trim();
                String pass1=register_password1.getText().toString().trim();
                String phnumber=phone.getText().toString().trim();
                String addressstr=address.getText().toString().trim();
                String realnamestr=realname.getText().toString().trim();
                Log.i("TAG",name+"_"+pass+"_"+pass1+"_"+addressstr+"_"+phnumber+"_"+realnamestr);
                RegisterHelper registerHelper = new RegisterHelper();
                if(name==null)
                    name="";
                if(pass==null)
                    pass="";
                if(addressstr==null)
                    addressstr="";
                if (!pass.equals(pass1)){
                    Toast.makeText(Register.this,"两次密码不同，注册失败",Toast.LENGTH_LONG).show();
                }
                registerHelper.setLogname(name);
                registerHelper.setPassword(pass);
                registerHelper.setAddress(addressstr);
                registerHelper.setPhone(phnumber);
                registerHelper.setRealname(realnamestr);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String longstr = DBUtils.register(registerHelper);
                        Message message = handler.obtainMessage();
                        if (longstr != null){
                            message.obj = longstr;
                            if (longstr == "注册成功"){
                                openlogin();
                            }
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }

        });
    }


    public void openLogin(View btn){
        //打开一个页面Activity
        openlogin();
    }

    public void openlogin(){
        Intent login = new Intent(this,Login.class);

        //startActivity(config);
        startActivityForResult(login,2);
    }


    private void findViews() {
        logname=(EditText) findViewById(R.id.logname);
        register_password=(EditText) findViewById(R.id.register_password);
        register_password1=(EditText) findViewById(R.id.register_password1);
        phone=(EditText) findViewById(R.id.phone);
        address=(EditText)findViewById(R.id.address);
        realname=(EditText)findViewById(R.id.realname);
        btn_register=(ImageButton) findViewById(R.id.btn_register);
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
