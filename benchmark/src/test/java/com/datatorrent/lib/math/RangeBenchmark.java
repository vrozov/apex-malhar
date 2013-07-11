/*
 * Copyright (c) 2013 Malhar Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.lib.math;

import com.datatorrent.api.Sink;
import com.datatorrent.lib.math.Range;
import com.datatorrent.lib.util.HighLow;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.datatorrent.lib.math.Range}<p>
 *
 */
public class RangeBenchmark
{
  private static Logger log = LoggerFactory.getLogger(RangeBenchmark.class);

  class TestSink implements Sink
  {
    Object tuple;

    @Override
    public void put(Object payload)
    {
      tuple = payload;
    }

    @Override
    public int getCount(boolean reset)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   * Test oper logic emits correct results
   */
  @Test
  @Category(com.datatorrent.lib.annotation.PerformanceTestCategory.class)
  public void testNodeSchemaProcessing()
  {
    Range<Double> oper = new Range<Double>();
    TestSink rangeSink = new TestSink();
    oper.range.setSink(rangeSink);

    oper.beginWindow(0); //

    int numTuples = 100000000;
    for (int i = 0; i < numTuples; i++) {
      Double a = new Double(2.0);
      Double b = new Double(20.0);
      Double c = new Double(1000.0);

      oper.data.process(a);
      oper.data.process(b);
      oper.data.process(c);

      a = 1.0;
      oper.data.process(a);
      a = 10.0;
      oper.data.process(a);
      b = 5.0;
      oper.data.process(b);

      b = 12.0;
      oper.data.process(b);
      c = 22.0;
      oper.data.process(c);
      c = 14.0;
      oper.data.process(c);

      a = 46.0;
      oper.data.process(a);
      b = 2.0;
      oper.data.process(b);
      a = 23.0;
      oper.data.process(a);
    }

    oper.endWindow(); //

    HighLow hl = (HighLow)rangeSink.tuple;
    log.debug(String.format("\nBenchmark total %d tuples was expected (1000,1) got (%f,%f)", numTuples * 12,
                            hl.getHigh().doubleValue(),
                            hl.getLow().doubleValue()));
  }
}