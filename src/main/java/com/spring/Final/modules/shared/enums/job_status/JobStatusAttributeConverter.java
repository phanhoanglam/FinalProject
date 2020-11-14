package com.spring.Final.modules.shared.enums.job_status;

import javax.persistence.AttributeConverter;

public class JobStatusAttributeConverter implements AttributeConverter<JobStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(JobStatus jobStatus) {
        if (jobStatus == null) {
            return null;
        }
        switch (jobStatus) {
            case OPEN:
                return 1;
            case PROGRESSING:
                return 2;
            case CLOSED:
                return 3;
            default:
                throw new IllegalArgumentException(jobStatus + " not supported!");
        }
    }

    @Override
    public JobStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return JobStatus.OPEN;
            case 2:
                return JobStatus.PROGRESSING;
            case 3:
                return JobStatus.CLOSED;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }
}
