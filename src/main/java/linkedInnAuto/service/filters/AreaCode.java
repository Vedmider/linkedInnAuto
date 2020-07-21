package linkedInnAuto.service.filters;

public enum AreaCode {
    USA("us:0"),
    CANADA("ca:0");

    private final String areaCode;
    AreaCode(String code){
        this.areaCode = code;
    }

    public String getAreaCode() {
        return areaCode;
    }
}
