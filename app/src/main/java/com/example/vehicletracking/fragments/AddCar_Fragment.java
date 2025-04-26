package com.example.vehicletracking.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehicletracking.R;
import com.example.vehicletracking.modelview.AddCarViewModel;
import com.example.vehicletracking.utils.LocationAdapter;
import com.google.android.material.card.MaterialCardView;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import java.io.IOException;

public class AddCar_Fragment extends Fragment {

    private static final int REQUEST_LOCATION = 1;

    private EditText carName, carModel, carNumber;
    private ImageView carImageView;
    private TextView carLocation;
    private Switch getLocationSwitch;
    private Button addCarButton;
    private Uri imageUri;
    private String latitude, longitude;
    private Bitmap bitmap;

    private LocationManager locationManager;
    private KProgressHUD progressHUD;
    private MaterialCardView SelectPhoto;

    private AddCarViewModel viewModel;
    private LocationAdapter locationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcar_info, container, false);

        viewModel = new ViewModelProvider(this).get(AddCarViewModel.class);
        locationAdapter = new LocationAdapter(requireContext());

        carName = view.findViewById(R.id.carName);
        carModel = view.findViewById(R.id.carModel);
        carNumber = view.findViewById(R.id.carNumber);
        carImageView = view.findViewById(R.id.carImage);
        carLocation = view.findViewById(R.id.carLocation);
        getLocationSwitch = view.findViewById(R.id.switcher);
        addCarButton = view.findViewById(R.id.addCar);
        SelectPhoto = view.findViewById(R.id.SelectPhoto);

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // LiveData observers
        viewModel.getIsUploading().observe(getViewLifecycleOwner(), uploading -> {
            if (uploading) {
                if (progressHUD == null) {
                    progressHUD = KProgressHUD.create(requireContext())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Сохранение...")
                            .setCancellable(false)
                            .show();
                } else {
                    progressHUD.show();
                }
            } else {
                if (progressHUD != null && progressHUD.isShowing()) {
                    progressHUD.dismiss();
                }
            }
        });

        viewModel.getIsSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Машина успешно добавлена", Toast.LENGTH_SHORT).show();
                carName.setText("");
                carModel.setText("");
                carNumber.setText("");
                carImageView.setImageResource(R.drawable.car);
                carLocation.setText("Включить текущее местоположение");
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Ошибка: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        getLocationSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkLocationPermission();
            } else {
                carLocation.setText("Включить текущее местоположение");
            }
        });

        addCarButton.setOnClickListener(v -> {
            String name = carName.getText().toString().trim();
            String model = carModel.getText().toString().trim();
            String number = carNumber.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(model) || TextUtils.isEmpty(number)) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri != null) {
                viewModel.uploadImageAndCar(imageUri, name, model, number, latitude, longitude);
            } else {
                viewModel.uploadCarWithoutImage(name, model, number, latitude, longitude);
            }
        });

        SelectPhoto.setOnClickListener(v -> {
            CheckStoragePermission();
            openImagePicker();
        });

        carImageView.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void CheckStoragePermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                openImagePicker();
            }
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(),
                                    imageUri
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        carImageView.setImageBitmap(bitmap);
                    }
                }
            });

    private void checkLocationPermission() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            promptEnableGPS();
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        locationAdapter.getClient().getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                carLocation.setText("Ш: " + latitude + " | Д: " + longitude);
            } else {
                carLocation.setText("Местоположение не найдено");
            }
        });
    }

    private void promptEnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Включить GPS?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialogInterface, i) ->
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Нет", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }
}
