package cn.org.faster.framework.web.exception.model;

import cn.org.faster.framework.web.exception.BusinessException;

/**
 * @author zhangbowen
 */
public interface ErrorCode {
    int getValue();

    String getDescription();

    default void throwException(String retMsg) throws BusinessException {
        throw new BusinessException(this.getValue(), retMsg);
    }

    default void throwException() throws BusinessException {
        throw BusinessException.build(this);
    }

    default BusinessException buildException() {
        return BusinessException.build(this);
    }

    default BusinessException buildException(String retMsg) {
        return BusinessException.build(this.getValue(), retMsg);
    }
}
