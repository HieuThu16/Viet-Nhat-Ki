package com.example.vitnhtk.ui.achievement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vitnhtk.AppDatabase;
import com.example.vitnhtk.Achievement;
import com.example.vitnhtk.R;
import com.example.vitnhtk.databinding.FragmentAchievementBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AchievementFragment extends Fragment {

    private FragmentAchievementBinding binding;
    private AchievementAdapter achievementAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAchievementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo RecyclerView
        initializeRecyclerView();

        // Nút thêm thành tựu
        binding.buttonAddAchievement.setOnClickListener(v -> openAddAchievementDialog());

        loadAchievementsFromDatabase();

        return root;
    }

    private void initializeRecyclerView() {
        achievementAdapter = new AchievementAdapter((FragmentActivity) requireContext());
        binding.recyclerViewAchievements.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewAchievements.setAdapter(achievementAdapter);
    }

    private void loadAchievementsFromDatabase() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        new Thread(() -> {
            List<Achievement> achievementList = db.achievementDao().getAllAchievements();
            requireActivity().runOnUiThread(() -> {
                achievementAdapter.setAchievementList(achievementList);
            });
        }).start();
    }

    private void openAddAchievementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thêm thành tựu mới");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_achievement, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_achievement_title);
        EditText editDescription = dialogView.findViewById(R.id.edit_achievement_description);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            addAchievementToDatabase(title, description);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void addAchievementToDatabase(String title, String description) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTime));

        Achievement achievement = new Achievement(title, description, formattedTime);

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        new Thread(() -> {
            db.achievementDao().insertAchievement(achievement);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Thành tựu mới đã được thêm!", Toast.LENGTH_SHORT).show();
                loadAchievementsFromDatabase(); // Tải lại danh sách sau khi thêm
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
