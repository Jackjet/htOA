package test.google;
import java.io.File;  
import java.sql.Timestamp;
import java.util.Hashtable;     
import com.google.zxing.BarcodeFormat;  
import com.google.zxing.EncodeHintType;  
import com.google.zxing.MultiFormatWriter;  
import com.google.zxing.WriterException;  
import com.google.zxing.common.BitMatrix;    

public class Test {         
	/**      
	 * * @param args      
	 * * @throws Exception       
	 * */     
	public static void main(String[] args) throws Exception {          
		String text = "3GYFN9E58GS547826";          
		int width = 300;          
		int height = 300;          
		//二维码的图片格式          
		String format = "gif";          
		Hashtable hints = new Hashtable();          
		//内容所使用编码          
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");          
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE, width, height, hints); 
		//生成二维码          
		File outputFile = new File("d:"+File.separator+"new.gif");          
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);      
//		Timestamp a = Timestamp.valueOf("2015-10-10 13:34:12");
//		System.out.println(a instanceof java.util.Date);
			
	}  
}
