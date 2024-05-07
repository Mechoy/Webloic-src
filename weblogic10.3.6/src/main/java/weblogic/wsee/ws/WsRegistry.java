package weblogic.wsee.ws;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.http.HttpRequestParser;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.server.jms.JmsDeploymentListener;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.init.WsDeploymentException;

public final class WsRegistry {
   private static final boolean verbose = Verbose.isVerbose(WsRegistry.class);
   private static final String DELIMITER = "#";
   private static final String nullVersion = "NONE";
   private Map portList = new HashMap();
   private Map<String, ArrayList> uriVersionMap = new HashMap();
   private List listeners;
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static WsRegistry registry = new WsRegistry();

   private WsRegistry() {
   }

   public static WsRegistry instance() {
      return registry;
   }

   private String getKey(String var1, String var2) {
      if (var2 == null) {
         return var1;
      } else {
         StringBuffer var3 = new StringBuffer(var1);
         var3.append("#").append(var2);
         return var3.toString();
      }
   }

   public static String getURL(String var0) {
      int var2 = var0.indexOf("#");
      String var1;
      if (var2 >= 0) {
         var1 = var0.substring(0, var2);
      } else {
         var1 = var0;
      }

      return var1;
   }

   public static String getVersion(String var0) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.indexOf("#");
         String var1;
         if (var2 >= 0) {
            var1 = var0.substring(var2 + 1);
            if (var1.length() == 0) {
               return null;
            }
         } else {
            var1 = null;
         }

