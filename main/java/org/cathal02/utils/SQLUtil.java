package org.cathal02.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.cathal02.Crates;

import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class SQLUtil {

    public Connection connection;
    Statement statement;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public SQLUtil(final Crates plugin) {
        host = plugin.getConfig().getString("sqlHost");
        port = plugin.getConfig().getInt("port");
        database = plugin.getConfig().getString("database");
        username = plugin.getConfig().getString("username");
        password = plugin.getConfig().getString("password");

        (new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.createStatement().execute("SELECT 1");
                    }
                } catch (final SQLException e) {
                    connection = getNewConnection();
                }
            }
        }).runTaskTimerAsynchronously(plugin, 60 * 20, 60 * 20);
        init();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private Connection getNewConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            final String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            final Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (final ClassNotFoundException | SQLException e) {
            System.out.println("[Crates] Could not connect to sql database");
            return null;
        }
    }

    public boolean checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = getNewConnection();

            if (connection == null || connection.isClosed()) {
                return false;
            }
            // CREATE statements here
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS playerData (playerUUID char(36), " +
                    "crateCooldown VARCHAR(100), crateOpens VARCHAR(500))");
        }
        return true;
    }

    public boolean init() {

        try {
            return checkConnection();
        } catch (final SQLException e) {
            // Handle Possible exception caused by syntax or other reasons.
            return false;
        }
    }

    public PlayerDataResponse getPlayerCrateCooldown(final UUID player) {
        try {
            if (!checkConnection() || connection == null) return new PlayerDataResponse();

            if (!playerExists(player)) {
                createPlayer(player);
            }

            final PreparedStatement ps = connection.prepareStatement("SELECT crateCooldown,crateOpens FROM " +
                    "playerData" +
                    " WHERE " +
                    "playerUUID=?");

            ps.setString(1, player.toString());
            final ResultSet results = ps.executeQuery();
            if (!results.next()) return null;

            final String coolDown = results.getString(1);
            final String[] opens = results.getString(2).split(",");
            final List<LocalDateTime> times = new ArrayList<>();

            Stream.of(opens).forEach(s -> {
                if (!s.equalsIgnoreCase("")) {
                    try {
                        times.add(LocalDateTime.parse(s));
                    } catch (final DateTimeException e) {
                        System.out.println("[Crates] Could not parse time. ");
                    }

                }
            });

            return new PlayerDataResponse(LocalDateTime.parse(coolDown), times);
        } catch (final SQLException e) {
            System.out.println("[Crates] Failed to get player data.");
        }
        return new PlayerDataResponse();
    }


    public void createPlayer(final UUID player) {
        try {
            if (!checkConnection()) return;

            if (connection != null) {
                final PreparedStatement ps = connection.prepareStatement("INSERT INTO playerData (playerUUID," +
                        "crateCooldown," +
                        "crateOpens) VALUES (?,?,?)");
                ps.setString(1, player.toString());
                ps.setString(2, "");
                ps.setString(3, "");
                ps.execute();
                ps.close();

            }
        } catch (final SQLException e) {
            System.out.println("[Crates] Failed to create player");
        }
    }

    private boolean playerExists(final UUID player) {
        try {
            if (!checkConnection()) return false;

            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerData WHERE playerUUID=?");
            ps.setString(1, player.toString());

            final ResultSet set = ps.executeQuery();
            final boolean found = set.next();

            set.close();
            return found;

        } catch (final SQLException e) {
            System.out.println("[Crates] Failed to check if player exists");

            return false;
        }
    }

    public void update(final UUID player, final LocalDateTime coolDownTime, final List<LocalDateTime> crateOpens) {
        try {
            if (!checkConnection()) return;

            if (!playerExists(player)) {
                createPlayer(player);
                return;
            }

            final PreparedStatement ps = connection.prepareStatement("UPDATE playerData SET crateCooldown=?," +
                    "crateOpens=? WHERE playerUUID=?");
            ps.setString(1, coolDownTime.toString());

            final List<String> times = new ArrayList<>();
            for (final LocalDateTime time : crateOpens) {
                times.add(time.toString());
            }

            final String crateOpenString = String.join(",", times);

            ps.setString(2, crateOpenString);
            ps.setString(3, player.toString());

            ps.execute();
            ps.close();
        } catch (
                final Exception e) {
            System.out.println("[Crates] Failed to update player");

        }
    }
}

