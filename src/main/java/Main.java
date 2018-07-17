import com.nchen.morphine.Morphine;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Started...");
        Morphine morphine = Morphine.create();
        morphine.setScanPackage("com.nchen.morphine");
        morphine.setDbUrl("jdbc:mysql://localhost/test");
        morphine.build();
    }
}
