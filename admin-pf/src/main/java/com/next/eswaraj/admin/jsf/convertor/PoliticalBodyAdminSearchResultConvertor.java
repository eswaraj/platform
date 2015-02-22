package com.next.eswaraj.admin.jsf.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;

@Component
public class PoliticalBodyAdminSearchResultConvertor extends BaseConvertor<PoliticalBodyAdminSearchResult> {

    public PoliticalBodyAdminSearchResultConvertor() {
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("Checking : " + value);
                long id = Long.parseLong(value);
                for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : list) {
                    System.out.println("onePoliticalBodyAdminSearchResult : " + onePoliticalBodyAdminSearchResult.getPerson().getName() + ","
                            + onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
                    if (onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId().equals(id)) {
                        System.out.println("Found in All Admins");
                        return onePoliticalBodyAdminSearchResult;
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
    public String getAsString(Object object) {
        if (object != null) {
            if (object instanceof String) {
                return "";
            }
            PoliticalBodyAdminSearchResult politicalBodyAdminSearchResult = ((PoliticalBodyAdminSearchResult) object);
            return String.valueOf(politicalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
        } else {
            return null;
        }
    }
}
