package messages;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class XbatisPluginBundle extends AbstractBundle {

    private XbatisPluginBundle() {
        super(PATH_TO_BUNDLE);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key, @NotNull Object... params) {
        return ourInstance.getMessage(key, params);
    }

    @NonNls
    private static final String PATH_TO_BUNDLE = "messages.XbatisPluginBundle";
    private static final XbatisPluginBundle ourInstance = new XbatisPluginBundle();


}
