package api.version_2_3_0.testdata.words;


public class FoodPrepVerbs  implements RandomItemsArray {

    public static final String[] items ={
        "chop", "mash", "cook", "freeze", "fry", "glaze", "boil", "blend", "season", "roast", "wash", "unwrap", "clean",
    };

    @Override
    public String[] getItems() {
        return items;
    }
}