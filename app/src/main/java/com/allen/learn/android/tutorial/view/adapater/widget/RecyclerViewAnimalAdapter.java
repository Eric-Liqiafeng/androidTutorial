package com.allen.learn.android.tutorial.view.adapater.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.learn.android.tutorial.R;

import java.util.List;

public class RecyclerViewAnimalAdapter extends RecyclerView.Adapter<RecyclerViewAnimalAdapter.MyViewHolder> {

    private Context context;
    private List<Animal> animalList;
    private int count;

    public RecyclerViewAnimalAdapter(Context context, List<Animal> animalList) {
        this.context = context;
        this.animalList = animalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.animalName.setText(animalList.get(position).getAnimal());
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView animalImageView;
        TextView animalName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            animalImageView = itemView.findViewById(R.id.listViewImageView);
            animalName = itemView.findViewById(R.id.listViewText);
            System.out.println("创建了"+count+"次");
            count++;
        }
    }
}
