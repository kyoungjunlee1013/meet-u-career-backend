package com.highfive.meetu.global.common.converter;

import jakarta.persistence.AttributeConverter;

/**
 * Integer 값을 가지는 Enum을 변환하기 위한 추상 컨버터 클래스
 * @param <T> 변환할 Enum 타입
 */
public abstract class EnumValueConverter<T extends EnumValueConverter.EnumValue> implements AttributeConverter<T, Integer> {

    /**
     * Enum 값을 Integer로 변환
     * @param attribute 변환할 Enum 값
     * @return 변환된 Integer 값
     */
    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    /**
     * Integer 값을 Enum으로 변환 (추상 메서드)
     * @param dbData 변환할 Integer 값
     * @return 변환된 Enum 값
     */
    @Override
    public T convertToEntityAttribute(Integer dbData) {
        return fromValue(dbData);
    }

    /**
     * Integer 값으로부터 특정 Enum 인스턴스를 반환하는 추상 메서드
     * 이 메서드는 각 구체적인 컨버터 클래스에서 구현해야 함
     * @param value 변환할 Integer 값
     * @return 변환된 Enum 인스턴스
     */
    protected abstract T fromValue(Integer value);

    /**
     * 정수 값을 저장할 수 있는 Enum을 위한 인터페이스
     */
    public interface EnumValue {
        /**
         * Enum의 정수 값을 반환
         * @return 정수 값
         */
        int getValue();
    }
}