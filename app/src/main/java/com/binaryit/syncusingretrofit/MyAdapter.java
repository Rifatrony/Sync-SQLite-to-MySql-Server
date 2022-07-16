package com.binaryit.syncusingretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {


    Context context;
    List<Model> modelList;

    MyDatabaseHelper databaseHelper;

    public MyAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        Model data = modelList.get(position);
        holder.nameTextView.setText(data.getName());
        holder.numberTextView.setText(data.getNumber());
        holder.ageTextView.setText(data.getAge());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHelper = new MyDatabaseHelper(context);

                int value = databaseHelper.deleteFromLocal(data.getId());
                if (value > 0){
                    Toast.makeText(context, "Deleted successfully ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Deleted not success", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, numberTextView, ageTextView;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);

        }
    }

}
