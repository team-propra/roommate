package com.example.roommate.utility;

import java.util.List;
import java.util.stream.StreamSupport;

public class IterableSupport {
    public static <T> List<T> toList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false).toList();
    }
}
