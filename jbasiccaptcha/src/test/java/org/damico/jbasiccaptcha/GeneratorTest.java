/**
 * Copyright [2019] [Jose Ricardo de Oliveira Damico]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 *
 */

package org.damico.jbasiccaptcha;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GeneratorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GeneratorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GeneratorTest.class );
    }

    
    public void testApp()
    {
    	
    	byte[] seed = new byte[]{10,20,30,40,60,80,90,00,10,20,30,40,60,80,90,00};
		try {
			Generator generator = new Generator(seed, new File("/tmp/captcha.png"));
			
			String otpPre = generator.getOtp();
			
			generator = new Generator(seed, generator.getOtpL());
			
			String otpPos = generator.getOtp();
			
			assertEquals(otpPre, otpPos);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UndeclaredThrowableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
