package api.version_2_3_0.testdata.words;

public class GenericVerbs  implements RandomItemsArray {
    public static final String[] items ={
            "buy", "find", "purchase",
            "research the best", "find out what is"
    };

    @Override
    public String[] getItems() {
        return items;
    }
}
