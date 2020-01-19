package rest_api.model;

public class RatesAll {

    private String currancy;
    private String code;
    private String mid;

    public String getCurrancy() {
        return currancy;
    }

    public String getCode() {
        return code;
    }

    public String getMid() {
        return mid;
    }

    @Override
    public String toString() {
        return "RatesAll{" +
                "currancy='" + currancy + '\'' +
                ", code='" + code + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
