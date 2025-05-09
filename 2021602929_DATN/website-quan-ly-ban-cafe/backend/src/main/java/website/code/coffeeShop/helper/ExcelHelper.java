package website.code.coffeeShop.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import website.code.coffeeShop.model.DoUong;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Product name", "Price"};
    static String SHEET = "Dinks";

    public static boolean hasExcelFormat(MultipartFile file) {
        if(!TYPE.equals(file.getContentType())){
            return false;
        }

        return true;
    }

    public static List<DoUong> excelToTutorials(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        try{
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<DoUong> doUongs = new ArrayList<DoUong>();

            int rowNumber = 0;
            while( rows.hasNext()) {
                Row currentRow = rows.next();

                if(rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                DoUong doUong = new DoUong();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx){
                        case 0:
                            doUong.setTen(currentCell.getStringCellValue());
                            break;
                        case 1:
                            doUong.setGia((float) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                doUongs.add(doUong);
            }
            return doUongs;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
        return new ArrayList<>();
    }
}
