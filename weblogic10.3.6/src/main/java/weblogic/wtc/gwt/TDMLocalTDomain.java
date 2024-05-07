package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import java.util.ArrayList;
import java.util.StringTokenizer;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCLocalTuxDomMBean;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.config.WTCLocalAccessPoint;
import weblogic.wtc.jatmi.TPException;

public class TDMLocalTDomain extends TDMLocal implements BeanUpdateListener {
   static final long serialVersionUID = -8456049645166331478L;
   private String[] myNWAddr;
   private String[] ipaddress;
   private int[] port;
   private int myCmpLimit;
   private int MinEncryptionBits;
   private int MaxEncryptionBits;
   private boolean myInteroperate;
   private WTCLocalTuxDomMBean mBean = null;
   private OatmialListener oatmialListener = null;
   private boolean registered = false;
   private int keepAlive = -1;
   private int keepAliveWait;
   private String origNWAddr;
   private boolean isSSL = false;
   private boolean[] useSDP;

   public TDMLocalTDomain(String var1) throws Exception {
      super(var1);
      super.setType("TDOMAIN");
      this.setSecurity("NONE");
      this.setConnectionPolicy("ON_DEMAND");
      this.setCmpLimit(Integer.MAX_VALUE);
      this.setMinEncryptBits(0);
      this.setMaxEncryptBits(128);
      this.setInteroperate("No");
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
            if (var1 >= this.myNWAddr.length) {
               var2 = null;
               return var2;
            }

            var2 = this.myNWAddr[var1];
         } finally {
            this.r.unlock();
         }

