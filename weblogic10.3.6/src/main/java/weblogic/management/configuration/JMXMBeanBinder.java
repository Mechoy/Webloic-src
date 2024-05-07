package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMXMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private JMXMBeanImpl bean;

   protected JMXMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMXMBeanImpl)var1;
   }

   public JMXMBeanBinder() {
      super(new JMXMBeanImpl());
      this.bean = (JMXMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("InvocationTimeoutSeconds")) {
                  try {
                     this.bean.setInvocationTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("CompatibilityMBeanServerEnabled")) {
                  try {
                     this.bean.setCompatibilityMBeanServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("DomainMBeanServerEnabled")) {
                  try {
                     this.bean.setDomainMBeanServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("EditMBeanServerEnabled")) {
                  try {
                     this.bean.setEditMBeanServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ManagementEJBEnabled")) {
                  try {
                     this.bean.setManagementEJBEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("PlatformMBeanServerEnabled")) {
                  try {
                     this.bean.setPlatformMBeanServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("PlatformMBeanServerUsed")) {
                  try {
                     this.bean.setPlatformMBeanServerUsed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("RuntimeMBeanServerEnabled")) {
                  try {
                     this.bean.setRuntimeMBeanServerEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var13) {
         System.out.println(var13 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (Exception var15) {
         if (var15 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var15);
         } else if (var15 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var15.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var15);
         }
      }
   }
}
