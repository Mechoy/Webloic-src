package weblogic.deploy.api.spi.config.templates;

import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.j2ee.descriptor.wl.AdminObjectGroupBean;
import weblogic.j2ee.descriptor.wl.AdminObjectsBean;
import weblogic.j2ee.descriptor.wl.ApplicationSecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.SecurityBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBeanDConfig;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBeanDConfig;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBeanDConfig;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public class ConfigTemplate {
   private static final boolean debug = Debug.isDebug("config");

   public static boolean requireEjbRefDConfig(DDBean var0, DConfigBean var1) {
      if (var0.getXpath().endsWith("ejb-ref") || var0.getXpath().endsWith("ejb-local-ref")) {
         DDBean[] var2 = var0.getChildBean("ejb-link");
         if (var2 != null) {
            return false;
         }
      }

      return true;
   }

   public static void configureSecurity(WeblogicApplicationBeanDConfig var0) {
      if (debug) {
         Debug.say("configuring ear security");
      }

      DDBean var1 = var0.getDDBean();
      DDBean[] var2 = var1.getChildBean(var0.applyNamespace("security-role/role-name"));
      if (var2 != null) {
         SecurityBean var3 = var0.getSecurity();
         WeblogicApplicationBean var4 = (WeblogicApplicationBean)var0.getDescriptorBean();
         if (var3 == null) {
            if (debug) {
               Debug.say("Creating SecurityBean");
            }

            var3 = var4.createSecurity();
         }

         ApplicationSecurityRoleAssignmentBean[] var5 = var3.getSecurityRoleAssignments();
         ArrayList var6 = new ArrayList();
         if (var5 != null) {
            for(int var7 = 0; var7 < var5.length; ++var7) {
               var6.add(var5[var7].getRoleName());
            }
         }

         for(int var9 = 0; var9 < var2.length; ++var9) {
            String var10 = ConfigHelper.getText(var2[var9]);
            if (debug) {
               Debug.say("Checking role, " + var10);
            }

            if (!var6.contains(var10)) {
               if (debug) {
                  Debug.say("Adding role, " + var10);
               }

               ApplicationSecurityRoleAssignmentBean var8 = var3.createSecurityRoleAssignment();
               var8.setRoleName(var10);
            }
         }

      }
   }

   public static void configureEntityDescriptor(WeblogicEnterpriseBeanBeanDConfig var0) {
      if (debug) {
         Debug.say("configuring ejb");
      }

      WeblogicEnterpriseBeanBean var1 = (WeblogicEnterpriseBeanBean)var0.getDescriptorBean();
      DDBean var2 = var0.getDDBean();
      if (var2.getXpath().endsWith("entity")) {
         DDBean[] var3 = var2.getChildBean(var0.applyNamespace("persistence-type"));
         if (var3 != null) {
            DDBean var4 = var3[0];
            if ("Container".equals(ConfigHelper.getText(var4))) {
               EntityDescriptorBean var5 = var0.getEntityDescriptor();
               if (var5 == null) {
                  var5 = var1.createEntityDescriptor();
               }

               PersistenceBean var6 = var5.getPersistence();
               if (var6 == null) {
                  var6 = var5.createPersistence();
               }

               PersistenceUseBean var7 = var6.getPersistenceUse();
               if (var7 == null) {
                  var7 = var6.createPersistenceUse();
               }

               if (debug) {
                  Debug.say("adding listener");
               }

               ((AbstractDescriptorBean)var7).addPropertyChangeListener(new TypeStorageListener(var0));
               if (var7.getTypeIdentifier() == null) {
                  DDBean[] var8 = var2.getChildBean(var0.applyNamespace("cmp-version"));
                  String var9;
                  if (var8 == null) {
                     var9 = "2.x";
                  } else {
                     var9 = ConfigHelper.getText(var8[0]);
                  }

                  if (var9.startsWith("2.")) {
                     if (var7.getTypeIdentifier() == null) {
                        var7.setTypeIdentifier("WebLogic_CMP_RDBMS");
                     }

                     if (var7.getTypeVersion() == null) {
                        var7.setTypeVersion("6.0");
                     }
                  } else {
                     if (var7.getTypeIdentifier() == null) {
                        var7.setTypeIdentifier("WebLogic_CMP_RDBMS");
                     }

                     if (var7.getTypeVersion() == null) {
                        var7.setTypeVersion("5.1.0");
                     }
                  }
               }
            }
         }
      }

   }

   public static void configureMessageDrivenDescriptor(WeblogicEnterpriseBeanBeanDConfig var0) {
      if (debug) {
         Debug.say("configuring mdb");
      }

      WeblogicEnterpriseBeanBean var1 = (WeblogicEnterpriseBeanBean)var0.getDescriptorBean();
      DDBean var2 = var0.getDDBean();
      if (var2.getXpath().endsWith("message-driven")) {
         DDBean[] var3 = var2.getChildBean(var0.applyNamespace("message-destination-type"));
         if (var3 != null) {
            DDBean var4 = var3[0];
            if ("javax.jms.Queue".equals(ConfigHelper.getText(var4)) || "javax.jms.Topic".equals(ConfigHelper.getText(var4))) {
               MessageDrivenDescriptorBean var5 = var1.getMessageDrivenDescriptor();
               if (var5 == null) {
                  var5 = var1.createMessageDrivenDescriptor();
               }
            }
         }
      }

   }

   public static void configureWeblogicApplication(WeblogicApplicationBeanDConfig var0) {
      if (debug) {
         Debug.say("configuring app");
      }

      WeblogicApplicationBean var1 = (WeblogicApplicationBean)var0.getDescriptorBean();
      WeblogicModuleBean[] var2 = var1.getModules();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            ((AbstractDescriptorBean)var2[var3]).addPropertyChangeListener(new ModuleListener(var0));
         }
      }

   }

   public static void configureAdminObj(WeblogicConnectorBeanDConfig var0) {
      if (debug) {
         Debug.say("Configuring rar admin objects");
      }

      DDBean var1 = var0.getDDBean();
      DDBean[] var2 = var1.getChildBean(var0.applyNamespace("connector/resourceadapter/adminobject/adminobject-interface"));
      if (var2 != null) {
         AdminObjectsBean var3 = var0.getAdminObjects();
         WeblogicConnectorBean var4 = (WeblogicConnectorBean)var0.getDescriptorBean();
         if (var3 == null) {
            if (debug) {
               Debug.say("Creating AdminOjbectsBean");
            }

            var3 = var4.createAdminObjects();
         }

         AdminObjectGroupBean[] var5 = var3.getAdminObjectGroups();
         ArrayList var6 = new ArrayList();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            var6.add(var5[var7].getAdminObjectInterface());
         }

         for(int var9 = 0; var9 < var2.length; ++var9) {
            String var10 = ConfigHelper.getText(var2[var9]);
            if (debug) {
               Debug.say("Checking interface, " + var10);
            }

            if (!var6.contains(var10)) {
               if (debug) {
                  Debug.say("Adding admin group for interface, " + var10);
               }

               AdminObjectGroupBean var8 = var3.createAdminObjectGroup();
               var8.setAdminObjectInterface(var10);
            }
         }

      }
   }
}
