package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import java.util.HashSet;
import weblogic.wtc.jatmi.TPException;

public abstract class TDMLocal extends WTCMBeanObject {
   private String myAccessPoint;
   private String myWlsClusterName;
   private String myAccessPointId;
   private String myType;
   private String mySecurity;
   private String myConnectionPolicy;
   private long myMaxRetries;
   private long myRetryInterval;
   private String myConnPrincipalName;
   private long myBlockTime;
   private HashSet myRemoteDomains;

   public TDMLocal(String var1) throws Exception {
      if (var1 == null) {
         throw new Exception("AccessPoint cannot be null");
      } else {
         this.myAccessPoint = var1;
         this.myRemoteDomains = new HashSet();
         this.myBlockTime = 60000L;
         this.myMaxRetries = -1L;
         this.myRetryInterval = -1L;
      }
   }

   public String getAccessPoint() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myAccessPoint;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public String getWlsClusterName() {
      return this.myWlsClusterName;
   }

   public boolean setWlsClusterName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMLocal/setWlsClusterName/name=" + var1);
      }

      this.myWlsClusterName = var1;
      if (var2) {
         ntrace.doTrace("]/TDMLocal/setWlsClusterName/15/true");
      }

      return true;
   }

   public String getAccessPointId() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myAccessPointId;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setAccessPoint(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setAccessPoint/id=null");
            ntrace.doTrace("*]/TDMLocal/setAccessPoint/10/TPEINVAL");
         }

         throw new TPException(4, "null AccessPoint found in one of the local domain");
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setAccessPoint/id=" + var1);
         }

         this.w.lock();
         this.myAccessPoint = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocal/setAccessPoint/20/SUCCESS");
         }

      }
   }

   public void setAccessPointId(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setAccessPointId/id=null");
            ntrace.doTrace("*]/TDMLocal/setAccessPointId/10/TPEINVAL");
         }

         throw new TPException(4, "null AccessPointId found in one of the local domain");
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setAccessPointId/id=" + var1);
         }

         this.w.lock();
         this.myAccessPointId = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMLocal/setAccessPointId/20/SUCCESS");
         }

      }
   }

   public synchronized String getType() {
      return this.myType;
   }

   public synchronized void setType(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMLocal/setType/type=" + var1);
         } else {
            ntrace.doTrace("[/TDMLocal/setType/type=null");
         }
      }

      if (var1 != null) {
         this.myType = var1;
      }

      if (var2) {
         ntrace.doTrace("]/TDMLocal/setType/10/SUCCESS");
      }

   }

   public String getSecurity() {
      this.r.lock();

      String var1;
      try {
         var1 = this.mySecurity;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setSecurity(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setSecurity/security=null");
            ntrace.doTrace("]/TDMLocal/setSecurity/10/TPEINVAL");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setSecurity/security=" + var1);
         }

         if (!var1.equals("NONE") && !var1.equals("APP_PW") && !var1.equals("DM_PW")) {
            if (var2) {
               ntrace.doTrace("*]/TDMLocal/setSecurity/25/TPEINVAL");
            }

            throw new TPException(4, "Invalid Domain Security type \"" + var1 + "\" found in local domain " + this.getAccessPointId());
         } else {
            this.w.lock();
            this.mySecurity = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMLocal/setSecurity/20/SUCCESS");
            }

         }
      }
   }

   public String getConnectionPolicy() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myConnectionPolicy;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setConnectionPolicy(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setConnectionPolicy/policy=null");
            ntrace.doTrace("]/TDMLocal/setConnectionPolicy/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setConnectionPolicy/policy=" + var1);
         }

         if (!var1.equals("ON_DEMAND") && !var1.equals("ON_STARTUP") && !var1.equals("INCOMING_ONLY")) {
            if (var2) {
               ntrace.doTrace("*]/TDMLocal/setConnectionPolicy/30/TPEINVAL");
            }

            throw new TPException(4, "Invalid ConnectionPolicy \"" + var1 + "\" found in local domain " + this.getAccessPointId());
         } else {
            this.w.lock();
            this.myConnectionPolicy = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMLocal/setConnectionPolicy/20/SUCCESS");
            }

         }
      }
   }

   public String getConnPrincipalName() {
      this.r.lock();

      String var1;
      try {
         if (this.myConnPrincipalName == null) {
            var1 = this.myAccessPointId;
            return var1;
         }

         var1 = this.myConnPrincipalName;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setConnPrincipalName(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMLocal/setConnPrincipalName/name=" + var1);
         }

         this.myConnPrincipalName = var1;
      } else if (var2) {
         ntrace.doTrace("[/TDMLocal/setConnPrincipalName/name=null");
      }

      if (var2) {
         ntrace.doTrace("]/TDMLocal/setConnPrincipalName/10/SUCCESS");
      }

   }

   public long getRetryInterval() {
      long var1 = Long.MAX_VALUE;
      this.r.lock();
      if (this.myConnectionPolicy.equals("ON_STARTUP")) {
         var1 = this.myRetryInterval;
         if (this.myMaxRetries != 0L && var1 == -1L) {
            var1 = 60L;
         }
      }

      this.r.unlock();
      return var1;
   }

   public void setRetryInterval(long var1) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var3) {
         ntrace.doTrace("[/TDMLocal/setRetryInterval/name=" + var1);
      }

      if (var1 >= 0L && var1 <= 2147483647L) {
         this.w.lock();
         this.myRetryInterval = var1;
         this.w.unlock();
         if (var3) {
            ntrace.doTrace("]/TDMLocal/setRetryInterval/10/SUCCESS");
         }

      } else {
         if (var3) {
            ntrace.doTrace("*]/TDMLocal/setRetryInterval/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid RetryInterval value " + var1 + " found in one of the local domain" + this.getAccessPointId());
      }
   }

   public long getMaxRetries() {
      long var1 = 0L;
      this.r.lock();
      if (this.myConnectionPolicy.equals("ON_STARTUP") && (var1 = this.myMaxRetries) == -1L) {
         var1 = Long.MAX_VALUE;
      }

      this.r.unlock();
      return var1;
   }

   public void setMaxRetries(long var1) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var3) {
         ntrace.doTrace("[/TDMLocal/setMaxRetries/retries=" + var1);
      }

      if (var1 >= 0L && var1 <= Long.MAX_VALUE) {
         this.w.lock();
         this.myMaxRetries = var1;
         this.w.unlock();
         if (var3) {
            ntrace.doTrace("]/TDMLocal/setMaxRetries/10/SUCCESS");
         }

      } else {
         if (var3) {
            ntrace.doTrace("*]/TDMLocal/setMaxRetries/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid MAXRETRY value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      }
   }

   public void setBlockTime(long var1) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var3) {
         ntrace.doTrace("[/TDMLocal/setBlockTime/time=" + var1);
      }

      if (var1 < 0L) {
         if (var3) {
            ntrace.doTrace("*]/TDMLocal/setBlockTime/10/TPEINVAL");
         }

         throw new TPException(4, "Invalid block time value \"" + var1 + "\" found in local domain " + this.getAccessPointId());
      } else {
         this.w.lock();
         this.myBlockTime = var1;
         this.w.unlock();
         if (var3) {
            ntrace.doTrace("]/TDMLocal/setBlockTime/20/SUCCESS");
         }

      }
   }

   public long getBlockTime() {
      this.r.lock();

      long var1;
      try {
         var1 = this.myBlockTime;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public synchronized boolean add_remote_domain(TDMRemote var1) {
      return this.myRemoteDomains.add(var1);
   }

   public synchronized void remove_remote_domain(TDMRemote var1) {
      this.myRemoteDomains.remove(var1);
   }

   public synchronized TDMRemote[] get_remote_domains() {
      return (TDMRemote[])((TDMRemote[])this.myRemoteDomains.toArray(new TDMRemote[0]));
   }

   public boolean equals(Object var1) {
      TDMLocal var2 = (TDMLocal)var1;
      String var3 = var2.getAccessPoint();
      this.r.lock();

      boolean var4;
      try {
         if (this.myAccessPoint != null && var2 != null) {
            var4 = var3.equals(this.myAccessPoint);
            return var4;
         }

         var4 = false;
      } finally {
         this.r.unlock();
      }

      return var4;
   }

   public int hashCode() {
      this.r.lock();

      int var1;
      try {
         if (this.myAccessPoint != null) {
            var1 = this.myAccessPoint.hashCode();
            return var1;
         }

         var1 = 0;
      } finally {
         this.r.unlock();
      }

      return var1;
   }
}
