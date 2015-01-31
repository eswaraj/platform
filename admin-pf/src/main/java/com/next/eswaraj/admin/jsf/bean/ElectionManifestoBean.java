package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Document;
import com.eswaraj.domain.nodes.Document.DocumentType;
import com.eswaraj.domain.nodes.ElectionManifesto;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class ElectionManifestoBean extends BaseBean {

    private String showPage;
    private List<ElectionManifesto> electionManifestos;
    private ElectionManifesto selectedElectionManifesto;

    private List<ElectionManifestoPromise> electionManifestoPromises;
    private ElectionManifestoPromise selectedElectionManifestoPromise;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AwsUploadUtil awsUploadUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        try {
            showPage = "ElectionManifestoList";
            electionManifestos = adminService.getElectionManifestos();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createElectionManifesto() {
        selectedElectionManifesto = new ElectionManifesto();
        showPage = "EditElectionManifesto";
    }

    public void createElectionManifestoPromise() {
        selectedElectionManifestoPromise = new ElectionManifestoPromise();
        showPage = "EditElectionManifestoPromise";
    }

    public void cancel() {
        selectedElectionManifesto = new ElectionManifesto();
        showPage = "ElectionManifestoList";
    }

    public void saveElectionManifesto() {
        try {
            selectedElectionManifesto = adminService.saveElectionManifesto(selectedElectionManifesto);
            electionManifestos = adminService.getElectionManifestos();
            showPage = "ElectionManifestoList";
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }

    public void handleDocFileUpload(FileUploadEvent event) {

        String imageType = ".pdf";
        String remoteFileName = selectedElectionManifesto.getElection().getId() + "/" + selectedElectionManifesto.getParty().getId() + "/" + selectedElectionManifesto.getId() + imageType;
        try {
            String httpFilePath = awsUploadUtil.uploadManifestoDocument(remoteFileName, event.getFile().getInputstream(), "pdf");
            // selectedElectionManifesto.setImageUrl(httpFilePath);
            Document document = selectedElectionManifesto.getDocument();
            if (document == null) {
                document = new Document();
            }
            document.setUrl(httpFilePath);
            document.setType(DocumentType.Pdf);
            selectedElectionManifesto.setDocument(document);
            selectedElectionManifesto = adminService.saveElectionManifesto(selectedElectionManifesto);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void showPromiseView(ElectionManifesto selectedElectionManifesto) {
        showPage = "EditElectionManifestoPromise";
        // electionManifestoPromises = adminService
    }
    public ElectionManifesto getSelectedElectionManifesto() {
        return selectedElectionManifesto;
    }

    public void setSelectedElectionManifesto(ElectionManifesto selectedElectionManifesto) {
        this.selectedElectionManifesto = new ElectionManifesto();
        BeanUtils.copyProperties(selectedElectionManifesto, this.selectedElectionManifesto);
        showPage = "EditElectionManifesto";
    }

    public String getShowPage() {
        return showPage;
    }

    public void setShowPage(String showPage) {
        this.showPage = showPage;
    }

    public List<ElectionManifesto> getElectionManifestos() {
        return electionManifestos;
    }

    public void setElectionManifestos(List<ElectionManifesto> electionManifestos) {
        this.electionManifestos = electionManifestos;
    }

    public List<ElectionManifestoPromise> getElectionManifestoPromises() {
        return electionManifestoPromises;
    }

    public void setElectionManifestoPromises(List<ElectionManifestoPromise> electionManifestoPromises) {
        this.electionManifestoPromises = electionManifestoPromises;
    }

    public ElectionManifestoPromise getSelectedElectionManifestoPromise() {
        return selectedElectionManifestoPromise;
    }

    public void setSelectedElectionManifestoPromise(ElectionManifestoPromise selectedElectionManifestoPromise) {
        this.selectedElectionManifestoPromise = selectedElectionManifestoPromise;
    }

}
