package com.example.vitnhtk.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitnhtk.Idea;
import com.example.vitnhtk.R;

import java.util.ArrayList;
import java.util.List;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder> {

    private List<Idea> ideaList = new ArrayList<>();

    public void setIdeaList(List<Idea> ideas) {
        this.ideaList = ideas;
        notifyDataSetChanged();
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
