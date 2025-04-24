package website.code.coffeeShop.service;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import website.code.coffeeShop.exception.FileNotFoundException;
import website.code.coffeeShop.helper.ExcelHelper;
import website.code.coffeeShop.model.DoUong;
import website.code.coffeeShop.repository.DoUongRepository;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    DoUongRepository doUongRepository;

    public void save(MultipartFile file) {
        try{
            List<DoUong> doUongs = ExcelHelper.excelToTutorials(file.getInputStream());
            doUongRepository.saveAll(doUongs);
        }catch(Exception e){
            throw new RuntimeException("Lưu trữ dữ liệu excel thất bại: "  + e.getMessage());
        }
    }

    @Override
    public void exportDataToExcelTemplate(OutputStream outputStream) {
        List<DoUong> data = initializeData();

        Map<String, Object> doUongs = new HashMap<>();
        doUongs.put("data", data);

        try (InputStream inputStream = this.getClass().getResourceAsStream("/template_exports/template_server_list_for_export2.xlsx")){
            Context context = new Context();
            context.toMap().putAll(doUongs);
            JxlsHelper.getInstance().processTemplate(inputStream, outputStream, context);
        }catch(NullPointerException e){
            throw new FileNotFoundException("Template not found in resources/templates_exports", e);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<DoUong> initializeData() {
        try{
            List<DoUong> doUongs = doUongRepository.findAll();
            return doUongs;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
