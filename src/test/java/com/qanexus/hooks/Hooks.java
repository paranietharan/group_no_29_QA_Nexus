package com.qanexus.hooks;

import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qanexus.support.TestContext;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        logger.info("Starting new scenario with shared TestContext");
    }
}