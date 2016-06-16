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

import com.google.common.collect.ImmutableSet;
import org.testng.annotations.Test;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestTestingSunDBServer
{
    @Test
    public void testDatabase()
            throws Exception
    {
        try (TestingSunDBServer server = new TestingSunDBServer("TEST", "test", "test", 22222)) {
//            assertTrue(server.isRunning());
//            assertTrue(server.isReadyForConnections());
//            assertEquals(server.getSunDBVersion(), "5.5.9");
//            assertEquals(server.getDatabases(), ImmutableSet.of("db1", "db2"));
//            assertEquals(server.getUser(), "TEST");
//            assertEquals(server.getPassword(), "test");
//            assertEquals(server.getJdbcUrl().substring(0, 5), "jdbc:");
//            assertEquals(server.getPort(), URI.create(server.getJdbcUrl().substring(5)).getPort());

//            for (String database : server.getDatabases()) {
//                try (Connection connection = DriverManager.getConnection(server.getJdbcUrl())) {
//                    connection.setCatalog(database);
//                    try (Statement statement = connection.createStatement()) {
//                        statement.execute("CREATE TABLE test_table (c1 bigint PRIMARY KEY)");
//                        statement.execute("INSERT INTO test_table (c1) VALUES (1)");
//                        try (ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM test_table")) {
//                            assertTrue(resultSet.next());
//                            assertEquals(resultSet.getLong(1), 1L);
//                            assertFalse(resultSet.next());
//                        }
        	
     Connection connection = DriverManager.getConnection(server.getJdbcUrl(), server.getUser(), server.getPassword());
        	Statement stmt = connection.createStatement();
        	stmt.execute("DROP TABLE IF EXISTS SAMPLE_TABLE");
        	stmt.execute("CREATE TABLE SAMPLE_TABLE ( ID INTEGER, NAME CHAR(20) )");
        	PreparedStatement pstmt = connection.prepareStatement("INSERT INTO SAMPLE_TABLE VALUES (?, ?)");
            pstmt.setInt(1, 100);
            pstmt.setString(2, "Tom");
            pstmt.executeUpdate();
            pstmt.setInt(1, 200);
            pstmt.setString(2, "Jerry");
            pstmt.executeUpdate();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SAMPLE_TABLE");
            while (rs.next())
            {
                System.out.println("ID = " + rs.getInt(1) + ": " + rs.getString(2));
            }
            rs.close();
            stmt.close();
            pstmt.close();
            connection.close();
        }
     catch (SQLException e) {
         throw e;
     }
    }
}
