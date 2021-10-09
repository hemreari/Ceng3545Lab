package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterActivity extends AppCompatActivity {
    final List<Animal> animals = new ArrayList<Animal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_adapter);
        animals.add(new Animal("CAT", R.mipmap.cat));
        animals.add(new Animal("DOG", R.mipmap.dog));
        animals.add(new Animal("RACOON", R.mipmap.racoon));

        final ListView listView = (ListView) findViewById(R.id.listView);
        AnimalAdapter animalAdapter = new AnimalAdapter(this, animals);
        listView.setAdapter(animalAdapter);
    }
}