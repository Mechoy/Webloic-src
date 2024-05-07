package weblogic.diagnostics.debug;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

public class ServerDebugService extends KernelDebugService implements PropertyChangeListener, DebugScopeUtil {
   private static ServerDebugService singleton = null;
   private static final boolean DEBUG = false;
   private ServerDebugMBean serverDebug = null;
   private DebugScopeTree debugScopeTree = null;
   private boolean initialized = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static ServerDebugService getInstance() {
      if (singleton == null) {
         singleton = new ServerDebugService();
      }

      return singleton;
   }

   private ServerDebugService() {
      try {
         this.debugScopeTree = DebugScopeTree.initializeFromPersistence();
      } catch (RuntimeException var2) {
         throw var2;
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public synchronized String[] getChildDebugScopes(String var1) throws InvalidDebugScopeException {
      DebugScopeNode var2 = this.debugScopeTree.findDebugScopeNode(var1);
      Iterator var3 = var2.getChildDebugScopeNodes().iterator();
      ArrayList var4 = new ArrayList();

      while(var3.hasNext()) {
         String var5 = ((DebugScopeNode)var3.next()).getNodeName();
         var4.add(var5);
      }

      String[] var6 = new String[var4.size()];
      var4.toArray(var6);
      return var6;
   }

   public synchronized String[] getChildDebugAttributes(String var1) throws InvalidDebugScopeException {
      DebugScopeNode var2 = this.debugScopeTree.findDebugScopeNode(var1);
      Iterator var3 = var2.getDebugAttributes().iterator();
      ArrayList var4 = new ArrayList();

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         var4.add(var5);
      }

      String[] var6 = new String[var4.size()];
      var4.toArray(var6);
      return var6;
   }

   public void initializeServerDebug(Logger var1) throws DebugProviderRegistrationException {
      if (!this.initialized) {
         this.serverDebug = ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug();
         ServerDebugProvider var2 = new ServerDebugProvider();
         DebugProviderRegistration.registerDebugProvider(var2, DebugLogger.getDefaultDebugLoggerRepository());
         this.serverDebug.addPropertyChangeListener(this);
         this.initDebugContextMode();
         DebugLogger.setDebugContext(new DebugContextImpl());
         this.initializeDebugLogging(var1);
         this.initializeDebugLoggersFromOldDebugCategoryCommandLineProperties();
         this.initialized = true;
      }
   }

   public void initializeDebugLogging(Logger var1) {
   }

   private void initDebugContextMode() {
      if (this.serverDebug.getDiagnosticContextDebugMode().equals("And")) {
         DebugLogger.setContextMode(1);
      } else if (this.serverDebug.getDiagnosticContextDebugMode().equals("Or")) {
         DebugLogger.setContextMode(2);
      } else {
         DebugLogger.setContextMode(0);
      }

      DebugLogger.setDebugMask(DiagnosticContextHelper.parseDyeMask(this.serverDebug.getDebugMaskCriterias()));
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = ((WebLogicMBean)var1.getSource()).getName();
      String var3 = ((WebLogicMBean)var1.getSource()).getType();
      this.attributeValueChanged(var2, var3, var1.getPropertyName(), var1.getOldValue(), var1.getNewValue());
   }

   private void attributeValueChanged(String var1, String var2, String var3, Object var4, Object var5) {
      if (var3.equals("DiagnosticContextDebugMode")) {
         this.initDebugContextMode();
      } else if (var3.equals("DebugMaskCriterias")) {
         this.initDebugContextMode();
      }

   }

   private void initializeDebugLoggersFromOldDebugCategoryCommandLineProperties() {
      try {
         Properties var1 = new Properties();
         var1.load(ServerDebugService.class.getResourceAsStream("OldDebugCategory.props"));
         Set var2 = var1.keySet();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String var5 = var1.getProperty(var4);
            StringTokenizer var6 = new StringTokenizer(var5, ",");

            while(var6.hasMoreTokens()) {
               String var7 = var6.nextToken();
               if (Debug.getCategory(var7).isEnabled()) {
                  DebugLogger.getDebugLogger(var4).setDebugEnabled(true);
               }
            }
         }
      } catch (Exception var8) {
         System.err.println("Error initializing from debug category properties");
      }

   }

   void testDebugScopeName(String var1) throws InvalidDebugScopeException {
      this.debugScopeTree.findDebugScopeNode(var1);
   }
}
