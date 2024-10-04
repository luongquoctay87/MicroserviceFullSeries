package vn.tayjava.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Keyword {
    SORT_BY("(\\w+?)(:)(.*)");

    private String value;
}
