package com.spring.Final.modules.shared.enums.job_proposal_type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobProposalTypeAttributeConverter implements AttributeConverter<JobProposalType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(JobProposalType jobProposalType) {

        if (jobProposalType == null) {
            return null;
        }

        switch (jobProposalType) {
            case PROPOSAL:
                return 1;
            case OFFER:
                return 2;
            default:
                throw new IllegalArgumentException(jobProposalType + " not supported!");
        }

    }

    @Override
    public JobProposalType convertToEntityAttribute(Integer dbData) {

        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case 1:
                return JobProposalType.PROPOSAL;
            case 2:
                return JobProposalType.OFFER;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }

    }

}
