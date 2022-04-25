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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest{ 

	private byte[] seed = new byte[]{10,20,30,40,60,80,90,00,10,20,30,40,60,80,90,00};
	private Generator generatorA = null;
	private Generator generatorB = null;
	
	@Before
	public void setUp() {
		
		try {
			generatorA = new Generator(seed, new File("/tmp/captcha.png"));
			generatorB = new Generator(seed, generatorA.getOtpL());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    @Test
    public void testGenerator(){
			String otpPre = generatorA.getOtp();
			System.out.println(generatorA.getOtpL());
			String otpPos = generatorB.getOtp();
			System.out.println(otpPos);
			assertEquals(otpPre, otpPos);
			
    }
    
    @Test
    public void testValidator() {
    	try {
			boolean isValid = generatorB.validateByWindow(1);
			assertEquals(true, isValid);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UndeclaredThrowableException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }
}
