package com.acuscorp.googlemaps.routes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.googlemaps.R;
import com.acuscorp.googlemaps.main.MainActivity;
import com.acuscorp.googlemaps.util.Utils;

import java.util.List;

public class SelectRouteActivity extends AppCompatActivity {

  private static final String TAG = "SelectRouteActivity";
  private LinearLayoutManager layoutManager;
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private boolean isPhone;
  private RouteAdapter routeAdapter;
  private RouteViewModel routeViewModel;
  private int PAGE_SIZE;
  private int spanCount = 4;
  private String username;
  private int id;
  private final Utils utils = Utils.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_route);
    Intent intent = getIntent();
    username = intent.getStringExtra(utils.getEXTRA_USER());
    id = intent.getIntExtra(utils.getEXTRA_USER_ID(),0);
    getUIElements();

  }
  private void getUIElements() {

    recyclerView = findViewById(R.id.recyclerview_card_view_routes);
    progressBar = findViewById(R.id.fabProgress);
    progressBar.setVisibility(ProgressBar.VISIBLE);
    setLayoutManager();
    initViewModel();
    onItemClickListener();
  }

  private void onItemClickListener() {
    routeAdapter.setOnItemClickListener(new RouteAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(Route route) {
        Intent intent = new Intent(SelectRouteActivity.this, MainActivity.class);
        intent.putExtra(utils.getEXTRA_USER(),username);
        intent.putExtra(utils.getEXTRA_USER_ID(),id);
        intent.putExtra(utils.getEXTRA_OBJECT_ROUTE(),route);
        startActivity(intent);
      }
    });
  }

  private void setLayoutManager() {
    Display display = getWindowManager().getDefaultDisplay();

    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;
    if (width > 1080) {
      PAGE_SIZE=20;

      layoutManager = new GridLayoutManager(this, spanCount + 2);

    } else {
      PAGE_SIZE=10;
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      layoutManager = new GridLayoutManager(this, spanCount);
      isPhone = true;

    }
    routeAdapter = new RouteAdapter();
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(routeAdapter);
  }



  private void initViewModel() {

    routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
    routeViewModel.getAllRoutes().observe(this, new Observer<List<Route>>() {
      @Override
      public void onChanged(List<Route> routes) {
        Log.d(TAG, "onChanged: ");
        routeAdapter.submitRoute(routes);
        progressBar.setVisibility(ProgressBar.GONE);

      }
    });
    routeViewModel.setRoutesByUser(username);
  }
}
