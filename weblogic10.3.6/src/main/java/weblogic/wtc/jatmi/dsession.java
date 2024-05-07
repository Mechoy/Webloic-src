package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCSecurityManager;
import com.bea.core.jatmi.intf.TCAppKey;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.transaction.xa.Xid;
import weblogic.iiop.CodeSet;
import weblogic.iiop.IOR;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.SSLFilter;
import weblogic.socket.utils.SDPSocketUtils;
import weblogic.utils.Debug;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.gwt.MethodParameters;
import weblogic.wtc.gwt.TimerEventManager;
import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoCorbaConnection;
import weblogic.wtc.gwt.WTCService;

public class dsession implements CorbaAtmi {
   protected String lpwd;
   protected String rpwd;
   protected String desired_name;
   protected String dom_target_name = null;
   protected atn gssatn = null;
   protected int dom_protocol;
   protected int security_type;
   protected tplle myLLE;
   protected boolean interoperate = false;
   protected TCAppKey myAppKey = null;
   protected String myAppKeySel = null;
   protected boolean myAllowAnon;
   protected int myDfltAppKey;
   protected String myUidKw = null;
   protected String myGidKw = null;
   protected String myCustomAppKeyClass = null;
   protected String myCustomAppKeyClassParam = null;
   private int acl_policy = 0;
   private int cred_policy = 0;
   private String tpusrfile;
   private int eflags;
   private int elevel;
   private InetAddress[] domaddr_ip;
   private int[] domaddr_port;
   private String remote_domain_id;
   private String local_domain_name;
   private Socket dom_socket;
   private DataOutputStream dom_ostream;
   private DataInputStream dom_istream;
   private TuxedoMuxableSocket ein;
   private TpeOut eout;
   private TpeIn rein;
   private int auth_type = -1;
   private boolean is_term = false;
   private boolean is_connected = false;
   private int reqid = 0;
   private int convid = 0;
   private int cmplimit = Integer.MAX_VALUE;
   private rdsession rcv_place;
   private InvokeSvc invoker;
   private boolean connector = true;
   private int uid;
   private Timer myTimeService;
   private Timer asyncTimeService;
   private TuxRply myRplyObj;
   private long myBlockTime;
   private TuxXidRply myXidRplyObj;
   private OnTerm myTerminationHandler;
   private ClientInfo myCltInfo = null;
   private BetaFeatures useBetaFeatures = null;
   private HashMap rmiCallList = null;
   private Object lockObject = new Object();
   private boolean cachedUR = false;
   private int tmsndprio = 50;
   private int kas = -2;
   private int kaws = -1;
   private int ka = -2;
   private int kaw = -1;
   protected int rdom_features = 0;
   private long lastRecvTime = 0L;
   private int kaState = -1;
   private Lock myLock = new ReentrantLock();
   private KeepAliveTask kaTask = null;
   private long kaExpTime = 0L;
   private long kawExpTime = 0L;
   private boolean useSSL = false;
   private String identityKeyStoreType;
   private String identityKeyStore;
   private String identityKeyStorePassphrase;
   private String identityKeyAlias;
   private String identityKeyPassphrase;
   private String trustKeyStoreType;
   private String trustKeyStore;
   private String trustKeyStorePassphrase;
   private int minEncryptBits = 0;
   private int maxEncryptBits = 256;
   private boolean[] useSDP;
   public static final int KA_NONE = -1;
   public static final int KA_INIT = 0;
   public static final int KA_ACTIVE = 1;
   public static final int KA_WAIT = 2;
   public static final int KA_SCHEDULED = 3;
   public static final int KA_SHUTDOWN = 4;
   public static final int KA_DISABLED = 5;
   public static final int KA_PENDING = 6;
   public static final int DMACL_LOCAL = 0;
   public static final int DMACL_GLOBAL = 1;
   public static final int TM_PRIORANGE = 100;
   public static final int TM_SENDBASE = 536870912;
   private static final String ANONYMOUS_USER = "anonymous";
   public static final String SEL_TPUSRFILE = "TpUsrFile";
   public static final String SEL_LDAP = "LDAP";
   private static boolean useCORBATimeout = Boolean.getBoolean("weblogic.wtc.corba.Timeout");
   private int flags = 0;
   private int char_codeset;
   private int wchar_codeset;
   private IOR remoteCodeBase;
   private int minorVersion;

   public dsession(Timer var1, InetAddress var2, int var3, int var4, TuxXidRply var5, BetaFeatures var6) {
      this.char_codeset = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_codeset = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.remoteCodeBase = null;
      this.minorVersion = 2;
      this.domaddr_ip = new InetAddress[1];
      this.domaddr_port = new int[1];
      this.domaddr_ip[0] = var2;
      this.domaddr_port[0] = var3;
      this.uid = var4;
      this.myTimeService = var1;
      this.myRplyObj = new TuxRply();
      this.myXidRplyObj = var5;
      this.useBetaFeatures = var6;
      DomainRegistry.addDomainSession(this);
   }

   public dsession(Timer var1, InetAddress var2, int var3, atn var4, int var5, TuxXidRply var6, BetaFeatures var7) {
      this.char_codeset = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_codeset = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.remoteCodeBase = null;
      this.minorVersion = 2;
      this.domaddr_ip = new InetAddress[1];
      this.domaddr_port = new int[1];
      this.domaddr_ip[0] = var2;
      this.domaddr_port[0] = var3;
      this.gssatn = var4;
      this.uid = var5;
      this.myTimeService = var1;
      this.myRplyObj = new TuxRply();
      this.myXidRplyObj = var6;
      this.useBetaFeatures = var7;
      DomainRegistry.addDomainSession(this);
   }

   public dsession(Timer var1, InetAddress[] var2, int[] var3, atn var4, InvokeSvc var5, int var6, TuxXidRply var7, BetaFeatures var8) {
      this.char_codeset = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_codeset = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.remoteCodeBase = null;
      this.minorVersion = 2;
      this.domaddr_ip = var2;
      this.domaddr_port = var3;
      this.gssatn = var4;
      this.invoker = var5;
      this.uid = var6;
      this.myTimeService = var1;
      this.myRplyObj = new TuxRply();
      this.myXidRplyObj = var7;
      this.useBetaFeatures = var8;
      DomainRegistry.addDomainSession(this);
   }

   public dsession(Timer var1, InetAddress var2, int var3, atn var4, InvokeSvc var5, int var6, TuxXidRply var7, BetaFeatures var8) {
      this.char_codeset = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_codeset = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.remoteCodeBase = null;
      this.minorVersion = 2;
      this.domaddr_ip = new InetAddress[1];
      this.domaddr_port = new int[1];
      this.domaddr_ip[0] = var2;
      this.domaddr_port[0] = var3;
      this.gssatn = var4;
      this.invoker = var5;
      this.uid = var6;
      this.myTimeService = var1;
      this.myRplyObj = new TuxRply();
      this.myXidRplyObj = var7;
      this.useBetaFeatures = var8;
      DomainRegistry.addDomainSession(this);
   }

   public dsession(Timer var1, Socket var2, atn var3, InvokeSvc var4, int var5, TuxXidRply var6, boolean var7, BetaFeatures var8) throws IOException {
      this.char_codeset = CodeSet.DEFAULT_CHAR_NATIVE_CODE_SET;
      this.wchar_codeset = CodeSet.DEFAULT_WCHAR_NATIVE_CODE_SET;
      this.remoteCodeBase = null;
      this.minorVersion = 2;
      this.dom_socket = var2;

      try {
         this.ein = new TuxedoMuxableSocket(this.dom_socket, var7);
      } catch (SocketException var10) {
         throw new IOException("ERROR: Could not create Tuxedo Muxable Socket: " + var10);
      }

      this.useSSL = var7;
      this.eout = new TpeOut(this.dom_socket.getOutputStream());
      this.dom_ostream = new DataOutputStream(this.eout);
      this.rein = new TpeIn(this.dom_socket.getInputStream());
      this.dom_istream = new DataInputStream(this.rein);
      this.gssatn = var3;
      this.invoker = var4;
      this.connector = false;
      this.uid = var5;
      this.myTimeService = var1;
      this.myRplyObj = new TuxRply();
      this.myXidRplyObj = var6;
      this.useBetaFeatures = var8;
      DomainRegistry.addDomainSession(this);
   }

   public InetAddress getIAddress() {
      return this.dom_socket.getInetAddress();
   }

   public TuxXidRply getUnknownRplyObj() {
      return this.myXidRplyObj;
   }

   public void set_BlockTime(long var1) {
      this.myBlockTime = var1;
      if (this.rcv_place != null) {
         this.rcv_place.set_BlockTime(var1);
      }

   }

   public long get_BlockTime() {
      return this.myBlockTime;
   }

   public Timer get_TimeService() {
      return this.myTimeService;
   }

   public void set_invoker(InvokeSvc var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/set_invoker/" + this.invoker);
      }

      if (this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/set_invoker/30/");
         }

