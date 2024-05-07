package weblogic.deployment;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kodo.jdbc.conf.descriptor.JDBCConfigurationBeanParser;
import kodo.jdbc.conf.descriptor.PersistenceConfigurationBean;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.lib.conf.AbstractProductDerivation;
import org.apache.openjpa.lib.conf.BootstrapException;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.persistence.PersistenceProductDerivation;
import org.apache.openjpa.util.UserException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class PersistenceConfigurationProductDerivation extends AbstractProductDerivation {
   private static final String PREFIX_KODO = "kodo.";
   private static final String PREFIX_OPENJPA = "openjpa.";

   public int getType() {
      return 1000;
   }

   public boolean beforeConfigurationConstruct(ConfigurationProvider var1) {
      boolean var2 = false;
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      URL var4 = var3.getResource("META-INF/persistence-configuration.xml");
      if (var4 == null) {
         return false;
      } else {
         Object var5 = null;
         Object var6 = null;
         String var7 = "";
         String var8 = null;

         try {
            var8 = var4.toURI().toString();
         } catch (URISyntaxException var25) {
            throw new UserException(var25);
         }

         Map var9 = var1.getProperties();
         boolean var10 = Configurations.containsProperty("Id", var9);
         String var11 = var10 ? Configurations.getProperty("Id", var9).toString() : null;
         org.apache.openjpa.persistence.PersistenceUnitInfoImpl var12 = this.getPersistenceUnit(var11);
         Properties var13 = var12 == null ? new Properties() : var12.getProperties();
         if (var12 != null && !var13.isEmpty()) {
            throw (new BootstrapException(var13.keySet() + " is set " + "before sourcing " + var8)).setFatal(true);
         } else {
            try {
               PersistenceDescriptorLoader var14 = new PersistenceDescriptorLoader(var4, (File)var5, (DeploymentPlanBean)var6, var7, var8);
               JDBCConfigurationBeanParser var15 = new JDBCConfigurationBeanParser();
               DescriptorBean var16 = var14.loadDescriptorBean();
               if (var16 instanceof PersistenceConfigurationBean) {
                  PersistenceConfigurationBean var17 = (PersistenceConfigurationBean)var16;
                  PersistenceUnitConfigurationBean[] var18 = var17.getPersistenceUnitConfigurations();

                  for(int var19 = 0; var19 < var18.length; ++var19) {
                     Properties var20 = null;
                     if (this.matches(var18[var19].getName(), var11)) {
                        var20 = var15.load(var18[var19]);
                        var20.remove("kodo.Name");
                        Iterator var21 = var20.keySet().iterator();

                        while(var21.hasNext()) {
                           String var22 = var21.next().toString();
                           String var23 = var22.substring("kodo.".length());
                           boolean var24 = Configurations.containsProperty(var23, var9);
                           if (!var24) {
                              var1.addProperty(var22, var20.get(var22));
                              var2 = true;
                           }
                        }
                     }
                  }
               }

               return var2;
            } catch (Exception var26) {
               throw new RuntimeException(var26);
            }
         }
      }
   }

   public boolean beforeConfigurationLoad(Configuration var1) {
      if (!(var1 instanceof OpenJPAConfigurationImpl)) {
         return false;
      } else {
         OpenJPAConfigurationImpl var2 = (OpenJPAConfigurationImpl)var1;
         var2.managedRuntimePlugin.setDefault("org.apache.openjpa.ee.WLSManagedRuntime");
         var2.managedRuntimePlugin.setString("org.apache.openjpa.ee.WLSManagedRuntime");
         return true;
      }
   }

   boolean matches(String var1, String var2) {
      return this.isEmpty(var1) && this.isEmpty(var2) || !this.isEmpty(var1) && var1.equals(var2);
   }

   boolean isEmpty(String var1) {
      return var1 == null || var1.trim().length() == 0;
   }

   public void validate() throws Exception {
      Class.forName("weblogic.descriptor.DescriptorBean");
   }

   org.apache.openjpa.persistence.PersistenceUnitInfoImpl getPersistenceUnit(String var1) {
      try {
         Enumeration var2 = this.getClass().getClassLoader().getResources("META-INF/persistence.xml");

         while(var2.hasMoreElements()) {
            PersistenceProductDerivation.ConfigurationParser var3 = new PersistenceProductDerivation.ConfigurationParser(new HashMap());
            var3.parse((URL)var2.nextElement());
            List var4 = var3.getResults();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               org.apache.openjpa.persistence.PersistenceUnitInfoImpl var6 = (org.apache.openjpa.persistence.PersistenceUnitInfoImpl)var5.next();
               if (this.matches(var1, var6.getPersistenceUnitName())) {
                  return var6;
               }
            }
         }
      } catch (IOException var7) {
      }

      return null;
   }

   Map ignoreKnownOrUnrecognizedProperty(Map var1) {
      if (var1 != null && !var1.isEmpty()) {
         HashMap var2 = new HashMap(var1);
         Configurations.removeProperty("Id", var2);
         Configurations.removeProperty("BrokerFactory", var2);
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (!var4.startsWith("kodo.") && !var4.startsWith("openjpa.")) {
               var2.remove(var4);
            }
         }

         return var2;
      } else {
         return var1;
      }
   }
}
