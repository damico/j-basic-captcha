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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.damico.javaotp.oath.totp.TotpImpl;

public class Generator {

	private int size = 6;
	private int x	= 30;
	private int t0		 = 0;
	private String steps = "0";
	private String algo = "HmacSHA1";

	private byte[] seed = null;
	private byte[] byteArray = null;
	private String otp = null;
	private String otpL = null;
	private String[] lettersA = {"a", "B", "c", "D", "e", "F", "g", "I", "j", "K"};
	private String[] lettersB = {"L", "m", "N", "o", "P", "q", "R", "s", "T", "u"};
	private String[] lettersC = {"v", "W", "x", "Y", "z", "A", "C", "E", "G", "J"};

	public Generator(byte[] seed) throws InvalidKeyException, UndeclaredThrowableException, NoSuchAlgorithmException, IOException {
		this.seed = seed;
		BufferedImage img = genOtpImage(seed);
		setByteArray(img);
	}

	public Generator(byte[] seed, File file) throws InvalidKeyException, UndeclaredThrowableException, NoSuchAlgorithmException, IOException {
		this.seed = seed;
		BufferedImage img = genOtpImage(seed);
		setByteArray(img);
		savePNG( img, file);
	}

	public Generator(byte[] seed, String typedOtp) throws Exception  {

		this.seed = seed;
		try {
			String partA = typedOtp.substring(0, 2);
			String partB = typedOtp.substring(2, 4);
			String partC = typedOtp.substring(4, 6);

			String aL = getString(partA);
			String replacementA = getNumberFromLetter(aL, lettersA);
			partA = partA.replaceAll(aL, replacementA);

			String bL = getString(partB);
			String replacementB = getNumberFromLetter(bL, lettersB);
			partB = partB.replaceAll(bL, replacementB);

			String cL = getString(partC);
			String replacementC = getNumberFromLetter(cL, lettersC);
			partC = partC.replaceAll(cL, replacementC);

			this.otpL = typedOtp;
			this.otp = partA+partB+partC;
		}catch (Exception e) {
			throw new Exception("invalid_otp");
		}
		//String otp = genOtp(seed);

	}

	private String getNumberFromLetter(String aL, String[] letters) {
		String strNumber = null;
		for (int i = 0; i < letters.length; i++) {
			if(aL.equals(letters[i])) {
				strNumber = String.valueOf(i);
				break;
			}

		}
		return strNumber;
	}


	private String getString(String source) {
		String letter = null;
		for (int i = 0; i < source.length(); i++) {
			try {
				int n = Integer.parseInt(String.valueOf(source.charAt(i)));
			} catch (NumberFormatException e) {
				letter = String.valueOf(source.charAt(i));
			}
		}
		return letter;
	}




	private BufferedImage genOtpImage(byte[] seed) throws InvalidKeyException, NoSuchAlgorithmException {

		String otp = genOtp(seed);
		BufferedImage img = genBufferedImg(otp);
		return img;
	}


	private String genOtp(byte[] seed) throws InvalidKeyException, NoSuchAlgorithmException {

		long unixTime = System.currentTimeMillis() / 1000L;

		String otp = genOtp(unixTime, size, seed);
		this.otp = otp;
		return otp;
	}

	public String getOtp() {
		return otp;
	}

	private BufferedImage genBufferedImg(String otp) {



		Random r = new Random();
		int a = r.nextInt(2);
		int b = r.nextInt(2);
		int c = r.nextInt(2);


		String partA = otp.substring(0, 2);
		String partB = otp.substring(2, 4);
		String partC = otp.substring(4, 6);

		String stringOfCharA = String.valueOf(partA.charAt(a));
		String stringOfCharB = String.valueOf(partB.charAt(b));
		String stringOfCharC = String.valueOf(partC.charAt(c));

		String lA = lettersA[Integer.parseInt(stringOfCharA)];
		String lB = lettersB[Integer.parseInt(stringOfCharB)];
		String lC = lettersC[Integer.parseInt(stringOfCharC)];

		partA = partA.replaceAll(stringOfCharA, lA);
		partB = partB.replaceAll(stringOfCharB, lB);
		partC = partC.replaceAll(stringOfCharC, lC);

		this.otpL = partA+partB+partC;



		BufferedImage img = map( 130, 40 );
		Graphics2D g2d = img.createGraphics();
		Font f = new Font(getRandomLogicalFont(), getRandomFontStyle(), getRandomFromMinMax(24, 28));
		g2d.setFont(f);
		g2d.setColor(getRandomFontColor());
		g2d.drawString(partA, 5, getRandomFromMinMax(20,40));
		f = new Font(getRandomLogicalFont(), getRandomFontStyle(), getRandomFromMinMax(24, 28));
		g2d.setFont(f);
		g2d.setColor(getRandomFontColor());
		g2d.drawString(partB, 45, getRandomFromMinMax(20,40));
		f = new Font(getRandomLogicalFont(), getRandomFontStyle(), getRandomFromMinMax(24, 28));
		g2d.setFont(f);
		g2d.setColor(getRandomFontColor());
		g2d.drawString(partC, 85, getRandomFromMinMax(20,40));
		return img;

	}



