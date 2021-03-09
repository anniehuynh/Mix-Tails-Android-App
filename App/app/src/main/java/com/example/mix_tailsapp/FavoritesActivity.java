package com.example.mix_tailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * created 08/03/2021
 * authors Miguel, An, Vasily
 *
 * The user has the options to add any drinks(included new added drinks)
 * to a favorites list by pressing the heart button when in drink details.
 *
 */

public class FavoritesActivity extends AppCompatActivity {

    ArrayList<String> favoriteDrinksList;
    DatabaseAccess db;
    private ImageButton goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();
        favoriteDrinksList = new ArrayList<String>();
        favoriteDrinksList = db.getFavs();
        goBack = findViewById(R.id.gobackBtn);
        goBack.setOnClickListener(v -> {
            Intent back = new Intent(FavoritesActivity.this, HomePage.class);
            startActivity(back);
        });
        ListView show = findViewById(R.id.favoriteList);
        show.setAdapter(new ArrayAdapter<String>(
                this, //activity instance
                android.R.layout.simple_expandable_list_item_1,
                favoriteDrinksList));

        show.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(FavoritesActivity.this,
                db.getDrinkIngs(favoriteDrinksList.get(position)),
                Toast.LENGTH_LONG).show());
    }
/*
    public View FavoritesView(Context context, Cursor c, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.activity_favorites, parent,false);
    }

    public void showFavorites(View v, Context context, Cursor c){

        ListView favs = (ListView)v.findViewById(R.id.favoriteList);
        String body = c.getString(c.getColumnIndexOrThrow("name"));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
        favs.setAdapter(body);


            }
            });
    }
*/
}


