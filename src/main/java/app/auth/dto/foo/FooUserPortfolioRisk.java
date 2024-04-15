package app.auth.dto.foo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FooUserPortfolioRisk {
    private String email;
    private double currentRisk;
    private double previousRisk;
}
