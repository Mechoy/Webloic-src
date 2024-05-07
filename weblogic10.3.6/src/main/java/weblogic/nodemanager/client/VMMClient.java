package weblogic.nodemanager.client;

import com.oracle.vmm.client.AdapterInfo;
import com.oracle.vmm.client.ConnectionSpec;
import com.oracle.vmm.client.ResourcePool;
import com.oracle.vmm.client.VMMConnector;
import com.oracle.vmm.client.VMState;
import com.oracle.vmm.client.VirtualMachine;
import com.oracle.vmm.client.VirtualMachineManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import weblogic.kernel.KernelStatus;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.security.utils.ClientKeyStoreConfiguration;
import weblogic.security.utils.KeyStoreConfiguration;
import weblogic.security.utils.KeyStoreConfigurationHelper;
import weblogic.security.utils.KeyStoreInfo;
import weblogic.security.utils.MBeanKeyStoreConfiguration;

public class VMMClient extends NMClient implements CallbackHandler {
   private static String defaultBasePort = "8888";
   private static String defaultBaseSport = "4443";
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();
   private boolean connected = false;
   private ConnectionSpec connSpec;
   private static boolean debug = System.getProperty("DebugVMM", "false").equalsIgnoreCase("true");
   private boolean isSecure = false;
   private String virtualMachineName;
   private VirtualMachineManager vmm;
   private String vmmAPIVersion = System.getProperty("vmm.api.version", "2.2");
   private String vmmType = System.getProperty("vmm.type", "OracleVM");
   private SSLSocketFactory sslSocketFactory = null;

   public VMMClient(String var1) {
      String var2 = null;
      if (var1 != null && var1.trim().length() != 0) {
         StringTokenizer var3 = new StringTokenizer(var1.trim(), "_-", true);
         int var4 = var3.countTokens();
         if (var4 >= 1 && (var3.nextToken() + "-").equalsIgnoreCase("vmms-")) {
            this.isSecure = true;
         }

         if (var4 == 3) {
            var2 = var3.nextToken();
            if (var2.equals("-")) {
               this.vmmType = var3.nextToken();
            } else if (var2.equals("_")) {
               this.vmmAPIVersion = var3.nextToken();
            }
         }

         if (var4 == 5) {
            var2 = var3.nextToken();
            this.vmmType = var3.nextToken();
            var2 = var3.nextToken();
            this.vmmAPIVersion = var3.nextToken();
         }

         if (this.isSecure) {
            this.setPort(new Integer(defaultBaseSport));

            try {
               this.sslSocketFactory = this.getSSLSocketFactory();
            } catch (IOException var6) {
               throw new IllegalArgumentException(var6.getLocalizedMessage());
            }
         } else {
            this.setPort(new Integer(defaultBasePort));
         }

      }
   }

   private void checkConnected(boolean var1) throws IOException {
      if (!this.connected) {
         if (this.domainName == null) {
            throw new IllegalStateException(nmText.getDomainNotSet());
         }

         if (var1 && this.serverName == null) {
            throw new IllegalStateException(nmText.getServerNotSet());
         }

         this.connect();
         this.connected = true;
      }

   }

   protected void checkNotConnected() throws IllegalStateException {
      if (this.connected) {
         throw new IllegalStateException(nmText.getAlreadyConnected());
      }
   }

   public void connect() throws IOException {
      this.connSpec = new ConnectionSpec();
      this.connSpec.setHost(this.host);
      this.connSpec.setPort(this.port);
      this.connSpec.setUseSecure(this.isSecure);
      if (this.isSecure) {
         this.connSpec.setSecurePort(this.port);
         this.connSpec.setSSLSocketFactory(this.sslSocketFactory);
      }

      this.connSpec.setCallbackHandler(this);

      try {
         if (this.host == null) {
            throw new IllegalStateException(nmText.getHostNotSet());
         } else if (this.port <= 0) {
            throw new IllegalStateException(nmText.getInvalidPort(Integer.toString(this.port)));
         } else if (!VMMConnector.hasAdapter(this.vmmType, this.vmmAPIVersion, this.connSpec)) {
            throw new IllegalStateException(nmText.getInvalidVMMPlugin(this.vmmType, this.vmmAPIVersion));
         } else {
            this.vmm = VMMConnector.connect(this.vmmType, this.vmmAPIVersion, this.connSpec);
            this.vmm.getAllPools();
            this.connected = true;
         }
      } catch (Exception var2) {
         if (debug) {
            System.out.println("DEBUG: In VMMClient - exception during connect():");
            var2.printStackTrace();
         }

         throw new IOException(var2.getLocalizedMessage());
      }
   }

