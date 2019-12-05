package com.allen.learn.android.tutorial.view.adapater.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.learn.android.tutorial.R;

import java.util.List;

public class      AnimalAdapter extends BaseAdapter {

    private Context context;
    private List<Animal> dataList;
    private int count = 0;

    public AnimalAdapter(Context context, List<Animal> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Animal animal = (Animal) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_view_item,null);
            viewHolder = new ViewHolder();
            viewHolder.animalImageView = view.findViewById(R.id.listViewImageView);
            viewHolder.animalName = view.findViewById(R.id.listViewText);
            view.setTag(viewHolder);
            count++;
            System.out.println("创建了"+count+"次");
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.animalName.setText(animal.getAnimal());
//        viewHolder.animalImageView.setImageResource(animal.getImgId());
        return view;
    }

    private class ViewHolder {
        ImageView animalImageView;
        TextView animalName;
    }
}
