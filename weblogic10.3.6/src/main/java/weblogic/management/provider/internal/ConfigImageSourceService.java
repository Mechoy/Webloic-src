package weblogic.management.provider.internal;

import com.bea.security.utils.random.SecureRandomData;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import weblogic.diagnostics.image.ImageManager;
import weblogic.management.ManagementLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ConfigImageSourceService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static String[] PROTECTED = new String[]{"password", "passphrase", "credential", "pass", "pwd"};
   static final String LINE_SEP = "line.separator";

   public void start() throws ServiceFailureException {
      try {
         this.logSystemProperties();
         ImageManager var1 = ImageManager.getInstance();
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
         ConfigImageSource var3 = new ConfigImageSource(var2);
         var1.registerImageSource("configuration", var3);
      } catch (Throwable var4) {
         throw new ServiceFailureException(var4);
      }
   }

   private void logSystemProperties() {
      this.logEntropyConfiguration();

      try {
         StringBuffer var1 = new StringBuffer();
         String var2 = null;
         var1.append(System.getProperty("line.separator"));
         Iterator var3 = (new TreeSet(System.getProperties().keySet())).iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (!var4.equals("line.separator")) {
               var2 = System.getProperty(var4);
               if (var2 != null && var2.length() != 0) {
                  var1.append(var4);
                  var1.append(" = ");
                  var1.append(this.hideIfProtected(var4, var2));
                  var1.append(System.getProperty("line.separator"));
               }
            }
         }

         ManagementLogger.logJavaSystemProperties(var1.toString());
      } catch (IllegalArgumentException var5) {
         ManagementLogger.logInvalidSystemProperty(var5);
      }

   }

   private void logEntropyConfiguration() {
      ManagementLogger.logJavaEntropyConfig(SecureRandomData.getJavaEntropyConfiguration());
      if (SecureRandomData.isJavaEntropyBlocking()) {
         ManagementLogger.logJavaEntropyConfigIsBlocking();
      } else {
         ManagementLogger.logJavaEntropyConfigIsNonBlocking();
      }

   }

   private String hideIfProtected(String var1, String var2) {
      for(int var3 = 0; var3 < PROTECTED.length; ++var3) {
         if (var1.toLowerCase(Locale.US).indexOf(PROTECTED[var3]) >= 0) {
            return "********";
         }
      }

      return var2;
   }
}
