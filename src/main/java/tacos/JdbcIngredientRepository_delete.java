package tacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.data.IngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository //@Component에서 특화된 액세스관련 스테레오타입 애노테이션
public class JdbcIngredientRepository_delete implements IngredientRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepository_delete(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query("select id, name, type from Ingredient",this::mapRowToIngredient);
    }

    @Override
    public Ingredient findById(String id) {
        return jdbc.queryForObject("select id, name, type from Ingredient where id=?",this::mapRowToIngredient,id);
    }

    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(rs.getString("id"),rs.getString("name"),Ingredient.Type.valueOf(rs.getString("type")));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(
                "insert into Ingredient (id,name,type) values (?,?,?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString()
        );

        return ingredient;
    }


}
