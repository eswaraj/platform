package com.next.eswaraj.admin.jsf.bean;

import org.primefaces.model.map.Overlay;

public class KmlLayer extends Overlay {

    private static final long serialVersionUID = 1L;
    private boolean clickable;
    private boolean preserveViewport;
    private boolean screenOverlays;
    private boolean suppressInfoWindows;
    private String url;
    private int zIndex;

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isPreserveViewport() {
        return preserveViewport;
    }

    public void setPreserveViewport(boolean preserveViewport) {
        this.preserveViewport = preserveViewport;
    }

    public boolean isScreenOverlays() {
        return screenOverlays;
    }

    public void setScreenOverlays(boolean screenOverlays) {
        this.screenOverlays = screenOverlays;
    }

    public boolean isSuppressInfoWindows() {
        return suppressInfoWindows;
    }

    public void setSuppressInfoWindows(boolean suppressInfoWindows) {
        this.suppressInfoWindows = suppressInfoWindows;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
