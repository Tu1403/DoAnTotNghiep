package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.Tables;
import website.code.coffeeShop.repository.TableRepository;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public Iterable<Tables> getAllTables() {return tableRepository.findAll();}

    public void updateTableStatus(int tableId, int status) {
        Tables table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(status);
        tableRepository.save(table);
    }

    public Tables findByTid(int tid) {
        return tableRepository.findById(tid).orElse(null);
    }

    public void save (Tables table) {
        tableRepository.save(table);
    }
}
