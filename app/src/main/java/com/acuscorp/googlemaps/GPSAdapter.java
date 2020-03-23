package com.acuscorp.googlemaps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Noé Adrian Acuna Prado on 12/02/2020.
 *
 * acuscorp@gmail.com
 */
//public class GPSAdapter extends RecyclerView.Adapter<GPSAdapter.GPSHolder> {
public class GPSAdapter extends ListAdapter<GPS, GPSAdapter.GPSHolder> {

    public GPSAdapter() {
        super(DIFF_CALLBACK);

    }
    private static final DiffUtil.ItemCallback<GPS> DIFF_CALLBACK = new DiffUtil.ItemCallback<GPS>() {
        @Override
        public boolean areItemsTheSame(@NonNull GPS oldItem, @NonNull GPS newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GPS oldItem, @NonNull GPS newItem) {
            return oldItem.getGpsName().equals(newItem.getGpsName()) &&
                    oldItem.getLatitude() == newItem.getLatitude() &&
                    oldItem.getLongitude() == newItem.getLongitude();
        }
    };

//    private List<Note> notes = new ArrayList<>();
    private  OnItemClickListener listener;
    @Override
    public GPSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
////                .inflate(R.layout.gps_data_items,parent,false);

//        return new GPSHolder(itemView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GPSHolder holder, int position) {
        GPS currentGPS = getItem(position);
        holder.textViewRute.setText(currentGPS.getGpsName());
        holder.textViewLatitude.setText(String.valueOf(currentGPS.getLatitude()));
        holder.textViewLongitude.setText(String.valueOf(currentGPS.getLongitude()));
    }



    class GPSHolder extends RecyclerView.ViewHolder{
        private TextView textViewRute, textViewLatitude, textViewLongitude;

        public GPSHolder(@NonNull View itemView) {
            super(itemView);
//            textViewRute = itemView.findViewById(R.id.tv_route);
//            textViewLatitude = itemView.findViewById(R.id.tv_longitude);
//            textViewLongitude = itemView.findViewById(R.id.tv_latitude);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position  = getAdapterPosition();
                   if(listener!=null && position != RecyclerView.NO_POSITION)
                   {
                       listener.onItemClick(getItem(position));
                   }
               }
           });

        }
    }

    public GPS getGPSAt(int position){
        return getItem(position);
    }
    public interface OnItemClickListener{
        void onItemClick(GPS note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }





}
