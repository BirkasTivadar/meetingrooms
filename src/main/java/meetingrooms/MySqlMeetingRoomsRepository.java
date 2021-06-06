package meetingrooms;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class MySqlMeetingRoomsRepository implements MeetingRoomsRepository {

    private JdbcTemplate jdbcTemplate;

    public MySqlMeetingRoomsRepository() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        flyway.migrate();

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(String name, int width, int length) {
        jdbcTemplate.update("INSERT INTO meetingrooms(mr_name, mr_width, mr_length) VALUES (?,?,?)", name, width, length);
    }

    @Override
    public List<String> getOrderedNames() {
        return jdbcTemplate.query("SELECT mr_name FROM meetingrooms ORDER BY mr_name;",
                (rs, i) -> rs.getString("mr_name"));
    }

    @Override
    public List<String> getReversedNames() {
        return jdbcTemplate.query("SELECT mr_name FROM meetingrooms ORDER BY mr_name DESC;",
                (rs, i) -> rs.getString("mr_name"));
    }

    @Override
    public List<String> getEvenOrderedNames() {
        return jdbcTemplate.query("SELECT mr_name FROM (SELECT mr_name, row_number() over (ORDER BY mr_name) as `rn` FROM `meetingrooms`) as `w_rownum` WHERE w_rownum.rn % 2 = 0;",
                (rs, i) -> rs.getString("mr_name"));
    }

    @Override
    public List<MeetingRoom> getMeetingRoomsOrderedByAreaDesc() {
        return jdbcTemplate.query("SELECT * FROM meetingrooms ORDER BY mr_width*mr_length DESC;",
                (rs, i) -> new MeetingRoom(rs.getLong("id"), rs.getString("mr_name"), rs.getInt("mr_width"), rs.getInt("mr_length")));
    }

    @Override
    public MeetingRoom getMeetingRoomsWithName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM meetingrooms WHERE mr_name= ?;",
                new Object[]{name}, (rs, i) -> new MeetingRoom(rs.getString("mr_name"), rs.getInt("mr_width"), rs.getInt("mr_length"))
        );
    }

    @Override
    public MeetingRoom getMeetingRoomsContains(String part) {
        String partSql = "%" + part + "%";

        return jdbcTemplate.queryForObject("SELECT * FROM meetingrooms WHERE mr_name LIKE ? ORDER BY mr_name;",
                new Object[]{partSql}, (rs, i) -> new MeetingRoom(rs.getString("mr_name"), rs.getInt("mr_width"), rs.getInt("mr_length"))
        );
    }

    @Override
    public List<MeetingRoom> getAreasLargerThan(int area) {
        return jdbcTemplate.query("SELECT * FROM meetingrooms WHERE mr_width*mr_length > ? ;",
                new Object[]{area}, (rs, i) -> new MeetingRoom(rs.getString("mr_name"), rs.getInt("mr_width"), rs.getInt("mr_length"))
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from meetingrooms");
    }
}
