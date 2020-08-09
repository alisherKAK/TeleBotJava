package model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Order extends BaseModel {
    private long userId;
    private long productId;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private int statusId = OrderStatuses.Waiting.getStatus();
    private int amount = 1;
}
