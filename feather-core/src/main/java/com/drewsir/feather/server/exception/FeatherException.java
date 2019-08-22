package com.drewsir.feather.server.exception;


import com.drewsir.feather.server.enums.StatusEnum;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/4/25 15:26
 * @since JDK 1.8
 */
public class FeatherException extends GenericException {


    public FeatherException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public FeatherException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public FeatherException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public FeatherException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public FeatherException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public FeatherException(Exception oriEx) {
        super(oriEx);
    }

    public FeatherException(Throwable oriEx) {
        super(oriEx);
    }

    public FeatherException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public FeatherException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }

}
