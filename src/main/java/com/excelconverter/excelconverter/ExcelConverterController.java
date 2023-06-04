package com.excelconverter.excelconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
public class ExcelConverterController
{



    @PostMapping("/convert")
    @Cacheable("dataCache")
    public ResponseEntity<String> convertExcelToJson(@RequestParam("file") MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming sheet 1 has data

            List<Map<String, Object>> dataList = new ArrayList<>();

            // Iterate over rows
            for (Row row : sheet) {
                Map<String, Object> rowData = new HashMap<>();

                // Iterate over cells
                for (Cell cell : row) {
                    String cellValue = getCellValueAsString(cell);
                    String columnName = getColumnHeader(sheet, cell.getColumnIndex());
// LL array
                    rowData.put(columnName, cellValue);
                }

                dataList.add(rowData);
            }

            // 1. Convert the dataList to JSON using ObjectMapper
            // 2. Store the JSON in a temporary file
            // 3. Store the temporary file on the local machine

            //1
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dataList);

            //2
            File tempFile = File.createTempFile("data", ".json");
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                fileWriter.write(json);
            }
            //delete the tempfile

            //3
            Path destination = Paths.get("D:/JSON/file.json");
            Files.copy(tempFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Excel converted to JSON and stored on the local machine.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing the file.");
        }
    }


    private String getColumnHeader(Sheet sheet, int columnIndex) {
        Row headerRow = sheet.getRow(0);
        Cell headerCell = headerRow.getCell(columnIndex);
        return headerCell.getStringCellValue();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
}



//        git config --global user.name "pankajsingh69"
