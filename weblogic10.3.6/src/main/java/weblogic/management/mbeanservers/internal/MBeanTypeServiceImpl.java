package weblogic.management.mbeanservers.internal;

import java.util.Iterator;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.management.jmx.modelmbean.ModelMBeanInfoWrapper;
import weblogic.management.jmx.modelmbean.ModelMBeanInfoWrapperManager;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.mbeanservers.MBeanTypeService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;

public class MBeanTypeServiceImpl implements MBeanTypeService {
   private WLSModelMBeanContext context;
   private BeanInfoAccess access;

   public MBeanTypeServiceImpl(WLSModelMBeanContext var1) {
      this.context = var1;
      this.access = ManagementService.getBeanInfoAccess();
   }

   public ModelMBeanInfo getMBeanInfo(String var1) throws OperationsException {
      ModelMBeanInfoWrapper var2 = ModelMBeanInfoWrapperManager.getModelMBeanInfoForInterface(var1, this.context.isReadOnly(), this.context.getVersion(), this.context.getNameManager());
      return var2.getModelMBeanInfo();
   }

   public String[] getSubtypes(String var1) {
      return this.access.getSubtypes(var1);
   }

   public void validateAttribute(String var1, Attribute var2) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      ModelMBeanInfoWrapper var3;
      try {
         var3 = ModelMBeanInfoWrapperManager.getModelMBeanInfoForInterface(var1, this.context.isReadOnly(), this.context.getVersion(), this.context.getNameManager());
      } catch (OperationsException var5) {
         throw new MBeanException(var5);
      }

      validateOneAttribute(var2, var3);
   }

   public AttributeList validateAttributes(String var1, AttributeList var2) throws MBeanException, ReflectionException {
      ModelMBeanInfoWrapper var3;
      try {
         var3 = ModelMBeanInfoWrapperManager.getModelMBeanInfoForInterface(var1, this.context.isReadOnly(), this.context.getVersion(), this.context.getNameManager());
      } catch (OperationsException var5) {
         throw new MBeanException(var5);
      }

      AttributeList var4 = validateMultipleAttributes(var2, var3);
      return var4;
   }

   public void validateAttribute(ObjectName var1, Attribute var2) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      ModelMBeanInfoWrapper var3 = this.getWrapperForObjectName(var1);
      validateOneAttribute(var2, var3);
   }

   public AttributeList validateAttributes(ObjectName var1, AttributeList var2) throws MBeanException, ReflectionException {
      ModelMBeanInfoWrapper var3 = this.getWrapperForObjectName(var1);
      return validateMultipleAttributes(var2, var3);
   }

   private static void validateOneAttribute(Attribute var0, ModelMBeanInfoWrapper var1) throws AttributeNotFoundException, MBeanException, InvalidAttributeValueException, ReflectionException {
      String var2 = var0.getName();
      Object var3 = var0.getValue();
      ModelMBeanAttributeInfo var4 = var1.getModelMBeanInfo().getAttribute(var2);
      if (var4 == null) {
         throw new AttributeNotFoundException(var2 + " for " + var1.getModelMBeanInfo().getClassName());
      } else {
         try {
            if (var3 != null) {
               Class var5 = Class.forName(var4.getType());
               if (!var3.getClass().isAssignableFrom(var5)) {
                  throw new InvalidAttributeValueException(var3.getClass() + "is not assignable from " + var5);
               }
            }
         } catch (ClassNotFoundException var13) {
            throw new ReflectionException(var13);
         }

         Descriptor var14 = var4.getDescriptor();
         Boolean var6 = (Boolean)var14.getFieldValue("isNullable");
         if (var6 != null && var6) {
            if (var3 == null) {
               return;
            }

            if (var3 == null) {
               throw new InvalidAttributeValueException("Null value is not allowed for " + var2);
            }
         }

         Object var7 = var14.getFieldValue("minValue");
         if (var7 == null || var3 != null && compareTo(var3, var7) >= 0) {
            Object var8 = var14.getFieldValue("maxValue");
            if (var8 != null && (var3 == null || compareTo(var3, var8) > 0)) {
               throw new InvalidAttributeValueException("Value " + var3 + " is greater than " + var8 + " for " + var2);
            } else {
               Object[] var9 = (Object[])((Object[])var14.getFieldValue("legalValues"));
               if (var9 != null) {
                  boolean var10 = true;

                  Object var12;
                  for(int var11 = 0; var10 && var11 < var9.length; var10 = var12.equals(var3)) {
                     var12 = var9[var11++];
                  }

                  if (var10) {
                     throw new InvalidAttributeValueException("Value " + var3 + " is not in " + var9 + " for " + var2);
                  }
               }

            }
         } else {
            throw new InvalidAttributeValueException("Value " + var3 + " is less than " + var7 + " for " + var2);
         }
      }
   }

   private static AttributeList validateMultipleAttributes(AttributeList var0, ModelMBeanInfoWrapper var1) throws MBeanException, ReflectionException {
      AttributeList var2 = new AttributeList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Attribute var4 = (Attribute)var3.next();

         try {
            validateOneAttribute(var4, var1);
         } catch (InvalidAttributeValueException var6) {
            var2.add(new Attribute(var4.getName(), var6));
         } catch (AttributeNotFoundException var7) {
            var2.add(new Attribute(var4.getName(), var7));
         }
      }

      return var2;
   }

   private ModelMBeanInfoWrapper getWrapperForObjectName(ObjectName var1) throws MBeanException {
      try {
         Object var3 = this.context.getNameManager().lookupObject(var1);
         ModelMBeanInfoWrapper var2 = ModelMBeanInfoWrapperManager.getModelMBeanInfoForInstance(var3, this.context.isReadOnly(), this.context.getVersion(), this.context.getNameManager());
         return var2;
      } catch (OperationsException var4) {
         throw new MBeanException(var4);
      }
   }

   private static int compareTo(Object var0, Object var1) {
      if (var1 instanceof Comparable) {
         return ((Comparable)var0).compareTo((Comparable)var1);
      } else {
         throw new AssertionError("Attribute with legalMin or legalMax is not comparable");
      }
   }
}
