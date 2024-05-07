package weblogic.deploy.api.internal.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.mail.internet.MimeUtility;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import weblogic.management.DeploymentNotification;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.utils.DeployerHelperTextFormatter;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class JMXDeployerHelper {
   public static final String HTTP_STRING = "http";
   public static final String HTTPS_STRING = "https";
   public static final String T3_STRING = "t3";
   public static final String T3S_STRING = "t3s";
   public static final String IIOP_STRING = "iiop";
   public static final String IIOPS_STRING = "iiops";
   private static final String MIME_BOUNDARY = "---------------------------7d01b33320494";
   private static final String TEMP_FILE_PREFIX = "wl_comp";
   private static final String TEMP_FILE_EXT = ".jar";
   private static final boolean UPLOAD_LARGEFILE = Boolean.getBoolean("weblogic.deploy.UploadLargeFile");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final MBeanServerConnection mBeanServer;
   private Vector notifications = null;
   private DeployerRuntimeMBean deployer;
   private static final DeployerHelperTextFormatter textFormatter = new DeployerHelperTextFormatter();
   private static final boolean debug = false;
   private DomainRuntimeMBean domainRuntime = null;
   private AppRuntimeStateRuntimeMBean appRT = null;
   private DomainRuntimeServiceMBean drsb;
   private DomainMBean domain;
   private JMXConnector connector = null;
   private ConfigurationManagerMBean configMgr = null;

   public JMXDeployerHelper(JMXConnector var1) throws DeployerHelperException {
      if (var1 != null) {
         this.connector = var1;
         MBeanServerConnection var2 = null;

         try {
            var2 = var1.getMBeanServerConnection();
         } catch (IOException var5) {
         }

         this.mBeanServer = var2;
      } else {
         this.mBeanServer = null;
      }

      try {
         this.init();
      } catch (InstanceNotFoundException var4) {
         throw new DeployerHelperException(var4.toString(), var4);
      }
   }

   public JMXDeployerHelper(MBeanServerConnection var1) throws DeployerHelperException {
      this.mBeanServer = var1;

      try {
         this.init();
      } catch (InstanceNotFoundException var3) {
         throw new DeployerHelperException(var3.toString(), var3);
      }
   }

   private void init() throws InstanceNotFoundException {
      try {
         if (this.mBeanServer == null) {
            DomainAccess var1 = ManagementService.getDomainAccess(kernelId);
            this.domainRuntime = var1.getDomainRuntime();
            this.deployer = this.domainRuntime.getDeployerRuntime();
            this.appRT = this.domainRuntime.getAppRuntimeStateRuntime();
         } else {
            ObjectName var4 = new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME);
            if (this.connector == null) {
               this.drsb = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.mBeanServer, var4);
            } else {
               this.drsb = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.connector, var4);
            }

            this.domainRuntime = this.drsb.getDomainRuntime();
            this.deployer = this.domainRuntime.getDeployerRuntime();
            this.appRT = this.domainRuntime.getAppRuntimeStateRuntime();
         }

      } catch (Throwable var3) {
         InstanceNotFoundException var2 = new InstanceNotFoundException(var3.toString());
         var2.initCause(var3);
         throw var2;
      }
   }

   public ConfigurationManagerMBean getConfigMgr() {
      return this.configMgr;
   }

   public boolean needsNonExclusiveLock() {
      if (this.getConfigMgr() != null) {
         return this.getConfigMgr().isEditor() && !this.getConfigMgr().isCurrentEditorExclusive();
      } else {
         EditAccess var1 = ManagementServiceRestricted.getEditAccess(kernelId);
         return var1 != null && var1.isEditor() && !var1.isEditorExclusive();
      }
   }

   public boolean isEditing() {
      if (this.getConfigMgr() != null) {
         return this.getConfigMgr().getCurrentEditor() != null;
      } else {
         EditAccess var1 = ManagementServiceRestricted.getEditAccess(kernelId);
         return var1 != null && var1.getEditor() != null;
      }
   }

   public void setConfigMgr(ConfigurationManagerMBean var1) {
      this.configMgr = var1;
   }

   public DomainMBean getDomain() {
      if (this.mBeanServer != null) {
         this.domain = this.drsb.getDomainPending();
         if (this.domain == null) {
            this.domain = this.drsb.getDomainConfiguration();
         }
      } else {
         try {
            this.domain = ManagementServiceRestricted.getEditAccess(kernelId).getDomainBeanWithoutLock();
         } catch (Throwable var3) {
            RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
            this.domain = var2.getDomain();
         }
      }

      return this.domain;
   }

   private boolean isAliveState(String var1) {
      return var1.equals("ADMIN") || var1.equals("RUNNING") || var1.equals("RESUMING");
   }

   public boolean isServerAlive(String var1) {
      try {
         ServerLifeCycleRuntimeMBean var2 = this.domainRuntime.lookupServerLifeCycleRuntime(var1);
         return this.isAliveState(var2.getState());
      } catch (Throwable var3) {
         return false;
      }
   }

   public AppRuntimeStateRuntimeMBean getAppRuntimeStateMBean() throws Exception {
      return this.appRT;
   }

   public static File createJarFromDirectory(File var0, File var1, File var2, Set var3) throws IOException {
      if (var1 == null) {
         var1 = var0;
      }

      File var4 = null;
      JarOutputStream var5 = null;

      File var7;
      try {
         var4 = File.createTempFile("wl_comp", ".jar", var2);
         var4.deleteOnExit();
         var5 = new JarOutputStream(new FileOutputStream(var4));
         String var6 = var0.toString().replace(File.separatorChar, '/');
         addFileOrDirectoryToJar(var6.length(), var1, var5, var3);
         var7 = var4;
      } finally {
         if (var5 != null) {
            var5.close();
         }

      }

      return var7;
   }

   private static void addFileOrDirectoryToJar(int var0, File var1, JarOutputStream var2, Set var3) throws IOException {
      String var4 = var1.toString().replace(File.separatorChar, '/');
      if (var4.length() > var0) {
         var4 = var4.substring(var0 + 1);
      } else if (var4.length() == var0 && var1.isFile()) {
         var4 = var1.getName();
      } else {
         var4 = "";
      }

      if (var1.isDirectory()) {
         if (var3 != null && var3.contains(var4)) {
            var3.remove(var4);
            var3 = null;
         }

         String[] var5 = var1.list();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            File var7 = new File(var1, var5[var6]);
            addFileOrDirectoryToJar(var0, var7, var2, var3);
         }
      } else {
         BufferedInputStream var16 = null;

         try {
            if (var3 == null || var3.contains(var4)) {
               if (var3 != null) {
                  var3.remove(var4);
               }

               JarEntry var18 = new JarEntry(var4);
               var18.setTime(var1.lastModified());
               var2.putNextEntry(var18);
               byte[] var8 = new byte[4096];
               boolean var9 = false;
               var16 = new BufferedInputStream(new FileInputStream(var1));

               int var19;
               while((var19 = var16.read(var8)) != -1) {
                  var2.write(var8, 0, var19);
               }
            }
         } catch (Exception var14) {
            String var17 = DeployerHelperTextFormatter.getInstance().exceptionArchivingFile(var1.toString(), var14.toString());
            if (var14 instanceof IOException) {
               throw (IOException)var14;
            }

            throw new IOException(var17);
         } finally {
            if (var16 != null) {
               var16.close();
            }

         }
      }

   }

   public DeployerRuntimeMBean getDeployer() throws Throwable {
      return this.deployer;
   }

   public DeploymentTaskRuntimeMBean getTaskMBean(String var1) {
      try {
         return this.deployer.query(var1);
      } catch (Throwable var3) {
         return null;
      }
   }

   public void removeTask(DeploymentTaskRuntimeMBean var1) {
      if (var1 != null) {
         this.deployer.removeTask(var1.getId());
      }
   }

   public DeploymentTaskRuntimeMBean getTaskByID(String var1) {
      try {
         return this.deployer.query(var1);
      } catch (Throwable var3) {
         throw new RuntimeException(var3);
      }
   }

   public DeploymentTaskRuntimeMBean[] getAllTasks() {
      try {
         return this.deployer.list();
      } catch (Throwable var2) {
         throw new RuntimeException(var2);
      }
   }

   public ApplicationMBean getApplication(String var1) throws InstanceNotFoundException {
      return this.getAppDeployment(var1).getAppMBean();
   }

   public AppDeploymentMBean getAppDeployment(String var1) throws InstanceNotFoundException {
      AppDeploymentMBean var2 = AppDeploymentHelper.lookupAppOrLib(var1, this.getDomain());
      if (var2 == null) {
         throw new InstanceNotFoundException(var1);
      } else {
         return var2;
      }
   }

   public String getAdminServerName() {
      if (this.domain == null) {
         this.getDomain();
      }

      return this.domain.getAdminServerName();
   }

   public DeploymentNotification getNextNotification(long var1) {
      DeploymentNotification var3 = null;
      long var4 = System.currentTimeMillis() + var1;

      do {
         if (this.notifications != null && !this.notifications.isEmpty()) {
            var3 = (DeploymentNotification)this.notifications.remove(0);
         } else {
            long var6 = var4 - System.currentTimeMillis();
            if (var6 <= 0L) {
               return null;
            }

            try {
               this.wait(var6);
            } catch (InterruptedException var9) {
            }
         }
      } while(var3 == null);

      return var3;
   }

   public boolean isSourceArchive(String var1) {
      File var2 = new File(var1);
      return var2.isFile();
   }

   public synchronized void queueNotification(Notification var1) {
      this.notifications.add(var1);
      this.notify();
   }

   public String uploadSource(String var1, String var2, String var3, String var4, String[] var5, String var6) throws DeployerHelperException {
      String var7 = null;
      String var8 = null;

      try {
         var8 = this.getDSSUrl(var1);
         HttpURLConnection var9 = this.getDSSConnection(var8);
         this.initConnection(var2, var3, var6, var9, "app_upload");
         File var10 = new File(var4);
         String var11 = var10.getName();
         String var12;
         if (this.isSourceArchive(var4)) {
            var12 = "true";
         } else {
            var12 = "false";
            var11 = var11.concat(".jar");
         }

         var9.setRequestProperty("archive", var12);
         var9.setRequestProperty("wl_upload_delta", Boolean.toString(var5 != null));
         BufferedInputStream var13 = this.getInputStreamForSource(var4, var5);
         OutputStream var14;
         if (UPLOAD_LARGEFILE) {
            var9.setChunkedStreamingMode(8192);
            var14 = var9.getOutputStream();
            this.transferData(var13, var14, var11);
         } else {
            UnsyncByteArrayOutputStream var15 = new UnsyncByteArrayOutputStream();
            this.transferData(var13, var15, var11);
            var9.setRequestProperty("Content-Length", String.valueOf(var15.size()));
            var14 = var9.getOutputStream();
            var15.writeTo(var14);
         }

         BufferedReader var18 = new BufferedReader(new InputStreamReader(var9.getInputStream()));
         var7 = var18.readLine();
         var7 = this.mimeDecode(var7);
         var18.close();
         return var7;
      } catch (URISyntaxException var16) {
         throw new DeployerHelperException(var16.toString(), var16);
      } catch (IOException var17) {
         throw new DeployerHelperException(textFormatter.exceptionUploadingSource(var8, var6, var4), var17);
      }
   }

   public String uploadPlan(String var1, String var2, String var3, String var4, String var5) throws DeployerHelperException {
      String var6 = null;
      String var7 = null;

      try {
         var7 = this.getDSSUrl(var1);
         HttpURLConnection var8 = this.getDSSConnection(var7);
         this.initConnection(var2, var3, var5, var8, "plan_upload");
         File var9 = new File(var4);
         String var10 = var9.getName();
         if (var9.isDirectory()) {
            var8.setRequestProperty("archive", "false");
            var10 = var10.concat(".jar");
         }

         OutputStream var11 = var8.getOutputStream();
         BufferedInputStream var12 = this.getInputStreamForSource(var4, (String[])null);
         this.transferData(var12, var11, var10);
         BufferedReader var13 = new BufferedReader(new InputStreamReader(var8.getInputStream()));
         var6 = var13.readLine();
         var13.close();
         return var6;
      } catch (URISyntaxException var14) {
         throw new DeployerHelperException(var14.toString(), var14);
      } catch (IOException var15) {
         throw new DeployerHelperException(textFormatter.exceptionUploadingSource(var7, var5, var4), var15);
      }
   }

   private void transferData(InputStream var1, OutputStream var2, String var3) throws IOException {
      PrintStream var4 = new PrintStream(var2);
      String var5 = "-----------------------------7d01b33320494\r\n";
      var5 = var5 + "Content-Disposition: form-data; name=\"file\"; filename=\"" + mimeEncode(var3) + "\"\r\n";
      var5 = var5 + "Content-Type: application/x-zip-compressed\r\n";
      var5 = var5 + "\r\n";
      var4.print(var5);
      DataOutputStream var6 = new DataOutputStream(var2);
      byte[] var7 = new byte[1024];

      while(true) {
         int var8 = var1.read(var7);
         if (var8 == -1) {
            var4.print("\r\n-----------------------------7d01b33320494--");
            var4.flush();
            var4.close();
            var6.close();
            var1.close();
            return;
         }

         var6.write(var7, 0, var8);
      }
   }

   private void initConnection(String var1, String var2, String var3, HttpURLConnection var4, String var5) {
      var4.setDoOutput(true);
      var4.setDoInput(true);
      var4.setAllowUserInteraction(true);
      var4.setRequestProperty("wl_request_type", mimeEncode(var5));
      if (var1 != null) {
         var4.setRequestProperty("username", mimeEncode(var1));
         var4.setRequestProperty("password", mimeEncode(var2));
      } else {
         ConnectionSigner.signConnection(var4, (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()));
      }

      if (var3 != null) {
         var4.setRequestProperty("wl_upload_application_name", mimeEncode(var3));
      }

      var4.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------7d01b33320494");
   }

   private String getDSSUrl(String var1) throws URISyntaxException {
      if (!var1.startsWith("http")) {
         URI var2 = new URI(var1);
         URI var3 = new URI(this.getHTTPProtocol(var2.getScheme()), (String)null, var2.getHost(), var2.getPort(), (String)null, (String)null, (String)null);
         var1 = var3.toString();
      }

      if (!var1.endsWith("/")) {
         var1 = var1 + "/";
      }

      return var1 + "bea_wls_deployment_internal/DeploymentService";
   }

   private HttpURLConnection getDSSConnection(String var1) throws IOException {
      URL var2 = new URL(var1);
      return (HttpURLConnection)var2.openConnection();
   }

   private String getHTTPProtocol(String var1) {
      return !var1.equals("https") && !var1.equals("t3s") && !var1.equals("iiops") ? "http" : "https";
   }

   BufferedInputStream getInputStreamForSource(String var1, String[] var2) throws IOException, DeployerHelperException {
      try {
         URL var9 = new URL(var1);
         return new BufferedInputStream(var9.openStream());
      } catch (MalformedURLException var8) {
         File var4 = new File(var1);
         if (!var4.exists()) {
            throw new DeployerHelperException(textFormatter.exceptionNoSuchSource(var1));
         } else {
            TreeSet var5 = null;
            if (var2 != null) {
               var5 = new TreeSet();

               for(int var6 = 0; var6 < var2.length; ++var6) {
                  String var7 = var2[var6].replace(File.separatorChar, '/');
                  var5.add(var7);
               }
            }

            if (var4.isDirectory()) {
               var4 = createJarFromDirectory(var4, (File)null, (File)null, var5);
            }

            if (var2 != null && var5 != null && var5.size() > 0) {
               throw new DeployerHelperException(textFormatter.exceptionUploadingFiles(var5.toString(), var1));
            } else {
               return new BufferedInputStream(new FileInputStream(var4));
            }
         }
      }
   }

   private static String mimeEncode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.encodeText(var0, "UTF-8", (String)null);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   private String mimeDecode(String var1) {
      try {
         if (var1 != null) {
            return MimeUtility.decodeText(var1);
         }
      } catch (UnsupportedEncodingException var3) {
      }

      return var1;
   }
}
