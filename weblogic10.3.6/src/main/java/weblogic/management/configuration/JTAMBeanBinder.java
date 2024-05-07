package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JTAMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private JTAMBeanImpl bean;

   protected JTAMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JTAMBeanImpl)var1;
   }

   public JTAMBeanBinder() {
      super(new JTAMBeanImpl());
      this.bean = (JTAMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AbandonTimeoutSeconds")) {
                  try {
                     this.bean.setAbandonTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("BeforeCompletionIterationLimit")) {
                  try {
                     this.bean.setBeforeCompletionIterationLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("CheckpointIntervalSeconds")) {
                  try {
                     this.bean.setCheckpointIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("CompletionTimeoutSeconds")) {
                  try {
                     this.bean.setCompletionTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("ForgetHeuristics")) {
                  try {
                     this.bean.setForgetHeuristics(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("MaxResourceRequestsOnServer")) {
                  try {
                     this.bean.setMaxResourceRequestsOnServer(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("MaxResourceUnavailableMillis")) {
                  try {
                     this.bean.setMaxResourceUnavailableMillis(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("MaxTransactions")) {
                  try {
                     this.bean.setMaxTransactions(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("MaxTransactionsHealthIntervalMillis")) {
                  try {
                     this.bean.setMaxTransactionsHealthIntervalMillis(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("MaxUniqueNameStatistics")) {
                  try {
                     this.bean.setMaxUniqueNameStatistics(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("MaxXACallMillis")) {
                  try {
                     this.bean.setMaxXACallMillis(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("MigrationCheckpointIntervalSeconds")) {
                  try {
                     this.bean.setMigrationCheckpointIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("ParallelXADispatchPolicy")) {
                  try {
                     this.bean.setParallelXADispatchPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("ParallelXAEnabled")) {
                  try {
                     this.bean.setParallelXAEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("PurgeResourceFromCheckpointIntervalSeconds")) {
                  try {
                     this.bean.setPurgeResourceFromCheckpointIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("RecoveryThresholdMillis")) {
                  this.handleDeprecatedProperty("RecoveryThresholdMillis", "7.0.0.0 Replaced with nothing.");

                  try {
                     this.bean.setRecoveryThresholdMillis(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("SecurityInteropMode")) {
                  try {
                     this.bean.setSecurityInteropMode((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("SerializeEnlistmentsGCIntervalMillis")) {
                  try {
                     this.bean.setSerializeEnlistmentsGCIntervalMillis(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("TimeoutSeconds")) {
                  try {
                     this.bean.setTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("UnregisterResourceGracePeriod")) {
                  try {
                     this.bean.setUnregisterResourceGracePeriod(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("WSATTransportSecurityMode")) {
                  try {
                     this.bean.setWSATTransportSecurityMode((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("TwoPhaseEnabled")) {
                  try {
                     this.bean.setTwoPhaseEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WSATIssuedTokenEnabled")) {
                  try {
                     this.bean.setWSATIssuedTokenEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var28) {
         System.out.println(var28 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var28;
      } catch (RuntimeException var29) {
         throw var29;
      } catch (Exception var30) {
         if (var30 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var30);
         } else if (var30 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var30.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var30);
         }
      }
   }
}
