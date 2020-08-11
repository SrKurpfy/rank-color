package me.pltzz.rankcolor.user;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.pltzz.rankcolor.RankColorPlugin;
import me.pltzz.rankcolor.util.SQLReader;
import me.saiintbrisson.minecraft.boilerplate.Dao;
import me.saiintbrisson.safe.Try;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class UserDataSourceDao implements Dao<UUID, User> {

    private SQLReader reader;
    private HikariDataSource dataSource;

    private UserAdapter adapter;

    public UserDataSourceDao(RankColorPlugin plugin) {
        this.adapter = new UserAdapter();

        Try.of(() -> {
            reader = new SQLReader();
            reader.loadFromResources(plugin.getClass(), "sql/");
        }).report("Could not read from resources")
          .printStackTrace();

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(plugin.getConfig().getString("database.url"));
        config.setUsername(plugin.getConfig().getString("database.username"));
        config.setPassword(plugin.getConfig().getString("database.password"));

        config.setMaximumPoolSize(5);

        this.dataSource = new HikariDataSource(config);

        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = prepareStatement(connection, "create_table");
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(Connection connection, String sqlName) throws SQLException {
        return connection.prepareStatement(reader.getSql(sqlName));
    }

    @Override
    public User getById(UUID key) {
        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = prepareStatement(connection, "select_user");
            statement.setString(1, key.toString());

            ResultSet set = statement.executeQuery();
            if(!set.next()) return null;

            return adapter.read(set);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public boolean save(UUID key, User element) {
        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = prepareStatement(connection, "create_user");
            adapter.insert(element, statement);
            adapter.update(element, statement);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(UUID key, User element) {
        return save(key, element);
    }

    @Override
    public boolean delete(UUID key) {
        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = prepareStatement(connection, "delete_user");
            statement.setString(1, key.toString());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
