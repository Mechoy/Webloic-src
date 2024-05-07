package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import java.util.StringTokenizer;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.configuration.WTCImportMBean;
import weblogic.wtc.jatmi.TPException;

public final class TDMImport extends WTCMBeanObject implements BeanUpdateListener {
   static final long serialVersionUID = -1088113749163657268L;
   private String myResourceName;
   private String myLocalAccessPoint;
   private String[] myRemoteAccessPointList;
   private TDMLocal myLocalAccessPointObject;
   private TDMRemote[] myRemoteAccessPointObjectList;
   private String myRemoteName;
   private int myTranTime;
   private WTCImportMBean mBean = null;
   private boolean registered = false;
   private boolean suspended = false;
   private WTCService myWTC;
   private String myRemoteAccessPointListString = null;

   public TDMImport(String var1, TDMLocal var2, TDMRemote[] var3) throws Exception {
      if (var1 != null && var2 != null) {
         this.myResourceName = var1;
         if (this.myRemoteName == null || this.myRemoteName.length() == 0) {
            this.myRemoteName = var1;
         }

         this.myLocalAccessPointObject = var2;
         this.myLocalAccessPoint = var2.getAccessPoint();
         int var4 = var3.length;
         this.myRemoteAccessPointList = new String[var4];

         for(int var5 = 0; var5 < var4; ++var5) {
            TDMRemote var6 = var3[var5];
            if (!this.myLocalAccessPoint.equals(var6.getLocalAccessPoint())) {
               throw new Exception("A remote access point in list not connected to the local access point");
            }

            this.myRemoteAccessPointList[var5] = var6.getAccessPoint();
         }

         this.myRemoteAccessPointObjectList = var3;
         this.myTranTime = 30;
         this.myWTC = WTCService.getWTCService();
      } else {
         throw new Exception("ResourceName and LocalAccessPoint may not be null");
      }
   }

