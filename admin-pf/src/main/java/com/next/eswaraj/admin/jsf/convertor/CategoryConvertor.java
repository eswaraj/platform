package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Category;

@Component("jsfCategoryConvertor")
public class CategoryConvertor implements Converter {

    private List<Category> categories;

    public CategoryConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                for (Category oneSystemCategory : categories) {
                    if (oneSystemCategory.getId().equals(id)) {
                        return oneSystemCategory;
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            if (object instanceof String) {
                return (String) object;
            }
            return String.valueOf(((Category) object).getId());
        } else {
            return null;
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
