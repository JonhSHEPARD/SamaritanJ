package io.github.jonhshepard.samaritanj.connect;

/**
 * @author JonhSHEPARD
 */
public class ResponseObject {

    private final int code;
    private final String data;

    ResponseObject(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

}
