package com.eswaraj.tasks.spout.mesage;

import java.io.Serializable;

public class RefreshCommentMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long commentId;
    private Long complaintId;

    public RefreshCommentMessage(Long commentId, Long complaintId) {
        super();
        this.commentId = commentId;
        this.complaintId = complaintId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }
}
