package com.spring.Final.modules.job_proposal.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ExistingProposalException extends BaseException {
    public ExistingProposalException() {
        super("PROPOSAL_EXISTS", "This proposal already exists", HttpStatus.BAD_REQUEST);
    }
}