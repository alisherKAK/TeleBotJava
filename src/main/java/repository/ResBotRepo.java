package repository;

import lombok.extern.slf4j.Slf4j;
import model.User;

import java.sql.*;

@Slf4j
public class ResBotRepo {
    private final String url = "jdbc:postgresql://localhost/ResBot";
    private final String user = "postgres";
    private final String password = "*********************";

    private static final String INSERT_USER_STATEMENT = "INSERT INTO public.Users(id, name, status) VALUES (?, ?, ?);";

    public void saveUser(User user){
        try {
            Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_STATEMENT);
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getStatus());
            statement.executeQuery();
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
    }
}
