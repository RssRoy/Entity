package practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ExcelReader {
	private static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		}

		return null;
	}

	public static List<FAQ> readBooksFromExcelFile(String excelFilePath) throws IOException {
		List<FAQ> listBooks = new ArrayList<FAQ>();
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		JsonArray jarr = new JsonArray();
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if(nextRow.getRowNum()==0) {
				System.out.println("Skipped first row ");
				continue;
			}
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			FAQ aBook = new FAQ();
			
			JsonObject element = new JsonObject();
			while (cellIterator.hasNext()) {
				
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				System.out.println(columnIndex);
				switch (columnIndex) {
				case 1:
					System.out.println("questions:  "+getCellValue(nextCell).toString());
					element.addProperty("question", getCellValue(nextCell).toString());
					break;
				case 2:
					String variations = (String) getCellValue(nextCell);
					JsonArray varArr = new JsonArray();
					System.out.println("variations:  "+variations);
					List<String> var = Arrays.asList(variations.split(" \\$\\$ "));
					for(String v : var) {
						System.out.println("v----------"+v);
						varArr.add(v);
					}
					System.out.println("variations:  "+variations.split(" \\$\\$ "));
					element.add("variation", varArr);
					break;
				case 3:
					System.out.println("answer:  "+getCellValue(nextCell).toString());
					element.addProperty("answer", getCellValue(nextCell).toString());
					break;
				}

			}
			System.out.println("element:   "+element);
			if(element.size()>0) {
				jarr.add(element);
			}
			listBooks.add(aBook);
		}

		inputStream.close();
		System.out.println(jarr.toString());
		return listBooks;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		String excelFilePath = "/home/ranjit/Desktop/default_data.xlsx";
	    //ExcelReaderExample2 reader = new ExcelReaderExample2();
	    List<FAQ> listBooks = readBooksFromExcelFile(excelFilePath);
	    //System.out.println(listBooks);


	}

}
