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
package com.datatorrent.lib.algo;

import com.datatorrent.lib.algo.FirstTillMatchString;
import com.datatorrent.lib.testbench.CollectorTestSink;
import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.datatorrent.lib.algo.FirstTillMatchString}<p>
 *
 */
public class FirstTillMatchStringBenchmark
{
  private static Logger log = LoggerFactory.getLogger(FirstTillMatchStringBenchmark.class);

  /**
   * Test node logic emits correct results
   */
  @Test
  @SuppressWarnings( {"SleepWhileInLoop", "unchecked"})
  @Category(com.datatorrent.lib.annotation.PerformanceTestCategory.class)
  public void testNodeProcessing() throws Exception
  {
    FirstTillMatchString<String> oper = new FirstTillMatchString<String>();
    CollectorTestSink matchSink = new CollectorTestSink();
    oper.first.setSink(matchSink);
    oper.setKey("a");
    oper.setValue(3);
    oper.setTypeEQ();

    int numTuples = 10000000;
    for (int i = 0; i < numTuples; i++) {
      matchSink.clear();
      oper.beginWindow(0);
      HashMap<String, String> input = new HashMap<String, String>();
      input.put("a", "4");
      input.put("b", "20");
      input.put("c", "1000");
      oper.data.process(input);
      input.clear();
      input.put("a", "2");
      oper.data.process(input);
      input.put("a", "3");
      input.put("b", "20");
      input.put("c", "1000");
      oper.data.process(input);
      input.clear();
      input.put("a", "4");
      input.put("b", "21");
      input.put("c", "1000");
      oper.data.process(input);
      input.clear();
      input.put("a", "6");
      input.put("b", "20");
      input.put("c", "5");
      oper.data.process(input);
      oper.endWindow();
    }

    Assert.assertEquals("number emitted tuples", 2, matchSink.collectedTuples.size());
    int atotal = 0;
    Integer aval;
    for (Object o: matchSink.collectedTuples) {
      aval = Integer.parseInt(((HashMap<String, String>)o).get("a"));
      atotal += aval.intValue();
    }
    Assert.assertEquals("Value of a was ", 6, atotal);
    log.debug(String.format("\nBenchmarked %d tuples", numTuples * 17));
  }
}