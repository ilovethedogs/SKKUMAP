package swe2022.team6.skkumap.adapters;

import swe2022.team6.skkumap.R;
import swe2022.team6.skkumap.dataclasses.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter<T extends Class> extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<T> classes;

    public ClassAdapter(List<T> classes) {
        this.classes = classes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T currentClass = (T) classes.get(position);

        holder.tvName.setText(currentClass.getName());
        holder.tvClassroom.setText(currentClass.getClassroom());
        holder.tvDay.setText(currentClass.getDay().toString());
        holder.tvStartTime.setText(currentClass.getBegTime().toString());
        holder.tvEndTime.setText(currentClass.getEndTime().toString());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvClassroom;
        private final TextView tvDay;
        private final TextView tvStartTime;
        private final TextView tvEndTime;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvListName);
            tvClassroom = (TextView) view.findViewById(R.id.tvListClassroom);
            tvDay = (TextView) view.findViewById(R.id.tvListDay);
            tvStartTime = (TextView) view.findViewById(R.id.tvListStartTime);
            tvEndTime = (TextView) view.findViewById(R.id.tvListEndTime);
        }
    }
}
