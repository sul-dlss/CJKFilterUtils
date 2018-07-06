/** @note Copyright (c) 2013 by The Board of Trustees of the Leland Stanford Junior University.
 * All rights reserved.  See {file:LICENSE} for details. **/
package edu.stanford.lucene.analysis;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Maps equivalent CJK Unicode characters to a single Unicode character.
 * Meant to supplement ICUNormalization and ICU translation of Han traditional
 * <-> simplified, as there are variants of specific characters that are not
 * mapped by either ICU filter.
 * @author Naomi Dushay
 */
public class CJKFoldingFilter extends TokenFilter
{
	 private final CharTermAttribute charTermAttr = addAttribute(CharTermAttribute.class);
	 private char[] output = new char[512];
	 private int outputPos;

	protected CJKFoldingFilter(TokenStream input)
	{
		super(input);
	}
	

	@Override
	public final boolean incrementToken() throws IOException
	{
		if (input.incrementToken())
		{
			final char[] buffer = charTermAttr.buffer();
			final int bufferLen = charTermAttr.length();
			for (int i = 0 ; i < bufferLen ; ++i)
			{
				final char c = buffer[i];
				// If no characters actually require rewriting then we
				// just return token as-is
				if (c >= '\u3000')
				{
					mapUnicode(buffer, bufferLen);
					charTermAttr.copyBuffer(output, 0, outputPos);
					break;
				}
			}
			return true;
		}

		return false;
	}

	/**
	* Maps Unicode characters per variant2Trad map below.
	* @param input The string to fold
	* @param length The number of characters in the input string
	*/
	public void mapUnicode(char[] input, int length)
	{
		// Worst-case length required:
		final int maxSizeNeeded = 4 * length;
		if (output.length < maxSizeNeeded)
			output = new char[ArrayUtil.oversize(maxSizeNeeded, RamUsageEstimator.NUM_BYTES_CHAR)];

	    outputPos = mapUnicode(input, 0, output, 0, length);
	}


	/**
	 * Maps Unicode characters per variant2Trad map below.
	 * @param input     The characters to map
	 * @param inputPos  Index of the first character to map
	 * @param output    The result of the mapping. Should be of size >= {@code length * 4}.
	 * @param outputPos Index of output where to put the result of the mapping
	 * @param length    The number of characters to map
	 * @return length of output
	 */
	public static final int mapUnicode(char input[], int inputPos, char output[], int outputPos, int length)
	{
		final int end = inputPos + length;
		for (int pos = inputPos; pos < end ; ++pos)
		{
			final char c = input[pos];

			// Quick test: if it's not in range then just keep current character
			if (c < '\u3000')
				output[outputPos++] = c;
			else
			{
				// check for Japanese Modern chars that aren't the same as Han Simplified
				if (variant2Trad.containsKey(String.valueOf(c)))
				{
					String mapped = variant2Trad.get(String.valueOf(c));
					int mappedLen = mapped.length();
					for (int i = 0; i < mappedLen; i++)
					{
						output[outputPos++] = mapped.charAt(i);
					}
				}
				else
					output[outputPos++] = c;
			}
		}
		return outputPos;
	}

	private static AbstractMap<String, String> variant2Trad = new HashMap<String, String>();	
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	 		org.w3c.dom.Document doc = dBuilder.parse(getClass().getResourceAsStream("variantmap.xml"));
			//doc.getDocumentElement().normalize();				
			NodeList nList = doc.getElementsByTagName("map");
			for ( int i=0; i<nList.getLength(); i++ ) {
				Element el = (Element)nList.item(i);
				variant2Trad.put(el.getAttribute("variant"),el.getAttribute("traditional"));
			}
		} catch (Exception e ) {
			System.err.println("Unable to initialize CJKFoldingFilter: " + e.getMessage());
		}
	}
	
	
	
	
	
 }
