package com.example.trevorragland.myapplication.POJO;

/**
 * Created by trial on 3/16/2017.
 */

public class Recipe{

    //Variables
    private String recipeIngredients;
    private String recipePrep;

    //default Constructor
    public Recipe(){

    }

    //Constructor with Parameters
    public Recipe(String ingredients, String prep){
        this.recipeIngredients= ingredients;
        this.recipePrep=prep;
    }

    //Setters and getters
    public String getRecipePrep() {
        return recipePrep;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipePrep(String recipePrep) {
        this.recipePrep = recipePrep;
    }
}
