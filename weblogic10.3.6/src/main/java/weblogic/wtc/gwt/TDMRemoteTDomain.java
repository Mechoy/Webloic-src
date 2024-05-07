package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCLicenseManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Timer;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCRemoteTuxDomMBean;
import weblogic.security.utils.MBeanKeyStoreConfiguration;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.OnTerm;
import weblogic.wtc.jatmi.PasswordUtils;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPINIT;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.gwatmi;

public class TDMRemoteTDomain extends TDMRemote implements OnTerm, BeanUpdateListener {
   static final long serialVersionUID = -6844147720547859282L;
   private String[] myNWAddr;
   private String[] ipaddress;
   private InetAddress[] myInetAddr;
   private int[] port;
   private boolean need_ia = true;
   private String federationURL = null;
   private String federationName = null;
   private int myCmpLimit;
   private int MinEncryptionBits;
   private int MaxEncryptionBits;
   private gwdsession myDsession;
   private Timer myTimeService;
   private String myAppKeyType;
   private boolean myAllowAnonymous;
   private int myDfltAppKey;
   private String myUidKw = null;
   private String myGidKw = null;
   private String myAppKeyClass = null;
   private String myParam = null;
   private WTCRemoteTuxDomMBean mBean = null;
   private boolean registered = false;
   private int keepAlive = -2;
   private int keepAliveWait = -1;
   private final TimedMutex lock = new TimedMutex();
   private boolean timedOut;
   private boolean[] useSDP;
   ScheduledReconnect connectingTask = null;

   public TDMRemoteTDomain(String var1, TuxXidRply var2, Timer var3) throws Exception {
      super(var1, var2);
      super.setType("TDOMAIN");
      this.setAclPolicy("LOCAL");
      this.setCredentialPolicy("LOCAL");
      this.setCmpLimit(Integer.MAX_VALUE);
      this.setMinEncryptBits(0);
      this.setMaxEncryptBits(128);
      this.myTimeService = var3;
      this.myDfltAppKey = -1;
      this.myAllowAnonymous = false;
   }

