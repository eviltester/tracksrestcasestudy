package api.version_2_3_0.testdata;

// Quick hack random word
public class RandomDataGenerator {
    public String randomWord() {
        return String.valueOf(System.currentTimeMillis()).
                replaceAll("1", "a").
                replaceAll("2", "d").
                replaceAll("3", "o").
                replaceAll("4", "n").
                replaceAll("5", "e").
                replaceAll("6", "r").
                replaceAll("7", "t").
                replaceAll("8", "h").
                replaceAll("9", "i").
                replaceAll("0", "s");
    }
}
