package vn.tayjava.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    NEW(1),
    PENDING(2),
    ACCEPTED(3),
    REJECTED(4),
    PROCESSING(5),
    CANCELED(6),
    PAID(7),
    CLOSED(0);

    private final int value;
}
