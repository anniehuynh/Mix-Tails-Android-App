package com.example.mix_tailsapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mix_tailsapp.Adapter.SearchAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 18/02/2021
 * authors: An Huynh, Miguel, Vasily
 * This class decides the activities take place in the drink recommended page including ImageButton
 * menu, drink detail, add drink to favorite list and so on.
 * @version 1: declare variables (Annie)
 * @version 1.2: binding the buttons and write functions for them (Annie)
 * @version 2: write function for pop up menu and surprise drink button (Annie)
 * @version 3: write function for search bar and listview
 * @version 3.1: binding database to search bar (Miguel)
 * @version 4: delete search bar and write search adapter (Annie)
 * @version 5: reimplement search bar from scratch, used materialSearchBar (Annie)
 * Reference material used in this activity: Android Popup menu mrBool.com
 * http://mrbool.com/android-menu-how-to-create-a-menu-in-android/30719
 * SearchView reference: https://developer.android.com/reference/android/widget/SearchView
 * Material Search Bar: https://github.com/mancj/MaterialSearchBar
 * https://stackoverflow.com/questions/32455745/recyclerview-and-cardview-click-listener-implementation
 * https://google-developer-training.github.io/android-developer-fundamentals-course-practicals/
 * en/Unit%204/101b_p_searching_an_sqlite_database.html
 * Progress bar https://www.tutlane.com/tutorial/android/android-progressbar-with-examples
 */

public class DrinkRecommendationPage extends AppCompatActivity {
    //Declare Variables
    private ImageButton menuBtn, surpriseDrink;
    private ImageView favoriteBtn;
    private SharedPreferences tempStorageGet;
    public static final String EXTRA_POSITION = "com.example.mix_tailsapp.EXTRA_POSITION";
    protected static final String SURPRISE_KEY = "KEWIOhguyfbvUWIGefyuowUILGYUOAWGYEURFQU3";
    protected static final String DETAIL_KEY = "DIDYOUKNOWTHAT_EINSTEIN_IS_SUPERIOR_THAN_HAWKING";
    protected static final String NAME_KEY = "TOM_CRUISE_HAS_SUPERPOWERS_SUPERIOR_THAN_SUPERMAN";

    private ListView listView;
    private List<DatabaseAccess> cocktails;

    //Recycler Searchbar
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter searchAdapter;
    MaterialSearchBar materialSearchBar;
    List<String> suggestions = new ArrayList<>();
    DatabaseOpen database;


    /**
     * This onCreate method contains functions of searchView, pop-up menu and surprise drink buttons
     *
     * @param savedInstanceState
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_recommendation_page);

        //Accessing database to show surprise drinks
        DatabaseAccess drinksAccess = DatabaseAccess.getInstance(getApplicationContext());
        drinksAccess.open();


        //Initiate variables

        tempStorageGet = getSharedPreferences(SignupActivity.TEMP_STORAGE, Activity.MODE_PRIVATE);
        favoriteBtn = (ImageView) findViewById(R.id.favBtn);

        //Initiate View
        recyclerView = (RecyclerView) findViewById(R.id.recycle_search);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchView);

        //Initiate database
        database = new DatabaseOpen(this);

        //Set up search bar
        materialSearchBar.setHint("Search");
        materialSearchBar.setCardViewElevation(10);
        loadSuggestions();


        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();

                for (String search : suggestions) {

                    /*this line of code taken  from
                    https://stackoverflow.com/questions/61905314/why-my-autocomplete-searchbar-isnt-working
                    */
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*
        reference
        https://www.codota.com/code/java/methods/com.mancj.materialsearchbar.MaterialSearchBar/setOnSearchActionListener
         */
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    recyclerView.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        //Initiate the search adapter at default to set all the result
        searchAdapter = new SearchAdapter(this, database.getCocktails());


        //Initiate ImageBtn menu with its id and create a pop-up menu inside it
        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(v -> {
            //Creating an instance of PopupMenu
            PopupMenu popupMenu = new PopupMenu(DrinkRecommendationPage.this, menuBtn);

            //Inflating the popup using xml file popup_menu.xml
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            //Creating the OnMenuItemClickListener
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent toHome = new Intent(DrinkRecommendationPage.this,
                                AppWelcomeScreen.class);
                        startActivity(toHome);
                        break;
                    case R.id.newDrink:
                        Intent addDrink = new Intent(DrinkRecommendationPage.this, AddingDrink.class);
                        startActivity(addDrink);
                        break;
                    case R.id.favorite:
                        Intent toFavoriteList = new Intent(DrinkRecommendationPage.this, FavoriteDrinks.class);
                        startActivity(toFavoriteList);
                    case R.id.settings:
                        Intent settings = new Intent(DrinkRecommendationPage.this,
                                Settings.class);
                        startActivity(settings);
                        break;
                    case R.id.signout:
                        Intent signOut = new Intent(DrinkRecommendationPage.this,
                                AppLaunching.class);
                        SharedPreferences.Editor deleter = tempStorageGet.edit();
                        deleter.clear();
                        if (deleter.commit()) {
                            startActivity(signOut);
                        }
                        break;
                }

                return true;
            });
            popupMenu.show();
        });

        //When the surprise drink Image(top right in recommendation page) Button clicked

        surpriseDrink = findViewById(R.id.imageButton);
        surpriseDrink.setOnClickListener(v -> {
            Intent toRandomDrink = new Intent(DrinkRecommendationPage.this,
                    ChosenDrinkSecondActivity.class);
            String i = drinksAccess.getRandom();
            toRandomDrink.putExtra(SURPRISE_KEY, i);
            startActivity(toRandomDrink);
            drinksAccess.close();
        });

    }


    /**
     * create a start search method for search bar
     */

    private void startSearch(String text) {

        searchAdapter = new SearchAdapter(this, database.getDrinkByName(text));
        recyclerView.setAdapter(searchAdapter);

    }

    /**
     * Create a method to load suggestions for search bar
     */
    private void loadSuggestions() {
        suggestions = database.getDrinkName();
        materialSearchBar.setLastSuggestions(suggestions);

    }
}