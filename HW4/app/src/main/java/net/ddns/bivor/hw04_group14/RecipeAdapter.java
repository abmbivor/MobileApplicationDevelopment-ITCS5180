package net.ddns.bivor.hw04_group14;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<Recipe> mData;
    private FragmentCommunication mCommunicator;

    public RecipeAdapter(ArrayList<Recipe> mData, FragmentCommunication mCommunicator) {
        this.mData = mData;
        this.mCommunicator = mCommunicator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        RecipeAdapter.ViewHolder viewHolder = new RecipeAdapter.ViewHolder(view, mCommunicator, mData);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        holder.recipes = mData;
        holder.textViewRecipeTitle.setText(recipe.title);
        holder.textViewRecipeIngredients.setText(recipe.ingredients);
        holder.textViewRecipeURL.setPaintFlags(holder.textViewRecipeURL.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.textViewRecipeURL.setText(recipe.href);
        if(!recipe.thumbnail.isEmpty())Picasso.get().load(recipe.thumbnail).into(holder.imageViewRecipe);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        FragmentCommunication mCommunication;
        ArrayList<Recipe> recipes;
        TextView textViewRecipeTitle, textViewRecipeIngredients, textViewRecipeURL;
        ImageView imageViewRecipe;

        public ViewHolder(final View itemView, FragmentCommunication Communicator, ArrayList<Recipe> list) {
            super(itemView);
            mCommunication = Communicator;
            recipes = list;

            textViewRecipeTitle = itemView.findViewById(R.id.textViewRecipeTitle);
            textViewRecipeIngredients = itemView.findViewById(R.id.textViewRecipeIngredients);
            textViewRecipeURL = itemView.findViewById(R.id.textViewRecipeURL);
            imageViewRecipe = itemView.findViewById(R.id.imageView);


            textViewRecipeURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(textViewRecipeURL.getText().toString()));
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }


}
