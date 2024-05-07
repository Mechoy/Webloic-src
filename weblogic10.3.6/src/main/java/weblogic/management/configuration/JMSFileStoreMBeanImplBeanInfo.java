package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSFileStoreMBeanImplBeanInfo extends JMSStoreMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSFileStoreMBean.class;

   public JMSFileStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSFileStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSFileStoreMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.FileStoreMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS file store that stores persistent messages and durable subscribers in a file-system directory.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSFileStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BlockSize")) {
         var3 = "getBlockSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBlockSize";
         }

         var2 = new PropertyDescriptor("BlockSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("BlockSize", var2);
         var2.setValue("description", "<p>The smallest addressable block, in bytes, of a file. When a native <code>wlfileio</code> driver is available and the block size has not been configured by the user, the store selects the minimum OS specific value for unbuffered (direct) I/O, if it is within the range [512, 8192].</p> A file store's block size does not change once the file store creates its files. Changes to block size only take effect for new file stores or after the current files have been deleted. See \"Tuning the Persistent Store\" in <i>Performance and Tuning for Oracle WebLogic Server</i>. ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(8192));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CacheDirectory")) {
         var3 = "getCacheDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheDirectory";
         }

         var2 = new PropertyDescriptor("CacheDirectory", JMSFileStoreMBean.class, var3, var4);
         var1.put("CacheDirectory", var2);
         var2.setValue("description", "<p>The location of the cache directory for <code>Direct-Write-With-Cache</code>, ignored for other policies.</p>  <p>When <code>Direct-Write-With-Cache</code> is specified as the <code>SynchronousWritePolicy</code>, cache files are created in addition to primary files (see <a href='#getDirectory'>Directory</a> for the location of primary files). If a cache directory location is specified, the cache file path is <code><i>CacheDirectory</i>/WLStoreCache/<i>StoreName</i>FileNum.DAT.cache</code>. When specified, Oracle recommends using absolute paths, but if the directory location is a relative path, then <code>CacheDirectory</code> is created relative to the WebLogic Server instance's home directory. If \"\" or <code>Null</code> is specified, the <code>Cache Directory</code> is located in the current operating system <code>temp</code> directory as determined by the <code>java.io.tmpdir</code> Java System property (JDK's default: <code>/tmp</code> on UNIX, <code>%TEMP% </code> on Windows) and is <code> <i>TempDirectory</i>/WLStoreCache/<i>DomainName</i>/<i>unique-id</i>/<i>StoreName</i>FileNum.DAT.cache</code>. The value of <code>java.io.tmpdir</code> varies between operating systems and configurations, and can be overridden by passing <code>-Djava.io.tmpdir=<i>My_path</i></code> on the JVM command line. </p>  <p>Considerations: <ul><li>Security: Some users may want to set specific directory permissions to limit access to the cache directory, especially if there are custom configured user access limitations on the primary directory. For a complete guide to WebLogic security, see \"Securing a Production Environment for Oracle WebLogic Server.\" </li>  <li>Additional Disk Space Usage: Cache files consume the same amount of disk space as the primary store files that they mirror. See <a href='#getDirectory'>Directory</a> for the location of primary store files.</li>  <li>Performance: For the best performance, a cache directory should be located in local storage instead of NAS/SAN (remote) storage, preferably in the operating system's <code>temp</code> directory. Relative paths should be avoided, as relative paths are located based on the domain installation, which is typically on remote storage. It is safe to delete a cache directory while the store is not running, but this may slow down the next store boot.</li>  <li>Preventing Corruption and File Locking: Two same named stores must not be configured to share the same primary or cache directory. There are store file locking checks that are designed to detect such conflicts and prevent corruption by failing the store boot, but it is not recommended to depend on the file locking feature for correctness. See <a href='#isFileLockingEnabled'>Enable File Locking</a>.</li>  <li> Boot Recovery: Cache files are reused to speed up the File Store boot and recovery process, but only if the store's host WebLogic Server instance has been shut down cleanly prior to the current boot. For example, cache files are not re-used and are instead fully recreated: after a <code>kill -9</code>, after an OS or JVM crash, or after an off-line change to the primary files, such as a store admin compaction. When cache files are recreated, a <code>Warning</code> log message 280102 is generated.</li>  <li>Fail-Over/Migration Recovery: A file store safely recovers its data without its cache directory. Therefore, a cache directory does not need to be copied or otherwise made accessible after a fail-over or migration, and similarly does not need to be placed in NAS/SAN storage. A <code>Warning</code> log message 280102, which is generated to indicate the need to recreate the cache on the new host system, can be ignored.</li>  <li> Cache File Cleanup: To prevent unused cache files from consuming disk space, test and developer environments should periodically delete cache files.</li> </ul></p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Directory")) {
         var3 = "getDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDirectory";
         }

         var2 = new PropertyDescriptor("Directory", JMSFileStoreMBean.class, var3, var4);
         var1.put("Directory", var2);
         var2.setValue("description", "<p>The path name to the file system directory where the file store maintains its data files.</p> <ul> <li>When targeting a file store to a migratable target, the store directory must be accessible from all candidate server members in the migratable target.</li> <li>For highest availability, use either a SAN (Storage Area Network) or other reliable shared storage.</li> <li>Use of NFS mounts is discouraged, but supported. Most NFS mounts are not transactionally safe by default, and, to ensure transactional correctness, need to be configured using your NFS vendor documentation in order to honor synchronous write requests.</li> <li>For <code>SynchronousWritePolicy</code> of <code>Direct-Write-With-Cache</code>, see <a href='#getCacheDirectory'>Cache Directory</a>. </li> <li>Additional O/S tuning may be required if the directory is hosted by Microsoft Windows, see <a href='#getSynchronousWritePolicy'>Synchronous Write Policy</a> for details.</li></ul> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("InitialSize")) {
         var3 = "getInitialSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialSize";
         }

         var2 = new PropertyDescriptor("InitialSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("InitialSize", var2);
         var2.setValue("description", "<p>The initial file size, in bytes. <ul> <li>Set <code>InitialSize</code> to pre-allocate file space during a file store boot. If <code>InitialSize</code> exceeds <code>MaxFileSize</code>, a store creates multiple files (number of files = <code>InitialSize</code>/<code>MaxFileSize</code> rounded up).</li>  <li>A file store automatically reuses the space from deleted records and automatically expands a file if there is not enough space for a new write request.</li>  <li>Use <code>InitialSize</code> to limit or prevent file expansions during runtime, as file expansion introduces temporary latencies that may be noticeable under rare circumstances. </li> <li>Changes to initial size only take effect for new file stores, or after any current files have been deleted prior to restart.</li>  <li> See <a href='#getMaxFileSize'>Maximum File Size</a>. </ul></p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IoBufferSize")) {
         var3 = "getIoBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIoBufferSize";
         }

         var2 = new PropertyDescriptor("IoBufferSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("IoBufferSize", var2);
         var2.setValue("description", "<p>The I/O buffer size, in bytes, automatically rounded down to the nearest power of 2. <ul> <li> For the <code>Direct-Write-With-Cache</code> policy when a native <code>wlfileio</code> driver is available, <code>IOBufferSize</code> describes the maximum portion of a cache view that is passed to a system call. This portion does not consume off-heap (native) or Java heap memory.</li>  <li> For the <code>Direct-Write</code> and <code>Cache-Flush</code> policies, <code>IOBufferSize</code> is the size of a per store buffer which consumes off-heap (native) memory, where one buffer is allocated during run-time, but multiple buffers may be temporarily created during boot recovery.</li>  <li>When a native <code>wlfileio</code> driver is not available, the setting applies to off-heap (native) memory for all policies (including <code>Disabled</code>).</li>  <li>For the best runtime performance, Oracle recommends setting <code>IOBufferSize</code> so that it is larger than the largest write (multiple concurrent store requests may be combined into a single write).</li>  <li>For the best boot recovery time performance of large stores, Oracle recommends setting <code>IOBufferSize</code> to at least 2 megabytes.</li>  See <code>AllocatedIOBufferBytes</code> to find out the actual allocated off-heap (native) memory amount. It is a multiple of <code>IOBufferSize</code> for the <code>Direct-Write</code> and <code>Cache-Flush</code> policies, or zero.</li>  <li> See <a href='#AllocatedIOBufferBytes'>AllocatedIOBufferBytes</a>. </ul></p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(67108864));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxFileSize")) {
         var3 = "getMaxFileSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxFileSize";
         }

         var2 = new PropertyDescriptor("MaxFileSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("MaxFileSize", var2);
         var2.setValue("description", "<p>The maximum file size, in bytes. <ul> <li>The <code>MaxFileSize</code> value affects the number of files needed to accommodate a store of a particular size (number of files = store size/MaxFileSize rounded up).</li>  <li>A file store automatically reuses space freed by deleted records and automatically expands individual files up to <code>MaxFileSize</code> if there is not enough space for a new record. If there is no space left in exiting files for a new record, a store creates an additional file.</li>  <li> A small number of larger files is normally preferred over a large number of smaller files as each file allocates Window Buffer and file handles. </li>  <li> If <code>MaxFileSize</code> is larger than 2^24 * <code>BlockSize</code>, then <code>MaxFileSize</code> is ignored, and the value becomes 2^24 * <code>BlockSize</code>. The default <code>BlockSize</code> is 512, and 2^24 * 512 is 8 GB. </li>  <li> See <a href='#getInitialSize'>Initial Size</a>.</li> </ul></p> ");
         setPropertyDescriptorDefault(var2, new Long(1342177280L));
         var2.setValue("legalMin", new Long(1048576L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxWindowBufferSize")) {
         var3 = "getMaxWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxWindowBufferSize";
         }

         var2 = new PropertyDescriptor("MaxWindowBufferSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("MaxWindowBufferSize", var2);
         var2.setValue("description", "<p>The maximum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per primary store file. Applies to synchronous write policies <code>Direct-Write-With-Cache</code> and <code>Disabled</code> but only when the native <code>wlfileio</code> library is loaded.</p>  <p>A window buffer does not consume Java heap memory, but does consume off-heap (native) memory. If the store is unable to allocate the requested buffer size, it allocates smaller and smaller buffers until it reaches <code>MinWindowBufferSize</code>, and then fails if cannot honor <code>MinWindowBufferSize</code>.</p>  <p>Oracle recommends setting the max window buffer size to more than double the size of the largest write (multiple concurrently updated records may be combined into a single write), and greater than or equal to the file size, unless there are other constraints. 32-bit JVMs may impose a total limit of between 2 and 4GB for combined Java heap plus off-heap (native) memory usage.</p>  <ul> <li>See store attribute <code>AllocatedWindowBufferBytes</code> to find out the actual allocated Window Buffer Size.<li>  <li> See <a href='#getMaxFileSize'>Maximum File Size</a> and <a href='#getMinWindowBufferSize'>Minimum Window Buffer Size</a>.</li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MinWindowBufferSize")) {
         var3 = "getMinWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinWindowBufferSize";
         }

         var2 = new PropertyDescriptor("MinWindowBufferSize", JMSFileStoreMBean.class, var3, var4);
         var1.put("MinWindowBufferSize", var2);
         var2.setValue("description", "<p>The minimum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per primary store file. Applies to synchronous write policies <code>Direct-Write-With-Cache</code> and <code>Disabled</code>, but only when a native <code>wlfileio</code> library is loaded. See <a href='#getMaxWindowBufferSize'>Maximum Window Buffer Size</a>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSFileStoreMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("SynchronousWritePolicy")) {
         var3 = "getSynchronousWritePolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSynchronousWritePolicy";
         }

         var2 = new PropertyDescriptor("SynchronousWritePolicy", JMSFileStoreMBean.class, var3, var4);
         var1.put("SynchronousWritePolicy", var2);
         var2.setValue("description", "<p>The disk write policy that determines how the file store writes data to disk.</p>  <p>This policy also affects the JMS file store's performance, scalability, and reliability. Oracle recommends <code>Direct-Write-With-Cache</code> which tends to have the highest performance. The default value is <code>Direct-Write</code>. The valid policy options are:</p> <ul> <li> <code>Direct-Write</code> Direct I/O is supported on all platforms. When available, file stores in direct I/O mode automatically load the native I/O <code>wlfileio</code> driver. This option tends to out-perform <code>Cache-Flush</code> and tend to be slower than <code>Direct-Write-With-Cache</code>. This mode does not require a native store <code>wlfileio</code> driver, but performs faster when they are available.</li>  <li><code>Direct-Write-With-Cache</code> Store records are written synchronously to primary files in the directory specified by the <code>Directory</code> attribute and asynchronously to a corresponding cache file in the <code>Cache Directory</code>. The <code>Cache Directory</code> provides information about disk space, locking, security, and performance implications. This mode requires a native store <code>wlfileiocode</code> driver. If the native driver cannot be loaded, then the write mode automatically switches to <code>Direct-Write</code>. See <a href='#getCacheDirectory'>Cache Directory</a>.</li>  <li><code>Cache-Flush</code> Transactions cannot complete until all of their writes have been flushed down to disk. This policy is reliable and scales well as the number of simultaneous users increases.Transactionally safe but tends to be a lower performer than direct-write policies.</li>  <li><code>Disabled</code> Transactions are complete as soon as their writes are cached in memory, instead of waiting for the writes to successfully reach the disk. This is the fastest policy because write requests do not block waiting to be synchronized to disk, but, unlike other policies, is not transactionally safe in the event of operating system or hardware failures. Such failures can lead to duplicate or lost data/messages. This option does not require native store <code>wlfileio</code> drivers, but may run faster when they are available. Some non-WebLogic JMS vendors default to a policy that is equivalent to <code>Disabled<code>.</li> </ul>  Notes: <ul> <li>When available, file stores load WebLogic <code>wlfileio</code> native drivers, which can improve performance. These drivers are included with Windows, Solaris, Linux, HP-UX, and AIX WebLogic installations.</li>  <li>Certain older versions of Microsoft Windows may incorrectly report storage device synchronous write completion if the Windows default <code>Write Cache Enabled</code> setting is used. This violates the transactional semantics of transactional products (not specific to Oracle), including file stores configured with a <code>Direct-Write</code> (default) or <code>Direct-Write-With-Cache</code> policy, as a system crash or power failure can lead to a loss or a duplication of records/messages. One of the visible symptoms is that this problem may manifest itself in high persistent message/transaction throughput exceeding the physical capabilities of your storage device. You can address the problem by applying a Microsoft supplied patch, disabling the Windows <code>Write Cache Enabled</code> setting, or by using a power-protected storage device. See <a href='http://support.microsoft.com/kb/281672/'>http://support.microsoft.com/kb/281672</a> and <a href='http://support.microsoft.com/kb/332023'>http://support.microsoft.com/kb/332023</a>. </li> <li>NFS storage note:  On some operating systems, native driver memory-mapping is incompatible with NFS when files are locked. Stores with synchronous write policies <code>Direct-Write-With-Cache</code> or Disabled, and WebLogic JMS paging stores enhance performance by using the native <code>wlfileio</code> driver to perform memory-map operating system calls. When a store detects an incompatibility between NFS, file locking, and memory mapping, it automatically downgrades to conventional read/write system calls instead of memory mapping. For best performance, Oracle recommends investigating alternative NFS client drivers, configuring a non-NFS storage location, or in controlled environments and at your own risk, disabling the file locks (See <a href='#isFileLockingEnabled'>Enable File Locking</a>). For more information, see \"Tuning the WebLogic Persistent Store\" in <i>Performance and Tuning for Oracle WebLogic Server</i>. </ul> ");
         setPropertyDescriptorDefault(var2, "Direct-Write");
         var2.setValue("legalValues", new Object[]{"Disabled", "Cache-Flush", "Direct-Write", "Direct-Write-With-Cache"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FileLockingEnabled")) {
         var3 = "isFileLockingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileLockingEnabled";
         }

         var2 = new PropertyDescriptor("FileLockingEnabled", JMSFileStoreMBean.class, var3, var4);
         var1.put("FileLockingEnabled", var2);
         var2.setValue("description", "<p>Determines whether OS file locking is used. </p> When file locking protection is enabled, a store boot fails if another store instance already has opened the store files. Do not disable this setting unless you have procedures in place to prevent multiple store instances from opening the same file. File locking is not required but helps prevent corruption in the event that two same-named file store instances attempt to operate in the same directories. This setting applies to both primary and cache files. ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getDirectory"), BeanInfoHelper.encodeEntities("#getCacheDirectory")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
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
      Method var3 = JMSFileStoreMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JMSFileStoreMBean.class.getMethod("restoreDefaultValue", String.class);
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
