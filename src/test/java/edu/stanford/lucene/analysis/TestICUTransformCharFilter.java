/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.stanford.lucene.analysis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.Transliterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.tests.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.tests.analysis.MockTokenizer;

public class TestICUTransformCharFilter extends BaseTokenStreamTestCase {

  public void testBasicFunctionality() throws Exception {
    checkToken(Transliterator.getInstance("Traditional-Simplified"),
            "簡化字", "简化字");
    checkToken(Transliterator.getInstance("Katakana-Hiragana"),
            "ヒラガナ", "ひらがな");
    checkToken(Transliterator.getInstance("Fullwidth-Halfwidth"),
            "アルアノリウ", "ｱﾙｱﾉﾘｳ");
    checkToken(Transliterator.getInstance("Any-Latin"),
            "Αλφαβητικός Κατάλογος", "Alphabētikós Katálogos");
    checkToken(Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove"),
            "Alphabētikós Katálogos", "Alphabetikos Katalogos");
    checkToken(Transliterator.getInstance("Han-Latin"),
            "中国", "zhōng guó");
  }

  private void checkToken(Transliterator transform, String input, String expectedOutput) throws IOException {

    CharFilter reader = new ICUTransformCharFilter(new StringReader(input), transform);
    char[] tempBuff = new char[10];
    StringBuilder output = new StringBuilder();
    while (true) {
      int length = reader.read(tempBuff);
      if (length == -1) {
        break;
      }
      output.append(tempBuff, 0, length);
    }

    assertEquals(expectedOutput, output.toString());
  }

}
