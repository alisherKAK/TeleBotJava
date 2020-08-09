package repository;

import lombok.extern.slf4j.Slf4j;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductRepository extends DbRepository<Product> {
    private final String url = "jdbc:postgresql://localhost:5432/ResBot";
    private final String user = "postgres";
    private final String password = "********************";

    private static final String SELECT_PRODUCT_STATEMENT = "SELECT id, name, typeid, countryid, price, description, image " +
            "FROM public.products u WHERE u.id = ?";
    private static final String SELECT_ALL_PRODUCT_STATEMENT = "SELECT id, name, typeid, countryid, price, description, image FROM public.products";
    private static final String INSERT_PRODUCT_STATEMENT =
            "INSERT INTO public.products (name, typeid, countryid, price, description, image) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_PRODUCT_STATEMENT = "UPDATE public.products set " +
            "name = ?, typeid = ?, countryid = ?, price = ?, description = ?, image = ? " +
            "WHERE id = ?";
    private static final String DELETE_PRODUCT_STATEMENT = "DELETE FROM public.products WHERE id = ?";

    @Override
    protected Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        }

        return this.connection;
    }

    @Override
    public void add(Product item) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT_STATEMENT);
            statement.setString(1, item.getName());
            statement.setInt(2, item.getTypeId());
            statement.setInt(3, item.getCountryId());
            statement.setFloat(4, item.getPrice());
            statement.setString(5, item.getDescription());
            statement.setString(6, item.getImagePath());
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
    public Product get(Long id) {
        Product product = null;
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_STATEMENT);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                product = new Product();
                product.setId(id);
                product.setName(result.getString(2));
                product.setTypeId(result.getInt(3));
                product.setCountryId(result.getInt(4));
                product.setPrice(result.getFloat(5));
                product.setDescription(result.getString(6));
                product.setImagePath(result.getString(7));
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
            product = null;
        }
        finally {
            this.close();
        }

        return product;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUCT_STATEMENT);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                Product product = new Product();
                product.setId(result.getLong(1));
                product.setName(result.getString(2));
                product.setTypeId(result.getInt(3));
                product.setCountryId(result.getInt(4));
                product.setPrice(result.getFloat(5));
                product.setDescription(result.getString(6));
                product.setImagePath(result.getString(7));

                products.add(product);
            }
        }
        catch(SQLException ex){
            log.warn(ex.getMessage());
            products.clear();
        }
        finally {
            this.close();
        }

        return products;
    }

    @Override
    public void update(Product item) {
        this.update(item, item.getId());
    }

    @Override
    public void update(Product item, Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_STATEMENT);
            statement.setString(1, item.getName());
            statement.setInt(2, item.getTypeId());
            statement.setInt(3, item.getCountryId());
            statement.setFloat(4, item.getPrice());
            statement.setString(5, item.getDescription());
            statement.setString(6, item.getImagePath());
            statement.setLong(7, id);
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
    public void delete(Product item) {
        this.delete(item.getId());
    }

    @Override
    public void delete(Long id) {
        try{
            connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_STATEMENT);
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
    public boolean has(Product item) {
        return this.has(item.getId());
    }

    @Override
    public boolean has(Long id) {
        return this.get(id) != null;
    }
}
