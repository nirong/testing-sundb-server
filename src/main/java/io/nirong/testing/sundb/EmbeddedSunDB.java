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

/*
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import io.airlift.command.Command;
import io.airlift.command.CommandFailedException;
import io.airlift.log.Logger;
import io.airlift.units.Duration;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.StandardSystemProperty.OS_ARCH;
import static com.google.common.base.StandardSystemProperty.OS_NAME;
import static com.google.common.collect.Lists.newArrayList;
import static io.airlift.concurrent.Threads.daemonThreadsNamed;
import static io.airlift.testing.FileUtils.deleteRecursively;
import static java.lang.String.format;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createTempDirectory;
import static java.util.concurrent.Executors.newCachedThreadPool;
*/

// forked from https://github.com/opentable/otj-pg-embedded
final class EmbeddedSunDB
{
    private static final String JDBC_FORMAT = "jdbc:sundb://localhost:%s/test";

    public EmbeddedSunDB()
    {
    }

    public synchronized void start(String threadName) {
    }
    
    public String getJdbcUrl(String userName, String dbName)
    {
        return JDBC_FORMAT.toString();
    }
    public boolean isRunning() {
        return true;
    }
    public void shutdown() {
    }    
    public boolean isReadyForConnections()
    {
        return true;
    }
    public String getVersion()
    {
        return "new";
    }
}