   public static SSLSocketFactory getSSLSocketFactory(KeyManager[] var0, TrustManager[] var1) {
      try {
         SSLContext var2 = SSLContext.getInstance("SSL");
         var2.init(var0, var1, new SecureRandom());
         SSLSocketFactory var3 = var2.getSocketFactory();
         return var3;
      } catch (GeneralSecurityException var4) {
         throw new IllegalStateException(var4.getLocalizedMessage());
      }
   }

   private static TrustManager[] getTrustManagers(String var0, char[] var1, String var2, String var3, String var4) throws IOException {
      TrustManagerFactory var5 = null;
      if (var0 == null) {
         return null;
      } else {
         if (debug) {
            System.out.println("DEBUG: trustKeystore=" + var0);
         }

         if (var3 == null) {
            var3 = "SunX509";
         }

         if (var2 == null) {
            var2 = "JKS";
         }

         if (var4 == null) {
            var4 = "SunJSSE";
         }

         try {
            var5 = TrustManagerFactory.getInstance(var3, var4);
         } catch (NoSuchAlgorithmException var9) {
            throw new IllegalStateException(nmText.msgErrorLoadKeyStoreNoSuchAlgorithmException(var0, var3, var9));
         } catch (NoSuchProviderException var10) {
            if (debug) {
               System.out.println("DEBUG: provider = " + var4);
               var10.printStackTrace();
            }

            throw new IllegalStateException(var10.getLocalizedMessage());
         }

         KeyStore var6 = getKeystore(var0, var1, var2);

         try {
            var5.init(var6);
         } catch (KeyStoreException var8) {
            if (debug) {
               System.out.println("DEBUG: failed to init keystore");
               var8.printStackTrace();
            }

            throw new IllegalStateException(var8.getLocalizedMessage());
         }

         TrustManager[] var7 = var5.getTrustManagers();
         if (debug) {
            System.out.println("DEBUG: Loaded trust managers");
         }

         return var7;
      }
   }

   private static KeyStore getKeystore(String var0, char[] var1, String var2) throws IOException {
      FileInputStream var3 = null;
      KeyStore var4 = null;
      if (var0 == null) {
         return null;
      } else {
         if (debug) {
            System.out.println("DEBUG: loading keystore from: " + var0);
         }

         try {
            try {
               var3 = new FileInputStream(var0);
            } catch (FileNotFoundException var18) {
               if (debug) {
                  System.out.println("DEBUG: can't find keystore: " + var0);
                  var18.printStackTrace();
               }

               throw var18;
            }

            try {
               var4 = KeyStore.getInstance(var2 == null ? "JKS" : var2);
            } catch (KeyStoreException var22) {
               if (debug) {
                  System.out.println("DEBUG: can't get keystore");
                  var22.printStackTrace();
               }

               throw new IOException(var22.getLocalizedMessage());
            }

            try {
               var4.load(var3, var1);
            } catch (NoSuchAlgorithmException var19) {
               if (debug) {
                  System.out.println("DEBUG: bad algorithm");
                  var19.printStackTrace();
               }

               throw new IllegalStateException(var19.getLocalizedMessage());
            } catch (CertificateException var20) {
               if (debug) {
                  System.out.println("DEBUG: problem with certificate");
                  var20.printStackTrace();
               }

               throw new IllegalStateException(var20.getLocalizedMessage());
            } catch (IOException var21) {
               if (debug) {
                  System.out.println("DEBUG: I/O error loading keystore");
                  var21.printStackTrace();
               }

               throw new IllegalStateException(var21.getLocalizedMessage());
            }
         } finally {
            try {
               if (var3 != null) {
                  var3.close();
               }
            } catch (IOException var17) {
            }

         }

         return var4;
      }
   }

