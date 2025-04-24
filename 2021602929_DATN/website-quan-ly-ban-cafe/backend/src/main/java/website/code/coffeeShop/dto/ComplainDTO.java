package website.code.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import website.code.coffeeShop.model.Complain;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplainDTO {
    private Complain complain;
    String username;
}
