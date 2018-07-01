import com.nchen.morphine.Morphine;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Started");
        Morphine morphine = Morphine.create();
        morphine.setScanPackage("com.nchen.morphine");
        morphine.setDbUrl("localhost/test");
        morphine.build();
    }
}
