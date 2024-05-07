package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JDBCMultiPoolMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private JDBCMultiPoolMBeanImpl bean;

   protected JDBCMultiPoolMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JDBCMultiPoolMBeanImpl)var1;
   }

   public JDBCMultiPoolMBeanBinder() {
      super(new JDBCMultiPoolMBeanImpl());
      this.bean = (JDBCMultiPoolMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ACLName")) {
                  try {
                     this.bean.setACLName((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("AlgorithmType")) {
                  try {
                     this.bean.setAlgorithmType((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("ConnectionPoolFailoverCallbackHandler")) {
                  try {
                     this.bean.setConnectionPoolFailoverCallbackHandler((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("FailoverRequestIfBusy")) {
                  try {
                     this.bean.setFailoverRequestIfBusy(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("HealthCheckFrequencySeconds")) {
                  try {
                     this.bean.setHealthCheckFrequencySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("JDBCSystemResource")) {
                  this.bean.setJDBCSystemResourceAsString((String)var2);
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("PoolList")) {
                  this.bean.setPoolListAsString((String)var2);
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("HighAvail")) {
                  try {
                     this.bean.setHighAvail(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("LoadBalance")) {
                  try {
                     this.bean.setLoadBalance(Boolean.valueOf((String)var2));
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
