package com.kaur.bowl2recipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kaur.bowl2recipe.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    JSONArray mRecipeJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        String jsonString = getIntent().getStringExtra(MainActivity.RECIPE_LIST_JSON);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recycler_view_recipe);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        try {
            mRecipeJsonArray = new JSONArray(jsonString);
            RecipeListAdapter adapter = new RecipeListAdapter(mRecipeJsonArray, listener);
            mRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Util.RecyclerViewClickListener listener = new Util.RecyclerViewClickListener() {
        @Override
        public void onClick(View view, int position) {
            Toast.makeText(ResultsActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
            try {
                JSONObject recipeJsonObject = (JSONObject) mRecipeJsonArray.get(position);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


}
