package weblogic.xml.registry;

import weblogic.logging.LogOutputStream;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.xml.XMLLogger;

public class XMLService extends AbstractServerService {
   private static final boolean debug = false;
   private static final boolean verbose = true;
   private static LogOutputStream log;

   public String getVersion() {
      return "XML 1.1";
   }

   public void start() throws ServiceFailureException {
      try {
         XMLRegistry.init();
         XMLLogger.logIntializingXMLRegistry();
      } catch (XMLRegistryException var2) {
         throw new ServiceFailureException("Failed to initialize XMLRegistry: ", var2);
      }
   }

   public static void main(String[] var0) {
   }
}
