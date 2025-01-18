package com.example.vitnhtk.ui.achievement;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitnhtk.AppDatabase;
import com.example.vitnhtk.Achievement;
import com.example.vitnhtk.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Achievement> achievementList = new ArrayList<>();
    private Context context;
    private FragmentActivity activity;

    public AchievementAdapter(FragmentActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void setAchievementList(List<Achievement> achievements) {
        this.achievementList = achievements;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Achievement achievement);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievementList.get(position);
        holder.titleTextView.setText(achievement.getTitle());
        holder.descriptionTextView.setText(achievement.getDescription());
        holder.timestampTextView.setText(achievement.getTimestamp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300;
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    openEditAchievementDialog(achievement);
                }
                lastClickTime = clickTime;
            }
        });
    }

    private void openEditAchievementDialog(Achievement achievement) {
        if (activity == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Chỉnh sửa thành tựu");

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_edit_achievement, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_achievement_title);
        EditText editDescription = dialogView.findViewById(R.id.edit_achievement_description);
        Button buttonSave = dialogView.findViewById(R.id.button_save);

        editTitle.setText(achievement.getTitle());
        editDescription.setText(achievement.getDescription());

        builder.setPositiveButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSave.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                updateAchievementInDatabase(achievement.getId(), newTitle, newDescription);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAchievementInDatabase(int id, String newTitle, String newDescription) {
        AppDatabase db = AppDatabase.getDatabase(context);
        new Thread(() -> {
            // Cập nhật thành tựu trong cơ sở dữ liệu
            Achievement achievement = db.achievementDao().getAchievementById(id);
            achievement.setTitle(newTitle);
            achievement.setDescription(newDescription);
            db.achievementDao().updateAchievement(achievement);

            // Lấy danh sách cập nhật từ cơ sở dữ liệu
            List<Achievement> updatedAchievements = db.achievementDao().getAllAchievements();

            // Cập nhật lại giao diện trên luồng chính
            new Handler(Looper.getMainLooper()).post(() -> {
                setAchievementList(updatedAchievements); // Cập nhật danh sách trong Adapter
                Toast.makeText(context, "Cập nhật thành tựu thành công!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timestampTextView;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_achievement_title);
            descriptionTextView = itemView.findViewById(R.id.text_achievement_description);
            timestampTextView = itemView.findViewById(R.id.text_achievement_timestamp);
        }
    }
}
