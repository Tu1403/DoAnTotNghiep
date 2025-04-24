package website.code.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import website.code.coffeeShop.model.Feedback;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Feedback feedback;
    private String username;
}
