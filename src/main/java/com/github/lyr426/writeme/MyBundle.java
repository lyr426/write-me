package com.github.lyr426.writeme;

import com.intellij.DynamicBundle;
import com.intellij.openapi.util.NlsSafe;
import java.util.function.Supplier;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

public final class MyBundle extends DynamicBundle {

    @NonNls
    private static final String BUNDLE = "messages.MyBundle";
    private static final MyBundle INSTANCE = new MyBundle();

    private MyBundle() {
        super(BUNDLE);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    public static Supplier<@NlsSafe String> messagePointer(
      @PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return () -> INSTANCE.getMessage(key, params);
    }

}
