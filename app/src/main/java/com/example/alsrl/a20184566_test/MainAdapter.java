package com.example.alsrl.a20184566_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainAdapter extends BaseAdapter {
    ArrayList<ListEntity> arItem;

    Context context;
    LayoutInflater inflater;

    class ViewHolder {
        public ImageView ivPhoto;
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvCount;
        public ImageView tvBarcode;
    }

    public MainAdapter(Context context) {
        this.context = context;
        arItem = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<ListEntity> getArItem() {
        return arItem;
    }

    public void setArItem(ArrayList<ListEntity> arItem) {
        this.arItem = arItem;
    }

    @Override
    public int getCount() {
        return arItem.size();
    }

    @Override
    public ListEntity getItem(int position) {
        return arItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_main,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tvContent);
            viewHolder.tvCount = (TextView)convertView.findViewById(R.id.tvCount);
            viewHolder.tvBarcode = (ImageView)convertView.findViewById(R.id.tvBarcode);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ListEntity item = getItem(position);
        String imageUrl = Config.HOME_URL+ "ImageView?id=" + item.getImage_id();
        Picasso.with(context)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(viewHolder.ivPhoto);
        viewHolder.tvTitle.setText(item.getName());
        viewHolder.tvContent.setText(item.getPrice());
        viewHolder.tvCount.setText(item.getCount());
        Bitmap barcode = createBarcode(item.getBarcode());
        viewHolder.tvBarcode.setImageBitmap(barcode);
        viewHolder.tvBarcode.invalidate();

        return convertView;
    }

    public Bitmap createBarcode(String code){
        Bitmap bitmap = null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int WIDTH = 250;
            final int HEIGHT = 50;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.EAN_13,WIDTH,HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH,HEIGHT,Bitmap.Config.ARGB_8888);
            for(int i = 0; i < WIDTH; i++){
                for(int j = 0; j < HEIGHT; j++){
                    bitmap.setPixel(i,j,bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
