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

        Schema schema = new Schema(11, "com.sensu.android.zimaogou.external.greendao.model");

        addUserInfo(schema);
        addSearchKeyword(schema);
        addAddressDefault(schema);


        schema.setDefaultJavaPackageDao("com.sensu.android.zimaogou.external.greendao.dao");
        schema.enableKeepSectionsByDefault();
        new DaoGenerator().generateAll(schema, "../ZiMaoGou/src");
    }

    public static void addUserInfo(Schema schema) {
        Entity userInfo = schema.addEntity("UserInfo");
        userInfo.addStringProperty("name");
        userInfo.addStringProperty("sex");
        userInfo.addStringProperty("mobile");
        userInfo.addStringProperty("uid");
        userInfo.addStringProperty("avatar");
        userInfo.addStringProperty("email");
        userInfo.addStringProperty("token");
        userInfo.addStringProperty("isLogin");
    }

    public static void addSearchKeyword(Schema schema) {
        Entity keyword = schema.addEntity("SearchKeyword");
        keyword.addStringProperty("keyword");
    }

    public static void addAddressDefault(Schema schema) {
        Entity addressDefault = schema.addEntity("AddressDefault");
        addressDefault.addStringProperty("id");
        addressDefault.addStringProperty("name");
        addressDefault.addStringProperty("mobile");
        addressDefault.addStringProperty("id_card");
        addressDefault.addStringProperty("province_id");
        addressDefault.addStringProperty("city_id");
        addressDefault.addStringProperty("district_id");
        addressDefault.addStringProperty("province");
        addressDefault.addStringProperty("city");
        addressDefault.addStringProperty("district");
        addressDefault.addStringProperty("address");
        addressDefault.addStringProperty("is_default");
    }
}
