package group.exception;

public enum ExceptionKind {

    DATABASE_NOT_FOUND(99, "数据库无此字段"),
    REQUEST_INFO_ERROR(103, "请求信息错误"),
    ENOUGH_PEOPLE(401, "需要人数已满"),
    FILE_SAVE_ERROR(501,"服务端文件储存失败");

    ExceptionKind(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public final int code;
    public final String msg;
}
