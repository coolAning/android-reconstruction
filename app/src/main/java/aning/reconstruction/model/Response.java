package aning.reconstruction.model;


public class Response {
    private int code;
    private String msg;
    private Object data;

    public Response() {
        //default
    }
    public Response(int code, String msg) {
        this();
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
