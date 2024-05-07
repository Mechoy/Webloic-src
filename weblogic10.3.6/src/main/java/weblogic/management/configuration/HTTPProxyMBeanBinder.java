package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class HTTPProxyMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private HTTPProxyMBeanImpl bean;

   protected HTTPProxyMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (HTTPProxyMBeanImpl)var1;
   }

   public HTTPProxyMBeanBinder() {
      super(new HTTPProxyMBeanImpl());
      this.bean = (HTTPProxyMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("HealthCheckInterval")) {
                  try {
                     this.bean.setHealthCheckInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("InitialConnections")) {
                  try {
                     this.bean.setInitialConnections(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("MaxConnections")) {
                  try {
                     this.bean.setMaxConnections(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("MaxHealthCheckInterval")) {
                  try {
                     this.bean.setMaxHealthCheckInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("MaxRetries")) {
                  try {
                     this.bean.setMaxRetries(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("ServerList")) {
                  try {
                     this.bean.setServerList((String)var2);
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
