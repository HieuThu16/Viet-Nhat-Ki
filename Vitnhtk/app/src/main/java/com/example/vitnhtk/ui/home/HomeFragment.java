package com.example.vitnhtk.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vitnhtk.AppDatabase;
import com.example.vitnhtk.JournalEntry;
import com.example.vitnhtk.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private AppDatabase appDatabase; // Đối tượng AppDatabase

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Khởi tạo Calendar và DateFormat
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Khởi tạo AppDatabase
        appDatabase = AppDatabase.getDatabase(requireContext());

        // Thiết lập các listener
        setupListeners();

        // Observe ViewModel
        observeViewModel();

        // Khởi tạo ngày hiện tại
        updateDateDisplay();

        return root;
    }

    private void setupListeners() {
        // Lưu dữ liệu
        binding.btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ EditText
            String positive = binding.etPositive.getText().toString();
            String negative = binding.etNegative.getText().toString();
            String progress = binding.etProgress.getText().toString();
            String learned = binding.etLearned.getText().toString();

            // In log để kiểm tra dữ liệu trước khi lưu
            Log.d("HomeFragment", "Saving Journal Entry: ");
            Log.d("HomeFragment", "Positive: " + positive);
            Log.d("HomeFragment", "Negative: " + negative);
            Log.d("HomeFragment", "Progress: " + progress);
            Log.d("HomeFragment", "Learned: " + learned);

            // Lưu vào cơ sở dữ liệu
            saveJournalEntry(
                    dateFormat.format(calendar.getTime()),
                    positive,
                    negative,
                    progress,
                    learned
            );
        });

        // Chọn ngày
        binding.btnSelectDate.setOnClickListener(v -> showDatePicker());
    }

    private void saveJournalEntry(String date, String positive, String negative, String progress, String learned) {
        new Thread(() -> {
            JournalEntry existingEntry = appDatabase.journalDao().getEntryByDate(date);

            if (existingEntry != null) {
                // Nếu có rồi, cập nhật toàn bộ đối tượng
                existingEntry.setPositive(positive);
                existingEntry.setNegative(negative);
                existingEntry.setProgress(progress);
                existingEntry.setLearned(learned);

                appDatabase.journalDao().update(existingEntry); // Cập nhật
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Đã cập nhật nhật ký!", Toast.LENGTH_SHORT).show();
                    loadJournalEntry(date); // Tải lại dữ liệu đã cập nhật
                });
            } else {
                // Nếu chưa có, tạo mục mới và chèn vào cơ sở dữ liệu
                JournalEntry journalEntry = new JournalEntry(date, positive, negative, progress, learned);
                appDatabase.journalDao().insert(journalEntry); // Chèn mới
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Đã lưu thành công!", Toast.LENGTH_SHORT).show();
                    loadJournalEntry(date); // Tải lại dữ liệu mới
                });
            }
        }).start();
    }



    private void loadJournalEntry(String date) {
        // Lấy dữ liệu nhật ký cho ngày đã chọn
        new Thread(() -> {
            JournalEntry entry = appDatabase.journalDao().getEntryByDate(date);

            requireActivity().runOnUiThread(() -> {
                if (entry != null) {
                    // Hiển thị dữ liệu lên giao diện
                    binding.etPositive.setText(entry.getPositive());
                    binding.etNegative.setText(entry.getNegative());
                    binding.etProgress.setText(entry.getProgress());
                    binding.etLearned.setText(entry.getLearned());
                } else {
                    // Nếu không có dữ liệu, thông báo
                    Toast.makeText(getContext(), "Không có dữ liệu cho ngày này!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                }
            });
        }).start();
    }




    private void observeViewModel() {
        // Theo dõi ngày được chọn
        homeViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            binding.tvSelectedDate.setText(String.format("Ngày: %s", date));
            // Tự động tải dữ liệu của ngày đó khi thay đổi
            loadJournalEntry(date);
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        String selectedDate = dateFormat.format(calendar.getTime());
        homeViewModel.setSelectedDate(selectedDate);
    }

    private void clearInputs() {
        binding.etPositive.setText("");
        binding.etNegative.setText("");
        binding.etProgress.setText("");
        binding.etLearned.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