   public String getNWAddr() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myNWAddr[0];
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public String getNWAddr(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         this.r.lock();

         String var2;
         try {
            if (var1 < this.myNWAddr.length) {
               var2 = this.myNWAddr[var1];
               return var2;
            }

            var2 = null;
         } finally {
            this.r.unlock();
         }

         return var2;
      }
   }

   public void setNWAddr(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemoteTDomain/setNWAddr/addr=null");
            ntrace.doTrace("*]/TDMRemoteTDomain/setNWAddr/10/TPEINVAL");
         }

         throw new TPException(4, "null NWAddr found in remote domain " + this.getAccessPointId());
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemoteTDomain/setNWAddr/addr=" + var1);
         }

         this.parseNWAddr(var1, false);
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setNWAddr/40/SUCCESS");
         }

      }
   }

   public static boolean isNWAddrFormat(String var0) {
      if (var0 != null && (var0.startsWith("//") || var0.toLowerCase().startsWith("sdp://"))) {
         if (var0.toLowerCase().startsWith("sdp://")) {
            var0 = var0.substring(4);
         }

         int var1 = var0.indexOf(58);
         if (var1 >= 3 && var1 + 1 < var0.length()) {
            String var2 = var0.substring(2, var1);
            if (Character.isDigit(var2.charAt(0))) {
               int var3 = 0;
               boolean var4 = false;
               String var5 = null;
               StringTokenizer var6 = new StringTokenizer(var2, ".");

               while(true) {
                  if (!var6.hasMoreTokens()) {
                     if (var3 != 4) {
                        return false;
                     }
                     break;
                  }

                  var5 = var6.nextToken();

                  try {
                     int var10 = Integer.parseInt(var5, 10);
                     if (var10 < 0 || var10 > 255) {
                        return false;
                     }
                  } catch (NumberFormatException var9) {
                     return false;
                  }

                  ++var3;
               }
            }

            try {
               return Integer.parseInt(var0.substring(var1 + 1), 10) >= 0;
            } catch (NumberFormatException var8) {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void parseNWAddr(String var1, boolean var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var3) {
            ntrace.doTrace("[/TDMRemoteTDomain/parseNWAddr/addr=null");
            ntrace.doTrace("*]/TDMRemoteTDomain/parseNWAddr/10/TPEINVAL");
         }

         throw new TPException(4, "null NWAddr found in remote domain " + this.getAccessPointId());
      } else {
         if (var3) {
            ntrace.doTrace("[/TDMRemoteTDomain/parseNWAddr/addr=" + var1 + ", validate = " + var2);
         }

         StringTokenizer var6 = new StringTokenizer(var1, ",");
         int var7 = var6.countTokens();
         String[] var8 = new String[var7];
         String[] var9 = new String[var7];
         int[] var10 = new int[var7];
         boolean[] var12 = new boolean[var7];

         for(int var11 = 0; var11 < var7; ++var11) {
            var8[var11] = var6.nextToken();
            if (!isNWAddrFormat(var8[var11])) {
               if (var3) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/parseNWAddr/20/TPEINVAL");
               }

               throw new TPException(4, "Unsupported NWAddr format \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
            }

            var12[var11] = false;
            if (var8[var11].toLowerCase().startsWith("sdp://")) {
               var8[var11] = var8[var11].substring(4);
               var12[var11] = true;
            }

            if (!var2) {
               int var4 = var8[var11].indexOf(58);
               var9[var11] = var8[var11].substring(2, var4);
               String var5 = var8[var11].substring(var4 + 1);
               var10[var11] = Integer.parseInt(var5, 10);
            }
         }

         if (!var2) {
            this.w.lock();
            this.myNWAddr = var8;
            this.ipaddress = var9;
            this.port = var10;
            this.myInetAddr = null;
            this.need_ia = true;
            this.useSDP = var12;
            this.w.unlock();
         }

         if (var3) {
            ntrace.doTrace("]/TDMRemoteTDomain/parseNWAddr/40/SUCCESS");
         }

      }
   }

   public String getFederationURL() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getFederationURL/");
      }

      this.r.lock();

      String var2;
      try {
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/getFederationURL/10/" + this.federationURL);
         }

         var2 = this.federationURL;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public boolean setFederationURL(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setFederationURL/url=" + var1);
      }

      this.w.lock();
      if (var1 != null && var1.length() != 0) {
         this.federationURL = var1;
      } else {
         this.federationURL = null;
         if (var2) {
            ntrace.doTrace("set to null");
         }
      }

      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setFederationURL/10");
      }

      return true;
   }

   public String getFederationName() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getFederationName/");
      }

      this.r.lock();

      String var2;
      try {
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/getFederationName/10/" + this.federationName);
         }

         var2 = this.federationName;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public boolean setFederationName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setFederationName/name=" + var1);
      }

      this.w.lock();
      if (var1 != null && var1.length() != 0) {
         this.federationName = var1;
      } else {
         this.federationName = null;
         if (var2) {
            ntrace.doTrace("set to null");
         }
      }

      this.w.unlock();
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setFederationName/10");
      }

      return true;
   }

   public gwdsession getDomainSession() {
      return this.myDsession;
   }

   public synchronized boolean getTimedOut() {
      return this.timedOut;
   }

   public boolean[] get_useSDP() {
      this.r.lock();

      boolean[] var1;
      try {
         var1 = this.useSDP;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public gwatmi getTsession(boolean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/getTsession/" + this.getAccessPoint() + ", create = " + var1);
      }

      TDMLocalTDomain var10 = (TDMLocalTDomain)this.getLocalAccessPointObject();
      var10.getBlockTime();
      synchronized(this) {
         this.timedOut = false;
         if (var1 || this.myDsession != null && this.myDsession.get_is_connected()) {
            if (var1) {
               if (!this.lock.acquire(var10.getBlockTime())) {
                  if (var2) {
                     ntrace.doTrace("]/TDMRemoteTDomain/getTsession/03/Timeout");
                  }

                  this.timedOut = true;
                  return null;
               }
            } else {
               this.lock.acquire(0L);
            }
         } else if (!this.lock.attempt()) {
            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/getTsession/05/null");
            }

            return null;
         }
      }

      gwdsession var11 = null;

      try {
         if (this.myDsession != null && this.myDsession.getIsTerminated()) {
            this.myDsession = null;
         }

         if (this.myDsession != null) {
            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/getTsession/10/" + this.myDsession);
            }

            var11 = this.myDsession;
            throw new DoneException();
         }

         if (!var1) {
            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/getTsession/20/null");
            }

            var11 = null;
            throw new DoneException();
         }

         if (this.getConnectionPolicy().equals("INCOMING_ONLY")) {
            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/getTsession/30/null");
            }

            var11 = null;
            throw new DoneException();
         }

         int var9;
         if (this.myNWAddr != null && (var9 = this.myNWAddr.length) != 0) {
            if (this.myInetAddr == null) {
               this.myInetAddr = new InetAddress[var9];
               this.need_ia = true;
            }

            if (this.need_ia) {
               this.need_ia = false;
               int var8 = 0;
               int var7 = 0;

               while(true) {
                  if (var7 >= var9) {
                     if (var8 == var9) {
                        WTCLogger.logWarnNoValidHostAddress(this.getAccessPointId());
                        if (var2) {
                           ntrace.doTrace("]/TDMRemoteTDomain/getTsession/45/null");
                        }

                        var11 = null;
                        throw new DoneException();
                     }
                     break;
                  }

                  if (this.myInetAddr[var7] == null) {
                     try {
                        this.myInetAddr[var7] = InetAddress.getByName(this.ipaddress[var7]);
                     } catch (UnknownHostException var31) {
                        this.myInetAddr[var7] = null;
                        this.need_ia = true;
                        if (var2) {
                           ntrace.doTrace("unknown host(" + this.ipaddress[var7] + ") skip it");
                        }

                        ++var8;
                     }
                  }

                  ++var7;
               }
            }

            OatmialServices var5 = WTCService.getOatmialServices();

            try {
               var10 = (TDMLocalTDomain)this.getLocalAccessPointObject();
               atntdom80 var12 = new atntdom80(var10.getConnPrincipalName());
               WLSInvoke var4 = new WLSInvoke(var10, this);
               this.myDsession = new gwdsession(var5.getTimeService(), this.myInetAddr, this.port, var12, var4, WTCService.getUniqueGwdsessionId(), this.getUnknownXidRplyObj());
               this.myDsession.set_BlockTime(var10.getBlockTime());
               this.myDsession.setTerminationHandler(this);
               this.myDsession.set_compression_threshold(var10.getCmpLimit());
               String var13 = var10.getAccessPoint();
               String var14 = this.getAccessPoint();
               String var15 = var10.getSecurity();
               this.myDsession.set_sess_sec(var15);
               this.myDsession.setDesiredName(var10.getConnPrincipalName());
               this.myDsession.set_dom_target_name(this.getConnPrincipalName());
               this.myDsession.set_local_domain_name(var13);
               this.myDsession.setRemoteDomainId(this.getAccessPointId());
               this.myDsession.setInteroperate(var10.isInteroperate());
               this.myDsession.setKeepAlive(this.getKeepAlive());
               this.myDsession.setKeepAliveWait(this.getKeepAliveWait());
               this.myDsession.setAclPolicy(this.getAclPolicy());
               this.myDsession.setCredentialPolicy(this.getCredentialPolicy());
               this.myDsession.setAppKey(this.myAppKeyType);
               if (this.myAppKeyType != null && !this.myAppKeyType.equals("TpUsrFile")) {
                  if (this.myAppKeyType.equals("LDAP")) {
                     this.myDsession.setUidKw(this.myUidKw);
                     this.myDsession.setGidKw(this.myGidKw);
                  } else {
                     this.myDsession.setCustomAppKeyClass(this.myAppKeyClass);
                     this.myDsession.setCustomAppKeyClassParam(this.myParam);
                  }
               } else {
                  this.myDsession.setTpUserFile(this.getTpUsrFile());
               }

               this.myDsession.setAllowAnonymous(this.myAllowAnonymous);
               this.myDsession.setDfltAppKey(this.myDfltAppKey);
               String var17;
               String var18;
               String var19;
               String var20;
               if (var15.equals("DM_PW")) {
                  TDMPasswd var35 = WTCService.getWTCService().getTDMPasswd(var13, var14);
                  var17 = WTCService.getPasswordKey();
                  var18 = WTCService.getEncryptionType();
                  var19 = PasswordUtils.decryptPassword(var17, var35.getLocalPasswordIV(), var35.getLocalPassword(), var18);
                  var20 = PasswordUtils.decryptPassword(var17, var35.getRemoteIV(), var35.getRemotePassword(), var18);
                  if (var19 == null || var20 == null) {
                     this.myDsession = null;
                     WTCLogger.logErrorTDomainPassword(var13, var14);
                     if (var2) {
                        ntrace.doTrace("]/TDMRemoteTDomain/getTsession/60/null");
                     }

                     var11 = null;
                     throw new DoneException();
                  }

                  this.myDsession.setLocalPassword(var19);
                  this.myDsession.setRemotePassword(var20);
               } else if (var15.equals("APP_PW")) {
                  String var16 = WTCService.getAppPasswordPWD();
                  var17 = WTCService.getAppPasswordIV();
                  var18 = WTCService.getPasswordKey();
                  var19 = WTCService.getEncryptionType();
                  var20 = PasswordUtils.decryptPassword(var18, var17, var16, var19);
                  if (var20 == null) {
                     this.myDsession = null;
                     if (var2) {
                        ntrace.doTrace("]/TDMRemoteTDomain/getTsession/65/null");
                     }

                     var11 = null;
                     throw new DoneException();
                  }

                  this.myDsession.setApplicationPassword(var20);
               }

               int var36 = 1;
               if (!var10.getMBean().getUseSSL().equals("TwoWay") && !var10.getMBean().getUseSSL().equals("OneWay")) {
                  var36 = TCLicenseManager.decideEncryptionLevel(16, this.MinEncryptionBits, this.MaxEncryptionBits);
               } else {
                  this.myDsession.setUseSSL(true);
                  if (var10.getMBean().getKeyStoresLocation().equals("WLS Stores")) {
                     MBeanKeyStoreConfiguration var37 = MBeanKeyStoreConfiguration.getInstance();
                     var18 = var37.getKeyStores();
                     var19 = null;
                     var20 = null;
                     Object var21 = null;
                     this.myDsession.setIdentityKeyStoreType(var37.getCustomIdentityKeyStoreType());
                     this.myDsession.setIdentityKeyStore(var37.getCustomIdentityKeyStoreFileName());
                     this.myDsession.setIdentityKeyStorePassphrase(var37.getCustomIdentityKeyStorePassPhrase());
                     this.myDsession.setIdentityKeyAlias(var37.getCustomIdentityAlias());
                     this.myDsession.setIdentityKeyPassphrase(var37.getCustomIdentityPrivateKeyPassPhrase());
                     if ("CustomIdentityAndCustomTrust".equals(var18)) {
                        this.myDsession.setTrustKeyStoreType(var37.getCustomTrustKeyStoreType());
                        this.myDsession.setTrustKeyStore(var37.getCustomTrustKeyStoreFileName());
                        this.myDsession.setTrustKeyStorePassphrase(var37.getCustomTrustKeyStorePassPhrase());
                     } else {
                        this.myDsession.setTrustKeyStoreType((String)null);
                        this.myDsession.setTrustKeyStore((String)null);
                        this.myDsession.setTrustKeyStorePassphrase((String)null);
                     }
                  } else {
                     this.myDsession.setIdentityKeyStoreType("jks");
                     this.myDsession.setIdentityKeyStore(var10.getMBean().getIdentityKeyStoreFileName());
                     this.myDsession.setIdentityKeyStorePassphrase(var10.getMBean().getIdentityKeyStorePassPhrase());
                     this.myDsession.setIdentityKeyAlias(var10.getMBean().getPrivateKeyAlias());
                     this.myDsession.setIdentityKeyPassphrase(var10.getMBean().getPrivateKeyPassPhrase());
                     this.myDsession.setTrustKeyStoreType("jks");
                     this.myDsession.setTrustKeyStore(var10.getMBean().getTrustKeyStoreFileName());
                     this.myDsession.setTrustKeyStorePassphrase(var10.getMBean().getTrustKeyStorePassPhrase());
                  }

                  this.myDsession.setMinEncryptBits(var10.getMinEncryptBits());
                  this.myDsession.setMaxEncryptBits(var10.getMaxEncryptBits());
               }

               this.myDsession.setEncryptionFlags(var36);
               this.myDsession.setUseSDP(this.useSDP);
               TPINIT var3 = new TPINIT();
               var3.usrname = var10.getAccessPointId();
               this.myDsession.tpinit(var3);
            } catch (TPException var29) {
               if (this.myDsession != null) {
                  this.myDsession._dom_drop();
                  this.myDsession = null;
               }

               if (var2) {
                  ntrace.doTrace("]/TDMRemoteTDomain/getTsession/70/null/" + var29);
               }

               if (var29.gettperrno() == 13) {
                  this.timedOut = true;
               }

               var11 = null;
               throw new DoneException();
            } catch (Exception var30) {
               if (this.myDsession != null) {
                  this.myDsession._dom_drop();
                  this.myDsession = null;
               }

               if (var2) {
                  ntrace.doTrace("]/TDMRemoteTDomain/getTsession/80/null/" + var30);
               }

               var11 = null;
               throw new DoneException();
            }

            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/getTsession/90/" + this.myDsession);
            }

            var11 = this.myDsession;
            throw new DoneException();
         }

         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/getTsession/40/null");
         }

         var11 = null;
         throw new DoneException();
      } catch (DoneException var32) {
         if (var2) {
            ntrace.doTrace("/TDMRemoteTDomain/getTsession/100/" + var11);
         }
      } finally {
         this.lock.release();
      }

      return var11;
   }

   public void setTsession(gwatmi var1) {
      this.myDsession = (gwdsession)var1;
   }

   public int getCmpLimit() {
      this.r.lock();

      int var1;
      try {
         var1 = this.myCmpLimit;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setCmpLimit(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setCmpLimit/limit=" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.myCmpLimit = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setCmpLimit/20/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setCmpLimit/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid compression limit value \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
      }
   }

   public synchronized int getMinEncryptBits() {
      this.r.lock();

      int var1;
      try {
         var1 = this.MinEncryptionBits;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMinEncryptBits(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setMinEncryptBits/numbits=" + var1);
      }

      if (var1 != 0 && var1 != 56 && var1 != 40 && var1 != 128) {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setMinEncryptBits/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid MinEncryptionBits " + this.MinEncryptionBits + " found in remote domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.MinEncryptionBits = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setMinEncryptBits/10/SUCCESS");
         }

      }
   }

   public int getMaxEncryptBits() {
      this.r.lock();

      int var1;
      try {
         var1 = this.MaxEncryptionBits;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMaxEncryptBits(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setMaxEncryptBits/numbits=" + var1);
      }

      if (var1 != 0 && var1 != 56 && var1 != 40 && var1 != 128) {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setMaxEncryptBits/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid MaxEncryptionBits \"" + this.MaxEncryptionBits + "\" found in remote domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.MaxEncryptionBits = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setMaxEncryptBits/10/SUCCESS");
         }

      }
   }

   public void setType(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setType/type=" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setType/type=null");
         }
      }

      if (var1 != null && var1.equals("TDOMAIN")) {
         super.setType(var1);
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setType/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setType/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid Domain Type \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
      }
   }

   public void setAppKey(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setAppKey/type=" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setAppKey/type=null");
         }
      }

      if (var1 != null && !var1.equals("TpUsrFile") && !var1.equals("LDAP") && !var1.equals("Custom")) {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setAppKey/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid AppKey generator plug-in Type \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.myAppKeyType = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setAppKey/10/SUCCESS");
         }

      }
   }

   public String getAppKey() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getAppKey/");
      }

      this.r.lock();

      String var2;
      try {
         if (var1) {
            if (this.myAppKeyType != null) {
               ntrace.doTrace("]/TDMRemoteTDomain/getAppKey/10/" + this.myAppKeyType);
            } else {
               ntrace.doTrace("]/TDMRemoteTDomain/getAppKey/10/null");
            }
         }

         var2 = this.myAppKeyType;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setAllowAnonymous(boolean var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setAllowAnonymous/" + var1);
      }

      this.w.lock();
      this.myAllowAnonymous = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setAllowAnonymous/10/SUCCESS");
      }

   }

   public boolean getAllowAnonymous() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getAllowAnonymous/");
      }

      this.r.lock();

      boolean var2;
      try {
         var2 = this.myAllowAnonymous;
      } finally {
         this.r.unlock();
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/getAllowAnonymous/10/" + this.myAllowAnonymous);
         }

      }

      return var2;
   }

   public boolean isAllowAnonymous() {
      return this.getAllowAnonymous();
   }

   public void setDefaultAppKey(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setDefaultAppKey/" + var1);
      }

      this.w.lock();
      this.myDfltAppKey = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setDefaultAppKey/10/");
      }

   }

   public int getDefaultAppKey() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getDefaultAppKey/");
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/getDefaultAppKey/10/" + this.myDfltAppKey);
      }

      this.r.lock();

      int var2;
      try {
         var2 = this.myDfltAppKey;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setTuxedoUidKw(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setTuxedoUidKw/" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setTuxedoUidKw/null");
         }
      }

      this.w.lock();
      this.myUidKw = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setTuxedoUidKw/10/");
      }

   }

   public String getTuxedoUidKw() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getTuxedoUidKw/");
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/setTuxedoUidKw/10/" + this.myUidKw);
      }

      this.r.lock();

      String var2;
      try {
         var2 = this.myUidKw;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setTuxedoGidKw(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setTuxedoGidKw/" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setTuxedoGidKw/null");
         }
      }

      this.w.lock();
      this.myGidKw = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setTuxedoGidKw/10/");
      }

   }

   public String getTuxedoGidKw() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getTuxedoGidKw/");
         ntrace.doTrace("]/TDMRemoteTDomain/getTuxedoGidKw/10/" + this.myGidKw);
      }

      this.r.lock();

      String var2;
      try {
         var2 = this.myGidKw;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setCustomAppKeyClass(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setCustomAppKey/" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setCustomAppKey/null");
         }
      }

      this.w.lock();
      this.myAppKeyClass = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setCustomAppKey/10/");
      }

   }

   public String getCustomAppKeyClass() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getCustomAppKeyClass/");
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/getCustomAppKeyClass/10/" + this.myAppKeyClass);
      }

      this.r.lock();

      String var2;
      try {
         var2 = this.myAppKeyClass;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setCustomAppKeyClassParam(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemoteTDomain/setCustomAppKeyClassParam/" + var1);
         } else {
            ntrace.doTrace("[/TDMRemoteTDomain/setCustomAppKeyClassParam/null");
         }
      }

      this.w.lock();
      this.myParam = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemoteTDomain/setCustomAppKeyClassParam/10/");
      }

   }

   void setKeepAlive(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setKeepAlive/ka =" + var1);
      }

      if (var1 >= -1 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.keepAlive = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setKeepAlive/20");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setKeepAlive/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid KeepAlive value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      }
   }

   public int getKeepAlive() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getKeepAlive");
      }

      this.r.lock();

      try {
         int var2 = this.keepAlive;
         if (var2 == -2) {
            var2 = 0;
         } else if (var2 == -1) {
            TDMLocalTDomain var8 = (TDMLocalTDomain)this.getLocalAccessPointObject();
            int var4 = var8.getKeepAlive();
            return var4;
         }

         int var3 = var2;
         return var3;
      } finally {
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/getKeepAlive/10");
         }

         this.r.unlock();
      }
   }

   void setKeepAliveWait(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/setKeepAliveWait/kaw =" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.keepAliveWait = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setKeepAliveWait/20");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMRemoteTDomain/setKeepAliveWait/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid KeepAliveWait value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      }
   }

   public int getKeepAliveWait() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getKeepAliveWait");
      }

      this.r.lock();

      int var7;
      try {
         int var2;
         if (this.keepAlive == -1) {
            TDMLocalTDomain var3 = (TDMLocalTDomain)this.getLocalAccessPointObject();
            var2 = var3.getKeepAliveWait();
         } else if (this.keepAlive != -2 && this.keepAliveWait != -1) {
            var2 = this.keepAliveWait;
         } else {
            var2 = 0;
         }

         var7 = var2;
      } finally {
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/getKeepAliveWait/10");
         }

         this.r.unlock();
      }

      return var7;
   }

   public void setMBean(WTCRemoteTuxDomMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemoteTDomain/setMBname/MBeanName=" + var1.getName());
         }

         if (this.mBean != null) {
            if (this.mBean == var1) {
               if (var2) {
                  ntrace.doTrace("]/TDMRemoteTDomain/setMBname/no change");
               }

               return;
            }

            this.unregisterListener();
         }

         this.mBean = var1;
         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setMBname/20/change & activated");
         }
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemoteTDomain/setMBname/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }

         if (var2) {
            ntrace.doTrace("]/TDMRemoteTDomain/setMBname/30/remove & deactivated");
         }
      }

   }

   public String getCustomAppKeyClassParam() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/getCustomAppKeyClassParam/");
         ntrace.doTrace("]/TDMRemoteTDomain/getCustomAppKeyClassParam/" + this.myParam);
      }

      this.r.lock();

      String var2;
      try {
         var2 = this.myParam;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public WTCRemoteTuxDomMBean getMBean() {
      return this.mBean;
   }

   public void onTerm(int var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/onTerm/" + var1);
      }

      this.myDsession = null;
      switch (var1) {
         case 0:
         case 2:
         default:
            if (var2) {
               ntrace.doTrace("]/TDMRemoteTDomain/onTerm/10");
            }

            return;
         case 1:
         case 3:
            String var3;
            if ((var3 = this.getConnectionPolicy()) != null && var3.equals("ON_STARTUP")) {
               if (var1 == 1) {
                  this.outboundConnect(false);
               } else {
                  this.outboundConnect(true);
               }

               if (var2) {
                  ntrace.doTrace("]/TDMRemoteTDomain/onTerm/30");
               }

            } else {
               if (var2) {
                  ntrace.doTrace("]/TDMRemoteTDomain/onTerm/20");
               }

            }
      }
   }

   private void outboundConnect(boolean var1) {
      boolean var6 = ntrace.isTraceEnabled(2);
      if (var6) {
         ntrace.doTrace("[/TDMRemoteTDomain/outboundConnect/" + this.getAccessPointId());
      }

      if (this.getTsession(false) != null) {
         if (var6) {
            ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/10");
         }

      } else if (this.connectingTask != null) {
         if (var6) {
            ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/20");
         }

      } else {
         long var2;
         String var7;
         if ((var2 = this.getMaxRetries()) < 0L) {
            var7 = null;
            if ((var7 = this.getConnectionPolicy()) != null && !var7.equals("ON_STARTUP")) {
               this.connectingTask = new ScheduledReconnect(this, 0L);

               try {
                  this.myTimeService.schedule(this.connectingTask, 0L);
               } catch (IllegalArgumentException var9) {
                  WTCLogger.logTTEstdSchedule(var9.getMessage());
                  if (var6) {
                     ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/30");
                  }

                  return;
               }
            }

            if (var6) {
               ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/40");
            }

         } else {
            long var4 = this.getRetryInterval() * 1000L;
            var7 = this.getConnectionPolicy();
            if (var7 != null && var7.equals("ON_DEMAND")) {
               this.getTsession(true);
               if (var6) {
                  ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/45");
               }

            } else {
               this.connectingTask = new ScheduledReconnect(this, var2);

               try {
                  if (var1) {
                     this.myTimeService.scheduleAtFixedRate(this.connectingTask, var4, var4);
                  } else {
                     this.myTimeService.scheduleAtFixedRate(this.connectingTask, 0L, var4);
                  }
               } catch (IllegalArgumentException var10) {
                  WTCLogger.logTTEstdSchedule(var10.getMessage());
                  if (var6) {
                     ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/50");
                  }

                  return;
               } catch (IllegalStateException var11) {
                  WTCLogger.logTTEstdSchedule(var11.getMessage());
                  if (var6) {
                     ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/60");
                  }

                  return;
               }

               if (var6) {
                  ntrace.doTrace("]/TDMRemoteTDomain/outboundConnect/70");
               }

            }
         }
      }
   }

   public void startConnection() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/startConnection/");
      }

      String var2;
      if ((var2 = this.getConnectionPolicy()) != null && !var2.equals("INCOMING_ONLY")) {
         this.outboundConnect(false);
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/startConnection/DONE");
         }

      } else {
         Loggable var3 = WTCLogger.logErrorIncomingOnlyLoggable(this.getAccessPointId());
         var3.log();
         if (var1) {
            ntrace.doTrace("*]/TDMRemoteTDomain/startConnection/10/INCOMING_ONLY");
         }

         throw new TPException(12, var3.getMessage());
      }
   }

   public void checkConfigIntegrity() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/checkConfigIntegrity/rdom=" + this.getAccessPointId());
      }

      this.r.lock();
      if (this.MinEncryptionBits > this.MaxEncryptionBits) {
         this.r.unlock();
         Loggable var2 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Remote", this.getAccessPointId());
         var2.log();
         throw new TPException(4, var2.getMessage());
      } else {
         this.r.unlock();
         if (var1) {
            ntrace.doTrace("]/TDMRemoteTDomain/checkConfigIntegrity/20/true");
         }

      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCRemoteTuxDomMBean var3 = (WTCRemoteTuxDomMBean)var1.getProposedBean();
      String var4 = null;
      String var5 = null;
      String var6 = null;
      int var7 = this.myCmpLimit;
      int var8 = this.MinEncryptionBits;
      int var9 = this.MaxEncryptionBits;
      String var10 = null;
      boolean var11 = this.myAllowAnonymous;
      int var12 = this.myDfltAppKey;
      String var13 = null;
      String var14 = null;
      String var15 = null;
      String var16 = null;
      String var17 = null;
      String var18 = null;
      String var19 = null;
      String var20 = null;
      String var21 = null;
      Object var22 = null;
      String var23 = null;
      String var24 = null;
      long var25 = -1L;
      long var27 = -1L;
      int var29 = -1;
      int var30 = -1;
      boolean var32 = ntrace.isTraceEnabled(16);
      if (var32) {
         ntrace.doTrace("[/TDMRemoteTDomain/prepareUpdate");
      }

      if (var3 == null) {
         if (var32) {
            ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMRemoteTuxDom!");
      } else {
         String var34;
         for(int var33 = 0; var33 < var2.length; ++var33) {
            var34 = var2[var33].getPropertyName();
            int var35 = var2[var33].getUpdateType();
            if (var32) {
               ntrace.doTrace("i = " + var33 + ", optype = " + var35 + ", key = " + var34);
            }

            if (var35 == 1) {
               if (var34.equals("AccessPointId")) {
                  var19 = var3.getAccessPointId();
                  if (var32) {
                     ntrace.doTrace("AccessPointId: " + var19);
                  }
               } else if (var34.equals("AccessPoint")) {
                  var18 = var3.getAccessPoint();
                  if (var32) {
                     ntrace.doTrace("AccessPoint: " + var18);
                  }
               } else if (var34.equals("NWAddr")) {
                  var4 = var3.getNWAddr();
                  if (var32) {
                     ntrace.doTrace("NWAddr: " + var4);
                  }
               } else if (var34.equals("LocalAccessPoint")) {
                  var17 = var3.getLocalAccessPoint();
                  if (var32) {
                     ntrace.doTrace("LocalAccessPoint: " + var17);
                  }
               } else if (var34.equals("ConnectionPolicy")) {
                  var24 = var3.getConnectionPolicy();
                  if (var32) {
                     ntrace.doTrace("ConnectionPolicy: " + var24);
                  }
               } else if (var34.equals("AclPolicy")) {
                  var20 = var3.getAclPolicy();
                  if (var32) {
                     ntrace.doTrace("AclPolicy: " + var20);
                  }
               } else if (var34.equals("CredentialPolicy")) {
                  var21 = var3.getCredentialPolicy();
                  if (var32) {
                     ntrace.doTrace("CredentialPolicy: " + var21);
                  }
               } else if (var34.equals("TpUsrFile")) {
                  var23 = var3.getTpUsrFile();
                  if (var32) {
                     ntrace.doTrace("TpUsrFile: " + var23);
                  }
               } else if (var34.equals("AppKey")) {
                  var10 = var3.getAppKey();
                  if (var32) {
                     ntrace.doTrace("AppKey: " + var10);
                  }
               } else if (var34.equals("DefaultAppKey")) {
                  var12 = var3.getDefaultAppKey();
                  if (var32) {
                     ntrace.doTrace("DefaultAppKey: " + var12);
                  }
               } else if (var34.equals("AllowAnonymous")) {
                  var11 = var3.getAllowAnonymous();
                  if (var32) {
                     ntrace.doTrace("AllowAnonymous: " + var11);
                  }
               } else if (var34.equals("TuxedoUidKw")) {
                  var13 = var3.getTuxedoUidKw();
                  if (var32) {
                     ntrace.doTrace("TuxedoUidKw: " + var13);
                  }
               } else if (var34.equals("TuxedoGidKw")) {
                  var14 = var3.getTuxedoGidKw();
                  if (var32) {
                     ntrace.doTrace("TuxedoGidKw: " + var14);
                  }
               } else if (var34.equals("CustomAppKeyClass")) {
                  var15 = var3.getCustomAppKeyClass();
                  if (var32) {
                     ntrace.doTrace("CustomAppKeyClass: " + var15);
                  }
               } else if (var34.equals("CustomAppKeyClassParam")) {
                  var16 = var3.getCustomAppKeyClassParam();
                  if (var32) {
                     ntrace.doTrace("CustomAppKeyClassParam: " + var16);
                  }
               } else if (var34.equals("FederationURL")) {
                  var5 = var3.getFederationURL();
                  if (var32) {
                     ntrace.doTrace("FederationURL: " + var5);
                  }
               } else if (var34.equals("FederationName")) {
                  var6 = var3.getFederationName();
                  if (var32) {
                     ntrace.doTrace("FederationName: " + var6);
                  }
               } else if (var34.equals("RetryInterval")) {
                  var27 = var3.getRetryInterval();
                  if (var32) {
                     ntrace.doTrace("ConnectionPolicy: " + var24);
                  }
               } else if (var34.equals("MaxRetries")) {
                  var25 = var3.getMaxRetries();
                  if (var32) {
                     ntrace.doTrace("MaxRetries: " + var25);
                  }
               } else if (var34.equals("CmpLimit")) {
                  var7 = var3.getCmpLimit();
                  if (var32) {
                     ntrace.doTrace("CmpLimit: " + var7);
                  }
               } else {
                  String var31;
                  if (var34.equals("MinEncryptBits")) {
                     var31 = var3.getMinEncryptBits();
                     var8 = Integer.parseInt(var31, 10);
                     if (var32) {
                        ntrace.doTrace("MinEncryptBits: " + var8);
                     }
                  } else if (var34.equals("MaxEncryptBits")) {
                     var31 = var3.getMaxEncryptBits();
                     var9 = Integer.parseInt(var31, 10);
                     if (var32) {
                        ntrace.doTrace("MaxEncryptBits: " + var9);
                     }
                  } else if (var34.equals("KeepAlive")) {
                     var29 = var3.getKeepAlive();
                     if (var32) {
                        ntrace.doTrace("KeepAlive: " + var29);
                     }
                  } else if (var34.equals("KeepAliveWait")) {
                     var30 = var3.getKeepAliveWait();
                     if (var32) {
                        ntrace.doTrace("KeepAliveWait: " + var30);
                     }
                  }
               }
            } else if (var35 == 2) {
               if (var32) {
                  ntrace.doTrace("Unexpected ADD operation, ignored!");
               }
            } else {
               if (var35 != 3) {
                  if (var32) {
                     ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/20/Unknown operation " + var35);
                  }

                  throw new BeanUpdateRejectedException("Unknown operation(" + var35 + ") for TDMResources.");
               }

               if (var32) {
                  ntrace.doTrace("Unexpected REMOVE operation, ignored!");
               }
            }
         }

         WTCService var38 = WTCService.getWTCService();
         if (var18 != null && !var18.equals(this.getLocalAccessPoint()) && var38.getLocalDomain(var18) == null) {
            if (var32) {
               ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/30/LAP (" + var17 + ") not defined!");
            }

            throw new BeanUpdateRejectedException("Local Access Point " + var17 + " is not configured.");
         } else {
            var34 = var17 == null ? this.getLocalAccessPoint() : var17;
            if (var17 != null || var18 != null) {
               TDMRemoteTDomain var39 = var38.getVTDomainSession(var34, var18);
               if (var39 != null && var39 != this) {
                  if (var32) {
                     ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/40/duplicate TDomain session(" + var17 + ", " + var18 + ").");
                  }

                  throw new BeanUpdateRejectedException("Duplicate TDomain Session(" + var17 + ", " + var18 + ").");
               }
            }

            if (var8 != this.MinEncryptionBits && var8 != 0 && var8 != 40 && var8 != 56 && var8 != 128) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/50/invalid MinEncryptBits value(" + var8 + ").");
               }

               throw new BeanUpdateRejectedException("Invalid MinEncryptBits Value: " + var8);
            } else if (var9 != this.MaxEncryptionBits && var9 != 0 && var9 != 40 && var9 != 56 && var9 != 128) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/60/invalid MinEncryptBits value(" + var8 + ").");
               }

               throw new BeanUpdateRejectedException("Invalid MaxEncryptBits Value: " + var9);
            } else if (var9 < var8) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/70/MinEncryptBits value(" + var8 + ") greater than MaxEncryptBits value(" + var9 + ").");
               }

               throw new BeanUpdateRejectedException("MinEncryptBits value greater than MaxEncryptBits value for RemoteTuxDom " + var18 == null ? this.getAccessPoint() : var18);
            } else if (var24 != null && !var24.equals("LOCAL") && !var24.equals("ON_DEMAND") && !var24.equals("ON_STARTUP") && !var24.equals("INCOMING_ONLY")) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/80/Invalid Connection Policy value: " + var24);
               }

               throw new BeanUpdateRejectedException("Invalid Connection Policy Value: " + var24);
            } else if (var20 != null && !var20.equals("LOCAL") && !var20.equals("GLOBAL")) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/90/Invalid ACL Policy value: " + var20);
               }

               throw new BeanUpdateRejectedException("Invalid ACL Policy Value: " + var20);
            } else if (var21 != null && !var21.equals("LOCAL") && !var21.equals("GLOBAL")) {
               if (var32) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/100/Invalid Credential Policy value: " + var21);
               }

               throw new BeanUpdateRejectedException("Invalid Credential Policy Value: " + var21);
            } else {
               if (var4 != null) {
                  try {
                     this.parseNWAddr(var4, true);
                  } catch (TPException var36) {
                     if (var32) {
                        ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/110/invalid NWAddr format(" + var4 + ").");
                     }

                     throw new BeanUpdateRejectedException(var36.getMessage());
                  }
               }

               try {
                  if (var18 != null) {
                     this.setAccessPoint(var18);
                  }

                  if (var17 != null) {
                     this.setLocalAccessPoint(var17);
                  }

                  if (var19 != null) {
                     this.setAccessPointId(var19);
                  }

                  if (var20 != null) {
                     this.setAclPolicy(var20);
                  }

                  if (var21 != null) {
                     this.setCredentialPolicy(var21);
                  }

                  if (var23 != null) {
                     this.setTpUsrFile(var23);
                  }

                  if (var5 != null) {
                     this.setFederationURL(var5);
                  }

                  if (var6 != null) {
                     this.setFederationName(var6);
                  }

                  if (var7 != this.myCmpLimit) {
                     this.setCmpLimit(var7);
                  }

                  if (var8 != this.MinEncryptionBits) {
                     this.setMinEncryptBits(var8);
                  }

                  if (var9 != this.MaxEncryptionBits) {
                     this.setMaxEncryptBits(var9);
                  }

                  if (var24 != null) {
                     this.setConnectionPolicy(var24);
                  }

                  if (var27 != -1L) {
                     this.setRetryInterval(var27);
                  }

                  if (var25 != -1L) {
                     this.setMaxRetries(var25);
                  }

                  if (var10 != null) {
                     this.setAppKey(var10);
                  }

                  if (var11 != this.myAllowAnonymous) {
                     this.setAllowAnonymous(var11);
                  }

                  if (var12 != this.myDfltAppKey) {
                     this.setDefaultAppKey(var12);
                  }

                  if (var13 != null) {
                     this.setTuxedoUidKw(var13);
                  }

                  if (var14 != null) {
                     this.setTuxedoGidKw(var14);
                  }

                  if (var15 != null) {
                     this.setCustomAppKeyClass(var15);
                  }

                  if (var16 != null) {
                     this.setCustomAppKeyClassParam(var16);
                  }

                  if (var29 != -1) {
                     this.setKeepAlive(var29);
                     if (this.myDsession != null) {
                        this.myDsession.setKeepAlive(this.getKeepAlive());
                     }
                  }

                  if (var30 != -1) {
                     this.setKeepAliveWait(var30);
                     if (this.myDsession != null) {
                        this.myDsession.setKeepAliveWait(this.getKeepAliveWait());
                     }
                  }
               } catch (TPException var37) {
                  if (var32) {
                     ntrace.doTrace("*]/TDMRemoteTDomain/prepareUpdate/120/change failed for RemoteTuxDom: " + this.getAccessPoint());
                  }

                  throw new BeanUpdateRejectedException(var37.getMessage());
               }

               if (var32) {
                  ntrace.doTrace("]/TDMRemoteTDomain/prepareUpdate/130/DONE");
               }

            }
         }
      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/activateUpdate");
         ntrace.doTrace("]/TDMRemoteTDomain/activateUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemoteTDomain/rollbackUpdate");
         ntrace.doTrace("]/TDMRemoteTDomain/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMRemoteTDomain: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMRemoteTD: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/unregisterListener/10/DONE");
      }

   }

   public void removeConnectingTaskReference() {
      this.connectingTask = null;
   }

   public boolean hasConnectingTask() {
      return this.connectingTask != null;
   }

   public void terminateConnectingTask() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/terminateConnectingTask/0");
      }

      if (this.connectingTask != null) {
         this.connectingTask.connectingTerminate();
      }

      if (var1) {
         ntrace.doTrace("]/TDMRemoteTDomain/terminateConnectingTask/10/DONE");
      }

   }

   public void setNetworkAddr(String[] var1) {
   }

   public String[] getNetworkAddr() {
      return this.myNWAddr;
   }

   private final class TimedMutex {
      private boolean locked;

      private TimedMutex() {
         this.locked = false;
      }

      public synchronized boolean acquire(long var1) {
         if (var1 == 0L) {
            while(this.locked) {
               try {
                  this.wait();
               } catch (InterruptedException var5) {
                  this.notify();
               }
            }
         } else if (this.locked) {
            try {
               this.wait(var1);
            } catch (InterruptedException var4) {
               this.notify();
            }

            if (this.locked) {
               return false;
            }
         }

         this.locked = true;
         return true;
      }

      public synchronized boolean attempt() {
         if (this.locked) {
            return false;
         } else {
            this.locked = true;
            return true;
         }
      }

      public synchronized void release() {
         this.locked = false;
         this.notify();
      }

      public boolean isLocked() {
         return this.locked;
      }

      // $FF: synthetic method
      TimedMutex(Object var2) {
         this();
      }
   }

   private class DoneException extends Exception {
      static final long serialVersionUID = -6282186224051838441L;

      private DoneException() {
      }

      // $FF: synthetic method
      DoneException(Object var2) {
         this();
      }
   }
}
