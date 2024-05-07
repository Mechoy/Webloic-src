package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFHarvestedTypeBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFHarvestedTypeBean.class;

   public WLDFHarvestedTypeBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFHarvestedTypeBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFHarvestedTypeBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Defines the set of types (beans) that are harvested. The WLDF framework allows the harvesting of all designated server-local Weblogic Server runtime MBeans, and most customer MBeans that are registered in the local server's runtime MBean server. Configuration MBeans cannot be harvested.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("HarvestedAttributes")) {
         var3 = "getHarvestedAttributes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHarvestedAttributes";
         }

         var2 = new PropertyDescriptor("HarvestedAttributes", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("HarvestedAttributes", var2);
         var2.setValue("description", "<p>The harvested attributes for this type. If a list of attributes is provided, only those attributes are harvested; otherwise all harvestable attributes are harvested.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HarvestedInstances")) {
         var3 = "getHarvestedInstances";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHarvestedInstances";
         }

         var2 = new PropertyDescriptor("HarvestedInstances", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("HarvestedInstances", var2);
         var2.setValue("description", "<p>The harvested instances of this type.</p>  <p>The configuration of a type can optionally provide a set of identifiers for specific instances. If this list is provided, only the provided instances are harvested; otherwise all instances of the type are harvested.</p>  <p>The identifier for an instance must be a valid JMX ObjectName or an ObjectName pattern.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The type name. For WebLogic Server runtime MBeans, the type name is the fully qualified name of the defining interface. For customer MBeans, the type name is the fully qualified MBean implementation class.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("Namespace")) {
         var3 = "getNamespace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNamespace";
         }

         var2 = new PropertyDescriptor("Namespace", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("Namespace", var2);
         var2.setValue("description", "<p>The namespace for the harvested type definition.</p> ");
         setPropertyDescriptorDefault(var2, "ServerRuntime");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"ServerRuntime", "DomainRuntime"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether this type is enabled. Note that enabling a type will have no effect unless the Harvester component is also enabled.</p>  <p>A <code>true</code> value means that this type is harvested. A <code>false</code> value indicates that that this type is not harvested.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KnownType")) {
         var3 = "isKnownType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKnownType";
         }

         var2 = new PropertyDescriptor("KnownType", WLDFHarvestedTypeBean.class, var3, var4);
         var1.put("KnownType", var2);
         var2.setValue("description", "<p>Specifies whether this type is known at startup.  Normally, if a type is not available, the Harvester will keep looking for it. If a type is designated as \"known\", the Harvester issues a validation fault if the type cannot be immediately resolved.</p>  <p>A <code>true</code> value means that this type is known. A <code>false</code> value indicates that this type may not be known.</p>  <p>This flag is useful for WebLogic Server types, where the type information is always available.  In this case, setting the flag to true results in earlier detection and reporting of problems.</p>  <p>This flag is optional, but is recommended for WebLogic Server types.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
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
