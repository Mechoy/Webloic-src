package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.configuration.WTCExportMBean;
import weblogic.wtc.jatmi.TPException;

public final class TDMExport extends WTCMBeanObject implements BeanUpdateListener {
   static final long serialVersionUID = -2363046738928971863L;
   private String myResourceName;
   private String myLocalAccessPoint;
   private TDMLocal myLocalAccessPointObject;
   private String myRemoteName;
   private String myEJBName;
   private String myTargetClass;
   private String myTargetJar;
   private WTCExportMBean mBean = null;
   private boolean registered = false;
   private WTCService myWTC;
   private boolean suspended = false;

   public TDMExport(String var1, TDMLocal var2) throws Exception {
      if (var1 != null && var2 != null && var1.length() != 0) {
         this.myResourceName = var1;
         this.myLocalAccessPoint = var2.getAccessPoint();
         this.myLocalAccessPointObject = var2;
         if (this.myRemoteName == null || this.myRemoteName.length() == 0) {
            this.myRemoteName = var1;
         }

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

   public String getEJBName() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myEJBName;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean setEJBName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMExport/setEJBName/name=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myEJBName = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMExport/setEJBName/10/true");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMExport/setEJBName/15/false");
         }

         return false;
      }
   }

   public String getTargetJar() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myTargetJar;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean setTargetJar(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMExport/setTargetJar/name=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myTargetJar = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMExport/setTargetJar/10/true");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMExport/setTargetJar/20/false");
         }

         return false;
      }
   }

   public String getTargetClass() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myTargetClass;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean setTargetClass(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMExport/setTargetClass/name=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myTargetClass = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMExport/setTargetClass/10/true");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMExport/setTargetClass/20/false");
         }

         return false;
      }
   }

   public String getRemoteName() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemoteName;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public WTCExportMBean getMBean() {
      return this.mBean;
   }

   public void setRemoteName(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMExport/setRemoteName/name=" + var1);
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         if (var1.length() == 0) {
            var1 = this.myResourceName;
         }

         String var3 = this.myRemoteName;
         this.myRemoteName = var1;
         this.myWTC.changeExportResourceName(this, var3);
         this.w.unlock();
      }

      if (var2) {
         ntrace.doTrace("]/TDMExport/setRemoteName/10/true");
      }

   }

   public void setResourceName(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null && var1.length() != 0) {
         if (var2) {
            ntrace.doTrace("[/TDMExport/setResourceName/ResourceName=" + var1);
         }

         this.w.lock();
         this.myResourceName = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMExport/setResourceName/20/DONE");
         }

      } else {
         if (var2) {
            ntrace.doTrace("[/TDMExport/setResourceName/ResourceName=null");
         }

         throw new TPException(4, "ResourceName may not be null");
      }
   }

   public void setLocalAccessPoint(String var1) throws TPException {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/TDMExport/setLocalAccessPoint/" + var1);
         } else {
            ntrace.doTrace("[/TDMExport/setLocalAccessPoint/null");
         }
      }

      if (var1 != null && var1.length() != 0) {
         TDMLocalTDomain var3 = this.myWTC.getLocalDomain(var1);
         this.w.lock();
         this.myLocalAccessPoint = var1;
         this.myLocalAccessPointObject = var3;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMExport/setLocalAccessPoint/20/DONE");
         }

      } else {
         throw new TPException(4, "LocalAccessPoint may not be null");
      }
   }

   public void setMBean(WTCExportMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMExport/setMBean/MBeanName=" + var1.getName());
         }

         if (this.mBean != null) {
            if (this.mBean == var1) {
               if (var2) {
                  ntrace.doTrace("]/TDMExport/setMBname/same, no change");
               }

               return;
            }

            this.unregisterListener();
         }

         this.mBean = var1;
         if (this.myRemoteName == null && this.myResourceName != null) {
            this.myRemoteName = this.myResourceName;
         }

         if (var2) {
            ntrace.doTrace("]/TDMExport/setMBname/20/change");
         }
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMExport/setMBname/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }

         if (var2) {
            ntrace.doTrace("]/TDMExport/setMBname/30/");
         }
      }

   }

   public boolean equals(Object var1) {
      TDMExport var2 = (TDMExport)var1;
      this.r.lock();
      if (this.myLocalAccessPoint != null && this.myResourceName != null && this.myRemoteName != null && var2 != null && this.myLocalAccessPoint.equals(var2.getLocalAccessPoint()) && this.myRemoteName.equals(var2.getRemoteName()) && this.myResourceName.equals(var2.getResourceName())) {
         this.r.unlock();
         return true;
      } else {
         this.r.unlock();
         return false;
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

      int var3;
      if (this.myRemoteName == null) {
         var3 = 0;
      } else {
         var3 = this.myRemoteName.hashCode();
      }

      this.r.unlock();
      return var1 + var2 + var3;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCExportMBean var3 = (WTCExportMBean)var1.getProposedBean();
      boolean var4 = ntrace.isTraceEnabled(16);
      String var5 = null;
      String var6 = null;
      String var7 = null;
      String var8 = null;
      String var9 = this.myRemoteName;
      boolean var10 = false;
      if (var4) {
         ntrace.doTrace("[/TDMExport/prepareUpdate");
      }

      if (var3 == null) {
         if (var4) {
            ntrace.doTrace("*]/TDMExport/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMExport!");
      } else {
         String var12;
         for(int var11 = 0; var11 < var2.length; ++var11) {
            var12 = var2[var11].getPropertyName();
            int var13 = var2[var11].getUpdateType();
            if (var4) {
               ntrace.doTrace("i = " + var11 + ", optype = " + var13 + ", key = " + var12);
            }

            if (var13 == 1) {
               if (var12.equals("ResourceName")) {
                  var5 = var3.getResourceName();
                  if (var5.length() == 0) {
                     var5 = null;
                  }

                  if (var4) {
                     ntrace.doTrace("Resource Name: " + var5);
                  }
               } else if (var12.equals("LocalAccessPoint")) {
                  var6 = var3.getLocalAccessPoint();
                  if (var6.length() == 0) {
                     var6 = null;
                  }

                  if (var4) {
                     ntrace.doTrace("Local AP: " + var6);
                  }
               } else if (var12.equals("RemoteName")) {
                  var7 = var3.getRemoteName();
                  if (var7.length() == 0) {
                     var7 = null;
                     var10 = true;
                  }

                  if (var4) {
                     ntrace.doTrace("Remote Name: " + var7);
                  }
               } else if (var12.equals("EJBName")) {
                  var8 = var3.getEJBName();
                  if (var8.length() == 0) {
                     var8 = null;
                  }

                  if (var4) {
                     ntrace.doTrace("EJB Name: " + var8);
                  }
               } else if (var4) {
                  ntrace.doTrace("key: " + var12 + "not supported, ignored!");
               }
            } else if (var13 == 2) {
               if (var4) {
                  ntrace.doTrace("Unexpected ADD operation, ignored!");
               }
            } else if (var13 == 3 && var4) {
               ntrace.doTrace("Unexpected REMOVE operation, ignored!");
            }
         }

         TDMLocalTDomain var15 = null;
         if (var6 != null && !var6.equals(this.myLocalAccessPoint)) {
            var15 = this.myWTC.getLocalDomain(var6);
            if (var15 == null) {
               if (var4) {
                  ntrace.doTrace("*]/TDMExport/prepareUpdate/30/LAP " + var6 + " is not configured!");
               }

               throw new BeanUpdateRejectedException("Unknow local access point " + var6);
            }

            var12 = var6;
         } else {
            var12 = this.myLocalAccessPoint;
         }

         if (var10) {
            if (var5 != null) {
               var7 = var5;
            } else if (this.myResourceName != null) {
               var7 = this.myResourceName;
            }
         }

         if (var7 != null) {
            TDMExport var16 = this.myWTC.getExportedService(var7, var12);
            if (var16 != null && var16 != this) {
               if (var4) {
                  ntrace.doTrace("*]/TDMExport/prepareUpdate/40/duplicated rname: " + var7 + " for LAP:" + var12);
               }

               String var14 = var5 == null ? this.myResourceName : var5;
               throw new BeanUpdateRejectedException("Ambiguous or duplicated exported service: " + var14 + ", remote Name:" + var7);
            }
         }

         this.w.lock();
         if (var5 != null) {
            this.myResourceName = var5;
         }

         if (var7 != null) {
            this.myRemoteName = var7;
            if (var9 != null && !var7.equals(var9)) {
               this.myWTC.changeExportResourceName(this, var9);
            }
         }

         if (var6 != null) {
            this.myLocalAccessPoint = var6;
            this.myLocalAccessPointObject = var15;
         }

         if (var8 != null) {
            this.myEJBName = var8;
         }

         this.w.unlock();
         if (var4) {
            ntrace.doTrace("]/TDMExport/prepareUpdate/50/DONE");
         }

      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMExport/activeUpdate");
         ntrace.doTrace("]/TDMExport/activeUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMExport/rollbackUpdate");
         ntrace.doTrace("]/TDMExport/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMExport/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMExport: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMExport/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMExport/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMExport: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMExport/unregisterListener/10/DONE");
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
         ntrace.doTrace("[/TDMExport/getStatus/");
      }

      if (this.suspended) {
         if (var1) {
            ntrace.doTrace("]/TDMExport/getStatus/10/SUSPENDED");
         }

         return 1;
      } else {
         if (var1) {
            ntrace.doTrace("]/TDMExport/getStatus/20/AVAILABLE");
         }

         return 3;
      }
   }

   public boolean match(String var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMExport/match/lap = " + var1);
      }

      if (this.myLocalAccessPoint.equals(var1)) {
         if (var2) {
            ntrace.doTrace("]/TDMExport/getResourceName/10/false");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMExport/getResourceName/20/false");
         }

         return false;
      }
   }

   public DServiceInfo getServiceInfo() {
      return new DServiceInfo(this.myResourceName, this.myLocalAccessPoint, 2, this.getStatus());
   }
}
