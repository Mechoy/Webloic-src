package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoImpl;
import weblogic.management.mbeanservers.edit.Change;

public class ChangeImplBeanInfo extends BeanInfoImpl {
   public static Class INTERFACE_CLASS = Change.class;

   public ChangeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ChangeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ChangeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("valueObject", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var3 = (new String("Describes a single change to the configuration. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.Change");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AttributeName")) {
         var3 = "getAttributeName";
         var4 = null;
         var2 = new PropertyDescriptor("AttributeName", Change.class, var3, (String)var4);
         var1.put("AttributeName", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Bean")) {
         var3 = "getBean";
         var4 = null;
         var2 = new PropertyDescriptor("Bean", Change.class, var3, (String)var4);
         var1.put("Bean", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("NewValue")) {
         var3 = "getNewValue";
         var4 = null;
         var2 = new PropertyDescriptor("NewValue", Change.class, var3, (String)var4);
         var1.put("NewValue", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("OldValue")) {
         var3 = "getOldValue";
         var4 = null;
         var2 = new PropertyDescriptor("OldValue", Change.class, var3, (String)var4);
         var1.put("OldValue", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Operation")) {
         var3 = "getOperation";
         var4 = null;
         var2 = new PropertyDescriptor("Operation", Change.class, var3, (String)var4);
         var1.put("Operation", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, "modify");
         var2.setValue("legalValues", new Object[]{"modify", "create", "destroy", "add", "remove", "unset"});
      }

      if (!var1.containsKey("RestartRequired")) {
         var3 = "isRestartRequired";
         var4 = null;
         var2 = new PropertyDescriptor("RestartRequired", Change.class, var3, (String)var4);
         var1.put("RestartRequired", var2);
         var2.setValue("description", " ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
