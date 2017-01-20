package gweltaz.calori.channelmessaging;

/**
 * Created by calorig on 20/01/2017.
 */
public class Connect
{
    private String response;
    private int code;
    private String accesstoken;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public Connect(String response, int code, String accesstoken) {
        this.response = response;
        this.code = code;
        this.accesstoken = accesstoken;
    }

    @Override
    public String toString() {
        return "Connect{" +
                "response='" + response + '\'' +
                ", code=" + code +
                ", accesstoken='" + accesstoken + '\'' +
                '}';
    }
}
