import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Administrator on 2015/11/11.
 */
public class DaoGeneratorDemo {

    //Android 数据库的第三方greenDao的使用

    public static void main(String[] args) throws Exception {
        System.out.println("Hello DaoGenerator");

        // first parameter for version, <span></span> second for default
        // generate package
        // Schema对象接受2个参数，第一个参数是DB的版本号，通过更新版本号来更新数据库。第二个参数是自动生成代码的包路径

        Schema schema = new Schema(11, "xxxx");

        addUserInfo(schema);


        schema.setDefaultJavaPackageDao("com.wordoor.andr.external.greendao.dao");
        schema.enableKeepSectionsByDefault();
        new DaoGenerator().generateAll(schema, "../../zimaogou/src");
    }

    public static void addUserInfo(Schema schema) {
        Entity userInfo = schema.addEntity("classname");
        userInfo.addStringProperty("xxx");
    }
}
