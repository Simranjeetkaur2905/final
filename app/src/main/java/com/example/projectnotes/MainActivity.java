package com.example.projectnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseNotes mDatabase;
    public static List<String> CategoriesList;
    Button AddNote;
    SwipeMenuListView listView;
    ArrayAdapter<String> arrayAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddNote = findViewById(R.id.add_btn);
        listView = findViewById(R.id.list_view);


        findViewById(R.id.add_btn).setOnClickListener(this);

        mDatabase = new DatabaseNotes(this);
        CategoriesList = new ArrayList<>();
        loadCategory();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(MainActivity.this, listofnotes.class);
                intent.putExtra("CAT",CategoriesList.get(position));
                startActivity(intent);
            }
        });
//        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//
//                SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
//
//                delete.setTitle("DELETE");
//                delete.setIcon(R.drawable.ic_delete_black_24dp);
//                delete.setBackground(new ColorDrawable(Color.parseColor("#FFF71B05")));
//                delete.setWidth(250);
//                menu.addMenuItem(delete);
//            }
//        };
//        listView.setMenuCreator(swipeMenuCreator);


//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//
//                if (index == 0){
//
//
//                    Notesdata note = .get(position);
//                    int id = note.getId();
//                    if(mDatabase.deleteNote(id))
//                        CategoriesList.remove(position);
////
//                    loadNotesTitle();
//                }
//                return true;
//            }
//        });

       // listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }




    @Override
    protected void onStart() {
        super.onStart();

        loadCategory();


    }

    private void loadCategory(){


        CategoriesList.clear();
        Cursor cursor = mDatabase.getAllCategories();

        if(cursor.moveToFirst()){
            do{
                CategoriesList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,CategoriesList);
            listView.setAdapter(arrayAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                Intent intent = new Intent(MainActivity.this,detailofnotes.class);

                startActivity(intent);
        }
    }


}
