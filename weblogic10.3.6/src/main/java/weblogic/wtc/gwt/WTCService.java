package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.config.TuxedoConnectorRAP;
import com.bea.core.jatmi.internal.ConfigHelper;
import com.bea.core.jatmi.internal.TCLicenseManager;
import com.bea.core.jatmi.internal.TCRouteManager;
import com.bea.core.jatmi.internal.TCSecurityManager;
import com.bea.core.jatmi.internal.TCTaskHelper;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXA;
import com.bea.core.jatmi.intf.ConfigHelperDelegate;
import com.bea.core.jatmi.intf.TCTask;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import java.lang.Thread.State;
import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.Home;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.iiop.IIOPService;
import weblogic.jndi.Environment;
import weblogic.kernel.ExecuteThread;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.WTCExportMBean;
import weblogic.management.configuration.WTCImportMBean;
import weblogic.management.configuration.WTCLocalTuxDomMBean;
import weblogic.management.configuration.WTCPasswordMBean;
import weblogic.management.configuration.WTCRemoteTuxDomMBean;
import weblogic.management.configuration.WTCResourcesMBean;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.configuration.WTCtBridgeGlobalMBean;
import weblogic.management.configuration.WTCtBridgeRedirectMBean;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.management.internal.ResourceDependentDeploymentHandler;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WTCRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.BetaFeatures;
import weblogic.wtc.jatmi.DomainRegistry;
import weblogic.wtc.jatmi.FldTbl;
import weblogic.wtc.jatmi.Objinfo;
import weblogic.wtc.jatmi.ObjinfoImpl;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.ViewHelper;
import weblogic.wtc.jatmi.gwatmi;
import weblogic.wtc.tbridge.tBexec;
import weblogic.wtc.wls.WlsLicenseService;
import weblogic.wtc.wls.WlsLogService;
import weblogic.wtc.wls.WlsSecurityService;
import weblogic.wtc.wls.WlsTaskManager;
import weblogic.wtc.wls.WlsTransactionService;

public final class WTCService extends RuntimeMBeanDelegate implements ResourceDependentDeploymentHandler, WTCRuntimeMBean, BeanUpdateListener, ConfigHelperDelegate {
   private static Context myNameService;
   private static Timer myTimeService;
   private static Timer asyncTimeService;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Hashtable myImportedServices;
   private Hashtable myExportedServices;
   private Hashtable tmpImportedServices;
   private Hashtable tmpExportedServices;
   private static int myUid;
   private TDMLocalTDomain[] ltd_list;
   private TDMRemoteTDomain[] rtd_list;
   private TDMPasswd[] pwd_list;
   private static TDMResources myGlobalResources;
   private int ltdcnt = 0;
   private int rtdcnt = 0;
   private int pwdcnt = 0;
   private int tBridgeConfig = 0;
   private boolean do_replicate = false;
   private boolean tBridgeStartup = false;
   private boolean tBridgePending = false;
   private boolean xaAffinity = true;
   private static String myPasswordKey = null;
   private static String myEncryptionType = null;
   private static HashMap myOutboundXidMap;
   private static HashMap myInboundXidMap;
   private static HashMap myXidRetryMap;
   private static HashMap myXidReadyMap;
   private static HashMap recoveredXids;
   private static HashMap committedXids;
   private static HashMap preparedXids;
   private static HashMap committingXids;
   private static Map myXidTLogMap;
   private static boolean initialized = false;
   private static BetaFeatures useBetaFeatures;
   private TuxXidRply unknownTxidRply;
   private WTCServerMBean myWtcSrvrMBean = null;
   private Context myDomainContext = null;
   private Random myRandom = null;
   private transient TDMImport[] available_array = null;
   private boolean registered = false;
   private TimerEventManager myTEManager;
   private static WTCService myWTCService = null;
   private static int countWTC = 0;
   private static String myName = null;
   private static final String APPLICATION_QUEUE_NAME = "weblogic.wtc.applicationQueue";
   public static final int DFLT_APPLICATION_QUEUE_SIZE = 10;
   static int applicationQueueId = -1;

   public WTCService() throws ManagementException {
      super("WTCService", false);
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setWTCRuntime(this);
      myWTCService = this;
   }

   public static synchronized WTCService getService() throws ManagementException {
      if (myWTCService == null) {
         myWTCService = new WTCService();
      }

      return myWTCService;
   }

   public static WTCService getWTCService() {
      return myWTCService;
   }

   public static Timer getAsyncTimerService() {
      return asyncTimeService;
   }

   public static String getWTCServerName() {
      return myName;
   }

   public static Timer getTimerService() {
      return myTimeService;
   }

   public static int getApplicationQueueId() {
      return applicationQueueId;
   }

   public TuxXidRply getUnknownTxidRply() {
      return this.unknownTxidRply;
   }

   public int getImplementationId() {
      return 0;
   }

   public long getImplementedCapabilities() {
      return 16383L;
   }

   public String getGlobalMBEncodingMapFile() {
      return getMBEncodingMapFile();
   }

   public String getGlobalRemoteMBEncoding() {
      return getRemoteMBEncoding();
   }

   public String getHomePath() {
      return Home.getPath();
   }

   public Objinfo createObjinfo() {
      return new ObjinfoImpl();
   }

   public OatmialServices getTuxedoServices() {
      return !initialized ? null : new OatmialServices(myNameService, myTimeService, myOutboundXidMap, myInboundXidMap, myXidRetryMap, myXidReadyMap);
   }

   public gwatmi getRAPSession(TuxedoConnectorRAP var1, boolean var2) {
      return ((TDMRemoteTDomain)var1).getTsession(var2);
   }

   void initializeCommon() {
      WlsLogService var1 = new WlsLogService();
      ntrace.init(var1);
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/initializeCommon/");
      }

      WlsLicenseService var3;
      try {
         var3 = new WlsLicenseService();
         TCLicenseManager.initialize(var3);
         ConfigHelper.initialize(this);
         WlsTaskManager var4 = new WlsTaskManager();
         TCTaskHelper.initialize(var4);
         WlsSecurityService var5 = new WlsSecurityService();
         TCSecurityManager.initialize(var5);
         WlsRouteService var6 = new WlsRouteService();
         TCRouteManager.initialize(var6);
         WlsTransactionService var7 = new WlsTransactionService();
         TCTransactionHelper.initialize(var7);
      } catch (TPException var8) {
         ntrace.doTrace("TPException: " + var8.getMessage());
         if (var2) {
            ntrace.doTrace("]/WTCService/initializeCommon return");
         }

         return;
      }

      myPasswordKey = System.getProperty("weblogic.wtc.PasswordKey");
      myEncryptionType = System.getProperty("weblogic.wtc.EncryptionType");
      this.do_replicate = false;
      var3 = null;
      String var9 = System.getProperty("weblogic.wtc.replicateBindings", "false");
      if (var9 != null && var9.equals("true")) {
         this.do_replicate = true;
      }

      var9 = System.getProperty("weblogic.wtc.xaAffinity", "true");
      if (var9 != null && var9.equals("false")) {
         this.xaAffinity = false;
      }

      boolean var10 = false;
      String var12 = System.getProperty("weblogic.wtc.applicationQueueSize");
      if (var12 != null) {
         if (!Kernel.isDispatchPolicy("weblogic.wtc.applicationQueue")) {
            int var11 = Integer.parseInt(var12);
            if (var11 > 0) {
               Kernel.addExecuteQueue("weblogic.wtc.applicationQueue", var11);
               applicationQueueId = Kernel.getDispatchPolicyIndex("weblogic.wtc.applicationQueue");
            }
         } else {
            applicationQueueId = Kernel.getDispatchPolicyIndex("weblogic.wtc.applicationQueue");
         }
      }

