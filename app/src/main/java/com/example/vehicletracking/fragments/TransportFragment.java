package com.example.vehicletracking.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.vehicletracking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport, container, false);

        FloatingActionButton btnAddCar = view.findViewById(R.id.fab_add_car);

        btnAddCar.setOnClickListener(v -> {
            // Заменяем текущий фрагмент на AddCarFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new AddCar_Fragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
