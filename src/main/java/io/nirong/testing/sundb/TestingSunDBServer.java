/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nirong.testing.sundb;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.airlift.log.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.airlift.testing.FileUtils.deleteRecursively;
import static java.lang.String.format;
import static java.nio.file.Files.createTempDirectory;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class TestingSunDBServer
		implements Closeable
{
    private static final Logger log = Logger.get(TestingSunDBServer.class);

    private final String user;
    private final String password;
    private final Set<String> databases;
    private final int port;
    private final Path sundbDir;
    private final EmbeddedSunDB server;

    public TestingSunDBServer(String user, String password, String... databases)
            throws Exception
    {
        this(user, password, ImmutableList.copyOf(databases));
    }

    public TestingSunDBServer(String user, String password, Iterable<String> databases)
            throws Exception
    {
        this.user = checkNotNull(user, "user is null");
        this.password = checkNotNull(password, "password is null");
        this.databases = ImmutableSet.copyOf(checkNotNull(databases, "databases is null"));
        port = randomPort();

        sundbDir = createTempDirectory("testing-sundb-server");
        Path dataDir = sundbDir.resolve("data");
        server = new EmbeddedSunDB();
        /*
        server = new EmbeddedSunDB(sundbDir.toFile(), dataDir.toFile());

        Map<String, String> args = ImmutableMap.<String, String>builder()
                .put(MysqldResourceI.PORT, Integer.toString(port))
                .put(MysqldResourceI.INITIALIZE_USER, "true")
                .put(MysqldResourceI.INITIALIZE_USER_NAME, user)
                .put(MysqldResourceI.INITIALIZE_PASSWORD, password)
                .build();

        server.start("testing-mysql-server", args);
*/
        server.start("testing-sundb-server");

        if (!server.isRunning()) {
            close();
            throw new RuntimeException("SUNDB did not start");
        }

        try (Connection connection = waitForConnection(getJdbcUrl())) {
            for (String database : databases) {
                try (Statement statement = connection.createStatement()) {
                    execute(statement, format("CREATE DATABASE %s", database));
                    execute(statement, format("GRANT ALL ON %s.* TO '%s'@'%%' IDENTIFIED BY '%s'", database, user, password));
                }
            }
        }
        catch (SQLException e) {
            close();
            throw e;
        }

        log.info("MySQL server ready: %s", getJdbcUrl());
    }

    private static int randomPort()
            throws IOException
    {
        try (ServerSocket socket = new ServerSocket()) {
            socket.bind(new InetSocketAddress(0));
            return socket.getLocalPort();
        }
    }

    private static void execute(Statement statement, String sql)
            throws SQLException
    {
        log.debug("Executing: %s", sql);
        statement.execute(sql);
    }

    private static Connection waitForConnection(String jdbcUrl)
            throws InterruptedException
    {
        while (true) {
            try {
                return DriverManager.getConnection(jdbcUrl);
            }
            catch (SQLException e) {
                // ignored
            }
            log.info("Waiting for MySQL to start at " + jdbcUrl);
            MILLISECONDS.sleep(10);
        }
    }

    @Override
    public void close()
    {
        try {
            server.shutdown();
        }
        finally {
            deleteRecursively(sundbDir.toFile());
        }
    }

    public boolean isRunning()
    {
        return server.isRunning();
    }

    public boolean isReadyForConnections()
    {
        return server.isReadyForConnections();
    }

    public String getSunDBVersion()
    {
        return server.getVersion();
    }

    public String getUser()
    {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public Set<String> getDatabases()
    {
        return databases;
    }

    public int getPort()
    {
        return port;
    }

    public String getJdbcUrl()
    {
        return format("jdbc:sundb://localhost:%d?/%s", port, user, password);
    }
}
