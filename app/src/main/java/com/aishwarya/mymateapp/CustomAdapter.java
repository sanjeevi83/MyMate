package com.aishwarya.mymateapp;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList note_id, note_title, note_subtitle, note_description;
//    private ArrayList<byte[]> note_images;
    int position;

    CustomAdapter(Context context,
                  ArrayList note_id,
                  ArrayList note_title,
                  ArrayList note_subtitle,
                  ArrayList note_description
//                  ArrayList<byte[]> note_images
    ){
        this.context = context;
        this.note_id = note_id;
        this.note_title= note_title;
        this.note_subtitle =note_subtitle;
        this.note_description =note_description;
//        this.note_images = note_images;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // Set the data to the views in the ViewHolder
        holder.txtnoteId.setText(valueOf(note_id.get(position)));
        holder.txtnoteTitle.setText(valueOf(note_title.get(position)));
        holder.txtnoteSubtitle.setText(valueOf(note_subtitle.get(position)));


        // Set the click listener for the main layout
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handle the click event
                Intent intent = new Intent(context, UpdatenotePage.class);
                intent.putExtra("id", String.valueOf(note_id.get(position)));
                intent.putExtra("title", String.valueOf(note_title.get(position)));
                intent.putExtra("subtitle", String.valueOf(note_subtitle.get(position)));
                intent.putExtra("description", String.valueOf(note_description.get(position)));

//                intent.putExtra("image", note_images.get(position));
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {

        return note_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView txtnoteId, txtnoteTitle, txtnoteSubtitle;
        LinearLayout mainlayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnoteId = itemView.findViewById(R.id.txtnoteId);
            txtnoteTitle = itemView.findViewById(R.id.txtnoteTitle);
            txtnoteSubtitle =itemView.findViewById(R.id.txtnoteSubtitle);
            mainlayout = itemView.findViewById(R.id.mainLayout);
//            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
