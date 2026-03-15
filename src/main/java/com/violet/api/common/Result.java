package com.violet.api.common;

// <T> 代表泛型，意思是这个盒子里装的 data 可以是任何类型（比如 String, User, List 等等）
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 私有构造函数，强迫大家使用下面提供的快捷方法
    private Result() {}

/*    // 快捷方法 1：成功，并且有数据返回
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }*/

//    // 快捷方法 2：成功，但只返回一句提示，不需要具体数据
//    public static <T> Result<T> successMsg(String msg) {
//        Result<T> result = new Result<>();
//        result.code = 200;
//        result.msg = msg;
//        return result;
//    }

    // 快捷方法 3：失败报错
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
    // 泛型方法：<T> 代表它可以接收任何类型的数据，不仅仅是 Map
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // Get 和 Set 方法 (如果用 Lombok 加个 @Data 注解就行，这里我们手写一下)
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}