package weblogic.t3.srvr.servicegroups;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import weblogic.Home;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.server.ServerLogger;
import weblogic.server.ServiceFailureException;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceDependencies;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ServerServiceDependencies extends ServiceDependencies {
   private static final DebugCategory debugSLC = Debug.getCategory("weblogic.slc");
   private static final DebugLogger debugSLCWLDF = DebugLogger.getDebugLogger("DebugServerLifeCycle");
   private static String serverTypeProp = System.getProperty("serverType");
   private static boolean checkServiceConfig;
   private static String DISABLED_SERVICES;
   private EJBServiceGroup ejbServiceGroup = null;
   private ConnectorServiceGroup connectorServiceGroup = null;
   private JMSServiceGroup jmsServiceGroup = null;
   private ServicePluginGroup servicePluginGroup = null;
   private WseeServiceGroup wseeServiceGroup = null;
   private static ServiceDependencies INSTANCE;

   private ServerServiceDependencies() {
      try {
         this.addServiceGroup(new CoreServiceGroup(true));
         this.servicePluginGroup = new ServicePluginGroup(this.isGroupConfigured(ServicePluginGroup.class.getName()));
         this.addServiceGroup(this.servicePluginGroup);
         this.ejbServiceGroup = new EJBServiceGroup(this.isGroupConfigured(EJBServiceGroup.class.getName()));
         this.addServiceGroup(this.ejbServiceGroup);
         this.connectorServiceGroup = new ConnectorServiceGroup(this.isGroupConfigured(ConnectorServiceGroup.class.getName()));
         this.addServiceGroup(this.connectorServiceGroup);
         this.jmsServiceGroup = new JMSServiceGroup(this.isGroupConfigured(JMSServiceGroup.class.getName()));
         this.addServiceGroup(this.jmsServiceGroup);
         this.wseeServiceGroup = new WseeServiceGroup(this.isGroupConfigured(WseeServiceGroup.class.getName()));
         this.addServiceGroup(this.wseeServiceGroup);
      } catch (ServiceFailureException var2) {
         var2.printStackTrace();
         throw new RuntimeException(var2);
      }
   }

   private boolean isGroupConfigured(String var1) {
      if (!checkServiceConfig) {
         return true;
      } else {
         if (DISABLED_SERVICES == null) {
            DISABLED_SERVICES = getDisabledServices(serverTypeProp);
         }

         if (isDebugEnabled()) {
            debugSLCWLDF.debug("isGroupConfigured( " + var1 + " ) = " + !DISABLED_SERVICES.contains(var1));
         }

         return !DISABLED_SERVICES.contains(var1);
      }
   }

   private static String getDisabledServices(String var0) {
      Object var1 = null;
      String var2 = "";
      String var3 = "";
      Object var4 = null;

      try {
         var2 = Home.getPath() + File.separator + "lib" + File.separator + "service-config.properties";
         File var5 = new File(var2);
         FileInputStream var6 = new FileInputStream(var5);
         Properties var7 = new Properties();
         var7.load(var6);
         if (isDebugEnabled()) {
            StringWriter var8 = new StringWriter();
            var7.list(new PrintWriter(var8));
            debugSLCWLDF.debug(var8.toString());
         }

         var0 = var0.toLowerCase(Locale.ENGLISH);
         String var13 = var7.getProperty(var0 + "-config");
         String var10;
         if (var13 == null) {
            ServerLogger.logUnknownServerType(var0);
         } else {
            for(StringTokenizer var9 = new StringTokenizer(var13, " "); var9.hasMoreTokens(); var3 = var3 + var7.getProperty(var10)) {
               var10 = var9.nextToken();
            }
         }
      } catch (FileNotFoundException var11) {
         ServerLogger.logMissingServiceConfigFile(var2);
      } catch (IOException var12) {
         ServerLogger.logServiceConfigFileException(var2, var12);
      }

      return var3;
   }

   public boolean isServiceAvailable(String var1) {
      if (var1.equals("EJB")) {
         return this.ejbServiceGroup.isAvailable();
      } else if (var1.equals("JMS")) {
         return this.jmsServiceGroup.isAvailable();
      } else if (var1.equals("CONNECTOR")) {
         return this.connectorServiceGroup.isAvailable();
      } else if (var1.equals("WSEE")) {
         return this.wseeServiceGroup.isAvailable();
      } else {
         return this.servicePluginGroup.isAvailable() && this.servicePluginGroup.isPluginAvailable(var1);
      }
   }

   public static ServiceDependencies getInstance() {
      return INSTANCE;
   }

   private static boolean isDebugEnabled() {
      return debugSLC.isEnabled() || debugSLCWLDF.isDebugEnabled();
   }

   public static void main(String[] var0) {
      ServiceDependencies var1 = getInstance();
      List var2 = var1.getOrderedServices();
      System.out.println(" The ordered services: ");
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Service var4 = (Service)var3.next();
         System.out.println(var4.getServiceClassName() + "[" + var4.getIndex() + "]");
      }

   }

   static {
      checkServiceConfig = serverTypeProp != null;
      DISABLED_SERVICES = null;
      INSTANCE = new ServerServiceDependencies();
   }
}
