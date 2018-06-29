import com.nchen.morphine.Morphine;
import com.sun.org.apache.bcel.internal.generic.MONITORENTER;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Started");
        Morphine morphine = Morphine.create().build();
    }
}
