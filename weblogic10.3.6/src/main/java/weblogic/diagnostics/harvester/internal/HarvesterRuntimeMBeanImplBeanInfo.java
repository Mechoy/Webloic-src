package weblogic.diagnostics.harvester.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFHarvesterRuntimeMBean;

public class HarvesterRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFHarvesterRuntimeMBean.class;

   public HarvesterRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public HarvesterRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = HarvesterRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("notificationTranslator", "weblogic.diagnostics.harvester.internal.RuntimeMBeanNotificationTranslator");
      var2.setValue("package", "weblogic.diagnostics.harvester.internal");
      String var3 = (new String("<p>Provides information about harvestable and harvested attributes, types, and instances. Harvestable means potentially available for harvesting; harvested means explicitly designated for harvesting. These terms apply to types, instances, and the attributes within those types. In addition, the interface provides access to sampling and snapshot statistics. All statistics are base on data collected during the current server session.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFHarvesterRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AverageSamplingTime")) {
         var3 = "getAverageSamplingTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageSamplingTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageSamplingTime", var2);
         var2.setValue("description", "<p>The average amount of time, in nanoseconds, spent in sampling cycles.</p> ");
      }

      if (!var1.containsKey("ConfiguredNamespaces")) {
         var3 = "getConfiguredNamespaces";
         var4 = null;
         var2 = new PropertyDescriptor("ConfiguredNamespaces", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("ConfiguredNamespaces", var2);
         var2.setValue("description", "<p>Returns the set of MBean namespaces currently configured within the WLDF Harvester.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CurrentDataSampleCount")) {
         var3 = "getCurrentDataSampleCount";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentDataSampleCount", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentDataSampleCount", var2);
         var2.setValue("description", "<p>The number of collected data samples in the current snapshot.</p> ");
      }

      if (!var1.containsKey("CurrentImplicitDataSampleCount")) {
         var3 = "getCurrentImplicitDataSampleCount";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentImplicitDataSampleCount", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentImplicitDataSampleCount", var2);
         var2.setValue("description", "<p>The number of implicit data samples gathered in the last sample.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CurrentSnapshotElapsedTime")) {
         var3 = "getCurrentSnapshotElapsedTime";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentSnapshotElapsedTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentSnapshotElapsedTime", var2);
         var2.setValue("description", "<p>The elapsed time, in nanoseconds, of a snapshot.</p> ");
      }

      if (!var1.containsKey("CurrentSnapshotStartTime")) {
         var3 = "getCurrentSnapshotStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentSnapshotStartTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentSnapshotStartTime", var2);
         var2.setValue("description", "<p>The start time, in nanoseconds, of a snapshot.</p> ");
      }

      if (!var1.containsKey("DefaultNamespace")) {
         var3 = "getDefaultNamespace";
         var4 = null;
         var2 = new PropertyDescriptor("DefaultNamespace", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("DefaultNamespace", var2);
         var2.setValue("description", "<p> Returns the default MBean namespace within the WLDF Harvester.  This is the namespace used if none is provided for a configured MBean metric. </p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("KnownHarvestableTypes")) {
         var3 = "getKnownHarvestableTypes";
         var4 = null;
         var2 = new PropertyDescriptor("KnownHarvestableTypes", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("KnownHarvestableTypes", var2);
         var2.setValue("description", "<p>The set of all known types, regardless of whether the types are currently configured for harvesting. The set includes the WebLogic Server MBeans, which are always present, plus any other types that can be discovered. MBeans that are not WebLogic Server MBeans will require instances to exist in order to discover the type.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("MaximumSamplingTime")) {
         var3 = "getMaximumSamplingTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaximumSamplingTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("MaximumSamplingTime", var2);
         var2.setValue("description", "<p>The maximum sampling time, in nanoseconds.</p> ");
      }

      if (!var1.containsKey("MinimumSamplingTime")) {
         var3 = "getMinimumSamplingTime";
         var4 = null;
         var2 = new PropertyDescriptor("MinimumSamplingTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("MinimumSamplingTime", var2);
         var2.setValue("description", "<p>The minimum sampling time, in nanoseconds.</p> ");
      }

      if (!var1.containsKey("OutlierDetectionFactor")) {
         var3 = "getOutlierDetectionFactor";
         var4 = null;
         var2 = new PropertyDescriptor("OutlierDetectionFactor", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("OutlierDetectionFactor", var2);
         var2.setValue("description", "<p>The multiplicative factor used to determine a statistical outlier. If the actual sampling time exceeds this the session average multiplied by the outlier detection factor, then the sampling time is considered to be a statistical outlier.</p> ");
      }

      if (!var1.containsKey("SamplePeriod")) {
         var3 = "getSamplePeriod";
         var4 = null;
         var2 = new PropertyDescriptor("SamplePeriod", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("SamplePeriod", var2);
         var2.setValue("description", "<p>The current global sample period, in nanoseconds.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("TotalDataSampleCount")) {
         var3 = "getTotalDataSampleCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalDataSampleCount", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalDataSampleCount", var2);
         var2.setValue("description", "<p>The number of configured data samples that have been collected so far in this server session.</p> ");
      }

      if (!var1.containsKey("TotalImplicitDataSampleCount")) {
         var3 = "getTotalImplicitDataSampleCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalImplicitDataSampleCount", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalImplicitDataSampleCount", var2);
         var2.setValue("description", "<p>The number of implicit data samples that have been collected in this server session.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("TotalSamplingCycles")) {
         var3 = "getTotalSamplingCycles";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSamplingCycles", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSamplingCycles", var2);
         var2.setValue("description", "<p>The total number of sampling cycles taken thus far.</p> ");
      }

      if (!var1.containsKey("TotalSamplingTime")) {
         var3 = "getTotalSamplingTime";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSamplingTime", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSamplingTime", var2);
         var2.setValue("description", "<p>The total amount of time, in nanoseconds, spent in sampling cycles.</p> ");
      }

      if (!var1.containsKey("TotalSamplingTimeOutlierCount")) {
         var3 = "getTotalSamplingTimeOutlierCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSamplingTimeOutlierCount", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSamplingTimeOutlierCount", var2);
         var2.setValue("description", "<p>The number of times within this server session that the sampling time differed significantly enough from the average to be considered a statistical outlier.  The Harvester removes these values form the ongoing averages.</p> ");
      }

      if (!var1.containsKey("CurrentSampleTimeAnOutlier")) {
         var3 = "isCurrentSampleTimeAnOutlier";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentSampleTimeAnOutlier", WLDFHarvesterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentSampleTimeAnOutlier", var2);
         var2.setValue("description", "<p>Whether or not the sampling time for the most recent data sample differed significantly enough from the average to be considered a statistical outlier.</p> ");
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
      Method var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getHarvestableAttributes", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "the name of the type to get the attributes for ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.HarvestableTypesNotFoundException if the type          name is valid but could not be located"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousTypeName if the          type name requires qualification to resolve"), BeanInfoHelper.encodeEntities("HarvesterException.TypeNotHarvestableException if the          type could never be harvested")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of attributes that are eligible for harvesting for the specified type. The specified type does not need to be currently configured for harvesting. For MBeans other than WebLogic Server MBeans, returns null until at least one instance has been created.</p>  <p>The returned array represents a list of pairs. The first element in each pair is the attribute name and the second element is the class name of the attribute's type.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("preDeregister");
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getHarvestableAttributesForInstance", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("instancePattern", "the ObjectName or ObjectName pattern of the type to get the attributes for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.HarvestableTypesNotFoundException            if the type name is valid but could not be located"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousTypeName            if the type name requires qualification to resolve"), BeanInfoHelper.encodeEntities("HarvesterException.TypeNotHarvestableException            if the type could never be harvested")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of attributes that are eligible for harvesting for the specified instance name. The type of the specified instance does not need to be currently configured for harvesting. For MBeans other than WebLogic Server MBeans, returns null until at least one instance has been created.</p>  <p>Note that in the case where an ObjectName pattern is specified for a WebLogic Server MBean, the ObjectName's property list must contain the &quot;Type&quot; property (e.g., &quot;com.bea:Type=ServerRuntime,*&quot;). Otherwise, as is the case for any non-WebLogic Server MBean, an instance must exist in order for the set of harvestable attributes to be known.</p>  <p> The returned array represents a list of pairs. The first element in each pair is the attribute name and the second element is the class name of the attribute's type. </p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getCurrentlyHarvestedAttributes", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "the name of the type to get the attributes for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.MissingConfigurationType if the provided type          is not configured for harvesting"), BeanInfoHelper.encodeEntities("HarvesterException.HarvestingNotEnabled if the Harvester is not deployed"), BeanInfoHelper.encodeEntities("HarvesterException.HarvesterException.AmbiguousTypeName if the          type name requires qualification to resolve")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of attributes that are currently being harvested for the specified type. The specified type must be explicitly configured for harvesting.</p>  <p>The returned set of attributes usually matches the corresponding set of attributes configured for harvesting; but if an error occurs when harvesting an attribute, that attribute will be omitted from the returned set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getKnownHarvestableTypes", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p> The set of all known types within a particular MBean namespace, regardless of whether the types are currently configured for harvesting. An MBean namespace loosely corresponds to those MBeans that can be found within a particular MBeanServer, although there may be multiple Harvester delegates that service a particular MBean namespace. The returned set includes the WebLogic Server MBeans, which are always present, plus any other types that can be discovered. MBeans that are not WebLogic Server MBeans will require instances to exist in order to discover the type. </p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("unharvestable", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getKnownHarvestableInstances", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "the name of the type to get the attributes for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.HarvestableTypesNotFoundException if the type          name is valid but could not be located"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousTypeName if the          type name requires qualification to resolve"), BeanInfoHelper.encodeEntities("HarvesterException.TypeNotHarvestableException if the          type could never be harvested")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of instances that are eligible for harvesting for the specified type at the time of the call.  The type does not need to be currently configured for harvesting.</p>  <p>The caller should be aware that instances come and go. This method returns only those instances that exist at the time of the call.</p>  <dl><dt>Note</dt><dd><p>For MBeans that are not WebLogic Server MBeans, returns null until at least one instance has been created.</p></dd></dl> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getKnownHarvestableInstances", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("namespace", "the MBean namespace to query "), createParameterDescriptor("type", "the name of the type to get the attributes for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.HarvestableTypesNotFoundException if the type          name is valid but could not be located"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousTypeName if the          type name requires qualification to resolve"), BeanInfoHelper.encodeEntities("HarvesterException.TypeNotHarvestableException if the          type could never be harvested")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of instances that are eligible for harvesting for the specified type at the time of the call within a particular MBean namespace.  An MBean namespace loosely corresponds to those MBeans that can be found within a particular MBeanServer, although there may be multiple Harvester delegates that service a particular MBean namespace.  The type parameter provided to this call does not need to be currently configured for harvesting.</p>  <p>The caller should be aware that instances come and go. This method returns only those instances that exist at the time of the call.</p>  <dl><dt>Note</dt><dd><p>For MBeans that are not WebLogic Server MBeans, returns null until at least one instance has been created.</p></dd></dl> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getCurrentlyHarvestedInstances", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "the name of the type to get the instances for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.MissingConfigurationType if the provided type          is not configured for harvesting"), BeanInfoHelper.encodeEntities("HarvesterException.HarvestingNotEnabled if the Harvester is not          deployed"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousTypeName if the          type name requires qualification to resolve")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The set of instances that are currently being harvested for the specified type. The type must be configured for harvesting.</p>  <p>The returned set of instances usually matches the corresponding set of instances configured for harvesting; but if an error occurs when harvesting an instance, that instance will be omitted from the returned set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFHarvesterRuntimeMBean.class.getMethod("getHarvestableType", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("instanceName", "the name of the instance to get the type for ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("HarvesterException.HarvestableInstancesNotFoundException if          the provided instance does not currently exist"), BeanInfoHelper.encodeEntities("HarvesterException.AmbiguousInstanceName if the          instance name requires qualification to resolve")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The type associated with a particular harvestable instance.</p> ");
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
