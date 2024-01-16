package com.diary.fitness_diary.list.editIntent;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

public class EidtHolder extends RecyclerView.ViewHolder{
    public EditText editText;
    public RecyclerView recyclerView;
    public ImageButton button,listButton;
    public EidtHolder(View root,EditIntentAdapter adapter) {
        super(root);
        recyclerView=root.findViewById(R.id.editRecyclerView);
        editText=root.findViewById(R.id.list_custom_item_EditText2);
        button=root.findViewById(R.id.list_custom_item_imgbtn2);
        listButton=root.findViewById(R.id.list_view_list2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.datas.set(getAdapterPosition(),new EditIntentVO(""));
                adapter.datas.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            }
        });
    }
}
