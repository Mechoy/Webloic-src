package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SNMPProxyMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SNMPProxyMBeanImpl bean;

   protected SNMPProxyMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SNMPProxyMBeanImpl)var1;
   }

   public SNMPProxyMBeanBinder() {
      super(new SNMPProxyMBeanImpl());
      this.bean = (SNMPProxyMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Community")) {
                  try {
                     this.bean.setCommunity((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("OidRoot")) {
                  try {
                     this.bean.setOidRoot((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Port")) {
                  try {
                     this.bean.setPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("SecurityLevel")) {
                  try {
                     this.bean.setSecurityLevel((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SecurityName")) {
                  try {
                     this.bean.setSecurityName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Timeout")) {
                  try {
                     this.bean.setTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var11) {
         System.out.println(var11 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Exception var13) {
         if (var13 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var13);
         } else if (var13 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var13.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var13);
         }
      }
   }
}
