package weblogic.nodemanager.server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import weblogic.nodemanager.common.ConfigException;

public interface ServerManagerI {
   void start(Properties var1) throws IllegalStateException, InterruptedException, ConfigException, IOException;

   String getState() throws IOException;

   void kill() throws IOException, InterruptedException;

   ServerDir getServerDir();

   DomainManager getDomainManager();

   String getServerName();

   void log(Level var1, String var2, Throwable var3);
}
