package model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel{
    private String name;
    private int typeId;
    private int countryId;
    private float price;
    private String description;
    private String imagePath;
}
