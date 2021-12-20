/**
 *@CenkOlcay
 *java version 16.0.1
 */
package oburr.searching.webSearching;

import java.util.ArrayList;

import oburr.searching.AbstractRecipe;
import oburr.searching.Ingredient;
import oburr.user.User;

public class WebRecipe extends AbstractRecipe {

    /**
     * defines the properties of the recipes that will be scrapped from the web resources
     */

    private String recipeResource, imageUrl;

    public WebRecipe(String recipeName, String recipeResource, String imageUrl, ArrayList<Ingredient> recipeIngredients,
                     String recipeSteps, String timeInfo, String nutritionFacts, User user){

        super(recipeName, recipeIngredients, recipeSteps, timeInfo, 0, 0, nutritionFacts, user);
        setRecipeResource(recipeResource);
        setImageUrl(imageUrl);

        setTotalTime(readTime(timeInfo));
        setCalories(readCalories(nutritionFacts));
    }

    /**
     * mutator for recipeResource
     * @param recipeResource
     */
    public void setRecipeResource(String recipeResource){
        this.recipeResource = recipeResource;
    }

    /**
     * mutator for imageUrl
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}


    /**
     * reads from nutrtionsFacts and sets the calories
     * @param nutritionFacts
     * @return
     */
    public int readCalories(String nutritionFacts){
        int caloriesIntake = 0;


        int caloriesIndex = nutritionFacts.indexOf("calories");

            nutritionFacts = nutritionFacts.toLowerCase();

            if (caloriesIndex >= 0) {
                int index = 0;
                while (!Character.isDigit(nutritionFacts.charAt(index))) {
                    index++;
                }

                caloriesIntake = (int) Double.parseDouble(nutritionFacts.substring(index, caloriesIndex));
            }


        return caloriesIntake;
    }


    /**
     * reads from timeInfo and sets the totalTime
     * @param timeInfo
     * @return
     */
    public int readTime(String timeInfo){
        int time = 0;

        timeInfo = timeInfo.toLowerCase();

        if(timeInfo.indexOf('h') > 0){

            time += Integer.parseInt(timeInfo.substring(0,timeInfo.indexOf(" ")));
            time *= 60;

            if(timeInfo.indexOf('m') > 0){

                timeInfo = timeInfo.substring(timeInfo.indexOf("h"));

                time += Integer.parseInt(timeInfo.substring(timeInfo.indexOf(" ")+1,timeInfo.indexOf("m")-1));
            }

        }

        else if(timeInfo.indexOf('m') > 0){
            time += Integer.parseInt(timeInfo.substring(0,timeInfo.indexOf(" ")));
        }


        return time;
    }

    /**
     * accessor for recipeResource
     * @return
     */
    public String getRecipeResource(){ return recipeResource;}

    /**
     * accessor for imageUrl
     * @return
     */
    public String getImageUrl(){ return imageUrl;}



}
