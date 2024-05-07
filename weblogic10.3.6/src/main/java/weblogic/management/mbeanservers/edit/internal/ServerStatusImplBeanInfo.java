package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.internal.mbean.BeanInfoImpl;
import weblogic.management.mbeanservers.edit.ServerStatus;

public class ServerStatusImplBeanInfo extends BeanInfoImpl {
   public static Class INTERFACE_CLASS = ServerStatus.class;

   public ServerStatusImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerStatusImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerStatusImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("valueObject", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var3 = (new String("Contains the name and state for a target server in a Configuration Manager deployment request. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.ServerStatus");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ServerException")) {
         var3 = "getServerException";
         var4 = null;
         var2 = new PropertyDescriptor("ServerException", ServerStatus.class, var3, (String)var4);
         var1.put("ServerException", var2);
         var2.setValue("description", "<p>Returns the exception that describes why the activation has failed on this server.</p> ");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", ServerStatus.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "<p>Returns the name of the server.</p> ");
      }

      if (!var1.containsKey("ServerState")) {
         var3 = "getServerState";
         var4 = null;
         var2 = new PropertyDescriptor("ServerState", ServerStatus.class, var3, (String)var4);
         var1.put("ServerState", var2);
         var2.setValue("description", " ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("ActivationTaskMBean")};
         var2.setValue("see", var5);
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
