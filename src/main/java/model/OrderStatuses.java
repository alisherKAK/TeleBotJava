package model;

public enum OrderStatuses {
    Waiting(1),
    Succeed(2),
    Canceled(3);

    private int status;
    OrderStatuses(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}
