alter table SAM_ASSESSFEEDBACK_T
add column  (FEEDBACKAUTHORING integer);
alter table SAM_PUBLISHEDFEEDBACK_T
add column  (FEEDBACKAUTHORING integer);

INSERT INTO SAM_ASSESSMETADATA_T ("ASSESSMENTMETADATAID", "ASSESSMENTID","LABEL",
    "ENTRY")
    VALUES(NULL, 1, 'feedbackAuthoring_isInstructorEditable', 'true')
;
update SAM_ASSESSFEEDBACK_T set FEEDBACKAUTHORING=1;
