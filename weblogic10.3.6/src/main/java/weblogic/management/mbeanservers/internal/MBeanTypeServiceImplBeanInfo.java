package weblogic.management.mbeanservers.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.internal.mbean.BeanInfoImpl;
import weblogic.management.mbeanservers.MBeanTypeService;

public class MBeanTypeServiceImplBeanInfo extends BeanInfoImpl {
   public static Class INTERFACE_CLASS = MBeanTypeService.class;

   public MBeanTypeServiceImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MBeanTypeServiceImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MBeanTypeServiceImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.internal");
      String var3 = (new String("<p>Provides operations for discovering the attributes and operations of an MBean type that has not yet been instantiated.</p>  <p>The <code>javax.management.ObjectName</code> of this MBean is \"<code>com.bea:Name=MBeanTypeService,Type=weblogic.management.mbeanservers.MBeanTypeService</code>\".</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.MBeanTypeService");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = MBeanTypeService.class.getMethod("getMBeanInfo", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInterface", "The fully-qualified interface name of the MBean. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the MBean info object for the specified interface.</p>  <p>For example,<br/> <code>MBeanServerConnection.invoke(MBeanTypeServiceMBean, \"getMBeanInfo\",</code><br/> <code>      new Object[] { \"weblogic.security.providers.authorization.DefaultAuthorizationProviderMBean\" }</code><br> <code>      new String[] { \"java.lang.String\" });</code> </p> ");
         var2.setValue("role", "operation");
      }

      var3 = MBeanTypeService.class.getMethod("getSubtypes", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInterface", "The fully-qualified interface name of the base MBean. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the names of all MBean types that extend or implement the specified MBean.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MBeanTypeService.class.getMethod("validateAttribute", String.class, Attribute.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInterface", "The fully-qualified interface name of the MBean that contains the attribute. "), createParameterDescriptor("attribute", "The name of the attribute and a proposed value. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("AttributeNotFoundException     if the attribute is not specified for the interface."), BeanInfoHelper.encodeEntities("InvalidAttributeValueException if the value violates any of the constraints"), BeanInfoHelper.encodeEntities("MBeanException                 if the interface is not recognized."), BeanInfoHelper.encodeEntities("ReflectionException            if the attribute type or the bean interface cannot be loaded.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Validates that the specified attribute value complies with the contraints for that attribute.</p> <p>This operation evaluates the following types of constraints:</p> <ul> <li>If the value you specify falls within an allowed minimum or maximum range.</li> <li>If the value you specify is one of a set of enumerated allowed values.</li> <li>If you pass a null value, this operation evaluates whether the attribute is allowed to contain a null value.</li> </ul> <p>There are two signatures for this operation. One takes the interface name of an MBean type and the other takes the <code>javax.management.ObjectName</code> of an MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MBeanTypeService.class.getMethod("validateAttributes", String.class, AttributeList.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInterface", "The fully-qualified interface name of the MBean that contains the attributes. "), createParameterDescriptor("attributes", "The names of the attributes and proposed values. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var7;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("MBeanException                 if the interface is not recognized."), BeanInfoHelper.encodeEntities("ReflectionException            if the attribute type or the bean interface cannot be loaded.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Validates a set of attributes with a single invocation.</p> <p>If all of the attribute values are valid, this operation returns an empty <code>AttributeList</code>. For each invalid attribute value, operation stores an exception the <code>AttributeList</code> that is returned.</p> <p>There are two signatures for this operation. One takes the interface name of an MBean type and the other takes the <code>javax.management.ObjectName</code> of an MBean instance.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("#validateAttribute")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = MBeanTypeService.class.getMethod("validateAttribute", ObjectName.class, Attribute.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInstance", "An MBean instance. "), createParameterDescriptor("attribute", "The name of the attribute and a proposed value. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("AttributeNotFoundException     if the attribute is not specified for the interface."), BeanInfoHelper.encodeEntities("InvalidAttributeValueException if the value violates any of the constraints"), BeanInfoHelper.encodeEntities("MBeanException                 if the interface is not recognized."), BeanInfoHelper.encodeEntities("ReflectionException            if the attribute type or the bean interface cannot be loaded.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Validates that the specified attribute value complies with the contraints for that attribute.</p> <p>This operation evaluates the following types of constraints:</p> <ul> <li>If the value you specify falls within an allowed minimum or maximum range.</li> <li>If the value you specify is one of a set of enumerated allowed values.</li> <li>If you pass a null value, this operation evaluates whether the attribute is allowed to contain a null value.</li> </ul> <p>There are two signatures for this operation. One takes the interface name of an MBean type and the other takes the <code>javax.management.ObjectName</code> of an MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MBeanTypeService.class.getMethod("validateAttributes", ObjectName.class, AttributeList.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("beanInstance", "An MBean instance. "), createParameterDescriptor("attributes", "the names of the attributes and a proposed values. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("MBeanException                 if the interface is not recognized."), BeanInfoHelper.encodeEntities("ReflectionException            if the attribute type or the bean interface cannot be loaded.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Validates a set of attributes with a single invocation.</p> <p>If all of the attribute values are valid, this operation returns an empty <code>AttributeList</code>. For each invalid attribute value, operation stores an exception the <code>AttributeList</code> that is returned.</p> <p>There are two signatures for this operation. One takes the interface name of an MBean type and the other takes the <code>javax.management.ObjectName</code> of an MBean instance.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("#validateAttributes(String beanInterface, AttributeList)")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

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
