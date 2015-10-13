package com.jj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 10/13/15.
 */
@Component
public class IdGenerator {
    private Logger logger = LoggerFactory.getLogger(IdGenerator.class);
    private long currentId = 1L;

    public IdGenerator() {
        logger.info(">> IdGenerator created.");
    }

    public synchronized Long getNextId() {
        return currentId++;
    }
}
