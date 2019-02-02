package com.kaur.bowl2recipe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaur.bowl2recipe.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeViewActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recipe);
        String stringJson = getIntent().getStringExtra(ResultsActivity.RECIPE_JSON);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringJson);
            setTitle(jsonObject.getString("label"));
            ImageView imageView = findViewById(R.id.image_dish_recipe);
            Picasso.get().load(jsonObject.get("image").toString()).placeholder(R.mipmap.loading_image).into(imageView);
            mRecyclerView = findViewById(R.id.ingredients_recycler_view);
            IngredientsListAdapter incIngredientsListAdapter = new IngredientsListAdapter(jsonObject.getJSONArray("ingredientLines"), this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(incIngredientsListAdapter);
            TextView calorieTextView = findViewById(R.id.calorie_recipe_view);
            String calories = "Calorie : " + jsonObject.getString("calories");
            calorieTextView.setText(calories);

            TextView healthLabelsView = findViewById(R.id.health_labels_recipe_view);
            String healthLabels = "Labels : " + Util.toCSV(jsonObject.getJSONArray("healthLabels"));
            healthLabelsView.setText(healthLabels);

            TextView urlView = findViewById(R.id.url_recipe_view);
            String url = "Link : " + jsonObject.getString("url");
            urlView.setText(url);
            urlView.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        setSupportActionBar(toolbar);

    }
}
