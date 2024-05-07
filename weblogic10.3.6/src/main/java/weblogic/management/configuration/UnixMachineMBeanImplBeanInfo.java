package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class UnixMachineMBeanImplBeanInfo extends MachineMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UnixMachineMBean.class;

   public UnixMachineMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UnixMachineMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UnixMachineMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This bean represents a machine that is running the UNIX operating system. It extends MachineMBean with properties specific to the UNIX platform.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.UnixMachineMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("PostBindGID")) {
         var3 = "getPostBindGID";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostBindGID";
         }

         var2 = new PropertyDescriptor("PostBindGID", UnixMachineMBean.class, var3, var4);
         var1.put("PostBindGID", var2);
         var2.setValue("description", "<p>The UNIX group ID (GID) that a server running on this machine will run under after it has carried out all privileged startup actions. Otherwise, the server will continue to run under the group under which it was started.(Requires that you enable Post-Bind GID.)</p> ");
         setPropertyDescriptorDefault(var2, "nobody");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("PostBindUID")) {
         var3 = "getPostBindUID";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostBindUID";
         }

         var2 = new PropertyDescriptor("PostBindUID", UnixMachineMBean.class, var3, var4);
         var1.put("PostBindUID", var2);
         var2.setValue("description", "<p>The UNIX user ID (UID) that a server running on this machine will run under after it has carried out all privileged startup actions. Otherwise, the server will continue to run under the account under which it was started.(Requires that you enable Post-Bind UID.)</p> ");
         setPropertyDescriptorDefault(var2, "nobody");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("PostBindGIDEnabled")) {
         var3 = "isPostBindGIDEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostBindGIDEnabled";
         }

         var2 = new PropertyDescriptor("PostBindGIDEnabled", UnixMachineMBean.class, var3, var4);
         var1.put("PostBindGIDEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a server running on this machine binds to a UNIX Group ID (GID) after it has carried out all privileged startup actions.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("PostBindUIDEnabled")) {
         var3 = "isPostBindUIDEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostBindUIDEnabled";
         }

         var2 = new PropertyDescriptor("PostBindUIDEnabled", UnixMachineMBean.class, var3, var4);
         var1.put("PostBindUIDEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a server running on this machine binds to a UNIX User ID (UID) after it has carried out all privileged startup actions.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
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
