package me.pltzz.rankcolor.user;

import me.saiintbrisson.minecraft.boilerplate.SQLAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAdapter implements SQLAdapter<User> {

    @Override
    public User read(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        String name = resultSet.getString("name");
        String rank = resultSet.getString("rank_color");

        return new User(id, name, rank);
    }

    @Override
    public void insert(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getId().toString());
        statement.setString(2, user.getName());
        statement.setString(3, user.getRank());
    }

    @Override
    public void update(User user, PreparedStatement statement) throws SQLException {
        statement.setString(4, user.getRank());
    }
}
