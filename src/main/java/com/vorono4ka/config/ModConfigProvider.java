package com.vorono4ka.config;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModConfigProvider implements SimpleConfig.DefaultConfig {
    private final List<Pair<String, ?>> values = new ArrayList<>();
    private boolean isDirty = false;
    private String content;

    public void add(String key, Object value) {
        values.add(new Pair<>(key, value));
        isDirty = true;
    }

    @Override
    public String get(String namespace) {
        if (isDirty) {
            StringBuilder builder = new StringBuilder();

            for (Pair<String, ?> pair : values) {
                builder.append(pair.getFirst()).append("=").append(pair.getSecond());
                builder.append("\n");
            }

            content = builder.toString();
            isDirty = false;
        }
        return content;
    }
}
