import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class BillReader {
	int i=0;
	
	
    public static void main(String[] args) {
    	String[]phoneNumbers= {"201-702-3929","330-501-4669","469-617-1147","803-693-2543","803-792-2439","803-992-3317","803-992-3443","980-616-1500"};
        try {
        	File files = new File("G:/TV/MyBill_20220425.pdf");
            PDDocument document = PDDocument.load(files);
            int year=(int)Integer.parseInt( "G:/TV/MyBill_20220425.pdf".substring(13, 17));
            int x=1;
            if(year==2024) {
            	x=2;
            }
            String month="G:/TV/MyBill_20240325.pdf".substring(15, 16);
            // Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();
            for(int i=document.getNumberOfPages()-1;i>12;i--) {
            	document.removePage(i);
            }
            
            System.out.println(document.getNumberOfPages());
            
            // Retrieving text from PDF document
            String text = pdfStripper.getText(document);
            
            // Printing the text
           // System.out.println(text);
            Stream<String> line=text.lines();
            		
            String[] data = text.split("\n+");
            

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("pdf content");
            for(int i=0;i<data.length;i++) {
            Row row = sheet.createRow(i);
            		String[] data2=	data[i].split("\\s+");
            			for(int j=0;j<data2.length;j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(data2[j]);
                    }
            	}
            		
          String FileName="G:/TV/template.xlsx";
            FileOutputStream outputStream = new FileOutputStream(FileName);
            
            workbook.write(outputStream);

            // Closing the workbook
            workbook.close();

            // Closing the document
            document.close();
            
            FileInputStream file = new FileInputStream(new File(FileName));
			XSSFWorkbook workbook2 = new XSSFWorkbook(file);
			
			int index = workbook2.getSheetIndex("pdf content");

			XSSFSheet sheet2 = workbook2.getSheetAt(index);
			
			int number=0;
			int rowindex = 0;
			sheet2.getLastRowNum();
			
			outer:	while(phoneNumbers.length>number) {
			for(rowindex=0;rowindex<sheet2.getLastRowNum()-1;rowindex++) {
				
			
				XSSFRow xrow=	sheet2.getRow(rowindex);
				
				
				for(int cellindex = 0;xrow.getLastCellNum()>cellindex;cellindex++) {
					XSSFCell xcell=xrow.getCell(cellindex);

					if (xcell.getCellTypeEnum() == CellType.STRING) {
						if(number==phoneNumbers.length) {
							break outer;
						}
						if(xcell.getStringCellValue().equals(phoneNumbers[number])) {
							
							XSSFRow row=	sheet2.getRow((rowindex)-x);
							
							
							if (row.getCell(0).getCellTypeEnum()==CellType.STRING) {
								System.out.print(row.getCell(0).getStringCellValue()+" ");
							}
							
							if (row.getCell(1).getCellTypeEnum()==CellType.STRING) {
								System.out.println(row.getCell(1).getStringCellValue());
							}
							if(x==2) {
							
							if (row.getCell(2).getCellTypeEnum()==CellType.STRING) {
								System.out.println(row.getCell(2).getStringCellValue());
							}
							}
							else {
								row=	sheet2.getRow((rowindex)+1);
								if (row.getCell(0).getCellTypeEnum()==CellType.STRING) {
									System.out.println(row.getCell(0).getStringCellValue());
								}
							}
											
							number=number+1;
							
							
							
						}
						
						
					} 
					
				}
				
			}
			if(number<phoneNumbers.length) {
				number=number+1;
			}
			
			}
			
				
			

			file.close();
			workbook.close();
			
            
            
            

            System.out.println("PDF content written to Excel successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }  
   
    }
}