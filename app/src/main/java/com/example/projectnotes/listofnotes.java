package com.example.projectnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class listofnotes extends AppCompatActivity implements View.OnClickListener {

    SearchView searchoption;
    List<Notesdata> filter;
    ListView listView;
    DatabaseNotes mDatabase;
    public  static List<String> notesTitle;
    public  static List<Notesdata> AllData;

    Button delete;


    String category_name = "";
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofnotes);
        searchoption = findViewById(R.id.SearchOption);

        delete = findViewById(R.id.del_btn);

        searchoption.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                    filter.clear();
                    for(int i =0;i<AllData.size();i++){


                        Notesdata getdata = AllData.get(i);
                        if(getdata.notesTitle.contains(newText)){

                            filter.add(getdata);

                        }

                    }
                    arrayAdapter = new ArrayAdapter(listofnotes.this, android.R.layout.simple_list_item_1,filter);
                    listView.setAdapter(arrayAdapter);
                }

                if(newText.isEmpty()){

                    arrayAdapter = new ArrayAdapter(listofnotes.this, android.R.layout.simple_list_item_1,notesTitle);
                    listView.setAdapter(arrayAdapter);

                }
                return false;
            }
        });





        Intent i = getIntent();
        category_name = i.getStringExtra("CAT");


        listView = findViewById(R.id.Notes_listview);
        mDatabase = new DatabaseNotes(this);

        findViewById(R.id.add_note).setOnClickListener(this);

        notesTitle = new ArrayList<>();
        AllData = new ArrayList<>();
        loadNotesTitle();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(listofnotes.this,detailofnotes.class);

                intent.putExtra("object", AllData.get(position));
                startActivity(intent);


            }
        });
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

