package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebServiceReliabilityMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WebServiceReliabilityMBeanImpl bean;

   protected WebServiceReliabilityMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebServiceReliabilityMBeanImpl)var1;
   }

   public WebServiceReliabilityMBeanBinder() {
      super(new WebServiceReliabilityMBeanImpl());
      this.bean = (WebServiceReliabilityMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AcknowledgementInterval")) {
                  try {
                     this.bean.setAcknowledgementInterval((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("BaseRetransmissionInterval")) {
                  try {
                     this.bean.setBaseRetransmissionInterval((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("InactivityTimeout")) {
                  try {
                     this.bean.setInactivityTimeout((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("SequenceExpiration")) {
                  try {
                     this.bean.setSequenceExpiration((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("NonBufferedDestination")) {
                  try {
                     this.bean.setNonBufferedDestination(new Boolean((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("NonBufferedSource")) {
                  try {
                     this.bean.setNonBufferedSource(new Boolean((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("RetransmissionExponentialBackoff")) {
                  try {
                     this.bean.setRetransmissionExponentialBackoff(new Boolean((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var12) {
         System.out.println(var12 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (Exception var14) {
         if (var14 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var14);
         } else if (var14 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var14.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var14);
         }
      }
   }
}
