package com.seklyza.skywars.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineManager {
    private final List<String> lines = new ArrayList<>();

    private LineManager() {
    }

    public Map<Integer, String> build() {
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0, n = lines.size(); i < n; i++) {
            String line = lines.get(i);
            if (line.equals("{NEW_LINE}")) {
                line = "";
                for (int j = 0; j < i; j++) {
                    line += " ";
                }
            }
            result.put(n - i, line);
        }

        return result;
    }

    public LineManager add(String line, Object... args) {
        lines.add(String.format(line, args));

        return this;
    }

    public LineManager newLine() {
        add("{NEW_LINE}");

        return this;
    }

    public static LineManager builder() {
        return new LineManager();
    }
}
