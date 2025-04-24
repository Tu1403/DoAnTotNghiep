package website.code.coffeeShop.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoUongRequest {
    private String productName;

    private Float Price;
}
