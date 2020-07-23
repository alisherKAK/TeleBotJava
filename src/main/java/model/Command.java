package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Command extends BaseModel{
    private String name;
    private long parent;
}
