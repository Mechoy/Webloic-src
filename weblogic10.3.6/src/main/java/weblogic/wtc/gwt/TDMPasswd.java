package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.configuration.WTCPasswordMBean;

public final class TDMPasswd extends WTCMBeanObject implements BeanUpdateListener {
   private String myLocalAccessPoint;
   private String myRemoteAccessPoint;
   private String myLocalIV;
   private String myLocalPasswd;
   private String myRemoteIV;
   private String myRemotePasswd;
   private WTCPasswordMBean mBean = null;
   private boolean registered = false;

   public TDMPasswd(String var1, String var2) throws Exception {
      if (var1 != null && var2 != null) {
         this.myLocalAccessPoint = var1;
         this.myRemoteAccessPoint = var2;
      } else {
         throw new Exception("LocalAccessPoint and RemoteAccessPoint may not be null");
      }
   }

   public void setLocalAccessPoint(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMPasswd/setLocalAccessPoint/" + var1);
         }

         this.w.lock();
         this.myLocalAccessPoint = var1;
         this.w.unlock();
      } else if (var2) {
         ntrace.doTrace("[/TDMPasswd/setLocalAccessPoint/null");
      }

      if (var2) {
         ntrace.doTrace("]/TDMPasswd/setLocalAccessPoint/10");
      }

   }

   public String getLocalAccessPoint() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMPasswd/getLocalAccessPoint/");
      }

      this.r.lock();

      String var2;
      try {
         if (var1) {
            ntrace.doTrace("]/TDMPasswd/getLocalAccessPoint/" + this.myLocalAccessPoint);
         }

         var2 = this.myLocalAccessPoint;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public void setRemoteAccessPoint(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMPasswd/setRemoteAccessPoint/" + var1);
         }

         this.w.lock();
         this.myRemoteAccessPoint = var1;
         this.w.unlock();
      } else if (var2) {
         ntrace.doTrace("[/TDMPasswd/setRemoteAccessPoint/null");
      }

      if (var2) {
         ntrace.doTrace("]/TDMPasswd/setRemoteAccessPoint/10");
      }

   }

   public String getRemoteAccessPoint() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMPasswd/getRemoteAccessPoint/");
      }

      this.r.lock();

      String var2;
      try {
         if (var1) {
            ntrace.doTrace("]/TDMPasswd/getRemoteAccessPoint/" + this.myRemoteAccessPoint);
         }

         var2 = this.myRemoteAccessPoint;
      } finally {
         this.r.unlock();
      }

      return var2;
   }

   public boolean equals(Object var1) {
      TDMPasswd var2 = (TDMPasswd)var1;
      if (var2 == null) {
         return false;
      } else {
         String var3;
         String var4;
         if ((var3 = var2.getLocalAccessPoint()) != null && (var4 = var2.getRemoteAccessPoint()) != null) {
            this.r.lock();
            if (this.myLocalAccessPoint != null && this.myRemoteAccessPoint != null && this.myLocalAccessPoint.equals(var3) && this.myRemoteAccessPoint.equals(var4)) {
               this.r.unlock();
               return true;
            } else {
               this.r.unlock();
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public TDMRemoteTDomain getVirtualTDomainSession() {
      TDMRemoteTDomain var1 = null;
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/getVirtualTDomainSession/");
      }

      this.r.lock();
      if (this.myLocalAccessPoint != null && this.myRemoteAccessPoint != null) {
         var1 = WTCService.getWTCService().getVTDomainSession(this.myLocalAccessPoint, this.myRemoteAccessPoint);
      }

      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("]/TDMPasswd/getVirtualTDomainSession/(" + this.myLocalAccessPoint + ", " + this.myRemoteAccessPoint + ") found");
         } else {
            ntrace.doTrace("]/TDMPasswd/getVirtualTDomainSession/(" + this.myLocalAccessPoint + ", " + this.myRemoteAccessPoint + ") not found");
         }
      }

      this.r.unlock();
      return var1;
   }

   public String getLocalPasswordIV() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myLocalIV;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setLocalPasswordIV(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/setLocalIV/iv=" + var1);
      }

      this.w.lock();
      if (var1 != null) {
         this.myLocalIV = var1;
         if (this.myRemoteIV == null) {
            this.myRemoteIV = var1;
         }

         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setLocalIV/10/changed");
         }

      } else {
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setLocalIV/15/no change");
         }

      }
   }

   public String getLocalPassword() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myLocalPasswd;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setLocalPassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/setLocalPassword/pwd=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myLocalPasswd = var1;
         if (this.myRemotePasswd == null) {
            this.myRemotePasswd = var1;
         }

         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setLocalPassword/10/changed");
         }

      } else {
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setLocalPassword/15/no change");
         }

      }
   }

   public String getRemotePasswordIV() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemoteIV;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public String getRemoteIV() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemoteIV;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setRemotePasswordIV(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/setRemoteIV/iv=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myRemoteIV = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setRemoteIV/10/changed");
         }

      } else {
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setRemoteIV/15/no change");
         }

      }
   }

   public String getRemotePassword() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemotePasswd;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public boolean setRemotePassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/setRemotePassword/pwd=" + var1);
      }

      if (var1 != null) {
         this.w.lock();
         this.myRemotePasswd = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setRemotePassword/10/changed");
         }

         return true;
      } else {
         if (var2) {
            ntrace.doTrace("]/TDMPasswd/setRemotePassword/20/no change");
         }

         return false;
      }
   }

   public WTCPasswordMBean getMBean() {
      return this.mBean;
   }

   public void setMBean(WTCPasswordMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMPasswd/setMBname/MBeanName=" + var1.getName());
         }

         if (this.mBean == var1) {
            if (var2) {
               ntrace.doTrace("]/TDMPasswd/setMBname/same, no change");
            }

            return;
         }

         if (this.mBean != null) {
            this.unregisterListener();
         }

         this.mBean = var1;
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMPasswd/setMBname/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMPasswd/setMBname/20/DONE");
      }

   }

   public int hashCode() {
      this.r.lock();
      int var1;
      if (this.myLocalAccessPoint == null) {
         var1 = 0;
      } else {
         var1 = this.myLocalAccessPoint.hashCode();
      }

      int var2;
      if (this.myRemoteAccessPoint == null) {
         var2 = 0;
      } else {
         var2 = this.myRemoteAccessPoint.hashCode();
      }

      this.r.unlock();
      return var1 + var2;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCPasswordMBean var3 = (WTCPasswordMBean)var1.getProposedBean();
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      String var8 = null;
      String var9 = null;
      boolean var10 = ntrace.isTraceEnabled(16);
      if (var10) {
         ntrace.doTrace("[/TDMPasswd/prepareUpdate");
      }

      if (var3 == null) {
         if (var10) {
            ntrace.doTrace("*]/TDMPasswd/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMPassword!");
      } else {
         String var12;
         for(int var11 = 0; var11 < var2.length; ++var11) {
            var12 = var2[var11].getPropertyName();
            int var13 = var2[var11].getUpdateType();
            if (var10) {
               ntrace.doTrace("i = " + var11 + ", optype = " + var13 + ", key = " + var12);
            }

            if (var13 == 1) {
               if (var12.equals("LocalAccessPoint")) {
                  var4 = var3.getLocalAccessPoint();
                  if (var10) {
                     ntrace.doTrace("LocalAccessPoint: " + var4);
                  }
               } else if (var12.equals("RemoteAccessPoint")) {
                  var5 = var3.getRemoteAccessPoint();
                  if (var10) {
                     ntrace.doTrace("RemoteAccessPoint: " + var5);
                  }
               } else if (var12.equals("LocalPasswordIV")) {
                  var6 = var3.getLocalPasswordIV();
                  if (var10) {
                     ntrace.doTrace("LocalPasswordIV: " + var6);
                  }
               } else if (var12.equals("LocalPassword")) {
                  var7 = var3.getLocalPassword();
                  if (var10) {
                     ntrace.doTrace("LocalPassword: " + var7);
                  }
               } else if (var12.equals("RemotePasswordIV")) {
                  var8 = var3.getRemotePasswordIV();
                  if (var10) {
                     ntrace.doTrace("RemotePasswordIV: " + var8);
                  }
               } else if (var12.equals("RemotePassword")) {
                  var9 = var3.getRemotePassword();
                  if (var10) {
                     ntrace.doTrace("RemotePassword: " + var9);
                  }
               } else if (var10) {
                  ntrace.doTrace("Key: " + var12 + " not supported, ignored!");
               }
            } else if (var13 == 2) {
               if (var10) {
                  ntrace.doTrace("Unexpected ADD operation, ignored!");
               }
            } else {
               if (var13 != 3) {
                  if (var10) {
                     ntrace.doTrace("*]/TDMPasswd/prepareUpdate/20/Unknown operation " + var13);
                  }

                  throw new BeanUpdateRejectedException("Unknown operation(" + var13 + ") for TDMPasswd.");
               }

               if (var10) {
                  ntrace.doTrace("Unexpected REMOVE operation, ignored!");
               }
            }
         }

         String var14;
         if (var4 != null) {
            var14 = var4;
         } else {
            var14 = this.myLocalAccessPoint;
         }

         if (var5 != null) {
            var12 = var5;
         } else {
            var12 = this.myRemoteAccessPoint;
         }

         if ((var4 != null || var5 != null) && WTCService.getWTCService().getVTDomainSession(var14, var12) == null) {
            if (var10) {
               ntrace.doTrace("*]/TDMPasswd/prepareUpdate/30/TDomSession(" + var14 + ", " + var12 + ") is not defined!");
            }

            throw new BeanUpdateRejectedException("TDomain Session(" + var14 + ", " + var12 + ") is not defined!");
         } else {
            this.w.lock();
            if (var4 != null) {
               this.myLocalAccessPoint = var4;
            }

            if (var5 != null) {
               this.myRemoteAccessPoint = var5;
            }

            if (var7 != null) {
               this.myLocalPasswd = var7;
            }

            if (var6 != null) {
               this.myLocalIV = var6;
            }

            if (var9 != null) {
               this.myRemotePasswd = var9;
            }

            if (var8 != null) {
               this.myRemoteIV = var8;
            }

            this.w.unlock();
            if (var10) {
               ntrace.doTrace("]/TDMPasswd/prepareUpdate/40/DONE");
            }

         }
      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/activeUpdate");
         ntrace.doTrace("]/TDMPasswd/activeUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMPasswd/rollbackUpdate");
         ntrace.doTrace("]/TDMPasswd/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMPasswd/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMPasswd: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMPasswd/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMPasswd/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMPasswd: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMPasswd/unregisterListener/10/DONE");
      }

   }
}
