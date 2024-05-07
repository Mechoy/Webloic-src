package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WTCtBridgeRedirectMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WTCtBridgeRedirectMBeanImpl bean;

   protected WTCtBridgeRedirectMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WTCtBridgeRedirectMBeanImpl)var1;
   }

   public WTCtBridgeRedirectMBeanBinder() {
      super(new WTCtBridgeRedirectMBeanImpl());
      this.bean = (WTCtBridgeRedirectMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Direction")) {
                  try {
                     this.bean.setDirection((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("MetaDataFile")) {
                  try {
                     this.bean.setMetaDataFile((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ReplyQ")) {
                  try {
                     this.bean.setReplyQ((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("SourceAccessPoint")) {
                  try {
                     this.bean.setSourceAccessPoint((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("SourceName")) {
                  try {
                     this.bean.setSourceName((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("SourceQspace")) {
                  try {
                     this.bean.setSourceQspace((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("TargetAccessPoint")) {
                  try {
                     this.bean.setTargetAccessPoint((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("TargetName")) {
                  try {
                     this.bean.setTargetName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("TargetQspace")) {
                  try {
                     this.bean.setTargetQspace((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("TranslateFML")) {
                  try {
                     this.bean.setTranslateFML((String)var2);
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
