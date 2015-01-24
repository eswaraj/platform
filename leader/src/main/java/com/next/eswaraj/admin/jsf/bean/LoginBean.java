package com.next.eswaraj.admin.jsf.bean;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.web.dto.UserDto;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class LoginBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionUtil sessionUtil;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;

    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;

    private UserDto user;

    @Autowired
    private ApplicationContext applicationCtx;
    

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        try {
            user = sessionUtil.getLoggedInUserFromSession();
            userPoliticalBodyAdmins = adminService.getUserPoliticalBodyAdmins(user.getId());

            if (userPoliticalBodyAdmins.size() == 1) {
                selectedPoliticalBodyAdmin = userPoliticalBodyAdmins.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSelectPoliticalBodyAdmin() {
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> paramNames = request.getSession().getAttributeNames();
        while (paramNames.hasMoreElements()) {
            logger.info("param : " + paramNames.nextElement());
        }
        // Destroy beans first
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationCtx.getAutowireCapableBeanFactory();
        logger.info("Destroying beans  {} , {} , {} ", ((DefaultListableBeanFactory) beanFactory).getBean("staffBean"));

        ((DefaultListableBeanFactory) beanFactory).destroyScopedBean("staffBean");
        ((DefaultListableBeanFactory) beanFactory).destroyScopedBean("personSearchBean");
        ((DefaultListableBeanFactory) beanFactory).destroyScopedBean("complaintsBean");

        logger.info("Beans Destroyed  {} , {} , {} ", ((DefaultListableBeanFactory) beanFactory).getBean("staffBean"));
        refreshPage();
    }

    public List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins() {
        return userPoliticalBodyAdmins;
    }

    public void setUserPoliticalBodyAdmins(List<PoliticalBodyAdmin> userPoliticalBodyAdmins) {
        this.userPoliticalBodyAdmins = userPoliticalBodyAdmins;
    }

    public PoliticalBodyAdmin getSelectedPoliticalBodyAdmin() {
        return selectedPoliticalBodyAdmin;
    }

    public void setSelectedPoliticalBodyAdmin(PoliticalBodyAdmin selectedPoliticalBodyAdmin) {
        this.selectedPoliticalBodyAdmin = selectedPoliticalBodyAdmin;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

}
