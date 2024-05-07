package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SingletonServiceMBeanBinder extends SingletonServiceBaseMBeanBinder implements AttributeBinder {
   private SingletonServiceMBeanImpl bean;

   protected SingletonServiceMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SingletonServiceMBeanImpl)var1;
   }

   public SingletonServiceMBeanBinder() {
      super(new SingletonServiceMBeanImpl());
      this.bean = (SingletonServiceMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ClassName")) {
                  try {
                     this.bean.setClassName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("Cluster")) {
                  this.bean.setClusterAsString((String)var2);
               } else if (var1.equals("ConstrainedCandidateServers")) {
                  this.bean.setConstrainedCandidateServersAsString((String)var2);
               } else if (var1.equals("HostingServer")) {
                  this.bean.setHostingServerAsString((String)var2);
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("UserPreferredServer")) {
                  this.bean.setUserPreferredServerAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var7) {
         System.out.println(var7 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var7;
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         if (var9 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var9);
         } else if (var9 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var9.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var9);
         }
      }
   }
}
