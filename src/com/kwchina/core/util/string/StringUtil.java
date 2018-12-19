package com.kwchina.core.util.string;


public class StringUtil {

	private StringUtil() {
	}

	public static int indexOf(String srcString, String searchString) {
		return indexOf(srcString, searchString, 0, srcString.length());
	}

	public static int indexOf(String srcString, String searchString, int start) {
		return indexOf(srcString, searchString, start, srcString.length());
	}

	public static int indexOf(String srcString, String searchString, int start, int end) {
		return srcString.indexOf(searchString, start);
	}

	public static String replace(String srcString, String subString, String replaceString) {
		return replace(srcString, subString, replaceString, 0, srcString.length());
	}

	public static String replace(String srcString, String subString, String replaceString, int start) {
		return replace(srcString, subString, replaceString, start, srcString.length());
	}

	public static String replace(String srcString, String subString, String replaceString, int start, int end) {
		int stringLength = subString.length();
		int startPosition = 0;
		int endPosition = indexOf(srcString, subString);
		StringBuffer sb = new StringBuffer();
		for (; endPosition != -1; endPosition = indexOf(srcString, subString, startPosition)) {
			sb.append(srcString.substring(startPosition, endPosition));
			sb.append(replaceString);
			startPosition = endPosition + stringLength;
		}

		sb.append(srcString.substring(startPosition));
		return sb.toString();
	}

	public static int countOccurence(String srcString, String searchString) {
		return countOccurence(srcString, searchString, 0, srcString.length());
	}

	public static int countOccurence(String srcString, String searchString, int start) {
		return countOccurence(srcString, searchString, start, srcString.length());
	}

	public static int countOccurence(String srcString, String searchString, int start, int end) {
		int stringLength = searchString.length();
		int count = 0;
		for (int nextPosition = indexOf(srcString, searchString); nextPosition != -1; nextPosition = indexOf(srcString, searchString, nextPosition + stringLength))
			count++;

		return count;
	}
	
	public static boolean isNotEmpty(String srcString){
		if(srcString != null && !srcString.equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isNumeric(String str) {
		final String number = "0123456789";
		for(int i = 0;i < number.length(); i ++) {
			if(number.indexOf(str.charAt(i)) == -1) {  
				return false;  
			}  
		}  
		return true;
	}

	public static void main(String args[]) {
		String srcString = new String("AAA_BBBBBZZDD_AA_SFKLJFLS_AAA_KDLHLDADD_AAA_ASD");
		System.out.println(replace(srcString, "AA", "QQQTTT"));
		System.out.println(countOccurence(srcString, "AA"));
	}
}