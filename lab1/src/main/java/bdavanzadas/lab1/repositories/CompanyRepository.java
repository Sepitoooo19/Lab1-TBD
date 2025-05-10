package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //find all
    public List<CompanyEntity> findAll() {
        String sql = "SELECT * FROM companies";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failed_deliveries"),
                        rs.getInt("total_sales")
                )
        );
    }

    //find by id
    public CompanyEntity findbyid(int id){
        String sql = "SELECT * FROM companies WHERE id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{id},(rs,rowNum)->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failedDeliveries"),
                        rs.getInt("total_sales")
                ));
    }

    //SAVE
    public void save(CompanyEntity c) {
        String sql = "INSERT INTO companies (name, email, phone, address, rut, type, deliveries, failed_deliveries, total_sales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getRut(), c.getType(), c.getDeliveries(), c.getFailedDeliveries(), c.getTotalSales());
    }

    //UPDATE
    public void update(CompanyEntity c) {
        String sql = "UPDATE companies SET name = ?, email = ?, phone = ?, address = ?, rut = ?, type = ?, deliveries = ?, failed_deliveries = ?, total_sales = ? WHERE id = ?";
        jdbcTemplate.update(sql, c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getRut(), c.getType(), c.getDeliveries(), c.getFailedDeliveries(), c.getTotalSales(), c.getId());
    }

    //DELETE
   public  void delete(int id){
        String sql = "DELETE FROM companies WHERE id=?";
        jdbcTemplate.update(sql,id);
   }

    public List<CompanyEntity> getCompaniesWithMostFailedDeliveries() {
        String sql = """
        SELECT 
            c.id,
            c.name,
            c.email,
            c.phone,
            c.address,
            c.rut,
            c.type,
            c.deliveries,
            c.failed_deliveries,
            c.total_sales
        FROM 
            companies c
        ORDER BY 
            c.failed_deliveries DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failed_deliveries"),
                        rs.getInt("total_sales")
                )
        );
    }

}
