package com.kwchina.webmail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Locale;

import javax.mail.internet.MimeUtility;

/**
 * TranscodeUtil.
 * 
 */
public class TranscodeUtil {
	 private static final String CHARSET_UTF8 = "UTF-8";   
	  
	 private static final String CHARSET_ASCII = "US-ASCII";
	 
	 private static final byte ESCAPE_CHAR = '=';  
	 
	 private static final BitSet PRINTABLE_CHARS = new BitSet(256);   
	 
	  private static byte TAB = 9;   
	  
	    private static byte SPACE = 32;   
	 
	  
	    static final int CHUNK_SIZE = 76;   
	

	    /**  
	 
	     * Chunk separator per RFC 2045 section 2.1.  
	 
	     *   
	 
	     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 2.1</a>  
	 
	     */  
	  
	    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();   
	  
	  
	  
	    /**  
	 
	     * The base length.  
	 
	     */  
	  
	    static final int BASELENGTH = 255;   
	  
	  
	  
	    /**  
	 
	     * Lookup length.  
	 
	     */  
	  
	    static final int LOOKUPLENGTH = 64;   
	  
	  
	  
	    /**  
	 
	     * Used to calculate the number of bits in a byte.  
	 
	     */  
	  
	    static final int EIGHTBIT = 8;   
	  
	  
	  
	    /**  
	 
	     * Used when encoding something which has fewer than 24 bits.  
	 
	     */  
	  
	    static final int SIXTEENBIT = 16;   
	  
	  
	  
	    /**  
	 
	     * Used to determine how many bits data contains.  
	 
	     */  
	  
	    static final int TWENTYFOURBITGROUP = 24;   
	  
	  
	  
	    /**  
	 
	     * Used to get the number of Quadruples.  
	 
	     */  
	  
	    static final int FOURBYTE = 4;   
	  
	  
	  
	    /**  
	 
	     * Used to test the sign of a byte.  
	 
	     */  
	  
	    static final int SIGN = -128;   
	  
	  
	  
	    // Create arrays to hold the base64 characters and a   
	  
	    // lookup for base64 chars   
	  
	    private static byte[] base64Alphabet = new byte[BASELENGTH];   
	  
