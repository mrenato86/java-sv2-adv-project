package springsskytravel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Participant {

    @NotBlank(message = "Name must be provided")
    private String name;

    @PositiveOrZero(message = "Age cannot be negative")
    private int age;
}
