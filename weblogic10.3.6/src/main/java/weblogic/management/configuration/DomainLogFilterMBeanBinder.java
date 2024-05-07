package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class DomainLogFilterMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private DomainLogFilterMBeanImpl bean;

   protected DomainLogFilterMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (DomainLogFilterMBeanImpl)var1;
   }

   public DomainLogFilterMBeanBinder() {
      super(new DomainLogFilterMBeanImpl());
      this.bean = (DomainLogFilterMBeanImpl)this.getBean();
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
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("SeverityLevel")) {
                  this.handleDeprecatedProperty("SeverityLevel", "This attibute is deprecated in favor of LogMBean's DomainLogBroadcastSeverity attribute.");

                  try {
                     this.bean.setSeverityLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SubsystemNames")) {
                  this.handleDeprecatedProperty("SubsystemNames", "<unknown>");

                  try {
                     this.bean.setSubsystemNames(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("UserIds")) {
                  this.handleDeprecatedProperty("UserIds", "<unknown>");

                  try {
                     this.bean.setUserIds(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var9) {
         System.out.println(var9 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Exception var11) {
         if (var11 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var11);
         } else if (var11 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var11.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var11);
         }
      }
   }
}
