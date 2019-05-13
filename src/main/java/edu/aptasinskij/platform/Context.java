package edu.aptasinskij.platform;

import java.util.HashMap;
import java.util.Map;

public interface Context {

    Object get(String name);

    void register(String name, Object object);

    static <T> T get(Context context, Class<T> type, String name) {
        return type.cast(context.get(name));
    }

    static Context context() {
        return new ContextImpl();
    }

    final class ContextImpl implements Context {

        private final Map<String, Object> registry = new HashMap<>();

        @Override
        public Object get(String name) {
            return registry.get(name);
        }

        @Override
        public void register(String name, Object object) {
            registry.put(name, object);
        }

    }

}
