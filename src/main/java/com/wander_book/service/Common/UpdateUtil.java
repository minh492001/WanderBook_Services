package com.wander_book.service.Common;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class UpdateUtil {
    // Cập nhật nếu giá trị không null
    public static <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    // Cập nhật nếu giá trị nguyên dương
    public static void updateIfPositive(int newValue, IntConsumer setter) {
        if (newValue > 0) {
            setter.accept(newValue);
        }
    }

    // Cập nhật nếu giá trị không âm
    public static void updateIfNonNegative(int newValue, IntConsumer setter) {
        if (newValue >= 0) {
            setter.accept(newValue);

        }
    }
}
