package weblogic.deploy.utils;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.mail.internet.MimeUtility;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.utils.JMXDeployerHelper;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.management.DeploymentNotification;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.utils.DeployerHelperTextFormatter;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.utils.Debug;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class DeployerHelper {
   public static final String HTTP_STRING = "http";
   public static final String HTTPS_STRING = "https";
   public static final String T3_STRING = "t3";
   public static final String T3S_STRING = "t3s";
   public static final String IIOP_STRING = "iiop";
   public static final String IIOPS_STRING = "iiops";
   private static final String DEPLOYER_MBEAN_TYPE = "DeployerRuntime";
   private static final String APPLICATION_MBEAN_TYPE = "Application";
   private static final String MIME_BOUNDARY = "---------------------------7d01b33320494";
   private static final String TEMP_FILE_PREFIX = "wl_comp";
   private static final String TEMP_FILE_EXT = ".jar";
   private MBeanHome mBeanHome;
   private MBeanServerConnection mbeanServerConnection;
   private JMXDeployerHelper jmxHelper;
   private DeployerRuntimeMBean deployer;
   private static final DeployerHelperTextFormatter textFormatter = new DeployerHelperTextFormatter();
   private static DeployerTextFormatter messageFormatter = new DeployerTextFormatter();
   private boolean isUsingMBeanServerConnection;
   private boolean formatted;
   private TaskCompletionNotificationListener taskCompletion;

   private DeployerHelper() {
      this.isUsingMBeanServerConnection = false;
      this.formatted = false;
      this.taskCompletion = null;
   }

   public DeployerHelper(MBeanHome var1) {
      this();
      this.mBeanHome = var1;
      this.isUsingMBeanServerConnection = false;
   }

   public DeployerHelper(MBeanServerConnection var1) {
      this();
      this.mbeanServerConnection = var1;

      try {
         this.jmxHelper = new JMXDeployerHelper(this.mbeanServerConnection);
      } catch (weblogic.deploy.api.internal.utils.DeployerHelperException var3) {
         throw new ServerConnectionException(var3.getMessage(), var3);
      }

      this.isUsingMBeanServerConnection = true;
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
         BufferedInputStream var15 = null;

         try {
            if (var3 == null || var3.contains(var4)) {
               if (var3 != null) {
                  var3.remove(var4);
               }

               JarEntry var17 = new JarEntry(var4);
               var17.setTime(var1.lastModified());
               var2.putNextEntry(var17);
               byte[] var8 = new byte[4096];
               boolean var9 = false;
               var15 = new BufferedInputStream(new FileInputStream(var1));

               int var18;
               while((var18 = var15.read(var8)) != -1) {
                  var2.write(var8, 0, var18);
               }
            }
         } catch (Exception var13) {
            String var16 = DeployerHelperTextFormatter.getInstance().exceptionArchivingFile(var1.toString(), var13.toString());
            if (var13 instanceof IOException) {
               throw (IOException)var13;
            }

            throw new IOException(var16);
         } finally {
            if (var15 != null) {
               var15.close();
            }

         }
      }

   }

   public MBeanHome getBeanHome() {
      return this.mBeanHome;
   }

   public MBeanServerConnection getMBeanServerConnection() {
      return this.mbeanServerConnection;
   }

   public DeployerRuntimeMBean getDeployer() {
      if (this.deployer != null) {
         return this.deployer;
      } else if (!this.isUsingMBeanServerConnection) {
         Set var1 = this.mBeanHome.getMBeansByType("DeployerRuntime");
         if (var1.size() != 1) {
            return null;
         } else {
            this.deployer = (DeployerRuntimeMBean)var1.iterator().next();
            return this.deployer;
         }
      } else {
         try {
            this.deployer = this.jmxHelper.getDeployer();
         } catch (Throwable var2) {
            var2.printStackTrace();
         }

         return this.deployer;
      }
   }

   public DeploymentTaskRuntimeMBean getTaskByID(String var1) {
      return this.getDeployer().query(var1);
   }

   public DeploymentTaskRuntimeMBean[] getAllTasks() {
      return this.getDeployer().list();
   }

   public ApplicationMBean getApplication(String var1) throws InstanceNotFoundException {
      if (!this.isUsingMBeanServerConnection) {
         return (ApplicationMBean)this.mBeanHome.getMBean("Application", var1);
      } else {
         DomainMBean var2 = this.jmxHelper.getDomain();
         return var2.lookupApplication(var1);
      }
   }

   public List getAppDeployments() {
      ArrayList var1 = new ArrayList();
      if (this.isUsingMBeanServerConnection) {
         DomainMBean var6 = this.jmxHelper.getDomain();
         AppDeploymentMBean[] var7 = var6.getAppDeployments();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            if (!var7[var8].isInternalApp()) {
               String var5 = ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var7[var8]);
               var1.add(var5);
            }
         }

         return var1;
      } else {
         Set var2 = this.mBeanHome.getMBeansByType("Application");
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            ApplicationMBean var4 = (ApplicationMBean)var3.next();
            if (!var4.isInternalApp()) {
               var1.add(var4.getName());
            }
         }

         return var1;
      }
   }

   public String getAdminServerName() {
      if (!this.isUsingMBeanServerConnection) {
         DomainMBean var1 = this.mBeanHome.getActiveDomain();
         return var1.getAdminServerName();
      } else {
         return this.jmxHelper.getDomain().getAdminServerName();
      }
   }

   public boolean isSourceArchive(String var1) {
      File var2 = new File(var1);
      return var2.isFile();
   }

   public void initiateListening(DeploymentTaskRuntimeMBean var1) throws InstanceNotFoundException {
      ApplicationMBean var2 = var1.getDeploymentObject();
      if (var2 != null) {
         if (!this.isUsingMBeanServerConnection) {
            RemoteMBeanServer var3 = this.mBeanHome.getMBeanServer();

            try {
               var3.addNotificationListener(var2.getObjectName(), new RemoteDeployerHelperListener(var1.getId(), this), new DeployerHelperFilter(), (Object)null);
            } catch (Exception var5) {
            }

         } else {
            try {
               this.mbeanServerConnection.addNotificationListener(var2.getObjectName(), new DeployerHelperListener(var1.getId(), this), new DeployerHelperFilter(), (Object)null);
            } catch (IOException var6) {
               var6.printStackTrace();
            }

         }
      }
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
         UnsyncByteArrayOutputStream var13 = new UnsyncByteArrayOutputStream();
         BufferedInputStream var14 = this.getInputStreamForSource(var4, var5);
         this.transferData(var14, var13, var11);
         var9.setRequestProperty("Content-Length", String.valueOf(var13.size()));
         OutputStream var15 = var9.getOutputStream();
         var13.writeTo(var15);
         BufferedReader var16 = new BufferedReader(new InputStreamReader(var9.getInputStream()));
         var7 = var16.readLine();
         var16.close();
         return var7;
      } catch (URISyntaxException var17) {
         throw new DeployerHelperException(var17.toString(), var17);
      } catch (IOException var18) {
         throw new DeployerHelperException(textFormatter.exceptionUploadingSource(var8, var6, var4), var18);
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

   public void setFormatted(boolean var1) {
      this.formatted = var1;
   }

   private void transferData(InputStream var1, OutputStream var2, String var3) throws IOException {
      PrintStream var4 = new PrintStream(var2);
      String var5 = "-----------------------------7d01b33320494\r\n";
      var5 = var5 + "Content-Disposition: form-data; name=\"file\"; filename=\"" + var3 + "\"\r\n";
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
      var4.setRequestProperty("username", mimeEncode(var1));
      var4.setRequestProperty("password", mimeEncode(var2));
      if (var3 != null) {
         var4.setRequestProperty("wl_upload_application_name", var3);
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

   public void registerTaskCompletionNotificationListener(DeploymentTaskRuntimeMBean var1) {
      try {
         if (this.isUsingMBeanServerConnection) {
            Debug.assertion(var1 != null);
            this.taskCompletion = new TaskCompletionNotificationListener(var1);
            this.mbeanServerConnection.addNotificationListener(var1.getObjectName(), this.taskCompletion, new TaskCompletionNotificationFilter(), (Object)null);
            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void waitForTaskCompletion(DeploymentTaskRuntimeMBean var1, long var2) {
      if (this.isUsingMBeanServerConnection && this.taskCompletion != null) {
         long var4 = var2 - System.currentTimeMillis();
         if (var4 > 0L) {
            this.taskCompletion.waitForTaskCompletion(var4);
         }

      } else {
         var1.waitForTaskCompletion(var2);
      }
   }

   protected final void showDeploymentNotificationInformation(String var1, DeploymentNotification var2) {
      String var3;
      if (this.formatted) {
         var3 = this.translateNotificationType(var2.getPhase());
         if (var2.isAppNotification()) {
            println(messageFormatter.showDeploymentNotification(var1, var3, var2.getAppName(), var2.getServerName()));
         }
      } else {
         var3 = var2.getAppName();
         String var4 = var2.getServerName();
         String var5 = null;
         String var6 = null;
         String var7;
         if (var2.isModuleNotification()) {
            var6 = var2.getModuleName();
            var7 = var2.getCurrentState();
            String var8 = var2.getTargetState();
            String var9 = var2.getTransition();
            if (var9.equals("end")) {
               var5 = messageFormatter.successfulTransition(var6, var7, var8, var4);
            } else if (var9.equals("failed")) {
               var5 = messageFormatter.failedTransition(var6, var7, var8, var4);
            }

            if (var5 != null) {
               println(var5);
            }
         } else {
            var7 = var2.getPhase();
            println(messageFormatter.appNotification(var3, var4, var7));
         }
      }

   }

   private String translateNotificationType(String var1) {
      if ("activated".equals(var1)) {
         return messageFormatter.messageNotificationActivated();
      } else if ("activating".equals(var1)) {
         return messageFormatter.messageNotificationActivating();
      } else if ("deactivated".equals(var1)) {
         return messageFormatter.messageNotificationDeactivated();
      } else if ("deactivating".equals(var1)) {
         return messageFormatter.messageNotificationDeactivating();
      } else if ("prepared".equals(var1)) {
         return messageFormatter.messageNotificationPrepared();
      } else if ("preparing".equals(var1)) {
         return messageFormatter.messageNotificationPreparing();
      } else if ("unprepared".equals(var1)) {
         return messageFormatter.messageNotificationUnprepared();
      } else if ("unpreparing".equals(var1)) {
         return messageFormatter.messageNotificationUnpreparing();
      } else if ("distributing".equals(var1)) {
         return messageFormatter.messageNotificationDistributing();
      } else if ("distributed".equals(var1)) {
         return messageFormatter.messageNotificationDistributed();
      } else {
         return "failed".equals(var1) ? messageFormatter.messageNotificationFailed() : var1;
      }
   }

   private static void println(String var0) {
      System.out.println(var0);
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
}
