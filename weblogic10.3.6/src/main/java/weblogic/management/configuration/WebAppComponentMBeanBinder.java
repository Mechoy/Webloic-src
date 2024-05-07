package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebAppComponentMBeanBinder extends ComponentMBeanBinder implements AttributeBinder {
   private WebAppComponentMBeanImpl bean;

   protected WebAppComponentMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebAppComponentMBeanImpl)var1;
   }

   public WebAppComponentMBeanBinder() {
      super(new WebAppComponentMBeanImpl());
      this.bean = (WebAppComponentMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AuthFilter")) {
                  this.handleDeprecatedProperty("AuthFilter", "8.0.0.0 Use weblogic.xml.");

                  try {
                     this.bean.setAuthFilter((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("AuthRealmName")) {
                  this.handleDeprecatedProperty("AuthRealmName", "8.1.0.0 Use weblogic.xml.");

                  try {
                     this.bean.setAuthRealmName((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("ServletReloadCheckSecs")) {
                  this.handleDeprecatedProperty("ServletReloadCheckSecs", "8.1.0.0 Use weblogic.xml or update using console.");

                  try {
                     this.bean.setServletReloadCheckSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("SingleThreadedServletPoolSize")) {
                  this.handleDeprecatedProperty("SingleThreadedServletPoolSize", "8.1.0.0 Use weblogic.xml or update using console.");

                  try {
                     this.bean.setSingleThreadedServletPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("VirtualHosts")) {
                  this.bean.setVirtualHostsAsString((String)var2);
               } else if (var1.equals("IndexDirectoryEnabled")) {
                  this.handleDeprecatedProperty("IndexDirectoryEnabled", "8.1.0.0 Use weblogic.xml or update using console.");

                  try {
                     this.bean.setIndexDirectoryEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("PreferWebInfClasses")) {
                  this.handleDeprecatedProperty("PreferWebInfClasses", "8.0.0.0 Use weblogic.xml.");

                  try {
                     this.bean.setPreferWebInfClasses(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SessionMonitoringEnabled")) {
                  this.handleDeprecatedProperty("SessionMonitoringEnabled", "8.0.0.0 Use weblogic.xml or update using console.");

                  try {
                     this.bean.setSessionMonitoringEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SessionURLRewritingEnabled")) {
                  this.handleDeprecatedProperty("SessionURLRewritingEnabled", "<unknown>");

                  try {
                     this.bean.setSessionURLRewritingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var14) {
         System.out.println(var14 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (Exception var16) {
         if (var16 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var16);
         } else if (var16 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var16.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var16);
         }
      }
   }
}
