package weblogic.wsee.ws.init;

import com.bea.xml.XmlException;
import com.sun.java.xml.ns.j2Ee.ParamValueType;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import weblogic.wsee.tools.wseegen.schemas.ConfigDocument;
import weblogic.wsee.tools.wseegen.schemas.ConfigType;
import weblogic.wsee.tools.wseegen.schemas.DeploymentListenersType;
import weblogic.wsee.tools.wseegen.schemas.ListenerType;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.Verbose;

class WsConfigFactory {
   private static final String CONFIG_FILE_NAME = "wsee-config.xml";
   private static final String CONFIG_FILE_RESOURCE = "META-INF/wsee-config.xml";
   private static boolean VERBOSE = Verbose.isVerbose(WsConfigFactory.class);

   WsConfig newInstance() {
      return this.newInstance(findConfigFiles());
   }

   WsConfig newInstance(URL... var1) {
      WsConfig var2 = new WsConfig();
      URL[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         URL var6 = var3[var5];
         this.load(var2, var6);
      }

      return var2;
   }

   private static URL[] findConfigFiles() {
      ArrayList var0 = new ArrayList();
      URL var1 = WsConfigFactory.class.getResource("wsee-config.xml");
      if (var1 == null) {
         throw new IllegalStateException("Cannot find standard configuration");
      } else {
         var0.add(var1);
         ClassLoader var2 = WsConfigFactory.class.getClassLoader();

         try {
            Enumeration var3 = var2.getResources("META-INF/wsee-config.xml");

            while(var3.hasMoreElements()) {
               var0.add(var3.nextElement());
            }
         } catch (IOException var4) {
            throw new IllegalStateException("Error attempting to load META-INF/wsee-config.xml");
         }

         return (URL[])var0.toArray(new URL[var0.size()]);
      }
   }

   private void load(WsConfig var1, URL var2) {
      assert var2 != null : "No url specified";

      if (VERBOSE) {
         Verbose.log((Object)("Loading configuration from " + var2 + "..."));
      }

      try {
         ConfigType var3 = ConfigDocument.Factory.parse(var2).getConfig();
         if (!var3.validate()) {
            throw new IllegalArgumentException("Configuration at " + var2 + " is invalid");
         } else {
            DeploymentListenersType var4 = var3.getDeploymentListeners();
            if (var4 != null) {
               ListenerType[] var5;
               int var6;
               int var7;
               ListenerType var8;
               if (var4.getClient() != null) {
                  var5 = var4.getClient().getListenerArray();
                  var6 = var5.length;

                  for(var7 = 0; var7 < var6; ++var7) {
                     var8 = var5[var7];
                     var1.getDeploymentListeners().addClientListener(getDeploymentListenerConfig(var8));
                  }
               }

               if (var4.getServer() != null) {
                  var5 = var4.getServer().getListenerArray();
                  var6 = var5.length;

                  for(var7 = 0; var7 < var6; ++var7) {
                     var8 = var5[var7];
                     var1.getDeploymentListeners().addServerListener(getDeploymentListenerConfig(var8));
                  }
               }
            }

         }
      } catch (ClassNotFoundException var9) {
         throw new IllegalArgumentException("Unable to process configuration at " + var2, var9);
      } catch (XmlException var10) {
         throw new IllegalArgumentException("Unable to process configuration at " + var2, var10);
      } catch (IOException var11) {
         throw new IllegalArgumentException("Unable to process configuration at " + var2, var11);
      }
   }

   private static WsDeploymentListenerConfig getDeploymentListenerConfig(ListenerType var0) throws ClassNotFoundException {
      if (VERBOSE) {
         Verbose.log((Object)("Loading deployment listener " + var0.getListenerClass().getStringValue() + "..."));
      }

      Class var1 = ClassUtil.loadClass(var0.getListenerClass().getStringValue());
      HashMap var2 = new HashMap();
      ParamValueType[] var3 = var0.getInitParamArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ParamValueType var6 = var3[var5];
         var2.put(var6.getParamName().getStringValue(), var6.getParamValue().getStringValue());
      }

      WsDeploymentListenerConfig var7 = new WsDeploymentListenerConfig(var1, var2);
      return var7;
   }
}
