package repository;

import lombok.extern.slf4j.Slf4j;
import model.Command;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CommandRepository extends DbRepository<Command> {
    private final String url = "jdbc:postgresql://localhost:5432/ResBot";
    private final String user = "postgres";
    private final String password = "********************";

    private static final String SELECT_COMMAND_STATEMENT = "SELECT id, name, parent FROM public.commands c WHERE c.id = ?";
    private static final String SELECT_ALL_COMMAND_STATEMENT = "SELECT id, name, parent FROM public.commands";
    private static final String INSERT_COMMAND_STATEMENT = "INSERT INTO public.commands (id, name, parent) VALUES (?, ?, ?);";
    private static final String UPDATE_COMMAND_STATEMENT = "UPDATE public.commands set name = ?, parent = ? WHERE id = ?";
    private static final String DELETE_COMMAND_STATEMENT = "DELETE FROM public.commands WHERE id = ?";

    @Override
    protected Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        }

        return this.connection;
    }

    @Override
    public void add(Command item) {
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_COMMAND_STATEMENT);
            statement.setLong(1, item.getId());
            statement.setString(2, item.getName());
            statement.setLong(3, item.getParent());
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
    public Command get(Long id) {
        Command command = null;
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_COMMAND_STATEMENT);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            command = new Command();
            while(resultSet.next()){
                command.setId(resultSet.getLong(1));
                command.setName(resultSet.getString(2));
                command.setParent(resultSet.getInt(3));
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
            command = null;
        }
        finally {
            this.close();
        }

        return command;
    }

    @Override
    public List<Command> getAll() {
        List<Command> commands = new ArrayList<>();
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_COMMAND_STATEMENT);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Command command = new Command();
                command.setId(resultSet.getLong(1));
                command.setName(resultSet.getString(2));
                command.setParent(resultSet.getInt(3));

                commands.add(command);
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
        }
        finally {
            this.close();
        }

        return commands;
    }

    @Override
    public void update(Command item) {
        this.update(item, item.getId());
    }

    @Override
    public void update(Command item, Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_COMMAND_STATEMENT);
            statement.setString(1, item.getName());
            statement.setLong(2, item.getParent());
            statement.setLong(3, item.getId());
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
    public void delete(Command item) {
        this.delete(item.getId());
    }

    @Override
    public void delete(Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COMMAND_STATEMENT);
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
    public boolean has(Command item) {
        return this.has(item.getId());
    }

    @Override
    public boolean has(Long id) {
        return this.get(id) != null;
    }
}
