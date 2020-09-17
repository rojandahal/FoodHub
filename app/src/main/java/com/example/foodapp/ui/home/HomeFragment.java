package com.example.foodapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.model.PromoDish;
import com.example.foodapp.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ru.nikartm.support.ImageBadgeView;

public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";

    private HomeViewAdapter homeViewAdapter;
    private ImageBadgeView imageBadgeView;
    private RecyclerView recyclerView;

    private List<PromoDish> promoDishes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        promoDishes = new ArrayList<>();
        promoDishes.add(new PromoDish(R.drawable.fruits,"Fruit salad","It is a good Fruit Salad", "200"));
        promoDishes.add(new PromoDish(R.drawable.fruits,"Chicken salad","It is a good Chicken Salad.", "200"));
        promoDishes.add(new PromoDish(R.drawable.fruits,"Momo ","It is best Momo", "200"));
        homeViewAdapter = new HomeViewAdapter(promoDishes);

        recyclerView = root.findViewById(R.id.todayPromoRecyclerView);
        LinearLayoutManager horizontalLinearLayout = new LinearLayoutManager(getActivity());
        horizontalLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(horizontalLinearLayout);
        recyclerView.setAdapter(homeViewAdapter);
        homeViewAdapter.notifyDataSetChanged();

        Log.d(TAG, "onCreateView: " + promoDishes);
        imageBadgeView = root.findViewById(R.id.notification_badge);
        imageBadgeView.setBadgeValue(1)
                .setRoundBadge(true)
                .setRoundBadge(true);
        imageBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBadgeView.setBadgeValue(0);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return root;
    }

}