      DeploymentHandlerHome.addDeploymentHandler(this);
      if (var2) {
         ntrace.doTrace("]/WTCService/initializeCommon/10/void: DONE");
      }

   }

   void resumeCommon() {
      if (ntrace.isTraceEnabled(2)) {
         ntrace.doTrace("[/WTCService/resumeCommon/");
         ntrace.doTrace("]/WTCService/resumeCommon/00/void: NOT USED");
      }

   }

   void suspend(boolean var1) {
      if (ntrace.isTraceEnabled(2)) {
         ntrace.doTrace("[/WTCService/suspend/force = " + var1);
         ntrace.doTrace("]/WTCService/suspend/00/void: NOT USED");
      }

   }

   void shutdownCommon() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/shutdownCommon/");
      }

      this.shutdownWTC();
      if (var1) {
         ntrace.doTrace("]/WTCService/shutdownCommon/10/void: DONE");
      }

   }

   void shutdownWTC() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/shutdownWTC/");
      }

      this.removeBeanListeners();
      if (this.myTEManager != null) {
         this.myTEManager.shutdown();
         this.myTEManager = null;
      }

      for(int var4 = 0; var4 < this.ltdcnt; ++var4) {
         if (this.ltd_list[var4] != null) {
            OatmialListener var2 = this.ltd_list[var4].getOatmialListener();
            if (var2 != null) {
               var2.shutdown();
               Thread.State var3 = var2.getState();
               if (var3 != State.TERMINATED) {
                  var2.interrupt();
               }
            }
         }
      }

      TCTransactionHelper.unregisterResource();

      for(int var5 = 0; var5 < this.rtdcnt; ++var5) {
         gwatmi var9;
         if (this.rtd_list[var5] != null && (var9 = this.rtd_list[var5].getTsession(false)) != null) {
            try {
               var9.tpterm();
            } catch (TPException var8) {
            }
         }
      }

      try {
         if (myNameService != null) {
            if (var1) {
               ntrace.doTrace("/WTCService/shutdownWTC/UNBINDING.");
            }

            myNameService.unbind("tuxedo.services.TuxedoConnection");
            myNameService.unbind("tuxedo.services.TuxedoCorbaConnection");
         }
      } catch (NamingException var7) {
         WTCLogger.logNEtuxConnFactory(var7);
      }

      if (this.tBridgeStartup) {
         tBexec.tBcancel();
         this.tBridgeStartup = false;
      }

      this.tBridgeConfig = 0;
      if (this.myImportedServices != null) {
         this.myImportedServices.clear();
      }

      if (this.myExportedServices != null) {
         this.myExportedServices.clear();
      }

      if (myOutboundXidMap != null) {
         myOutboundXidMap.clear();
      }

      if (myInboundXidMap != null) {
         myInboundXidMap.clear();
      }

      if (myXidRetryMap != null) {
         myXidRetryMap.clear();
      }

      if (myXidReadyMap != null) {
         myXidReadyMap.clear();
      }

      if (myTimeService != null) {
         myTimeService.cancel();
      }

      if (asyncTimeService != null) {
         asyncTimeService.cancel();
      }

      if (var1) {
         ntrace.doTrace("]/WTCService/shutdownWTC/30/void: DONE.");
      }

   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (ntrace.isTraceEnabled(2)) {
         ntrace.doTrace("[/WTCService/prepareDeployment/");
         ntrace.doTrace("]/WTCService/prepareDeployment/00/void: NOT USED");
      }

   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/activateDeployment/");
      }

      if (countWTC != 0) {
         if (var3) {
            ntrace.doTrace("*]/WTCService/activateDeployment/1/duplicated server");
         }

         throw new DeploymentException("Only one WTC server is allowed per WebLogic Server!");
      } else if (var1.getTargets().length == 0) {
         if (var3) {
            ntrace.doTrace("]/WTCService/activateDeployment/5/target length 0");
         }

      } else if (var1 instanceof WTCServerMBean) {
         try {
            Environment var4 = new Environment();
            var4.setCreateIntermediateContexts(true);
            var4.setReplicateBindings(this.do_replicate);
            myNameService = var4.getInitialContext();
         } catch (NamingException var5) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/activateDeployment/7/naming exception");
            }

            WTCLogger.logNEConfigInfo(var5.getMessage());
            throw new DeploymentException(var5.getMessage());
         }

         this.myWtcSrvrMBean = (WTCServerMBean)var1;
         if (!this.checkWtcSrvrMBean()) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/activateDeployment/10/void: No usable WTCServer");
            }

            throw new DeploymentException("Could not use WTCServerMBean.");
         } else {
            myTimeService = new Timer(true);
            asyncTimeService = new Timer(true);

            try {
               this.startWTC();
            } catch (TPException var6) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/activateDeployment/20: startWTC failure");
               }

               throw new DeploymentException("startWTC failure: " + var6.getMessage());
            }

            for(int var7 = 0; var7 < this.rtdcnt; ++var7) {
               if (this.rtd_list[var7] != null) {
                  this.rtd_list[var7].activateObject();
               }
            }

            countWTC = 1;
            myName = var1.getName();
            if (var3) {
               ntrace.doTrace("]/WTCService/activateDeployment/30/void: WTCServerMBean(" + myName + ") DEPLOYED.");
            }

         }
      } else {
         if (var3) {
            ntrace.doTrace("]/WTCService/activateDeployment/40/void: WTCServerMBean NOT DEPLOYED.");
         }

      }
   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/deactivateDeployment/");
      }

      if (var1 == null) {
         if (var3) {
            ntrace.doTrace("]/WTCService/deactivateDeployment/10/null deployment");
         }

      } else if (var1 instanceof WTCServerMBean && var1.getName().equals(myName)) {
         this.shutdownWTC();
         this.myWtcSrvrMBean = null;
         this.ltd_list = null;
         this.rtd_list = null;
         this.ltdcnt = 0;
         this.rtdcnt = 0;
         countWTC = 0;
         if (var3) {
            ntrace.doTrace("]/WTCService/deactivateDeployment/20/void:(" + var1.getName() + ") UNDEPLOYED.");
         }

      } else {
         if (var3) {
            ntrace.doTrace("]/WTCService/deactivateDeployment/20/No Deployment");
         }

      }
   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (ntrace.isTraceEnabled(2)) {
         ntrace.doTrace("[/WTCService/unprepareDeployment");
         ntrace.doTrace("]/WTCService/unprepareDeployment/00/void: NOT USED");
      }

   }

   public static OatmialServices getOatmialServices() {
      return new OatmialServices(myNameService, myTimeService, myOutboundXidMap, myInboundXidMap, myXidRetryMap, myXidReadyMap);
   }

   public static synchronized int getUniqueGwdsessionId() {
      return myUid++;
   }

   public static String getPasswordKey() {
      return myPasswordKey;
   }

   public static String getEncryptionType() {
      return myEncryptionType;
   }

   public static void addRecoveredXid(Xid var0, String[] var1) {
      if (recoveredXids == null) {
         recoveredXids = new HashMap();
      }

      recoveredXids.put(var0, var1);
   }

   public static void addPreparedXid(Xid var0, String var1, TuxedoLoggable var2) {
      Object[] var3 = new Object[2];
      if (preparedXids == null) {
         preparedXids = new HashMap();
      }

      var3[0] = var1;
      var3[1] = var2;
      preparedXids.put(var0, var3);
   }

   public static void addCommittingXid(Xid var0, String var1, TuxedoLoggable var2) {
      Object[] var3 = null;
      if (preparedXids != null && (var3 = (Object[])((Object[])preparedXids.remove(var0))) != null) {
         TuxedoLoggable var4 = (TuxedoLoggable)var3[1];
         var4.forget();
      }

      if (committingXids == null) {
         committingXids = new HashMap();
      }

      if (var3 == null) {
         var3 = new Object[2];
      }

      var3[0] = var1;
      var3[1] = var2;
      committingXids.put(var0, var3);
   }

   public Xid[] getRecoveredXids() {
      if (recoveredXids != null && recoveredXids.size() != 0) {
         Xid[] var1 = (Xid[])((Xid[])recoveredXids.keySet().toArray(new Xid[recoveredXids.size()]));
         return var1;
      } else {
         return new Xid[0];
      }
   }

   public void forgetRecoveredXid(Xid var1) {
      if (recoveredXids != null && recoveredXids.size() != 0) {
         recoveredXids.remove(var1);
      }
   }

   public void addXidTLogMap(Xid var1, TuxedoLoggable var2) {
      if (myXidTLogMap == null) {
         myXidTLogMap = Collections.synchronizedMap(new HashMap());
      }

      myXidTLogMap.put(var1, var2);
   }

   public static void AddXidTLogMap(Xid var0, TuxedoLoggable var1) {
      if (myXidTLogMap == null) {
         myXidTLogMap = Collections.synchronizedMap(new HashMap());
      }

      myXidTLogMap.put(var0, var1);
   }

   public TuxedoLoggable removeXidTLogMap(Xid var1, int var2) {
      if (myXidTLogMap == null) {
         return null;
      } else {
         TuxedoLoggable var3 = (TuxedoLoggable)myXidTLogMap.get(var1);
         if (var3 == null) {
            return null;
         } else if (var3.getType() == var2) {
            myXidTLogMap.remove(var1);
            return var3;
         } else {
            return null;
         }
      }
   }

   public static void addCommittedXid(Xid var0, String[] var1) {
      if (committedXids == null) {
         committedXids = new HashMap();
      }

      committedXids.put(var0, var1);
   }

   public synchronized TDMPasswd getTDMPasswd(String var1, String var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/getTDMPasswd/lap " + var1 + ", RAP " + var2);
      }

      if (var1 != null && var2 != null && this.pwdcnt != 0) {
         for(int var6 = 0; var6 < this.pwdcnt; ++var6) {
            String var4 = this.pwd_list[var6].getLocalAccessPoint();
            String var5 = this.pwd_list[var6].getRemoteAccessPoint();
            if (var4.equals(var1) && var5.equals(var2)) {
               if (var3) {
                  ntrace.doTrace("]/WTCService/getTDMPasswd/20/" + var6);
               }

               return this.pwd_list[var6];
            }
         }

         if (var3) {
            ntrace.doTrace("]/WTCService/getTDMPasswd/30/null, not found");
         }

         return null;
      } else {
         if (var3) {
            ntrace.doTrace("]/WTCService/getTDMPasswd/10/null");
         }

         return null;
      }
   }

   public synchronized TDMRemoteTDomain getRemoteTDomain(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getRemoteTDomain/" + var1);
      }

      if (var1 != null && this.rtdcnt != 0) {
         for(int var3 = 0; var3 < this.rtdcnt; ++var3) {
            if (var1.equals(this.rtd_list[var3].getAccessPointId())) {
               if (var2) {
                  ntrace.doTrace("</WTCService/getRemoteTDomain/20/" + this.rtd_list[var3].getAccessPoint());
               }

               return this.rtd_list[var3];
            }
         }

         if (var2) {
            ntrace.doTrace("]/WTCService/getRemoteTDomain/30/null, not found");
         }

         return null;
      } else {
         if (var2) {
            ntrace.doTrace("]/WTCService/getRemoteTDomain/10/null");
         }

         return null;
      }
   }

   public static String getAppPasswordPWD() {
      return myGlobalResources != null ? myGlobalResources.getAppPassword() : null;
   }

   public static String getAppPasswordIV() {
      return myGlobalResources != null ? myGlobalResources.getAppPasswordIV() : null;
   }

   public static String getGlobalTpUsrFile() {
      return myGlobalResources != null ? myGlobalResources.getTpUsrFile() : null;
   }

   public static String getRemoteMBEncoding() {
      return myGlobalResources != null ? myGlobalResources.getRemoteMBEncoding() : null;
   }

   public static String getMBEncodingMapFile() {
      return myGlobalResources != null ? myGlobalResources.getMBEncodingMapFile() : null;
   }

   public static FldTbl[] getFldTbls(String var0) {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/getFldTbls/" + var0);
      }

      if (null == myGlobalResources) {
         return null;
      } else {
         if (var0 != null) {
            if (var0.equals("fml16")) {
               if (var1) {
                  ntrace.doTrace("]/WTCService/getFldTbls/10/FldTbl[]");
               }

               return myGlobalResources.getFieldTables(false);
            }

            if (var0.equals("fml32")) {
               if (var1) {
                  ntrace.doTrace("]/WTCService/getFldTbls/15/FldTbl[]");
               }

               return myGlobalResources.getFieldTables(true);
            }
         }

         WTCLogger.logErrorBadGetFldTblsType(var0);
         if (var1) {
            ntrace.doTrace("]/WTCService/getFldTbls/20/null");
         }

         return null;
      }
   }

   public boolean checkWtcSrvrMBean() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/checkWtcSrvrMBean/");
      }

      if (this.myWtcSrvrMBean.getTargets().length != 1) {
         if (var1) {
            ntrace.doTrace("/WTCService/checkWtcSrvrMBean/10/WTCServerMBean has multi targets");
         }

         if (var1) {
            ntrace.doTrace("/WTCService/checkWtcSrvrMBean/30/false.");
         }

         return false;
      } else {
         if (var1) {
            ntrace.doTrace("]/WTCService/checkWtcSrvrMBean/20/true");
         }

         return true;
      }
   }

   public void startWTC() throws TPException {
      boolean var1 = ntrace.isMixedTraceEnabled(18);
      if (var1) {
         ntrace.doTrace("[/WTCService/startWTC/");
      }

      XAResource var20 = null;
      boolean var24 = true;
      new DomainRegistry();
      this.myImportedServices = new Hashtable();
      this.myExportedServices = new Hashtable();
      this.tmpImportedServices = new Hashtable();
      this.tmpExportedServices = new Hashtable();
      TCLicenseManager.updateInstalledEncryptionInfo();
      useBetaFeatures = new BetaFeatures(true, true);
      this.unknownTxidRply = new TuxXidRply(new TxidHandlerFactory());

      try {
         WTCLogger.logInfoStartConfigParse();
         this.extractInfo();
         WTCLogger.logInfoDoneConfigParse();
      } catch (TPException var33) {
         WTCLogger.logTPEConfigError("extractInfo: " + var33);
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/10/extractInfo failure");
         }

         throw var33;
      }

      int var27;
      for(var27 = 0; var27 < this.ltdcnt; ++var27) {
         if (this.ltd_list[var27] != null) {
            this.startOatmialListener(this.ltd_list[var27]);
         }
      }

      this.myTEManager = new TimerEventManager(this);
      this.myTEManager.setDaemon(true);
      this.myTEManager.start();
      if (var1) {
         ntrace.doTrace("create tuxedo subcontext");
      }

      Loggable var28;
      try {
         myNameService.createSubcontext("tuxedo");
      } catch (NameAlreadyBoundException var40) {
      } catch (InvalidAttributesException var41) {
         var28 = WTCLogger.logIAEcreateSubCntxtLoggable(var41);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/20/invalid attributes");
         }

         throw new TPException(12, var28.getMessage());
      } catch (NamingException var42) {
         var28 = WTCLogger.logNEcreateSubCntxtLoggable(var42);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/30/naming exception");
         }

         throw new TPException(12, var28.getMessage());
      }

      if (var1) {
         ntrace.doTrace("Setting up federation points");
      }

      try {
         if (var1) {
            ntrace.doTrace("create tuxedo.domains subcontext");
         }

         try {
            this.myDomainContext = myNameService.createSubcontext("tuxedo.domains");
         } catch (NameAlreadyBoundException var31) {
         }

         for(var27 = 0; var27 < this.rtdcnt; ++var27) {
            this.federateRemoteDomain(this.myDomainContext, this.rtd_list[var27]);
         }
      } catch (InvalidAttributesException var46) {
         var28 = WTCLogger.logIAEcreateSubCntxtLoggable(var46);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/40/invalid attribute");
         }

         throw new TPException(12, var28.getMessage());
      } catch (NamingException var47) {
         var28 = WTCLogger.logNEcreateSubCntxtLoggable(var47);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/50/Naming Exception");
         }

         throw new TPException(12, var28.getMessage());
      }

      if (var1) {
         ntrace.doTrace("create tuxedo.services subcontext");
      }

      try {
         myNameService.createSubcontext("tuxedo.services");
      } catch (NameAlreadyBoundException var34) {
      } catch (InvalidAttributesException var35) {
         var28 = WTCLogger.logIAEcreateSubCntxtLoggable(var35);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/60/invalid attributes");
         }

         throw new TPException(12, var28.getMessage());
      } catch (NamingException var36) {
         var28 = WTCLogger.logNEcreateSubCntxtLoggable(var36);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/70/naming exception");
         }

         throw new TPException(12, var28.getMessage());
      }

      if (var1) {
         ntrace.doTrace("bind nameservices");
      }

      TuxedoConnectionFactory var3 = new TuxedoConnectionFactory();
      TuxedoCorbaConnectionFactory var4 = new TuxedoCorbaConnectionFactory();

      try {
         myNameService.bind("tuxedo.services.TuxedoConnection", var3);
         myNameService.bind("tuxedo.services.TuxedoCorbaConnection", var4);
      } catch (NameAlreadyBoundException var37) {
         var28 = WTCLogger.logNABEtuxConnFactoryLoggable(var37);
         var28.log();
      } catch (InvalidAttributesException var38) {
         var28 = WTCLogger.logIAEtuxConnFactoryLoggable(var38);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/70/invalid attrib");
         }

         throw new TPException(12, var28.getMessage());
      } catch (NamingException var39) {
         var28 = WTCLogger.logNEtuxConnFactoryLoggable(var39);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/80/Naming Exception");
         }

         throw new TPException(12, var28.getMessage());
      }

      myOutboundXidMap = new HashMap();
      myInboundXidMap = new HashMap();
      myXidRetryMap = new HashMap();
      myXidReadyMap = new HashMap();
      TuxedoXA var5 = new TuxedoXA(new OatmialServices(myNameService, myTimeService, myOutboundXidMap, myInboundXidMap, myXidRetryMap, myXidReadyMap));
      if (var1) {
         ntrace.doTrace("register transaction resource");
      }

      try {
         TCTransactionHelper.registerResource(var5);
      } catch (SystemException var43) {
         var28 = WTCLogger.logSEgetTranMgrLoggable(var43);
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/90/unable register with TM");
         }

         throw new TPException(12, var28.getMessage());
      }

      Set var6;
      Iterator var7;
      Map.Entry var8;
      Xid var9;
      String[] var10;
      int var11;
      int var12;
      OatmialServices var16;
      String var17;
      if (recoveredXids != null && recoveredXids.size() != 0) {
         if (var1) {
            ntrace.doTrace("recovering " + recoveredXids.size() + " transactions");
         }

         var16 = getOatmialServices();
         var6 = recoveredXids.entrySet();
         var7 = var6.iterator();

         label578:
         while(true) {
            do {
               do {
                  if (!var7.hasNext()) {
                     break label578;
                  }

                  var8 = (Map.Entry)var7.next();
               } while((var9 = (Xid)var8.getKey()) == null);
            } while((var10 = (String[])((String[])var8.getValue())) == null);

            for(var11 = 0; var11 < var10.length; ++var11) {
               var17 = var10[var11];

               for(var12 = 0; var12 < this.rtd_list.length && !var17.equals(this.rtd_list[var12].getAccessPoint()); ++var12) {
               }

               if (var12 >= this.rtd_list.length) {
                  WTCLogger.logErrorTranId(var17);
               } else {
                  var16.addOutboundRdomToXid(var9, this.rtd_list[var12]);
               }
            }
         }
      }

      if (committedXids != null && committedXids.size() != 0) {
         if (var1) {
            ntrace.doTrace("/WTCService/startWTC/committed " + committedXids.size() + " transactions");
         }

         var16 = getOatmialServices();
         HashSet var49 = new HashSet();
         var6 = committedXids.entrySet();
         var7 = var6.iterator();

         label548:
         while(true) {
            do {
               do {
                  if (!var7.hasNext()) {
                     var7 = var49.iterator();

                     while(var7.hasNext()) {
                        TDMRemoteTDomain var50 = (TDMRemoteTDomain)var7.next();
                        if (var50 != null) {
                           var50.getTsession(true);
                        }
                     }

                     var6 = committedXids.entrySet();
                     var7 = var6.iterator();

                     while(var7.hasNext()) {
                        var8 = (Map.Entry)var7.next();
                        if ((var9 = (Xid)var8.getKey()) != null && (String[])((String[])var8.getValue()) != null) {
                           var28 = null;

                           MBeanHome var51;
                           try {
                              var51 = (MBeanHome)myNameService.lookup("weblogic.management.adminhome");
                           } catch (NamingException var30) {
                              throw new TPException(12, var30.getMessage());
                           }

                           DomainMBean var29 = var51.getActiveDomain();
                           OatmialCommitter var15 = new OatmialCommitter(var9, var5, var29.getJTA().getAbandonTimeoutSeconds(), myTimeService);
                           TCTaskHelper.schedule(var15);
                        }
                     }
                     break label548;
                  }

                  var8 = (Map.Entry)var7.next();
               } while((var9 = (Xid)var8.getKey()) == null);
            } while((var10 = (String[])((String[])var8.getValue())) == null);

            for(var11 = 0; var11 < var10.length; ++var11) {
               var17 = var10[var11];

               for(var12 = 0; var12 < this.rtd_list.length && !var17.equals(this.rtd_list[var12].getAccessPoint()); ++var12) {
               }

               if (var12 >= this.rtd_list.length) {
                  WTCLogger.logErrorTranId(var17);
               } else {
                  var16.addOutboundRdomToXid(var9, this.rtd_list[var12]);
                  var49.add(this.rtd_list[var12]);
               }
            }
         }
      }

      committedXids = null;
      if ((var20 = TCTransactionHelper.getXAResource()) == null) {
         WTCLogger.logErrorNoTransactionManager();
      }

      Xid[] var21;
      try {
         var21 = var20.recover(25165824);
         if (var1) {
            if (var21 != null) {
               ntrace.doTrace("/WTCService/startWTC/recovered " + var21.length + " transactions");
            } else {
               ntrace.doTrace("/WTCService/startWTC/recover returned null");
            }
         }
      } catch (XAException var45) {
         WTCLogger.logWarningXaRecoverFailed();
         if (var1) {
            ntrace.doTrace("/WTCService/startWTC/recover failed/" + var45);
         }

         var21 = null;
      }

      int var13;
      if (var21 != null) {
         for(var13 = 0; var13 < var21.length; ++var13) {
            if (var21[var13].getFormatId() != TCTransactionHelper.getXidFormatId()) {
               var21[var13] = null;
            }
         }
      }

      int var14;
      TuxedoLoggable var18;
      OatmialInboundRecover var19;
      byte[] var22;
      byte[] var23;
      byte var25;
      Object[] var26;
      int var48;
      if (preparedXids != null && preparedXids.size() != 0) {
         if (var1) {
            ntrace.doTrace("preparing " + preparedXids.size() + " transactions");
         }

         var16 = getOatmialServices();
         var6 = preparedXids.entrySet();
         var7 = var6.iterator();

         label493:
         while(true) {
            do {
               do {
                  do {
                     if (!var7.hasNext()) {
                        break label493;
                     }

                     var8 = (Map.Entry)var7.next();
                  } while((var9 = (Xid)var8.getKey()) == null);
               } while((var26 = (Object[])((Object[])var8.getValue())) == null);
            } while((var17 = (String)var26[0]) == null);

            var18 = (TuxedoLoggable)var26[1];
            var48 = -1;

            for(var12 = 0; var12 < this.rtd_list.length; ++var12) {
               if (var17.equals(this.rtd_list[var12].getAccessPoint())) {
                  var48 = var12;
                  break;
               }
            }

            if (var12 >= this.rtd_list.length) {
               WTCLogger.logErrorTranId(var17);
            } else {
               var16.addInboundRdomToXid(var9, this.rtd_list[var12]);
               var16.addXidToReadyMap(var9);
            }

            var25 = 3;
            if (var21 != null) {
               for(var13 = 0; var13 < var21.length; ++var13) {
                  if (var21[var13] != null) {
                     var22 = var21[var13].getGlobalTransactionId();
                     var23 = var9.getGlobalTransactionId();
                     if (var22.length == var23.length) {
                        for(var14 = 0; var14 < var22.length && var22[var14] == var23[var14]; ++var14) {
                        }

                        if (var14 >= var22.length) {
                           var25 = 1;
                           var21[var13] = null;
                           break;
                        }
                     }
                  }
               }
            }

            var19 = new OatmialInboundRecover(var9, var25, this.rtd_list[var48], myTimeService, var18);
            this.runRecoverObject(var19);
            var19 = null;
         }
      }

      preparedXids = null;
      if (committingXids != null && committingXids.size() != 0) {
         if (var1) {
            ntrace.doTrace("committing " + committingXids.size() + " transactions");
         }

         var16 = getOatmialServices();
         var6 = committingXids.entrySet();
         var7 = var6.iterator();

         label441:
         while(true) {
            do {
               do {
                  do {
                     if (!var7.hasNext()) {
                        break label441;
                     }

                     var8 = (Map.Entry)var7.next();
                  } while((var9 = (Xid)var8.getKey()) == null);
               } while((var26 = (Object[])((Object[])var8.getValue())) == null);
            } while((var17 = (String)var26[0]) == null);

            var18 = (TuxedoLoggable)var26[1];
            var48 = -1;

            for(var12 = 0; var12 < this.rtd_list.length; ++var12) {
               if (var17.equals(this.rtd_list[var12].getAccessPoint())) {
                  var48 = var12;
                  break;
               }
            }

            if (var12 >= this.rtd_list.length) {
               WTCLogger.logErrorTranId(var17);
            } else {
               var16.addInboundRdomToXid(var9, this.rtd_list[var12]);
            }

            var25 = 4;
            if (var21 != null) {
               for(var13 = 0; var13 < var21.length; ++var13) {
                  if (var21[var13] != null) {
                     var22 = var21[var13].getGlobalTransactionId();
                     var23 = var9.getGlobalTransactionId();
                     if (var22.length == var23.length) {
                        for(var14 = 0; var14 < var22.length && var22[var14] == var23[var14]; ++var14) {
                        }

                        if (var14 >= var22.length) {
                           var25 = 2;
                           var21[var13] = null;
                           break;
                        }
                     }
                  }
               }
            }

            var19 = new OatmialInboundRecover(var9, var25, this.rtd_list[var48], myTimeService, var18);
            this.runRecoverObject(var19);
            var19 = null;
         }
      }

      committingXids = null;
      if (var21 != null) {
         var25 = 0;

         for(var13 = 0; var13 < var21.length; ++var13) {
            if (var21[var13] != null) {
               var19 = new OatmialInboundRecover(var21[var13], var25, (TDMRemote)null, myTimeService, (TuxedoLoggable)null);
               this.runRecoverObject(var19);
            }
         }
      }

      if (ManagementService.getRuntimeAccess(kernelId).getServer().isTGIOPEnabled()) {
         IIOPService.setTGIOPEnabled(true);
      }

      initialized = true;
      if (var1) {
         ntrace.doTrace("tBridgeStartup = " + this.tBridgeStartup + ", tBridgeConfig = " + this.tBridgeConfig);
      }

      if (!this.tBridgeStartup && (this.tBridgeConfig & 1) == 1 && this.tBridgeConfig >= 3) {
         if (var1) {
            ntrace.doTrace("start tBridge ...");
         }

         try {
            tBexec.tBmain(this.myWtcSrvrMBean);
            if (var1) {
               ntrace.doTrace("tBridge started.");
            }

            this.tBridgeStartup = true;
         } catch (TPException var44) {
            if (var44.gettperrno() == 4) {
               if (var1) {
                  ntrace.doTrace("*]/WTCService/startWTC/TBRIDGE config failure");
               }

               throw var44;
            }

            if (var1) {
               ntrace.doTrace("/WTCService/startWTC/TBRIDGE failed to execute...");
            }
         }
      }

      try {
         this.registerBeanListeners();
      } catch (ManagementException var32) {
         var28 = WTCLogger.logErrorNotificationRegistrationLoggable();
         var28.log();
         if (var1) {
            ntrace.doTrace("*]/WTCService/startWTC/100/Register listeners failure");
         }

         throw new TPException(12, var28.getMessage());
      }

      if (var1) {
         ntrace.doTrace("/WTCService/startWTC/110/WTC Service started...");
      }

   }

   private void runRecoverObject(final OatmialInboundRecover var1) {
      TCTaskHelper.schedule(new TCTask() {
         public int execute() {
            try {
               var1.execute((ExecuteThread)null);
               return 0;
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         }

         public void setTaskName(String var1x) {
         }

         public String getTaskName() {
            return "runRecoverObject$unknown";
         }
      });
   }

   private void federateRemoteDomain(Context var1, TDMRemoteTDomain var2) throws NamingException {
      String var3 = var2.getAccessPoint();
      String var4 = var2.getFederationURL();
      String var5 = var2.getFederationName();
      boolean var7 = ntrace.isTraceEnabled(2);
      String var6;
      if (var4 != null && var4.length() != 0) {
         var6 = var4;
      } else {
         var6 = "tgiop://" + var2.getAccessPoint();
      }

      String var8 = var3;
      if (var4 != null && var4.length() != 0) {
         StringTokenizer var9 = new StringTokenizer(var5, "./");
         var1 = myNameService;

         for(int var10 = var9.countTokens(); var10 > 1; --var10) {
            try {
               var1 = var1.createSubcontext(var9.nextToken());
            } catch (NameAlreadyBoundException var12) {
            }
         }

         var8 = var9.nextToken();
      }

      Reference var13 = new Reference("javax.naming.Context", new StringRefAddr("URL", var6));
      var1.rebind(var8, var13);
      if (var7) {
         ntrace.doTrace("Federating [" + var8 + "] to [" + var6 + "]");
      }

   }

   public static boolean isInitialized() {
      return initialized;
   }

   public static BetaFeatures canUseBetaFeatures() {
      return useBetaFeatures;
   }

   private void extractInfo() throws TPException {
      boolean var1 = ntrace.isMixedTraceEnabled(18);
      if (var1) {
         ntrace.doTrace("[/WTCService/extractInfo/");
      }

      if (var1) {
         ntrace.doTrace("Start LocTuxDom MBeans");
      }

      WTCServerMBean var2 = this.myWtcSrvrMBean;
      WTCLocalTuxDomMBean[] var3 = var2.getLocalTuxDoms();
      this.ltdcnt = var3.length;
      if (this.ltdcnt != 0) {
         this.ltd_list = new TDMLocalTDomain[this.ltdcnt];
         if (var1) {
            ntrace.doTrace("ltdcnt=" + this.ltdcnt);
         }

         for(int var4 = 0; var4 < this.ltdcnt; ++var4) {
            try {
               TDMLocalTDomain var5 = TDMLocalTDomain.create(var3[var4]);
               this.ltd_list[var4] = var5;
               var5.prepareObject();
            } catch (TPException var13) {
               WTCLogger.logErrorExecMBeanDefLoggable(var3[var4].getName());
               if (var1) {
                  ntrace.doTrace("*]/WTCService/extractInfo/40/setup error " + var3[var4].getName());
               }

               throw var13;
            }
         }

         if (var1) {
            ntrace.doTrace("Done LocalTuxDom MBeans");
         }
      } else if (var1) {
         ntrace.doTrace("No LocalTuxDom MBeans");
      }

      if (var1) {
         ntrace.doTrace("Start RemoteTuxDom MBeans");
      }

      WTCRemoteTuxDomMBean[] var19 = var2.getRemoteTuxDoms();
      this.rtdcnt = var19.length;
      if (this.rtdcnt != 0) {
         this.rtd_list = new TDMRemoteTDomain[this.rtdcnt];
         if (var1) {
            ntrace.doTrace("rtdcnt=" + this.rtdcnt);
         }
      }

      for(int var20 = 0; var20 < this.rtdcnt; ++var20) {
         try {
            TDMRemoteTDomain var6 = this.setupTDMRemoteTD(var19[var20]);
            this.rtd_list[var20] = var6;
            var6.prepareObject();
         } catch (TPException var14) {
            WTCLogger.logErrorExecMBeanDef(var19[var20].getName());
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/70/setup error " + var19[var20].getName());
            }

            throw var14;
         }
      }

      if (var1) {
         ntrace.doTrace("Done RemoteTuxDom MBeans");
      }

      if (var1) {
         ntrace.doTrace("Start Export MBeans");
      }

      WTCExportMBean[] var21 = var2.getExports();

      for(int var22 = 0; var22 < var21.length; ++var22) {
         try {
            TDMExport var7 = this.setupTDMExport(var21[var22]);
            this.validateExport(var7);
            this.addExport(var7);
            var7.prepareObject();
         } catch (TPException var15) {
            WTCLogger.logErrorExecMBeanDef(var21[var22].getName());
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/90/setup error " + var21[var22].getName());
            }

            throw var15;
         }
      }

      if (var1) {
         ntrace.doTrace("Done Export MBeans");
      }

      if (var1) {
         ntrace.doTrace("Start Import MBeans");
      }

      WTCImportMBean[] var23 = var2.getImports();

      for(int var24 = 0; var24 < var23.length; ++var24) {
         TDMImport var8;
         try {
            var8 = this.setupTDMImport(var23[var24]);
            this.validateImport(var8);
         } catch (TPException var17) {
            WTCLogger.logErrorExecMBeanDef(var23[var24].getName());
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/110/setup error " + var23[var24].getName());
            }

            throw var17;
         }

         if (!this.addImport(var8)) {
            Loggable var9 = WTCLogger.logErrorDupImpSvcLoggable(var23[var24].getName());
            var9.log();
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/115/add export error " + var23[var24].getName());
            }

            throw new TPException(4, var9.getMessage());
         }

         var8.prepareObject();
      }

      if (var1) {
         ntrace.doTrace("Done Import MBeans");
      }

      if (var1) {
         ntrace.doTrace("Start Password MBeans");
      }

      WTCPasswordMBean[] var25 = var2.getPasswords();
      this.pwdcnt = var25.length;
      if (this.pwdcnt != 0) {
         this.pwd_list = new TDMPasswd[this.pwdcnt];
         if (var1) {
            ntrace.doTrace("pwdcnt=" + this.pwdcnt);
         }
      }

      for(int var26 = 0; var26 < var25.length; ++var26) {
         try {
            TDMPasswd var28 = this.setupTDMPasswd(var25[var26]);
            this.pwd_list[var26] = var28;
            var28.prepareObject();
         } catch (TPException var18) {
            WTCLogger.logErrorExecMBeanDef(var25[var26].getName());
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/140/setup error " + var25[var26].getName());
            }

            throw var18;
         }
      }

      if (var1) {
         ntrace.doTrace("Done Password MBeans");
      }

      if (var1) {
         ntrace.doTrace("Start Resource MBean");
      }

      WTCResourcesMBean var27 = var2.getWTCResources();
      if (var27 != null) {
         try {
            myGlobalResources = TDMResources.create(var27);
            myGlobalResources.prepareObject();
         } catch (TPException var16) {
            WTCLogger.logErrorExecMBeanDef(var27.getName());
            if (var1) {
               ntrace.doTrace("/WTCService/extractInfo/160/TPException" + var16.getMessage());
            }

            throw var16;
         }
      } else if (var1) {
         ntrace.doTrace("No Resource MBean found");
      }

      if (var1) {
         ntrace.doTrace("Done Resource MBean");
      }

      WTCtBridgeGlobalMBean var29 = var2.getWTCtBridgeGlobal();
      if (var29 != null) {
         ++this.tBridgeConfig;
      } else if (var1) {
         ntrace.doTrace("No tBridgeGlobal found");
      }

      WTCtBridgeRedirectMBean[] var10 = var2.gettBridgeRedirects();
      if (var10.length != 0) {
         this.tBridgeConfig += 2 * var10.length;
      } else if (var1) {
         ntrace.doTrace("No tBridgeRedirect found");
      }

      if ((this.tBridgeConfig & 1) == 1 && this.tBridgeConfig >= 3) {
         if (var1) {
            ntrace.doTrace("tBridge Enabled.");
         }
      } else if (var1) {
         ntrace.doTrace("tBridge Not Enabled.");
      }

      if (this.rtd_list != null) {
         try {
            this.crossChecking();
         } catch (TPException var12) {
            if (var1) {
               ntrace.doTrace("*]/WTCService/extractInfo/180/crossChecking error");
            }

            throw var12;
         }

         if (var1) {
            ntrace.doTrace("process RTD with length = " + this.rtd_list.length);
         }

         for(int var11 = 0; var11 < this.rtd_list.length; ++var11) {
            this.rtd_list[var11].onTerm(1);
         }
      }

      if (var1) {
         ntrace.doTrace("]/WTCService/extractInfo/190/DONE");
      }

   }

   public synchronized TDMLocalTDomain getLocalDomain(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getLocalDomain/" + var1);
      }

      int var3 = this.getLTDindex(var1);
      if (var3 >= 0) {
         if (var2) {
            ntrace.doTrace("]/WTCService/getLocalDomain/05/" + var3);
         }

         return this.ltd_list[var3];
      } else {
         if (var2) {
            ntrace.doTrace("]/WTCService/getLocalDomain/10/null");
         }

         return null;
      }
   }

   private synchronized int getLTDindex(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getLTDindex/");
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/WTCService/getLTDindex/05/-1");
         }

         return -1;
      } else {
         for(int var3 = 0; var3 < this.ltdcnt; ++var3) {
            if (this.ltd_list[var3].getAccessPoint().equals(var1)) {
               if (var2) {
                  ntrace.doTrace("]/WTCService/getLTDindex/10/" + var3);
               }

               return var3;
            }
         }

         if (var2) {
            ntrace.doTrace("]/WTCService/getLTDindex/20/-1");
         }

         return -1;
      }
   }

   private synchronized int getRTDindex(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getRTDindex/");
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/WTCService/getRTDindex/05/-1");
         }

         return -1;
      } else {
         for(int var3 = 0; var3 < this.rtdcnt; ++var3) {
            if (this.rtd_list[var3].getAccessPoint().equals(var1)) {
               if (var2) {
                  ntrace.doTrace("]/WTCService/getRTDindex/10/" + var3);
               }

               return var3;
            }
         }

         if (var2) {
            ntrace.doTrace("]/WTCService/getRTDindex/20/-1");
         }

         return -1;
      }
   }

   public synchronized TDMRemoteTDomain getRTDbyAP(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getRTDbyAP/" + var1 + " rtdcnt " + this.rtdcnt);
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("]/WTCService/getRTDbyAP/05/null argument");
         }

         return null;
      } else {
         for(int var3 = 0; var3 < this.rtdcnt; ++var3) {
            if (this.rtd_list[var3].getAccessPoint().equals(var1)) {
               if (var2) {
                  ntrace.doTrace("]/WTCService/getRTDbyAP/10/found it");
               }

               return this.rtd_list[var3];
            }
         }

         if (var2) {
            ntrace.doTrace("]/WTCService/getRTDbyAP/15/did not find it");
         }

         return null;
      }
   }

   public synchronized TDMRemoteTDomain getVTDomainSession(String var1, String var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/getVTDomainSession/(" + var1 + ", " + var2 + "), rtdcnt " + this.rtdcnt);
      }

      if (var1 != null && var2 != null) {
         for(int var4 = 0; var4 < this.rtdcnt; ++var4) {
            if (this.rtd_list[var4].getAccessPoint().equals(var2) && this.rtd_list[var4].getLocalAccessPoint().equals(var1)) {
               if (var3) {
                  ntrace.doTrace("]/WTCService/getVTDomainSession/20/found it");
               }

               return this.rtd_list[var4];
            }
         }

         if (var3) {
            ntrace.doTrace("]/WTCService/getVTDomainSession/30/did not find it");
         }

         return null;
      } else {
         if (var3) {
            ntrace.doTrace("]/WTCService/getVTDomainSession/10/null argument");
         }

         return null;
      }
   }

   private void crossChecking() throws TPException {
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      boolean var6 = ntrace.isMixedTraceEnabled(18);
      if (var6) {
         ntrace.doTrace("[/WTCService/crossCheck/");
      }

      int var1;
      RDomainListEntry var8;
      for(var1 = 0; var1 < this.rtd_list.length; ++var1) {
         TDMRemoteTDomain var3 = this.rtd_list[var1];

         try {
            var3.checkConfigIntegrity();
         } catch (TPException var14) {
            if (var6) {
               ntrace.doTrace("*]/WTCService/crossCheck/10/RDOM checkConfigInteg");
            }

            throw var14;
         }

         var4.put(var3.getAccessPointId(), var3);
         RDomainListEntry var7 = new RDomainListEntry(var3);
         if ((var8 = (RDomainListEntry)var5.put(var3.getAccessPointId(), var7)) != null) {
            var7.setNext(var8);
         }
      }

      for(var1 = 0; var1 < this.ltd_list.length; ++var1) {
         TDMLocalTDomain var2 = this.ltd_list[var1];

         try {
            var2.checkConfigIntegrity();
         } catch (TPException var13) {
            if (var6) {
               ntrace.doTrace("*]/WTCService/crossCheck/20/LDOM checkConfigInteg");
            }

            throw var13;
         }

         if (var4.put(var2.getAccessPointId(), var2) != null) {
            Loggable var15 = WTCLogger.logErrorDuplicatedLocalDomainLoggable(var2.getAccessPointId());
            var15.log();
            if (var6) {
               ntrace.doTrace("*]/WTCService/crossCheck/30/duplicated LDOM");
            }

            throw new TPException(4, var15.getMessage());
         }
      }

      Iterator var16 = var5.values().iterator();

      while(var16.hasNext()) {
         var8 = (RDomainListEntry)var16.next();
         HashMap var9 = new HashMap();

         while(true) {
            TDMRemoteTDomain var10 = var8.getRDom();
            TDMLocal var11 = var10.getLocalAccessPointObject();
            if (var11 == null) {
               return;
            }

            if (var9.put(var11.getAccessPointId(), var11) != null) {
               Loggable var12 = WTCLogger.logErrorDuplicatedRemoteDomainLoggable(var10.getAccessPointId(), var11.getAccessPointId());
               var12.log();
               if (var6) {
                  ntrace.doTrace("*]/WTCService/crossCheck/40/duplicated RDOM");
               }

               throw new TPException(4, var12.getMessage());
            }

            if ((var8 = var8.getNext()) == null) {
               break;
            }
         }
      }

      if (var6) {
         ntrace.doTrace("]/WTCService/crossCheck/50/success");
      }

   }

   public boolean updateRuntimeViewList(String var1, Class var2, int var3) {
      boolean var4 = ntrace.isTraceEnabled(2);
      if (var4) {
         ntrace.doTrace("[/WTCService/update_runtime_viewlist/view=" + var1);
      }

      new ViewHelper();
      ViewHelper var5 = ViewHelper.getInstance();
      var5.setViewClass(var1, var2);
      if (var4) {
         ntrace.doTrace("]/WTCService/update_runtime_viewlist/10/true");
      }

      return true;
   }

   public String[] parseCommaSeparatedList(String var1) {
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/parseCommaSeparatedList/" + var1);
      }

      String[] var2;
      if (var1.indexOf(44) == -1) {
         var2 = new String[]{var1};
      } else {
         StringTokenizer var4 = new StringTokenizer(var1, ",");
         var2 = new String[var4.countTokens()];

         for(int var5 = 0; var4.hasMoreTokens(); ++var5) {
            var2[var5] = var4.nextToken();
            if (var3) {
               ntrace.doTrace("token " + var5 + ": " + var2[var5]);
            }
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/parseCommaSeparatedList/10/");
      }

      return var2;
   }

   private boolean validateLocalTuxDom(TDMLocalTDomain var1) {
      String var2 = var1.getAccessPoint();
      String var3 = var1.getAccessPointId();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/validateLocalTuxDom/" + var2);
      }

      for(int var5 = 0; var5 < this.ltdcnt; ++var5) {
         if (var2.equals(this.ltd_list[var5].getAccessPoint()) || var3.equals(this.ltd_list[var5].getAccessPointId())) {
            if (var4) {
               ntrace.doTrace("]/WTCService/validateLocalTuxDom/10/false");
            }

            return false;
         }
      }

      if (var4) {
         ntrace.doTrace("]/WTCService/validateLocalTuxDom/10/success");
      }

      return true;
   }

   private boolean addLocalTuxDom(TDMLocalTDomain var1) {
      String var2 = var1.getAccessPoint();
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/addLocalTuxDom/" + var2);
      }

      if (this.ltdcnt == 0) {
         this.ltd_list = new TDMLocalTDomain[1];
      } else {
         TDMLocalTDomain[] var4 = this.ltd_list;
         this.ltd_list = new TDMLocalTDomain[this.ltdcnt + 1];
         System.arraycopy(var4, 0, this.ltd_list, 0, this.ltdcnt);
      }

      this.ltd_list[this.ltdcnt] = var1;
      ++this.ltdcnt;
      if (var3) {
         ntrace.doTrace("]/WTCService/addLocalTuxDom/10/success");
      }

      return true;
   }

   private TDMLocalTDomain getLocalTuxDomByMBean(WTCLocalTuxDomMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/getLocalTuxDomByMBean/" + var1.getAccessPoint());
      }

      for(int var3 = 0; var3 < this.ltdcnt; ++var3) {
         if (this.ltd_list[var3].getAccessPoint().equals(var1.getAccessPoint())) {
            if (var2) {
               ntrace.doTrace("]/WTCService/getLocalTuxDomByMBean/10/found it");
            }

            return this.ltd_list[var3];
         }
      }

      if (var2) {
         ntrace.doTrace("]/WTCService/getLocalTuxDomByMBean/20/not found");
      }

      return null;
   }

   private boolean checkLocalTuxDomInUse(TDMLocalTDomain var1) {
      String var6 = var1.getAccessPoint();
      boolean var7 = ntrace.isTraceEnabled(2);
      if (var7) {
         ntrace.doTrace("[/WTCService/checkLocalTuxDomInUse/" + var6);
      }

      int var2;
      for(var2 = 0; var2 < this.rtdcnt; ++var2) {
         if (var6.equals(this.rtd_list[var2].getLocalAccessPoint())) {
            if (var7) {
               ntrace.doTrace("]/WTCService/checkLocalTuxDomInUse/10/in use by RDOM: " + this.rtd_list[var2].getAccessPoint());
            }

            return true;
         }
      }

      for(var2 = 0; var2 < this.pwdcnt; ++var2) {
         if (var6.equals(this.pwd_list[var2].getLocalAccessPoint())) {
            if (var7) {
               ntrace.doTrace("]/WTCService/checkLocalTuxDomInUse/20/in use by PWD: (" + var1.getAccessPoint() + ", " + this.rtd_list[var2].getAccessPoint() + ")");
            }

            return true;
         }
      }

      Iterator var3 = this.myImportedServices.values().iterator();

      Iterator var4;
      HashSet var5;
      while(var3.hasNext()) {
         var5 = (HashSet)var3.next();
         var4 = var5.iterator();

         while(var4.hasNext()) {
            TDMImport var8 = (TDMImport)var4.next();
            if (var6.equals(var8.getLocalAccessPoint())) {
               if (var7) {
                  ntrace.doTrace("]/WTCService/checkLocalTuxDomInUse/30/in use by RSVC: " + var8.getResourceName());
               }

               return true;
            }
         }
      }

      var3 = this.myExportedServices.values().iterator();

      while(var3.hasNext()) {
         var5 = (HashSet)var3.next();
         var4 = var5.iterator();

         while(var4.hasNext()) {
            TDMExport var9 = (TDMExport)var4.next();
            if (var6.equals(var9.getLocalAccessPoint())) {
               if (var7) {
                  ntrace.doTrace("]/WTCService/checkLocalTuxDomInUse/40/in use by LSVC: " + var9.getResourceName());
               }

               return true;
            }
         }
      }

      if (var7) {
         ntrace.doTrace("]/WTCService/checkLocalTuxDomInUse/50/not in use");
      }

      return false;
   }

   private void removeLocalTuxDom(TDMLocalTDomain var1) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/removeLocalTuxDom/" + var1.getAccessPoint());
      }

      int var2;
      for(var2 = 0; var2 < this.ltdcnt && this.ltd_list[var2] != var1; ++var2) {
      }

      if (var2 >= this.ltdcnt) {
         if (var3) {
            ntrace.doTrace("]/WTCService/removeLocalTuxDom/10/not found");
         }

      } else {
         TDMLocalTDomain[] var4 = new TDMLocalTDomain[this.ltdcnt - 1];
         this.removeFromArray(this.ltd_list, this.ltdcnt, var4, var2);
         --this.ltdcnt;
         this.ltd_list = var4;
         if (var3) {
            ntrace.doTrace("]/WTCService/removeLocalTuxDom/20/success");
         }

      }
   }

   private TDMRemoteTDomain setupTDMRemoteTD(WTCRemoteTuxDomMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(18);
      if (var2) {
         ntrace.doTrace("[/WTCService/setupTDMRemoteTD/");
      }

      String var3 = var1.getAccessPoint();
      if (var3 == null) {
         Loggable var17 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPoint", var1.getName());
         var17.log();
         if (var2) {
            ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/10/no AP");
         }

         throw new TPException(4, var17.getMessage());
      } else {
         if (var2) {
            ntrace.doTrace("AccessPoint: " + var3);
         }

         String var4 = var1.getAccessPointId();
         if (var4 == null) {
            Loggable var18 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPointId", var1.getName());
            var18.log();
            if (var2) {
               ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/20/no APId");
            }

            throw new TPException(4, var18.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("AccessPointId:" + var4);
            }

            String var5 = var1.getLocalAccessPoint();
            if (var5 == null) {
               Loggable var19 = WTCLogger.logUndefinedMBeanAttrLoggable("LocalAccessPoint", var1.getName());
               var19.log();
               if (var2) {
                  ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/30/no LAP");
               }

               throw new TPException(4, var19.getMessage());
            } else {
               if (var2) {
                  ntrace.doTrace("LocalAccessPoint:" + var5);
               }

               String var6 = var1.getNWAddr();
               if (var6 == null) {
                  Loggable var20 = WTCLogger.logUndefinedMBeanAttrLoggable("NWAddr", var1.getName());
                  var20.log();
                  if (var2) {
                     ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/40/no NWAddr");
                  }

                  throw new TPException(4, var20.getMessage());
               } else {
                  if (var2) {
                     ntrace.doTrace("NWAddr:" + var6);
                  }

                  if (var2) {
                     ntrace.doTrace("create rtd from " + var3);
                  }

                  TDMRemoteTDomain var7;
                  Loggable var9;
                  try {
                     var7 = new TDMRemoteTDomain(var3, this.unknownTxidRply, myTimeService);
                  } catch (Exception var15) {
                     var9 = WTCLogger.logUEconstructTDMRemoteTDLoggable(var15.getMessage());
                     var9.log();
                     if (var2) {
                        ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/50/create failed");
                     }

                     throw new TPException(4, var9.getMessage());
                  }

                  TDMLocalTDomain var8 = this.getLocalDomain(var5);
                  if (null == var8) {
                     var9 = WTCLogger.logErrorBadTDMRemoteLTDLoggable(var5);
                     var9.log();
                     if (var2) {
                        ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/60/no LDOM");
                     }

                     throw new TPException(4, var9.getMessage());
                  } else {
                     if (var2) {
                        ntrace.doTrace("valid LocalAccessPoint");
                     }

                     var7.setLocalAccessPoint(var5);
                     var7.setAccessPointId(var4);
                     var7.setAclPolicy(var1.getAclPolicy());
                     var7.setCredentialPolicy(var1.getCredentialPolicy());
                     var7.setTpUsrFile(var1.getTpUsrFile());

                     Loggable var10;
                     try {
                        var7.setNWAddr(var6);
                     } catch (TPException var16) {
                        var10 = WTCLogger.logInvalidMBeanAttrLoggable("NWAddr", var1.getName());
                        var10.log();
                        if (var2) {
                           ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/70/" + var16.getMessage());
                        }

                        throw new TPException(4, var10.getMessage());
                     }

                     var7.setFederationURL(var1.getFederationURL());
                     var7.setFederationName(var1.getFederationName());

                     try {
                        var7.setCmpLimit(var1.getCmpLimit());
                     } catch (TPException var14) {
                        var10 = WTCLogger.logInvalidMBeanAttrLoggable("CmpLimit", var1.getName());
                        var10.log();
                        if (var2) {
                           ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/80/" + var14.getMessage());
                        }

                        throw new TPException(4, var10.getMessage());
                     }

                     String var21 = var1.getMinEncryptBits();
                     if (var21 != null) {
                        var7.setMinEncryptBits(Integer.parseInt(var21, 10));
                     }

                     var21 = var1.getMaxEncryptBits();
                     if (var21 != null) {
                        var7.setMaxEncryptBits(Integer.parseInt(var21, 10));
                     }

                     var7.setConnectionPolicy(var1.getConnectionPolicy());
                     var7.setRetryInterval(var1.getRetryInterval());
                     var7.setMaxRetries(var1.getMaxRetries());
                     var7.setKeepAlive(var1.getKeepAlive());
                     var7.setKeepAliveWait(var1.getKeepAliveWait());
                     String var22 = var1.getAppKey();
                     if (var22 == null && var1.getTpUsrFile() != null) {
                        var22 = new String("TpUsrFile");
                        if (var2) {
                           ntrace.doTrace("Use dflt AppKey Generator");
                        }
                     }

                     var7.setAppKey(var22);
                     var7.setAllowAnonymous(var1.getAllowAnonymous());
                     var7.setDefaultAppKey(var1.getDefaultAppKey());
                     if (var22 != null) {
                        if (var22.equals("LDAP")) {
                           var7.setTuxedoUidKw(var1.getTuxedoUidKw());
                           var7.setTuxedoGidKw(var1.getTuxedoGidKw());
                           if (var2) {
                              ntrace.doTrace("LDAP, allow=" + var1.getAllowAnonymous() + ",Dflt AppKey=" + var1.getDefaultAppKey() + ",UID KW=" + var1.getTuxedoUidKw() + ", GID KW=" + var1.getTuxedoGidKw());
                           }
                        } else if (var22.equals("Custom")) {
                           String var11 = var1.getCustomAppKeyClass();
                           String var12 = var1.getCustomAppKeyClassParam();
                           if (var11 == null) {
                              Loggable var13 = WTCLogger.logUndefinedMBeanAttrLoggable("CustomAppKeyClass", var1.getName());
                              var13.log();
                              if (var2) {
                                 ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/90/no custom class defined");
                              }

                              throw new TPException(4, var13.getMessage());
                           }

                           if (var2) {
                              ntrace.doTrace("Custom, allow=" + var1.getAllowAnonymous() + ",Dflt AppKey=" + var1.getDefaultAppKey() + ",Class=" + var11 + ", Parm =" + var12);
                           }

                           var7.setCustomAppKeyClass(var11);
                           var7.setCustomAppKeyClassParam(var12);
                        } else {
                           if (!var22.equals("TpUsrFile")) {
                              Loggable var23 = WTCLogger.logInvalidMBeanAttrLoggable("AppKey", var1.getName());
                              var23.log();
                              if (var2) {
                                 ntrace.doTrace("*]/WTCService/setupTDMRemoteTD/100/unsupported appkey");
                              }

                              throw new TPException(4, var23.getMessage());
                           }

                           if (var2) {
                              ntrace.doTrace("TpUsrFile, allow=" + var1.getAllowAnonymous() + ",Dflt AppKey=" + var1.getDefaultAppKey() + ",File=" + var1.getTpUsrFile());
                           }
                        }
                     }

                     var7.setMBean(var1);
                     if (var2) {
                        ntrace.doTrace("]/WTCService/setupTDMRemoteTD/140/success");
                     }

                     return var7;
                  }
               }
            }
         }
      }
   }

   private boolean validateRemoteTuxDom(TDMRemoteTDomain var1) {
      String var2 = var1.getLocalAccessPoint();
      String var3 = var1.getAccessPoint();
      String var4 = var1.getAccessPointId();
      String var5 = var1.getNWAddr();
      boolean var7 = ntrace.isTraceEnabled(2);
      if (var7) {
         ntrace.doTrace("[/WTCService/validateRemoteTuxDom/(" + var2 + ", " + var3 + ")");
      }

      int var6;
      for(var6 = 0; var6 < this.rtdcnt; ++var6) {
         if (var3.equals(this.rtd_list[var6].getAccessPoint())) {
            if (var7) {
               ntrace.doTrace("]/WTCService/validateRemoteTuxDom/10/false");
            }

            return false;
         }

         if (var4.equals(this.rtd_list[var6].getAccessPointId()) && var2.equals(this.rtd_list[var6].getLocalAccessPoint()) && var5.equals(this.rtd_list[var6].getNWAddr())) {
            if (var7) {
               ntrace.doTrace("]/WTCService/validateRemoteTuxDom/20/false");
            }

            return false;
         }
      }

      for(var6 = 0; var6 < this.ltdcnt; ++var6) {
         if (var3.equals(this.ltd_list[var6].getAccessPoint())) {
            if (var7) {
               ntrace.doTrace("]/WTCService/validateRemoteTuxDom/30/false");
            }

            return false;
         }
      }

      if (var7) {
         ntrace.doTrace("]/WTCService/validateRemoteTuxDom/40/success");
      }

      return true;
   }

   private TDMRemoteTDomain getRemoteTuxDomByMBean(WTCRemoteTuxDomMBean var1) {
      boolean var3 = ntrace.isTraceEnabled(2);
      String var4 = var1.getLocalAccessPoint();
      String var5 = var1.getAccessPoint();
      if (var3) {
         ntrace.doTrace("[/WTCService/getRemoteTuxDomByMBean/(" + var4 + ", " + var5 + ")");
      }

      for(int var2 = 0; var2 < this.rtdcnt; ++var2) {
         if (this.rtd_list[var2].getLocalAccessPoint().equals(var4) && this.rtd_list[var2].getAccessPoint().equals(var5)) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getRemoteTuxDomByMBean/10/found it");
            }

            return this.rtd_list[var2];
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/getRemoteTuxDomByMBean/20/not found");
      }

      return null;
   }

   private boolean addRemoteTuxDom(TDMRemoteTDomain var1) {
      boolean var2 = ntrace.isMixedTraceEnabled(18);
      if (var2) {
         ntrace.doTrace("[/WTCService/addRemoteTuxDom/(" + var1.getLocalAccessPoint() + ", " + var1.getAccessPoint() + ")");
      }

      TDMRemoteTDomain[] var3 = this.rtd_list;
      this.rtd_list = new TDMRemoteTDomain[this.rtdcnt + 1];
      if (this.rtdcnt > 0) {
         System.arraycopy(var3, 0, this.rtd_list, 0, this.rtdcnt);
      }

      this.rtd_list[this.rtdcnt] = var1;
      ++this.rtdcnt;
      if (var2) {
         ntrace.doTrace("]/WTCService/addRemoteTuxDom/10/success");
      }

      return true;
   }

   private boolean checkRemoteTuxDomInUse(TDMRemoteTDomain var1) {
      String var6 = var1.getAccessPoint();
      boolean var7 = ntrace.isTraceEnabled(2);
      if (var7) {
         ntrace.doTrace("[/WTCService/checkRemoteTuxDomInUse/(" + var1.getLocalAccessPoint() + ", " + var6 + ")");
      }

      if (var1.getTsession(false) != null) {
         if (var7) {
            ntrace.doTrace("]/WTCService/checkRemoteTuxDomInUse/10/has active session");
         }

         return true;
      } else {
         for(int var2 = 0; var2 < this.pwdcnt; ++var2) {
            if (var6.equals(this.pwd_list[var2].getRemoteAccessPoint())) {
               if (var7) {
                  ntrace.doTrace("]/WTCService/checkRemoteTuxDomInUse/20/pwd reference");
               }

               return true;
            }
         }

         Iterator var3 = this.myImportedServices.values().iterator();

         while(var3.hasNext()) {
            HashSet var5 = (HashSet)var3.next();
            Iterator var4 = var5.iterator();

            while(var4.hasNext()) {
               TDMImport var8 = (TDMImport)var4.next();
               if (var8.hasRemoteDomain(var1)) {
                  if (var7) {
                     ntrace.doTrace("]/WTCService/checkRemoteTuxDomInUse/30/import reference");
                  }

                  return true;
               }
            }
         }

         if (var7) {
            ntrace.doTrace("]/WTCService/checkRemoteTuxDomInUse/40/not in use");
         }

         return false;
      }
   }

   private void removeRemoteTuxDom(TDMRemoteTDomain var1) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/removeRemoteTuxDom/(" + var1.getLocalAccessPoint() + ", " + var1.getAccessPoint() + ")");
      }

      int var2;
      for(var2 = 0; var2 < this.rtdcnt && this.rtd_list[var2] != var1; ++var2) {
      }

      if (var2 >= this.rtdcnt) {
         if (var3) {
            ntrace.doTrace("]/WTCService/removeRemoteTuxDom/10/not found");
         }

      } else {
         TDMLocal var4 = var1.getLocalAccessPointObject();
         if (var4 != null) {
            var4.remove_remote_domain(var1);
         }

         TDMRemoteTDomain[] var5 = new TDMRemoteTDomain[this.rtdcnt - 1];
         this.removeFromArray(this.rtd_list, this.rtdcnt, var5, var2);
         --this.rtdcnt;
         this.rtd_list = var5;
         if (var3) {
            ntrace.doTrace("]/WTCService/removeRemoteTuxDom/20/success");
         }

      }
   }

   private TDMImport setupTDMImport(WTCImportMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(18);
      if (var2) {
         ntrace.doTrace("[/WTCService/setupTDMImport/" + var1.getResourceName());
      }

      String var3 = var1.getLocalAccessPoint();
      if (var3 == null) {
         Loggable var16 = WTCLogger.logUndefinedMBeanAttrLoggable("LocalAccessPoint", var1.getName());
         var16.log();
         if (var2) {
            ntrace.doTrace("*]/WTCService/setupTDMImport/10/LAP equals null");
         }

         throw new TPException(4, var16.getMessage());
      } else {
         if (var2) {
            ntrace.doTrace("LocalAccessPoint:" + var3);
         }

         TDMLocalTDomain var4 = this.getLocalDomain(var3);
         if (null == var4) {
            Loggable var17 = WTCLogger.logErrorBadTDMImportLTDLoggable(var3);
            var17.log();
            if (var2) {
               ntrace.doTrace("*]/WTCService/setupTDMImport/20/LAP not found");
            }

            throw new TPException(4, var17.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("valid LocalAccessPoint");
            }

            String var5 = var1.getResourceName();
            if (var5 == null) {
               Loggable var18 = WTCLogger.logUndefinedMBeanAttrLoggable("ResourceName", var1.getName());
               var18.log();
               if (var2) {
                  ntrace.doTrace("*]/WTCService/setupTDMImport/30/Resource is null");
               }

               throw new TPException(4, var18.getMessage());
            } else {
               if (var2) {
                  ntrace.doTrace("ResourceName:" + var5);
               }

               String var9 = var1.getRemoteAccessPointList();
               Loggable var11;
               if (var9 == null) {
                  var11 = WTCLogger.logUndefinedMBeanAttrLoggable("RemoteAccessPointList", var1.getName());
                  var11.log();
                  if (var2) {
                     ntrace.doTrace("*]/WTCService/setupTDMImport/40/RAP List is null");
                  }

                  throw new TPException(4, var11.getMessage());
               } else {
                  TDMRemoteTDomain var6;
                  TDMRemoteTDomain[] var7;
                  TDMLocal var8;
                  if (var9.indexOf(44) == -1) {
                     var6 = this.getRTDbyAP(var9);
                     if (null == var6) {
                        var11 = WTCLogger.logErrorBadTDMImportRTDLoggable(var9);
                        var11.log();
                        if (var2) {
                           ntrace.doTrace("*]/WTCService/setupTDMImport/41/RAP " + var9 + " not found");
                        }

                        throw new TPException(4, var11.getMessage());
                     }

                     if ((var8 = var6.getLocalAccessPointObject()) == null) {
                        var6.setLocalAccessPoint(var3);
                     } else if (!var8.getAccessPoint().equals(var3)) {
                        var11 = WTCLogger.logErrorUndefinedTDomainSessionLoggable(var5, var3, var9);
                        var11.log();
                        if (var2) {
                           ntrace.doTrace("*]/WTCService/setupTDMImport/42/TSession(" + var9 + ", " + var3 + ") not found");
                        }

                        throw new TPException(4, var11.getMessage());
                     }

                     var7 = new TDMRemoteTDomain[]{var6};
                  } else {
                     StringTokenizer var19 = new StringTokenizer(var9, ",");
                     var7 = new TDMRemoteTDomain[var19.countTokens()];

                     for(int var12 = 0; var19.hasMoreTokens(); ++var12) {
                        String var13 = var19.nextToken();
                        var6 = this.getRTDbyAP(var13);
                        Loggable var14;
                        if (null == var6) {
                           var14 = WTCLogger.logErrorBadTDMImportRTDLoggable(var13);
                           var14.log();
                           if (var2) {
                              ntrace.doTrace("*]/WTCService/setupTDMImport/50/RAP " + var13 + " not found");
                           }

                           throw new TPException(4, var14.getMessage());
                        }

                        if ((var8 = var6.getLocalAccessPointObject()) == null) {
                           var6.setLocalAccessPoint(var3);
                        } else if (!var8.getAccessPoint().equals(var3)) {
                           var14 = WTCLogger.logErrorUndefinedTDomainSessionLoggable(var5, var3, var13);
                           var14.log();
                           if (var2) {
                              ntrace.doTrace("*]/WTCService/setupTDMImport/51/TSession(" + var13 + ", " + var3 + ") not found");
                           }

                           throw new TPException(4, var14.getMessage());
                        }

                        var7[var12] = var6;
                     }
                  }

                  if (var2) {
                     ntrace.doTrace("valid RemoteAccessPointList");
                  }

                  if (var2) {
                     ntrace.doTrace("create imp from " + var5);
                  }

                  TDMImport var20;
                  try {
                     var20 = new TDMImport(var5, var4, var7);
                  } catch (Exception var15) {
                     Loggable var22 = WTCLogger.logUEconstructTDMImportLoggable(var15.getMessage());
                     var22.log();
                     if (var2) {
                        ntrace.doTrace("*]/WTCService/setupTDMImport/50/create import failed");
                     }

                     throw new TPException(4, var22.getMessage());
                  }

                  String var21 = var1.getRemoteName();
                  if (var21 != null) {
                     var20.setRemoteName(var21);
                  } else {
                     var20.setRemoteName(var5);
                  }

                  var20.setRemoteAccessPointListString(var9);
                  var20.setMBean(var1);
                  if (var2) {
                     ntrace.doTrace("]/WTCService/setupTDMImport/60/success");
                  }

                  return var20;
               }
            }
         }
      }
   }

   private boolean validateImport(TDMImport var1) throws TPException {
      String var2 = var1.getResourceName();
      String var3 = var1.getLocalAccessPoint();
      String[] var4 = var1.getRemoteAccessPointList();
      boolean var7 = ntrace.isMixedTraceEnabled(18);
      if (var7) {
         ntrace.doTrace("[/WTCService/validateImport/" + var3 + "(" + var2 + ", " + var1.getResourceName() + ")");
      }

      HashSet var5 = (HashSet)this.myImportedServices.get(var2);
      if (var5 != null) {
         Iterator var9 = var5.iterator();

         while(var9.hasNext()) {
            TDMImport var6 = (TDMImport)var9.next();
            String[] var8 = var6.getRemoteAccessPointList();
            if (var3.equals(var6.getLocalAccessPoint()) && var4[0].equals(var8[0])) {
               if (var7) {
                  ntrace.doTrace("*]/WTCService/validateImport/10/duplicate remote service name " + var2);
               }

               throw new TPException(4, "ERROR: Duplicate imported service entry for (" + var3 + "," + var4[0] + ") with resource name(" + var2 + ")!");
            }
         }
      }

      if (var7) {
         ntrace.doTrace("]/WTCService/validateImport/20/success");
      }

      return true;
   }

   private TDMImport getImportByMBean(WTCImportMBean var1) {
      String var4 = var1.getRemoteName();
      boolean var5 = ntrace.isTraceEnabled(2);
      String var6 = var1.getResourceName();
      String var7 = var1.getLocalAccessPoint();
      String var8 = var1.getRemoteAccessPointList();
      String[] var9 = TDMImport.parseCommaSeparatedList(var8);
      if (var5) {
         ntrace.doTrace("[/WTCService/getImportByMBean/(" + var6 + ", remote " + var4 + ")");
      }

      HashSet var2;
      if ((var2 = (HashSet)this.myImportedServices.get(var6)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/getImportByMBean/10/set is empty");
         }

         return null;
      } else if (var2.size() == 0) {
         if (var5) {
            ntrace.doTrace("]/WTCService/getImportByMBean/20/set is 0 length");
         }

         return null;
      } else {
         Iterator var11 = var2.iterator();

         TDMImport var3;
         String var10;
         do {
            if (!var11.hasNext()) {
               if (var5) {
                  ntrace.doTrace("]/WTCService/getImportByMBean/40/not found");
               }

               return null;
            }

            var3 = (TDMImport)var11.next();
            var10 = var3.getPrimaryRemoteAccessPoint();
         } while(!var10.equals(var9[0]) || !var3.getLocalAccessPoint().equals(var7));

         if (var5) {
            ntrace.doTrace("]/WTCService/getImportByMBean/30/success");
         }

         return var3;
      }
   }

   private TDMImport getTmpImportByMBean(WTCImportMBean var1) {
      String var4 = var1.getRemoteName();
      boolean var5 = ntrace.isTraceEnabled(2);
      String var6 = var1.getResourceName();
      String var7 = var1.getLocalAccessPoint();
      String var8 = var1.getRemoteAccessPointList();
      String[] var9 = TDMImport.parseCommaSeparatedList(var8);
      if (var5) {
         ntrace.doTrace("[/WTCService/getTmpImportByMBean/(" + var6 + ", remote " + var4 + ")");
      }

      HashSet var2;
      if ((var2 = (HashSet)this.tmpImportedServices.get(var6)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/getTmpImportByMBean/10/set is empty");
         }

         return null;
      } else if (var2.size() == 0) {
         if (var5) {
            ntrace.doTrace("]/WTCService/getTmpImportByMBean/20/set is 0 length");
         }

         return null;
      } else {
         Iterator var11 = var2.iterator();

         TDMImport var3;
         String var10;
         do {
            if (!var11.hasNext()) {
               if (var5) {
                  ntrace.doTrace("]/WTCService/getTmpImportByMBean/40/not found");
               }

               return null;
            }

            var3 = (TDMImport)var11.next();
            var10 = var3.getPrimaryRemoteAccessPoint();
         } while(!var10.equals(var9[0]) || !var3.getLocalAccessPoint().equals(var7));

         if (var5) {
            ntrace.doTrace("]/WTCService/getTmpImportByMBean/30/success");
         }

         return var3;
      }
   }

   private boolean addImport(TDMImport var1) {
      String var2 = var1.getRemoteName();
      String var3 = var1.getResourceName();
      boolean var6 = ntrace.isMixedTraceEnabled(18);
      if (var6) {
         ntrace.doTrace("[/WTCService/addImport/" + var3 + ", remote " + var2);
      }

      HashSet var4;
      if ((var4 = (HashSet)this.myImportedServices.get(var3)) == null) {
         String var7 = new String(var3);
         var4 = new HashSet();
         this.myImportedServices.put(var7, var4);
      }

      if (!var4.add(var1)) {
         if (var6) {
            ntrace.doTrace("]/WTCService/addImport/10/failed to add");
         }

         return false;
      } else {
         if (var6) {
            ntrace.doTrace("]/WTCService/addImport/20/success");
         }

         return true;
      }
   }

   private boolean addTmpImport(TDMImport var1) {
      String var2 = var1.getRemoteName();
      String var3 = var1.getResourceName();
      boolean var6 = ntrace.isMixedTraceEnabled(18);
      if (var6) {
         ntrace.doTrace("[/WTCService/addTmpImport/" + var3 + ", remote " + var2);
      }

      HashSet var4;
      if ((var4 = (HashSet)this.tmpImportedServices.get(var3)) == null) {
         String var7 = new String(var3);
         var4 = new HashSet();
         this.tmpImportedServices.put(var7, var4);
      }

      if (!var4.add(var1)) {
         if (var6) {
            ntrace.doTrace("]/WTCService/addTmpImport/10/failed to add");
         }

         return false;
      } else {
         if (var6) {
            ntrace.doTrace("]/WTCService/addTmpImport/20/success");
         }

         return true;
      }
   }

   private boolean checkImportInUse(TDMImport var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/checkImportInUse/" + var1.getResourceName() + ", remote " + var1.getRemoteName());
      }

      if (var2) {
         ntrace.doTrace("]/WTCService/checkImportInUse/20/false");
      }

      return false;
   }

   private void removeImport(TDMImport var1) {
      String var4 = var1.getResourceName();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/removeImport/" + var4 + ", for LAP: " + var1.getLocalAccessPoint());
      }

      HashSet var2;
      if ((var2 = (HashSet)this.myImportedServices.get(var4)) != null) {
         var2.remove(var1);
         if (var2.isEmpty()) {
            this.myImportedServices.remove(var4);
         }
      }

      if (var5) {
         ntrace.doTrace("]/WTCService/removeImport/10/false");
      }

   }

   private void removeTmpImport(TDMImport var1) {
      String var4 = var1.getResourceName();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/removeTmpImport/" + var4 + ", for LAP: " + var1.getLocalAccessPoint());
      }

      HashSet var2;
      if ((var2 = (HashSet)this.tmpImportedServices.get(var4)) != null) {
         var2.remove(var1);
         if (var2.isEmpty()) {
            this.tmpImportedServices.remove(var4);
         }
      }

      if (var5) {
         ntrace.doTrace("]/WTCService/removeTmpImport/10/false");
      }

   }

   private TDMExport setupTDMExport(WTCExportMBean var1) throws TPException {
      String var2 = var1.getResourceName();
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/setupTDMExport/" + var2);
      }

      String var4 = var1.getLocalAccessPoint();
      if (var4 == null) {
         Loggable var10 = WTCLogger.logUndefinedMBeanAttrLoggable("LocalAccessPoint", var1.getName());
         var10.log();
         if (var3) {
            ntrace.doTrace("*]WTCService/setupTDMExport/10/missing LAP");
         }

         throw new TPException(4, var10.getMessage());
      } else {
         if (var3) {
            ntrace.doTrace("LocalAccessPoint:" + var4);
         }

         TDMLocalTDomain var5 = this.getLocalDomain(var4);
         Loggable var11;
         if (null == var5) {
            if (var3) {
               ntrace.doTrace("*]WTCService/setupTDMExport/20/LAP does not exist");
            }

            var11 = WTCLogger.logErrorBadTDMExportLTDLoggable(var4);
            var11.log();
            throw new TPException(4, var11.getMessage());
         } else {
            if (var3) {
               ntrace.doTrace("valid LocalAccessPoint");
            }

            if (var2 == null) {
               if (var3) {
                  ntrace.doTrace("*]WTCService/setupTDMExport/30/missing resource name");
               }

               var11 = WTCLogger.logUndefinedMBeanAttrLoggable("ResourceName", var1.getName());
               var11.log();
               throw new TPException(4, var11.getMessage());
            } else {
               if (var3) {
                  ntrace.doTrace("ResourceName:" + var2);
               }

               if (var3) {
                  ntrace.doTrace("create export from " + var2 + ", lapnm:" + var4);
               }

               TDMExport var6;
               try {
                  var6 = new TDMExport(var2, var5);
               } catch (Exception var9) {
                  if (var3) {
                     ntrace.doTrace("*]WTCService/setupTDMExport/40/create Export failed");
                  }

                  Loggable var8 = WTCLogger.logUEconstructTDMExportLoggable(var9.getMessage());
                  var8.log();
                  throw new TPException(4, var8.getMessage());
               }

               String var7 = var1.getRemoteName();
               if (var7 != null) {
                  var6.setRemoteName(var7);
               } else {
                  var6.setRemoteName(var2);
               }

               var7 = var1.getEJBName();
               String var12 = var1.getTargetClass();
               if (var7 == null && var12 == null) {
                  var7 = "tuxedo.services." + var2 + "Home";
               }

               var6.setEJBName(var7);
               var6.setTargetClass(var12);
               var6.setTargetJar(var1.getTargetJar());
               var6.setMBean(var1);
               if (var3) {
                  ntrace.doTrace("]/WTCService/setupTDMExport/50/success");
               }

               return var6;
            }
         }
      }
   }

   private boolean validateExport(TDMExport var1) throws TPException {
      String var2 = var1.getRemoteName();
      String var3 = var1.getLocalAccessPoint();
      boolean var6 = ntrace.isMixedTraceEnabled(18);
      if (var6) {
         ntrace.doTrace("[/WTCService/validateExport/" + var3 + "(" + var1.getResourceName() + ", " + var2 + ")");
      }

      HashSet var4;
      if ((var4 = (HashSet)this.myExportedServices.get(var2)) != null) {
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            TDMExport var5 = (TDMExport)var7.next();
            if (var3.equals(var5.getLocalAccessPoint())) {
               if (var6) {
                  ntrace.doTrace("*]/WTCService/validateExport/10/duplicate export");
               }

               throw new TPException(4, "ERROR: Duplicate exported service entry for " + var3 + " with remote name(" + var2 + ")!");
            }
         }
      }

      if (var6) {
         ntrace.doTrace("]/WTCService/validateExport/20/success");
      }

      return true;
   }

   private TDMExport getExportByMBean(WTCExportMBean var1) {
      String var2 = var1.getRemoteName();
      HashSet var3 = (HashSet)this.myExportedServices.get(var2);
      boolean var4 = ntrace.isTraceEnabled(2);
      String var5 = var1.getLocalAccessPoint();
      if (var4) {
         ntrace.doTrace("[/WTCService/getExportByMBean/(" + var5 + ", " + var2 + ")");
      }

      if (var3 != null) {
         Iterator var7 = var3.iterator();

         while(var7.hasNext()) {
            TDMExport var6 = (TDMExport)var7.next();
            if (var6.getLocalAccessPoint().equals(var5)) {
               if (var4) {
                  ntrace.doTrace("]/WTCService/getExportByMBean/10/found");
               }

               return var6;
            }
         }
      }

      if (var4) {
         ntrace.doTrace("]/WTCService/getExportByMBean/20/not found");
      }

      return null;
   }

   private TDMExport getTmpExportByMBean(WTCExportMBean var1) {
      String var2 = var1.getRemoteName();
      HashSet var3 = (HashSet)this.tmpExportedServices.get(var2);
      boolean var4 = ntrace.isTraceEnabled(2);
      String var5 = var1.getLocalAccessPoint();
      if (var4) {
         ntrace.doTrace("[/WTCService/getTmpExportByMBean/(" + var5 + ", " + var2 + ")");
      }

      if (var3 != null) {
         Iterator var7 = var3.iterator();

         while(var7.hasNext()) {
            TDMExport var6 = (TDMExport)var7.next();
            if (var6.getLocalAccessPoint().equals(var5)) {
               if (var4) {
                  ntrace.doTrace("]/WTCService/getTmpExportByMBean/10/found");
               }

               return var6;
            }
         }
      }

      if (var4) {
         ntrace.doTrace("]/WTCService/getTmpExportByMBean/20/not found");
      }

      return null;
   }

   private boolean addExport(TDMExport var1) {
      String var2 = var1.getRemoteName();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/addExport/" + var1.getResourceName());
      }

      HashSet var3;
      if ((var3 = (HashSet)this.myExportedServices.get(var2)) == null) {
         var3 = new HashSet();
         String var5 = new String(var2);
         this.myExportedServices.put(var5, var3);
      }

      if (!var3.add(var1)) {
         if (var4) {
            ntrace.doTrace("]/WTCService/addExport/10/add failed");
         }

         return false;
      } else {
         if (var4) {
            ntrace.doTrace("]/WTCService/addExport/20/success");
         }

         return true;
      }
   }

   private boolean addTmpExport(TDMExport var1) {
      String var2 = var1.getRemoteName();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/addTmpExport/" + var1.getResourceName());
      }

      HashSet var3;
      if ((var3 = (HashSet)this.tmpExportedServices.get(var2)) == null) {
         var3 = new HashSet();
         String var5 = new String(var2);
         this.tmpExportedServices.put(var5, var3);
      }

      if (!var3.add(var1)) {
         if (var4) {
            ntrace.doTrace("]/WTCService/addExport/10/add failed");
         }

         return false;
      } else {
         if (var4) {
            ntrace.doTrace("]/WTCService/addExport/20/success");
         }

         return true;
      }
   }

   private boolean checkExportInUse(TDMExport var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/checkExportInUse/" + var1.getResourceName());
      }

      if (var2) {
         ntrace.doTrace("]/WTCService/checkExportInUse/20/not in use");
      }

      return false;
   }

   private void removeExport(TDMExport var1) {
      String var2 = var1.getRemoteName();
      HashSet var3 = (HashSet)this.myExportedServices.get(var2);
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/removeExport/" + var1.getResourceName() + ", remote name: " + var2 + " for LAP: " + var1.getLocalAccessPoint());
      }

      if (var3 != null) {
         var3.remove(var1);
         if (var3.isEmpty()) {
            this.myExportedServices.remove(var2);
         }
      }

      if (var4) {
         ntrace.doTrace("]/WTCService/removeExport/10/success");
      }

   }

   private void removeTmpExport(TDMExport var1) {
      String var2 = var1.getRemoteName();
      HashSet var3 = (HashSet)this.tmpExportedServices.get(var2);
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/removeTmpExport/" + var1.getResourceName() + ", remote name: " + var2 + " for LAP: " + var1.getLocalAccessPoint());
      }

      if (var3 != null) {
         var3.remove(var1);
         if (var3.isEmpty()) {
            this.tmpExportedServices.remove(var2);
         }
      }

      if (var4) {
         ntrace.doTrace("]/WTCService/removeTmpExport/10/success");
      }

   }

   private TDMPasswd setupTDMPasswd(WTCPasswordMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(18);
      if (var2) {
         ntrace.doTrace("[/WTCService/setupTDMPasswd/");
      }

      String var3 = var1.getLocalAccessPoint();
      if (var3 == null) {
         Loggable var12 = WTCLogger.logUndefinedMBeanAttrLoggable("LocalAccessPoint", var1.getName());
         var12.log();
         if (var2) {
            ntrace.doTrace("*]/WTCService/setupTDMPasswd/10/no LAP");
         }

         throw new TPException(4, var12.getMessage());
      } else {
         if (var2) {
            ntrace.doTrace("localAccessPoint:" + var3);
         }

         TDMLocalTDomain var4 = this.getLocalDomain(var3);
         if (null == var4) {
            Loggable var13 = WTCLogger.logErrorBadTDMPasswdLTDLoggable(var3);
            var13.log();
            if (var2) {
               ntrace.doTrace("*]/WTCService/setupTDMPasswd/20/no LDOM defined");
            }

            throw new TPException(4, var13.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("valid LocalAccessPoint");
            }

            String var5 = var1.getRemoteAccessPoint();
            if (var5 == null) {
               Loggable var14 = WTCLogger.logUndefinedMBeanAttrLoggable("RemoteAccessPoint", var1.getName());
               var14.log();
               if (var2) {
                  ntrace.doTrace("*]/WTCService/setupTDMPasswd/30/no RAP");
               }

               throw new TPException(4, var14.getMessage());
            } else {
               if (var2) {
                  ntrace.doTrace("RemoteAccessPoint:" + var5);
               }

               TDMRemoteTDomain var6 = this.getRTDbyAP(var5);
               if (null == var6) {
                  Loggable var15 = WTCLogger.logErrorBadTDMPasswdRTDLoggable(var5);
                  var15.log();
                  if (var2) {
                     ntrace.doTrace("*]/WTCService/setupTDMPasswd/40/no RDOM");
                  }

                  throw new TPException(4, var15.getMessage());
               } else {
                  if (var2) {
                     ntrace.doTrace("valid RemoteAccessPointList member");
                  }

                  if (var2) {
                     ntrace.doTrace("create tdmpwd from lapnm:" + var3 + "and rapnm:" + var5);
                  }

                  TDMPasswd var7;
                  try {
                     var7 = new TDMPasswd(var3, var5);
                  } catch (Exception var11) {
                     Loggable var9 = WTCLogger.logUEconstructTDMPasswdLoggable(var11.getMessage());
                     var9.log();
                     if (var2) {
                        ntrace.doTrace("*]/WTCService/setupTDMPasswd/50/failed createion");
                     }

                     throw new TPException(4, var9.getMessage());
                  }

                  String var8 = var1.getLocalPasswordIV();
                  String var16 = var1.getLocalPassword();
                  Loggable var10;
                  if (var16 != null && var8 == null || var16 == null && var8 != null) {
                     var10 = WTCLogger.logErrorPasswordInfoLoggable("Local");
                     var10.log();
                     if (var2) {
                        ntrace.doTrace("*]/WTCService/setupTDMPasswd/60/bad lpassword");
                     }

                     throw new TPException(4, var10.getMessage());
                  } else {
                     var7.setLocalPasswordIV(var8);
                     var7.setLocalPassword(var16);
                     if (var2) {
                        ntrace.doTrace("checked Local Passwd,PasswdIV.");
                     }

                     var8 = var1.getRemotePasswordIV();
                     var16 = var1.getRemotePassword();
                     if ((var16 == null || var8 != null) && (var16 != null || var8 == null)) {
                        var7.setRemotePasswordIV(var8);
                        var7.setRemotePassword(var16);
                        if (var2) {
                           ntrace.doTrace("checked Remote Passwd,PasswdIV.");
                        }

                        var7.setMBean(var1);
                        if (var2) {
                           ntrace.doTrace("]/WTCService/setupTDMPasswd/80/success");
                        }

                        return var7;
                     } else {
                        var10 = WTCLogger.logErrorPasswordInfoLoggable("Remote");
                        var10.log();
                        if (var2) {
                           ntrace.doTrace("*]/WTCService/setupTDMPasswd/70/bad rpassword");
                        }

                        throw new TPException(4, var10.getMessage());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean validatePasswd(TDMPasswd var1) {
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/validatePasswd/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      String var4 = var1.getLocalAccessPoint();
      String var5 = var1.getRemoteAccessPoint();

      for(int var2 = 0; var2 < this.pwdcnt; ++var2) {
         TDMPasswd var6 = this.pwd_list[var2];
         if (var4.equals(var6.getLocalAccessPoint()) && var5.equals(var6.getRemoteAccessPoint())) {
            if (var3) {
               ntrace.doTrace("]/WTCService/validatePasswd/10/duplicate");
            }

            return false;
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/validatePasswd/20/success");
      }

      return true;
   }

   private TDMPasswd getPasswdByMBean(WTCPasswordMBean var1) {
      boolean var3 = ntrace.isTraceEnabled(2);
      String var4 = var1.getLocalAccessPoint();
      String var5 = var1.getRemoteAccessPoint();
      if (var3) {
         ntrace.doTrace("[/WTCService/getPasswdByMBean/(" + var4 + ", " + var5 + ")");
      }

      for(int var2 = 0; var2 < this.pwdcnt; ++var2) {
         if (this.pwd_list[var2].getLocalAccessPoint().equals(var4) && this.pwd_list[var2].getRemoteAccessPoint().equals(var5)) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getPasswdByMBean/10/found it");
            }

            return this.pwd_list[var2];
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/getPasswdByMBean/20/null");
      }

      return null;
   }

   private boolean addPasswd(TDMPasswd var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/addPasswd/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      TDMPasswd[] var3 = this.pwd_list;
      this.pwd_list = new TDMPasswd[this.pwdcnt + 1];
      if (this.pwdcnt > 0) {
         System.arraycopy(var3, 0, this.pwd_list, 0, this.pwdcnt);
      }

      this.pwd_list[this.pwdcnt] = var1;
      ++this.pwdcnt;
      if (var2) {
         ntrace.doTrace("]/WTCService/addPasswd/10/success");
      }

      return true;
   }

   private boolean checkPasswdInUse(TDMPasswd var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/checkPasswdInUse/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      if (var2) {
         ntrace.doTrace("]/WTCService/checkPasswdInUse/10/not in use");
      }

      return false;
   }

   private void removePasswd(TDMPasswd var1) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/removePasswd/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      int var2;
      for(var2 = 0; var2 < this.pwdcnt && this.pwd_list[var2] != var1; ++var2) {
      }

      if (var2 >= this.pwdcnt) {
         if (var3) {
            ntrace.doTrace("]/WTCService/removePasswd/10/not found");
         }

      } else {
         TDMPasswd[] var4 = new TDMPasswd[this.pwdcnt - 1];
         this.removeFromArray(this.pwd_list, this.pwdcnt, var4, var2);
         --this.pwdcnt;
         this.pwd_list = var4;
         if (var3) {
            ntrace.doTrace("]/WTCService/removePasswd/20/success");
         }

      }
   }

   public void startAddWTCLocalTuxDom(WTCLocalTuxDomMBean var1) throws BeanUpdateRejectedException {
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/startAddWTCLocalTuxDom");
      }

      TDMLocalTDomain var2;
      try {
         var2 = TDMLocalTDomain.create(var1);
         var2.checkConfigIntegrity();
      } catch (TPException var6) {
         Loggable var5 = WTCLogger.logErrorExecMBeanDefLoggable(var1.getName());
         if (var3) {
            ntrace.doTrace("*]/WTCService/handleLocalTDomChange/20/setup error " + var1.getName());
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      }

      if (!this.validateLocalTuxDom(var2)) {
         if (var3) {
            ntrace.doTrace("*]/WTCService/startAddWTCLocalTuxDom/30/invalid");
         }

         throw new BeanUpdateRejectedException("Error: Adding duplicatedLocalTuxDom: " + var1.getAccessPoint());
      } else {
         this.addLocalTuxDom(var2);
         this.startOatmialListener(var2);
         var2.prepareObject();
         if (var3) {
            ntrace.doTrace("]/WTCService/startAddWTCLocalTuxDom/40/success");
         }

      }
   }

   public void finishAddWTCLocalTuxDom(WTCLocalTuxDomMBean var1, boolean var2) {
      TDMLocalTDomain var3 = null;
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/finishAddWTCLocalTuxDom");
      }

      if ((var3 = this.getLocalTuxDomByMBean(var1)) == null) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCLocalTuxDom/10, not found");
         }

      } else if (!var3.isObjectPrepared()) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCLocalTuxDom/20, wrong state");
         }

      } else if (var2) {
         var3.activateObject();
         var3.setMBean(var1);
         var3.registerListener();
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCLocalTuxDom/30/done with activation");
         }

      } else {
         var3.deactivateObject();
         this.removeLocalTuxDom(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCLocalTuxDom/30");
         }

      }
   }

   public void startRemoveWTCLocalTuxDom(WTCLocalTuxDomMBean var1) throws BeanUpdateRejectedException {
      boolean var4 = false;
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/startRemoveWTCLocalTuxDom");
      }

      TDMLocalTDomain var2;
      Loggable var6;
      if ((var2 = this.getLocalTuxDomByMBean(var1)) == null) {
         var6 = WTCLogger.logErrorNoSuchLocalDomainLoggable(var1.getAccessPointId());
         var6.log();
         if (var5) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCLocalTuxDom/10, not found");
         }

         throw new BeanUpdateRejectedException(var6.getMessage());
      } else if (this.checkLocalTuxDomInUse(var2)) {
         if (var5) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCLocalTuxDom/20/beingUsed");
         }

         var6 = WTCLogger.logErrorLocalTDomInUseLoggable(var1.getAccessPointId());
         var6.log();
         throw new BeanUpdateRejectedException(var6.getMessage());
      } else {
         var2.suspendObject();
         if (var5) {
            ntrace.doTrace("]/WTCService/startRemoveWTCLocalTuxDom/30/suspended");
         }

      }
   }

   public void finishRemoveWTCLocalTuxDom(WTCLocalTuxDomMBean var1, boolean var2) {
      TDMLocalTDomain var3 = null;
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/finishRemoveWTCLocalTuxDom/" + var1.getAccessPoint());
      }

      if ((var3 = this.getLocalTuxDomByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCLocalTuxDom/10/not found");
         }

      } else if (!var2) {
         if (var3.isObjectSuspended()) {
            var3.activateObject();
         }

         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCLocalTuxDom/20/rollbacked");
         }

      } else {
         var3.deactivateObject();
         var3.unregisterListener();
         this.removeLocalTuxDom(var3);
         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCLocalTuxDom/30/succeeded");
         }

      }
   }

   public void startAddWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1) throws BeanUpdateRejectedException {
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/startAddWTCRemoteTuxDom/" + var1.getAccessPoint());
      }

      TDMRemoteTDomain var2;
      Loggable var6;
      try {
         var2 = this.setupTDMRemoteTD(var1);
         var2.checkConfigIntegrity();
      } catch (TPException var7) {
         var6 = WTCLogger.logErrorExecMBeanDefLoggable(var1.getAccessPointId());
         if (var4) {
            ntrace.doTrace("*]/WTCService/startAddWTCRemoteTuxDom/10/" + var1.getAccessPoint());
         }

         throw new BeanUpdateRejectedException(var6.getMessage());
      }

      if (!this.validateRemoteTuxDom(var2)) {
         Loggable var5 = WTCLogger.logErrorDuplicateRemoteTDomLoggable(var1.getAccessPointId());
         if (var4) {
            ntrace.doTrace("*]/WTCService/startAddWTCRemoteTuxDom/20/" + var1.getAccessPoint() + " duplicate");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      } else {
         try {
            this.federateRemoteDomain(this.myDomainContext, var2);
         } catch (NamingException var8) {
            var6 = WTCLogger.logNEcreateSubCntxtLoggable(var8);
            if (var4) {
               ntrace.doTrace("*]/WTCService/startAddWTCRemoteTuxDom/30/" + var1.getAccessPoint() + " Naming Exception");
            }

            throw new BeanUpdateRejectedException(var6.getMessage());
         }

         var2.onTerm(1);
         this.addRemoteTuxDom(var2);
         var2.prepareObject();
         if (var4) {
            ntrace.doTrace("]/WTCService/startAddWTCRemoteTuxDom/40/" + var1.getAccessPoint() + " Prepared");
         }

      }
   }

   public void finishAddWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1, boolean var2) {
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/finishAddWTCRemoteTuxDom/" + var1.getAccessPoint());
      }

      TDMRemoteTDomain var3;
      if ((var3 = this.getRemoteTuxDomByMBean(var1)) == null) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCRemoteTuxDom/10/not found");
         }

      } else if (!var3.isObjectPrepared()) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCRemoteTuxDom/20/not in PREPARED state");
         }

      } else if (var2) {
         var3.activateObject();
         var3.setMBean(var1);
         var3.registerListener();
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCRemoteTuxDom/30/" + var3.getAccessPointId() + " is activated");
         }

      } else {
         var3.deactivateObject();
         this.removeRemoteTuxDom(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCRemoteTuxDom/40/" + var1.getAccessPoint() + " rolled back");
         }

      }
   }

   public void startRemoveWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1) throws BeanUpdateRejectedException {
      TDMRemoteTDomain var2 = null;
      String var4 = var1.getAccessPoint();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/startRemoveWTCRemoteTuxDom/" + var1.getAccessPoint());
      }

      if ((var2 = this.getRemoteTuxDomByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCRemoteTuxDom/10/" + var4 + " not found");
         }

         throw new BeanUpdateRejectedException("ERROR: RemoteTuxDom " + var4 + " does not exist!");
      } else if (!this.checkRemoteTuxDomInUse(var2)) {
         Loggable var6 = WTCLogger.logErrorRemoteTDomInUseLoggable(var1.getAccessPointId());
         var6.log();
         if (var5) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCRemoteTuxDom/20/" + var4 + " in use");
         }

         throw new BeanUpdateRejectedException(var6.getMessage());
      } else {
         var2.suspendObject();
         if (var5) {
            ntrace.doTrace("]/WTCService/startRemoveWTCRemoteTuxDom/30/" + var4 + " suspended");
         }

      }
   }

   public void finishRemoveWTCRemoteTuxDom(WTCRemoteTuxDomMBean var1, boolean var2) {
      String var3 = var1.getAccessPoint();
      TDMRemoteTDomain var4 = null;
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/finishRemoveWTCRemoteTuxDom/" + var3);
      }

      if ((var4 = this.getRemoteTuxDomByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCRemoteTuxDom/10/" + var3 + " not found");
         }

      } else if (!var2) {
         if (var4.isObjectSuspended()) {
            if (var5) {
               ntrace.doTrace("activate remote TuxDom AP " + var3);
            }

            var4.activateObject();
         }

         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCRemoteTuxDom/20/" + var3 + " not removed");
         }

      } else {
         var4.deactivateObject();
         var4.unregisterListener();
         this.removeRemoteTuxDom(var4);
         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCRemoteTuxDom/30/" + var3 + " removed");
         }

      }
   }

   public void startAddWTCImport(WTCImportMBean var1) throws BeanUpdateRejectedException {
      TDMImport var2 = null;
      String var3 = var1.getResourceName();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/startAddWTCImport/" + var3);
      }

      try {
         var2 = this.setupTDMImport(var1);
         this.validateImport(var2);
      } catch (TPException var7) {
         Loggable var6 = WTCLogger.logErrorExecMBeanDefLoggable(var3);
         var6.log();
         if (var4) {
            ntrace.doTrace("*]/WTCService/startAddWTCImport/10/" + var3 + " setup failed");
         }

         throw new BeanUpdateRejectedException(var6.getMessage());
      }

      this.addImport(var2);
      var2.prepareObject();
      if (var4) {
         ntrace.doTrace("]/WTCService/startAddWTCImport/20/" + var1.getResourceName() + " prepared");
      }

   }

   public void finishAddWTCImport(WTCImportMBean var1, boolean var2) {
      String var4 = var1.getResourceName();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/finishAddWTCImport/" + var4);
      }

      TDMImport var3;
      if ((var3 = this.getImportByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCImport/10/" + var4 + " not found");
         }

      } else if (!var2) {
         var3.deactivateObject();
         this.removeImport(var3);
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCImport/20/" + var4 + " setup failed");
         }

      } else if (!var3.isObjectPrepared()) {
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCImport/30/" + var4 + " not in prepared state");
         }

      } else {
         var3.activateObject();
         var3.setMBean(var1);
         var3.registerListener();
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCImport/40/" + var1.getResourceName() + " activated");
         }

      }
   }

   public void startRemoveWTCImport(WTCImportMBean var1) throws BeanUpdateRejectedException {
      TDMImport var2 = null;
      String var3 = var1.getResourceName();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/startRemoveWTCImport/" + var3);
      }

      Loggable var5;
      if ((var2 = this.getImportByMBean(var1)) == null) {
         var5 = WTCLogger.logErrorNoSuchImportLoggable(var3);
         var5.log();
         if (var4) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCImport/10/not found");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      } else if (this.checkImportInUse(var2)) {
         var5 = WTCLogger.logErrorResourceInUseLoggable(var3);
         var5.log();
         if (var4) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCImport/20/in use");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      } else {
         var2.suspendObject();
         this.addTmpImport(var2);
         this.removeImport(var2);
         if (var4) {
            ntrace.doTrace("]/WTCService/startRemoveWTCImport/30/" + var1.getResourceName() + " suspended");
         }

      }
   }

   public void finishRemoveWTCImport(WTCImportMBean var1, boolean var2) {
      TDMImport var3 = null;
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/finishRemoveWTCImport/" + var1.getResourceName());
      }

      if ((var3 = this.getTmpImportByMBean(var1)) == null) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCImport/10/" + var1.getResourceName() + " rollbacked");
         }

      } else if (!var2) {
         if (var3.isObjectSuspended()) {
            var3.activateObject();
         }

         this.addImport(var3);
         this.removeTmpImport(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCImport/20/" + var1.getResourceName() + " rollbacked");
         }

      } else {
         var3.deactivateObject();
         var3.unregisterListener();
         this.removeTmpImport(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCImport/10/" + var1.getResourceName() + " deactivated");
         }

      }
   }

   public void startAddWTCExport(WTCExportMBean var1) throws BeanUpdateRejectedException {
      TDMExport var2 = null;
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/startAddWTCExport/" + var1.getResourceName());
      }

      try {
         var2 = this.setupTDMExport(var1);
         this.validateExport(var2);
      } catch (TPException var5) {
         if (var3) {
            ntrace.doTrace("]/WTCService/startAddWTCExport/10/" + var1.getResourceName() + " failed");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      }

      this.addExport(var2);
      var2.prepareObject();
      if (var3) {
         ntrace.doTrace("]/WTCService/startAddWTCExport/10/" + var1.getResourceName() + " prepared");
      }

   }

   public void finishAddWTCExport(WTCExportMBean var1, boolean var2) {
      TDMExport var3 = null;
      String var4 = var1.getResourceName();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/finishAddWTCExport/" + var4);
      }

      if ((var3 = this.getExportByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCExport/10/" + var4 + " not found");
         }

      } else if (var2) {
         var3.activateObject();
         var3.setMBean(var1);
         var3.registerListener();
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCExport/20/" + var4 + " activated");
         }

      } else {
         var3.deactivateObject();
         this.removeExport(var3);
         if (var5) {
            ntrace.doTrace("]/WTCService/finishAddWTCExport/10/" + var4 + " not activated");
         }

      }
   }

   public void startRemoveWTCExport(WTCExportMBean var1) throws BeanUpdateRejectedException {
      TDMExport var2 = null;
      String var3 = var1.getResourceName();
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/startRemoveWTCExport/" + var3);
      }

      Loggable var5;
      if ((var2 = this.getExportByMBean(var1)) == null) {
         var5 = WTCLogger.logErrorNoSuchExportLoggable(var3);
         var5.log();
         if (var4) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCExport/10/" + var3 + " not found");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      } else if (this.checkExportInUse(var2)) {
         var5 = WTCLogger.logErrorResourceInUseLoggable(var3);
         var5.log();
         if (var4) {
            ntrace.doTrace("*]/WTCService/startRemoveWTCExport/20/" + var3 + " is in use");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      } else {
         var2.suspendObject();
         this.addTmpExport(var2);
         this.removeExport(var2);
         if (var4) {
            ntrace.doTrace("]/WTCService/startRemoveWTCExport/10/" + var3 + " suspended");
         }

      }
   }

   public void finishRemoveWTCExport(WTCExportMBean var1, boolean var2) {
      TDMExport var3 = null;
      String var4 = var1.getResourceName();
      boolean var5 = ntrace.isMixedTraceEnabled(18);
      if (var5) {
         ntrace.doTrace("[/WTCService/finishRemoveWTCExport/" + var1.getResourceName());
      }

      if ((var3 = this.getTmpExportByMBean(var1)) == null) {
         if (var5) {
            ntrace.doTrace("]/WTCService/startRemoveWTCExport/10/" + var4 + " not found");
         }

      } else if (!var2) {
         if (var3.isObjectSuspended()) {
            var3.activateObject();
         }

         this.addExport(var3);
         this.removeTmpExport(var3);
         if (var5) {
            ntrace.doTrace("]/WTCService/startRemoveWTCExport/20/" + var4 + " rollbacked");
         }

      } else {
         var3.deactivateObject();
         var3.unregisterListener();
         this.removeTmpExport(var3);
         if (var5) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCExport/30/" + var4 + " prepared");
         }

      }
   }

   public void startAddWTCPassword(WTCPasswordMBean var1) throws BeanUpdateRejectedException {
      TDMPasswd var2 = null;
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/startAddWTCPassword/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      try {
         var2 = this.setupTDMPasswd(var1);
      } catch (TPException var6) {
         Loggable var5 = WTCLogger.logErrorExecMBeanDefLoggable(var1.getName());
         if (var3) {
            ntrace.doTrace("*]/WTCService/startAddWTCPassword/10/setup failed");
         }

         throw new BeanUpdateRejectedException(var5.getMessage());
      }

      if (!this.validatePasswd(var2)) {
         if (var3) {
            ntrace.doTrace("*]/WTCService/startAddWTCPassword/20/duplicate");
         }

         throw new BeanUpdateRejectedException("ERROR: duplicated password entry");
      } else {
         this.addPasswd(var2);
         var2.prepareObject();
         if (var3) {
            ntrace.doTrace("]/WTCService/startAddWTCPassword/20/prepared");
         }

      }
   }

   public void finishAddWTCPassword(WTCPasswordMBean var1, boolean var2) {
      TDMPasswd var3 = null;
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/finishAddWTCPassword/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      if ((var3 = this.getPasswdByMBean(var1)) == null) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCPassword/10/not found");
         }

      } else if (!var2) {
         this.removePasswd(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCPassword/20/rollbacked");
         }

      } else if (!var3.isObjectPrepared()) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCPassword/30/not in prepared state");
         }

      } else {
         var3.setMBean(var1);
         var3.registerListener();
         var3.activateObject();
         if (var4) {
            ntrace.doTrace("]/WTCService/finishAddWTCPassword/10/activated");
         }

      }
   }

   public void startRemoveWTCPassword(WTCPasswordMBean var1) throws BeanUpdateRejectedException {
      TDMPasswd var2 = null;
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      if (var3) {
         ntrace.doTrace("[/WTCService/startRemoveWTCPassword/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      if ((var2 = this.getPasswdByMBean(var1)) == null) {
         Loggable var4 = WTCLogger.logErrorNoSuchPasswordLoggable(var1.getName());
         var4.log();
         if (var3) {
            ntrace.doTrace("]/WTCService/startRemoveWTCPassword/10/not found");
         }

         throw new BeanUpdateRejectedException(var4.getMessage());
      } else if (this.checkPasswdInUse(var2)) {
         if (var3) {
            ntrace.doTrace("]/WTCService/startRemoveWTCPassword/20/not found");
         }

         throw new BeanUpdateRejectedException("ERROR: Requested password object in use, can not be deleted.");
      } else {
         var2.suspendObject();
         if (var3) {
            ntrace.doTrace("]/WTCService/startRemoveWTCExport/30/suspended");
         }

      }
   }

   public void finishRemoveWTCPassword(WTCPasswordMBean var1, boolean var2) {
      TDMPasswd var3 = null;
      boolean var4 = ntrace.isMixedTraceEnabled(18);
      if (var4) {
         ntrace.doTrace("[/WTCService/finishRemoveWTCPassword/(" + var1.getLocalAccessPoint() + ", " + var1.getRemoteAccessPoint() + ")");
      }

      if ((var3 = this.getPasswdByMBean(var1)) == null) {
         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCPassword/10/prepared");
         }

      } else if (!var2) {
         if (var3.isObjectSuspended()) {
            var3.activateObject();
         }

         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCPassword/20/rollbacked");
         }

      } else {
         var3.deactivateObject();
         var3.unregisterListener();
         this.removePasswd(var3);
         if (var4) {
            ntrace.doTrace("]/WTCService/finishRemoveWTCExport/10/deactivated");
         }

      }
   }

   private void startOatmialListener(TDMLocalTDomain var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/startOatmialListener/ldom:" + var1.getAccessPoint());
      }

      if (var1.getOatmialListener() == null) {
         if (var2) {
            ntrace.doTrace("start OATMIAL Listener");
         }

         OatmialListener var3 = new OatmialListener(myTimeService, var1, this, this.unknownTxidRply);
         var3.setDaemon(true);
         var3.start();
         var1.setOatmialListener(var3);
      }

      if (var2) {
         ntrace.doTrace("]/WTCService/startOatmialListener/10/Done");
      }

   }

   private void removeFromArray(Object var1, int var2, Object var3, int var4) {
      if (var4 > 0) {
         System.arraycopy(var1, 0, var3, 0, var4);
      }

      if (var4 < var2 - 1) {
         System.arraycopy(var1, var4 + 1, var3, var4, var2 - var4 - 1);
      }

   }

   public synchronized void changeImportResourceName(TDMImport var1, String var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/changeImportResourceName/" + var2);
      }

      HashSet var4 = (HashSet)this.myImportedServices.get(var2);
      TDMImport var5 = null;
      if (null != var4) {
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            var5 = (TDMImport)var6.next();
            if (var5 == var1) {
               break;
            }
         }
      }

      if (var5 != var1) {
         if (var3) {
            ntrace.doTrace("]/WTCService/changeImportResourceName/10/not found");
         }

      } else {
         var4.remove(var5);
         if (var4.isEmpty()) {
            this.myImportedServices.remove(var2);
         }

         this.addImport(var5);
         if (var3) {
            ntrace.doTrace("]/WTCService/changeImportResourceName/20/success");
         }

      }
   }

   public synchronized void changeExportResourceName(TDMExport var1, String var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/changeExportResourceName/" + var2);
      }

      HashSet var4 = (HashSet)this.myExportedServices.get(var2);
      TDMExport var5 = null;
      if (null != var4) {
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            var5 = (TDMExport)var6.next();
            if (var5 == var1) {
               break;
            }
         }
      }

      if (var5 != var1) {
         if (var3) {
            ntrace.doTrace("]/WTCService/changeExportResourceName/10/not found");
         }

      } else {
         var4.remove(var5);
         if (var4.isEmpty()) {
            this.myExportedServices.remove(var2);
         }

         this.addExport(var5);
         if (var3) {
            ntrace.doTrace("]/WTCService/changeExportResourceName/20/success");
         }

      }
   }

   public synchronized TDMExport getExportedService(String var1, String var2) {
      HashSet var3 = (HashSet)this.myExportedServices.get(var1);
      if (null == var3) {
         return null;
      } else {
         Iterator var4 = var3.iterator();

         TDMExport var5;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (TDMExport)var4.next();
         } while(!var5.getLocalAccessPoint().equals(var2) || var5.getStatus() != 3);

         return var5;
      }
   }

   public synchronized TDMImport getImport(String var1, Xid var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/getImport/(svc = " + var1 + ", xid = " + var2 + ")");
      }

      int var6 = 0;
      TDMRemoteTDomain[] var8 = null;
      HashSet var4;
      if ((var4 = (HashSet)this.myImportedServices.get(var1)) == null) {
         if (var3) {
            ntrace.doTrace("*]/WTCService/getImport/10/" + var4);
         }

         throw new TPException(6, "Could not find service " + var1);
      } else {
         int var5;
         if ((var5 = var4.size()) == 0) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/getImport/20/Zero length services array");
            }

            throw new TPException(12, "Invalid service array for " + var1);
         } else {
            if (var2 != null && this.xaAffinity) {
               synchronized(myOutboundXidMap) {
                  HashSet var9;
                  if ((var9 = (HashSet)myOutboundXidMap.get(var2)) != null && var9.size() > 0) {
                     var8 = (TDMRemoteTDomain[])((TDMRemoteTDomain[])var9.toArray(new TDMRemoteTDomain[var9.size()]));
                  }
               }
            }

            if (this.available_array == null || this.available_array.length < var5) {
               this.available_array = new TDMImport[var5];
            }

            Iterator var20 = var4.iterator();

            while(var20.hasNext()) {
               TDMImport var7 = (TDMImport)var20.next();
               if (var7.getStatus() == 3) {
                  this.available_array[var6++] = var7;
               }
            }

            if (var6 == 0) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/getImport/30/TPENOENT");
               }

               throw new TPException(6, "No local or remote domain available for " + var1);
            } else {
               int var10;
               if (var6 <= 1) {
                  var10 = 0;
               } else {
                  var10 = -1;
                  int var11;
                  if (var8 != null) {
                     int var14 = var8.length;
                     int var16 = Integer.MAX_VALUE;
                     byte var18 = 0;

                     for(var11 = 0; var11 < var6 && var18 < 2; ++var11) {
                        TDMRemote[] var17 = this.available_array[var11].getRemoteAccessPointObjectList();
                        int var15 = var17.length;
                        int var13 = 0;

                        for(var18 = 0; var13 < var15 && var18 < 1; ++var13) {
                           for(int var12 = 0; var12 < var14 && var18 < 1; ++var12) {
                              if (var8[var12] == var17[var13]) {
                                 if (var13 == 0) {
                                    var16 = 0;
                                    var10 = var11;
                                    var18 = 2;
                                    break;
                                 }

                                 if (var13 < var16) {
                                    var16 = var13;
                                    var10 = var11;
                                    var18 = 1;
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (var10 == -1) {
                     if (this.myRandom == null) {
                        this.myRandom = new Random();
                     }

                     if ((var11 = this.myRandom.nextInt()) < 0) {
                        if (var11 == Integer.MIN_VALUE) {
                           var11 = Integer.MAX_VALUE;
                        } else {
                           var11 *= -1;
                        }
                     }

                     var10 = var11 % var6;
                  }
               }

               if (var3) {
                  ntrace.doTrace("]/WTCService/getImport/40/(" + var10 + ")");
               }

               return this.available_array[var10];
            }
         }
      }
   }

   public TDMImport getImport(String var1, String var2, String var3) {
      boolean var6 = ntrace.isTraceEnabled(2);
      if (var6) {
         ntrace.doTrace("[/WTCService/getImport/" + var1 + ", remote " + var3);
      }

      HashSet var4 = (HashSet)this.myImportedServices.get(var1);
      if (var4 != null) {
         Iterator var8 = var4.iterator();

         while(var8.hasNext()) {
            TDMImport var5 = (TDMImport)var8.next();
            String[] var7 = var5.getRemoteAccessPointList();
            if (var1.equals(var5.getResourceName()) && var2.equals(var5.getLocalAccessPoint()) && var3.equals(var7[0])) {
               if (var6) {
                  ntrace.doTrace("*]/WTCService/getImport/10/found import service name " + var1);
               }

               return var5;
            }
         }
      }

      if (var6) {
         ntrace.doTrace("]/WTCService/getImport/20/import not found service name " + var1);
      }

      return null;
   }

   private synchronized TDMLocal getLocalDomainById(String var1) {
      for(int var2 = 0; var2 < this.ltdcnt; ++var2) {
         if (this.ltd_list[var2] != null && var1.equals(this.ltd_list[var2].getAccessPointId())) {
            return this.ltd_list[var2];
         }
      }

      return null;
   }

   public void startConnection(String var1, String var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/startConnection/ldom=" + var1 + " rdom=" + var2);
      }

      TDMRemoteTDomain var4 = this.getRemoteTDomain(var2);
      if (null == var4) {
         Loggable var7 = WTCLogger.logErrorNoSuchRemoteDomainLoggable(var2);
         var7.log();
         if (var3) {
            ntrace.doTrace("*]/WTCService/startConnection/10/remote domain not found " + var2);
         }

         throw new TPException(6, var7.getMessage());
      } else {
         TDMLocal var5 = var4.getLocalAccessPointObject();
         if (null != var5 && var1.equals(var5.getAccessPointId())) {
            var4.startConnection();
            if (var3) {
               ntrace.doTrace("]/WTCService/startConnection/30/DONE");
            }

         } else {
            Loggable var6 = WTCLogger.logErrorNoSuchLocalDomainLoggable(var1);
            var6.log();
            if (var3) {
               ntrace.doTrace("*]/WTCService/startConnection/20/local domain not found " + var1);
            }

            throw new TPException(6, var6.getMessage());
         }
      }
   }

   public void startConnection(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WTCService/startConnection/ldom=" + var1);
      }

      TDMLocal var3 = this.getLocalDomainById(var1);
      if (null == var3) {
         Loggable var6 = WTCLogger.logErrorNoSuchLocalDomainLoggable(var1);
         var6.log();
         if (var2) {
            ntrace.doTrace("*]/WTCService/startConnection/210/local domain not found " + var1);
         }

         throw new TPException(6, var6.getMessage());
      } else {
         TDMRemote[] var4 = var3.get_remote_domains();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var2) {
               ntrace.doTrace("[/WTCService/startConnection/rdom=" + var4[var5].getAccessPoint());
            }

            ((TDMRemoteTDomain)var4[var5]).startConnection();
         }

         if (var2) {
            ntrace.doTrace("]/WTCService/startConnection/DONE");
         }

      }
   }

   private void stopConnection(TDMRemoteTDomain var1) throws TPException {
      var1.terminateConnectingTask();
      gwatmi var2;
      if ((var2 = var1.getTsession(false)) != null) {
         var2.tpterm();
      }

   }

   public void stopConnection(String var1, String var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/stopConnection/ldom=" + var1);
      }

      TDMRemoteTDomain var4 = this.getRemoteTDomain(var2);
      if (null == var4) {
         Loggable var7 = WTCLogger.logErrorNoSuchRemoteDomainLoggable(var2);
         var7.log();
         if (var3) {
            ntrace.doTrace("*]/WTCService/stopConnection/10/remote domain not found " + var2);
         }

         throw new TPException(6, var7.getMessage());
      } else {
         TDMLocal var5 = var4.getLocalAccessPointObject();
         if (null != var5 && var1.equals(var5.getAccessPointId())) {
            this.stopConnection(var4);
            if (var3) {
               ntrace.doTrace("]/WTCService/stopConnection/DONE");
            }

         } else {
            Loggable var6 = WTCLogger.logErrorNoSuchLocalDomainLoggable(var1);
            var6.log();
            if (var3) {
               ntrace.doTrace("*]/WTCService/stopConnection/20/local domain not found " + var1);
            }

            throw new TPException(6, var6.getMessage());
         }
      }
   }

   public void stopConnection(String var1) throws TPException {
      TDMLocal var2 = this.getLocalDomainById(var1);
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/stopConnection/ldom=" + var1);
      }

      if (null == var2) {
         Loggable var6 = WTCLogger.logErrorNoSuchLocalDomainLoggable(var1);
         var6.log();
         if (var3) {
            ntrace.doTrace("*]/WTCService/stopConnection/10/local domain not found " + var1);
         }

         throw new TPException(6, var6.getMessage());
      } else {
         TDMRemote[] var4 = var2.get_remote_domains();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var3) {
               ntrace.doTrace("[/WTCService/stopConnection/rdom=" + var4[var5].getAccessPoint());
            }

            this.stopConnection((TDMRemoteTDomain)var4[var5]);
         }

         if (var3) {
            ntrace.doTrace("]/WTCService/stopConnection/DONE");
         }

      }
   }

   public DSessConnInfo[] listConnectionsConfigured() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/listConnectionsConfigured");
      }

      DSessConnInfo[] var2 = new DSessConnInfo[this.rtdcnt];
      int var3 = 0;
      int var4;
      synchronized(this) {
         var4 = this.rtdcnt;

         for(int var6 = 0; var6 < this.rtdcnt; ++var6) {
            if (this.rtd_list[var6] != null) {
               String var7 = this.rtd_list[var6].getAccessPointId();
               TDMLocal var8 = this.rtd_list[var6].getLocalAccessPointObject();
               String var9 = null;
               if (var8 != null) {
                  var9 = var8.getAccessPointId();
               }

               boolean var10 = this.rtd_list[var6].getTsession(false) != null;
               String var11 = "";
               var11 = var11 + var10;
               if (!var10 && this.rtd_list[var6].hasConnectingTask()) {
                  var11 = "retrying";
               }

               var2[var3++] = new DSessConnInfo(var9, var7, var11);
            }
         }
      }

      DSessConnInfo[] var5;
      if (var3 == var4) {
         var5 = var2;
      } else {
         var5 = new DSessConnInfo[var3];
         System.arraycopy(var2, 0, var5, 0, var3);
      }

      if (var1) {
         ntrace.doTrace("]/WTCService/listConnectionsConfigured/DONE");
      }

      return var5;
   }

   public void suspendService(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      boolean var6 = false;
      if (var2) {
         ntrace.doTrace("[/WTCService/suspendService/svc = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var3;
         Iterator var7;
         if ((var3 = (HashSet)this.myImportedServices.get(var1)) != null && var3.size() != 0) {
            var7 = var3.iterator();

            while(var7.hasNext()) {
               TDMImport var4 = (TDMImport)var7.next();
               var4.suspend();
            }

            var6 = true;
         }

         if ((var3 = (HashSet)this.myExportedServices.get(var1)) != null && var3.size() != 0) {
            var7 = var3.iterator();

            while(var7.hasNext()) {
               TDMExport var5 = (TDMExport)var7.next();
               var5.suspend();
            }

            var6 = true;
         }

         if (!var6) {
            if (var2) {
               ntrace.doTrace("*]/WTCService/suspendService/10/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var1 + " found!");
         } else {
            if (var2) {
               ntrace.doTrace("]/WTCService/suspendService/20/DONE");
            }

         }
      } else {
         if (var2) {
            ntrace.doTrace("*]/WTCService/suspendService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void suspendService(String var1, boolean var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/suspendService/svc = " + var1 + ", isImport = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var4;
         Iterator var8;
         if (var2) {
            if ((var4 = (HashSet)this.myImportedServices.get(var1)) != null && var4.size() != 0) {
               var8 = var4.iterator();

               while(var8.hasNext()) {
                  TDMImport var5 = (TDMImport)var8.next();
                  var5.suspend();
               }

               var7 = true;
            }
         } else if ((var4 = (HashSet)this.myExportedServices.get(var1)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMExport var6 = (TDMExport)var8.next();
               var6.suspend();
            }

            var7 = true;
         }

         if (!var7) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/suspendService/10/TPENOENT");
            }

            if (var2) {
               throw new TPException(6, "No imported services/resources of the name " + var1 + " found!");
            } else {
               throw new TPException(6, "No exported services/resources of the name " + var1 + " found!");
            }
         } else {
            if (var3) {
               ntrace.doTrace("]/WTCService/suspendService/20/DONE");
            }

         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/suspendService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void suspendService(String var1, String var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/suspendService/svc = " + var2 + ", ldom = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var4;
         Iterator var8;
         if ((var4 = (HashSet)this.myImportedServices.get(var2)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMImport var5 = (TDMImport)var8.next();
               if (var5.match(var1, (String)null)) {
                  var5.suspend();
                  var7 = true;
               }
            }
         }

         if ((var4 = (HashSet)this.myExportedServices.get(var2)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMExport var6 = (TDMExport)var8.next();
               if (var6.match(var1)) {
                  var6.suspend();
                  var7 = true;
               }
            }
         }

         if (!var7) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/suspendService/10/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var2 + " found!");
         } else {
            if (var3) {
               ntrace.doTrace("]/WTCService/suspendService/20/DONE");
            }

         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/suspendService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void suspendService(String var1, String var2, boolean var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      boolean var8 = false;
      if (var4) {
         ntrace.doTrace("[/WTCService/suspendService/svc = " + var2 + ", ldom = " + var1 + ", isImport = " + var3);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var5;
         Iterator var9;
         if (var3) {
            if ((var5 = (HashSet)this.myImportedServices.get(var2)) != null && var5.size() != 0) {
               var9 = var5.iterator();

               while(var9.hasNext()) {
                  TDMImport var6 = (TDMImport)var9.next();
                  if (var6.match(var1, (String)null)) {
                     var6.suspend();
                     var8 = true;
                  }
               }
            }
         } else if ((var5 = (HashSet)this.myExportedServices.get(var2)) != null && var5.size() != 0) {
            var9 = var5.iterator();

            while(var9.hasNext()) {
               TDMExport var7 = (TDMExport)var9.next();
               if (var7.match(var1)) {
                  var7.suspend();
                  var8 = true;
               }
            }
         }

         if (!var8) {
            if (var4) {
               ntrace.doTrace("*]/WTCService/suspendService/10/TPENOENT");
            }

            if (var3) {
               throw new TPException(6, "No imported services/resources of the name " + var2 + " for local access point " + var1 + " found!");
            } else {
               throw new TPException(6, "No exported services/resources of the name " + var2 + " for local access point " + var1 + " found!");
            }
         } else {
            if (var4) {
               ntrace.doTrace("]/WTCService/suspendService/20/DONE");
            }

         }
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/suspendService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void suspendService(String var1, String var2, String var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var4) {
         ntrace.doTrace("[/WTCService/suspendService/svc = " + var3 + ", ldom = " + var1 + ", rdomlist = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var5;
         if ((var5 = (HashSet)this.myImportedServices.get(var3)) != null && var5.size() != 0) {
            Iterator var8 = var5.iterator();

            while(var8.hasNext()) {
               TDMImport var6 = (TDMImport)var8.next();
               if (var6.match(var1, var2)) {
                  var6.suspend();
                  var7 = true;
               }
            }
         }

         if (!var7) {
            if (var4) {
               ntrace.doTrace("*]/WTCService/suspendService/10/TPENOENT");
            }

            throw new TPException(6, "No imported services/resources of the name " + var3 + " for local access point " + var1 + "and remote access point list " + var2 + " found!");
         } else {
            if (var4) {
               ntrace.doTrace("]/WTCService/suspendService/DONE");
            }

         }
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/suspendService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void resumeService(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      boolean var6 = false;
      if (var2) {
         ntrace.doTrace("[/WTCService/resumeService/svc = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var3;
         Iterator var7;
         if ((var3 = (HashSet)this.myImportedServices.get(var1)) != null && var3.size() != 0) {
            var7 = var3.iterator();

            while(var7.hasNext()) {
               TDMImport var4 = (TDMImport)var7.next();
               var4.resume();
            }

            var6 = true;
         }

         if ((var3 = (HashSet)this.myExportedServices.get(var1)) != null && var3.size() != 0) {
            var7 = var3.iterator();

            while(var7.hasNext()) {
               TDMExport var5 = (TDMExport)var7.next();
               var5.resume();
            }

            var6 = true;
         }

         if (!var6) {
            if (var2) {
               ntrace.doTrace("*]/WTCService/resumeService/10/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var1 + " found!");
         } else {
            if (var2) {
               ntrace.doTrace("]/WTCService/resumeService/20/DONE");
            }

         }
      } else {
         if (var2) {
            ntrace.doTrace("*]/WTCService/resumeService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void resumeService(String var1, boolean var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/resumeService/svc = " + var1 + ", isImport = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var4;
         Iterator var8;
         if (var2) {
            if ((var4 = (HashSet)this.myImportedServices.get(var1)) != null && var4.size() != 0) {
               var8 = var4.iterator();

               while(var8.hasNext()) {
                  TDMImport var5 = (TDMImport)var8.next();
                  var5.resume();
               }

               var7 = true;
            }
         } else if ((var4 = (HashSet)this.myExportedServices.get(var1)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMExport var6 = (TDMExport)var8.next();
               var6.resume();
            }

            var7 = true;
         }

         if (!var7) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/resumeService/10/TPENOENT");
            }

            if (var2) {
               throw new TPException(6, "No imported services/resources of the name " + var1 + " found!");
            } else {
               throw new TPException(6, "No exported services/resources of the name " + var1 + " found!");
            }
         } else {
            if (var3) {
               ntrace.doTrace("]/WTCService/resumeService/20/DONE");
            }

         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/resumeService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void resumeService(String var1, String var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/resumeService/svc = " + var2 + ", ldom = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var4;
         Iterator var8;
         if ((var4 = (HashSet)this.myImportedServices.get(var2)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMImport var5 = (TDMImport)var8.next();
               if (var5.match(var1, (String)null)) {
                  var5.resume();
                  var7 = true;
               }
            }
         }

         if ((var4 = (HashSet)this.myExportedServices.get(var2)) != null && var4.size() != 0) {
            var8 = var4.iterator();

            while(var8.hasNext()) {
               TDMExport var6 = (TDMExport)var8.next();
               if (var6.match(var1)) {
                  var6.resume();
                  var7 = true;
               }
            }
         }

         if (!var7) {
            if (var3) {
               ntrace.doTrace("*]/WTCService/resumeService/10/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var2 + " found!");
         } else {
            if (var3) {
               ntrace.doTrace("]/WTCService/resumeService/DONE");
            }

         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/resumeService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void resumeService(String var1, String var2, boolean var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      boolean var8 = false;
      if (var4) {
         ntrace.doTrace("[/WTCService/resumeService/svc = " + var2 + ", ldom = " + var1 + ", isImport = " + var3);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var5;
         Iterator var9;
         if (var3) {
            if ((var5 = (HashSet)this.myImportedServices.get(var2)) != null && var5.size() != 0) {
               var9 = var5.iterator();

               while(var9.hasNext()) {
                  TDMImport var6 = (TDMImport)var9.next();
                  if (var6.match(var1, (String)null)) {
                     var6.resume();
                     var8 = true;
                  }
               }
            }
         } else if ((var5 = (HashSet)this.myExportedServices.get(var2)) != null && var5.size() != 0) {
            var9 = var5.iterator();

            while(var9.hasNext()) {
               TDMExport var7 = (TDMExport)var9.next();
               if (var7.match(var1)) {
                  var7.resume();
                  var8 = true;
               }
            }
         }

         if (!var8) {
            if (var4) {
               ntrace.doTrace("*]/WTCService/resumeService/10/TPENOENT");
            }

            if (var3) {
               throw new TPException(6, "No imported services/resources of the name " + var2 + " for local access point " + var1 + " found!");
            } else {
               throw new TPException(6, "No exported services/resources of the name " + var2 + " for local access point " + var1 + " found!");
            }
         } else {
            if (var4) {
               ntrace.doTrace("]/WTCService/resumeService/DONE");
            }

         }
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/resumeService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public void resumeService(String var1, String var2, String var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      if (var4) {
         ntrace.doTrace("[/WTCService/resumeService/svc = " + var3 + ", ldom = " + var1 + ", rdomlist = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var5;
         if ((var5 = (HashSet)this.myImportedServices.get(var3)) != null && var5.size() != 0) {
            Iterator var8 = var5.iterator();

            while(var8.hasNext()) {
               TDMImport var6 = (TDMImport)var8.next();
               if (var6.match(var1, var2)) {
                  var6.resume();
                  var7 = true;
               }
            }
         }

         if (!var7) {
            if (var4) {
               ntrace.doTrace("*]/WTCService/resumeService/10/TPENOENT");
            }

            throw new TPException(6, "No imported services/resources of the name " + var3 + " for local access point " + var1 + "and remote access point list " + var2 + " found!");
         } else {
            if (var4) {
               ntrace.doTrace("]/WTCService/resumeService/DONE");
            }

         }
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/resumeService/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public DServiceInfo[] getServiceStatus() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(2);
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      if (var1) {
         ntrace.doTrace("[/WTCService/getServiceStatus/");
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         Iterator var2;
         HashSet var10;
         for(var2 = this.myExportedServices.values().iterator(); var2.hasNext(); var6 += var10.size()) {
            var10 = (HashSet)var2.next();
         }

         for(var2 = this.myImportedServices.values().iterator(); var2.hasNext(); var7 += var10.size()) {
            var10 = (HashSet)var2.next();
         }

         int var9 = var6 + var7;
         if (var1) {
            ntrace.doTrace("total = " + var9 + ", lsize = " + var6 + ", rsize = " + var7);
         }

         if (var9 == 0) {
            if (var1) {
               ntrace.doTrace("*]/WTCService/getServiceStatus/TPENOENT");
            }

            return null;
         } else {
            DServiceInfo[] var11 = new DServiceInfo[var9];
            var2 = this.myImportedServices.values().iterator();

            Iterator var3;
            while(var2.hasNext()) {
               var10 = (HashSet)var2.next();

               TDMImport var4;
               for(var3 = var10.iterator(); var3.hasNext(); var11[var8++] = var4.getServiceInfo()) {
                  var4 = (TDMImport)var3.next();
               }
            }

            var2 = this.myExportedServices.values().iterator();

            while(var2.hasNext()) {
               var10 = (HashSet)var2.next();

               TDMExport var5;
               for(var3 = var10.iterator(); var3.hasNext(); var11[var8++] = var5.getServiceInfo()) {
                  var5 = (TDMExport)var3.next();
               }
            }

            if (var1) {
               ntrace.doTrace("]/WTCService/getServiceStatus/DONE");
            }

            return var11;
         }
      } else {
         if (var1) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/null");
         }

         return null;
      }
   }

   public int getServiceStatus(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      boolean var7 = false;
      boolean var8 = false;
      if (var2) {
         ntrace.doTrace("[/WTCService/getServiceStatus/svc = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         Iterator var3;
         HashSet var6;
         if ((var6 = (HashSet)this.myImportedServices.get(var1)) != null && var6.size() != 0) {
            var3 = var6.iterator();

            while(var3.hasNext()) {
               TDMImport var4 = (TDMImport)var3.next();
               switch (var4.getStatus()) {
                  case 2:
                     var8 = true;
                     break;
                  case 3:
                     if (var2) {
                        ntrace.doTrace("]/WTCService/getServiceStatus/10/AVAILABLE");
                     }

                     return 3;
                  default:
                     var7 = true;
               }
            }
         }

         if ((var6 = (HashSet)this.myExportedServices.get(var1)) != null && var6.size() != 0) {
            var3 = var6.iterator();

            while(var3.hasNext()) {
               TDMExport var5 = (TDMExport)var3.next();
               switch (var5.getStatus()) {
                  case 2:
                     var8 = true;
                     break;
                  case 3:
                     if (var2) {
                        ntrace.doTrace("]/WTCService/getServiceStatus/20/AVAILABLE");
                     }

                     return 3;
                  default:
                     var7 = true;
               }
            }
         }

         if (var7) {
            if (var2) {
               ntrace.doTrace("]/WTCService/getServiceStatus/30/SUSPENDED");
            }

            return 1;
         } else if (var8) {
            if (var2) {
               ntrace.doTrace("]/WTCService/getServiceStatus/40/UNAVAILABLE");
            }

            return 2;
         } else {
            if (var2) {
               ntrace.doTrace("*]/WTCService/getServiceStatus/50/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var1 + " found");
         }
      } else {
         if (var2) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public int getServiceStatus(String var1, boolean var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var8 = false;
      boolean var9 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/getServiceStatus/svc = " + var1 + ", isImport = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         Iterator var4;
         HashSet var7;
         if (var2) {
            if ((var7 = (HashSet)this.myImportedServices.get(var1)) != null && var7.size() != 0) {
               var4 = var7.iterator();

               while(var4.hasNext()) {
                  TDMImport var5 = (TDMImport)var4.next();
                  switch (var5.getStatus()) {
                     case 2:
                        var9 = true;
                        break;
                     case 3:
                        if (var3) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/10/AVAILABLE");
                        }

                        return 3;
                     default:
                        var8 = true;
                  }
               }
            }
         } else if ((var7 = (HashSet)this.myExportedServices.get(var1)) != null && var7.size() != 0) {
            var4 = var7.iterator();

            while(var4.hasNext()) {
               TDMExport var6 = (TDMExport)var4.next();
               switch (var6.getStatus()) {
                  case 2:
                     var9 = true;
                     break;
                  case 3:
                     if (var3) {
                        ntrace.doTrace("]/WTCService/getServiceStatus/20/AVAILABLE");
                     }

                     return 3;
                  default:
                     var8 = true;
               }
            }
         }

         if (var8) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getServiceStatus/30/SUSPENDED");
            }

            return 1;
         } else if (var9) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getServiceStatus/40/UNAVAILABLE");
            }

            return 2;
         } else {
            if (var3) {
               ntrace.doTrace("*]/WTCService/getServiceStatus/50/TPENOENT");
            }

            if (var2) {
               throw new TPException(6, "No imported services/resources of the name " + var1 + " found");
            } else {
               throw new TPException(6, "No imported services/resources of the name " + var1 + " found");
            }
         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public int getServiceStatus(String var1, String var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      boolean var8 = false;
      boolean var9 = false;
      if (var3) {
         ntrace.doTrace("[/WTCService/getServiceStatus/svc = " + var2 + ", ldom = " + var1);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         Iterator var4;
         HashSet var7;
         if ((var7 = (HashSet)this.myImportedServices.get(var2)) != null && var7.size() != 0) {
            var4 = var7.iterator();

            while(var4.hasNext()) {
               TDMImport var5 = (TDMImport)var4.next();
               if (var5.match(var1, (String)null)) {
                  switch (var5.getStatus()) {
                     case 2:
                        var9 = true;
                        break;
                     case 3:
                        if (var3) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/10/AVAILABLE");
                        }

                        return 3;
                     default:
                        var8 = true;
                  }
               }
            }
         }

         if ((var7 = (HashSet)this.myExportedServices.get(var2)) != null && var7.size() != 0) {
            var4 = var7.iterator();

            while(var4.hasNext()) {
               TDMExport var6 = (TDMExport)var4.next();
               if (var6.match(var1)) {
                  switch (var6.getStatus()) {
                     case 2:
                        var9 = true;
                        break;
                     case 3:
                        if (var3) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/20/AVAILABLE");
                        }

                        return 3;
                     default:
                        var8 = true;
                  }
               }
            }
         }

         if (var8) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getServiceStatus/30/SUSPENDED");
            }

            return 1;
         } else if (var9) {
            if (var3) {
               ntrace.doTrace("]/WTCService/getServiceStatus/40/UNAVAILABLE");
            }

            return 2;
         } else {
            if (var3) {
               ntrace.doTrace("*]/WTCService/getServiceStatus/50/TPENOENT");
            }

            throw new TPException(6, "No imported or exported services/resources of the name " + var2 + " for local access point " + var1 + " found");
         }
      } else {
         if (var3) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public int getServiceStatus(String var1, String var2, boolean var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      boolean var9 = false;
      boolean var10 = false;
      if (var4) {
         ntrace.doTrace("[/WTCService/getServiceStatus/svc = " + var2 + ", ldom = " + var1 + ", isImport = " + var3);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         Iterator var5;
         HashSet var8;
         if (var3) {
            if ((var8 = (HashSet)this.myImportedServices.get(var2)) != null && var8.size() != 0) {
               var5 = var8.iterator();

               while(var5.hasNext()) {
                  TDMImport var6 = (TDMImport)var5.next();
                  if (var6.match(var1, (String)null)) {
                     switch (var6.getStatus()) {
                        case 2:
                           var10 = true;
                           break;
                        case 3:
                           if (var4) {
                              ntrace.doTrace("]/WTCService/getServiceStatus/10/AVAILABLE");
                           }

                           return 3;
                        default:
                           var9 = true;
                     }
                  }
               }
            }
         } else if ((var8 = (HashSet)this.myExportedServices.get(var2)) != null && var8.size() != 0) {
            var5 = var8.iterator();

            while(var5.hasNext()) {
               TDMExport var7 = (TDMExport)var5.next();
               if (var7.match(var1)) {
                  switch (var7.getStatus()) {
                     case 2:
                        var10 = true;
                        break;
                     case 3:
                        if (var4) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/20/AVAILABLE");
                        }

                        return 3;
                     default:
                        var9 = true;
                  }
               }
            }
         }

         if (var9) {
            if (var4) {
               ntrace.doTrace("]/WTCService/getServiceStatus/30/SUSPENDED");
            }

            return 1;
         } else if (var10) {
            if (var4) {
               ntrace.doTrace("]/WTCService/getServiceStatus/40/UNAVAILABLE");
            }

            return 2;
         } else {
            if (var4) {
               ntrace.doTrace("*]/WTCService/getServiceStatus/50/TPENOENT");
            }

            if (var3) {
               throw new TPException(6, "No imported services/resources of the name " + var2 + " for local access point " + var1 + " found");
            } else {
               throw new TPException(6, "No exported services/resources of the name " + var2 + " for local access point " + var1 + " found");
            }
         }
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no imported/exported servicecan be found!");
      }
   }

   public int getServiceStatus(String var1, String var2, String var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(2);
      if (var4) {
         ntrace.doTrace("[/WTCService/getServiceStatus/svc = " + var3 + ", ldom = " + var1 + ", rdomlist = " + var2);
      }

      if (this.myImportedServices != null && this.myExportedServices != null) {
         HashSet var7;
         if ((var7 = (HashSet)this.myExportedServices.get(var3)) != null && var7.size() != 0) {
            Iterator var5 = var7.iterator();

            while(var5.hasNext()) {
               TDMImport var6 = (TDMImport)var5.next();
               if (var6.match(var1, var2)) {
                  switch (var6.getStatus()) {
                     case 2:
                        if (var4) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/20/SUSPENDED");
                        }

                        return 1;
                     case 3:
                        if (var4) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/10/AVAILABLE");
                        }

                        return 3;
                     default:
                        if (var4) {
                           ntrace.doTrace("]/WTCService/getServiceStatus/30/UNAVAILABLE");
                        }

                        return 2;
                  }
               }
            }
         }

         if (var4) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/40/TPENOENT");
         }

         throw new TPException(6, "No exported services/resources of the name " + var3 + " for local access point " + var1 + " and remote access point list " + var2 + " found");
      } else {
         if (var4) {
            ntrace.doTrace("*]/WTCService/getServiceStatus/5/TPENOENT");
         }

         throw new TPException(6, "WTC not deployed, no exported servicecan be found!");
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      boolean var3 = ntrace.isMixedTraceEnabled(18);
      int var4 = this.tBridgeConfig;
      if (var3) {
         ntrace.doTrace("[/WTCService/prepareUpdate/length " + var2.length);
      }

      BeanUpdateEvent.PropertyUpdate[] var5 = new BeanUpdateEvent.PropertyUpdate[var2.length];
      boolean[] var6 = new boolean[var2.length];

      int var7;
      for(var7 = 0; var7 < var2.length; ++var7) {
         var6[var7] = false;
      }

      var7 = 0;

      int var8;
      for(var8 = 0; var8 < var2.length; ++var8) {
         if (var2[var8].getPropertyName().indexOf("WTCLocalTuxDom") >= 0) {
            var5[var7++] = var2[var8];
            var6[var8] = true;
         }
      }

      for(var8 = 0; var8 < var2.length; ++var8) {
         if (!var6[var8] && var2[var8].getPropertyName().indexOf("WTCRemoteTuxDom") >= 0) {
            var5[var7++] = var2[var8];
            var6[var8] = true;
         }
      }

      for(var8 = 0; var8 < var2.length; ++var8) {
         if (!var6[var8]) {
            var5[var7++] = var2[var8];
         }
      }

      BeanUpdateEvent.PropertyUpdate var9;
      int var10;
      boolean var11;
      Object var12;
      String var13;
      for(var8 = 0; var8 < var5.length; ++var8) {
         var9 = var5[var8];
         var10 = var9.getUpdateType();
         var11 = var10 == 2;
         var12 = var11 ? var9.getAddedObject() : var9.getRemovedObject();
         var13 = var9.getPropertyName();
         if (var3) {
            ntrace.doTrace("i = " + var8 + ", isAdd = " + var11 + ", type = " + var13);
         }

         if (var13.indexOf("WTCResources") >= 0) {
            try {
               if (myGlobalResources == null) {
                  if (var10 == 1) {
                     WTCServerMBean var14 = (WTCServerMBean)var1.getProposedBean();
                     if (var3) {
                        ntrace.doTrace("WTCServer getProposedBean = " + var14);
                     }

                     WTCResourcesMBean var15 = var14.getWTCResources();
                     if (var3) {
                        ntrace.doTrace("getWTCResources = " + var15);
                     }

                     myGlobalResources = TDMResources.create(var15);
                     myGlobalResources.prepareObject();
                  }
               } else if (var10 == 3) {
                  if (var3) {
                     ntrace.doTrace("remove op, bean = " + (WTCResourcesMBean)var12);
                  }

                  myGlobalResources = null;
               }
            } catch (Exception var16) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/prepareUpdate/10/1/" + var16.getMessage());
               }

               throw new BeanUpdateRejectedException(var16.getMessage());
            }
         } else if (var13.indexOf("WTCtBridgeGlobal") >= 0) {
            if (var10 == 1) {
               if (this.tBridgeStartup) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/30/1/tBridge already started, modification is prohibited.");
                  }

                  throw new BeanUpdateRejectedException("No tBridgeGlobal modification is allowed after it is started.");
               }

               if (var3) {
                  ntrace.doTrace("tBridgeGlobal modified");
               }
            } else if (var10 == 3) {
               if ((this.tBridgeConfig & 1) == 0) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/40/1/tBridge MBean does not exist, nothing to delete.");
                  }

                  throw new BeanUpdateRejectedException("No tBridgeGlobal MBean to remove.");
               }

               --this.tBridgeConfig;
               if (var3) {
                  ntrace.doTrace("tBridgeGlobal is removed, tBridgeConfig = " + this.tBridgeConfig);
               }
            }
         } else if (var13.indexOf("WTCtBridgeRedirect") >= 0) {
            if (var10 == 1) {
               if (this.tBridgeStartup) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/60/1/tBridge already started, modification is prohibited");
                  }

                  throw new BeanUpdateRejectedException("No tBridgeRedirect modification is allowed after tBridge started.");
               }

               if (var3) {
                  ntrace.doTrace("tBridgeRedirect modified");
               }
            } else if (var10 == 3) {
               if (this.tBridgeConfig < 2) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/70/1/No tBridgeRedirect configured, nothing to delete.");
                  }

                  throw new BeanUpdateRejectedException("No tBridgeRedirectMBean to remove.");
               }

               this.tBridgeConfig -= 2;
               if (var3) {
                  ntrace.doTrace("tBridgeRedirect is removed, tBridgeConfig = " + this.tBridgeConfig);
               }
            }
         } else {
            if (var10 == 1) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/prepareUpdate/80/1/Change type not supported");
               }

               throw new BeanUpdateRejectedException("CHANGE operation not supported by WtcSrvrMBean");
            }

            if (!var11) {
               if (var13.indexOf("WTCLocalTuxDom") >= 0) {
                  this.startRemoveWTCLocalTuxDom((WTCLocalTuxDomMBean)var12);
               } else if (var13.indexOf("WTCRemoteTuxDom") >= 0) {
                  this.startRemoveWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var12);
               } else if (var13.indexOf("WTCImport") >= 0) {
                  this.startRemoveWTCImport((WTCImportMBean)var12);
               } else if (var13.indexOf("WTCExport") >= 0) {
                  this.startRemoveWTCExport((WTCExportMBean)var12);
               } else if (var13.indexOf("WTCPassword") >= 0) {
                  this.startRemoveWTCPassword((WTCPasswordMBean)var12);
               } else {
                  if (var13.indexOf("Targets") < 0) {
                     if (var3) {
                        ntrace.doTrace("*]/WTCService/prepareUpdate/80/1/unknow type = " + var13);
                     }

                     throw new BeanUpdateRejectedException("Unknown attribute type " + var13 + " for WtcSrvrMBean");
                  }

                  if (var3) {
                     ntrace.doTrace("WTCServer being untargeted");
                  }
               }
            }
         }
      }

      for(var8 = 0; var8 < var5.length; ++var8) {
         var9 = var5[var8];
         var10 = var9.getUpdateType();
         var11 = var10 == 2;
         var12 = var11 ? var9.getAddedObject() : var9.getRemovedObject();
         var13 = var9.getPropertyName();
         if (var3) {
            ntrace.doTrace("i = " + var8 + ", isAdd = " + var11 + ", type = " + var13);
         }

         if (var13.indexOf("WTCResources") >= 0) {
            try {
               if (myGlobalResources == null && var11) {
                  if (var3) {
                     ntrace.doTrace("add op, bean = " + (WTCResourcesMBean)var12);
                  }

                  myGlobalResources = TDMResources.create((WTCResourcesMBean)var12);
                  myGlobalResources.prepareObject();
               }
            } catch (Exception var19) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/prepareUpdate/10/2/" + var19.getMessage());
               }

               throw new BeanUpdateRejectedException(var19.getMessage());
            }
         } else if (var13.indexOf("WTCtBridgeGlobal") >= 0) {
            if (var11) {
               if ((this.tBridgeConfig & 1) != 0) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/20/tBridge is singleton, and already configured");
                  }

                  throw new BeanUpdateRejectedException("No duplicated tBridgeGlobal is allowed");
               }

               try {
                  tBexec.tBupdateGlobal((WTCtBridgeGlobalMBean)var12);
                  ++this.tBridgeConfig;
                  if (var3) {
                     ntrace.doTrace("tBridgeGlobal being added, tBridgeConfig = " + this.tBridgeConfig);
                  }
               } catch (TPException var17) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/25/tBridge Global configuration error!");
                  }

                  throw new BeanUpdateRejectedException(var17.getMessage());
               }
            }
         } else if (var13.indexOf("WTCtBridgeRedirect") >= 0) {
            if (var11) {
               if (this.tBridgeStartup) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/50/tBridge already deployed, no new redirect will be accepted.");
                  }

                  throw new BeanUpdateRejectedException("tBridge already started, no new tBridgeRedirect accepted.");
               }

               try {
                  tBexec.tBupdateRedirect((WTCtBridgeRedirectMBean)var12);
                  this.tBridgeConfig += 2;
                  if (var3) {
                     ntrace.doTrace("tBridgeRedirect being added, tBridgeConfig = " + this.tBridgeConfig);
                  }
               } catch (TPException var18) {
                  if (var3) {
                     ntrace.doTrace("*]/WTCService/prepareUpdate/35/tBridge Redirect configuration error!");
                  }

                  throw new BeanUpdateRejectedException(var18.getMessage());
               }
            }
         } else {
            if (var10 == 1) {
               if (var3) {
                  ntrace.doTrace("*]/WTCService/prepareUpdate/80/2/Change type not supported");
               }

               throw new BeanUpdateRejectedException("CHANGE operation not supported by WtcSrvrMBean");
            }

            if (var11) {
               if (var13.indexOf("WTCLocalTuxDom") >= 0) {
                  this.startAddWTCLocalTuxDom((WTCLocalTuxDomMBean)var12);
               } else if (var13.indexOf("WTCRemoteTuxDom") >= 0) {
                  this.startAddWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var12);
               } else if (var13.indexOf("WTCImport") >= 0) {
                  this.startAddWTCImport((WTCImportMBean)var12);
               } else if (var13.indexOf("WTCExport") >= 0) {
                  this.startAddWTCExport((WTCExportMBean)var12);
               } else if (var13.indexOf("WTCPassword") >= 0) {
                  this.startAddWTCPassword((WTCPasswordMBean)var12);
               } else {
                  if (var13.indexOf("Targets") < 0) {
                     if (var3) {
                        ntrace.doTrace("*]/WTCService/prepareUpdate/80/2/unknow type = " + var13);
                     }

                     throw new BeanUpdateRejectedException("Unknown attribute type " + var13 + " for WtcSrvrMBean");
                  }

                  if (var3) {
                     ntrace.doTrace("WTCServer being targeted");
                  }
               }
            }
         }
      }

      if (var3) {
         ntrace.doTrace("old_tb_cfg = " + var4 + ", tBridgeConfig = " + this.tBridgeConfig);
      }

      if (var4 != this.tBridgeConfig) {
         if (!this.tBridgeStartup) {
            if (this.tBridgeConfig >= 3 && (this.tBridgeConfig & 1) == 1) {
               this.tBridgePending = true;
            }
         } else if (this.tBridgeConfig == 0) {
            if (var3) {
               ntrace.doTrace("tBridge removed completely, shutdown tBridge.");
            }

            tBexec.tBcancel();
            this.tBridgeStartup = false;
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/prepareUpdate/100/success");
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/activateUpdate");
      }

      for(int var4 = 0; var4 < var2.length; ++var4) {
         BeanUpdateEvent.PropertyUpdate var5 = var2[var4];
         int var6 = var5.getUpdateType();
         boolean var7 = var5.getUpdateType() == 2;
         Object var8 = var7 ? var5.getAddedObject() : var5.getRemovedObject();
         String var9 = var5.getPropertyName();
         if (var3) {
            ntrace.doTrace("i = " + var4 + ", isAdd = " + var7 + ", type" + var9);
         }

         if (var9.indexOf("WTCResources") >= 0 && myGlobalResources != null && (var6 == 1 || var6 == 2)) {
            WTCResourcesMBean var10 = this.myWtcSrvrMBean.getWTCResources();
            myGlobalResources.setMBean(var10);
            myGlobalResources.registerListener();
            myGlobalResources.activateObject();
            if (var3) {
               ntrace.doTrace("]/WTCService/activateUpdate/5/nothing to do");
            }
         } else if (var9.indexOf("WTCtBridgeGlobal") < 0 && var9.indexOf("WTCtBridgeRedirect") < 0) {
            if (var6 == 1) {
               if (var3) {
                  ntrace.doTrace("]/WTCService/activateUpdate/10/Change type not supported");
               }

               return;
            }

            if (var9.indexOf("WTCLocalTuxDom") >= 0) {
               if (var7) {
                  this.finishAddWTCLocalTuxDom((WTCLocalTuxDomMBean)var8, true);
               } else {
                  this.finishRemoveWTCLocalTuxDom((WTCLocalTuxDomMBean)var8, true);
               }
            } else if (var9.indexOf("WTCRemoteTuxDom") >= 0) {
               if (var7) {
                  this.finishAddWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var8, true);
               } else {
                  this.finishRemoveWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var8, true);
               }
            } else if (var9.indexOf("WTCImport") >= 0) {
               if (var7) {
                  this.finishAddWTCImport((WTCImportMBean)var8, true);
               } else {
                  this.finishRemoveWTCImport((WTCImportMBean)var8, true);
               }
            } else if (var9.indexOf("WTCExport") >= 0) {
               if (var7) {
                  this.finishAddWTCExport((WTCExportMBean)var8, true);
               } else {
                  this.finishRemoveWTCExport((WTCExportMBean)var8, true);
               }
            } else if (var9.indexOf("WTCPassword") >= 0) {
               if (var7) {
                  this.finishAddWTCPassword((WTCPasswordMBean)var8, true);
               } else {
                  this.finishRemoveWTCPassword((WTCPasswordMBean)var8, true);
               }
            } else if (var9.indexOf("WTCtBridgeGlobal") >= 0) {
               if (var7) {
                  if (var3) {
                     ntrace.doTrace("tBridgeGlobal being actually added");
                  }
               } else if (var3) {
                  ntrace.doTrace("tBridgeGlobal being actually removed");
               }
            } else {
               if (var9.indexOf("Targets") < 0) {
                  if (var3) {
                     ntrace.doTrace("]/WTCService/activateUpdate/20/unknow type = " + var9);
                  }

                  return;
               }

               if (var7) {
                  if (var3) {
                     ntrace.doTrace("WTCServer being targeted");
                  }
               } else if (var3) {
                  ntrace.doTrace("WTCServer being untargeted");
               }
            }
         } else if (this.tBridgePending) {
            if (tBexec.tBactivate()) {
               this.tBridgeStartup = true;
            }

            this.tBridgePending = false;
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/activateUpdate/30/success");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WTCService/rollbackUpdate");
      }

      for(int var4 = 0; var4 < var2.length; ++var4) {
         BeanUpdateEvent.PropertyUpdate var5 = var2[var4];
         int var6 = var5.getUpdateType();
         boolean var7 = var5.getUpdateType() == 2;
         Object var8 = var7 ? var5.getAddedObject() : var5.getRemovedObject();
         String var9 = var5.getPropertyName();
         if (var6 == 1) {
            if (var3) {
               ntrace.doTrace("]/WTCService/rollbackUpdate/10/Change type not supported");
            }

            return;
         }

         if (var3) {
            ntrace.doTrace("i = " + var4 + ", isAdd = " + var7 + ", type" + var9);
         }

         if (var9.indexOf("WTCLocalTuxDom") >= 0) {
            if (var7) {
               this.finishAddWTCLocalTuxDom((WTCLocalTuxDomMBean)var8, false);
            } else {
               this.finishRemoveWTCLocalTuxDom((WTCLocalTuxDomMBean)var8, false);
            }
         } else if (var9.indexOf("WTCRemoteTuxDom") >= 0) {
            if (var7) {
               this.finishAddWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var8, false);
            } else {
               this.finishRemoveWTCRemoteTuxDom((WTCRemoteTuxDomMBean)var8, false);
            }
         } else if (var9.indexOf("WTCImport") >= 0) {
            if (var7) {
               this.finishAddWTCImport((WTCImportMBean)var8, false);
            } else {
               this.finishRemoveWTCImport((WTCImportMBean)var8, false);
            }
         } else if (var9.indexOf("WTCExport") >= 0) {
            if (var7) {
               this.finishAddWTCExport((WTCExportMBean)var8, false);
            } else {
               this.finishRemoveWTCExport((WTCExportMBean)var8, false);
            }
         } else {
            if (var9.indexOf("WTCPassword") < 0) {
               if (var3) {
                  ntrace.doTrace("]/WTCService/rollbackUpdate/20/unknow type = " + var9);
               }

               return;
            }

            if (var7) {
               this.finishAddWTCPassword((WTCPasswordMBean)var8, false);
            } else {
               this.finishRemoveWTCPassword((WTCPasswordMBean)var8, false);
            }
         }
      }

      if (var3) {
         ntrace.doTrace("]/WTCService/rollbackUpdate/30/success");
      }

   }

   private void registerBeanListeners() throws ManagementException {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/registerBeanListeners/");
      }

      int var2;
      for(var2 = 0; var2 < this.ltdcnt; ++var2) {
         if (this.ltd_list[var2] != null) {
            this.ltd_list[var2].registerListener();
         }
      }

      for(var2 = 0; var2 < this.rtdcnt; ++var2) {
         if (this.rtd_list[var2] != null) {
            this.rtd_list[var2].registerListener();
         }
      }

      for(var2 = 0; var2 < this.pwdcnt; ++var2) {
         if (this.pwd_list[var2] != null) {
            this.pwd_list[var2].registerListener();
         }
      }

      Iterator var6 = this.myImportedServices.values().iterator();

      HashSet var3;
      Iterator var4;
      while(var6.hasNext()) {
         var3 = (HashSet)var6.next();
         var4 = var3.iterator();

         while(var4.hasNext()) {
            TDMImport var5 = (TDMImport)var4.next();
            var5.registerListener();
         }
      }

      var6 = this.myExportedServices.values().iterator();

      while(var6.hasNext()) {
         var3 = (HashSet)var6.next();
         var4 = var3.iterator();

         while(var4.hasNext()) {
            TDMExport var7 = (TDMExport)var4.next();
            var7.registerListener();
         }
      }

      if (myGlobalResources != null) {
         myGlobalResources.registerListener();
      }

      if (this.myWtcSrvrMBean != null && !this.registered) {
         ((AbstractDescriptorBean)this.myWtcSrvrMBean).addBeanUpdateListener(this);
         this.registered = true;
      }

      this.register();
      if (var1) {
         ntrace.doTrace("]/WTCService/registerBeanListeners/DONE");
      }

   }

   private synchronized void removeBeanListeners() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/removeBeanListeners/");
      }

      if (this.myWtcSrvrMBean != null && this.registered) {
         ((AbstractDescriptorBean)this.myWtcSrvrMBean).removeBeanUpdateListener(this);
      }

      try {
         this.unregister();
      } catch (ManagementException var6) {
      }

      int var2;
      for(var2 = 0; var2 < this.ltdcnt; ++var2) {
         if (this.ltd_list[var2] != null) {
            this.ltd_list[var2].unregisterListener();
         }
      }

      for(var2 = 0; var2 < this.rtdcnt; ++var2) {
         if (this.rtd_list[var2] != null) {
            this.rtd_list[var2].unregisterListener();
         }
      }

      for(var2 = 0; var2 < this.pwdcnt; ++var2) {
         if (this.pwd_list[var2] != null) {
            this.pwd_list[var2].unregisterListener();
         }
      }

      HashSet var3;
      Iterator var4;
      Iterator var7;
      if (this.myImportedServices != null) {
         var7 = this.myImportedServices.values().iterator();

         while(var7.hasNext()) {
            var3 = (HashSet)var7.next();
            var4 = var3.iterator();

            while(var4.hasNext()) {
               TDMImport var5 = (TDMImport)var4.next();
               var5.unregisterListener();
            }
         }
      }

      if (this.myExportedServices != null) {
         var7 = this.myExportedServices.values().iterator();

         while(var7.hasNext()) {
            var3 = (HashSet)var7.next();
            var4 = var3.iterator();

            while(var4.hasNext()) {
               TDMExport var8 = (TDMExport)var4.next();
               var8.unregisterListener();
            }
         }
      }

      if (myGlobalResources != null) {
         myGlobalResources.unregisterListener();
      }

      if (var1) {
         ntrace.doTrace("]/WTCService/removeBeanListeners/DONE");
      }

   }

   public void processTSessionKAEvents(long var1) {
      for(int var3 = 0; var3 < this.rtdcnt; ++var3) {
         gwdsession var4;
         if (this.rtd_list[var3] != null && (var4 = this.rtd_list[var3].getDomainSession()) != null && var4.isKeepAliveAvailable() && var4.isKATimersExpired(var1)) {
            this.rtd_list[var3].onTerm(3);
         }
      }

   }
}
