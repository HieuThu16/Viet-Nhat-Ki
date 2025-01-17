package com.example.vitnhtk.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitnhtk.AppDatabase;
import com.example.vitnhtk.Idea;
import com.example.vitnhtk.R;
import com.example.vitnhtk.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private IdeaAdapter ideaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo RecyclerView
        initializeRecyclerView();

        // Nút thêm ý tưởng
        binding.buttonAddIdea.setOnClickListener(v -> openAddIdeaDialog());

        // Tải dữ liệu từ database
        loadIdeasFromDatabase();

        return root;
    }

    private void initializeRecyclerView() {
        ideaAdapter = new IdeaAdapter();
        binding.recyclerViewTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewTasks.setAdapter(ideaAdapter);
    }

    private void loadIdeasFromDatabase() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        new Thread(() -> {
            List<Idea> ideaList = db.ideaDao().getAllIdeas();
            requireActivity().runOnUiThread(() -> {
                ideaAdapter.setIdeaList(ideaList);
            });
        }).start();
    }

    private void openAddIdeaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thêm ý tưởng mới");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_idea, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_idea_title);
        EditText editDescription = dialogView.findViewById(R.id.edit_idea_description);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            addIdeaToDatabase(title, description);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void addIdeaToDatabase(String title, String description) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTime));

        Idea idea = new Idea(title, description, formattedTime);

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        new Thread(() -> {
            db.ideaDao().insertIdea(idea);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Ý tưởng mới đã được thêm!", Toast.LENGTH_SHORT).show();
                loadIdeasFromDatabase(); // Tải lại danh sách sau khi thêm
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
