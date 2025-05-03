package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.FailedDeliveriesCompanyDTO;
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
    public List<CompanyEntity> findAll(){
        String sql= "SELECT * FROM companies";
        return jdbcTemplate.query(sql,(rs, rowNum) ->
               new CompanyEntity(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getString("email"),
                       rs.getString("phone")
               ));
    }

    //find by id
    public CompanyEntity findbyid(int id){
        String sql = "SELECT * FROM companies WHERE id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{id},(rs,rowNum)->
               new CompanyEntity(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getString("email"),
                       rs.getString("phone")
               ));
    }

    //SAVE
    public void save(CompanyEntity c){
        String sql = "INSERT INTO companies (name, email, phone) VALUES (?,?,?)";
        jdbcTemplate.update(sql,c.getName(),c.getEmail(),c.getPhone());
    }

    //UPDATE
    public void update(CompanyEntity c){
        String sql = "UPDATE companies SET name=?, email=?, phone=? WHERE id=?";
        jdbcTemplate.update(sql,c.getName(),c.getEmail(),c.getPhone(),c.getId());
    }

    //DELETE
   public  void delete(int id){
        String sql = "DELETE FROM companies WHERE id=?";
        jdbcTemplate.update(sql,id);
   }

    public List<FailedDeliveriesCompanyDTO> getCompaniesWithMostFailedDeliveries() {
        String sql = """
            SELECT 
                c.name AS company_name,
                COUNT(o.id) AS failed_deliveries
            FROM 
                orders o
            JOIN 
                dealers d ON o.client_id = d.id
            JOIN 
                company_driver cd ON d.id = cd.dealer_id
            JOIN 
                companies c ON cd.company_id = c.id
            WHERE 
                o.status = 'cancelado' -- Cambia esto si el estado de las entregas fallidas es diferente
            GROUP BY 
                c.id, c.name
            ORDER BY 
                failed_deliveries DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new FailedDeliveriesCompanyDTO(
                        rs.getString("company_name"),
                        rs.getInt("failed_deliveries")
                )
        );
    }

}
