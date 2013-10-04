package edu.stanford.lucene.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

/**
 * Factory for CJKAddlCharsFoldingFilter
 * @author Naomi Dushay
 *
 */
public class CJKAddlCharsFoldingFilterFactory extends TokenFilterFactory
{

	/**
	 * @param map
	 */
	protected CJKAddlCharsFoldingFilterFactory(Map<String, String> map)
	{
		super(map);
	}

	/**
	 * @see org.apache.lucene.analysis.util.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	@Override
	public TokenStream create(TokenStream input)
	{
		return new CJKAddlCharsFoldingFilter(input);
	}

}
