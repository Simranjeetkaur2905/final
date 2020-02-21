package com.example.projectnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class listofnotes extends AppCompatActivity implements View.OnClickListener {

    EditText searchoption;
    //List<Notesdata> filter;
 //   ListView listView;
    SwipeMenuListView listView;
    DatabaseNotes mDatabase;
    public  static List<String> notesTitle;
    public  static List<Notesdata> AllData;
    List<Notesdata> searchList = new ArrayList<>();
    List<String> search_list = new ArrayList<>();




    String category_name = "";
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofnotes);
        searchoption = findViewById(R.id.SearchOption);

        Intent i = getIntent();
        category_name = i.getStringExtra("CAT");


        listView = findViewById(R.id.Notes_listView);
        mDatabase = new DatabaseNotes(this);

        findViewById(R.id.add_note).setOnClickListener(this);
        searchoption.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                      String searchtext = s.toString();
                      if(!searchtext.isEmpty()) {
                          for (Notesdata notesdata : AllData)
                              if (notesdata.getNotesTitle().contains(searchtext)) {
                                  searchList.add(notesdata);
                                  search_list.add(notesdata.getNotesTitle());

                              }
                      }
                              else {
                                  searchList.addAll(AllData);
                                  for (Notesdata notesdata:AllData){
                                      search_list.add(notesdata.getNotesTitle());
                              }

                      }
//                              arrayAdapter = new ArrayAdapter(listofnotes.this,android.R.layout.simple_list_item_1, search_list);
//                              listView.setAdapter(arrayAdapter);
                                                    arrayAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {

                                                }
                                            }
        );

        notesTitle = new ArrayList<>();
        AllData = new ArrayList<>();
        //loadNotesTitle();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(listofnotes.this,detailofnotes.class);

                intent.putExtra("object", AllData.get(position));
                startActivity(intent);


            }
        });

        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());

                delete.setTitle("DELETE");
                delete.setIcon(R.drawable.ic_delete_black_24dp);
                delete.setBackground(new ColorDrawable(Color.parseColor("#FFF71B05")));
                delete.setWidth(250);
                menu.addMenuItem(delete);
            }
        };
        listView.setMenuCreator(swipeMenuCreator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                if (index == 0){


                    Notesdata note = AllData.get(position);
                    int id = note.getId();
                    if(mDatabase.deleteNote(id))
                        AllData.remove(position);
//
                    loadNotesTitle();
                }
                return true;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNotesTitle();
    }

    private void loadNotesTitle(){

        notesTitle.clear();
        AllData.clear();
        Cursor cursor = mDatabase.getAllNotes(category_name);

        if(cursor.moveToFirst()){
            do{
                notesTitle.add(cursor.getString(2));
                AllData.add(new Notesdata(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6)));

            } while (cursor.moveToNext());
            cursor.close();

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,notesTitle);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,detailofnotes.class);
        startActivity(intent);
    }
}

