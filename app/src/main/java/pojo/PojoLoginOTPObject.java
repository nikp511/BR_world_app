package pojo;

public class PojoLoginOTPObject {

    String phoneno, isAndroid, deviceToken, type;

    public PojoLoginOTPObject(String isAndroid, String phoneno, String deviceToken, String type) {

        this.phoneno = phoneno;
        this.isAndroid = isAndroid;
        this.deviceToken = deviceToken;
        this.type = type;

    }

}
