package com.jlu;

import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TrackLogTest extends SpringBaseTest {
    Logger logger = LoggerFactory.getLogger(TrackLogTest.class);

    @Test
    public void testTrack() throws Exception {
        logger.info("hello,track");
    }
}
