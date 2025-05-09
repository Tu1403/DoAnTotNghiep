package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.BillDetail;
import website.code.coffeeShop.repository.BillDetailRepository;

import java.util.List;
// import javax
@Service
public class BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;

    public void save(BillDetail billDetail) {billDetailRepository.save(billDetail);}

    public List<BillDetail> findByBillId(int billId) {

        return billDetailRepository.findByIdBillId(billId);
    }
}
