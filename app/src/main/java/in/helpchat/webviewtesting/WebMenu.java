package in.helpchat.webviewtesting;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by adarshpandey on 8/7/16.
 */
public class WebMenu {

    public List<Menu> menus;

    public static class Menu {
        public int id;
        public int overflow;
        public String name;
        public String action;
        public String imageUrl;
        public String iconId;

        public transient Drawable drawable;

    }
}
