package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.next.eswaraj.admin.jsf.bean.TimelineItemBean;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PoliticalBodyAdminSearchResultConvertor implements Converter {

    @Autowired
    private TimelineItemBean timelineItemBean;

    public PoliticalBodyAdminSearchResultConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("timelineItemBean : " + timelineItemBean);
                List<PoliticalBodyAdminSearchResult> elections = timelineItemBean.getAllAdmins();
                long id = Long.parseLong(value);
                for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : elections) {
                    if (onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId().equals(id)) {
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
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            if (object instanceof String) {
                return "";
            }
            return String.valueOf(((PoliticalBodyAdminSearchResult) object).getPoliticalBodyAdmin().getId());
        } else {
            return null;
        }
    }
}
