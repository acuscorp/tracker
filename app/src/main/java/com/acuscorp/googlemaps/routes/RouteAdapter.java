package com.acuscorp.googlemaps.routes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.googlemaps.R;

import java.util.List;

class RouteAdapter extends ListAdapter<Route,RouteAdapter.RouteHolder> {
  private OnItemClickListener listener;
  protected RouteAdapter() {
    super(diffCallback);


  }
  public static final DiffUtil.ItemCallback<Route> diffCallback = new DiffUtil.ItemCallback<Route>() {
    @Override
    public boolean areItemsTheSame(@NonNull Route oldItem, @NonNull Route newItem) {
      return oldItem.getId()==newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Route oldItem, @NonNull Route newItem) {
      return oldItem.getRouteName().equals(newItem.getRouteName());
    }
  };
  @NonNull
  @Override
  public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_route,parent,false);

    return new RouteHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
    holder.routeName.setText(getItem(position).getRouteName());
  }

  public void submitRoute(List<Route> routes) {
    submitList(routes);
  }

  public class RouteHolder extends RecyclerView.ViewHolder {
    TextView routeName;
    public RouteHolder(@NonNull View itemView) {
      super(itemView);
      routeName = itemView.findViewById(R.id.tv_route_name);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if(listener!= null && position!= RecyclerView.NO_POSITION)
          {
            listener.onItemClick(getItem(position));
          }
        }
      });
    }
  }
  public void setOnItemClickListener(OnItemClickListener listener){
    this.listener= listener;
  }
  public interface OnItemClickListener {
    void onItemClick(Route route);
  }

}
