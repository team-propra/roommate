package com.example.roommate.frontend.utility;

import org.springframework.lang.NonNull;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestModel implements Model {

    HashMap<String, Object> map = new HashMap<>();

    @Override
    @NonNull
    public Model addAttribute(@NonNull String attributeName, Object attributeValue) {
        map.put(attributeName,attributeValue);
        return this;
    }

    @Override
    @NonNull
    public Model addAttribute(@NonNull Object attributeValue) {
        throw new Error("lets not use this one");
    }
    
    @Override
    @NonNull
    public Model addAllAttributes(@NonNull Collection<?> attributeValues) {
        throw new Error("lets not use this one");
    }

    @Override
    @NonNull
    public Model addAllAttributes(@NonNull Map<String, ?> attributes) {
        throw new Error("lets not use this one");
    }

    @Override
    @NonNull
    public Model mergeAttributes(@NonNull Map<String, ?> attributes) {
        throw new Error("lets not use this one");
    }

    @Override
    public boolean containsAttribute(@NonNull String attributeName) {
        throw new Error("lets not use this one");
    }

    @Override
    public Object getAttribute(@NonNull String attributeName) {
        throw new Error("lets not use this one");
    }

    @Override
    @NonNull
    public Map<String, Object> asMap() {
        return map;
    }
}
