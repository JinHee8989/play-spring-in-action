package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository{

    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {

        return null;
    }
}
