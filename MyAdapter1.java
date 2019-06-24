package cn.edu.swufe.eatingbar;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter1 extends ArrayAdapter {

    private static final String TAG = "MyAdapter";

    public MyAdapter1(Context context, int resource, ArrayList<CarItem> helpers) {
        super(context, resource, helpers);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.product_item,parent,false);
        }

        CarItem item = (CarItem) getItem(position);

        ImageView picture = (ImageView)itemView.findViewById(R.id.picture);
        TextView productname = (TextView) itemView.findViewById(R.id.goodsname);
        TextView description = (TextView) itemView.findViewById(R.id.description);
        TextView price = (TextView)itemView.findViewById(R.id.price);
        TextView idd = (TextView)itemView.findViewById(R.id.number);

        if(item.getPhoto()!=null){
            Log.i(TAG, "getView: photo not null" + item.getName() + item.getPhoto().length);
            byte[] b = item.getPhoto();
            picture.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length, null));
        }else{
            Log.i(TAG, "getView: photo is null" + item.getName());
        }

        productname.setText(item.getName());
        description.setText(item.getIntro());
        price.setText(String.valueOf(item.getPrice()));
        idd.setText(String.valueOf(item.getNumber()));
        Log.i(TAG, "getView: number is " + item.getNumber());

        return itemView;
    }
}
