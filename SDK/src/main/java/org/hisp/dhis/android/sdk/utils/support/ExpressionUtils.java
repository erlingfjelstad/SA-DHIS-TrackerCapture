/*
 *  Copyright (c) 2015, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.utils.support;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;
import org.hisp.dhis.android.sdk.utils.support.math.ExpressionFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Lars Helge Overland
 */
public class ExpressionUtils
{
    private static final JexlEngine JEXL = new JexlEngine();

    private static final Pattern NUMERIC_PATTERN = Pattern.compile( "^(-?0|-?[1-9]\\d*)(\\.\\d+)?(E(-)?\\d+)?$" );
    
    static 
    {
        Map<String, Object> functions = new HashMap<>();
        functions.put( ExpressionFunctions.NAMESPACE, ExpressionFunctions.class );
        
        JEXL.setFunctions( functions );
        JEXL.setCache( 512 );
        JEXL.setSilent( false );
    }
    
    /**
     * Evaluates the given expression. The given variables will be substituted 
     * in the expression.
     * 
     * @param expression the expression.
     * @param vars the variables, can be null.
     * @return the result of the evaluation.
     */
    public static Object evaluate( String expression, Map<String, Object> vars )
    {
        Expression exp = JEXL.createExpression( expression );
        
        JexlContext context = vars != null ? new MapContext( vars ) : new MapContext();
                
        return exp.evaluate( context );
    }

    /**
     * Evaluates the given expression. The given variables will be substituted 
     * in the expression. Converts the result of the evaluation to a Double.
     * Throws an IllegalStateException if the result could not be converted to
     * a Double
     * 
     * @param expression the expression.
     * @param vars the variables, can be null.
     * @return the result of the evaluation.
     */
    public static Double evaluateToDouble( String expression, Map<String, Object> vars )
    {
        Object result = evaluate( expression, vars );
        
        if ( result == null || !isNumeric( String.valueOf( result ) ) )
        {
            throw new IllegalStateException( "Result must be not null and numeric: " + result );
        }
        
        return Double.valueOf( String.valueOf( result ) );
    }

    /**
     * Evaluates the given expression to true or false. The given variables will 
     * be substituted in the expression.
     * 
     * @param expression the expression.
     * @param vars the variables, can be null.
     * @return true or false.
     */
    public static boolean isTrue( String expression, Map<String, Object> vars )
    {
        Object result = evaluate( expression, vars );
        
        return ( result != null && result instanceof Boolean ) ? (Boolean) result : false;
    }
    
    /**
     * Indicates whether the given expression is valid and evaluates to true or
     * false.
     * 
     * @param expression the expression.
     * @param vars the variables, can be null.
     * @return true or false.
     */
    public static boolean isBoolean( String expression, Map<String, Object> vars )
    {
        try
        {
            Object result = evaluate( expression, vars );
            
            return ( result instanceof Boolean );
        }
        catch ( JexlException ex )
        {
            return false;
        }
    }
    
    /**
     * Indicates whether the given value is numeric.
     * 
     * @param value the value.
     * @return true or false.
     */
    public static boolean isNumeric( String value )
    {
        return NUMERIC_PATTERN.matcher( value ).matches();
    }
}
