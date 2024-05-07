package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class IIOPMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private IIOPMBeanImpl bean;

   protected IIOPMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (IIOPMBeanImpl)var1;
   }

   public IIOPMBeanBinder() {
      super(new IIOPMBeanImpl());
      this.bean = (IIOPMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CompleteMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteMessageTimeout", "<unknown>");

                  try {
                     this.bean.setCompleteMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("DefaultCharCodeset")) {
                  try {
                     this.bean.setDefaultCharCodeset((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("DefaultMinorVersion")) {
                  try {
                     this.bean.setDefaultMinorVersion(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("DefaultWideCharCodeset")) {
                  try {
                     this.bean.setDefaultWideCharCodeset((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("EnableIORServlet")) {
                  try {
                     this.bean.setEnableIORServlet(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("IdleConnectionTimeout")) {
                  this.handleDeprecatedProperty("IdleConnectionTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getIdleConnectionTimeout()}");

                  try {
                     this.bean.setIdleConnectionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("LocationForwardPolicy")) {
                  try {
                     this.bean.setLocationForwardPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("MaxMessageSize")) {
                  this.handleDeprecatedProperty("MaxMessageSize", "<unknown>");

                  try {
                     this.bean.setMaxMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("SystemSecurity")) {
                  try {
                     this.bean.setSystemSecurity((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("TxMechanism")) {
                  try {
                     this.bean.setTxMechanism((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("UseFullRepositoryIdList")) {
                  try {
                     this.bean.setUseFullRepositoryIdList(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("UseJavaSerialization")) {
                  try {
                     this.bean.setUseJavaSerialization(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("UseLocateRequest")) {
                  try {
                     this.bean.setUseLocateRequest(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UseSerialFormatVersion2")) {
                  try {
                     this.bean.setUseSerialFormatVersion2(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("UseStatefulAuthentication")) {
                  try {
                     this.bean.setUseStatefulAuthentication(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var20) {
         System.out.println(var20 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var20;
      } catch (RuntimeException var21) {
         throw var21;
      } catch (Exception var22) {
         if (var22 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var22);
         } else if (var22 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var22.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var22);
         }
      }
   }
}
