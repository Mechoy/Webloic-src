package weblogic.ejb.container.dd.xml;

import java.util.HashMap;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.xml.process.SAXValidationException;

public final class WLDD61Helper {
   private HashMap pStorage = new HashMap();

   public void addPersistenceType(String var1, String var2, String var3) {
      this.pStorage.put(var1 + var2, var3);
   }

   public String getPersistenceStorage(String var1, String var2, String var3) throws SAXValidationException {
      if (!this.pStorage.containsKey(var1 + var2)) {
         Loggable var4 = EJBLogger.logpersistentTypeMissingLoggable(var1, var2, var3);
         throw new SAXValidationException(var4.getMessage());
      } else {
         return (String)this.pStorage.get(var1 + var2);
      }
   }
}
