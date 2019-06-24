package cn.edu.swufe.eatingbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Update extends AppCompatActivity implements Runnable{
    private static final int WRITE_PERMISSION = 0x01;

    private final String TAG="upproduct";
    ProductItem itt;
    byte[] by;
    private static final int VIDEO_WIDTH = 160;
    private static final int VIDEO_HEIGHT = 200;
    private RadioGroup uptype;
    private RadioButton upfruit;
    private RadioButton upsx;
    private RadioButton upls;
    private ImageButton upProduct;
    private EditText upproductname;
    private EditText upproductintro;
    private ImageButton upproductphoto;
    private EditText upproductprice;
    private Button del;
    ProductItem car;
    String str; //存放点击的按钮的值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Thread t = new Thread(this); // 创建新线程
        t.start();
        findViews();


        upproductphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itt.setPhoto(null);
                //一个重定向打开系统图库
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String ret = (String) msg.obj;
                        Toast.makeText(Update.this, ret, Toast.LENGTH_LONG).show();
                        }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        Products pp = new Products();
                        int idd2 = getIntent().getIntExtra("id", 0);
                        String ret = pp.deleteProduct(idd2);
                        if (ret == "删除成功"){
                            openmain2();
                        }
                        message.obj=ret;
                        handler.sendMessage(message);
                        }
                }).start();
            }
        });
        upProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        String text = (String) msg.obj;
                        Toast.makeText(Update.this,text,Toast.LENGTH_LONG).show();
                    }
                };
                String pro_name=upproductname.getText().toString().trim();
                String pro_intro=upproductintro.getText().toString().trim();
                String pro_price=upproductprice.getText().toString().trim();
                Log.i(TAG,pro_name+"_"+pro_intro+"_"+pro_price);
                final ProductItem helper = new ProductItem();
                int iddd = getIntent().getIntExtra("id",0);
                helper.setNumber(iddd);
                helper.setName(pro_name);
                helper.setIntro(pro_intro);
                helper.setPrice(pro_price);
                for (int i = 0;i<uptype.getChildCount();i++){
                    RadioButton r = (RadioButton)uptype.getChildAt(i);//根据索引获取单选按钮
                    if (r.isChecked()){ //判断按钮是否被选中
                        str = r.getText().toString().trim();//获取被选中的单选按钮的值
                        helper.setType(str);
                        break;
                    }
                }
                if (by !=null){
                    helper.setPhoto(by);
                }
                else {
                    Log.i(TAG, "onClick: ERR : 没有获取到图片或者未添加图片" );
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String longstr = Products.updateProduct(helper);
                        Message message = handler.obtainMessage();
                        if (longstr != null){
                            message.obj = longstr;
                            if (longstr == "修改商品成功"){
                                openmain2();
                            }
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager,menu);
        return true;
    }

    public void openMain2(View btn){
        //打开一个页面Activity
        openmain2();
    }

    public void openmain2(){
        Intent login = new Intent(this,Main2Activity.class);

        //startActivity(config);
        startActivityForResult(login,2);
    }

    private void findViews() {
        upproductname=(EditText) findViewById(R.id.upproductname);
        upproductintro=(EditText) findViewById(R.id.upproductintro);
        upproductphoto=(ImageButton) findViewById(R.id.upproductphoto);
        upproductprice=(EditText)findViewById(R.id.upproductprice);
        uptype = (RadioGroup) findViewById(R.id.id_upradiogroup);
        upfruit = (RadioButton) findViewById(R.id.upfruit);
        upsx = (RadioButton) findViewById(R.id.upsx);
        upls = (RadioButton) findViewById(R.id.upls);
        upProduct = (ImageButton) findViewById(R.id.btn_upproduct);
        del = (Button) findViewById(R.id.delete);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            itt = (ProductItem) msg.obj;
            upproductname.setText(itt.getName());
            upproductintro.setText(itt.getIntro());
            upproductprice.setText(itt.getPrice());
            Log.i(TAG, "handleMessage:  " + itt.getName());
        }
    };

    @Override
    public void run() {
        int idd2 = getIntent().getIntExtra("id",0);
        Products products = new Products();
        car = products.getInfoByName(idd2);

        Message msg = handler.obtainMessage();
        msg.what=7;

        msg.obj = car;
        handler.sendMessage(msg);

        Log.i(TAG, "run:  " + msg.obj.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.look){
            Intent first = new Intent(this,Main2Activity.class);
            startActivity(first);
        }else if (item.getItemId()==R.id.add){
            //打开列表窗口
            Intent car = new Intent(this,AddProduct.class);
            startActivity(car);
        }else if (item.getItemId()==R.id.login1){
            Intent login = new Intent(this,Login.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, "已经得到bitmap");

            //Image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            //从手机选择图片插入数据库

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不申请内存 计算图片比例
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            options.inJustDecodeBounds = false; //设为 false  申请内存
            // 计算缩放比
            int h = options.outHeight;
            int w = options.outWidth;
            int beWidth  = w / VIDEO_WIDTH;
            int beHeight = h / VIDEO_HEIGHT;
            int be = 4;
            if (beWidth < beHeight && beHeight >= 1) {
                be = beHeight;
            }
            if (beHeight< beWidth  && beWidth >= 1) {
                be = beWidth;
            }

            if (be <= 0) {
                be = 1;
            } else if (be > 3) {
                be = 3;
            }

            options.inSampleSize = be;
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            options.inPurgeable = true;
            options.inInputShareable = true;
            try {
                // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
                bitmap = BitmapFactory.decodeFile(picturePath);

                // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
                //bitmap = ThumbnailUtils.extractThumbnail(bitmap, VIDEO_WIDTH, VIDEO_HEIGHT,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            } catch (OutOfMemoryError e) {
                System.gc();
                bitmap = null;
            }
            Log.i(TAG, "onActivityResult: " + picturePath);
            Log.i(TAG, "onActivityResult: bitmap" + bitmap);
            by = bitmapToBytes(bitmap);
        }
    }

    public static byte[] bitmapToBytes(Bitmap bitmap){
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);//除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION ){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Write Permission Failed");
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION );
        }
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