         return var2;
      }
   }

   public void setNWAddr(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMLocalTDomain/setNWAddr/addr=" + var1);
         } else {
            ntrace.doTrace("[/TDMLocalTDomain/setNWAddr/addr=null");
         }
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setNWAddr/10/TPEINVAL");
         }

         throw new TPException(4, "null NWAddr found in local domain " + this.getAccessPointId());
      } else if (!var1.startsWith("//") && !var1.toLowerCase().startsWith("sdp://")) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setNWAddr/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid NWAddr format \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      } else {
         StringTokenizer var5 = new StringTokenizer(var1, ",");
         int var6 = var5.countTokens();
         String[] var7 = new String[var6];
         String[] var8 = new String[var6];
         int[] var9 = new int[var6];
         boolean[] var11 = new boolean[var6];

         for(int var10 = 0; var10 < var6; ++var10) {
            var7[var10] = var5.nextToken();
            if (!TDMRemoteTDomain.isNWAddrFormat(var7[var10])) {
               if (var2) {
                  ntrace.doTrace("*]/TDMLocalTDomain/setNWAddr/30/TPEINVAL");
               }

               throw new TPException(4, "Invalid NWAddr format \"" + var1 + "\" found in local domain " + this.getAccessPointId());
            }

            var11[var10] = false;
            if (var7[var10].toLowerCase().startsWith("sdp://")) {
               var7[var10] = var7[var10].substring(4);
               var11[var10] = true;
            }

            int var3 = var7[var10].indexOf(58);
            var8[var10] = var7[var10].substring(2, var3);
            String var4 = var7[var10].substring(var3 + 1);
            var9[var10] = Integer.parseInt(var4, 10);
         }

         this.w.lock();
         this.myNWAddr = var7;
         this.ipaddress = var8;
         this.port = var9;
         this.origNWAddr = var1;
         this.useSDP = var11;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setNWAddr/40/SUCCESS");
         }

      }
   }

   public String[] get_ipaddress() {
      this.r.lock();

      String[] var1;
      try {
         var1 = this.ipaddress;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public int[] get_port() {
      this.r.lock();

      int[] var1;
      try {
         var1 = this.port;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public WTCLocalTuxDomMBean getMBean() {
      return this.mBean;
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
         ntrace.doTrace("[/TDMLocalTDomain/setCmpLimit/limit=" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.myCmpLimit = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setCmpLimit/20/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setCmpLimit/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid compression limit value " + var1 + " found in local domain " + this.getAccessPointId());
      }
   }

   public int getMinEncryptBits() {
      this.r.lock();

      int var1;
      try {
         if (this.MinEncryptionBits >= 256 && !this.isSSL) {
            short var5 = 128;
            return var5;
         }

         var1 = this.MinEncryptionBits;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMinEncryptBits(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/setMinEncryptBits/bits=" + var1);
      }

      if (var1 != 0 && var1 != 40 && var1 != 56 && var1 != 112 && var1 != 128 && var1 != 256) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setMinEncryptBits/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid MIN Encryption bit value " + this.MinEncryptionBits + " found in local domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.MinEncryptionBits = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setMinEncryptBits/5/SUCCESS");
         }

      }
   }

   public int getMaxEncryptBits() {
      this.r.lock();

      int var1;
      try {
         if (this.MaxEncryptionBits >= 256 && !this.isSSL) {
            short var5 = 128;
            return var5;
         }

         var1 = this.MaxEncryptionBits;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMaxEncryptBits(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/setMaxEncryptBits/bits=" + var1);
      }

      if (var1 != 0 && var1 != 40 && var1 != 56 && var1 != 112 && var1 != 128 && var1 != 256) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setMaxEncryptBits/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid MAX Encryption bit value " + this.MaxEncryptionBits + " found in local domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.MaxEncryptionBits = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setMaxEncryptBits/5/SUCCESS");
         }

      }
   }

   public String getInteroperate() {
      return this.myInteroperate ? "YES" : "NO";
   }

   public boolean isInteroperate() {
      this.r.lock();

      boolean var1;
      try {
         var1 = this.myInteroperate;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setInteroperate(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMLocalTDomain/setInteroperate/interop=" + var1);
         } else {
            ntrace.doTrace("[/TDMLocalTDomain/setInteroperate/interop=null");
         }
      }

      if (var1 != null) {
         if (var1.compareToIgnoreCase("Yes") == 0) {
            this.w.lock();
            this.myInteroperate = true;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMLocalTDomain/setInteroperate/5/SUCCESS");
            }

            return;
         }

         if (var1.compareToIgnoreCase("No") == 0) {
            this.w.lock();
            this.myInteroperate = false;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMLocalTDomain/setInteroperate/10/SUCCESS");
            }

            return;
         }
      }

      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setInteroperate/15/TPEINVAL");
         }

         throw new TPException(4, "Invalid Interoperate value specified \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setInteroperate/20/SUCCESS");
         }

      }
   }

   public void setType(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMLocalTDomain/setType/type=" + var1);
         } else {
            ntrace.doTrace("[/TDMLocalTDomain/setType/type=null");
         }
      }

      if (var1 != null && var1.equals("TDOMAIN")) {
         super.setType(var1);
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setType/5/SUCCESS");
         }

      } else if (var1 != null) {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setType/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid Domain type \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setType/20/SUCCESS");
         }

      }
   }

   public void setKeepAlive(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/setKeepAlive/ka =" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.keepAlive = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setKeepAlive/20");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setKeepAlive/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid KeepAlive value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      }
   }

   public int getKeepAlive() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/getKeepAlive");
      }

      this.r.lock();

      int var3;
      try {
         int var2 = this.keepAlive;
         if (var2 == -1) {
            var2 = 0;
         }

         var3 = var2;
      } finally {
         if (var1) {
            ntrace.doTrace("]/TDMLocalTDomain/getKeepAlive/10");
         }

         this.r.unlock();
      }

      return var3;
   }

   public void setKeepAliveWait(int var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/setKeepAliveWait/kaw =" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.keepAliveWait = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocalTDomain/setKeepAliveWait/20");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/setKeepAliveWait/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid KeepAliveWait value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      }
   }

   public int getKeepAliveWait() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/getKeepAliveWait");
      }

      this.r.lock();

      int var3;
      try {
         int var2;
         if (this.keepAlive != -1 && this.keepAliveWait != -1) {
            var2 = this.keepAliveWait;
         } else {
            var2 = 0;
         }

         var3 = var2;
      } finally {
         if (var1) {
            ntrace.doTrace("]/TDMLocalTDomain/getKeepAliveWait/10");
         }

         this.r.unlock();
      }

      return var3;
   }

   public boolean isUseSSL() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/isUseSSL");
      }

      this.r.lock();

      boolean var2;
      try {
         var2 = this.isSSL;
      } finally {
         if (var1) {
            ntrace.doTrace("]/TDMLocalTDomain/isUseSSL/10");
         }

         this.r.unlock();
      }

      return var2;
   }

   public void setMBean(WTCLocalTuxDomMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocalTDomain/setMBname/MBeanName=" + var1.getName());
         }

         if (this.mBean != null) {
            if (this.mBean == var1) {
               if (var2) {
                  ntrace.doTrace("]/TDMLocalTDomain/setMBname/10/no change");
               }

               return;
            }

            this.unregisterListener();
         }

         this.mBean = var1;
         String var3 = var1.getUseSSL();
         if (var3.equals("TwoWay") || var3.equals("OneWay")) {
            this.isSSL = true;
         }
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMLocalTDomain/setMBname/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }

         this.isSSL = false;
      }

      if (!this.isSSL && (this.MinEncryptionBits >= 256 || this.MaxEncryptionBits >= 256)) {
         WTCLogger.logInfoLLEEncryptBitsDowngrade(this.getAccessPointId());
      }

      if (var2) {
         ntrace.doTrace("]/TDMLocalTDomain/setMBname/20/DONE");
      }

   }

   public void checkConfigIntegrity() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(16);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/checkConfigIntegrity/ldom=" + this.getAccessPointId());
      }

      if (this.MinEncryptionBits > this.MaxEncryptionBits) {
         Loggable var2 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Local", this.getAccessPointId());
         var2.log();
         throw new TPException(4, var2.getMessage());
      } else {
         if (var1) {
            ntrace.doTrace("]/TDMLocalTDomain/checkConfigIntegrity/20/true");
         }

      }
   }

   public void setOatmialListener(OatmialListener var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/setOatmialListener/10/BEGIN");
      }

      this.oatmialListener = var1;
      if (var2) {
         ntrace.doTrace("]/TDMLocalTDomain/setOatmialListener/20/DONE");
      }

   }

   public OatmialListener getOatmialListener() {
      return this.oatmialListener;
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

   public boolean hasActiveTsession() {
      TDMRemoteTDomain[] var1 = (TDMRemoteTDomain[])((TDMRemoteTDomain[])this.get_remote_domains());

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].getTsession(false) != null) {
            return true;
         }
      }

      return false;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCLocalTuxDomMBean var3 = (WTCLocalTuxDomMBean)var1.getProposedBean();
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      String var8 = null;
      long var9 = -1L;
      long var11 = -1L;
      Object var13 = null;
      long var14 = -1L;
      int var16 = -1;
      int var17 = -1;
      int var18 = -1;
      int var19 = -1;
      byte var20 = -1;
      String var21 = null;
      String var23 = null;
      boolean var24 = ntrace.isTraceEnabled(16);
      if (var24) {
         ntrace.doTrace("[/TDMLocalTDomain/prepareUpdate");
      }

      if (var3 == null) {
         if (var24) {
            ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMLocalTuxDom!");
      } else {
         int var25;
         for(var25 = 0; var25 < var2.length; ++var25) {
            String var26 = var2[var25].getPropertyName();
            int var27 = var2[var25].getUpdateType();
            if (var24) {
               ntrace.doTrace("i = " + var25 + ", optype = " + var27 + ", key = " + var26);
            }

            if (var27 == 1) {
               if (var26.equals("AccessPointId")) {
                  var6 = var3.getAccessPointId();
                  if (var24) {
                     ntrace.doTrace("AccessPointId: " + var6);
                  }
               } else if (var26.equals("AccessPoint")) {
                  var5 = var3.getAccessPoint();
                  if (var24) {
                     ntrace.doTrace("AccessPoint: " + var5);
                  }
               } else if (var26.equals("NWAddr")) {
                  var4 = var3.getNWAddr();
                  if (var24) {
                     ntrace.doTrace("NWAddr: " + var4);
                  }
               } else if (var26.equals("Security")) {
                  var7 = var3.getSecurity();
                  if (var24) {
                     ntrace.doTrace("Security: " + var7);
                  }
               } else if (var26.equals("ConnectionPolicy")) {
                  var8 = var3.getConnectionPolicy();
                  if (var24) {
                     ntrace.doTrace("ConnectionPolicy: " + var8);
                  }
               } else if (var26.equals("Interoperate")) {
                  var21 = var3.getInteroperate();
                  if (var24) {
                     ntrace.doTrace("Interoperate: " + var21);
                  }
               } else if (var26.equals("RetryInterval")) {
                  var11 = var3.getRetryInterval();
                  if (var24) {
                     ntrace.doTrace("RetryInterval: " + var11);
                  }
               } else if (var26.equals("MaxRetries")) {
                  var9 = var3.getMaxRetries();
                  if (var24) {
                     ntrace.doTrace("MaxRetries: " + var9);
                  }
               } else if (var26.equals("BlockTime")) {
                  var14 = var3.getBlockTime();
                  if (var24) {
                     ntrace.doTrace("BlockTime: " + var14);
                  }
               } else if (var26.equals("CmpLimit")) {
                  var16 = var3.getCmpLimit();
                  if (var24) {
                     ntrace.doTrace("CmpLimit: " + var16);
                  }
               } else {
                  String var22;
                  if (var26.equals("MinEncryptBits")) {
                     var22 = var3.getMinEncryptBits();
                     var17 = Integer.parseInt(var22, 10);
                     if (var24) {
                        ntrace.doTrace("MinEncryptBits: " + var17);
                     }
                  } else if (var26.equals("MaxEncryptBits")) {
                     var22 = var3.getMaxEncryptBits();
                     var18 = Integer.parseInt(var22, 10);
                     if (var24) {
                        ntrace.doTrace("MaxEncryptBits: " + var18);
                     }
                  } else if (var26.equals("KeepAlive")) {
                     var19 = var3.getKeepAlive();
                     if (var24) {
                        ntrace.doTrace("KeepAlive: " + var19);
                     }
                  } else if (var26.equals("KeepAliveWait")) {
                     var19 = var3.getKeepAliveWait();
                     if (var24) {
                        ntrace.doTrace("KeepAliveWait: " + var20);
                     }
                  } else if (var26.equals("UseSSL")) {
                     var23 = var3.getUseSSL();
                     if (var24) {
                        ntrace.doTrace("UseSSL: " + var23);
                     }
                  }
               }
            } else if (var27 == 2) {
               if (var24) {
                  ntrace.doTrace("Unexpected ADD operation, ignored!");
               }
            } else {
               if (var27 != 3) {
                  if (var24) {
                     ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/20/Unknown operation " + var27);
                  }

                  throw new BeanUpdateRejectedException("Unknown operation(" + var27 + ") for TDMResources.");
               }

               if (var24) {
                  ntrace.doTrace("Unexpected REMOVE operation, ignored!");
               }
            }
         }

         if (var5 != null && !this.getAccessPoint().equals(var5)) {
            TDMLocalTDomain var29 = WTCService.getWTCService().getLocalDomain(var5);
            if (var29 != null) {
               if (var24) {
                  ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/30/LAP  " + var5 + " already exists!");
               }

               throw new BeanUpdateRejectedException("LocalTuxDom" + var5 + " already exists!");
            }
         }

         if (var23 != null) {
            if (var23.equals("Off")) {
               this.isSSL = false;
            } else {
               this.isSSL = true;
            }
         }

         if (var17 != -1 && var17 != this.MinEncryptionBits) {
            if (var17 != 0 && var17 != 40 && var17 != 56 && var17 != 128 && var17 != 256) {
               if (var24) {
                  ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/40/Invalid MinEncryptionBits " + var17);
               }

               throw new BeanUpdateRejectedException("Invalid MinEncryptionBits value:" + var17);
            }

            var25 = var17;
         } else {
            var25 = this.MinEncryptionBits;
         }

         int var30;
         if (var18 != -1 && var18 != this.MaxEncryptionBits) {
            if (var18 != 0 && var18 != 40 && var18 != 56 && var18 != 128 && var18 != 256) {
               if (var24) {
                  ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/50/Invalid MaxEncryptionBits " + var18);
               }

               throw new BeanUpdateRejectedException("Invalid MaxEncryptionBits value:" + var18);
            }

            var30 = var18;
         } else {
            var30 = this.MaxEncryptionBits;
         }

         if ((var18 != -1 || var17 != -1) && var30 < var25) {
            if (var24) {
               ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/60/MaxEncryptionBits (" + var30 + ") less than MinEncryptionBits (" + var25 + " )");
            }

            throw new BeanUpdateRejectedException("MaxEncryptionBits value less than MinEncryptionBits value");
         } else if (var7 != null && !var7.equals("NONE") && !var7.equals("APP_PW") && !var7.equals("DM_PW")) {
            if (var24) {
               ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/70/Invalid security value  " + var7);
            }

            throw new BeanUpdateRejectedException("Invalid security value: " + var7);
         } else if (var8 != null && !var8.equals("ON_DEMAND") && !var8.equals("ON_STARTUP") && !var8.equals("INCOMING_ONLY")) {
            if (var24) {
               ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/80/Invalid connection policy " + var8);
            }

            throw new BeanUpdateRejectedException("Invalid connection policy: " + var8);
         } else if (var21 != null && var21.compareToIgnoreCase("Yes") == 0 && var21.compareToIgnoreCase("No") == 0) {
            if (var24) {
               ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/90/Invalid interoperate value " + var21);
            }

            throw new BeanUpdateRejectedException("Invalid interoperate value: " + var21);
         } else {
            try {
               if (var5 != null) {
                  this.setAccessPoint(var5);
               }

               if (var6 != null) {
                  this.setAccessPointId(var6);
               }

               if (var4 != null) {
                  this.setNWAddr(var4);
               }

               if (var7 != null) {
                  this.setSecurity(var7);
               }

               if (var8 != null) {
                  this.setConnectionPolicy(var8);
               }

               if (var9 != -1L) {
                  this.setMaxRetries(var9);
               }

               if (var11 != -1L) {
                  this.setRetryInterval(var11);
               }

               if (var14 != -1L) {
                  this.setBlockTime(var14);
               }

               if (var16 != -1) {
                  this.setCmpLimit(var16);
               }

               if (var17 != -1) {
                  this.setMinEncryptBits(var17);
               }

               if (var18 != -1) {
                  this.setMaxEncryptBits(var18);
               }

               if (var21 != null) {
                  this.setInteroperate(var21);
               }

               if (var19 != -1) {
                  this.setKeepAlive(var19);
               }

               if (var20 != -1) {
                  this.setKeepAliveWait(var20);
               }
            } catch (TPException var28) {
               if (var24) {
                  ntrace.doTrace("*]/TDMLocalTDomain/prepareUpdate/100/Exception: " + var28.getMessage());
               }

               throw new BeanUpdateRejectedException(var28.getMessage());
            }

            if ((var23 != null || var17 != -1 || var18 != -1) && !this.isSSL && (this.MinEncryptionBits >= 256 || this.MaxEncryptionBits >= 256)) {
               WTCLogger.logInfoLLEEncryptBitsDowngrade(this.getAccessPointId());
            }

            if (var24) {
               ntrace.doTrace("]/TDMLocalTDomain/prepareUpdate/110/DONE");
            }

         }
      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/activeUpdate");
         ntrace.doTrace("]/TDMLocalTDomain/activeUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/rollbackUpdate");
         ntrace.doTrace("]/TDMLocalTDomain/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMLocalTDomain: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMLocalTDomain/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMLocalTDomain/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMLocalTDomain: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMLocalTDomain/unregisterListener/10/DONE");
      }

   }

   public static TDMLocalTDomain create(WTCLocalTuxDomMBean var0) throws TPException {
      String var1 = var0.getAccessPoint();
      boolean var2 = ntrace.isMixedTraceEnabled(18);
      if (var2) {
         ntrace.doTrace("[/TDMLocalTDomain/create/" + var1);
      }

      if (var1 == null) {
         Loggable var11 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPoint", var0.getName());
         var11.log();
         if (var2) {
            ntrace.doTrace("*]/TDMLocalTDomain/create/10/no AP");
         }

         throw new TPException(4, var11.getMessage());
      } else {
         if (var2) {
            ntrace.doTrace("AccessPoint: " + var1);
         }

         String var3 = var0.getAccessPointId();
         if (var3 == null) {
            Loggable var12 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPointId", var0.getName());
            var12.log();
            if (var2) {
               ntrace.doTrace("*]/TDMLocalTDomain/create/20/no APId");
            }

            throw new TPException(4, var12.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("AccessPointId:" + var3);
            }

            String var4 = var0.getNWAddr();
            if (var4 == null) {
               Loggable var13 = WTCLogger.logUndefinedMBeanAttrLoggable("NWAddr", var0.getName());
               var13.log();
               if (var2) {
                  ntrace.doTrace("*]/TDMLocalTDomain/create/30/no NWAddr");
               }

               throw new TPException(4, var13.getMessage());
            } else {
               if (var2) {
                  ntrace.doTrace("NWAddr:" + var4);
               }

               if (var2) {
                  ntrace.doTrace("create ltd from " + var1);
               }

               TDMLocalTDomain var5;
               Loggable var7;
               try {
                  var5 = new TDMLocalTDomain(var1);
               } catch (Exception var8) {
                  var7 = WTCLogger.logUEconstructTDMLocalTDLoggable(var8.getMessage());
                  var7.log();
                  if (var2) {
                     ntrace.doTrace("*]/TDMLocalTDomain/create/40/create failed");
                  }

                  throw new TPException(4, var7.getMessage());
               }

               var5.setAccessPointId(var3);
               var5.setSecurity(var0.getSecurity());
               var5.setConnectionPolicy(var0.getConnectionPolicy());
               var5.setInteroperate(var0.getInteroperate());
               var5.setRetryInterval(var0.getRetryInterval());
               var5.setMaxRetries(var0.getMaxRetries());
               var5.setBlockTime(var0.getBlockTime() * 1000L);

               try {
                  var5.setNWAddr(var4);
               } catch (TPException var10) {
                  var7 = WTCLogger.logInvalidMBeanAttrLoggable("NWAddr", var0.getName());
                  var7.log();
                  if (var2) {
                     ntrace.doTrace("*]/TDMLocalTDomain/create/50/bad NWAddr:" + var10.getMessage());
                  }

                  throw new TPException(4, var7.getMessage());
               }

               try {
                  var5.setCmpLimit(var0.getCmpLimit());
               } catch (TPException var9) {
                  var7 = WTCLogger.logInvalidMBeanAttrLoggable("CmpLimit", var0.getName());
                  var7.log();
                  if (var2) {
                     ntrace.doTrace("*]/TDMLocalTDomain/create/60/bad CMPLIMIT:" + var9.getMessage());
                  }

                  throw new TPException(4, var7.getMessage());
               }

               String var6 = var0.getMinEncryptBits();
               if (var6 != null) {
                  var5.setMinEncryptBits(Integer.parseInt(var6, 10));
               }

               var6 = var0.getMaxEncryptBits();
               if (var6 != null) {
                  var5.setMaxEncryptBits(Integer.parseInt(var6, 10));
               }

               var5.setKeepAlive(var0.getKeepAlive());
               var5.setKeepAliveWait(var0.getKeepAliveWait());
               var5.setMBean(var0);
               if (var2) {
                  ntrace.doTrace("]/TDMLocalTDomain/create/70/success");
               }

               return var5;
            }
         }
      }
   }

   public ArrayList assembleConfigObject() throws TPException {
      WTCLocalAccessPoint var1 = new WTCLocalAccessPoint();
      String[] var2 = new String[]{this.origNWAddr};
      var1.setNetworkAddr(var2);
      var1.setEndPointGroup(this.myNWAddr);
      var1.fillFromSource(this);
      this.addConfigObj(var1);
      return this.getConfigObjList();
   }
}
