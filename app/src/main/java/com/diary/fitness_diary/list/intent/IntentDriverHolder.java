package com.diary.fitness_diary.list.intent;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

public class IntentDriverHolder extends RecyclerView.ViewHolder {
    public EditText editText;
    public ListView listView;
    public ImageButton imageButton,listButton;
    public IntentDriverHolder(View root,IntentDriverAdapter adapter){
        super(root);
        editText=root.findViewById(R.id.list_custom_item_EditText);
        listView=(ListView) root.findViewById(R.id.intentListView);
        listButton=(ImageButton) root.findViewById(R.id.list_view_list);
        root.findViewById(R.id.list_custom_item_imgbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.datas.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            }
        });
    }
}
