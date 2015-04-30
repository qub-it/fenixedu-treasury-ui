package org.fenixedu.treasury.ui.tld;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.standards.geographic.Place;

public class PlaceNameTag extends SimpleTagSupport {
    //stores the value of the attribute of the custom tag
    private Place place;

    @Override
    public void doTag() throws JspException, IOException {
        //this is the method that is called when the custom tag is called from a JSP page 
        String name = (place != null) ? place.getLocalizedName(I18N.getLocale()) : new String();
        getJspContext().getOut().write(name);
    }

    public void setPlace(Place place) {
        //the method name must match the attribute name defined in the TLD file
        this.place = place;
    }
}