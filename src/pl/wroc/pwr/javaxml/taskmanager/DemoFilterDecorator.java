package pl.wroc.pwr.javaxml.taskmanager;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import pl.wroc.pwr.javaxml.taskmanager.TaskTableUI.State;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;

class DemoFilterDecorator implements FilterDecorator, Serializable {

    public String getEnumFilterDisplayName(Object propertyId, Object value) {
        if ("state".equals(propertyId)) {
            State state = (State) value;
            switch (state) {
            case TODO:
                return "Task has been created";
            case IN_PROGRESS:
                return "Task is in progress";
            case DONE:
                return "Task is finished";
            }
        }
        // returning null will output default value
        return null;
    }

    public Resource getEnumFilterIcon(Object propertyId, Object value) {
        if ("state".equals(propertyId)) {
            State state = (State) value;
            switch (state) {
            case TODO:
                return new ThemeResource("../runo/icons/16/document.png");
            case IN_PROGRESS:
                return new ThemeResource("../runo/icons/16/reload.png");
            case DONE:
                return new ThemeResource("../runo/icons/16/globe.png");
            }
        }
        return null;
    }

    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? "Validated" : "Not validated";
        }
        // returning null will output default value
        return null;
    }

    public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? new ThemeResource("../runo/icons/16/ok.png")
                    : new ThemeResource("../runo/icons/16/cancel.png");
        }
        return null;
    }

    public String getFromCaption() {
        return "Start date:";
    }

    public String getToCaption() {
        return "End date:";
    }

    public String getSetCaption() {
        // use default caption
        return null;
    }

    public String getClearCaption() {
        // use default caption
        return null;
    }

    public boolean isTextFilterImmediate(Object propertyId) {
        // use text change events for all the text fields
        return true;
    }

    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    public String getAllItemsVisibleString() {
        return "Show all";
    }

    public Resolution getDateFieldResolution(Object propertyId) {
        return Resolution.DAY;
    }

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("fi",
                "FI"));
    }

    public boolean usePopupForNumericProperty(Object propertyId) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getDateFormatPattern(Object propertyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        // TODO Auto-generated method stub
        return null;
    }
}
