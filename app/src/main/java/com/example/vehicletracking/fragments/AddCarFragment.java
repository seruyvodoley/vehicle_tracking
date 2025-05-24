package com.example.vehicletracking.presentation.addcar;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.databinding.FragmentAddcarInfoBinding;
import com.example.vehicletracking.data.CarRepositoryImpl;
import com.example.vehicletracking.domain.AddCarUseCase;
import com.example.vehicletracking.presentation.AddCarEffect;
import com.example.vehicletracking.presentation.AddCarEvent;
import com.example.vehicletracking.presentation.AddCarViewModel;
import com.example.vehicletracking.utils.AddCarViewModelFactory;

public class AddCarFragment extends Fragment {

    private FragmentAddcarInfoBinding binding;
    private AddCarViewModel viewModel;

    private Uri selectedImageUri; // Предполагается, что где-то выбирается изображение и записывается сюда

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddcarInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Создаём ViewModel с кастомной фабрикой, передавая UseCase с реализацией репозитория
        AddCarUseCase useCase = new AddCarUseCase(new CarRepositoryImpl());
        viewModel = new ViewModelProvider(this, new AddCarViewModelFactory(useCase)).get(AddCarViewModel.class);
        String coordinates = binding.carLocation.getText().toString(); // "55.7558, 37.6173"
        String[] parts = coordinates.split(",");
        observeViewModel();

        binding.addCar.setOnClickListener(v -> {
            String name = binding.carName.getText().toString();
            String model = binding.carModel.getText().toString();
            String number = binding.carNumber.getText().toString();
            String lat = parts[0].trim();
            String lon = parts[1].trim();

            if (name.isEmpty() || model.isEmpty() || number.isEmpty() || lat.isEmpty() || lon.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri != null) {
                viewModel.onEvent(new AddCarEvent.SubmitWithImage(name, model, number, lat, lon, selectedImageUri));
            } else {
                viewModel.onEvent(new AddCarEvent.SubmitWithoutImage(name, model, number, lat, lon));
            }
        });

        // Здесь можно добавить логику выбора изображения и установить selectedImageUri
    }

    private void observeViewModel() {
        viewModel.state.observe(getViewLifecycleOwner(), state -> {
            binding.addCar.setEnabled(!state.isUploading);
            if (state.errorMessage != null) {
                Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.effect.observe(getViewLifecycleOwner(), effect -> {
            if (effect instanceof AddCarEffect.ShowToast) {
                Toast.makeText(requireContext(), ((AddCarEffect.ShowToast) effect).message, Toast.LENGTH_SHORT).show();
            } else if (effect instanceof AddCarEffect.NavigateBack) {
                requireActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
