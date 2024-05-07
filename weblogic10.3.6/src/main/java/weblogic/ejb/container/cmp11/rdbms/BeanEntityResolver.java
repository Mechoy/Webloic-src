package weblogic.ejb.container.cmp11.rdbms;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.Debug;
import weblogic.utils.PlatformConstants;
import weblogic.xml.dom.ResourceEntityResolver;

public final class BeanEntityResolver extends ResourceEntityResolver implements PlatformConstants {
   private static final DebugLogger debugLogger;

   public BeanEntityResolver() {
      if (debugLogger.isDebugEnabled()) {
         debug("BeanEntityResolver constructor dtd name is weblogic-rdbms-persistence.dtd");
      }

      if (debugLogger.isDebugEnabled()) {
         debug("My class loader is " + this.getClass().getClassLoader());
      }

      this.addEntityResource("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN", "weblogic-rdbms-persistence.dtd", this.getClass());
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      assert var1 != null;

      assert var2 != null;

      if (debugLogger.isDebugEnabled()) {
         debug("cmp11.rdbms.BeanEntityResolver.resolveIdentity(" + var1 + ", " + var2 + ")");
      }

      if (!var1.equals("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN")) {
         Loggable var4 = EJBLogger.logincorrectDocTypeLoggable(var1, "-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN");
         throw new SAXException(var4.getMessage());
      } else {
         InputSource var3 = super.resolveEntity(var1, var2);
         Debug.assertion(var3 != null);
         return var3;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[BeanEntityResolver] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
