package com.spring.Final.modules.shared.enums.job_proposal_status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobProposalStatusAttributeConverter implements AttributeConverter<JobProposalStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(JobProposalStatus jobProposalStatus) {

        if (jobProposalStatus == null) {
            return null;
        }

        switch (jobProposalStatus) {
            case PENDING:
                return 1;
            case ACCEPTED:
                return 2;
            case REJECTED:
                return 3;
            case FAILED:
                return 4;
            case SUCCEEDED:
                return 5;
            default:
                throw new IllegalArgumentException(jobProposalStatus + " not supported!");
        }

    }

    @Override
    public JobProposalStatus convertToEntityAttribute(Integer dbData) {

        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case 1:
                return JobProposalStatus.PENDING;
            case 2:
                return JobProposalStatus.ACCEPTED;
            case 3:
                return JobProposalStatus.REJECTED;
            case 4:
                return JobProposalStatus.FAILED;
            case 5:
                return JobProposalStatus.SUCCEEDED;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }

    }

}
