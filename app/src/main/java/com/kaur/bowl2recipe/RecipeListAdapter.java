package com.kaur.bowl2recipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaur.bowl2recipe.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {
    static final String TAG = RecipeListAdapter.class.getSimpleName();
    JSONArray mRecipeJsonArray;
    Util.RecyclerViewClickListener mRecyclerViewClickListener;

    public RecipeListAdapter(JSONArray recipeJsonArray, Util.RecyclerViewClickListener recyclerViewClickListener) {
        mRecipeJsonArray = recipeJsonArray;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }


    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_row, parent, false);
        Log.d(TAG, "onCreateViewHolder: Size : " + mRecipeJsonArray.length());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.MyViewHolder myViewHolder, int i) {
        try {
            JSONObject item = (JSONObject) mRecipeJsonArray.get(i);
            String imageUrl = item.getString("image");
            Log.d(TAG, "onBindViewHolder: image : " + imageUrl);
            Picasso.get().load(imageUrl).placeholder(R.mipmap.loading_image).into(myViewHolder.dishImage);
            myViewHolder.dishName.setText(item.getString("label"));
            myViewHolder.healthLabel.setText(Util.toCSV(item.getJSONArray("healthLabels")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeJsonArray != null && mRecipeJsonArray.length() > 0) {
            return mRecipeJsonArray.length();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView dishName, healthLabel;
        public ImageView dishImage;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            dishName = view.findViewById(R.id.dish_name);
            healthLabel = view.findViewById(R.id.health_labels);
            dishImage = view.findViewById(R.id.image_dish);
        }

        @Override
        public void onClick(View v) {
            mRecyclerViewClickListener.onClick(v, getLayoutPosition());
        }
    }
}
