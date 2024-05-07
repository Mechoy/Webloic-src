package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class KernelDebugMBeanBinder extends DebugMBeanBinder implements AttributeBinder {
   private KernelDebugMBeanImpl bean;

   protected KernelDebugMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (KernelDebugMBeanImpl)var1;
   }

   public KernelDebugMBeanBinder() {
      super(new KernelDebugMBeanImpl());
      this.bean = (KernelDebugMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DebugAbbreviation")) {
                  try {
                     this.bean.setDebugAbbreviation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("DebugConnection")) {
                  try {
                     this.bean.setDebugConnection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("DebugDGCEnrollment")) {
                  try {
                     this.bean.setDebugDGCEnrollment(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("DebugFailOver")) {
                  try {
                     this.bean.setDebugFailOver(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("DebugIIOP")) {
                  this.handleDeprecatedProperty("DebugIIOP", "<unknown>");

                  try {
                     this.bean.setDebugIIOP(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("DebugIIOPConnection")) {
                  try {
                     this.bean.setDebugIIOPConnection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("DebugIIOPMarshal")) {
                  try {
                     this.bean.setDebugIIOPMarshal(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("DebugIIOPOTS")) {
                  try {
                     this.bean.setDebugIIOPOTS(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DebugIIOPReplacer")) {
                  try {
                     this.bean.setDebugIIOPReplacer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("DebugIIOPSecurity")) {
                  try {
                     this.bean.setDebugIIOPSecurity(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("DebugIIOPStartup")) {
                  try {
                     this.bean.setDebugIIOPStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("DebugIIOPTransport")) {
                  try {
                     this.bean.setDebugIIOPTransport(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("DebugLoadBalancing")) {
                  try {
                     this.bean.setDebugLoadBalancing(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("DebugMessaging")) {
                  try {
                     this.bean.setDebugMessaging(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("DebugMuxer")) {
                  try {
                     this.bean.setDebugMuxer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("DebugMuxerConnection")) {
                  try {
                     this.bean.setDebugMuxerConnection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("DebugMuxerDetail")) {
                  try {
                     this.bean.setDebugMuxerDetail(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("DebugMuxerException")) {
                  try {
                     this.bean.setDebugMuxerException(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("DebugMuxerTimeout")) {
                  try {
                     this.bean.setDebugMuxerTimeout(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("DebugRC4")) {
                  try {
                     this.bean.setDebugRC4(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("DebugRSA")) {
                  try {
                     this.bean.setDebugRSA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("DebugRouting")) {
                  try {
                     this.bean.setDebugRouting(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("DebugSSL")) {
                  try {
                     this.bean.setDebugSSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("DebugSelfTuning")) {
                  try {
                     this.bean.setDebugSelfTuning(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("DebugWorkContext")) {
                  try {
                     this.bean.setDebugWorkContext(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("ForceGCEachDGCPeriod")) {
                  try {
                     this.bean.setForceGCEachDGCPeriod(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("LogDGCStatistics")) {
                  try {
                     this.bean.setLogDGCStatistics(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var32) {
         System.out.println(var32 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var32;
      } catch (RuntimeException var33) {
         throw var33;
      } catch (Exception var34) {
         if (var34 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var34);
         } else if (var34 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var34.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var34);
         }
      }
   }
}
