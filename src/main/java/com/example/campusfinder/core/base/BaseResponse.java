package com.example.campusfinder.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.example.campusfinder.core.base
 * fileName       : BaseResponse
 * author         : tlswl
 * date           : 2024-08-17
 * description    : API Response TYPE 설정
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-17        tlswl       최초 생성
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
final public class BaseResponse<T> {

    // 커스텀 상태 코드
    private final int status;

    // 응답 메시지
    private final String description;

    // 실제 응답 데이터
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    // 성공 메시지 상수
    private static final String SUCCESS_MESSAGE = "SUCCESS";

    /**
     * 데이터를 포함한 성공 응답 생성
     *
     * @param <T>    the type parameter
     * @param status the status
     * @param data   the data
     * @return the base response
     */
    public static <T> BaseResponse<T> ofSuccess(int status, T data) {
        return new BaseResponse<>(status, SUCCESS_MESSAGE, data);
    }

    /**
     * 데이터를 포함하지 않는 성공 응답 생성
     *
     * @param status the status
     * @param description the description
     * @return the base response
     */
    public static BaseResponse<Void> ofSuccessWithoutData(int status, String description) {
        return new BaseResponse<>(status, description, null);
    }

    /**
     * 실패 응답 생성
     *
     * @param <T>          the type parameter
     * @param status       the status
     * @param description  the description
     * @return the base response
     */
    public static <T> BaseResponse<T> ofFail(int status, String description) {
        return new BaseResponse<>(status, description, null);
    }

    /**
     * 커스텀 실패 응답 생성
     *
     * @param customStatus the custom status
     * @param description      the description
     * @return the base response
     */
    public static BaseResponse<Void> ofCustomFail(int customStatus, String description) {
        return new BaseResponse<>(customStatus, description, null);
    }

    // 오류 응답을 생성하는 메서드
    public static <T> BaseResponse<T> ofError(int status, String description) {
        return new BaseResponse<>(status, description, null);
    }
}
