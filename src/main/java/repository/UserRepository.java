package repository;

import lombok.extern.slf4j.Slf4j;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepository extends DbRepository<User> {
    private final String url = "jdbc:postgresql://localhost:5432/ResBot";
    private final String user = "postgres";
    private final String password = "********************";

    private static final String SELECT_USER_STATEMENT = "SELECT id, name, status FROM public.users u WHERE u.id = ?";
    private static final String SELECT_ALL_USER_STATEMENT = "SELECT id, name, status FROM public.users";
    private static final String INSERT_USER_STATEMENT = "INSERT INTO public.users (id, name, status) VALUES (?, ?, ?);";
    private static final String UPDATE_USER_STATEMENT = "UPDATE public.users set name = ?, status = ? WHERE id = ?";
    private static final String DELETE_USER_STATEMENT = "DELETE FROM public.users WHERE id = ?";

    @Override
    protected Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        }

        return this.connection;
    }

    @Override
    public void add(User item) {
        try {
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_STATEMENT);
            statement.setLong(1, item.getId());
            statement.setString(2, item.getName());
            statement.setLong(3, item.getStatus());
            statement.execute();
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
        finally {
            this.close();
        }
    }

    @Override
    public User get(Long id) {
        User user = null;
        try {
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_STATEMENT);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                user = new User();
                user.setId(result.getLong(1));
                user.setName(result.getString(2));
                user.setStatus(result.getInt(3));

                log.info(String.valueOf(result.getLong(1)));
                log.info(result.getString(2));
                log.info(result.getString(3));
            }

        }
        catch(SQLException ex){
            log.warn("getUser : chatId={}", id);
        }
        finally {
            this.close();
        }

        return user;
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USER_STATEMENT);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setStatus(resultSet.getInt(3));
                users.add(user);
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
        finally {
            this.close();
        }

        return users;
    }

    @Override
    public void update(User item) {
        this.update(item, item.getId());
    }

    @Override
    public void update(User item, Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_STATEMENT);
            statement.setString(1, item.getName());
            statement.setLong(2, item.getStatus());
            statement.setLong(3, id);
            statement.executeUpdate();
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
        finally {
            this.close();
        }
    }

    @Override
    public void delete(User item) {
        this.delete(item.getId());
    }

    @Override
    public void delete(Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_STATEMENT);
            statement.setLong(1, id);
            statement.execute();
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
        finally {
            this.close();
        }
    }

    @Override
    public boolean has(User item) {
        return this.has(item.getId());
    }

    @Override
    public boolean has(Long id) {
        User user = this.get(id);
        return user != null;
    }
}
