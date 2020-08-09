package model;

public enum ProductTypes {
    Drink(1),
    Meal(2);

    private int code;
    ProductTypes(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
