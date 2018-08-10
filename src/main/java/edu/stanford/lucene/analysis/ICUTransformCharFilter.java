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

import com.ibm.icu.text.ReplaceableString;
import com.ibm.icu.text.Transliterator;
import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.charfilter.BaseCharFilter;

public final class ICUTransformCharFilter extends BaseCharFilter {
  // Transliterator to transform the text
  private final Transliterator transform;
  // Reusable position object
  private final Transliterator.Position position = new Transliterator.Position();

  private final StringBuilder inputBuffer = new StringBuilder();
  private final StringBuilder resultBuffer = new StringBuilder();
  private final CharacterUtils.CharacterBuffer tmpBuffer;

  private boolean inputFinished;
  private int charCount;

  public ICUTransformCharFilter(Reader in, Transliterator transform) {
    super(in);
    this.transform = transform;
    this.tmpBuffer = CharacterUtils.newCharacterBuffer(128);
  }

  /**
   * Reads characters into a portion of an array.  This method will block
   * until some input is available, an I/O error occurs, or the end of the
   * stream is reached.
   *
   * @param cbuf Destination buffer
   * @param off  Offset at which to start storing characters
   * @param len  Maximum number of characters to read
   * @return The number of characters read, or -1 if the end of the
   * stream has been reached
   * @throws IOException If an I/O error occurs
   */
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    if (off < 0) throw new IllegalArgumentException("off < 0");
    if (off >= cbuf.length) throw new IllegalArgumentException("off >= cbuf.length");
    if (len <= 0) throw new IllegalArgumentException("len <= 0");

    while (!inputFinished || inputBuffer.length() > 0 || resultBuffer.length() > 0) {
      int retLen;

      if (resultBuffer.length() > 0) {
        retLen = outputFromResultBuffer(cbuf, off, len);
        if (retLen > 0) {
          return retLen;
        }
      }

      int resLen = readFromIoNormalizeUptoBoundary();
      if (resLen > 0) {
        retLen = outputFromResultBuffer(cbuf, off, len);
        if (retLen > 0) {
          return retLen;
        }
      }

      readInputToBuffer();
    }

    return -1;
  }

  private int readFromIoNormalizeUptoBoundary() {
    // if there's no buffer to normalize, return 0
    if (inputBuffer.length() <= 0) {
      return 0;
    }

    final int length = inputBuffer.length();
    final int destOrigLen = resultBuffer.length();
    CharSequence s = inputBuffer.subSequence(0, length);
    StringBuffer sb = new StringBuffer(s);
    ReplaceableString replaceableString = new ReplaceableString(sb);

    position.start = 0;
    position.limit = length;
    position.contextStart = 0;
    position.contextLimit = length;

    transform.filteredTransliterate(replaceableString, position, false);

//    transform.transliterate(replaceableString);

    resultBuffer.append(sb);
    inputBuffer.delete(0, length);
    final int resultLength = resultBuffer.length() - destOrigLen;
    recordOffsetDiff(length, resultLength);
    return resultLength;
  }

  private void recordOffsetDiff(int inputLength, int outputLength) {
    if (inputLength == outputLength) {
      charCount += outputLength;
      return;
    }
    final int diff = inputLength - outputLength;
    final int cumuDiff = getLastCumulativeDiff();
    if (diff < 0) {
      for (int i = 1;  i <= -diff; ++i) {
        addOffCorrectMap(charCount + i, cumuDiff - i);
      }
    } else {
      addOffCorrectMap(charCount + outputLength, cumuDiff + diff);
    }
    charCount += outputLength;
  }

  private void readInputToBuffer() throws IOException {
    while (true) {
      // CharacterUtils.fill is supplementary char aware
      final boolean hasRemainingChars = CharacterUtils.fill(tmpBuffer, input);

      assert tmpBuffer.getOffset() == 0;
      inputBuffer.append(tmpBuffer.getBuffer(), 0, tmpBuffer.getLength());

      if (hasRemainingChars == false) {
        inputFinished = true;
        break;
      }
    }
  }

  private int outputFromResultBuffer(char[] cbuf, int begin, int len) {
    len = Math.min(resultBuffer.length(), len);
    resultBuffer.getChars(0, len, cbuf, begin);
    if (len > 0) {
      resultBuffer.delete(0, len);
    }
    return len;
  }
}
