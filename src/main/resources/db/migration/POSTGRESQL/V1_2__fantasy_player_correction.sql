ALTER TABLE PLYR_MISC_INFO RENAME COLUMN CREATIVITYRANK TO CREATIVITY_RANK;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN CREATIVITYRANKTYPE TO CREATIVITY_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN FORMRANK TO FORM_RANK;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN FORMRANKTYPE TO FORM_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN ICTINDEXRANK TO ICT_INDEX_RANK;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN ICTINDEXRANKTYPE TO ICT_INDEX_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN INFLUENCERANK TO INFLUENCE_RANK;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN INFLUENCERANKTYPE TO INFLUENCE_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN THREATRANK TO THREAT_RANK;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN THREATRANKTYPE TO THREAT_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO RENAME COLUMN NEWSADDED TO NEWS_ADDED;

ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN CREATIVITYRANK TO CREATIVITY_RANK;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN CREATIVITYRANKTYPE TO CREATIVITY_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN FORMRANK TO FORM_RANK;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN FORMRANKTYPE TO FORM_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN ICTINDEXRANK TO ICT_INDEX_RANK;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN ICTINDEXRANKTYPE TO ICT_INDEX_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN INFLUENCERANK TO INFLUENCE_RANK;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN INFLUENCERANKTYPE TO INFLUENCE_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN THREATRANK TO THREAT_RANK;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN THREATRANKTYPE TO THREAT_RANK_TYPE;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN NEWSADDED TO NEWS_ADDED;
ALTER TABLE PLYR_MISC_INFO_AUD RENAME COLUMN EN_ORDR TO PEN_ORDR;

ALTER TABLE PLYR_BSC_INFO ALTER WEB_NAME TYPE VARCHAR(50);
ALTER TABLE PLYR_BSC_INFO_AUD ALTER WEB_NAME TYPE VARCHAR(50);