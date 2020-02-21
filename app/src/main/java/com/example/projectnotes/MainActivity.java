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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseNotes mDatabase;
    public static List<String> CategoriesList;
    Button AddNote;
    ListView listView;
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
