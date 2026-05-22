package com.swalllow_erp.common;
import lombok.Getter;

/**
 * 统一状态码枚举
 * @Author: Swallow333
 * @Date: 2026/05/22 19:31
 */

@Getter
public enum CommonCodeEnum {
    // ========== 1xx-9xx: HTTP 状态码 ==========
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "请求资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    SERVER_ERROR(500, "服务器内部错误"),
    // ========== 1xxx-9xxx: 通用业务 ==========
    PARAM_IS_EMPTY(1001, "参数不能为空"),
    PARAM_TYPE_ERROR(1002, "参数类型错误"),
    PARAM_FORMAT_ERROR(1003, "参数格式错误"),
    DATA_NOT_EXIST(1101, "数据不存在"),
    DATA_SAVE_ERROR(1102, "数据保存失败"),
    DATA_UPDATE_ERROR(1103, "数据更新失败"),
    DATA_DELETE_ERROR(1104, "数据删除失败"),
    DATA_DUPLICATE(1105, "数据已存在"),
    FILE_UPLOAD_ERROR(1201, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(1202, "文件下载失败"),
    FILE_FORMAT_ERROR(1203, "文件格式错误"),
    FILE_SIZE_EXCEED(1204, "文件大小超限"),
    // ========== 1xxxx-2xxxx: 用户模块 ==========
    USER_NOT_EXIST(10001, "用户不存在"),
    USER_PASSWORD_ERROR(10002, "密码错误"),
    USER_DISABLED(10003, "账号已被禁用"),
    USERNAME_EXIST(10004, "用户名已存在"),
    USER_LOCKED(10005, "账号已被锁定"),
    USER_EXPIRED(10006, "账号已过期"),
    PHONE_EXIST(10007, "手机号已存在"),
    EMAIL_EXIST(10008, "邮箱已存在"),
    USER_NOT_LOGIN(10009, "用户未登录"),
    LOGIN_EXPIRED(10010, "登录已过期"),
    OLD_PASSWORD_ERROR(10011, "原密码错误"),
    NEW_PASSWORD_SAME(10012, "新密码不能与旧密码相同"),
    // ========== 2xxxx-3xxxx: 订单模块 ==========
    ORDER_NOT_EXIST(20001, "订单不存在"),
    ORDER_STATUS_ERROR(20002, "订单状态错误"),
    ORDER_CANNOT_CANCEL(20003, "订单无法取消"),
    ORDER_CANNOT_PAY(20004, "订单无法支付"),
    ORDER_CANNOT_SHIP(20005, "订单无法发货"),
    ORDER_CANNOT_CONFIRM(20006, "订单无法确认"),
    ORDER_CLOSED(20007, "订单已关闭"),
    ORDER_COMPLETED(20008, "订单已完成"),
    ORDER_PAYMENT_TIMEOUT(20009, "订单支付超时"),
    ORDER_REFUND_ERROR(20010, "订单退款失败"),
    ORDER_REFUND_COMPLETE(20011, "订单退款完成"),
    // ========== 系统级 ==========
    SYSTEM_BUSY(90001, "系统繁忙，请稍后重试"),
    DB_ERROR(90002, "数据库异常"),
    NETWORK_ERROR(90003, "网络异常"),
    THIRD_PARTY_ERROR(90004, "第三方服务异常");
    private final int code;
    private final String message;
    CommonCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    /**
     * 根据 code 获取枚举
     */
    public static CommonCodeEnum getByCode(int code) {
        for (CommonCodeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }
    /**
     * 判断是否客户端错误（4xx）
     */
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }
    /**
     * 判断是否服务器错误（5xx）
     */
    public boolean isServerError() {
        return code >= 500 && code < 600;
    }
}