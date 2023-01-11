package group.exception;

public enum ExceptionKind {

    DATABASE_NOT_FOUND(99, "数据库无此字段"),
    REQUEST_INFO_ERROR(103, "请求信息错误"),
    ENOUGH_PEOPLE(401, "需要人数已满"),
    ALREADY_PARTICIPATE(403,"任务已接"),
    SOCKET_CONNECTION_ERROR(703,"与Socket通信异常"),
    NO_LESSONS_INFO(705,"未导入课程表信息"),
    SAME_FILE_ERROR(501,"已含同名文件");



    ExceptionKind(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public final int code;
    public final String msg;
}
