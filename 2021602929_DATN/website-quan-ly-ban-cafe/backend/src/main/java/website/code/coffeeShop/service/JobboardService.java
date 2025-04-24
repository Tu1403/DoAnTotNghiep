package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.Jobboard;
import website.code.coffeeShop.repository.JobboardRepository;

@Service
public class JobboardService {
    @Autowired
    private JobboardRepository jobboardRepository;

    public Page<Jobboard> getAllJobboards(Pageable pageable) {return jobboardRepository.findAll(pageable);}

    public Page<Jobboard> searchJobboards(String fullname, Pageable pageable) {
        if(fullname.isEmpty() || fullname == null) {
            return getAllJobboards(pageable);
        }
        return jobboardRepository.findByUserFullnameContaining(fullname, pageable);
    }

    public void incrementPresents(int jobboardId) {
        Jobboard jobboard = jobboardRepository.findById(jobboardId).orElseThrow(() -> new RuntimeException("Jobboard not found"));
        jobboard.setPresents(jobboard.getPresents() + 1);
        jobboardRepository.save(jobboard);
    }

    public void incrementAbsents(int jobboardId) {
        Jobboard jobboard = jobboardRepository.findById(jobboardId).orElseThrow(() -> new RuntimeException("Jobboard not found"));
        jobboard.setAbsents(jobboard.getAbsents() + 1);
        jobboardRepository.save(jobboard);
    }

    public void updateShift(int jobboardId, int shift) {
        Jobboard jobboard = jobboardRepository.findById(jobboardId)
                .orElseThrow(() -> new RuntimeException("Jobboard not found"));
        jobboard.setShift(shift);
        jobboardRepository.save(jobboard);
    }
}
