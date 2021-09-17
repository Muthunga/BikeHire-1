package com.example.bikehire;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

    public class Adapter extends RecyclerView.Adapter{

        List<Modelll> list;

        public Adapter(List<Modelll> list) {
            this.list = list;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1,parent,false);

            ViewHolder viewHolder=new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ViewHolder viewHolder=(ViewHolder)holder;
            Modelll stationData=list.get(position);
            viewHolder.name.setText(stationData.getName());
            viewHolder.number.setText(stationData.getNumber().toString());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView name,number;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name=itemView.findViewById(R.id.station_name);
                number=itemView.findViewById(R.id.bikes);
            }
        }
    }


