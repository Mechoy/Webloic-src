package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.config.TuxedoConnectorRAP;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.gwatmi;

public abstract class TDMRemote extends WTCMBeanObject implements TuxedoConnectorRAP {
   static final long serialVersionUID = 8838736910331494533L;
   private String myAccessPoint;
   private String myAccessPointId;
   private String myLocalAccessPoint;
   private String myType;
   private String myAclPolicy;
   private String myConnPrincipalName;
   private String myCredentialPolicy;
   private String myTpUsrFile;
   private String myConnectionPolicy;
   private long myMaxRetries;
   private long myRetryInterval;
   private int myConnPolicyConfigState;
   private TDMLocal myLocalAccessPointObject;
   private TuxXidRply myXidRplyObj;
   public static final int NOTDEFINED_STATE = 0;
   public static final int DEFINED_STATE = 1;
   public static final int DEFAULT_STATE = 2;
   public static final int FORCEDEFAULT_STATE = 3;

   public TDMRemote(String var1, TuxXidRply var2) throws Exception {
      if (var1 == null) {
         throw new Exception("AccessPoint may not be null");
      } else {
         this.myAccessPoint = var1;
         this.myXidRplyObj = var2;
         this.myConnPolicyConfigState = 0;
         this.myMaxRetries = -1L;
         this.myRetryInterval = -1L;
      }
   }

   public TuxXidRply getUnknownXidRplyObj() {
      return this.myXidRplyObj;
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

   public void setAccessPointId(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAccessPointId/id=null");
            ntrace.doTrace("*]TDMRemote/setAccessPointId/10/TPEINVAL");
         }

         throw new TPException(4, "null AccessPointId found in one of the remotedomain.");
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAccessPointId/id=" + var1);
         }

