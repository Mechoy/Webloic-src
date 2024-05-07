package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFServerDiagnosticMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFServerDiagnosticMBean.class;

   public WLDFServerDiagnosticMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFServerDiagnosticMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFServerDiagnosticMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Use this interface to configure the WebLogic Diagnostic Framework (WLDF) components that are defined for each WebLogic Server instance.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WLDFServerDiagnosticMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DiagnosticDataArchiveType")) {
         var3 = "getDiagnosticDataArchiveType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticDataArchiveType";
         }

         var2 = new PropertyDescriptor("DiagnosticDataArchiveType", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticDataArchiveType", var2);
         var2.setValue("description", "<p>Determines whether the current server persists its harvested metrics and event data in a diagnostic store (file-based store) or a JDBC based archive. The default store is file-based. </p> ");
         setPropertyDescriptorDefault(var2, "FileStoreArchive");
         var2.setValue("legalValues", new Object[]{"FileStoreArchive", "JDBCArchive"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("DiagnosticJDBCResource")) {
         var3 = "getDiagnosticJDBCResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticJDBCResource";
         }

         var2 = new PropertyDescriptor("DiagnosticJDBCResource", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticJDBCResource", var2);
         var2.setValue("description", "<p>The JDBC data source that the server uses to archive its harvested metrics and event data.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getDiagnosticDataArchiveType")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreBlockSize")) {
         var3 = "getDiagnosticStoreBlockSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreBlockSize";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreBlockSize", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreBlockSize", var2);
         var2.setValue("description", "<p>The smallest addressable block, in bytes, of a file. When a native <code>wlfileio</code> driver is available and the block size has not been configured by the user, the store selects the minimum OS specific value for unbuffered (direct) I/O, if it is within the range [512, 8192].</p> A diagnostic store's block size does not change once the diagnostic store creates its files. Changes to block size only take effect for new diagnostic stores or after the current files have been deleted. See \"Tuning the Persistent Store\" in <i>Performance and Tuning for Oracle WebLogic Server</i>. ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(8192));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreDir")) {
         var3 = "getDiagnosticStoreDir";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreDir";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreDir", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreDir", var2);
         var2.setValue("description", "<p>The directory in which the current server maintains its diagnostic store.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getDiagnosticDataArchiveType")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "data/store/diagnostics");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreIoBufferSize")) {
         var3 = "getDiagnosticStoreIoBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreIoBufferSize";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreIoBufferSize", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreIoBufferSize", var2);
         var2.setValue("description", "<p>The I/O buffer size, in bytes, automatically rounded down to the nearest power of 2, controls the largest write size. <ul> <li>When a native <code>wlfileio</code> driver is available, the setting applies to off-heap (native) memory.</li> <li>When a native <code>wlfileio</code> driver is not available, the setting applies to JAVA heap memory.</li> <li>For the best runtime performance, Oracle recommends setting <code>DiagnosticStoreIOBufferSize</code> so that it is larger than the largest write (multiple concurrent store requests may be combined into a single write).</li>  <li>See <code>AllocatedIOBufferBytes</code> to find out the actual allocated off-heap (native) memory amount. It is a multiple of <code>IOBufferSize</code></li>  </ul></p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(67108864));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreMaxFileSize")) {
         var3 = "getDiagnosticStoreMaxFileSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreMaxFileSize";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreMaxFileSize", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreMaxFileSize", var2);
         var2.setValue("description", "<p>The maximum file size, in bytes. <ul> <li>The <code>DiagnosticStoreMaxFileSize</code> value affects the number of files needed to accommodate a diagnostic store of a particular size (number of files = diagnostic store size/MaxFileSize rounded up).</li>  <li>A diagnostic store automatically reuses space freed by deleted records and automatically expands individual files up to <code>DiagnosticStoreMaxFileSize</code> if there is not enough space for a new record. If there is no space left in exiting files for a new record, a diagnostic store creates an additional file.</li>  <li> A small number of larger files is normally preferred over a large number of smaller files as each file allocates Window Buffer and file handles. </li>  <li> If <code>DiagnosticStoreMaxFileSize</code> is larger than 2^24 * <code>DiagnosticStoreBlockSize</code>, then <code>DiagnosticStoreMaxFileSize</code> is ignored, and the value becomes 2^24 * <code>DiagnosticStoreBlockSize</code>. The default <code>DiagnosticStoreBlockSize</code> is 512, and 2^24 * 512 is 8 GB. </li> </ul> Oracle recommends not setting the Diagnostic Store Max File Size above the default value of 1,342,177,280.  </p> ");
         setPropertyDescriptorDefault(var2, new Long(1342177280L));
         var2.setValue("legalMin", new Long(10485760L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreMaxWindowBufferSize")) {
         var3 = "getDiagnosticStoreMaxWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreMaxWindowBufferSize";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreMaxWindowBufferSize", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreMaxWindowBufferSize", var2);
         var2.setValue("description", "<p>The maximum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per diagnostic store file. Applies only when a native <code>wlfileio</code> library is loaded.</p>  <p>A window buffer does not consume Java heap memory, but does consume off-heap (native) memory. If the store is unable to allocate the requested buffer size, it allocates smaller and smaller buffers until it reaches <code>DiagnosticStoreMinWindowBufferSize</code>, and then fails if it cannot honor <code>DiagnosticStoreMinWindowBufferSize</code>.</p>  <p>Oracle recommends setting the max window buffer size to more than double the size of the largest write (multiple concurrently updated records may be combined into a single write), and greater than or equal to the file size, unless there are other constraints. 32-bit JVMs may impose a total limit of between 2 and 4GB for combined Java heap plus off-heap (native) memory usage.</p>  <ul> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DiagnosticStoreMinWindowBufferSize")) {
         var3 = "getDiagnosticStoreMinWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreMinWindowBufferSize";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreMinWindowBufferSize", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreMinWindowBufferSize", var2);
         var2.setValue("description", "<p>The minimum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per diagnostic store file. Applies only when a native <code>wlfileio</code> library is loaded. See <a href='#getDiagnosticStoreMinWindowBufferSize'>Diagnostic Store Maximum Window Buffer Size</a>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("EventPersistenceInterval")) {
         var3 = "getEventPersistenceInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEventPersistenceInterval";
         }

         var2 = new PropertyDescriptor("EventPersistenceInterval", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("EventPersistenceInterval", var2);
         var2.setValue("description", "<p>The interval, in milliseconds, at which queued up instrumentation events will be periodically dispatched to the archive.</p> ");
         setPropertyDescriptorDefault(var2, new Long(5000L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EventsImageCaptureInterval")) {
         var3 = "getEventsImageCaptureInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEventsImageCaptureInterval";
         }

         var2 = new PropertyDescriptor("EventsImageCaptureInterval", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("EventsImageCaptureInterval", var2);
         var2.setValue("description", "<p>The time span, in milliseconds, for which recently archived events will be captured in the diagnostic image. All events archived on or after ( <code>System.currentTimeMillis() - interval</code> ) will be captured.</p> ");
         setPropertyDescriptorDefault(var2, new Long(60000L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ImageDir")) {
         var3 = "getImageDir";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setImageDir";
         }

         var2 = new PropertyDescriptor("ImageDir", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("ImageDir", var2);
         var2.setValue("description", "<p>The default directory where the server stores captured diagnostic images.</p>  <p>If you specify a relative pathname, the root of that path is the server's root directory.</p>  <p>If the directory does not exist, it will be created when the WebLogic Diagnostic Framework is initialized on the server. Note that each image capture request can override this default directory location. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.WLDFImageRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ImageTimeout")) {
         var3 = "getImageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setImageTimeout";
         }

         var2 = new PropertyDescriptor("ImageTimeout", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("ImageTimeout", var2);
         var2.setValue("description", "<p>The default timeout period, in minutes, that the server uses to delay future diagnostic image-capture requests.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(1440));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("PreferredStoreSizeLimit")) {
         var3 = "getPreferredStoreSizeLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreferredStoreSizeLimit";
         }

         var2 = new PropertyDescriptor("PreferredStoreSizeLimit", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("PreferredStoreSizeLimit", var2);
         var2.setValue("description", "<p>Return the preferred limit on the size of diagnostic store file in MB.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StoreSizeCheckPeriod")) {
         var3 = "getStoreSizeCheckPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreSizeCheckPeriod";
         }

         var2 = new PropertyDescriptor("StoreSizeCheckPeriod", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("StoreSizeCheckPeriod", var2);
         var2.setValue("description", "<p>Return the period in hours at which diagnostic store file size check will be performed</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WLDFDataRetirementByAges")) {
         var3 = "getWLDFDataRetirementByAges";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFDataRetirementByAges", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("WLDFDataRetirementByAges", var2);
         var2.setValue("description", "<p>Return the WLDFDataRetirementByAgeMBeans parented by this WLDFServerDiagnosticMBean.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWLDFDataRetirementByAge");
         var2.setValue("destroyer", "destroyWLDFDataRetirementByAge");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WLDFDataRetirements")) {
         var3 = "getWLDFDataRetirements";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFDataRetirements", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("WLDFDataRetirements", var2);
         var2.setValue("description", "<p>Return the WLDFDataRetirementMBeans parented by this WLDFServerDiagnosticMBean.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3", (String)null, this.targetVersion) && !var1.containsKey("WLDFDiagnosticVolume")) {
         var3 = "getWLDFDiagnosticVolume";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWLDFDiagnosticVolume";
         }

         var2 = new PropertyDescriptor("WLDFDiagnosticVolume", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("WLDFDiagnosticVolume", var2);
         var2.setValue("description", "<p>Specifies the volume of diagnostic data that is automatically produced by WebLogic Server at run time. Note that the WLDF diagnostic volume setting does not affect explicitly configured diagnostic modules. For example, this controls the volume of events generated for JRockit Flight Recorder.</p>  <p>This attribute has the following settings:</p> <ul> <li><code>Off</code> No diagnostic data is produced.</li> <li><code>Low</code> Minimal amounts of automatic diagnostic data are produced. This is the default.</li> <li><code>Medium</code> Additional diagnostic data is automatically generated beyond the amount generated for <code>Low</code>.</li> <li><code>High</code> Additional diagnostic data is automatically generated beyond the amount generated for <code>Medium</code>.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "Low");
         var2.setValue("legalValues", new Object[]{"Off", "Low", "Medium", "High"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.3");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DataRetirementEnabled")) {
         var3 = "isDataRetirementEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataRetirementEnabled";
         }

         var2 = new PropertyDescriptor("DataRetirementEnabled", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DataRetirementEnabled", var2);
         var2.setValue("description", "<p>This attribute controls if configuration based data retirement functionality is enabled on the server. If disabled, all retirement policies will be disabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("DiagnosticContextEnabled")) {
         var3 = "isDiagnosticContextEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticContextEnabled";
         }

         var2 = new PropertyDescriptor("DiagnosticContextEnabled", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticContextEnabled", var2);
         var2.setValue("description", "<p>If true, diagnostic context creation is enabled. If false, the diagnostic context will not be created when requested. However, if the diagnostics context already exists because it was propagated from another VM or was created through the DyeInjection monitor, the context will be made available.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DiagnosticStoreFileLockingEnabled")) {
         var3 = "isDiagnosticStoreFileLockingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticStoreFileLockingEnabled";
         }

         var2 = new PropertyDescriptor("DiagnosticStoreFileLockingEnabled", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("DiagnosticStoreFileLockingEnabled", var2);
         var2.setValue("description", "<p>Determines whether OS file locking is used. </p> When file locking protection is enabled, a store boot fails if another store instance already has opened the store files. Do not disable this setting unless you have procedures in place to prevent multiple store instances from opening the same file. File locking is not required but helps prevent corruption in the event that two same-named file store instances attempt to operate in the same directories. This setting applies to both primary and cache files. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SynchronousEventPersistenceEnabled")) {
         var3 = "isSynchronousEventPersistenceEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSynchronousEventPersistenceEnabled";
         }

         var2 = new PropertyDescriptor("SynchronousEventPersistenceEnabled", WLDFServerDiagnosticMBean.class, var3, var4);
         var1.put("SynchronousEventPersistenceEnabled", var2);
         var2.setValue("description", "<p>Specifies the instrumentation events persistence policy. If true, events will be persisted synchronously within the same thread. If false, events will be queued up to be persisted in a separate thread.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = WLDFServerDiagnosticMBean.class.getMethod("createWLDFDataRetirementByAge", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create WLDFDataRetirement instance corresponding to a WLDF archive</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WLDFDataRetirementByAges");
            var2.setValue("since", "10.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = WLDFServerDiagnosticMBean.class.getMethod("destroyWLDFDataRetirementByAge", WLDFDataRetirementByAgeMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wldfDataRetirement", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Deletes WLDFDataRetirementByAgeMBean object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WLDFDataRetirementByAges");
            var2.setValue("since", "10.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         Method var3 = WLDFServerDiagnosticMBean.class.getMethod("lookupWLDFDataRetirementByAge", String.class);
         ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            MethodDescriptor var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Look up WLDFDataRetirementByAgeMBean object with given name</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WLDFDataRetirementByAges");
            var2.setValue("since", "10.0.0.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFServerDiagnosticMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFServerDiagnosticMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = WLDFServerDiagnosticMBean.class.getMethod("lookupWLDFDataRetirement", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the data retirement ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Look up WLDFDataRetirement object with given name</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.0.0.0");
         }
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
