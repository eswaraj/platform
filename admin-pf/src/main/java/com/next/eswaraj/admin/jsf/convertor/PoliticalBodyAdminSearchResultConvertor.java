package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.next.eswaraj.admin.jsf.bean.TimelineItemBean;

@Component
public class PoliticalBodyAdminSearchResultConvertor implements Converter {

    @Autowired
    private TimelineItemBean timelineItemBean;

    public PoliticalBodyAdminSearchResultConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("Checking : " + value);
                long id = Long.parseLong(value);
                List<PoliticalBodyAdminSearchResult> allAdmins = timelineItemBean.getAllAdmins();
                if (allAdmins != null) {
                    for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : allAdmins) {
                        System.out.println("onePoliticalBodyAdminSearchResult : " + onePoliticalBodyAdminSearchResult.getPerson().getName() + ","
                                + onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
                        if (onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId().equals(id)) {
                            System.out.println("Found in All Admins");
                            return onePoliticalBodyAdminSearchResult;
                        }
                    }
                }
                allAdmins = timelineItemBean.getSelectedAdmins();
                if (allAdmins != null) {
                    for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : allAdmins) {
                        System.out.println("onePoliticalBodyAdminSearchResult : " + onePoliticalBodyAdminSearchResult.getPerson().getName() + ","
                                + onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
                        if (onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId().equals(id)) {
                            System.out.println("Found in Selected Admins");
                            return onePoliticalBodyAdminSearchResult;
                        }
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Election."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            if (object instanceof String) {
                return "";
            }
            PoliticalBodyAdminSearchResult politicalBodyAdminSearchResult = ((PoliticalBodyAdminSearchResult) object);
            System.out.println("getAsString : "+politicalBodyAdminSearchResult.getPerson().getName()+", "+politicalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
            return String.valueOf(politicalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
        } else {
            return null;
        }
    }
}
