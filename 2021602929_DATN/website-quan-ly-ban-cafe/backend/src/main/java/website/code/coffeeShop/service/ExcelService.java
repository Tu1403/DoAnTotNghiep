package website.code.coffeeShop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface ExcelService {
    public void save(MultipartFile file);

    void exportDataToExcelTemplate(OutputStream outputStream);
}
