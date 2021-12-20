package oburr.user;


import oburr.searching.Ingredient;

import java.util.ArrayList;

public class Tester {

    public static final String[] PROTEIN_BASED_BANS = { "sugar", "soda", "candy", "margarine", "bread", "cracker" };
    public static final String[] KETO_BANS = { "Bean", "Beer", "Fruit", "Lentil", "Liquor", "Mayonnaise", "Pasta", "Potato", "Pudding", "Rice", "Sugar", "Wine" };
    public static final String[] LOW_CARB_BANS = { "Bread", "Candy", "Ice cream", "Margarine", "Pasta", "Rice", "Sugar" };
    public static final String[] GLUTEN_FREE_BANS = { "Biscuit", "Bread", "Cake", "Cereal", "Cracker", "Pasta", "Pie" };
    public static final String[] VEGETARIAN_BANS = { "Beef", "Chicken", "Duck", "Gelatin", "haddock", "Pork", "Salmon", "shrimp", "Trout", "Tuna", "tuna", "Turkey", "Veal" };
    public static final String[] VEGAN_BANS = { "Beef", "Butter", "Cheese", "Chicken", "Cream", "Duck", "Egg", "Gelatin", "haddock", "honey", "Ice cream", "Mayonnaise", "Milk", "Pork", "Salmon", "shrimp", "Trout", "Tuna", "tuna", "Turkey", "Veal" };
    public static final String[] PALEO_BANS = { "bean", "bread", "butter", "candy", "cheese", "lentil", "margarine", "milk", "pasta", "sugar", "wheat", "yogurt" };
    public static final String[] LACTOSE_FREE_BANS = { "butter", "buttermilk", "cheese", "cream", "ice cream", "kefir", "milk", "sour cream", "yogurt" };


    static DBCaller connector = new DBCaller();
    static User user;



    public static boolean login( String userName, String userPassword) {
        String query = "SELECT * FROM OS2g280ES9.oburr_userInfo where user_name = ";
        query = query + "'" + userName + "' AND user_password = '" + userPassword + "'";
        if( connector.checkDB( query ) ){
            user = new User( userName, userPassword );
            user.setUserId( connector.returnId( user ) );
            user.setDislikedIngredients( dislikesFromDatabase() );
            user.setLikedIngredients( likesFromDatabase() );
            user.setAllergenIngredients( allergensFromDatabase() );
            user.setBannedIngredients( bannedItemsFromDatabase() );
            return true;
        }
        else{
            //error message: account does not exist
            return false;
        }
    }

    public static boolean register( String userName, String userPassword ) {

        String query = "SELECT * FROM OS2g280ES9.oburr_userInfo where user_name = ";
        query = query + "'" + userName + "'";
        if ( !connector.checkDB( query ) ){
            user = new User( userName, userPassword );
            connector.insertUser( user );
            user.setUserId( connector.returnId( user ) );
            connector.insertUserLikesAndDislikes( user );
            return true;

        }
        else{
            //error message: username is already used
            return false;
        }

    }

    public static void changeBannedItems( String dietType ){
        initializeBannedItems( dietType );
    }
    public static void changeDietType( String dietType ){
        user.setDietType( dietType );
    }
    public static void removeDietType(){
        user.setDietType( "" );
    }
    public static void updateDietTypeInfo(){
        connector.updateDietType( user );
    }
    public static void addAllergenItem( String item ){
        user.updateAllergenItems( new Ingredient( item ) );
    }
    public static void addLikedItem( String item ){
        user.updateLikedItems( new Ingredient( item ));
    }
    public static void addDislikedItem( String item ){
        user.updateDislikedItems( new Ingredient( item ));
    }
    public static void removeAllergenItem( String item ){
        user.deleteAllergenItem( item );
    }
    public static void removeLikedItem( String item ){
        user.deleteLikedItem( item );
    }
    public static void removeDislikedItem( String item ){
        user.deleteDislikedItem( item );
    }
    public static void updateAllergenInfo(){
        connector.updateAllergens( user );
    }
    public static void updateLikedInfo(){
        connector.updateLiked( user );
    }
    public static void updateDislikedInfo(){
        connector.updateDisliked( user );
    }


    public static ArrayList<Ingredient> initializeBannedItems( String dietType){
        ArrayList<Ingredient> tmp = new ArrayList<Ingredient>();
        if( dietType.equalsIgnoreCase( "Protein-based" ) ){
            for( String ingredient: PROTEIN_BASED_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "keto" ) ){
            for( String ingredient: KETO_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "low carb" ) ){
            for( String ingredient: LOW_CARB_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "gluten free" ) ){
            for( String ingredient: GLUTEN_FREE_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "vegetarian" ) ){
            for( String ingredient: VEGETARIAN_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "vegan" ) ){
            for( String ingredient: VEGAN_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "paleo" ) ){
            for( String ingredient: PALEO_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        else if( dietType.equalsIgnoreCase( "lactose free" ) ){
            for( String ingredient: LACTOSE_FREE_BANS){
                tmp.add( new Ingredient( ingredient ) );
            }
        }
        System.out.println( tmp );
        return tmp;
    }
    public static ArrayList<Ingredient> bannedItemsFromDatabase(){
        String dietType = connector.returnDietType( user );
        return initializeBannedItems( dietType );
    }

    public static String dietTypeFromDatabase(){
        return connector.returnDietType( user );
    }
    public static ArrayList<Ingredient> allergensFromDatabase(){
        String allergens = connector.returnAllergens( user );
        System.out.println(allergens);
        ArrayList<Ingredient> tmp = new ArrayList<Ingredient>();
        int firstIndex = -1;
        int secondIndex = allergens.indexOf( "," );
        while( secondIndex != -1 ){
            String tmpStr = allergens.substring( firstIndex + 1, secondIndex );
            firstIndex = secondIndex;
            secondIndex = allergens.indexOf( ",", firstIndex + 1 );
            tmp.add( new Ingredient( tmpStr ) );
        }
        return tmp;
    }
    public static ArrayList<Ingredient> likesFromDatabase(){
        String likes = connector.returnLikes( user );
        System.out.println(likes);
        ArrayList<Ingredient> tmp = new ArrayList<Ingredient>();
        int firstIndex = -1;
        int secondIndex = likes.indexOf( "," );
        while( secondIndex != -1 ){
            String tmpStr = likes.substring( firstIndex + 1, secondIndex );
            firstIndex = secondIndex;
            secondIndex = likes.indexOf( ",", firstIndex + 1 );
            tmp.add( new Ingredient( tmpStr ) );
        }
        return tmp;
    }
    public static ArrayList<Ingredient> dislikesFromDatabase(){
        String dislikes = connector.returnDislikes( user );
        System.out.println(dislikes);
        ArrayList<Ingredient> tmp = new ArrayList<Ingredient>();
        int firstIndex = -1;
        int secondIndex = dislikes.indexOf( "," );
        while( secondIndex != -1 ){
            String tmpStr = dislikes.substring( firstIndex + 1, secondIndex );
            firstIndex = secondIndex;
            secondIndex = dislikes.indexOf( ",", firstIndex + 1 );
            tmp.add( new Ingredient( tmpStr ) );
        }
        return tmp;
    }
}
