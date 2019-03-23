package net.ddns.bivor.hw04_group14;

import java.io.Serializable;

public class Recipe implements Serializable {

    String title, href, ingredients, thumbnail;

    public Recipe() {

    }

    public Recipe(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

}
