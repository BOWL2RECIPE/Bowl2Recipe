package com.kaur.bowl2recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsViewHolder> {
    JSONArray mIngreAsrray;
    Context mContext;

    public IngredientsListAdapter(JSONArray ingredientLines, RecipeViewActivity recipeViewActivity) {
        this.mIngreAsrray = ingredientLines;
        this.mContext = recipeViewActivity;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ingredient_row, viewGroup, false);
//        Log.d(TAG, "onCreateViewHolder: Size : " + mRecipeJsonArray.length());
        return new IngredientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int i) {
        try {
            ingredientsViewHolder.ingTextView.setText(mIngreAsrray.get(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mIngreAsrray.length();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView ingTextView;


        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ingTextView = itemView.findViewById(R.id.ingredient_text_view_row);
        }
    }
}