         return var1;
      } else {
         return null;
      }
   }

   public WsPort lookup(String var1) {
      String var2 = getVersion(var1);
      String var3 = getURL(var1);
      if (var2 == null) {
         ArrayList var4 = (ArrayList)this.uriVersionMap.get(var3);
         if (var4 != null && var4.size() > 0) {
            if (verbose) {
               Verbose.log((Object)("Deployed versions present for " + var1));
            }

            var2 = (String)var4.get(var4.size() - 1);
            if (verbose) {
               Verbose.log((Object)("Picking last version " + var2));
            }
         } else if (verbose) {
            Verbose.log((Object)("No deployed versions for " + var1));
         }
      } else if (verbose) {
         Verbose.log((Object)("Looking up specific version " + var2 + " url " + var1));
      }

      return var2 != null ? this.lookup(var3, var2) : this.lookup(var3, (String)null);
   }

   public WsPort lookup(String var1, String var2) {
      String var3 = this.getKey(var1, var2);
      WsPort var4 = (WsPort)this.portList.get(var3);
      String var6;
      String var7;
      String var8;
      if (var4 == null) {
         int var5 = var1.indexOf("/", 1);
         if (var5 > 0) {
            var6 = var1.substring(0, var5);
            var7 = var1.substring(var5);
         } else {
            var6 = var1;
            var7 = "/";
         }

         var8 = HttpParsing.unescape(var6, HttpRequestParser.getURIDecodeEncoding());
         String var9 = AsyncUtil.calculateServiceTargetURI(var8, var7);
         var4 = (WsPort)this.portList.get(this.getKey(var9, var2));
         if (verbose) {
            Verbose.log((Object)("lookup port failed with url:" + var1 + " use decoded url:" + var9 + " to lookup, result port = " + var4));
         }
      }

      if (verbose) {
         Verbose.log((Object)("lookup: url " + var1 + " version " + (var2 == null ? "NONE" : var2) + "port = " + var4));
      }

      if (var4 == null && verbose) {
         (new Exception("Port lookup failure from")).printStackTrace();
         Verbose.log((Object)"----------All the current ports-------------");
         Iterator var10 = this.portList.keySet().iterator();

         while(var10.hasNext()) {
            var6 = (String)var10.next();
            var7 = getURL(var6);
            var8 = getVersion(var6);
            WsPort var11 = (WsPort)this.portList.get(var6);
            Verbose.log((Object)("\nURL: " + var7 + "\n Port " + var11 + "\nVersion " + var8));
         }

         Verbose.log((Object)"----------All the current ports-------------");
      }

      return var4;
   }

   public void register(String var1, WsPort var2) {
      this.register(var1, (String)null, var2);
   }

   public void register(String var1, String var2, WsPort var3) {
      String var4 = this.getKey(var1, var2);
      if (verbose) {
         Verbose.log((Object)("New Web Service registered at " + var1 + "version = " + getVersion(var4)));
      }

      this.callListeners(var1, var2, var3);
      this.portList.put(var4, var3);
      if (var2 != null) {
         ArrayList var5 = (ArrayList)this.uriVersionMap.get(var1);
         if (var5 == null) {
            if (verbose) {
               Verbose.log((Object)("Creating new version deployment list for " + var1));
            }

            var5 = new ArrayList(2);
            this.uriVersionMap.put(var1, var5);
         }

         if (verbose) {
            Verbose.log((Object)("Adding version " + var2 + " to deployed versions for " + var1));
         }

         var5.add(var2);
      }

   }

   public void unregister(String var1) {
      this.unregister(var1, (String)null);
   }

   public void unregister(String var1, String var2) {
      String var3 = this.getKey(var1, var2);
      if (verbose) {
         Verbose.log((Object)("Webservice with url " + var1 + " is unregistered."));
      }

      WsPort var4 = (WsPort)this.portList.remove(var3);
      if (var4 != null) {
         unregisterMBean(var4);

         try {
            JmsDeploymentListener.removeJmsListener(var4);
         } catch (WsDeploymentException var6) {
            Verbose.log((Object)var6);
         }
      }

      if (var2 != null) {
         ArrayList var5 = (ArrayList)this.uriVersionMap.get(var1);
         if (var5 != null) {
            var5.remove(var2);
            if (var5.isEmpty()) {
               this.uriVersionMap.remove(var1);
            }
         }
      }

   }

   public void unregisterMBean(String var1) {
      this.unregisterMBean(var1, (String)null);
   }

   public void unregisterMBean(String var1, String var2) {
      String var3 = this.getKey(var1, var2);
      WsPort var4 = (WsPort)this.portList.get(var3);
      if (var4 != null) {
         unregisterMBean(var4);
      }

      if (var2 != null) {
         ArrayList var5 = (ArrayList)this.uriVersionMap.get(var1);
         if (var5 != null) {
            var5.remove(var2);
            if (var5.isEmpty()) {
               this.uriVersionMap.remove(var1);
            }
         }
      }

   }

   public static void unregisterMBean(WsPort var0) {
      if (var0 != null && var0.getRuntimeMBean() != null) {
         final RuntimeMBeanDelegate var1 = (RuntimeMBeanDelegate)var0.getRuntimeMBean().getParent();
         if (var1 != null) {
            SecurityServiceManager.runAs(kernelID, kernelID, new PrivilegedAction() {
               public Object run() {
                  try {
                     var1.unregister();
                  } catch (ManagementException var2) {
                     Verbose.log((Object)var2);
                  }

                  return null;
               }
            });
            if (verbose) {
               Verbose.log((Object)("Webservice mbean " + var1.getName() + " unregistered."));
            }

         }
      }
   }

   public void addListener(WsRegistrationListener var1) {
      assert var1 != null;

      if (this.listeners == null) {
         this.listeners = new ArrayList();
      }

      this.listeners.add(var1);
   }

   public void removeListener(WsRegistrationListener var1) {
      if (this.listeners != null) {
         this.listeners.remove(var1);
      }

   }

   private void callListeners(String var1, String var2, WsPort var3) {
      if (this.listeners != null) {
         Iterator var4 = this.listeners.iterator();

         while(var4.hasNext()) {
            WsRegistrationListener var5 = (WsRegistrationListener)var4.next();
            var5.onRegistration(var1, var2, var3);
         }
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
