package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class CommentValidator extends BaseValidator<Comment> {

	@Autowired
	public CommentValidator(ValidationManager validationManager) {
        super(Comment.class, validationManager);
	}

    @Override
    public void validateBeforeSave(Comment comment) throws ValidationException {
        checkIfEmpty("Comment Text", comment.getText());
        checkIfNull("Created By", comment.getCreatedBy());
	}

    @Override
    public void validateBeforeDelete(Comment comment) throws ValidationException {
	}
}
