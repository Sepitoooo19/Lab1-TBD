package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Find all
   public List<ProductEntity> findAll() {
        String sql="SELECT * FROM products";
        return jdbcTemplate.query(sql,(rs,rownum)->
                new ProductEntity(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getInt("stock"),
                  rs.getFloat("price"),
                  rs.getString("category"),
                  rs.getInt("company_id")));
    }
    //find by id
   public ProductEntity findbyid(int id){
       String sql="SELECT * FROM products WHERE id= ?";
       return jdbcTemplate.queryForObject(sql,new Object[]{id},(rs,rownum)->
               new ProductEntity(
                   rs.getInt("id"),
                   rs.getString("name"),
                   rs.getInt("stock"),
                   rs.getFloat("price"),
                       rs.getString("category"),
                   rs.getInt("company_id")));
   }
   //SAVE
    public void save(ProductEntity p){
       String sql="INSERTO INTO products (name,stock,price,category,company_id) VALUES (?,?,?,?,?)";
       jdbcTemplate.update(sql,p.getName(),p.getStock(),p.getPrice(),p.getCompanyId());
    }
    //UPDATE
    public void update(ProductEntity p){
       String sql="UPDATE products SET name=?,stock=?,price=?,category=?,company_id=? WHERE id=?";
        jdbcTemplate.update(sql,p.getName(),p.getStock(),p.getPrice(),p.getCategory(),p.getCompanyId(),p.getId());
    }

    //DELETE
    public void delete(int id){
       String sql="DELETE FROM products WHERE id=?";
       jdbcTemplate.update(sql,id);
    }

    //find by stock para mostrar lo disponible
    public List<ProductEntity> findByStock(){
        String sql="SELECT * FROM products WHERE stock > 0";
        return jdbcTemplate.query(sql,(rs,rownum)->
                new ProductEntity(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getInt("stock"),
                  rs.getFloat("price"),
                  rs.getString("category"),
                  rs.getInt("company_id")));
    }

    //find by categoria
    public List<ProductEntity> findbyCategory(String c){
       String sql="SELECT * FROM products WHERE category=?";
       return jdbcTemplate.query(sql,new Object[]{c},(rs,rownum)->
               new ProductEntity(
                   rs.getInt("id"),
                   rs.getString("name"),
                   rs.getInt("stock"),
                   rs.getFloat("price"),
                   rs.getString("category"),
                       rs.getInt("company_id")));
    }

    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT category FROM products";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<ProductEntity> getTopProductsByCategoryAndMonth(String category, int month, int year) {
        String sql = """
        SELECT p.id, p.name, COUNT(op.product_id) AS product_count
        FROM products p
        JOIN order_products op ON p.id = op.product_id
        JOIN orders o ON o.id = op.order_id
        WHERE EXTRACT(MONTH FROM o.order_date) = ? 
        AND EXTRACT(YEAR FROM o.order_date) = ? 
        AND p.category = ?
        GROUP BY p.id, p.name
        ORDER BY product_count DESC
        LIMIT 5
    """;
        return jdbcTemplate.query(sql, new Object[]{month, year, category}, (rs, rowNum) ->
                new ProductEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        0, // Puedes obtener estos valores si los necesitas
                        0,
                        category, // Pasamos la categoría directamente
                        0 // Puedes obtener estos valores si los necesitas
                )
        );
    }

    //get products by company id
    public List<ProductEntity> findByCompanyId(int companyId) {
        String sql = "SELECT * FROM products WHERE company_id = ?";
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new ProductEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getInt("company_id")
                )
        );
    }

    //GetCompanyIdByProductId
    public int getCompanyIdByProductId(int productId) {
        String sql = "SELECT company_id FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
    }


}
