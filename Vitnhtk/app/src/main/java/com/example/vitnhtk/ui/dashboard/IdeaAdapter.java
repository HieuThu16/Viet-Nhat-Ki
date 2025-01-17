package com.example.vitnhtk.ui.dashboard;

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
import com.example.vitnhtk.Idea;
import com.example.vitnhtk.R;

import java.util.ArrayList;
import java.util.List;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Idea> ideaList = new ArrayList<>();
    private Context context;
    private FragmentActivity activity;


    public IdeaAdapter(FragmentActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void setIdeaList(List<Idea> ideas) {
        this.ideaList = ideas;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Idea idea);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public IdeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_idea, parent, false);
        return new IdeaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaViewHolder holder, int position) {
        Idea idea = ideaList.get(position);
        holder.titleTextView.setText(idea.getTitle());
        holder.descriptionTextView.setText(idea.getDescription());
        holder.timestampTextView.setText(idea.getTimestamp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300;
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {

                        openEditIdeaDialog(idea);

                }
                lastClickTime = clickTime;
            }
        });
    }

    private void openEditIdeaDialog(Idea idea) {
        if (activity == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Chỉnh sửa ý tưởng");

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_edit_idea, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_idea_title);
        EditText editDescription = dialogView.findViewById(R.id.edit_idea_description);
        Button buttonSave = dialogView.findViewById(R.id.button_save);

        editTitle.setText(idea.getTitle());
        editDescription.setText(idea.getDescription());

        builder.setPositiveButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSave.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                updateIdeaInDatabase(idea.getId(), newTitle, newDescription);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIdeaInDatabase(int id, String newTitle, String newDescription) {
        AppDatabase db = AppDatabase.getDatabase(context);
        new Thread(() -> {
            // Cập nhật ý tưởng trong cơ sở dữ liệu
            Idea idea = db.ideaDao().getIdeaById(id);
            idea.setTitle(newTitle);
            idea.setDescription(newDescription);
            db.ideaDao().updateIdea(idea);

            // Lấy danh sách cập nhật từ cơ sở dữ liệu
            List<Idea> updatedIdeas = db.ideaDao().getAllIdeas();

            // Cập nhật lại giao diện trên luồng chính
            new Handler(Looper.getMainLooper()).post(() -> {
                setIdeaList(updatedIdeas); // Cập nhật danh sách trong Adapter
                Toast.makeText(context, "Cập nhật ý tưởng thành công!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return ideaList.size();
    }

    static class IdeaViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timestampTextView;

        public IdeaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            timestampTextView = itemView.findViewById(R.id.text_timestamp);
        }
    }
}
