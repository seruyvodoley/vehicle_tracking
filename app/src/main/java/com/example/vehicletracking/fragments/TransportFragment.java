package com.example.vehicletracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicletracking.R;
import com.example.vehicletracking.models.CarModel;
import com.example.vehicletracking.utils.CarAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TransportFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CarModel> carList = new ArrayList<>();
    private CarAdapter carAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport, container, false);

        FloatingActionButton btnAddCar = view.findViewById(R.id.fab_add_car);
        recyclerView = view.findViewById(R.id.recyclerViewGarage);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carAdapter = new CarAdapter(carList, getContext(), car -> {
            MapFragment mapFragment = new MapFragment();

            Bundle args = new Bundle();
            args.putString("latitude", String.valueOf(car.getLatitude()));
            args.putString("longitude", String.valueOf(car.getLongitude()));
            args.putString("carName", car.getName());
            mapFragment.setArguments(args);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        recyclerView.setAdapter(carAdapter);

        btnAddCar.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new com.example.vehicletracking.presentation.addcar.AddCarFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        fetchCarsFromFirestore();

        return view;
    }

    private void fetchCarsFromFirestore() {
        FirebaseFirestore.getInstance().collection("CarInfo")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    carList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        CarModel car = doc.toObject(CarModel.class);
                        carList.add(car);
                    }
                    carAdapter.notifyDataSetChanged();
                });
    }
}
