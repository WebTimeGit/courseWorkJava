package com.parking.management.services;

import com.parking.management.entities.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorService.class);

    private final MongoOperations mongoOperations;

    @Autowired
    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long generateSequence(String seqName) {
        try {
            DatabaseSequence counter = mongoOperations.findAndModify(
                    Query.query(where("_id").is(seqName)),
                    new Update().inc("seq", 1),
                    options().returnNew(true).upsert(true),
                    DatabaseSequence.class);

            if (counter != null) {
                logger.info("Generated sequence for {}: {}", seqName, counter.getSeq());
                return counter.getSeq();
            } else {
                logger.warn("Failed to generate sequence for {}: returning default value 1", seqName);
                return 1;
            }
        } catch (Exception e) {
            logger.error("Error generating sequence for {}", seqName, e);
            throw new RuntimeException("Error generating sequence", e);
        }
    }
}
