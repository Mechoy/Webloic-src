package weblogic.console.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.security.AccessController;
import weblogic.Home;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ConsoleRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.protocol.ServerURL;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.http.HttpParsing;

public class ConsoleRuntimeImpl extends RuntimeMBeanDelegate implements ConsoleRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final char SPECIAL = '\\';
   private static final char SEPARATOR = ';';
   private static final char NULL = '0';

   static void initialize() throws ManagementException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         new ConsoleRuntimeImpl(getDomainRuntimeMBean());
      }

   }

   private ConsoleRuntimeImpl(DomainRuntimeMBean var1) throws ManagementException {
      super(var1.getName(), var1);
      var1.setConsoleRuntime(this);
   }

   public boolean isEnabled() {
      File var1 = new File(Home.getPath() + File.separator + "lib" + File.separator + "consoleapp");
      return !var1.exists() ? false : getDomainMBean().isConsoleEnabled();
   }

   public String getHomePageURL() {
      return this.getBaseConsoleURL() + this.getPage("HomePage1");
   }

   public String getSpecificPageURL(String var1, String[] var2) {
      if (var1 != null && var1.length() >= 1) {
         String var3 = this.getBaseConsoleURL() + this.getPage(var1);
         if (var2 != null && var2.length > 1) {
            var3 = var3 + this.getHandle(var2);
         } else {
            var3 = var3 + this.getHandle(this.getObjectNameContext(getDomainObjectName()));
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Page must not be null or empty.");
      }
   }

   public String[] getSpecificPageURLs(String var1, String[][] var2) {
      if (var1 != null && var1.length() >= 1) {
         if (var2 == null) {
            throw new IllegalArgumentException("Contexts must not be null.");
         } else {
            String[] var3 = new String[var2.length];

            for(int var4 = 0; var4 < var2.length; ++var4) {
               try {
                  var3[var4] = this.getSpecificPageURL(var1, var2[var4]);
               } catch (IllegalArgumentException var6) {
                  throw new IllegalArgumentException("Problem with contexts[" + var4 + "]", var6);
               }
            }

            return var3;
         }
      } else {
         throw new IllegalArgumentException("Page must not be null or empty.");
      }
   }

   public String[] getSpecificPageURLs(String[] var1, String[][] var2) {
      if (var1 != null && var2 != null && var1.length == var2.length) {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            try {
               var3[var4] = this.getSpecificPageURL(var1[var4], var2[var4]);
            } catch (IllegalArgumentException var6) {
               throw new IllegalArgumentException("Problem with pages/contexts[" + var4 + "]", var6);
            }
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Pages and contexts must not be null and must be the same length.");
      }
   }

   public String getDefaultPageURL(String[] var1, String var2) {
      if (var1 != null && var1.length >= 1) {
         String var3 = this.getBaseConsoleURL() + this.getPage("DispatcherPage") + this.getHandle(var1);
         if (var2 != null && var2.length() > 0) {
            var3 = var3 + "&perspective=" + var2;
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Context must not be null or empty.");
      }
   }

   public String[] getDefaultPageURLs(String[][] var1, String var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Contexts must not be null.");
      } else {
         String[] var3 = new String[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            try {
               var3[var4] = this.getDefaultPageURL(var1[var4], var2);
            } catch (IllegalArgumentException var6) {
               throw new IllegalArgumentException("Problem with contexts[" + var4 + "]", var6);
            }
         }

         return var3;
      }
   }

   public String[] getDefaultPageURLs(String[][] var1, String[] var2) {
      if (var1 != null && var2 != null && var1.length == var2.length) {
         String[] var3 = new String[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            try {
               var3[var4] = this.getDefaultPageURL(var1[var4], var2[var4]);
            } catch (IllegalArgumentException var6) {
               throw new IllegalArgumentException("Problem with contexts/perspectives[" + var4 + "]", var6);
            }
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Contexts and perspectives must not be null and must be the same length.");
      }
   }

   public String[] getObjectNameContext(String var1) {
      if (var1 != null && var1.length() >= 1) {
         return new String[]{"com.bea.console.handles.JMXHandle", var1};
      } else {
         throw new IllegalArgumentException("ObjectName must not be null or empty.");
      }
   }

   private String getPage(String var1) {
      return "?_nfpb=true&_pageLabel=" + var1;
   }

   private String getHandle(String[] var1) {
      if (var1 != null && var1.length >= 1) {
         StringBuffer var2 = new StringBuffer();
         var2.append(var1[0]);
         var2.append("(\"");

         for(int var3 = 1; var3 < var1.length; ++var3) {
            if (var3 > 1) {
               var2.append(";");
            }

            String var4 = var1[var3];
            if (var4 == null) {
               var2.append('\\');
               var2.append('0');
            } else {
               for(int var5 = 0; var5 < var4.length(); ++var5) {
                  char var6 = var4.charAt(var5);
                  if (var6 == '\\') {
                     var2.append('\\');
                     var2.append('\\');
                  } else if (var6 == ';') {
                     var2.append('\\');
                     var2.append(';');
                  } else {
                     var2.append(var6);
                  }
               }
            }
         }

         var2.append("\")");
         return "&handle=" + HttpParsing.escape(var2.toString(), "UTF-8");
      } else {
         throw new IllegalArgumentException("context must include at least one string : received " + this.getContextString(var1));
      }
   }

   private String getBaseConsoleURL() {
      try {
         ServerURL var1 = new ServerURL(ChannelHelper.getLocalAdministrationURL());
         String var2 = var1.getProtocol();
         String var3 = !"https".equalsIgnoreCase(var2) && !"t3s".equalsIgnoreCase(var2) && !"admin".equalsIgnoreCase(var2) ? "http" : "https";
         return var3 + "://" + var1.getHost() + ":" + var1.getPort() + "/" + getDomainMBean().getConsoleContextPath() + "/" + "console.portal";
      } catch (MalformedURLException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static DomainRuntimeMBean getDomainRuntimeMBean() {
      return ManagementService.getDomainAccess(kernelId).getDomainRuntime();
   }

   private static DomainMBean getDomainMBean() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain();
   }

   private static String getDomainObjectName() {
      return "com.bea:Name=" + ManagementService.getRuntimeAccess(kernelId).getDomainName() + ",Type=Domain";
   }

   private String getContextString(String[] var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append("[");

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 > 0) {
               var2.append(",");
            }

            var2.append(var1[var3]);
         }

         var2.append("]");
         return var2.toString();
      }
   }
}
