package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class EJBComponentMBeanBinder extends ComponentMBeanBinder implements AttributeBinder {
   private EJBComponentMBeanImpl bean;

   protected EJBComponentMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (EJBComponentMBeanImpl)var1;
   }

   public EJBComponentMBeanBinder() {
      super(new EJBComponentMBeanImpl());
      this.bean = (EJBComponentMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ExtraEjbcOptions")) {
                  try {
                     this.bean.setExtraEjbcOptions((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ExtraRmicOptions")) {
                  try {
                     this.bean.setExtraRmicOptions((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ForceGeneration")) {
                  try {
                     this.bean.setForceGeneration(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("JavaCompiler")) {
                  try {
                     this.bean.setJavaCompiler((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("JavaCompilerPostClassPath")) {
                  try {
                     this.bean.setJavaCompilerPostClassPath((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("JavaCompilerPreClassPath")) {
                  try {
                     this.bean.setJavaCompilerPreClassPath((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("KeepGenerated")) {
                  try {
                     this.bean.setKeepGenerated(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("TmpPath")) {
                  this.handleDeprecatedProperty("TmpPath", "<unknown>");

                  try {
                     this.bean.setTmpPath((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
                  this.handleDeprecatedProperty("VerboseEJBDeploymentEnabled", "Deprecated as of 10.3.3.0 in favor of");

                  try {
                     this.bean.setVerboseEJBDeploymentEnabled((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var15) {
         System.out.println(var15 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (Exception var17) {
         if (var17 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var17);
         } else if (var17 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var17.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var17);
         }
      }
   }
}
