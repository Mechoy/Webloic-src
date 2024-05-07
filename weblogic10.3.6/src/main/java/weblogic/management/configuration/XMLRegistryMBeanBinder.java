package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class XMLRegistryMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private XMLRegistryMBeanImpl bean;

   protected XMLRegistryMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (XMLRegistryMBeanImpl)var1;
   }

   public XMLRegistryMBeanBinder() {
      super(new XMLRegistryMBeanImpl());
      this.bean = (XMLRegistryMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DocumentBuilderFactory")) {
                  try {
                     this.bean.setDocumentBuilderFactory((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("SAXParserFactory")) {
                  try {
                     this.bean.setSAXParserFactory((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("TransformerFactory")) {
                  try {
                     this.bean.setTransformerFactory((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("WhenToCache")) {
                  try {
                     this.bean.setWhenToCache((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("XMLEntitySpecRegistryEntry")) {
                  try {
                     this.bean.addXMLEntitySpecRegistryEntry((XMLEntitySpecRegistryEntryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     this.bean.removeXMLEntitySpecRegistryEntry((XMLEntitySpecRegistryEntryMBean)var7.getExistingBean());
                     this.bean.addXMLEntitySpecRegistryEntry((XMLEntitySpecRegistryEntryMBean)((AbstractDescriptorBean)((XMLEntitySpecRegistryEntryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("XMLParserSelectRegistryEntry")) {
                  try {
                     this.bean.addXMLParserSelectRegistryEntry((XMLParserSelectRegistryEntryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     this.bean.removeXMLParserSelectRegistryEntry((XMLParserSelectRegistryEntryMBean)var6.getExistingBean());
                     this.bean.addXMLParserSelectRegistryEntry((XMLParserSelectRegistryEntryMBean)((AbstractDescriptorBean)((XMLParserSelectRegistryEntryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("HandleEntityInvalidation")) {
                  try {
                     this.bean.setHandleEntityInvalidation(Boolean.valueOf((String)var2));
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
