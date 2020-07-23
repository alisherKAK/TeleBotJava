package repository;

import interfaces.IRepository;
import lombok.extern.slf4j.Slf4j;
import model.BaseModel;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public abstract class DbRepository<T extends BaseModel> implements IRepository<T> {
    protected Connection connection;

    protected abstract Connection getConnection() throws SQLException;

    protected void close(){
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
    }
}