         throw new TPException(9, "Can not set invoker once connected");
      } else {
         this.invoker = var1;
         if (var2) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/set_invoker/10");
         }

      }
   }

   public InvokeSvc get_invoker() {
      return this.invoker;
   }

   public void set_dom_target_name(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/set_dom_target_name/" + var1);
      }

      this.dom_target_name = new String(var1);
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/set_dom_target_name/10/");
      }

   }

   public String getTargetName() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getTargetName/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getTargetName/10/" + this.dom_target_name);
      }

      return this.dom_target_name;
   }

   public void set_compression_threshold(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/set_compression_threshold/" + var1);
      }

      if (this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/set_compression_threshold/10/");
         }

         throw new TPException(9, "Can not set compression threshold once connected");
      } else {
         this.cmplimit = var1;
         if (var2) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/set_compression_threshold/20/");
         }

      }
   }

   public int getCompressionThreshold() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getCompressionThreshold/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getCompressionThreshold/10/" + this.cmplimit);
      }

      return this.cmplimit;
   }

   public void set_sess_sec(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/set_sess_sec/" + var1);
      }

      if (this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/set_sess_sec/10/");
         }

         throw new TPException(9, "Can not set security type once connected");
      } else {
         if (var1.equals("NONE")) {
            this.security_type = 0;
         } else if (var1.equals("DM_PW")) {
            this.security_type = 2;
         } else {
            if (!var1.equals("APP_PW")) {
               if (var2) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/set_sess_sec/20/");
               }

               throw new TPException(9, "Invalid security type(" + var1 + ") specified");
            }

            this.security_type = 1;
         }

         if (var2) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/set_sess_sec/30/");
         }

      }
   }

   public int get_sess_sec() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/get_sess_sec/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/get_sess_sec/10/" + this.security_type);
      }

      return this.security_type;
   }

   public void setKeepAlive(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setKeepAlive/" + var1);
      }

      if (var1 >= -1) {
         this.myLock.lock();
         if ((this.ka = var1) != 0) {
            if (this.ka % 1000 != 0) {
               this.kas = var1 / 1000 + 1;
            } else {
               this.kas = var1 / 1000;
            }

            if (this.kaw == -1) {
               this.kaws = 0;
            }

            if (this.is_connected && this.kaState == -1) {
               this.startKACountDown();
            }
         } else {
            this.kas = 0;
            if (this.kaState == 3) {
               this.kaTask.cancel();
            }

            this.kaState = -1;
         }

         this.myLock.unlock();
      }

      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setKeepAlive/10/");
      }

   }

   public int getKeepAlive() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getKeepAlive/");
      }

      if (var1) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/getKeepAlive/10/" + this.ka);
      }

      return this.ka;
   }

   public void setKeepAliveWait(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setKeepAliveWait/" + var1);
      }

      if (var1 >= 0) {
         this.kaw = var1;
         if (this.kaw % 1000 != 0) {
            this.kaws = var1 / 1000 + 1;
         } else {
            this.kaws = var1 / 1000;
         }
      }

      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setKeepAliveWait/10/");
      }

   }

   public int getKeepAliveWait() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getKeepAliveWait/");
      }

      if (var1) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/getKeepAliveWait/10/" + this.kaw);
      }

      return this.kaw;
   }

   public void setLocalPassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setLocalPasswd/" + var1);
      }

      this.lpwd = new String(var1);
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setLocalPasswd/10/");
      }

   }

   public void setSessionFeatures(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setSessionFeatures/" + var1);
      }

      this.rdom_features = var1 & 27;
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setSessionFeatures/10");
      }

   }

   public int getSessionFeatures() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getSessionFeatures/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getSessionFeatures/10/" + this.rdom_features);
      }

      return this.rdom_features;
   }

   public void dmqDecision() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/dmqDecision/");
      }

      if ((this.rdom_features & 1) == 0) {
         if (this.ka > 0) {
            WTCLogger.logWarnDisableKeepAlive(this.local_domain_name, this.remote_domain_id);
         }

         this.myLock.lock();
         this.kaState = 5;
         this.myLock.unlock();
      } else {
         this.myLock.lock();
         if (this.kaState == 5) {
            this.kaState = -1;
         }

         this.myLock.unlock();
         if (this.ka > 0) {
            this.startKACountDown();
         }
      }

      this.asyncTimeService = WTCService.getAsyncTimerService();
      if (var1) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/dmqDecision/10");
      }

   }

   public void setRemotePassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setRemotePasswd/" + var1);
      }

      this.rpwd = new String(var1);
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setRemotePasswd/10/");
      }

   }

   public void setApplicationPassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setApplicationPassword/" + var1);
      }

      this.lpwd = new String(var1);
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setApplicationPassword/10/");
      }

   }

   public String getLocalPassword() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getLocalPassword/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getLocalPassword/10/" + this.lpwd);
      }

      return this.lpwd;
   }

   public String getRemotePassword() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getRemotePassword/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getRemotePassword/10/" + this.rpwd);
      }

      return this.rpwd;
   }

   public String getApplicationPassword() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getApplicationPassword/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getApplicationPassword/10/" + this.lpwd);
      }

      return this.lpwd;
   }

   public void setDesiredName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setDesiredName/" + var1);
      }

      if (var1 != null) {
         this.desired_name = new String(var1);
      }

      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setDesiredName/10/");
      }

   }

   public String getDesiredName() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getDesiredName/");
         ntrace.doTrace("]/dsession(" + this.uid + ")/getDesiredName/10/" + this.desired_name);
      }

      return this.desired_name;
   }

   public void setEncryptionFlags(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setEncryptionFlags/" + var1);
      }

      this.eflags = var1;
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setEncryptionFlags/10");
      }

   }

   public void setELevel(int var1) {
      this.elevel = var1;
   }

   public int getELevel() {
      return this.elevel;
   }

   public void setLLE() {
      if (this.elevel != 0 && this.elevel != 1) {
         this.rein.setElevel(this.elevel);
         this.rein.setLLE(this.myLLE);
         this.ein.setElevel(this.elevel);
         this.ein.setLLE(this.myLLE);
         this.eout.setElevel(this.elevel);
         this.eout.setLLE(this.myLLE);
      } else if (this.myLLE != null) {
         this.myLLE = null;
      }

   }

   public void setAclPolicy(String var1) {
      if (var1 != null && var1.equals("GLOBAL")) {
         this.acl_policy = 1;
      } else {
         this.acl_policy = 0;
      }

   }

   public void setCredentialPolicy(String var1) {
      if (var1 != null && var1.equals("GLOBAL")) {
         this.cred_policy = 1;
      } else {
         this.cred_policy = 0;
      }

   }

   public int getAclPolicy() {
      return this.acl_policy;
   }

   public int getCredentialPolicy() {
      return this.cred_policy;
   }

   public void setTpUserFile(String var1) {
      if (var1 != null) {
         this.tpusrfile = new String(var1);
      } else {
         this.tpusrfile = null;
      }

   }

   public String getTpUserFile() {
      return this.tpusrfile;
   }

   public int setUpTuxedoAAA() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setUpTuxedoAAA/");
      }

      if (this.myAppKey == null && this.cred_policy == 1 && this.dom_protocol >= 15) {
         String var2 = null;
         String var3 = null;
         if (this.myAppKeySel != null && this.myAppKeySel.compareToIgnoreCase("TpUsrFile") != 0) {
            if (this.myAppKeySel.compareToIgnoreCase("LDAP") == 0) {
               var2 = this.myUidKw;
               var3 = this.myGidKw;
            } else {
               var2 = this.myCustomAppKeyClass;
               var3 = this.myCustomAppKeyClassParam;
            }
         } else {
            var2 = this.tpusrfile;
            if (var2 == null) {
               var2 = WTCService.getGlobalTpUsrFile();
            }
         }

         this.myAppKey = TCSecurityManager.getSecurityService().getAppKeyGenerator(this.myAppKeySel, var2, var3, this.myAllowAnon, this.myDfltAppKey);
         if (this.myAppKey != null) {
            this.cachedUR = this.myAppKey.isCached();
         }
      }

      if (var1) {
         ntrace.doTrace("]/dsession/setUpTuxedoAAA/10/return 0");
      }

      return 0;
   }

   public void setInteroperate(boolean var1) {
      this.interoperate = var1;
   }

   public boolean getInteroperate() {
      return this.interoperate;
   }

   public boolean getIsTerminated() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/getIsTerminated/ldom=" + this.local_domain_name + " rdom=" + this.remote_domain_id);
         ntrace.doTrace("]/dsession(" + this.uid + ")/getIsTerminated/" + this.is_term);
      }

      return this.is_term;
   }

   public void setIsTerminated() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.local_domain_name + ", " + this.remote_domain_id + ")/setIsTerminated");
      }

      if (!this.is_term) {
         this.terminateTDomainSession(false);
      }

      if (var1) {
         ntrace.doTrace("]/dsession/setIsTerminated(10)/done");
      }

   }

   public void doLocalTerminate() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.local_domain_name + ", " + this.remote_domain_id + ")/doLocalTerminate");
      }

      if (!this.is_term) {
         this.terminateTDomainSession(true);
      }

      if (var1) {
         ntrace.doTrace("]/dsession/doLocalTerminate(10)/done");
      }

   }

   public boolean get_is_connected() {
      return this.is_connected;
   }

   public void set_is_connected(boolean var1) {
      this.is_connected = var1;
   }

   public String get_local_domain_name() {
      return this.local_domain_name;
   }

   public void set_local_domain_name(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/set_local_domain_name/" + var1);
      }

      this.local_domain_name = var1;
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/set_local_domain_name/10");
      }

   }

   public String getRemoteDomainId() {
      return this.remote_domain_id;
   }

   public void setRemoteDomainId(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/setRemoteDomainId/" + var1);
      }

      this.remote_domain_id = var1;
      if (var2) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/setRemoteDomainId/10");
      }

   }

   public void setAppKey(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setAppKey/" + var1);
      }

      this.myAppKeySel = var1;
      if (var2) {
         ntrace.doTrace("]/dsession/setAppKey/10/");
      }

   }

   public void setDfltAppKey(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setDfltAppKey/" + var1);
      }

      this.myDfltAppKey = var1;
      if (var2) {
         ntrace.doTrace("]/dsession/setDfltAppKey/10/");
      }

   }

   public void setAllowAnonymous(boolean var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setAllowAnonymous/" + var1);
      }

      this.myAllowAnon = var1;
      if (var2) {
         ntrace.doTrace("]/dsession/setAllowAnonymous/10/");
      }

   }

   public void setUidKw(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setUidKw/" + var1);
      }

      this.myUidKw = var1.trim();
      if (var2) {
         ntrace.doTrace("]/dsession/setUidKw/10/");
      }

   }

   public void setGidKw(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setGidKw/" + var1);
      }

      this.myGidKw = var1.trim();
      if (var2) {
         ntrace.doTrace("]/dsession/setGidKw/10/");
      }

   }

   public void setCustomAppKeyClass(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setCustomAppKeyClass/" + var1);
      }

      this.myCustomAppKeyClass = var1;
      if (var2) {
         ntrace.doTrace("]/dsession/setCustomAppKeyClass/10/");
      }

   }

   public void setCustomAppKeyClassParam(String var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession/setCustomAppKeyClassParam/" + var1);
      }

      this.myCustomAppKeyClassParam = var1;
      if (var2) {
         ntrace.doTrace("]/dsession/setCustomAppKeyClassParam/10/");
      }

   }

   public int tpchkauth() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpchkauth/");
      }

      if (this.is_term) {
         if (var1) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpchkauth/10/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else {
         if (var1) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/tpchkauth/20/" + this.auth_type);
         }

         return this.auth_type;
      }
   }

   public void set_authtype(int var1) {
      this.auth_type = var1;
   }

   public boolean get_is_connector() {
      return this.connector;
   }

   public DataOutputStream get_output_stream() {
      return this.dom_ostream;
   }

   public DataInputStream get_input_stream() {
      return this.dom_istream;
   }

   public void setInProtocol(int var1) {
      this.dom_protocol = var1;
      this.ein.setProtocol(var1);
      this.rein.setProtocol(var1);
   }

   public void setOutProtocol(int var1) {
      this.eout.setProtocol(var1);
   }

   public void set_rcv_place(rdsession var1) {
      this.rcv_place = var1;
      if (this.useSSL) {
         try {
            SSLIOContext var2 = SSLIOContextTable.findContext((SSLSocket)this.dom_socket);
            SSLFilter var3 = null;
            if (var2 != null) {
               var3 = (SSLFilter)var2.getFilter();
               this.ein.setSocketFilter(var3);
            }

            if (var3 != null) {
               var3.setDelegate(this.ein);
               var3.activate();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      this.ein.setRecvSession(this.rcv_place);
   }

   public rdsession get_rcv_place() {
      return this.rcv_place;
   }

   public void setUseSSL(boolean var1) {
      this.useSSL = var1;
   }

   public boolean getUseSSL() {
      return this.useSSL;
   }

   public void setIdentityKeyStoreType(String var1) {
      this.identityKeyStoreType = var1;
   }

   public void setIdentityKeyStore(String var1) {
      this.identityKeyStore = var1;
   }

   public String getIdentityKeyStore() {
      return this.identityKeyStore;
   }

   public void setIdentityKeyStorePassphrase(String var1) {
      this.identityKeyStorePassphrase = var1;
   }

   public void setIdentityKeyAlias(String var1) {
      this.identityKeyAlias = var1;
   }

   public void setIdentityKeyPassphrase(String var1) {
      this.identityKeyPassphrase = var1;
   }

   public void setTrustKeyStoreType(String var1) {
      this.trustKeyStoreType = var1;
   }

   public void setTrustKeyStore(String var1) {
      this.trustKeyStore = var1;
   }

   public void setTrustKeyStorePassphrase(String var1) {
      this.trustKeyStorePassphrase = var1;
   }

   public void setMinEncryptBits(int var1) {
      this.minEncryptBits = var1;
   }

   public void setMaxEncryptBits(int var1) {
      this.maxEncryptBits = var1;
   }

   public final int getMinorVersion() {
      return this.minorVersion;
   }

   public final void setMinorVersion(int var1) {
      this.minorVersion = var1;
   }

   public final void setCodeSets(int var1, int var2) {
      this.char_codeset = var1;
      this.wchar_codeset = var2;
   }

   public final int getWcharCodeSet() {
      return this.wchar_codeset;
   }

   public final int getCharCodeSet() {
      return this.char_codeset;
   }

   public void setFlag(int var1) {
      this.flags |= var1;
   }

   public boolean getFlag(int var1) {
      return (this.flags & var1) != 0;
   }

   public final IOR getRemoteCodeBase() {
      return this.remoteCodeBase;
   }

   public final void setRemoteCodeBase(IOR var1) {
      this.remoteCodeBase = var1;
   }

   public void setUseSDP(boolean[] var1) {
      this.useSDP = var1;
   }

   private void do_connect(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/do_connect/" + var1);
      }

      byte var4 = 0;
      boolean var8 = true;
      byte[] var9 = null;
      byte[] var10 = null;
      tcm var14 = null;
      atncredtdom var16 = null;
      atnctxtdom var17 = null;
      TypedCArray var18 = null;
      int var3;
      char[] var11;
      if (var1 != null) {
         if (var1.length() > 30) {
            if (var2) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/10/");
            }

            throw new TPException(4, "domainid must be less than 30 characters");
         }

         var11 = var1.toCharArray();
      } else {
         var11 = new char[32];

         for(var3 = 0; var3 < 32; ++var3) {
            var11[var3] = 0;
         }
      }

      if (this.is_term) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if (this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/30/");
         }

         throw new TPException(9, "Domain session is already connected");
      } else {
         tfmh var12 = new tfmh(16, (tcm)null, 1);
         TdomTcb var13 = this.alloc_TDOM(14, 0, (String)null);
         var12.tdom = new tcm((short)7, var13);
         boolean var20 = false;

         for(var3 = 0; var3 < this.domaddr_ip.length && !var20; ++var3) {
            if (this.domaddr_ip[var3] == null) {
               if (var3 >= this.domaddr_ip.length - 1) {
                  WTCLogger.logWarnNoMoreValidRemoteAddress(this.local_domain_name, this.remote_domain_id);
                  break;
               }
            } else {
               try {
                  if (this.useSDP[var3] && this.useSSL) {
                     this.useSSL = false;
                     WTCLogger.logWarnIgnoreSSLwithSDP(var1);
                  }

                  if (this.useSSL) {
                     String var21 = this.identityKeyAlias;
                     if (var21 == null && (var21 = this.desired_name) == null) {
                        var21 = this.local_domain_name;
                     }

                     TuxedoSSLSocketFactory var22 = new TuxedoSSLSocketFactory(this.identityKeyStoreType, this.identityKeyStore, this.identityKeyStorePassphrase, var21, this.identityKeyPassphrase, this.trustKeyStoreType, this.trustKeyStore, this.trustKeyStorePassphrase);
                     this.dom_socket = var22.createSocket(this.domaddr_ip[var3], this.domaddr_port[var3]);
                     String[] var23 = new String[]{"TLSv1"};
                     ((SSLSocket)this.dom_socket).setEnabledProtocols(var23);
                     ((SSLSocket)this.dom_socket).setEnabledCipherSuites(TuxedoSSLSocketFactory.getCiphers(this.minEncryptBits, this.maxEncryptBits));
                  } else if (!this.useSDP[var3]) {
                     this.dom_socket = new Socket();
                     this.dom_socket.connect(new InetSocketAddress(this.domaddr_ip[var3], this.domaddr_port[var3]), (int)this.myBlockTime);
                  } else {
                     Class var36 = Class.forName("com.oracle.net.Sdp");
                     SDPSocketUtils.ensureEnvironment();
                     Method var37 = var36.getDeclaredMethod("openSocket", (Class[])null);
                     this.dom_socket = (Socket)var37.invoke((Object)null, (Object[])null);
                     this.dom_socket.connect(new InetSocketAddress(this.domaddr_ip[var3], this.domaddr_port[var3]), (int)this.myBlockTime);
                  }

                  var20 = true;
               } catch (ClassNotFoundException var33) {
                  WTCLogger.logErrorSdpClassNotFound();
               } catch (Exception var34) {
                  if (var3 < this.domaddr_ip.length - 1) {
                     WTCLogger.logInfoTryNextAddress(this.domaddr_ip[var3].getHostName(), this.domaddr_port[var3]);
                  } else {
                     WTCLogger.logWarnNoMoreAddressToTry(this.domaddr_ip[var3].getHostName(), this.domaddr_port[var3]);
                  }
               }
            }
         }

         if (!var20) {
            if (var2) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/35/exception");
            }

            throw new TPException(12, "Unable to create connection from local domain <" + this.local_domain_name + "> to remote TDomain <" + this.remote_domain_id + ">!");
         } else {
            try {
               this.ein = new TuxedoMuxableSocket(this.dom_socket, this.useSSL);
               if (this.useSSL) {
                  SSLIOContext var38 = SSLIOContextTable.findContext((SSLSocket)this.dom_socket);
                  SSLFilter var39 = null;
                  if (var38 != null) {
                     var39 = (SSLFilter)var38.getFilter();
                     this.ein.setSocketFilter(var39);
                  }

                  if (var2) {
                     ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/Performing SSL Handshake");
                  }

                  ((SSLSocket)this.dom_socket).addHandshakeCompletedListener(new MyListener());
                  ((SSLSocket)this.dom_socket).startHandshake();
                  if (var39 != null) {
                     var39.setDelegate(this.ein);
                     var39.activate();
                     this.eout = new TpeOut(this.dom_socket.getOutputStream());
                     this.dom_ostream = new DataOutputStream(this.eout);
                     this.rein = new TpeIn(this.ein.getInputStream());
                     this.dom_istream = new DataInputStream(this.rein);
                  }
               } else {
                  this.eout = new TpeOut(this.dom_socket.getOutputStream());
                  this.dom_ostream = new DataOutputStream(this.eout);
                  this.rein = new TpeIn(this.dom_socket.getInputStream());
                  this.dom_istream = new DataInputStream(this.rein);
               }

               this.local_domain_name = var1;
               var13.set_lle_flags(this.eflags);
               if (var12.write_dom_65_tfmh(this.dom_ostream, var1, 10, 134217727) != 0) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/40/");
                  }

                  throw new TPException(9, "Could not get authorization parameters from remote domain");
               }

               if (this.useSSL) {
                  if (var2) {
                     ntrace.doTrace("/dsession/(" + this.uid + ")/do_connect/SSL connection - waiting to receive data");
                  }

                  synchronized(this.ein) {
                     while(this.ein.getBufferOffset() == 0) {
                        try {
                           this.ein.wait();
                        } catch (InterruptedException var25) {
                        }
                     }
                  }
               }

               if (var12.read_dom_65_tfmh(this.dom_istream, 10) != 0) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/50/");
                  }

                  throw new TPException(4, "Could not read message from remote domain");
               }

               if (var13.get_opcode() != 15) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/50/");
                  }

                  throw new TPException(4, "Invalid opcode");
               }

               if (var13.get_security_type() != this.security_type) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/60/");
                  }

                  throw new TPException(4, "Remote Security level(" + var13.get_security_type() + ") does not match local security level(" + this.security_type + ")");
               }

               this.rdom_features = var13.getFeaturesSupported() & 27;
               if (var2) {
                  ntrace.doTrace("Remote gateway support features = " + this.rdom_features);
               }

               this.dom_protocol = var13.get_dom_protocol();
               this.ein.setProtocol(this.dom_protocol);
               this.rein.setProtocol(this.dom_protocol);
               this.eout.setProtocol(this.dom_protocol);
               if (var2) {
                  ntrace.doTrace(" /dsession(" + this.uid + ")/do_connect/dom_protocol " + this.dom_protocol);
               }

               int var40 = this.dom_protocol & 31;
               if ((var40 < 13 || var40 == 14) && (this.dom_protocol & 2147483616 & 16) == 0) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/70/");
                  }

                  throw new TPException(4, "ERROR: Protocol level " + this.dom_protocol + " is not supported!");
               }

               if (var40 == 13 && !this.interoperate) {
                  if (var2) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/65/");
                  }

                  throw new TPException(12, "Use Interoperate option to interoperate with sites older than 7.1");
               }

               int var19 = var13.get_lle_flags();
               if (this.dom_protocol >= 13) {
                  this.eflags &= var19;
               } else {
                  this.eflags &= 1;
               }

               int var6;
               int var7;
               TypedCArray var24;
               UserTcb var41;
               tcm var42;
               if (this.eflags == 1) {
                  if (var2) {
                     ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/no LLE protocol");
                  }

                  this.elevel = 0;
               } else {
                  if (this.eflags == 0) {
                     if (var2) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/80/");
                     }

                     throw new TPException(4, "Link level encryption negotiation failure" + var19);
                  }

                  if (var2) {
                     ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/do LLE protocol");
                  }

                  this.myLLE = new tplle();
                  var3 = -1;
                  var6 = 2048;

                  while(true) {
                     if (var3 >= 0) {
                        if (var3 == 0) {
                           if (var2) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/80/");
                           }

                           throw new TPException(12, "Unable to generate first diffie-hellman packet" + var19);
                        }

                        var13.setLLELength(var6);
                        var13.setSendSecPDU(var9, var6);
                        var18 = new TypedCArray();
                        var14 = new tcm((short)0, new UserTcb(var18));
                        var18.carray = var9;
                        var12.user = var14;
                        var18.setSendSize(var3);
                        var13.set_opcode(20);
                        if (this.dom_protocol <= 13) {
                           if (var12.write_dom_65_tfmh(this.dom_ostream, var1, 10, this.cmplimit) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/81/");
                              }

                              throw new TPException(4, "Could not send LLE message to remote domain");
                           }

                           if (var2) {
                              ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/...send LLE");
                           }

                           if (var12.read_dom_65_tfmh(this.dom_istream, 10) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/82/");
                              }

                              throw new TPException(4, "Could not read message from remote domain");
                           }
                        } else {
                           if (var12.write_tfmh(this.dom_ostream, this.cmplimit) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/83/");
                              }

                              throw new TPException(4, "Could not send LLE message to remote domain");
                           }

                           if (var2) {
                              ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/...send LLE");
                           }

                           if (var12.read_tfmh(this.dom_istream) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/84/");
                              }

                              throw new TPException(4, "Could not read message from remote domain");
                           }

                           var13 = (TdomTcb)var12.tdom.body;
                        }

                        if (var13.get_opcode() != 21) {
                           if (var2) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/85/");
                           }

                           throw new TPException(4, "Invalid opcode");
                        }

                        if (var2) {
                           ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/...recv LLE_RPLY");
                        }

                        var42 = var12.user;
                        var41 = (UserTcb)var42.body;
                        var24 = (TypedCArray)var41.user_data;
                        var10 = var24.carray;
                        var7 = var10.length;
                        if (var2) {
                           ntrace.doTrace("recv size = " + var7);
                        }

                        switch (this.myLLE.crypFinishOne(var10)) {
                           case 3:
                              this.elevel = 0;
                              this.myLLE = null;
                              break;
                           case 4:
                              this.elevel = 2;
                              break;
                           case 5:
                              this.elevel = 32;
                              break;
                           case 6:
                              this.elevel = 4;
                              break;
                           default:
                              this.myLLE = null;
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/83/");
                              }

                              throw new TPException(12, "ERROR: unexpected link level encryption failure");
                        }

                        if (this.elevel != 0) {
                           this.rein.setElevel(this.elevel);
                           this.rein.setLLE(this.myLLE);
                           this.ein.setElevel(this.elevel);
                           this.ein.setLLE(this.myLLE);
                           this.eout.setElevel(this.elevel);
                           this.eout.setLLE(this.myLLE);
                        }

                        if (var2) {
                           ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/ready to use LLE");
                        }
                        break;
                     }

                     if (var2) {
                        ntrace.doTrace(" /dsession(" + this.uid + ")/do_connect/lle buffer " + var6);
                     }

                     var9 = new byte[var6];
                     var3 = this.myLLE.crypKeyeOne(this.eflags, var9, 0);
                     if (var3 < 0) {
                        var6 = -var3;
                     }
                  }
               }

               if (this.security_type == 0) {
                  this.auth_type = 0;
               } else {
                  Object var15;
                  if (this.dom_protocol <= 13) {
                     var15 = new atntdom65(this.desired_name);
                  } else {
                     if (this.gssatn == null) {
                        if (var2) {
                           ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/82/");
                        }

                        throw new TPException(12, "Missing appropriate GSSATN object");
                     }

                     var15 = this.gssatn;
                  }

                  ((atn)var15).setSecurityType(this.security_type);
                  ((atn)var15).setSrcName(this.desired_name);
                  ((atn)var15).setTargetName(this.dom_target_name);
                  if (this.security_type == 1) {
                     ((atn)var15).setApplicationPasswd(this.lpwd);
                  } else {
                     ((atn)var15).setLocalPasswd(this.lpwd);
                     ((atn)var15).setRemotePasswd(this.rpwd);
                  }

                  if (this.dom_protocol >= 15) {
                     ((atn)var15).setInitiatorAddr(this.dom_socket.getLocalAddress().getAddress());
                     ((atn)var15).setAcceptorAddr(this.dom_socket.getInetAddress().getAddress());
                     if (this.myLLE != null && this.elevel > 0 && this.elevel != 1) {
                        ((atn)var15).setApplicationData(this.myLLE.getFingerprint());
                     }

                     var18 = new TypedCArray();
                     var14 = new tcm((short)0, new UserTcb(var18));
                  }

                  try {
                     var16 = (atncredtdom)((atn)var15).gssAcquireCred(this.desired_name, this.desired_name);
                  } catch (EngineSecError var27) {
                     if (var2) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/90/");
                     }

                     throw new TPException(8, "Unable to acquire credentials (" + var27.errno + ")");
                  }

                  try {
                     var17 = (atnctxtdom)((atn)var15).gssGetContext(var16, this.dom_target_name);
                  } catch (EngineSecError var28) {
                     if (var2) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/100/");
                     }

                     throw new TPException(8, "Unable to get security context (" + var28.errno + ")");
                  }

                  int var35 = 1;

                  while(var35 > 0) {
                     var6 = ((atn)var15).getEstimatedPDUSendSize(var17);
                     var7 = ((atn)var15).getEstimatedPDURecvSize(var17);
                     if (var6 > 0) {
                        if (var9 == null || var9.length < var6) {
                           var9 = new byte[var6];
                           if (var2) {
                              ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/send_size " + var6);
                           }
                        }

                        if (this.dom_protocol <= 13) {
                           switch (var17.context_state) {
                              case 0:
                                 var4 = 16;
                                 break;
                              case 2:
                                 var4 = 18;
                           }
                        }
                     }

                     if (var7 > 0) {
                        if (this.dom_protocol > 13) {
                           if (var12.read_tfmh(this.dom_istream) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/120/");
                              }

                              throw new TPException(4, "Could not receive security exchange from remote domain");
                           }

                           var42 = var12.user;
                           var41 = (UserTcb)var42.body;
                           var24 = (TypedCArray)var41.user_data;
                           var10 = var24.carray;
                           var7 = var10.length;
                           var13 = (TdomTcb)var12.tdom.body;
                           if (var2) {
                              ntrace.doTrace("recv size = " + var7);
                           }
                        } else {
                           if (var10 == null || var10.length < var7) {
                              var10 = new byte[var7];
                              if (var2) {
                                 ntrace.doTrace("/dsession(" + this.uid + ")/do_connect/recv size " + var7);
                              }
                           }

                           var13.setRecvSecPDU(var10, var7);
                           if (var12.read_dom_65_tfmh(this.dom_istream, 10) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/110/");
                              }

                              throw new TPException(4, "Could not receive security exchange from remote domain");
                           }
                        }
                     }

                     try {
                        var35 = ((atn)var15).gssInitSecContext(var17, var10, var7, var9);
                     } catch (EngineSecError var29) {
                        if (var29.errno != -3005) {
                           if (var2) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/140/");
                           }

                           throw new TPException(8, "Security violation (" + var29.errno + ")");
                        }

                        var9 = new byte[var29.needspace];

                        try {
                           var35 = ((atn)var15).gssInitSecContext(var17, var10, var7, var9);
                        } catch (EngineSecError var26) {
                           if (var2) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/130/");
                           }

                           throw new TPException(8, "Security violation (" + var26.errno + ")");
                        }
                     }

                     if (var35 == -1) {
                        if (var2) {
                           ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/145/");
                        }

                        throw new TPException(8, "Security violation");
                     }

                     if (var6 > 0) {
                        int var5 = ((atn)var15).getActualPDUSendSize();
                        var13.setSendSecPDU(var9, var5);
                        if (this.dom_protocol <= 13) {
                           var13.set_opcode(var4);
                           if (var12.write_dom_65_tfmh(this.dom_ostream, var1, 10, this.cmplimit) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/140/");
                              }

                              throw new TPException(4, "Could not send message to remote domain");
                           }
                        } else {
                           var18.carray = var9;
                           var12.user = var14;
                           var18.setSendSize(var5);
                           var13.set_opcode(22);
                           if (var12.write_tfmh(this.dom_ostream, this.cmplimit) != 0) {
                              if (var2) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/150/");
                              }

                              throw new TPException(4, "Could not send message to remote domain");
                           }
                        }
                     }
                  }
               }
            } catch (SocketTimeoutException var31) {
               if (var2) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/155/Exception:" + var31);
               }

               throw new TPException(13, "Connection establishment timed out");
            } catch (IOException var32) {
               if (var2) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/160/Exception:" + var32);
               }

               throw new TPException(12, "Unable to get authentication level");
            }

            if (this.setUpTuxedoAAA() < 0) {
               if (var2) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/do_connect/170/Error");
               }

               throw new TPException(12, "Unable to setup security authentication and auditing");
            } else {
               if (this.useSSL) {
                  this.ein.setBufferOffset(0);
               }

               this.rcv_place = new rdsession(this.dom_ostream, this, this.invoker, this.dom_protocol, this.local_domain_name, this.myTimeService, this.myXidRplyObj, this.useBetaFeatures);
               this.rcv_place.set_BlockTime(this.myBlockTime);
               this.rcv_place.setSessionReference(this);
               this.ein.setRecvSession(this.rcv_place);
               this.dmqDecision();
               WTCLogger.logInfoConnectedToRemoteDomain(this.remote_domain_id);
               this.is_connected = true;
               if (var2) {
                  ntrace.doTrace("]/dsession(" + this.uid + ")/do_connect/170/");
               }

            }
         }
      }
   }

   public synchronized void _dom_drop() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/_dom_drop/");
      }

      this.is_term = true;
      if (this.myTerminationHandler != null) {
         this.myTerminationHandler.onTerm(0);
         this.myTerminationHandler = null;
      }

      DomainRegistry.removeDomainSession(this);
      if (this.dom_socket == null) {
         if (var1) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/_dom_drop/10/");
         }

      } else {
         try {
            this.dom_ostream = null;
            this.dom_istream = null;
            if (this.ein != null) {
               this.ein.close();
               this.ein = null;
            }

            this.dom_socket.close();
            this.dom_socket = null;
         } catch (IOException var3) {
            WTCLogger.logIOEbadDomSocketClose(var3.getMessage());
         }

         if (var1) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/_dom_drop/20/");
         }

      }
   }

   public void terminateTDomainSession(boolean var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.local_domain_name + ", " + this.remote_domain_id + ")/termimateTDomainSession/" + var1);
      }

      this.myLock.lock();
      this.is_term = true;
      DomainRegistry.removeDomainSession(this);
      if (this.myAppKey != null) {
         try {
            this.myAppKey.uninit();
         } catch (TPException var4) {
            this.myLock.unlock();
            if (var2) {
               ntrace.doTrace("]/dsession/terminateTDomainSession(10)/" + var4.getMessage());
            }

            return;
         }

         this.myAppKey = null;
      }

      if (this.kaTask != null && this.kaState == 3) {
         this.kaTask.cancel();
      }

      this.kaState = 4;
      this.kaTask = null;
      this.myLock.unlock();
      if (this.dom_socket != null) {
         try {
            this.dom_ostream = null;
            this.dom_istream = null;
            if (this.ein != null) {
               if (!this.ein.isClosed()) {
                  this.ein.close();
               }

               this.ein = null;
            }

            if (!this.dom_socket.isClosed()) {
               this.dom_socket.close();
            }

            this.dom_socket = null;
         } catch (InterruptedIOException var5) {
            if (var2) {
               ntrace.doTrace("]/dsession/terminateTDomainSession(15) " + var5.getMessage());
            }
         } catch (IOException var6) {
            if (var2) {
               ntrace.doTrace("]/dsession/terminateTDomainSession(20)" + var6.getMessage());
            }

            return;
         }
      }

      if (this.myTerminationHandler != null) {
         if (var1) {
            this.myTerminationHandler.onTerm(0);
         } else {
            this.myTerminationHandler.onTerm(1);
         }

         this.myTerminationHandler = null;
      }

      if (var2) {
         ntrace.doTrace("]/dsession/terminateTDomainSession(30)");
      }

   }

   public synchronized void tpinit(TPINIT var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpinit/" + var1);
      }

      if (this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpinit/10/");
         }

         throw new TPException(9, "Can not init object more than once");
      } else if (this.is_term) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpinit/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if (!this.connector) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpinit/30/");
         }

         throw new TPException(9, "We are accepting, not connecting");
      } else {
         this.do_connect(var1.usrname);
         if (var2) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/tpinit/30/");
         }

      }
   }

   public synchronized void tpterm() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpterm/");
      }

      if (!this.is_term) {
         this.terminateTDomainSession(true);
      }

      if (var1) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/tpterm/10/");
      }

   }

   private synchronized int allocReqid() {
      ++this.reqid;
      return this.reqid;
   }

   public TdomTcb alloc_TDOM(int var1, int var2, String var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/alloc_TDOM/" + var1 + "/" + var3);
      }

      TdomTcb var5 = new TdomTcb(var1, this.allocReqid(), var2, var3);
      if (var4) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/alloc_TDOM/10/" + var5);
      }

      return var5;
   }

   private CallDescriptor _tpacall_internal(ReplyQueue var1, String var2, tfmh var3, int var4, Xid var5, int var6, MethodParameters var7, boolean var8, TuxedoCorbaConnection var9, GatewayTpacallAsyncReply var10, TuxedoConnection var11) throws TPException {
      boolean var12 = ntrace.isTraceEnabled(4);
      boolean var13 = ntrace.isTraceEnabled(64);
      if (var12) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/_tpacall_internal/" + var2 + "/" + var3 + "/" + var4 + "/" + var5 + "/" + var6);
      }

      boolean var18 = false;
      int var19 = 0;
      Object[] var20 = new Object[4];
      Txid var22 = null;
      String var26 = null;
      UserRec var27 = null;
      if (var12) {
         ntrace.doTrace("myAppKey = " + this.myAppKey);
      }

      if (this.myAppKey != null && (var27 = this.getCurrentUser(var11)) == null) {
         throw new TPException(8, "User does not have permission to access Tuxedo");
      } else {
         TdomTcb var14;
         int var28;
         if (var8) {
            var4 &= -17;
            if (var3.tdom == null) {
               var14 = this.alloc_TDOM(4, var4, var2);
            } else {
               var14 = (TdomTcb)var3.tdom.body;
               var14.set_opcode(4);
               var14.set_reqid(this.allocReqid());
               var14.set_flag(var4);
               var14.set_service(var2);
            }

            var14.setConvId(this.convid);
            var14.set_seqnum(1);
            var28 = var14.get_info();
            var14.set_info(var28 | 1);
         } else {
            if ((var4 & 16384) != 0) {
               var4 &= -16385;
               var18 = true;
            }

            if (var3.tdom == null) {
               var14 = this.alloc_TDOM(1, var4, var2);
            } else {
               var14 = (TdomTcb)var3.tdom.body;
               var14.set_opcode(1);
               var14.set_reqid(this.allocReqid());
               var14.set_flag(var4);
               var14.set_service(var2);
               var14.set_msgprio(this.tmsndprio);
            }
         }

         if (var3.tdom == null) {
            tcm var15 = new tcm((short)7, var14);
            var3.tdom = var15;
         }

         int var17;
         if (var5 != null) {
            var17 = var6;
         } else if ((var4 & 32) != 0) {
            var17 = -1;
         } else {
            var17 = 0;
         }

         SessionAcallDescriptor var16;
         if ((var4 & 4) == 0) {
            if (var8) {
               var16 = new SessionAcallDescriptor(var14.getConvId(), var8);
            } else {
               var16 = new SessionAcallDescriptor(var14.get_reqid(), var8);
            }

            if (var10 != null) {
               var16.setHasCallback(true);
            }

            if (var1 != null) {
               this.rcv_place.add_rplyObj(var16, var1, var17, var10);
            }
         } else {
            var16 = new SessionAcallDescriptor(-1, false);
         }

         if (var5 != null) {
            TdomTranTcb var21 = new TdomTranTcb(var5, var6, this.local_domain_name);
            var22 = new Txid(var21.getGlobalTransactionId());
            tcm var23 = new tcm((short)10, var21);
            var3.tdomtran = var23;
         } else {
            var3.tdomtran = null;
         }

         if (var27 != null) {
            var28 = var27.getAppKey();
            var26 = var27.getRemoteUserName();
            if (var12) {
               ntrace.doTrace("/dsession/_tpacall_internal/" + var26 + "," + var28);
            }

            AaaTcb var24 = new AaaTcb();
            var24.setATZUserName(var26);
            var24.setATZAppKey(var28);
            var24.setAUDUserName(var26);
            var24.setAUDAppKey(var28);
            tcm var25 = new tcm((short)15, var24);
            var3.AAA = var25;
         } else {
            var3.AAA = null;
         }

         if (var10 != null) {
            var10.setTargetSubject(TCSecurityManager.getCurrentUser());
         }

         try {
            if (var18) {
               if (this.rmiCallList == null) {
                  synchronized(this.lockObject) {
                     if (this.rmiCallList == null) {
                        this.rmiCallList = new HashMap(100);
                        if (var12) {
                           ntrace.doTrace("/dsession/_tpacall_internal/4/" + this + "/" + this.rmiCallList);
                        }
                     }
                  }
               }

               var19 = var14.get_reqid();
               if (var12) {
                  ntrace.doTrace("/dsession/_tpacall_internal/5:RMI/IIOP call: reqId =" + var19);
               }

               var20[0] = var9;
               var20[1] = var26;
               Debug.assertion(var7 != null, "RMI/IIOP call made with null MethodParameters");
               var20[2] = new Integer(var7.getGIOPRequestID());
               var20[3] = var22;
               synchronized(this.rmiCallList) {
                  if (var12) {
                     ntrace.doTrace("/dsession/_tpacall_internal/7: RMI/IIOP call: reqId =" + var19 + ", xid =" + var22);
                  }

                  this.rmiCallList.put(new Integer(var19), var20);
               }
            }

            if (useCORBATimeout && (var4 & 36) == 0 && var5 == null && this.myBlockTime > 0L) {
               this.rcv_place.addTimeoutRequest(var7.getGIOPRequestID(), var19, this.myBlockTime);
            }

            synchronized(this.dom_ostream) {
               if (var13) {
                  var3.dumpUData(true);
               }

               if (this.dom_protocol >= 15) {
                  var3.write_tfmh(this.dom_ostream, this.cmplimit);
               } else {
                  var3.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
               }

               if (var13) {
                  var3.dumpUData(false);
               }
            }
         } catch (IOException var37) {
            if (var12) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/_tpacall_internal/30/");
            }

            if (var18) {
               synchronized(this.rmiCallList) {
                  Object var30 = this.rmiCallList.remove(new Integer(var19));
                  if (var30 == null && var12) {
                     ntrace.doTrace("*/dsession(" + this.uid + ")/_tpacall_internal/35/");
                  }
               }
            }

            throw new TPException(12, "tpacall network send error: " + var37);
         }

         if (var12) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/_tpacall_internal/40/" + var16);
         }

         return var16;
      }
   }

   public CallDescriptor tpacall(String var1, TypedBuffer var2, int var3, Xid var4, int var5, GatewayTpacallAsyncReply var6, TuxedoConnection var7) throws TPException {
      boolean var8 = ntrace.isTraceEnabled(4);
      if (var8) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpacall/" + var1 + "/" + var2 + "/" + var3 + "/" + var4 + "/" + var5);
      }

      tfmh var10 = null;
      StandardTypes var12 = null;
      if (!this.is_connected) {
         if (var8) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpacall/10/");
         }

         throw new TPException(9, "Must init before tpacall");
      } else if (this.is_term) {
         if (var8) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpacall/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if ((var3 & -46) != 0) {
         if (var8) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpacall/30/");
         }

         throw new TPException(4);
      } else if (var1 != null && !var1.equals("")) {
         if (var2 == null) {
            var10 = new tfmh(1);
         } else {
            if (var2 instanceof StandardTypes) {
               var12 = (StandardTypes)var2;
               var10 = (tfmh)var12.getTfmhCache();
            }

            if (var10 == null) {
               tcm var9 = new tcm((short)0, new UserTcb(var2));
               var10 = new tfmh(var2.getHintIndex(), var9, 1);
            }
         }

         CallDescriptor var11 = this._tpacall_internal(this.myRplyObj, var1, var10, var3, var4, var5, (MethodParameters)null, false, (TuxedoCorbaConnection)null, var6, var7);
         if (var12 != null) {
            var12.setTfmhCache(var10);
         }

         if (var8) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/tpacall/100/" + var11);
         }

         return var11;
      } else {
         if (var8) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpacall/40/");
         }

         throw new TPException(4);
      }
   }

   public CallDescriptor tpacall(String var1, TypedBuffer var2, int var3) throws TPException {
      return this.tpacall(var1, var2, var3, (Xid)null, 0, (GatewayTpacallAsyncReply)null, (TuxedoConnection)null);
   }

   public CallDescriptor tpacall(String var1, TypedBuffer var2, int var3, TpacallAsyncReply var4) throws TPException {
      if (!(var4 instanceof GatewayTpacallAsyncReply)) {
         throw new TPException(4, "ERROR: internal tpacall called without gateway tpacall async reply");
      } else {
         return this.tpacall(var1, var2, var3, (Xid)null, 0, (GatewayTpacallAsyncReply)var4, (TuxedoConnection)null);
      }
   }

   public void tpcancel(CallDescriptor var1, int var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpcancel/" + var1 + "/" + var2);
      }

      if (!this.is_connected) {
         if (var3) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcancel/10/");
         }

         throw new TPException(9, "Must init before tpcancel");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var3) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcancel/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else if (var1 != null && var2 == 0 && var1 instanceof SessionAcallDescriptor) {
            SessionAcallDescriptor var4 = (SessionAcallDescriptor)var1;
            if (!this.rcv_place.remove_rplyObj(var4)) {
               if (var3) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcancel/40/");
               }

               throw new TPException(2, "This descriptor (" + var4 + ") has already received a reply");
            }
         } else {
            if (var3) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcancel/30/");
            }

            throw new TPException(4, "Invalid parameter passed to tpcancel");
         }
      }
   }

   private tfmh _tpgetrply_internal(SessionAcallDescriptor var1, int var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/_tpgetrply_internal/" + var1 + "/" + var2);
      }

      tfmh var4 = null;
      int var6 = var1.getCd();
      boolean var5 = (var2 & 1) == 0;
      if (var6 != -1) {
         ReqOid var7 = new ReqOid(var1, this);
         var4 = this.myRplyObj.get_specific_reply(var7, var5);
         if (var3) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/_tpgetrply_internal/20/" + var4);
         }

         return var4;
      } else {
         if (var3) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/_tpgetrply_internal/10/");
         }

         throw new TPException(12, "TPGETANY not implemented yet");
      }
   }

   public Reply tpgetrply(CallDescriptor var1, int var2) throws TPException, TPReplyException {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpgetrply/" + var1 + "/" + var2);
      }

      if (!this.is_connected) {
         if (var3) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/10/");
         }

         throw new TPException(9, "Must init before tpgetrply");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var3) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else if (var1 != null && var1 instanceof SessionAcallDescriptor) {
            SessionAcallDescriptor var14;
            if ((var14 = (SessionAcallDescriptor)var1) != null && var14.hasCallBack()) {
               if (var3) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/27/");
               }

               throw new TPException(4, "CallDescriptor given to tpgetrply has an asynchronous call-back function associated with it");
            } else {
               int var15 = var14.getCd();
               if (var15 >= -1 && (var2 & -162) == 0) {
                  if (var15 == -1 && (var2 & 128) == 0) {
                     if (var3) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/40/");
                     }

                     throw new TPException(4);
                  } else {
                     boolean var5 = (var2 & 1) == 0;
                     tfmh var13;
                     if ((var13 = this._tpgetrply_internal(var14, var2)) == null) {
                        if (var5) {
                           if (var3) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/70/");
                           }

                           throw new TPException(12, "Connection dropped");
                        } else {
                           if (var3) {
                              ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/80/");
                           }

                           throw new TPException(3);
                        }
                     } else {
                        TdomTcb var6 = (TdomTcb)var13.tdom.body;
                        int var7 = var6.get_diagnostic();
                        int var8 = var6.getTpurcode();
                        int var9 = var6.get_errdetail();
                        int var10 = var6.get_opcode();
                        if (var3) {
                           ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/85/opcode=" + var10 + "/diagnostic=" + var7);
                        }

                        if (var10 == 3 && var7 != 11 && var7 != 10) {
                           if (var7 == 7) {
                              if (var3) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/90/");
                              }

                              throw new TPException(var7, var8, 0, var9);
                           } else {
                              if (var3) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/95/");
                              }

                              throw new TPException(var7, 0, var8, var9);
                           }
                        } else {
                           if (var7 != 11 && var7 != 10) {
                              var7 = 0;
                           }

                           TypedBuffer var11;
                           if (var13.user == null) {
                              var11 = null;
                           } else {
                              UserTcb var12 = (UserTcb)var13.user.body;
                              var11 = var12.user_data;
                           }

                           TuxedoReply var16 = new TuxedoReply(var11, var8, var1);
                           if (var7 != 0) {
                              if (var3) {
                                 ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/90/" + var16);
                              }

                              throw new TPReplyException(var7, 0, var8, var9, var16);
                           } else {
                              if (this.rcv_place != null) {
                                 this.rcv_place.restoreTfmhToCache(var13);
                              }

                              if (var3) {
                                 ntrace.doTrace("]/dsession(" + this.uid + ")/tpgetrply/100/" + var16);
                              }

                              return var16;
                           }
                        }
                     }
                  }
               } else {
                  if (var3) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/30/");
                  }

                  throw new TPException(4);
               }
            }
         } else {
            if (var3) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpgetrply/25/");
            }

            throw new TPException(4, "Invalid object (cd) passed to tpgetrply");
         }
      }
   }

   private tfmh _tpcall_internal(String var1, tfmh var2, int var3, Xid var4, int var5, TuxedoConnection var6) throws TPException {
      boolean var7 = ntrace.isTraceEnabled(4);
      if (var7) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/_tpcall_internal/" + var1 + "/" + var2 + "/" + var3);
      }

      SessionAcallDescriptor var8 = (SessionAcallDescriptor)this._tpacall_internal(this.myRplyObj, var1, var2, var3, var4, var5, (MethodParameters)null, false, (TuxedoCorbaConnection)null, (GatewayTpacallAsyncReply)null, var6);
      tfmh var9 = this._tpgetrply_internal(var8, var3);
      if (var7) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/_tpcall_internal/10/" + var9);
      }

      return var9;
   }

   public Reply tpcall(String var1, TypedBuffer var2, int var3, Xid var4, int var5, TuxedoConnection var6) throws TPException, TPReplyException {
      boolean var7 = ntrace.isTraceEnabled(4);
      if (var7) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpcall/" + var1 + "/" + var2 + "/" + var3 + "/" + var4);
      }

      CallDescriptor var8;
      try {
         var8 = this.tpacall(var1, var2, var3, var4, var5, (GatewayTpacallAsyncReply)null, var6);
      } catch (TPException var11) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcall/10/" + var11);
         }

         throw var11;
      }

      Reply var9;
      try {
         var9 = this.tpgetrply(var8, var3 & -10);
      } catch (TPReplyException var12) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcall/20/" + var12);
         }

         throw var12;
      } catch (TPException var13) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcall/30/" + var13);
         }

         throw var13;
      }

      if (var7) {
         ntrace.doTrace("]/dsession(" + this.uid + ")/tpcall/20/" + var9);
      }

      return var9;
   }

   public Reply tpcall(String var1, TypedBuffer var2, int var3) throws TPException, TPReplyException {
      return this.tpcall(var1, var2, var3, (Xid)null, 0, (TuxedoConnection)null);
   }

   public synchronized void tpbegin(long var1, int var3) throws TPException {
      throw new TPException(9, "tpbegin not implemented");
   }

   public synchronized void tpabort(int var1) throws TPException {
      throw new TPException(9, "tpabort not implemented");
   }

   public synchronized void tpcommit(int var1) throws TPException {
      throw new TPException(9, "tpcommit not implemented");
   }

   public byte[] tpenqueue(String var1, String var2, EnqueueRequest var3, TypedBuffer var4, int var5, Xid var6, int var7, TuxedoConnection var8) throws TPException {
      boolean var9 = ntrace.isTraceEnabled(4);
      if (var9) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpenqueue/" + var1 + "/" + var2 + "/" + var3 + "/" + var4 + "/" + var5 + "/" + var6 + "/" + var7);
      }

      ComposFmlTcb var18 = null;
      Integer var25 = null;
      int var27 = 0;
      if (!this.is_connected) {
         if (var9) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/10/");
         }

         throw new TPException(9, "Must init before tpenqueue");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var9) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else if ((var5 & -42) == 0 && var1 != null && var2 != null && var1.length() != 0 && var2.length() != 0 && var3 != null) {
            int var19 = 1;
            if (var3.getexp_time() != null) {
               var19 |= 65536;
            } else if (var3.getdelivery_qos() == 4) {
               var19 |= 65536;
            } else if (var3.getreply_qos() == 4) {
               var19 |= 65536;
            }

            ComposHdrTcb var10 = new ComposHdrTcb(var19, var3.geturcode());
            tcm var13 = new tcm((short)5, var10);
            ComposFmlTcb var11 = new ComposFmlTcb(var2, var3);
            tcm var14 = new tcm((short)6, var11);
            tfmh var15;
            if (var4 == null) {
               var15 = new tfmh(1);
            } else {
               tcm var12 = new tcm((short)0, new UserTcb(var4));
               var15 = new tfmh(var4.getHintIndex(), var12, 1);
            }

            var15.set_TPENQUEUE(true);
            var15.compos_hdr = var13;
            var15.compos_fml = var14;
            tfmh var16;
            if ((var16 = this._tpcall_internal(var1, var15, var5 & -2, var6, var7, var8)) == null) {
               if (var9) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/40/");
               }

               throw new TPException(12, "tpenqueue got invalid return from _tpcall_internal");
            } else {
               TdomTcb var20 = (TdomTcb)var16.tdom.body;
               var19 = var20.get_opcode();
               int var21 = var20.get_diagnostic();
               if (var19 == 3 || var16.compos_hdr != null && var16.compos_fml != null) {
                  if (var16.compos_fml != null) {
                     var18 = (ComposFmlTcb)var16.compos_fml.body;
                     var25 = var18.getDiagnostic();
                  }

                  if (var19 == 3) {
                     if (var21 == 24) {
                        if (var25 == null) {
                           var27 = 7;
                        } else {
                           var27 = var25;
                        }
                     }

                     int var22 = var20.getTpurcode();
                     int var23 = var20.get_errdetail();
                     if (var9) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/90/" + var19 + "/" + var21);
                     }

                     throw new TPException(var21, 0, var22, var23, var27);
                  } else if (var25 != null && (var27 = var25) < 0) {
                     if (var9) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/100/" + var27);
                     }

                     throw new TPException(24, 0, 0, 0, var27);
                  } else {
                     byte[] var26 = var18.getMsgid();
                     if (this.rcv_place != null) {
                        this.rcv_place.restoreTfmhToCache(var16);
                     }

                     if (var9) {
                        ntrace.doTrace("]/dsession(" + this.uid + ")/tpenqueue/110/" + var26.length);
                     }

                     return var26;
                  }
               } else {
                  if (var9) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/50/");
                  }

                  throw new TPException(12, "tpenqueue could not get queue information");
               }
            }
         } else {
            if (var9) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/30/");
            }

            throw new TPException(4);
         }
      }
   }

   public byte[] tpenqueue(String var1, String var2, EnqueueRequest var3, TypedBuffer var4, int var5) throws TPException {
      return this.tpenqueue(var1, var2, var3, var4, var5, (Xid)null, 0, (TuxedoConnection)null);
   }

   public DequeueReply tpdequeue(String var1, String var2, byte[] var3, byte[] var4, boolean var5, boolean var6, int var7, Xid var8, int var9, TuxedoConnection var10) throws TPException {
      boolean var11 = ntrace.isTraceEnabled(4);
      if (var11) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpdequeue/" + var1 + "/" + var2 + "/" + var3 + "/" + var4 + "/" + var5 + "/" + var6 + "/" + var7 + "/" + var8 + "/" + var9);
      }

      ComposFmlTcb var20 = null;
      ComposHdrTcb var21 = null;
      TypedBuffer var23 = null;
      Integer var28 = null;
      int var29 = 0;
      if (!this.is_connected) {
         if (var11) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/10/");
         }

         throw new TPException(9, "Must init before tpenqueue");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var11) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpenqueue/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else if ((var7 & -42) == 0 && var1 != null && var2 != null && var1.length() != 0 && var2.length() != 0) {
            if (var6) {
               var7 |= 8;
            }

            ComposHdrTcb var12 = new ComposHdrTcb(2, 0);
            tcm var15 = new tcm((short)5, var12);
            ComposFmlTcb var13 = new ComposFmlTcb(var2, var3, var4, var5, var6);
            tcm var16 = new tcm((short)6, var13);
            tfmh var17 = new tfmh(1);
            var17.compos_hdr = var15;
            var17.compos_fml = var16;
            tfmh var18;
            if ((var18 = this._tpcall_internal(var1, var17, var7 & -2, var8, var9, var10)) == null) {
               if (var11) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/40/");
               }

               throw new TPException(12, "tpdequeue got invalid return from _tpcall_internal");
            } else {
               TdomTcb var27 = (TdomTcb)var18.tdom.body;
               int var22 = var27.get_opcode();
               int var25 = var27.getTpurcode();
               int var24 = var27.get_diagnostic();
               if (var22 == 3 || var18.compos_hdr != null && var18.compos_fml != null) {
                  if (var18.compos_fml != null) {
                     var20 = (ComposFmlTcb)var18.compos_fml.body;
                     var28 = var20.getDiagnostic();
                  }

                  if (var18.compos_hdr != null) {
                     var21 = (ComposHdrTcb)var18.compos_hdr.body;
                  }

                  if (var22 == 3) {
                     if (var24 == 24) {
                        if (var28 == null) {
                           var29 = 7;
                        } else {
                           var29 = var28;
                        }
                     }

                     int var26 = var27.get_errdetail();
                     if (var11) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/90/");
                     }

                     throw new TPException(var24, 0, var25, var26, var29);
                  } else if (var28 != null && (var29 = var28) < 0) {
                     if (var11) {
                        ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/100/" + var29);
                     }

                     throw new TPException(24, 0, 0, 0, var29);
                  } else {
                     if (var18.user != null) {
                        var23 = ((UserTcb)var18.user.body).user_data;
                     }

                     DequeueReply var30 = new DequeueReply(var23, var25, (CallDescriptor)null, var20.getMsgid(), var20.getCoorid(), var20.getReplyQueue(), var20.getFailureQueue(), new Integer(var21.getAppkey()), var20.getPriority(), var20.getDeliveryQualityOfService(), var20.getReplyQualityOfService(), var21.getUrcode());
                     if (this.rcv_place != null) {
                        this.rcv_place.restoreTfmhToCache(var18);
                     }

                     if (var11) {
                        ntrace.doTrace("]/dsession(" + this.uid + ")/tpdequeue/80/" + var30);
                     }

                     return var30;
                  }
               } else {
                  if (var11) {
                     ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/50/");
                  }

                  throw new TPException(12, "tpdequeue could not get queue information");
               }
            }
         } else {
            if (var11) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpdequeue/30/");
            }

            throw new TPException(4);
         }
      }
   }

   public DequeueReply tpdequeue(String var1, String var2, byte[] var3, byte[] var4, boolean var5, boolean var6, int var7) throws TPException {
      return this.tpdequeue(var1, var2, var3, var4, var5, var6, var7, (Xid)null, 0, (TuxedoConnection)null);
   }

   public DequeueReply tpdequeue(String var1, String var2, int var3) throws TPException {
      return this.tpdequeue(var1, var2, (byte[])null, (byte[])null, false, false, var3, (Xid)null, 0, (TuxedoConnection)null);
   }

   public void send_success_return(Serializable var1, tfmh var2, int var3, int var4, int var5) throws TPException {
      boolean var6 = ntrace.isTraceEnabled(4);
      if (var6) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/send_success_return/" + this.reqid + "/" + var2 + "/" + var4 + "/" + var5);
      }

      dreqid var8 = (dreqid)var1;
      if (!this.is_connected) {
         if (var6) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/send_success_return/10/");
         }

         throw new TPException(9, "How could this have happened?");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var6) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/send_success_return/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else if (var3 != 0 && var3 != 10 && var3 != 11) {
            if (var6) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/send_success_return/25/");
            }

            throw new TPException(4, "Invalid TPException value:" + var3);
         } else {
            var2.setTMREPLY(true);
            TdomTcb var7;
            if (var3 == 0) {
               var7 = new TdomTcb(2, var8.reqid, 4194304, (String)null);
            } else {
               var7 = new TdomTcb(3, var8.reqid, 4194304, (String)null);
            }

            var7.set_tpurcode(var4);
            var7.set_convid(var5);
            if (var5 != -1) {
               var7.set_info(2);
               switch (var3) {
                  case 0:
                     var7.set_tpevent(8);
                     var7.set_diagnostic(22);
                     break;
                  case 10:
                     var7.set_tpevent(2);
                     var7.set_diagnostic(22);
                     break;
                  case 11:
                     var7.set_tpevent(4);
                     var7.set_diagnostic(22);
                     break;
                  default:
                     var7.set_diagnostic(var3);
               }
            } else {
               var7.set_diagnostic(var3);
            }

            var2.tdom = new tcm((short)7, var7);

            try {
               synchronized(this.dom_ostream) {
                  if (this.dom_protocol >= 15) {
                     var2.write_tfmh(this.dom_ostream, this.cmplimit);
                  } else {
                     var2.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
                  }
               }
            } catch (IOException var12) {
               if (var6) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/send_success_return/30/");
               }

               throw new TPException(12, "Unable to send success reply network error: " + var12);
            }

            if (var6) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/send_success_return/40/");
            }

         }
      }
   }

   public void send_transaction_reply(tfmh var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/send_transaction_reply/" + var1);
      }

      if (!this.is_connected) {
         if (var2) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/send_transaction_reply/10/");
         }

         throw new TPException(9, "How could this have happened?");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var2) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/send_transaction_reply/20/");
            }

            throw new TPException(9, "Tuxedo session has been terminated");
         } else {
            try {
               synchronized(this.dom_ostream) {
                  if (this.dom_protocol >= 15) {
                     var1.write_tfmh(this.dom_ostream, this.cmplimit);
                  } else {
                     var1.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
                  }
               }
            } catch (IOException var6) {
               if (var2) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/send_transaction_reply/30/");
               }

               throw new TPException(12, "Unable to send transaction reply network error: " + var6);
            }

            if (var2) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/send_transaction_reply/40/");
            }

         }
      }
   }

   public void send_failure_return(Serializable var1, TPException var2, int var3) {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (ntrace.getTraceLevel() >= 50) {
         ntrace.doTrace("Some error happened! " + var2);
      }

      if (var4) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/send_failure_return/" + var1 + "/" + var2);
      }

      dreqid var7 = (dreqid)var1;
      if (!this.is_connected) {
         if (var4) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/send_failure_return/10/");
         }

      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var4) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/send_failure_return/20/");
            }

         } else {
            tfmh var5 = new tfmh(1);
            TdomTcb var6 = new TdomTcb(3, var7.reqid, 4194304, (String)null);
            var6.set_errdetail(var2.gettperrordetail());
            var6.set_tpurcode(var2.gettpurcode());
            var6.set_diagnostic(var2.gettperrno());
            var6.set_convid(var3);
            var5.tdom = new tcm((short)7, var6);

            try {
               synchronized(this.dom_ostream) {
                  if (this.dom_protocol >= 15) {
                     var5.write_tfmh(this.dom_ostream, this.cmplimit);
                  } else {
                     var5.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
                  }
               }
            } catch (IOException var11) {
               if (var4) {
                  ntrace.doTrace("]/dsession(" + this.uid + ")/send_failure_return/20/");
               }

               return;
            }

            if (var4) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/send_failure_return/30/");
            }

         }
      }
   }

   public CallDescriptor tprplycall(TuxRply var1, String var2, TypedBuffer var3, int var4, Xid var5, int var6, GatewayTpacallAsyncReply var7, TuxedoConnection var8) throws TPException {
      boolean var9 = ntrace.isTraceEnabled(4);
      if (var9) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tprplycall/" + var2 + "/" + var4);
      }

      if (!this.is_connected) {
         if (var9) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tprplycall/10/");
         }

         throw new TPException(9, "Must init before tprplycall");
      } else if (this.is_term) {
         if (var9) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tprplycall/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if ((var4 & -46) != 0) {
         if (var9) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tprplycall/30/");
         }

         throw new TPException(4);
      } else if (var2 != null && !var2.equals("")) {
         if ((var4 & 4) != 0 && var1 != null) {
            if (var9) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tprplycall/50/");
            }

            throw new TPException(4, "Cannot have a reply object if TPNOREPLY is set");
         } else {
            tfmh var11;
            if (var3 == null) {
               var11 = new tfmh(1);
            } else {
               tcm var10 = new tcm((short)0, new UserTcb(var3));
               var11 = new tfmh(var3.getHintIndex(), var10, 1);
            }

            CallDescriptor var12 = this._tpacall_internal(var1, var2, var11, var4, var5, var6, (MethodParameters)null, false, (TuxedoCorbaConnection)null, var7, var8);
            if (var9) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/tprplycall/60/" + var12);
            }

            return var12;
         }
      } else {
         if (var9) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tprplycall/40/");
         }

         throw new TPException(4);
      }
   }

   public Txid tpprepare(TuxXidRply var1, Xid var2, int var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpprepare/" + var1 + "/" + var2 + "/" + var3);
      }

      if (!this.is_connected) {
         if (var4) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpprepare/10/");
         }

         throw new TPException(9, "Must init before tprplycall");
      } else if (this.is_term) {
         if (var4) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpprepare/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if (var1 != null && var2 != null) {
         tfmh var5 = new tfmh(1);
         TdomTcb var7 = this.alloc_TDOM(7, 0, (String)null);
         tcm var8 = new tcm((short)7, var7);
         var5.tdom = var8;
         var7.set_info(32);
         TdomTranTcb var9 = new TdomTranTcb(var2, 0, this.local_domain_name);
         tcm var10 = new tcm((short)10, var9);
         var5.tdomtran = var10;
         Txid var6 = new Txid(var2.getGlobalTransactionId());
         this.rcv_place.add_rplyXidObj(var6, var1, var3);

         try {
            synchronized(this.dom_ostream) {
               if (this.dom_protocol >= 15) {
                  var5.write_tfmh(this.dom_ostream, this.cmplimit);
               } else {
                  var5.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
               }
            }
         } catch (IOException var14) {
            if (var4) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpprepare/40/");
            }

            throw new TPException(12, "Could not send prepare message" + var14);
         }

         if (var4) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/tpprepare/50/" + var6);
         }

         return var6;
      } else {
         if (var4) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpprepare/30/");
         }

         throw new TPException(4);
      }
   }

   public Txid tpcommit(TuxXidRply var1, Xid var2, int var3, boolean var4) throws TPException {
      boolean var5 = ntrace.isTraceEnabled(4);
      if (var5) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpcommit/" + var1 + "/" + var2 + "/" + var3 + "/" + var4);
      }

      if (!this.is_connected) {
         if (var5) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcommit/10/");
         }

         throw new TPException(9, "Must init before tprplycall");
      } else if (this.is_term) {
         if (var5) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcommit/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if (var1 != null && var2 != null) {
         tfmh var6 = new tfmh(1);
         TdomTcb var8;
         if (var4) {
            var8 = this.alloc_TDOM(9, 0, (String)null);
         } else {
            var8 = this.alloc_TDOM(12, 0, (String)null);
         }

         tcm var9 = new tcm((short)7, var8);
         var6.tdom = var9;
         var8.set_info(32);
         TdomTranTcb var10 = new TdomTranTcb(var2, 0, this.local_domain_name);
         tcm var11 = new tcm((short)10, var10);
         var6.tdomtran = var11;
         Txid var7 = new Txid(var2.getGlobalTransactionId());
         this.rcv_place.add_rplyXidObj(var7, var1, var3);

         try {
            synchronized(this.dom_ostream) {
               if (this.dom_protocol >= 15) {
                  var6.write_tfmh(this.dom_ostream, this.cmplimit);
               } else {
                  var6.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
               }
            }
         } catch (IOException var15) {
            if (var5) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/tpcommit/40/null");
            }

            return null;
         }

         if (var5) {
            ntrace.doTrace("]/dsession(" + this.uid + ")/tpcommit/50/" + var7);
         }

         return var7;
      } else {
         if (var5) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpcommit/30/");
         }

         throw new TPException(4);
      }
   }

   public int getUid() {
      return this.uid;
   }

   public int hashCode() {
      return this.uid & '\uffff';
   }

   public boolean equals(Object var1) {
      dsession var2 = (dsession)var1;
      if (var2 == null) {
         return false;
      } else {
         return var2.getUid() == this.uid;
      }
   }

   public synchronized Conversation tpconnect(String var1, TypedBuffer var2, int var3, Xid var4, int var5, TuxedoConnection var6) throws TPException {
      boolean var7 = ntrace.isTraceEnabled(4);
      if (var7) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/tpconnect/" + var1 + "/" + var2 + "/" + var3 + "/" + var4 + "/" + var5);
      }

      tfmh var9 = null;
      StandardTypes var11 = null;
      DomainOutboundConversation var12 = null;
      if (!this.is_connected) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/10/");
         }

         throw new TPException(9, "Must init before tpacall");
      } else if (this.is_term) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/20/");
         }

         throw new TPException(9, "Domain session has been terminated");
      } else if ((var3 & -6186) != 0) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/30/");
         }

         throw new TPException(4);
      } else if ((var3 & 6144) == 0) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/40/");
         }

         throw new TPException(4, "Must specify a flag TPSENDONLY or TPRECVONLY");
      } else if ((var3 & 2048) != 0 & (var3 & 4096) != 0) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/50/");
         }

         throw new TPException(4, "Only one of TPSENDONLY or TPRECVONLY should be set");
      } else {
         boolean var13 = (var3 & 2048) != 0;
         boolean var14 = var4 != null;
         if (var1 != null && !var1.equals("")) {
            if (var2 == null) {
               var9 = new tfmh(1);
            } else {
               if (var2 instanceof StandardTypes) {
                  var11 = (StandardTypes)var2;
                  var9 = (tfmh)var11.getTfmhCache();
               }

               if (var9 == null) {
                  tcm var8 = new tcm((short)0, new UserTcb(var2));
                  var9 = new tfmh(var2.getHintIndex(), var8, 1);
               }
            }

            ++this.convid;
            ConversationReply var15 = new ConversationReply();

            SessionAcallDescriptor var10;
            try {
               var10 = (SessionAcallDescriptor)this._tpacall_internal(var15, var1, var9, var3, var4, var5, (MethodParameters)null, true, (TuxedoCorbaConnection)null, (GatewayTpacallAsyncReply)null, var6);
            } catch (TPException var17) {
               if (var7) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/70/" + var17);
               }

               throw var17;
            }

            if (var11 != null) {
               var11.setTfmhCache(var9);
            }

            var12 = new DomainOutboundConversation(this, var15, this.convid, var13, var10, var14);
            if (var7) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/tpconnect/85/" + var12);
            }

            return var12;
         } else {
            if (var7) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/tpconnect/60/");
            }

            throw new TPException(4);
         }
      }
   }

   public Conversation tpconnect(String var1, TypedBuffer var2, int var3) throws TPException {
      return this.tpconnect(var1, var2, var3, (Xid)null, 0, (TuxedoConnection)null);
   }

   public void setTerminationHandler(OnTerm var1) {
      this.myTerminationHandler = var1;
   }

   public void restoreTfmhToCache(tfmh var1) {
      if (this.rcv_place != null) {
         this.rcv_place.restoreTfmhToCache(var1);
      }

   }

   public CallDescriptor tpMethodReq(TypedBuffer var1, Objinfo var2, MethodParameters var3, TuxedoCorbaConnection var4, int var5, TuxRply var6, Xid var7, int var8, TuxedoConnection var9) throws TPException {
      Objrecv var15 = null;
      boolean var16 = ntrace.isTraceEnabled(4);
      if (var16) {
         ntrace.doTrace("/dsession(" + this.uid + ")/tpMethodReq/0/" + var5 + ":" + var6 + ":" + var7 + ":" + var3);
      }

      if (var2 == null) {
         if (var16) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpMethodReq/10");
         }

         throw new TPException(4);
      } else if (var1 != null && var1.getHintIndex() != 10) {
         if (var16) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/tpMethodReq/20");
         }

         throw new TPException(4);
      } else {
         if (var3 != null) {
            var15 = var3.getObjrecv();
            if (var16) {
               ntrace.doTrace("*]/dsession/tpMethodReq/21,currObjrecv = " + var15);
            }
         }

         tfmh var11;
         if (var1 != null) {
            tcm var10 = new tcm((short)0, new UserTcb(var1));
            var11 = new tfmh(var1.getHintIndex(), var10, 1);
         } else {
            var11 = new tfmh(1);
         }

         if (this.myCltInfo == null) {
            this.myCltInfo = new ClientInfo(var15, 1, this.local_domain_name);
         }

         if (var15 == null) {
            var2.setSendSrcCltinfo(this.myCltInfo);
         } else {
            var2.setSendSrcCltinfo(var15.getObjinfo().getRecvSrcCltinfo());
         }

         TdomValsTcb var17;
         if (var2.getIsMyDomain() == 0 && var11.tdom_vals == null) {
            var11.tdom_vals = new tcm((short)17, new TdomValsTcb());
            var17 = (TdomValsTcb)var11.tdom_vals.body;
            var17.setDescrim(2);
         }

         String var12;
         if (var11.tdom_vals != null) {
            var17 = (TdomValsTcb)var11.tdom_vals.body;
            if ((var5 & 1) == 0) {
               var17.setOrigDomain(new String(this.local_domain_name));
            }

            ClientInfo var14;
            if ((var14 = var2.getSendSrcCltinfo()) == null) {
               var17.setSrcDomain(new String(this.local_domain_name));
            } else if (var14.getDomain() != null && !var14.getDomain().equals("")) {
               var17.setSrcDomain(new String(var14.getDomain()));
            } else {
               var17.setSrcDomain(new String(this.local_domain_name));
            }

            if (var17.getDestDomain() == null || var17.getDestDomain().equals("")) {
               if ((var12 = var2.getDomainId()) == null) {
                  var17.setDestDomain(new String(this.local_domain_name));
               } else {
                  var17.setDestDomain(new String(var12));
               }
            }
         }

         if (var2.getIsACallout() == 1) {
            TGIOPUtil.calloutSet(var11, var2, var15, 0);
            if (var2.getIsMyDomain() == 1) {
               if (var16) {
                  ntrace.doTrace("*]/dsession/tpMethodReq/25");
               }

               throw new TPException(12);
            }
         } else if (var15 != null) {
            TGIOPUtil.routeSetHost(var11, var15.getHost(), var15.getHost().length(), (short)var15.getPort(), 0);
         } else {
            TGIOPUtil.routeSetHost(var11, (String)null, 0, (short)0, 0);
         }

         if (var11.route != null) {
            RouteTcb var18 = (RouteTcb)var11.route.body;
            var18.setObjinfo(var2);
         }

         if (var2.getIsMyDomain() == 0) {
            var11.getMetahdr().setFlags(var11.getMetahdr().getFlags() & -8193);
            var12 = new String("//" + var2.getDomainId());
            var5 &= -2;
            CallDescriptor var13 = this._tpacall_internal(var6, var12, var11, var5, var7, var8, var3, false, var4, (GatewayTpacallAsyncReply)null, var9);
            var11.tdom_vals = var11.route = var11.callout = null;
            if (var16) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/tpMethodReq/40");
            }

            return var13;
         } else {
            if (var16) {
               ntrace.doTrace("*]/dsession/tpMethodReq/30");
            }

            throw new TPException(12);
         }
      }
   }

   protected TuxRply getRplyObj() {
      return this.myRplyObj;
   }

   public void _tpsend_internal(tfmh var1, int var2, int var3, boolean var4, boolean var5, boolean var6) throws TPException {
      boolean var7 = ntrace.isTraceEnabled(4);
      if (var7) {
         ntrace.doTrace("[/dsession(" + this.uid + ")/_tpsend_internal/" + var2 + "/" + var3 + "/" + var4 + "/" + var5 + "/" + var6);
      }

      Object var12 = null;
      if (!this.is_connected) {
         if (var7) {
            ntrace.doTrace("*]/dsession(" + this.uid + ")/_tpsend_internal/10/");
         }

         throw new TPException(9, "ERROR: Connection dropped");
      } else {
         if (this.rcv_place.isTerm()) {
            this._dom_drop();
         }

         if (this.is_term) {
            if (var7) {
               ntrace.doTrace("*]/dsession(" + this.uid + ")/_tpsend_internal/20/");
            }

            throw new TPException(9, "ERROR: Tuxedo session has been terminated");
         } else {
            TdomTcb var8;
            if (var6) {
               var8 = new TdomTcb(6, this.allocReqid(), 0, (String)null);
            } else {
               var8 = new TdomTcb(5, this.allocReqid(), 0, (String)null);
            }

            var8.setConvId(var3);
            var8.set_seqnum(var2);
            int var9 = var8.get_info();
            if (var4) {
               var8.set_info(var9 | 1);
            } else {
               var8.set_info(var9 | 2);
            }

            if (var5) {
               var8.set_flag(4096);
               var8.set_diagnostic(22);
            }

            if (var6) {
               var8.set_diagnostic(22);
               var8.set_tpevent(1);
            }

            var1.tdom = new tcm((short)7, var8);
            var1.AAA = null;

            try {
               synchronized(this.dom_ostream) {
                  if (this.dom_protocol >= 15) {
                     var1.write_tfmh(this.dom_ostream, this.cmplimit);
                  } else {
                     var1.write_dom_65_tfmh(this.dom_ostream, this.local_domain_name, this.dom_protocol, this.cmplimit);
                  }
               }
            } catch (IOException var16) {
               if (var7) {
                  ntrace.doTrace("*]/dsession(" + this.uid + ")/_tpsend_internal/30/");
               }

               throw new TPException(12, "ERROR: tpsend failed with network error: " + var16);
            }

            if (var7) {
               ntrace.doTrace("]/dsession(" + this.uid + ")/_tpsend_internal/40/");
            }

         }
      }
   }

   public HashMap getRMICallList() {
      return this.rmiCallList;
   }

   public UserRec getCurrentUser(TuxedoConnection var1) {
      boolean var3 = false;
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         if (var1 != null) {
            ntrace.doTrace("[/dsession/getCurrentUser/" + var1.toString());
         } else {
            ntrace.doTrace("[/dsession/getCurrentUser/null tuxUser");
         }

         ntrace.doTrace("cachedUR = " + this.cachedUR);
      }

      UserRec var2;
      if (this.cachedUR && var1 != null) {
         if ((var2 = var1.getUserRecord()) != null) {
            if (var4) {
               ntrace.doTrace("]/dsession/getCurrentUser(10) return " + var2.getRemoteUserName());
            }

            return var2;
         }

         var3 = true;
      }

      TCAuthenticatedUser var5 = TCSecurityManager.getCurrentUser();
      var2 = this.myAppKey.getTuxedoUserRecord(var5);
      if (var3 && var2 != null) {
         var1.setUserRecord(var2);
      }

      if (var4) {
         if (var2 != null) {
            ntrace.doTrace("]/dsession/getCurrentUser(20) return" + var2.getRemoteUserName());
         } else {
            ntrace.doTrace("]/dsession/getCurrentUser(30) return null");
         }
      }

      return var2;
   }

   public void tpsprio(int var1, int var2) throws TPException {
      if ((var2 & -65) != 0) {
         throw new TPException(4, "Bad flags value");
      } else {
         if ((var2 & 64) != 0) {
            if (var1 >= 1 && var1 <= 100) {
               this.tmsndprio = var1;
            } else {
               this.tmsndprio = 50;
            }
         } else if (var1 > 100) {
            this.tmsndprio = 100;
         } else if (var1 < 1) {
            this.tmsndprio = 1;
         } else {
            this.tmsndprio = var1;
         }

      }
   }

   public void sendKeepAliveRequest() {
      this.myLock.lock();
      int var3 = this.kaState;
      this.kaState = 6;
      this.myLock.unlock();
      TdomTcb var1 = new TdomTcb(23, this.allocReqid(), 0, (String)null);
      var1.set_diagnostic(0);
      tfmh var2 = new tfmh(1);
      var2.tdom = new tcm((short)7, var1);

      try {
         synchronized(this.dom_ostream) {
            var2.write_tfmh(this.dom_ostream, this.cmplimit);
         }
      } catch (IOException var7) {
         this.kaState = var3;
      }

      this.myLock.lock();
      this.kaExpTime = TimerEventManager.getClockTick() + (long)this.kas;
      if (this.kaState == 6) {
         if (this.kaws > 0) {
            this.kaState = 2;
         } else {
            this.kaState = 1;
         }
      }

      this.myLock.unlock();
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("...Send DMQUERY(KEEPALIVE) from " + this.local_domain_name + " to " + this.remote_domain_id);
      }

   }

   public void sendKeepAliveAcknowledge() {
      TdomTcb var1 = new TdomTcb(23, this.allocReqid(), 0, (String)null);
      var1.set_diagnostic(1);
      tfmh var2 = new tfmh(1);
      var2.tdom = new tcm((short)7, var1);

      try {
         synchronized(this.dom_ostream) {
            var2.write_tfmh(this.dom_ostream, this.cmplimit);
         }
      } catch (IOException var6) {
      }

      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("...Send DMQUERY(KEEPALIVE_RPLY) from " + this.local_domain_name + " to " + this.remote_domain_id);
      }

   }

   public void startKACountDown() {
      long var1 = TimerEventManager.getClockTick();
      this.myLock.lock();
      this.kaState = 1;
      this.kaExpTime = var1 + (long)this.kas;
      this.myLock.unlock();
   }

   public void updateLastReceiveTime() {
      if (this.kaState != -1 && this.kaState != 5 && this.kaState != 4) {
         long var1 = TimerEventManager.getClockTick();
         this.myLock.lock();
         if (this.kaState != 1) {
            if (this.kaTask != null && this.kaState == 3) {
               this.kaTask.cancel();
            }

            this.kaState = 1;
            this.kawExpTime = 0L;
         }

         if (var1 != this.lastRecvTime) {
            this.lastRecvTime = var1;
            this.kaExpTime = var1 + (long)this.kas;
         }

         this.myLock.unlock();
      }
   }

   public boolean isKATimersExpired(long var1) {
      if (this.kaState != 5 && this.kaState != -1 && this.kaState != 4) {
         this.myLock.lock();
         if (this.kawExpTime > 0L && var1 >= this.kawExpTime) {
            this.kawExpTime = 0L;
            this.myLock.unlock();

            try {
               WTCLogger.logInfoDisconnectNoKeepAliveAck(this.local_domain_name, this.remote_domain_id);
               this.tpterm();
            } catch (TPException var4) {
            }

            return true;
         } else {
            if (this.kaExpTime > 0L && var1 >= this.kaExpTime) {
               if (this.kaState == 1) {
                  this.kaExpTime = var1 + (long)this.kas;
                  this.kaState = 3;
                  if (this.kawExpTime == 0L && this.kaws > 0) {
                     this.kawExpTime = var1 + (long)this.kaws;
                  }

                  this.kaTask = new KeepAliveTask(this);
                  this.asyncTimeService.schedule(this.kaTask, 0L);
               } else if (this.kaState == 2) {
                  this.kaState = 3;
                  this.kaExpTime = var1 + (long)this.kas;
                  this.kaTask = new KeepAliveTask(this);
                  this.asyncTimeService.schedule(this.kaTask, 0L);
               }
            }

            this.myLock.unlock();
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isKeepAliveAvailable() {
      return this.is_connected && this.ka > 0 && (this.kaState == 1 || this.kaState == 2 || this.kaState == 3);
   }

   private class MyListener implements HandshakeCompletedListener {
      private MyListener() {
      }

      public void handshakeCompleted(HandshakeCompletedEvent var1) {
         boolean var2 = ntrace.isTraceEnabled(4);
         if (var2) {
            ntrace.doTrace("Client handshake done. Cipher used: " + var1.getCipherSuite());
         }

      }

      // $FF: synthetic method
      MyListener(Object var2) {
         this();
      }
   }
}
