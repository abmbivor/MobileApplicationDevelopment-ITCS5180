package net.ddns.bivor.hw04_group14;

import java.util.ArrayList;

public interface FragmentCommunication {
    void goToRecipe(ArrayList<Recipe> recipes);
    void goToSearch();
    void addIngredient(ArrayList<String> ListOfIngredient);
    void removeIngredient(ArrayList<String> ListOfIngredient);


}
