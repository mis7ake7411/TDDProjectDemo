package com.mis7ake7411.tddprojectdemo.enums;

import lombok.Getter;

@Getter
public enum SearchOperationEnum {
    EQUAL("="),
    NOT_EQUAL("!="),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    IN("IN"),
    NOT_IN("NOT IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    BETWEEN("BETWEEN");

    private final String operator;

    SearchOperationEnum(String operator) {
        this.operator = operator;
    }
}
