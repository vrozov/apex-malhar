/**
 * Copyright (c) 2012-2012 Malhar, Inc. All rights reserved.
 */
package com.datatorrent.lib.algo;

import com.datatorrent.lib.algo.Sampler;
import com.datatorrent.lib.testbench.CountTestSink;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.datatorrent.lib.algo.Sampler}<p>
 *
 */
public class SamplerBenchmark
{
  private static Logger log = LoggerFactory.getLogger(SamplerBenchmark.class);

  /**
   * Test node logic emits correct results
   */
  @Test
  @SuppressWarnings("SleepWhileInLoop")
  @Category(com.datatorrent.annotation.PerformanceTestCategory.class)
  public void testNodeProcessing() throws Exception
  {
    Sampler<String> oper = new Sampler<String>();
    CountTestSink sink = new CountTestSink<String>();
    oper.sample.setSink(sink);
    oper.setPassrate(10);
    oper.setTotalrate(100);

    String tuple = "a";
    int numTuples = 500000000;
    oper.beginWindow(0);
    for (int i = 0; i < numTuples; i++) {
      oper.data.process(tuple);
    }

    oper.endWindow();
    log.debug(String.format("\nBenchmarked %d tuples", numTuples));
  }
}