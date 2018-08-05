import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.nchen.morphine.Morphine;
import com.nchen.morphine.MorphineManager;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Started...");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost/test");
        MorphineManager.initMorphine(dataSource);
        Morphine morphine = MorphineManager.getMorphine();
        morphine.setScanPackage("com.nchen.morphine");
        morphine.build();
    }
}