   private SSLSocketFactory getSSLSocketFactory() throws IOException {
      Object var1 = null;
      TrustManager[] var2 = new TrustManager[1];
      if (KernelStatus.isServer()) {
         var1 = MBeanKeyStoreConfiguration.getInstance();
      } else {
         var1 = ClientKeyStoreConfiguration.getInstance();
      }

      KeyStoreInfo[] var3 = (new KeyStoreConfigurationHelper((KeyStoreConfiguration)var1)).getTrustKeyStores();
      if (var3.length > 0) {
         Vector var4 = new Vector();
         KeyStoreInfo[] var5 = var3;
         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            KeyStoreInfo var8 = var5[var7];
            TrustManager[] var9 = getTrustManagers(var8.getFileName(), var8.getPassPhrase(), (String)null, (String)null, (String)null);
            var4.addAll(Arrays.asList(var9));
         }

         var2 = (TrustManager[])var4.toArray(var2);
      }

      return getSSLSocketFactory((KeyManager[])null, var2);
   }

   public void done() throws IOException {
      this.quit();
   }

   public void executeScript(String var1, long var2) {
      throw new IllegalStateException(nmText.msgErrorVmmNoScripting());
   }

   private VirtualMachine findVM(String var1) throws IOException {
      if (debug) {
         System.out.println("DEBUG: VMMClient.findVM( " + var1 + " )");
      }

      VirtualMachine var2 = null;
      List var3 = this.getServerPools();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2 = this.getVM(var1, var5);
         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public List<VirtualMachine> getAllVMs(ResourcePool var1) throws IOException {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = null;
         ArrayList var3 = new ArrayList();
         VirtualMachine var4 = null;
         this.checkConnected(false);

         try {
            var2 = var1.getAllVMNames();
            String[] var5 = var2;
            int var6 = var2.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               var4 = var1.lookupVM(var8);
               if (var4 != null) {
                  var3.add(var4);
               }
            }

            return var3;
         } catch (Exception var9) {
            throw new IOException(var9.getLocalizedMessage());
         }
      }
   }

   public void getLog(Writer var1) throws IOException {
      throw new NMException(nmText.msgErrorVmCommandNotSupported());
   }

   public void getNMLog(Writer var1) throws IOException {
      throw new NMException(nmText.msgErrorVmCommandNotSupported());
   }

   private List<String> getServerPools() throws IOException {
      this.checkConnected(false);
      String[] var1 = null;

      try {
         var1 = this.vmm.getAllPools();
      } catch (Exception var3) {
         throw new IOException(var3.getLocalizedMessage());
      }

      return Arrays.asList(var1);
   }

   public String getState(int var1) throws IOException {
      this.checkConnected(true);

      try {
         return this.getStatus(this.getVmName()).toUpperCase();
      } catch (Exception var3) {
         throw new IOException(var3.getLocalizedMessage());
      }
   }

   public String getStates(int var1) throws NMException, IOException {
      this.checkConnected(true);
      throw new NMException(nmText.msgErrorVmCommandNotSupported());
   }

   private String getStatus(String var1) throws IOException {
      VirtualMachine var2 = this.findVM(var1);
      return var2 != null ? var2.getState().toString() : VMState.Unknown.toString();
   }

   public String getVersion() throws IOException {
      this.checkConnected(false);
      AdapterInfo var1 = this.vmm.getAdapterInfo();
      return var1.getVmmType() + "_" + var1.getVmmApiVersion() + "_" + var1.getImplVersion();
   }

   public VirtualMachine getVM(String var1, ResourcePool var2) throws IOException {
      if (var1 != null && var2 != null) {
         return null;
      } else {
         this.checkConnected(false);

         try {
            return var2.lookupVM(var1);
         } catch (Exception var4) {
            throw new IOException(var4.getLocalizedMessage());
         }
      }
   }

   public VirtualMachine getVM(String var1, String var2) throws IOException {
      ResourcePool var3 = null;
      if (var1 != null && var2 != null) {
         this.checkConnected(false);

         try {
            var3 = this.vmm.lookupPool(var2);
            return var3 != null ? var3.lookupVM(var1) : null;
         } catch (Exception var5) {
            throw new IOException(var5.getLocalizedMessage());
         }
      } else {
         return null;
      }
   }

   private String getVmName() {
      return this.virtualMachineName != null ? this.virtualMachineName : this.serverName;
   }

   public void handle(Callback[] var1) throws IOException, UnsupportedCallbackException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] instanceof NameCallback) {
            NameCallback var3 = (NameCallback)var1[var2];
            if (this.nmUser == null) {
               throw new IOException("user name is not specified.");
            }

            var3.setName(new String(this.nmUser, "UTF-8"));
         } else {
            if (!(var1[var2] instanceof PasswordCallback)) {
               throw new UnsupportedCallbackException(var1[var2], "Unrecognized Callback");
            }

            if (this.nmPass == null) {
               throw new IOException("password is not specified.");
            }

            PasswordCallback var4 = (PasswordCallback)var1[var2];
            var4.setPassword((new String(this.nmPass, "UTF-8")).toCharArray());
         }
      }

   }

   public boolean isNmPassSet() {
      return this.nmPass != null;
   }

   public boolean isNmUserSet() {
      return this.nmUser != null;
   }

   public void kill() throws IOException {
      this.checkConnected(true);

      try {
         this.stop(this.getVmName());
      } catch (Exception var2) {
         throw new IOException(var2.getLocalizedMessage());
      }
   }

   public void quit() throws IOException {
      if (this.vmm != null) {
         this.vmm.disconnect();
      }

      this.connected = false;
      this.connSpec = null;
      this.host = null;
      this.nmUser = null;
      this.port = 0;
      this.nmPass = null;
      this.vmm = null;
   }

   public void setVirtualMachineName(String var1) {
      this.virtualMachineName = var1;
   }

   public void start() throws IOException {
      this.checkConnected(true);
      this.startWlsveVM(this.getVmName(), this.serverName);
   }

   public void start(Properties var1) throws IOException {
      if (KernelStatus.isServer()) {
         this.start();
      } else {
         throw new NMException(nmText.msgErrorVmCommandNotSupported());
      }
   }

   private void startVM(VirtualMachine var1, String var2) throws IOException {
      this.checkConnected(false);
      String var3 = var1.getName();
      if (var1.getState().equals(VMState.Running)) {
         throw new NMException(nmText.getVmAlreadyRunning(var2));
      } else {
         if (debug) {
            System.out.println("DEBUG: Powering on VM " + var3 + " for server " + var2);
         }

         try {
            var1.start(0);
         } catch (Exception var5) {
            if (debug) {
               System.out.println("DEBUG: Exception while attempting to power on " + var3 + ":" + var5);
               var5.printStackTrace();
            }

            throw new NMException(var5.getLocalizedMessage());
         }
      }
   }

   private void startWlsveVM(String var1, String var2) throws IOException {
      VirtualMachine var3 = this.findVM(var1);
      if (var3 != null) {
         this.startVM(var3, var2);
      } else {
         throw new IOException(nmText.msgErrorVmForServerNotFound(var1, var2));
      }
   }

   private void stop(String var1) throws IOException {
      VirtualMachine var2 = this.findVM(var1);
      if (var2 != null) {
         this.stopVM(var2, this.serverName);
      } else {
         throw new NMException(nmText.msgErrorVmForServerNotFound(var1, this.serverName));
      }
   }

   private void stopVM(VirtualMachine var1, String var2) throws IOException {
      if (var1 != null) {
         this.checkConnected(false);
         String var3 = var1.getName();
         if (!var1.getState().equals(VMState.Running)) {
            throw new NMException(nmText.getVmStopped(var2));
         } else {
            if (debug) {
               System.out.println("DEBUG: Powering off " + var3);
            }

            try {
               var1.shutdown(0);
            } catch (Exception var5) {
               if (debug) {
                  System.out.println("Couldn't shutdown VM: " + var5);
                  var5.printStackTrace();
               }

               throw new NMException(var5.getLocalizedMessage());
            }

            if (debug) {
               System.out.println("DEBUG: " + VMState.PoweredOff);
            }

         }
      }
   }

   public void updateServerProps(Properties var1) throws NMException {
      throw new NMException(nmText.msgErrorVmCommandNotSupported());
   }

   public synchronized void setVmmAdapterName(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidVmmAdapter());
      this.checkNotConnected();
      this.vmmType = var1;
   }

   public synchronized void setVmmAdapterVersion(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidVmmAdapterVersion());
      this.checkNotConnected();
      this.vmmAPIVersion = var1;
   }
}
