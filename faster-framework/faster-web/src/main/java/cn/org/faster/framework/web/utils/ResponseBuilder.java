package cn.org.faster.framework.web.utils;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.web.exception.model.BasisErrorCode;
import cn.org.faster.framework.web.exception.model.ErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import cn.org.faster.framework.web.exception.model.ResultError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author zhangbowen
 * @since 2018/12/10
 */
public class ResponseBuilder {
    /**
     * 200
     *
     * @param body 内容
     * @param <T>  泛型
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<T> ok(T... body) {
        return ResponseEntity.ok(Utils.safeElement(body, 0));
    }

    /**
     * 201
     *
     * @param body 内容
     * @param <T>  泛型
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<T> created(T... body) {
        return new ResponseEntity<>(Utils.safeElement(body, 0), HttpStatus.CREATED);
    }

    /**
     * 204
     *
     * @param body 内容
     * @param <T>  泛型
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<T> noContent(T... body) {
        return new ResponseEntity<>(Utils.safeElement(body, 0), HttpStatus.NO_CONTENT);
    }

    /**
     * other success
     *
     * @param httpStatus 状态码
     * @param body 内容
     * @param <T>  泛型
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<T> success(HttpStatus httpStatus, T... body) {
        return new ResponseEntity<>(Utils.safeElement(body, 0), httpStatus);
    }

    /**
     * 400错误提交
     *
     * @param message 提示信息
     * @return ResponseEntity
     */
    public static ResponseEntity<ResultError> badRequest(String... message) {
        return ResponseErrorEntity.error(new ResultError(HttpStatus.BAD_REQUEST.value(), Utils.safeElement(message, 0)), HttpStatus.BAD_REQUEST);
    }

    /**
     * 400参数错误
     *
     * @param message 提示信息
     * @return ResponseEntity
     */
    public static ResponseEntity<ResultError> badArgument(String... message) {
        return ResponseErrorEntity.error(new ResultError(BasisErrorCode.VALIDATION_FAILED.getValue(), Utils.safeElement(message, 0)), HttpStatus.BAD_REQUEST);
    }

    /**
     * 404
     *
     * @param message 提示信息
     * @return ResponseEntity
     */
    public static ResponseEntity<ResultError> notFound(String... message) {
        return ResponseErrorEntity.error(new ResultError(HttpStatus.NOT_FOUND.value(), Utils.safeElement(message, 0)), HttpStatus.NOT_FOUND);
    }

    /**
     * other error message
     *
     * @param message    提示信息
     * @param httpStatus 状态码
     * @return ResponseEntity
     */
    public static ResponseEntity<ResultError> error(HttpStatus httpStatus, String... message) {
        return ResponseErrorEntity.error(new ResultError(httpStatus.value(), Utils.safeElement(message, 0)), httpStatus);
    }

    /**
     * other error message
     *
     * @param httpStatus 状态码
     * @param errorCode  错误码
     * @return ResponseEntity
     */
    public static ResponseEntity<ResultError> error(ErrorCode errorCode, HttpStatus httpStatus) {
        return ResponseErrorEntity.error(new ResultError(errorCode, httpStatus), httpStatus);
    }
}
