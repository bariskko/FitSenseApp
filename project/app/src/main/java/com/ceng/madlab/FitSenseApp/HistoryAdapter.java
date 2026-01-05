package com.ceng.madlab.FitSenseApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // XML'de ImageView kullandığın için bu gerekli
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Measurement> list;
    private FirebaseFirestore db;
    private String userId;

    public HistoryAdapter(List<Measurement> list, String userId) {
        this.list = list;
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Measurement item = list.get(position);

        if(item != null) {
            holder.tvPercent.setText(String.format("%% %.1f", item.getFatRate()));
            holder.tvStatus.setText(item.getStatus());

            if(item.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                holder.tvDate.setText(sdf.format(item.getDate().toDate()));
            }
        }


        holder.btnDelete.setOnClickListener(v -> {
            if (userId != null && item != null && item.getId() != null) {
                db.collection("users").document(userId)
                        .collection("measurements").document(item.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(v.getContext(), "Kayıt Silindi", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPercent, tvDate, tvStatus;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvPercent = itemView.findViewById(R.id.tvPercent);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}