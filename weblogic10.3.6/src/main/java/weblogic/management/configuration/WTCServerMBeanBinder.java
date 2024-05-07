package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WTCServerMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private WTCServerMBeanImpl bean;

   protected WTCServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WTCServerMBeanImpl)var1;
   }

   public WTCServerMBeanBinder() {
      super(new WTCServerMBeanImpl());
      this.bean = (WTCServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("WTCExport")) {
                  try {
                     this.bean.addWTCExport((WTCExportMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     this.bean.removeWTCExport((WTCExportMBean)var12.getExistingBean());
                     this.bean.addWTCExport((WTCExportMBean)((AbstractDescriptorBean)((WTCExportMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WTCImport")) {
                  try {
                     this.bean.addWTCImport((WTCImportMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     this.bean.removeWTCImport((WTCImportMBean)var11.getExistingBean());
                     this.bean.addWTCImport((WTCImportMBean)((AbstractDescriptorBean)((WTCImportMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WTCLocalTuxDom")) {
                  try {
                     this.bean.addWTCLocalTuxDom((WTCLocalTuxDomMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     this.bean.removeWTCLocalTuxDom((WTCLocalTuxDomMBean)var10.getExistingBean());
                     this.bean.addWTCLocalTuxDom((WTCLocalTuxDomMBean)((AbstractDescriptorBean)((WTCLocalTuxDomMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WTCPassword")) {
                  try {
                     this.bean.addWTCPassword((WTCPasswordMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     this.bean.removeWTCPassword((WTCPasswordMBean)var9.getExistingBean());
                     this.bean.addWTCPassword((WTCPasswordMBean)((AbstractDescriptorBean)((WTCPasswordMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WTCRemoteTuxDom")) {
                  try {
                     this.bean.addWTCRemoteTuxDom((WTCRemoteTuxDomMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     this.bean.removeWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var8.getExistingBean());
                     this.bean.addWTCRemoteTuxDom((WTCRemoteTuxDomMBean)((AbstractDescriptorBean)((WTCRemoteTuxDomMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WTCResources")) {
                  try {
                     this.bean.setWTCResources((WTCResourcesMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("WTCtBridgeGlobal")) {
                  try {
                     this.bean.setWTCtBridgeGlobal((WTCtBridgeGlobalMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WTCtBridgeRedirect")) {
                  try {
                     this.bean.addWTCtBridgeRedirect((WTCtBridgeRedirectMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     this.bean.removeWTCtBridgeRedirect((WTCtBridgeRedirectMBean)var5.getExistingBean());
                     this.bean.addWTCtBridgeRedirect((WTCtBridgeRedirectMBean)((AbstractDescriptorBean)((WTCtBridgeRedirectMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
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
