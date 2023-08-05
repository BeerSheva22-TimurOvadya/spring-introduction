package telran.spring.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advert {

	private int id;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Category cannot be empty")
	private String category;

	@Min(value = 0, message = "Price must be non-negative")
	private double price;

	private String additionalFields;
}