	public Color getRandomFontColor() {
		Random r = new Random();

		Color[] fontColors = {Color.BLACK, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.RED, Color.GREEN};
		int sorted = r.nextInt(fontColors.length);
		return fontColors[sorted];
	}

	public Color getRandomBgColor() {
		Random r = new Random();

		Color[] fontColors = {Color.GRAY, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE, Color.MAGENTA, Color.CYAN};
		int sorted = r.nextInt(fontColors.length);
		return fontColors[sorted];
	}


	private int getRandomFromMinMax(int min, int max) {
		Random r = new Random();
		int result = r.nextInt(max - min + 1) + min;
		return result;
	}

	private int getRandomFontStyle() {
		Random r = new Random();
		int sorted = r.nextInt(2);
		int[] fontStyles = {Font.BOLD, Font.ITALIC, Font.PLAIN};
		return fontStyles[sorted];
	}

	private String getRandomLogicalFont() {


		String[] fonts = {"Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif"};		

		Random r = new Random();
		int sorted = r.nextInt(fonts.length);

		return fonts[sorted];

	}

	private BufferedImage map( int sizeX, int sizeY ){
		final BufferedImage res = new BufferedImage( sizeX, sizeY, BufferedImage.TYPE_INT_RGB );
		for (int x = 0; x < sizeX; x++){
			for (int y = 0; y < sizeY; y++){
				if(x % 2 == 0) res.setRGB(x, y, getRandomBgColor().getRGB() );
				if(y % 2 == 0) res.setRGB(x, y, getRandomBgColor().getRGB() );
			}
		}
		return res;
	}

	private void savePNG( final BufferedImage bi, File file ){
		try {



			RenderedImage rendImage = bi;
			ImageIO.write(rendImage, "PNG", file);
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}


	private void setByteArray(final BufferedImage bi) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", outputStream);
		this.byteArray = outputStream.toByteArray();

	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public String getOtpL() {
		return otpL;
	}


	public boolean validateByWindow(int windowMinutes) throws InvalidKeyException, UndeclaredThrowableException, NoSuchAlgorithmException {
		boolean isValidOtp = false;
		long unixTime = System.currentTimeMillis() / 1000L;
		Map<String,Long> otpWindowMap = new HashMap<String, Long>();
		long backwardWindow = unixTime - (x*windowMinutes);
		long forwardWindow =  unixTime + (x*windowMinutes);
		otpWindowMap.put(genOtp(unixTime, size, seed),unixTime);
		for (long i = backwardWindow; i <= forwardWindow; i++) otpWindowMap.put(genOtp(i, size, seed),i);

		Iterator<String> iter = otpWindowMap.keySet().iterator();
		while(iter.hasNext()){
			String otp = iter.next();
			if(otp.equals(getOtp())) {
				isValidOtp = true;
				break;
			}
		}
		return isValidOtp;
	}


	private String genOtp(long baseTime, int size, byte[] seed) throws InvalidKeyException, UndeclaredThrowableException, NoSuchAlgorithmException {
		String otpGen = null;
		long T = (baseTime - t0)/x;
		steps = Long.toHexString(T).toUpperCase();
		while(steps.length() < 16) steps = "0" + steps;

		otpGen = TotpImpl.getInstance().generateTOTP(seed, steps, size, algo);
		


		return otpGen;
	}

}
