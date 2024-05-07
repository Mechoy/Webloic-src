package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class MigratableTargetMBeanBinder extends SingletonServiceBaseMBeanBinder implements AttributeBinder {
   private MigratableTargetMBeanImpl bean;

   protected MigratableTargetMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (MigratableTargetMBeanImpl)var1;
   }

   public MigratableTargetMBeanBinder() {
      super(new MigratableTargetMBeanImpl());
      this.bean = (MigratableTargetMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Cluster")) {
                  this.bean.setClusterAsString((String)var2);
               } else if (var1.equals("ConstrainedCandidateServers")) {
                  this.bean.setConstrainedCandidateServersAsString((String)var2);
               } else if (var1.equals("DestinationServer")) {
                  this.bean.setDestinationServerAsString((String)var2);
               } else if (var1.equals("HostingServer")) {
                  this.bean.setHostingServerAsString((String)var2);
               } else if (var1.equals("MigrationPolicy")) {
                  try {
                     this.bean.setMigrationPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("NumberOfRestartAttempts")) {
                  try {
                     this.bean.setNumberOfRestartAttempts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("PostScript")) {
                  try {
                     this.bean.setPostScript((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("PreScript")) {
                  try {
                     this.bean.setPreScript((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("RestartOnFailure")) {
                  try {
                     this.bean.setRestartOnFailure(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("SecondsBetweenRestarts")) {
                  try {
                     this.bean.setSecondsBetweenRestarts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UserPreferredServer")) {
                  this.bean.setUserPreferredServerAsString((String)var2);
               } else if (var1.equals("NonLocalPostAllowed")) {
                  try {
                     this.bean.setNonLocalPostAllowed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("PostScriptFailureFatal")) {
                  try {
                     this.bean.setPostScriptFailureFatal(Boolean.valueOf((String)var2));
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
