package api.version_2_3_0.synchronisation;

public class Wait {
    public static void aFewSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
