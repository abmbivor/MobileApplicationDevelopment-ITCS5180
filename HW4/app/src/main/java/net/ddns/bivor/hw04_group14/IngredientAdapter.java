package net.ddns.bivor.hw04_group14;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{

    ArrayList<String> mData;
    private FragmentCommunication mCommunicator;


    public IngredientAdapter(ArrayList<String> mData, FragmentCommunication mCommunicator) {
        this.mData = mData;
        this.mCommunicator = mCommunicator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType ==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, mCommunicator, mData);
            return viewHolder;
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, mCommunicator, mData);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.pos = position;
        holder.ingredientList = mData;

        if(mData.size()>0){
            if(position<mData.size()){
                holder.editTextIngredient.setText(holder.ingredientList.get(position));
                holder.editTextIngredient.setEnabled(false);
                holder.fabIngredient.setImageResource(R.drawable.ic_delete);
            }
            else {
                if(position==5){
                    holder.editTextIngredient.setVisibility(View.INVISIBLE);
                    holder.fabIngredient.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.editTextIngredient.setVisibility(View.VISIBLE);
                    holder.fabIngredient.setVisibility(View.VISIBLE);
                    holder.editTextIngredient.setText("");
                    holder.editTextIngredient.setEnabled(true);
                    holder.fabIngredient.setImageResource(R.drawable.ic_input_add);
                }
            }
        }
        else {
            holder.editTextIngredient.setText("");
            holder.editTextIngredient.setEnabled(true);
            holder.fabIngredient.setImageResource(R.drawable.ic_input_add);
        }

    }

    @Override
    public int getItemCount() {
        if(mData.size() == 0) return 1;
        else return mData.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size() == 0) return 1;
        else return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        FragmentCommunication mCommunication;
        EditText editTextIngredient;
        FloatingActionButton fabIngredient;
        ArrayList<String> ingredientList;
        int pos;

        public ViewHolder(final View itemView, FragmentCommunication Communicator, ArrayList<String> list) {
            super(itemView);
            mCommunication = Communicator;
            ingredientList = list;

            editTextIngredient = itemView.findViewById(R.id.editTextIngredient);
            fabIngredient = itemView.findViewById(R.id.fab);

            fabIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!editTextIngredient.isEnabled()){
                        if(pos>1){
                            ingredientList.remove(pos);
                            mCommunication.removeIngredient(ingredientList);
                        }
                        else {
                            ingredientList.remove(pos);
                            editTextIngredient.setEnabled(true);
                            editTextIngredient.setText("");
                            fabIngredient.setImageResource(R.drawable.ic_input_add);
                            mCommunication.removeIngredient(ingredientList);
                        }

                    }
                    else {
                        if(editTextIngredient.getText().toString().isEmpty())editTextIngredient.setError("Enter an Ingredient");
                        else{
                            fabIngredient.setImageResource(R.drawable.ic_delete);
                            editTextIngredient.setEnabled(false);

                            if(ingredientList.size()<5){
                                ingredientList.add(editTextIngredient.getText().toString().trim());
                                mCommunication.addIngredient(ingredientList);
                            }

                        }
                    }

                }
            });
        }
    }

}