   public String getResourceName() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myResourceName;
      } finally {
         this.r.unlock();
      }

      return var1;
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

   public String[] getRemoteAccessPointList() {
      this.r.lock();

      String[] var1;
      try {
         var1 = this.myRemoteAccessPointList;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public TDMRemote[] getRemoteAccessPointObjectList() {
      this.r.lock();

      TDMRemote[] var1;
      try {
         var1 = this.myRemoteAccessPointObjectList;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public String getPrimaryRemoteAccessPoint() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemoteAccessPointList[0];
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean hasRemoteDomain(TDMRemote var1) {
      this.r.lock();

      for(int var2 = 0; var2 < this.myRemoteAccessPointList.length; ++var2) {
         if (this.myRemoteAccessPointObjectList[var2] == var1) {
            this.r.unlock();
            return true;
         }
      }

      this.r.unlock();
      return false;
   }

   public boolean hasActiveTsession() {
      this.r.lock();

      for(int var1 = 0; var1 < this.myRemoteAccessPointList.length; ++var1) {
         if (this.myRemoteAccessPointObjectList[var1].isObjectActivated() && this.myRemoteAccessPointObjectList[var1].getTsession(false) != null) {
            this.r.unlock();
            return true;
         }
      }

      this.r.unlock();
      return false;
   }

   public boolean hasDynamicRemotePolicy() {
      this.r.lock();

      for(int var1 = 0; var1 < this.myRemoteAccessPointList.length; ++var1) {
         String var2;
         if ((var2 = this.myRemoteAccessPointObjectList[var1].getConnectionPolicy()) != null && (var2.equals("ON_STARTUP") || var2.equals("INCOMING_ONLY"))) {
            this.r.unlock();
            return true;
         }
      }

      this.r.unlock();
      return false;
   }

   public String getRemoteName() {
      this.r.lock();

      String var1;
      try {
         if (this.myRemoteName != null && this.myRemoteName.length() != 0) {
            var1 = this.myRemoteName;
            return var1;
         }

         var1 = this.myResourceName;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setRemoteName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMImport/setRemoteName/name=" + var1);
      }

      if (var1 == null && var1.length() == 0) {
         if (var2) {
            ntrace.doTrace("]/TDMImport/setRemoteName/15/not changed");
         }

      } else {
         this.w.lock();
         this.myRemoteName = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMImport/setRemoteName/10/changed");
         }

      }
   }

   public int getTranTime() {
      this.r.lock();

      int var1;
      try {
         var1 = this.myTranTime;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public WTCImportMBean getMBean() {
      return this.mBean;
   }

   public void setTranTime(int var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMImport/setTranTime/time=" + var1);
      }

      if (var1 >= 0 && var1 <= Integer.MAX_VALUE) {
         this.w.lock();
         this.myTranTime = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMImport/setTranTime/10/changed");
         }

      } else {
         if (var2) {
            ntrace.doTrace("]/TDMImport/setTranTime/15/no change");
         }

      }
   }

   public void setResourceName(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null && var1.length() != 0) {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setResourceName/ResourceName=" + var1);
         }

         String var3 = this.myResourceName;
         this.myResourceName = var1;
         this.myWTC.changeImportResourceName(this, var3);
         if (var2) {
            ntrace.doTrace("]/TDMImport/setResourceName/20/DONE");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setResourceName/ResourceName=null");
            ntrace.doTrace("*]/TDMImport/setResourceName/10/TPEINVAL");
         }

         throw new TPException(4, "ResourceName may not be null");
      }
   }

   public void setMBean(WTCImportMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setMBname/MBeanName=" + var1.getName());
         }

         if (this.mBean != null) {
            if (this.mBean == var1) {
               if (var2) {
                  ntrace.doTrace("]/TDMImport/setMBname/10/same, no change");
               }

               return;
            }

            this.unregisterListener();
         }

         this.mBean = var1;
         if (var2) {
            ntrace.doTrace("]/TDMImport/setMBname/20/changed");
         }
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setMBname/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }

         if (var2) {
            ntrace.doTrace("]/TDMImport/setMBname/30/");
         }
      }

   }

   public void setLocalAccessPoint(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null && var1.length() != 0) {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setLocalAccessPoint/" + var1);
         }

         this.w.lock();
         this.myLocalAccessPointObject = this.myWTC.getLocalDomain(var1);
         this.myLocalAccessPoint = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMImport/setLocalAccessPoint/20/DONE");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setLocalAccessPoint/null");
            ntrace.doTrace("*]/TDMImport/setLocalAccessPoint/10");
         }

         throw new TPException(4, "LocalAccessPoint may not be null");
      }
   }

   public void setRemoteAccessPointList(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null && var1.length() != 0) {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setRemoteAccessPointList/" + var1);
         }

         TDMRemoteTDomain var3;
         TDMRemoteTDomain[] var4;
         String[] var5;
         if (var1.indexOf(44) == -1) {
            var3 = this.myWTC.getRTDbyAP(var1);
            if (var3 == null) {
               if (var2) {
                  ntrace.doTrace("*]/TDMImport/setRemoteAccessPointList/20/Unknown RTD name " + var1);
               }

               throw new TPException(4, "Unknown Remote TDomain name " + var1);
            }

            var4 = new TDMRemoteTDomain[1];
            var5 = new String[1];
            var4[0] = var3;
            var5[0] = var1;
         } else {
            StringTokenizer var6 = new StringTokenizer(var1, ",");
            var4 = new TDMRemoteTDomain[var6.countTokens()];
            var5 = new String[var6.countTokens()];

            for(int var7 = 0; var6.hasMoreTokens(); ++var7) {
               if (var7 > 2) {
                  if (var2) {
                     ntrace.doTrace("*]/TDMImport/setRemoteAccessPointList/40/list size limited to three");
                  }

                  throw new TPException(4, "Remote access point list cannot have more than three elements");
               }

               String var8 = var6.nextToken();
               var3 = this.myWTC.getRTDbyAP(var8);
               if (var3 == null) {
                  if (var2) {
                     ntrace.doTrace("*]/TDMImport/setRemoteAccessPointList/50/Known RTDs list does not include " + var8);
                  }

                  throw new TPException(4, "Known RTDs list does not include " + var8);
               }

               var4[var7] = var3;
               var5[var7] = var8;
            }
         }

         this.w.lock();
         this.myRemoteAccessPointObjectList = var4;
         this.myRemoteAccessPointList = var5;
         this.myRemoteAccessPointListString = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMImport/setRemoteAccessPointList/60/DONE");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMImport/setRemoteAccessPointList/null");
            ntrace.doTrace("*]/TDMImport/setRemoteAccessPointList/10/List is NULL");
         }

         throw new TPException(4, "RemoteAccessPointList can not be null");
      }
   }

   public boolean equals(Object var1) {
      TDMImport var2 = (TDMImport)var1;
      if (var2 == this) {
         return true;
      } else {
         this.r.lock();
         String var3;
         String var4;
         String[] var5;
         if (var2 != null && (var3 = var2.getResourceName()) != null && (var4 = var2.getLocalAccessPoint()) != null && (var5 = var2.getRemoteAccessPointList()) != null && var5.length == this.myRemoteAccessPointList.length) {
            if (var3.equals(this.myResourceName) && var4.equals(this.myLocalAccessPoint)) {
               for(int var7 = 0; var7 < this.myRemoteAccessPointList.length; ++var7) {
                  if (!this.myRemoteAccessPointList[var7].equals(var5[var7])) {
                     this.r.unlock();
                     return false;
                  }
               }

               this.r.unlock();
               return true;
            } else {
               this.r.unlock();
               return false;
            }
         } else {
            this.r.unlock();
            return false;
         }
      }
   }

   public int hashCode() {
      this.r.lock();
      int var1;
      if (this.myResourceName == null) {
         var1 = 0;
      } else {
         var1 = this.myResourceName.hashCode();
      }

      int var2;
      if (this.myLocalAccessPoint == null) {
         var2 = 0;
      } else {
         var2 = this.myLocalAccessPoint.hashCode();
      }

      int var3 = 0;
      if (this.myRemoteAccessPointList != null) {
         int var4 = this.myRemoteAccessPointList.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var3 += this.myRemoteAccessPointList[var5].hashCode();
         }
      }

      this.r.unlock();
      return var1 + var2 + var3;
   }

   public void setRemoteAccessPointListString(String var1) {
      this.myRemoteAccessPointListString = var1;
   }

   public static String[] parseCommaSeparatedList(String var0) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMImport/parseCommaSeparatedList/string = " + var0);
      }

      String[] var1;
      if (var0.indexOf(44) == -1) {
         var1 = new String[]{var0};
      } else {
         StringTokenizer var3 = new StringTokenizer(var0, ",");
         var1 = new String[var3.countTokens()];

         for(int var4 = 0; var3.hasMoreTokens(); ++var4) {
            var1[var4] = var3.nextToken();
            if (var2) {
               ntrace.doTrace("Token " + var4 + ":" + var1[var4]);
            }
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMImport/parseCommaSeparateList/10/DONE");
      }

      return var1;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCImportMBean var3 = (WTCImportMBean)var1.getProposedBean();
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      boolean var8 = ntrace.isTraceEnabled(16);
      if (var8) {
         ntrace.doTrace("[/TDMImport/prepareUpdate");
      }

      if (var3 == null) {
         if (var8) {
            ntrace.doTrace("*]/TDMImport/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMImport!");
      } else {
         String var10;
         for(int var9 = 0; var9 < var2.length; ++var9) {
            var10 = var2[var9].getPropertyName();
            int var11 = var2[var9].getUpdateType();
            if (var8) {
               ntrace.doTrace("i = " + var9 + ", opType = " + var11 + ", key = " + var10);
            }

            if (var11 == 1) {
               if (var10.equals("ResourceName")) {
                  var4 = var3.getResourceName();
                  if (var4.length() == 0) {
                     var4 = null;
                  }

                  if (var8) {
                     ntrace.doTrace("Resource Name:" + var4);
                  }
               } else if (var10.equals("LocalAccessPoint")) {
                  var5 = var3.getLocalAccessPoint();
                  if (var5.length() == 0) {
                     var5 = null;
                  }

                  if (var8) {
                     ntrace.doTrace("Local AP:" + var5);
                  }
               } else if (var10.equals("RemoteAccessPointList")) {
                  var6 = var3.getRemoteAccessPointList();
                  if (var6.length() == 0) {
                     var6 = null;
                  }

                  if (var8) {
                     ntrace.doTrace("Local AP:" + var6);
                  }
               } else if (var10.equals("RemoteName")) {
                  var7 = var3.getRemoteName();
                  if (var7.length() == 0) {
                     var7 = null;
                  }

                  if (var8) {
                     ntrace.doTrace("Remote Name:" + var7);
                  }
               } else if (var8) {
                  ntrace.doTrace("Key " + var10 + " not supported, ignored!");
               }
            } else if (var11 == 2) {
               if (var8) {
                  ntrace.doTrace("Unexpected ADD operation, ignored!");
               }
            } else {
               if (var11 != 3) {
                  if (var8) {
                     ntrace.doTrace("*]/TDMImport/prepareUpdate/20/Unknown operation " + var11);
                  }

                  throw new BeanUpdateRejectedException("Unknown operation(" + var11 + ") for TDMImport.");
               }

               if (var8) {
                  ntrace.doTrace("Unexpected REMOVE operation, ignored!");
               }
            }
         }

         Object var16 = null;
         var10 = null;
         if (var5 != null && !var5.equals(this.myLocalAccessPoint)) {
            var16 = this.myWTC.getLocalDomain(var5);
            if (var16 == null) {
               if (var8) {
                  ntrace.doTrace("*]/TDMImport/prepareUpdate/30/LAP " + var5 + " is not configured!");
               }

               throw new BeanUpdateRejectedException("Unknown local access point " + var5);
            }

            var10 = var5;
         } else {
            var16 = this.myLocalAccessPointObject;
            var10 = this.myLocalAccessPoint;
         }

         TDMRemoteTDomain[] var12 = (TDMRemoteTDomain[])((TDMRemoteTDomain[])this.myRemoteAccessPointObjectList);
         String[] var13;
         if (var6 != null) {
            var13 = parseCommaSeparatedList(var6);
            if (var13.length == this.myRemoteAccessPointList.length) {
               int var14;
               for(var14 = 0; var14 < var13.length && var13[var14].equals(this.myRemoteAccessPointList[var14]); ++var14) {
               }

               if (var14 == var13.length) {
                  var13 = this.myRemoteAccessPointList;
               } else {
                  var12 = new TDMRemoteTDomain[var13.length];

                  for(var14 = 0; var14 < var13.length; ++var14) {
                     if ((var12[var14] = this.myWTC.getVTDomainSession(var10, var13[var14])) == null) {
                        if (var8) {
                           ntrace.doTrace("*]/TDMImport/prepareUpdate/40/Imported svc " + var4 + " is configured for a " + "existing TDomain session(" + var10 + ", " + var13[var14] + ")!");
                        }

                        throw new BeanUpdateRejectedException("Remote TDomain Session(" + var10 + ", " + var13[var14] + ") not defined.");
                     }
                  }
               }
            }
         } else {
            var13 = this.myRemoteAccessPointList;
         }

         if (var4 != null) {
            TDMImport var15 = this.myWTC.getImport(var4, var10, var13[0]);
            if (var15 != null && var15 != this) {
               if (var8) {
                  ntrace.doTrace("*]/TDMImport/prepareUpdate/50/Imported svc " + var4 + " already configured!");
               }

               throw new BeanUpdateRejectedException("Imported resouce " + var4 + " already exists for TDomain session(" + var10 + ", " + var13[0] + ")!");
            }
         }

         this.w.lock();
         if (var4 != null) {
            String var17 = this.myResourceName;
            this.myResourceName = var4;
            this.myWTC.changeImportResourceName(this, var17);
         }

         if (var5 != null) {
            this.myLocalAccessPointObject = (TDMLocal)var16;
            this.myLocalAccessPoint = var10;
         }

         if (var13 != this.myRemoteAccessPointList) {
            this.myRemoteAccessPointList = var13;
            this.myRemoteAccessPointObjectList = (TDMRemote[])var12;
            this.myRemoteAccessPointListString = var6;
         }

         if (var7 != null) {
            this.myRemoteName = var7;
         }

         this.w.unlock();
         if (var8) {
            ntrace.doTrace("]/TDMImport/prepareUpdate/60/DONE");
         }

      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMImport/activeUpdate");
         ntrace.doTrace("]/TDMImport/activeUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMImport/rollbackUpdate");
         ntrace.doTrace("]/TDMImport/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMImport/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMimport: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMImport/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMImport/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMImport: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMImport/unregisterListener/10/DONE");
      }

   }

   public void suspend() {
      this.suspended = true;
   }

   public void resume() {
      this.suspended = false;
   }

   public int getStatus() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMImport/getStatus/");
      }

      if (this.suspended) {
         if (var1) {
            ntrace.doTrace("]/TDMImport/getStatus/10/SUSPENDED");
         }

         return 1;
      } else if (!this.hasActiveTsession() && this.hasDynamicRemotePolicy()) {
         if (var1) {
            ntrace.doTrace("]/TDMImport/getStatus/40/UNAVAILABLE");
         }

         return 2;
      } else {
         if (var1) {
            ntrace.doTrace("]/TDMImport/getStatus/30/AVAILABLE");
         }

         return 3;
      }
   }

   public boolean match(String var1, String var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/TDMImport/match/lap = " + var1 + ", raplist = " + var2);
      }

      if (this.myLocalAccessPoint.equals(var1)) {
         if (var2 == null) {
            if (var3) {
               ntrace.doTrace("]/TDMImport/match/10/true");
            }

            return true;
         }

         if (this.myRemoteAccessPointListString != null && this.myRemoteAccessPointListString.equals(var2)) {
            if (var3) {
               ntrace.doTrace("]/TDMImport/match/20/true");
            }

            return true;
         }
      }

      if (var3) {
         ntrace.doTrace("]/TDMImport/match/30/false");
      }

      return false;
   }

   public DServiceInfo getServiceInfo() {
      return new DServiceInfo(this.myResourceName, this.myLocalAccessPoint, 1, this.getStatus());
   }
}