	    private static byte[] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];   
	/**
	 * Why we need org.bulbul.util.TranscodeUtil.transcodeThenEncodeByLocale()?
	 * 
	 * Because we specify client browser's encoding to UTF-8, IE seems to send
	 * all data encoded in UTF-8. That means the byte sequences we received are
	 * all UTF-8 bytes. However, strings read from HTTP is ISO8859_1 encoded,
	 * that's we need to transcode them (usually from ISO8859_1 to UTF-8. Next
	 * we encode those strings using MimeUtility.encodeText() depending on
	 * user's locale. Since MimeUtility.encodeText() is used to convert the
	 * strings into its transmission format, finally we can use the strings in
	 * the outgoing e-mail, then receiver's email agent is responsible for
	 * decoding the strings.
	 * 
	 * As described in JavaMail document, MimeUtility.encodeText() conforms to
	 * RFC2047 and as a result, we'll get strings like "=?Big5?B......".
	 * 
	 * @param sourceString
	 *            String to be encoded
	 * @param sourceStringEncoding
	 *            The encoding to decode `sourceString' string. If
	 *            `sourceStringEncoding' is null, use JVM's default enconding.
	 * @param Locale
	 *            prefered locale
	 * 
	 * @return empty string(prevent NullPointerException) if sourceString is
	 *         null or empty(""); otherwise RFC2047 conformed string, eg,
	 *         "=?Iso8859-1?Q....."
	 */
	public static String transcodeThenEncodeByLocale(String sourceString, String sourceStringEncoding, Locale locale)
			throws java.io.UnsupportedEncodingException {
		String str;

		if ((sourceString == null) || (sourceString.equals("")))
			return "";

		// Transcode to UTF-8
		if ((sourceStringEncoding == null) || (sourceStringEncoding.equals("")))
			str = sourceString;
			//str = new String(sourceString.getBytes(), "iso8859-1");
		else
			str = MimeUtility.encodeText(sourceString,sourceStringEncoding,"B");
			
			//str = new String(sourceString.getBytes(sourceStringEncoding), "iso8859-1");

		// Encode text
		//if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
		//	return MimeUtility.encodeText(str, "Big5", null);
		//} else {
			//return MimeUtility.encodeText(str);
			return str;
		//}
	}
	
	
	//±‡¬ÎQP
	/**  
	 
     * Convert a String to QP code  
 
     *   
 
     * @param qStr  
 
     * @return  
 
     * @throws Exception  
 
     */  
  
    public static String encodeQP(String str) throws Exception {   
  
        int count = 0;   
  
        if (str == null) {   
  
            return null;   
  
        }   
  
        byte[] bytes = str.getBytes(CHARSET_UTF8);   
  
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();   
  
        for (int i = 0; i < bytes.length; i++) {   
  
            int b = bytes[i];   
  
            if (b < 0) {   
  
                b = 256 + b;   
  
            }   
  
            count++;   
  
            if (PRINTABLE_CHARS.get(b)) {   
  
                if (count == 76) {   
  
                    count = 0;   
  
                    count++;   
  
                    buffer.write(ESCAPE_CHAR);   
  
                    buffer.write(CHUNK_SEPARATOR);   
  
                }   
  
                buffer.write(b);   
  
            } else {   
  
                count = encodeQuotedPrintable(b, buffer, count);   
  
            }   
  
        }   
  
  
  
        return new String(buffer.toByteArray(), CHARSET_ASCII);   
  
    }   
  

	
	
	//Ω‚¬ÎQP
	public static String decodeQP(String qpStr) throws Exception {   
		  
        if (qpStr == null) {     
            return null;     
        }   
  
        qpStr = qpStr.replaceAll("=\r\n", "");   
  
        byte[] bytes = qpStr.getBytes(CHARSET_ASCII);     
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();   
  
        for (int i = 0; i < bytes.length; i++) {  
            int b = bytes[i];   
            if (b == ESCAPE_CHAR) {     
                try {  
                    int u = Character.digit((char) bytes[++i], 16); 
                    int l = Character.digit((char) bytes[++i], 16);  
                    if (u == -1 || l == -1) {     
                        throw new Exception("Invalid quoted-printable encoding");
                    }     
                    buffer.write((char) ((u << 4) + l));  
                } catch (ArrayIndexOutOfBoundsException e) {  
                    throw new Exception("Invalid quoted-printable encoding");  
                }     
            } else {
                buffer.write(b); 
            }   
        }  
  
        return new String(buffer.toByteArray(), CHARSET_UTF8);   
  
    }  
	
	//Ω‚¬ÎBase64
	public static String dealSubject(String subject) 
			throws UnsupportedEncodingException,IOException {
		if (subject != null && !subject.equals("")) {
			
			String newSubject = subject;
			
			if (newSubject.indexOf("=?GB2312?B?") > 0) {
				int pos = newSubject.indexOf("=?GB2312?B?");

				int k = 0;
				subject = "";
				String encode = "";

				while (pos >= 0) {
					k += 1;

					int pos2 = newSubject.indexOf("?=");
					if (k > 1 && pos != 0) {
						sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
						byte[] b = decoder.decodeBuffer(encode);
						String aa = (new String(b));
						subject += aa;

						encode = "";
					}

					if (pos2 > 0)
						encode += newSubject.substring(pos + 11, pos2);
					else
						encode += newSubject.substring(pos + 11);

					// System.out.println(aa);
					subject += newSubject.substring(0, pos);

					if (pos2 > 0)
						newSubject = newSubject.substring(pos2 + 2);
					else
						newSubject = "";

					pos = newSubject.indexOf("=?GB2312?B?");
				}

				if (!encode.equals("")) {
					sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
					byte[] b = decoder.decodeBuffer(encode);
					String aa = (new String(b));
					subject += aa;
				}

				if (newSubject != "")
					subject += newSubject;
			} else {
				subject = MimeUtility.decodeText(subject);
			}
		}
		
		return subject;
	}

	
	 /**  
     * ≈–∂œ◊÷∑˚¥Æµƒ±‡¬Î  
     *  
     * @param str  
     * @return  
     */  
    public static String getEncoding(String str) {   
        String encode = "GB2312";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s = encode;   
                return s;   
            }   
        } catch (Exception exception) {   
        }   
        
        encode = "ISO-8859-1";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s1 = encode;   
                return s1;   
            }   
        } catch (Exception exception1) {   
        }
        
        encode = "UTF-8";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s2 = encode;   
                return s2;   
            }   
        } catch (Exception exception2) {   
        }   
        
        encode = "GBK";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s3 = encode;   
                return s3;   
            }   
        } catch (Exception exception3) {   
        }
        
        
        encode = " quoted-printable";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s4 = encode;   
                return s4;   
            }   
        } catch (Exception exception3) {   
        }        
       
        
        return "";   
        
        
    }   
    
    
    /**  
    
     * Encodes byte into its quoted-printable representation.  
 
     *   
 
     * @param b  
 
     * @param buffer  
 
     * @throws IOException  
 
     */  
  
    private static final int encodeQuotedPrintable(int b,   
  
            ByteArrayOutputStream buffer, int count) throws IOException {   
  
        if (count == 76) {   
  
            count = 0;   
  
            count++;   
  
            buffer.write(ESCAPE_CHAR);   
  
            buffer.write(CHUNK_SEPARATOR);   
  
        }   
  
        buffer.write(ESCAPE_CHAR);   
  
        char hex1 = Character.toUpperCase(Character   
  
                .forDigit((b >> 4) & 0xF, 16));   
  
        char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));   
  
        count++;   
  
        if (count == 76) {   
  
            count = 0;   
  
            count++;   
  
            buffer.write(ESCAPE_CHAR);   
  
            buffer.write(CHUNK_SEPARATOR);   
  
  
  
        }   
  
        buffer.write(hex1);   
  
        count++;   
  
        if (count == 76) {   
  
            count = 0;   
  
            count++;   
  
            buffer.write(ESCAPE_CHAR);   
  
            buffer.write(CHUNK_SEPARATOR);   
  
  
  
        }   
  
        buffer.write(hex2);   
  
        return count;   
  
    }   
  
    
    /**  
    
     * Convert a normal string to Base64 code format string  
 
     *   
 
     * @param str  
 
     * @return  
 
     */  
  
    public static String encodeBase64(String str) throws Exception {   
  
        byte[] binaryData = str.getBytes(CHARSET_UTF8);   
  
        int lengthDataBits = binaryData.length * EIGHTBIT;   
  
        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;   
  
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;   
  
        byte encodedData[] = null;   
  
        int encodedDataLength = 0;   
  
        int nbrChunks = 0;   
  
  
  
        if (fewerThan24bits != 0) {   
  
            // data not divisible by 24 bit   
  
            encodedDataLength = (numberTriplets + 1) * 4;   
  
        } else {   
  
            // 16 or 8 bit   
  
            encodedDataLength = numberTriplets * 4;   
  
        }   
  
  
  
        nbrChunks = (CHUNK_SEPARATOR.length == 0 ? 0 : (int) Math   
  
                .ceil((float) encodedDataLength / CHUNK_SIZE));   
  
        encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;   
  
  
  
        encodedData = new byte[encodedDataLength];   
  
  
  
        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;   
  
  
  
        int encodedIndex = 0;   
  
        int dataIndex = 0;   
  
        int i = 0;   
  
        int nextSeparatorIndex = CHUNK_SIZE;   
  
        int chunksSoFar = 0;   
  
  
  
        for (i = 0; i < numberTriplets; i++) {   
  
            dataIndex = i * 3;   
  
            b1 = binaryData[dataIndex];   
  
            b2 = binaryData[dataIndex + 1];   
  
            b3 = binaryData[dataIndex + 2];   
  
  
  
            l = (byte) (b2 & 0x0f);   
  
            k = (byte) (b1 & 0x03);   
  
  
  
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)   
  
                    : (byte) ((b1) >> 2 ^ 0xc0);   
  
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)   
  
                    : (byte) ((b2) >> 4 ^ 0xf0);   
  
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)   
  
                    : (byte) ((b3) >> 6 ^ 0xfc);   
  
  
  
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];   
  
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2   
  
                    | (k << 4)];   
  
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[(l << 2)   
  
                    | val3];   
  
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 0x3f];   
  
  
  
            encodedIndex += 4;   
  
  
  
            if (encodedIndex == nextSeparatorIndex) {   
  
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex,   
  
                        CHUNK_SEPARATOR.length);   
  
                chunksSoFar++;   
  
                nextSeparatorIndex = (CHUNK_SIZE * (chunksSoFar + 1))   
  
                        + (chunksSoFar * CHUNK_SEPARATOR.length);   
  
                encodedIndex += CHUNK_SEPARATOR.length;   
  
            }   
  
        }   
  
  
  
        // form integral number of 6-bit groups   
  
        dataIndex = i * 3;   
  
  
  
        if (fewerThan24bits == EIGHTBIT) {   
  
            b1 = binaryData[dataIndex];   
  
            k = (byte) (b1 & 0x03);   
  
            // log.debug("b1=" + b1);   
  
            // log.debug("b1<<2 = " + (b1>>2) );   
  
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)   
  
                    : (byte) ((b1) >> 2 ^ 0xc0);   
  
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];   
  
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];   
  
            encodedData[encodedIndex + 2] = ESCAPE_CHAR;   
  
            encodedData[encodedIndex + 3] = ESCAPE_CHAR;   
  
        } else if (fewerThan24bits == SIXTEENBIT) {   
  
  
  
            b1 = binaryData[dataIndex];   
  
            b2 = binaryData[dataIndex + 1];   
  
            l = (byte) (b2 & 0x0f);   
  
            k = (byte) (b1 & 0x03);   
  
  
  
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)   
  
                    : (byte) ((b1) >> 2 ^ 0xc0);   
  
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)   
  
                    : (byte) ((b2) >> 4 ^ 0xf0);   
  
  
  
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];   
  
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2   
  
                    | (k << 4)];   
  
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];   
  
            encodedData[encodedIndex + 3] = ESCAPE_CHAR;   
  
        }   
  
  
  
        // we also add a separator to the end of the final chunk.   
  
        if (chunksSoFar < nbrChunks) {   
  
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength   
  
                    - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);   
  
        }   
  
  
  
        return new String(encodedData);   
  
    }   
    
    
    /**  
    
     * Convert a base64 string to a normal string  
 
     *   
 
     * @param bStr  
 
     * @return  
 
     * @throws Exception  
 
     */  
  
    public static String decodeBase64(String bStr) throws Exception {   
  
        byte[] base64Data = bStr.getBytes();   
  
        base64Data = discardNonBase64(base64Data);   
  
  
  
        // handle the edge case, so we don't have to worry about it later   
  
        if (base64Data.length == 0) {   
  
            return null;   
  
        }   
  
  
  
        int numberQuadruple = base64Data.length / FOURBYTE;   
  
        byte decodedData[] = null;   
  
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;   
  
  
  
        // Throw away anything not in base64Data   
  
  
  
        int encodedIndex = 0;   
  
        int dataIndex = 0;   
  
        {   
  
            // this sizes the output array properly - rlw   
  
            int lastData = base64Data.length;   
  
            // ignore the '=' padding   
  
            while (base64Data[lastData - 1] == ESCAPE_CHAR) {   
  
                if (--lastData == 0) {   
  
                    return null;   
  
                }   
  
            }   
  
            decodedData = new byte[lastData - numberQuadruple];   
  
        }   
  
  
  
        for (int i = 0; i < numberQuadruple; i++) {   
  
            dataIndex = i * 4;   
  
            marker0 = base64Data[dataIndex + 2];   
  
            marker1 = base64Data[dataIndex + 3];   
  
  
  
            b1 = base64Alphabet[base64Data[dataIndex]];   
  
            b2 = base64Alphabet[base64Data[dataIndex + 1]];   
  
  
  
            if (marker0 != ESCAPE_CHAR && marker1 != ESCAPE_CHAR) {   
  
                b3 = base64Alphabet[marker0];   
  
                b4 = base64Alphabet[marker1];   
  
  
  
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);   
  
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));   
  
                decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);   
  
            } else if (marker0 == ESCAPE_CHAR) {   
  
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);   
  
            } else if (marker1 == ESCAPE_CHAR) {   
  
                b3 = base64Alphabet[marker0];   
  
  
  
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);   
  
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));   
  
            }   
  
            encodedIndex += 3;   
  
        }   
  
        return new String(decodedData, CHARSET_UTF8);   
  
    }   
    
    
    
    private static byte[] discardNonBase64(byte[] data) {   
    	  
        byte groomedData[] = new byte[data.length];   
  
        int bytesCopied = 0;   
  
  
  
        for (int i = 0; i < data.length; i++) {   
  
            if (isBase64(data[i])) {   
  
                groomedData[bytesCopied++] = data[i];   
  
            }   
  
        }   
  
  
  
        byte packedData[] = new byte[bytesCopied];   
  
  
  
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);   
  
  
  
        return packedData;   
  
    }   

  
    /**  
    
     * Test the byte  
 
     *   
 
     * @param octect  
 
     * @return  
 
     */  
  
    private static boolean isBase64(byte octect) {   
  
        if (octect == ESCAPE_CHAR) {   
  
            return true;   
  
        } else if (base64Alphabet[octect] == -1) {   
  
            return false;   
  
        } else {   
  
            return true;   
  
        }   
  
    }   
  

  


}  