         this.w.lock();
         this.myAccessPointId = var1;
         this.myConnPrincipalName = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setAccessPointId/20/SUCCESS");
         }

      }
   }

   public void setAccessPoint(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAccessPoint/name=null");
            ntrace.doTrace("*]/TDMRemote/setAccessPoint/10/TPEINVAL");
         }

         throw new TPException(4, "null AccessPoint found in one of the remote domains");
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAccessPoint/name=" + var1);
         }

         this.w.lock();
         this.myAccessPoint = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setAccessPoint/20/SUCCESS");
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
            ntrace.doTrace("[/TDMRemote/setType/type=" + var1);
         } else {
            ntrace.doTrace("[/TDMRemote/setType/type=null");
         }
      }

      if (var1 != null) {
         this.myType = var1;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setType/10/SUCCESS");
         }

      }
   }

   public synchronized String getConnectionPolicy() {
      this.r.lock();

      String var1;
      try {
         if (this.myConnectionPolicy != null && !this.myConnectionPolicy.equals("LOCAL") || this.myLocalAccessPointObject == null) {
            var1 = this.myConnectionPolicy;
            return var1;
         }

         var1 = this.myLocalAccessPointObject.getConnectionPolicy();
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public String getConfigConnectionPolicy() {
      this.r.lock();

      String var1;
      try {
         if (this.myConnPolicyConfigState == 3) {
            var1 = "LOCAL";
            return var1;
         }

         var1 = this.myConnectionPolicy;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setConnectionPolicy(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMRemote/setConnectionPolicy/policy=" + var1);
         } else {
            ntrace.doTrace("[/TDMRemote/setConnectionPolicy/policy=null");
         }
      }

      if (var1 != null && !var1.equals("LOCAL")) {
         if (!var1.equals("ON_DEMAND") && !var1.equals("ON_STARTUP") && !var1.equals("INCOMING_ONLY")) {
            if (var2) {
               ntrace.doTrace("*]/TDMRemote/setConnectionPolicy/40/TPEINVAL");
            }

            throw new TPException(4, "Invalid ConnectionPolicy \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
         }

         this.setConnPolicyConfigState(1);
      } else {
         if (var1 == null) {
            this.setConnPolicyConfigState(2);
         } else {
            this.setConnPolicyConfigState(3);
         }

         if (this.myLocalAccessPointObject != null) {
            var1 = this.myLocalAccessPointObject.getConnectionPolicy();
         }
      }

      this.w.lock();
      this.myConnectionPolicy = var1;
      this.w.unlock();
      if (var2) {
         ntrace.doTrace("]/TDMRemote/setConnectionPolicy/50/SUCCESS");
      }

   }

   public int getConnPolicyConfigState() {
      this.r.lock();

      int var1;
      try {
         var1 = this.myConnPolicyConfigState;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean setConnPolicyConfigState(int var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMRemote/setConnPolicyConfigState/state=" + var1);
      }

      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 3:
            this.w.lock();
            this.myConnPolicyConfigState = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setConnPolicyConfigState/10/true");
            }

            return true;
         default:
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setConnPolicyConfigState/15/false");
            }

            return false;
      }
   }

   public String getAclPolicy() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myAclPolicy;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setAclPolicy(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAclPolicy/policy=null");
            ntrace.doTrace("]/TDMRemote/setAclPolicy/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setAclPolicy/policy=" + var1);
         }

         if (!var1.equals("LOCAL") && !var1.equals("GLOBAL")) {
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setAclPolicy/15/false");
            }

            throw new TPException(4, "Invalid AclPolicy \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
         } else {
            this.w.lock();
            this.myAclPolicy = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setAclPolicy/10/SUCCESS");
            }

         }
      }
   }

   public String getCredentialPolicy() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myCredentialPolicy;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setCredentialPolicy(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setCredentialPolicy/policy=null");
            ntrace.doTrace("]/TDMRemote/setCredentialPolicy/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setCredentialPolicy/policy=" + var1);
         }

         if (!var1.equals("LOCAL") && !var1.equals("GLOBAL")) {
            if (var2) {
               ntrace.doTrace("*]/TDMRemote/setCredentialPolicy/30/false");
            }

            throw new TPException(4, "Invalid CredentialPolicy \"" + var1 + "\" found in remote domain " + this.getAccessPointId());
         } else {
            this.w.lock();
            this.myCredentialPolicy = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setCredentialPolicy/20/SUCCESS");
            }

         }
      }
   }

   public String getTpUsrFile() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myTpUsrFile;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setTpUsrFile(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setTpUsrFile/file=" + var1);
            ntrace.doTrace("]/TDMRemote/setTpUsrFile/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setTpUsrFile/file=null");
         }

         this.w.lock();
         this.myTpUsrFile = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setTpUsrFile/20/SUCCESS");
         }

      }
   }

   public String getLocalAccessPoint() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myLocalAccessPoint;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setLocalAccessPoint(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setLocalAccessPoint/accesspoint=null");
            ntrace.doTrace("*]/TDMRemote/setLocalAccessPoint/10/TPEINVAL");
         }

         throw new TPException(4, "null LocalAccessPoint found in remote domain " + this.getAccessPointId());
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setLocalAccessPoint/accesspoint=" + var1);
         }

         this.w.lock();
         if (this.myLocalAccessPoint != null) {
            if (this.myLocalAccessPoint.equals(var1)) {
               this.w.unlock();
               if (var2) {
                  ntrace.doTrace("]/TDMRemote/setLocalAccessPoint/10/no change");
               }

               return;
            }

            if (this.myLocalAccessPointObject != null) {
               this.myLocalAccessPointObject.remove_remote_domain(this);
               this.myLocalAccessPointObject = null;
            }

            TDMLocal var3 = this.myLocalAccessPointObject;
            this.myLocalAccessPointObject = WTCService.getWTCService().getLocalDomain(var1);
            if (this.myLocalAccessPointObject == null || !this.myLocalAccessPointObject.add_remote_domain(this)) {
               this.myLocalAccessPointObject = var3;
               this.w.unlock();
               if (var2) {
                  ntrace.doTrace("*]/TDMRemote/setLocalAccessPoint/20/TPEINVAL");
               }

               throw new TPException(4, "Failed to add remote domain " + this.myAccessPoint + " to local domain " + var1);
            }
         } else {
            this.myLocalAccessPointObject = WTCService.getWTCService().getLocalDomain(var1);
            if (this.myLocalAccessPointObject == null || !this.myLocalAccessPointObject.add_remote_domain(this)) {
               this.w.unlock();
               if (var2) {
                  ntrace.doTrace("*]/TDMRemote/setLocalAccessPoint/30/TPEINVAL");
               }

               throw new TPException(4, "Failed to add remote domain " + this.myAccessPoint + " to local domain " + var1);
            }
         }

         this.myLocalAccessPoint = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setLocalAccessPoint/40/changed");
         }

      }
   }

   public TDMLocal getLocalAccessPointObject() {
      this.r.lock();

      TDMLocal var1;
      try {
         var1 = this.myLocalAccessPointObject;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setLocalAccessPointObject(TDMLocal var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setLocalAccessPointObject/lapObject=null");
            ntrace.doTrace("*]/TDMRemote/setLocalAccessPointObject/10/TPEINVAL");
         }

         throw new TPException(4, "null LocalAccessPoint found in remote domain " + this.getAccessPointId());
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setLocalAccessPointObject/accesspoint=" + var1.getAccessPoint());
         }

         this.w.lock();
         if (this.myLocalAccessPointObject != null) {
            if (this.myLocalAccessPointObject.equals(var1)) {
               this.w.unlock();
               if (var2) {
                  ntrace.doTrace("]/TDMRemote/setLocalAccessPointObject/10/no change");
               }

               return;
            }

            this.myLocalAccessPointObject.remove_remote_domain(this);
         }

         if (!var1.add_remote_domain(this)) {
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("*]/TDMRemote/setLocalAccessPoint/20/TPESYSTEM");
            }

            throw new TPException(12, "Failed to add remote domain " + this.myAccessPoint + " to local domain " + var1.getAccessPoint());
         } else {
            this.myLocalAccessPointObject = var1;
            this.w.unlock();
            if (var2) {
               ntrace.doTrace("]/TDMRemote/setLocalAccessPoint/30/SUCCESS");
            }

         }
      }
   }

   public String getConnPrincipalName() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myConnPrincipalName;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setConnPrincipalName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setConnPrincipalName/name=" + var1);
         }

         this.w.lock();
         this.myConnPrincipalName = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMRemote/setConnPrincipalName/10/SUCCESS");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMRemote/setConnPrincipalName/name=null");
            ntrace.doTrace("]/TDMRemote/setConnPrincipalName/20/FAILURE");
         }

      }
   }

   public long getRetryInterval() {
      long var1 = 0L;
      this.r.lock();
      if (this.myConnectionPolicy.equals("ON_STARTUP")) {
         var1 = this.myRetryInterval;
         if (this.myMaxRetries != 0L && var1 == -1L) {
            var1 = 60L;
         }
      } else if (this.myConnectionPolicy.equals("LOCAL") && this.myLocalAccessPointObject != null) {
         var1 = this.myLocalAccessPointObject.getRetryInterval();
      }

      this.r.unlock();
      return var1;
   }

   public void setRetryInterval(long var1) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var3) {
         ntrace.doTrace("[/TDMRemote/setRetryInterval/interval=" + var1);
      }

      if (var1 >= -1L && var1 <= 2147483647L) {
         this.w.lock();
         this.myRetryInterval = var1;
         this.w.unlock();
         if (var3) {
            ntrace.doTrace("]/TDMRemote/setRetryInterval/10/SUCCESS");
         }

      } else {
         if (var3) {
            ntrace.doTrace("*]/TDMRemote/setRetryInterval/20/TPEINVAL");
         }

         throw new TPException(4, "Invalid RetryInterval  value" + var1 + " found in remote domain " + this.getAccessPointId());
      }
   }

   public long getMaxRetries() {
      long var1 = 0L;
      this.r.lock();
      if (this.myConnectionPolicy.equals("ON_STARTUP")) {
         if ((var1 = this.myMaxRetries) == -1L) {
            var1 = Long.MAX_VALUE;
         }
      } else if (this.myConnectionPolicy.equals("LOCAL") && this.myLocalAccessPointObject != null) {
         var1 = this.myLocalAccessPointObject.getMaxRetries();
      }

      this.r.unlock();
      return var1;
   }

   public void setMaxRetries(long var1) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(16);
      if (var3) {
         ntrace.doTrace("[/TDMRemote/setMaxRetries/retries=" + var1);
      }

      if (var1 >= -1L && var1 <= Long.MAX_VALUE) {
         this.w.lock();
         this.myMaxRetries = var1;
         this.w.unlock();
         if (var3) {
            ntrace.doTrace("]/TDMRemote/setMaxRetries/10/SUCCESS");
         }

      } else {
         if (var3) {
            ntrace.doTrace("*]/TDMRemote/setMaxRetries/15/TPEINVAL");
         }

         throw new TPException(4, "Invalid MaxRetry value " + var1 + " found in remote domain " + this.getAccessPointId());
      }
   }

   public boolean equals(Object var1) {
      TDMRemote var2 = (TDMRemote)var1;
      if (this.myAccessPoint != null && this.myLocalAccessPoint != null && var2 != null) {
         TDMLocal var3 = var2.getLocalAccessPointObject();
         if (var3 != null && this.myAccessPoint.equals(var2.getAccessPoint()) && this.myLocalAccessPoint.equals(var3.getAccessPoint())) {
            return true;
         }
      }

      return false;
   }

   public synchronized int hashCode() {
      return this.myAccessPoint == null ? 0 : this.myAccessPoint.hashCode();
   }

   abstract gwatmi getTsession(boolean var1);

   abstract void setTsession(gwatmi var1);

   abstract boolean getTimedOut();
}
