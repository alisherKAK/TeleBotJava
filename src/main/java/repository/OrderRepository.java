package repository;

import lombok.extern.slf4j.Slf4j;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderRepository extends DbRepository<Order> {
    private final String url = "jdbc:postgresql://localhost:5432/ResBot";
    private final String user = "postgres";
    private final String password = "********************";

    private static final String SELECT_ORDER_STATEMENT = "SELECT id, userid, productid, timestamp, statusid, amount " +
            "FROM public.orders u WHERE u.id = ?";
    private static final String SELECT_ALL_ORDER_STATEMENT =
            "SELECT id, userid, productid, timestamp, statusid, amount FROM public.orders";
    private static final String INSERT_ORDER_STATEMENT =
            "INSERT INTO public.orders (userid, productid, timestamp, statusid, amount) " +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_ORDER_STATEMENT =
            "UPDATE public.orders set " +
            "userid = ?, productid = ?, timestamp = ?, statusid = ?, amount = ?" +
            "WHERE id = ?";
    private static final String DELETE_ORDER_STATEMENT = "DELETE FROM public.orders WHERE id = ?";

    @Override
    protected Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        }

        return this.connection;
    }

    @Override
    public void add(Order item) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_ORDER_STATEMENT);
            statement.setLong(1, item.getUserId());
            statement.setLong(2, item.getProductId());
            statement.setTimestamp(3, item.getTimestamp());
            statement.setInt(4, item.getStatusId());
            statement.setInt(5, item.getAmount());
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
    public Order get(Long id) {
        Order order = null;

        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_STATEMENT);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                order = new Order();
                order.setId(id);
                order.setUserId(result.getLong(2));
                order.setProductId(result.getLong(3));
                order.setTimestamp(result.getTimestamp(4));
                order.setStatusId(result.getInt(5));
                order.setAmount(result.getInt(6));
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
            order = null;
        }
        finally {
            this.close();
        }

        return order;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();

        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ORDER_STATEMENT);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setUserId(result.getLong(2));
                order.setProductId(result.getLong(3));
                order.setTimestamp(result.getTimestamp(4));
                order.setStatusId(result.getInt(5));
                order.setAmount(result.getInt(6));

                orders.add(order);
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
            orders.clear();
        }
        finally {
            this.close();
        }

        return orders;
    }

    @Override
    public void update(Order item) {
        this.update(item, item.getId());
    }

    @Override
    public void update(Order item, Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_STATEMENT);
            statement.setLong(1, item.getUserId());
            statement.setLong(2, item.getProductId());
            statement.setTimestamp(3, item.getTimestamp());
            statement.setInt(4, item.getStatusId());
            statement.setInt(5, item.getAmount());
            statement.setLong(6, id);
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
    public void delete(Order item) {
        this.delete(item.getId());
    }

    @Override
    public void delete(Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_STATEMENT);
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
    public boolean has(Order item) {
        return this.has(item.getId());
    }

    @Override
    public boolean has(Long id) {
        return this.get(id) != null;
    }
}
