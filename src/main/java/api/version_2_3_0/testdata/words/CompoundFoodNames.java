package api.version_2_3_0.testdata.words;

public class CompoundFoodNames implements RandomItemsArray {

    public static final String[] items ={
            "pie", "pudding", "surprise", "stew", "platter"
    };

    @Override
    public String[] getItems() {
        return items;
    }
}