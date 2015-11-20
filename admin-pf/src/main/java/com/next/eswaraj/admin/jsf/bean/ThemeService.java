package com.next.eswaraj.admin.jsf.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "application")
public class ThemeService {

    private String[] themes = { "afterdark", "afternoon", "afterwork", "aristo", "black-tie", "blitzer", "bluesky", "bootstrap", "casablanca", "cupertino", "cruze", "dark-hive", "delta", "dot-luv",
            "eggplant", "excite-bike", "flick", "glass-x", "home", "hot-sneaks", "humanity", "le-frog", "metroui", "midnight", "mint-choc", "overcast", "pepper-grinder", "redmond", "rocket", "sam",
            "smoothness", "south-street", "start", "sunny", "swanky-purse", "trontastic", "ui-darkness", "ui-lightness", "vader" };

    public String[] getThemes() {
        return themes;
    }
}
