package weblogic.cluster.messaging.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogServiceImpl implements LogService {
   public static LogServiceImpl getInstance() {
      return LogServiceImpl.Factory.THE_ONE;
   }

   public void debug(String var1) {
      Logger.global.logp(Level.INFO, (String)null, (String)null, var1);
   }

   private static final class Factory {
      static final LogServiceImpl THE_ONE = new LogServiceImpl();
   }
}
