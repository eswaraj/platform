package com.next.eswaraj.admin.jsf.bean;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Department;
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

    private List<Department> userDepartments;

    private Department selectedDepartment;

    private UserDto user;

    private String userName;
    private String password;

    @Autowired
    private ApplicationContext applicationCtx;
    

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        refreshLoginRoles();
    }

    public void login() {

    }

    public void refreshLoginRoles() {
        try {
            user = sessionUtil.getLoggedInUserFromSession();
            userDepartments = adminService.getUserDepartments(user.getId());

            if (userDepartments.size() == 1) {
                selectedDepartment = userDepartments.get(0);
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
        request.getSession().removeAttribute("scopedTarget.staffBean");
        request.getSession().removeAttribute("scopedTarget.complaintsBean");
        refreshPage();
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<Department> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(List<Department> userDepartments) {
        this.userDepartments = userDepartments;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
