package website.code.coffeeShop.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageableDTO {
    Integer PageSize;

    Integer PageNumber;

    public PageableDTO(int currentPage, int pageSize) {
        this.PageNumber = currentPage;
        this.PageSize = pageSize;
    }
}
