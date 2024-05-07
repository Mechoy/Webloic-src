package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import java.util.ArrayList;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCResourcesMBean;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.config.WTCResources;
import weblogic.wtc.jatmi.FldTbl;
import weblogic.wtc.jatmi.PasswordUtils;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.ViewHelper;

public final class TDMResources extends WTCMBeanObject implements BeanUpdateListener {
   static final long serialVersionUID = 7822438947350357255L;
   private static TDMResources myGlobalResources = null;
   private static String key = null;
   private static String encType = null;
   private static String myName = null;
   private String myAppPasswordIV;
   private String myAppPassword;
   private String myTpUsrFile;
   private FldTbl[] myFldTbl16Classes;
   private FldTbl[] myFldTbl32Classes;
   private WTCResourcesMBean mBean;
   private String myRemoteMBEncoding;
   private String myMBEncodingMapFile;
   private boolean registered = false;
   private String[] myFtbl16;
   private String[] myFtbl32;
   private String[] myVtbl16;
   private String[] myVtbl32;

   public TDMResources() {
      myGlobalResources = this;
      this.myAppPasswordIV = null;
      this.myAppPassword = null;
      this.myTpUsrFile = null;
      this.myFldTbl16Classes = null;
      this.myFldTbl32Classes = null;
      this.myRemoteMBEncoding = null;
      this.myMBEncodingMapFile = null;
      this.mBean = null;
      key = System.getProperty("weblogic.wtc.PasswordKey");
      encType = System.getProperty("weblogic.wtc.EncryptionType");
   }

   public String getAppPasswordIV() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myAppPasswordIV;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setAppPasswordIV(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMResources/setAppPasswordIV/iv=" + var1);
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         this.myAppPasswordIV = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMResources/setAppPasswordIV/10/set");
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setAppPasswordIV/15/DONE");
      }

   }

   public String getAppPassword() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myAppPassword;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setAppPassword(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMResources/setAppPassword/pwd=" + var1);
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         this.myAppPassword = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMResources/setAppPassword/10/set");
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setAppPassword/15/DONE");
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

   public void setTpUsrFile(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 == null) {
            ntrace.doTrace("[/TDMResources/setTpUsrFile/file=null");
         } else {
            ntrace.doTrace("[/TDMResources/setTpUsrFile/file=" + var1);
         }
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         this.myTpUsrFile = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMResources/setTpUsrFile/10/set");
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setTpUsrFile/20/DONE");
      }

   }

   public String getRemoteMBEncoding() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myRemoteMBEncoding;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setRemoteMBEncoding(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 == null) {
            ntrace.doTrace("[/TDMResources/setRemoteMBEncoding/null");
         } else {
            ntrace.doTrace("[/TDMResources/setRemoteMBEncoding/encoding=" + var1);
         }
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         this.myRemoteMBEncoding = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMResources/setRemoteMBEncoding/10/set");
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setRemoteMBEncoding/20/DONE");
      }

   }

   public String getMBEncodingMapFile() {
      this.r.lock();

      String var1;
      try {
         var1 = this.myMBEncodingMapFile;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMBEncodingMapFile(String var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         if (var1 == null) {
            ntrace.doTrace("[/TDMResources/setMBEncodingMapFile/null");
         } else {
            ntrace.doTrace("[/TDMResources/setMBEncodingMapFile/mapFile=" + var1);
         }
      }

      if (var1 != null && var1.length() != 0) {
         this.w.lock();
         this.myMBEncodingMapFile = var1;
         this.w.unlock();
         if (var2) {
            ntrace.doTrace("]/TDMResources/setMBEncodingMapFile/10/set");
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setMBEncodingMapFile/20/DONE");
      }

   }

   public FldTbl[] getFieldTables(boolean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/TDMResources/getFieldTables/type32=" + var1);
      }

      this.r.lock();

      FldTbl[] var3;
      try {
         if (!var1) {
            if (var2) {
               ntrace.doTrace("[/TDMResources/getFieldTables/20/DONE");
            }

            var3 = this.myFldTbl16Classes;
            return var3;
         }

         if (var2) {
            ntrace.doTrace("[/TDMResources/getFieldTables/10/DONE");
         }

         var3 = this.myFldTbl32Classes;
      } finally {
         this.r.unlock();
      }

      return var3;
   }

   public void setFldTbl32Classes(String[] var1) throws TPException {
      this.setFieldTables(var1, true);
   }

   public void setFldTbl16Classes(String[] var1) throws TPException {
      this.setFieldTables(var1, false);
   }

   public void setFieldTables(String[] var1, boolean var2) throws TPException {
      boolean var3 = false;
      boolean var4 = ntrace.isTraceEnabled(16);
      if (var4) {
         if (var1 == null) {
            ntrace.doTrace("[/TDMResources/setFieldTables/FldTblNames=null");
         } else {
            ntrace.doTrace("[/TDMResources/setFieldTables/FldTblNames=" + var1 + " type32=" + var2);
         }
      }

      if (var1 != null && var1.length != 0) {
         if (var2) {
            this.myFtbl32 = var1;
         } else {
            this.myFtbl16 = var1;
         }

         if (var4) {
            ntrace.doTrace("FldTblNames = " + var1.length);
         }

         int var5 = 0;

         int var11;
         for(var11 = 0; var11 < var1.length; ++var11) {
            if (var1[var11] != null && var1[var11].length() != 0) {
               ++var5;
            } else if (var4) {
               ntrace.doTrace("skip " + var11);
            }
         }

         if (var4) {
            ntrace.doTrace("count = " + var5);
         }

         if (var5 == 0) {
            if (var2) {
               this.myFldTbl32Classes = null;
            } else {
               this.myFldTbl16Classes = null;
            }

            if (var4) {
               ntrace.doTrace("]/TDMResources/setFieldTables/20/not set");
            }

         } else {
            this.w.lock();
            if (var2) {
               this.myFldTbl32Classes = new FldTbl[var5];
            } else {
               this.myFldTbl16Classes = new FldTbl[var5];
            }

            String var6 = null;
            int var8 = 0;

            for(var11 = 0; var11 < var1.length; ++var11) {
               if (var1[var11] != null && var1[var11].length() != 0) {
                  var6 = var1[var11];

                  try {
                     Class var7 = Class.forName(var6);
                     if (var2) {
                        this.myFldTbl32Classes[var8] = (FldTbl)var7.newInstance();
                     } else {
                        this.myFldTbl16Classes[var8] = (FldTbl)var7.newInstance();
                     }

                     ++var8;
                  } catch (Exception var10) {
                     this.w.unlock();
                     if (var4) {
                        ntrace.doTrace("/TDMResources/setFieldTables/30/Classloader problem with: " + var6);
                     }

                     throw new TPException(4, "Class loader problem with: " + var6);
                  }
               }
            }

            this.w.unlock();
            if (var4) {
               ntrace.doTrace("]/TDMResources/setFieldTables/40/DONE");
            }

         }
      } else {
         if (var2) {
            this.myFldTbl32Classes = null;
         } else {
            this.myFldTbl16Classes = null;
         }

         if (var4) {
            ntrace.doTrace("]/TDMResources/setFieldTables/10/not set");
         }

      }
   }

   public void setViewTbl32Classes(String[] var1) throws TPException {
      this.setViewTables(var1, true);
   }

   public void setViewTbl16Classes(String[] var1) throws TPException {
      this.setViewTables(var1, false);
   }

   public void setViewTables(String[] var1, boolean var2) throws TPException {
      boolean var3 = false;
      boolean var4 = ntrace.isTraceEnabled(16);
      if (var4) {
         if (var1 == null) {
            ntrace.doTrace("[/TDMResources/setViewTables/ViewTblNames=null");
         } else {
            ntrace.doTrace("[/TDMResources/setViewTables/ViewTblNames=" + var1 + " type32=" + var2);
         }
      }

      if (var1 != null && var1.length != 0) {
         if (var2) {
            this.myVtbl32 = var1;
         } else {
            this.myVtbl16 = var1;
         }

         ViewHelper var5 = new ViewHelper();
         ViewHelper var6 = ViewHelper.getInstance();
         String var7 = null;
         String var8 = null;

         for(int var11 = 0; var11 < var1.length; ++var11) {
            if (var1[var11] != null && var1[var11].length() != 0) {
               var8 = var1[var11];

               try {
                  var7 = var8.substring(var8.lastIndexOf(46) + 1);
                  var6.setViewClass(var7, var8);
               } catch (Exception var10) {
                  if (var4) {
                     ntrace.doTrace("*]/TDMResources/setViewTables/30/Class loader problem with: " + var8);
                  }

                  throw new TPException(4, "Class loader problem with: " + var8);
               }
            }
         }

         if (var4) {
            ntrace.doTrace("]/TDMResources/setViewTables/40/DONE");
         }

      } else {
         if (var4) {
            ntrace.doTrace("]/TDMResources/setViewTables/10/not set");
         }

      }
   }

   public WTCResourcesMBean getMBean() {
      this.r.lock();

      WTCResourcesMBean var1;
      try {
         var1 = this.mBean;
      } finally {
         this.r.unlock();
      }

      return var1;
   }

   public void setMBean(WTCResourcesMBean var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var1 != null) {
         if (var2) {
            ntrace.doTrace("[/TDMResources/setMBean/MBeanName=" + var1.getName());
         }

         if (this.mBean != null) {
            if (this.mBean == var1) {
               if (var2) {
                  ntrace.doTrace("]/TDMResources/setMBean/10/same, no change");
               }

               return;
            }

            this.unregisterListener();
         }

         this.mBean = var1;
      } else {
         if (var2) {
            ntrace.doTrace("[/TDMResources/setMBean/MBeanName=null");
         }

         if (this.mBean != null) {
            this.unregisterListener();
            this.mBean = null;
         }
      }

      if (var2) {
         ntrace.doTrace("]/TDMResources/setMBean/20/DONE");
      }

   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      WTCResourcesMBean var3 = (WTCResourcesMBean)var1.getProposedBean();
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String[] var7 = null;
      String[] var8 = null;
      String[] var9 = null;
      String[] var10 = null;
      String var11 = null;
      String var12 = null;
      boolean var14 = ntrace.isTraceEnabled(16);
      boolean var15 = false;
      boolean var16 = false;
      if (var14) {
         ntrace.doTrace("[/TDMResources/prepareUpdate");
      }

      if (var3 == null) {
         if (var14) {
            ntrace.doTrace("*]/TDMResources/prepareUpdate/10/null MBean");
         }

         throw new BeanUpdateRejectedException("A null MBean for TDMResources class!");
      } else {
         for(int var13 = 0; var13 < var2.length; ++var13) {
            String var17 = var2[var13].getPropertyName();
            int var18 = var2[var13].getUpdateType();
            if (var14) {
               ntrace.doTrace("i = " + var13 + ", optype = " + var18 + ", key = " + var17);
            }

            if (var18 != 1 && var18 != 2) {
               if (var18 != 3) {
                  if (var14) {
                     ntrace.doTrace("*]/TDMResources/prepareUpdate/20/Unknown operation " + var18);
                  }

                  throw new BeanUpdateRejectedException("Unknown operation(" + var18 + ") for TDMResources.");
               }

               if (var14) {
                  ntrace.doTrace("REMOVE operation, deactivate it!");
               }

               this.setMBean((WTCResourcesMBean)null);
               this.myAppPasswordIV = null;
               this.myAppPassword = null;
               this.myTpUsrFile = null;
               this.myFldTbl16Classes = null;
               this.myFldTbl32Classes = null;
               this.myRemoteMBEncoding = null;
               this.myMBEncodingMapFile = null;
               var16 = true;
            } else {
               if (var18 == 2) {
                  var15 = true;
               }

               if (var17.equals("FldTbl16Classes")) {
                  var7 = var3.getFldTbl16Classes();
                  if (var14) {
                     for(var13 = 0; var13 < var7.length; ++var13) {
                        ntrace.doTrace("FldTbl16Class[" + var13 + "] = " + var7[var13]);
                     }
                  }
               } else if (var17.equals("FldTbl32Classes")) {
                  var8 = var3.getFldTbl32Classes();
                  if (var14) {
                     for(var13 = 0; var13 < var8.length; ++var13) {
                        ntrace.doTrace("FldTbl32Class[" + var13 + "] = " + var8[var13]);
                     }
                  }
               } else if (var17.equals("ViewTbl16Classes")) {
                  var9 = var3.getViewTbl16Classes();
                  if (var14) {
                     for(var13 = 0; var13 < var9.length; ++var13) {
                        ntrace.doTrace("FldView16Class[" + var13 + "] = " + var9[var13]);
                     }
                  }
               } else if (var17.equals("ViewTbl32Classes")) {
                  var10 = var3.getViewTbl32Classes();
                  if (var14) {
                     for(var13 = 0; var13 < var10.length; ++var13) {
                        ntrace.doTrace("ViewTbl32Class[" + var13 + "] = " + var10[var13]);
                     }
                  }
               } else if (var17.equals("TpUsrFile")) {
                  var6 = var3.getTpUsrFile();
                  if (var14) {
                     ntrace.doTrace("TpUsrFile: " + var6);
                  }
               } else if (var17.equals("AppPasswordIV")) {
                  var4 = var3.getAppPasswordIV();
                  if (var14) {
                     ntrace.doTrace("AppPasswordIV: " + var4);
                  }
               } else if (var17.equals("AppPassword")) {
                  var5 = var3.getAppPassword();
                  if (var14) {
                     ntrace.doTrace("AppPassword: " + var5);
                  }
               } else if (var17.equals("RemoteMBEncoding")) {
                  var11 = var3.getRemoteMBEncoding();
                  if (var14) {
                     ntrace.doTrace("RemoteMBEncoding: " + var11);
                  }
               } else if (var17.equals("MBEncodingMapFile")) {
                  var12 = var3.getMBEncodingMapFile();
                  if (var14) {
                     ntrace.doTrace("MBEncodingMapFile: " + var12);
                  }
               } else if (var14) {
                  ntrace.doTrace("Unknown attribute " + var17 + ", ignored!");
               }
            }
         }

         this.w.lock();

         try {
            if (var7 != null) {
               this.setFieldTables(var7, false);
            }

            if (var8 != null) {
               this.setFieldTables(var8, true);
            }

            if (var9 != null) {
               this.setViewTables(var9, false);
            }

            if (var9 != null) {
               this.setViewTables(var10, true);
            }

            if (var6 != null) {
               this.myTpUsrFile = var6;
            }

            if (var4 != null) {
               this.myAppPasswordIV = var4;
            }

            if (var5 != null) {
               this.myAppPassword = var5;
            }

            if (var11 != null) {
               this.myRemoteMBEncoding = var11;
            }

            if (var12 != null) {
               this.myMBEncodingMapFile = var12;
            }
         } catch (TPException var19) {
            this.w.unlock();
            if (var14) {
               ntrace.doTrace("*]/TDMResources/prepareUpdate/30/update rejected");
            }

            throw new BeanUpdateRejectedException("Error: " + var19.getMessage());
         }

         if (var15 && this.isObjectSuspended()) {
            this.setMBean(var3);
            this.registerListener();
            this.activateObject();
         }

         this.w.unlock();
         if (var14) {
            ntrace.doTrace("]/TDMResources/prepareUpdate/40/DONE");
         }

      }
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMResources/activeUpdate");
         ntrace.doTrace("]/TDMResources/activeUpdate/10/DONE");
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      boolean var2 = ntrace.isTraceEnabled(16);
      if (var2) {
         ntrace.doTrace("[/TDMResources/rollbackUpdate");
         ntrace.doTrace("]/TDMResources/rollbackUpdate/10/DONE");
      }

   }

   public void registerListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMResources/registerListener");
      }

      if (this.mBean != null && !this.registered) {
         if (var1) {
            ntrace.doTrace("TDMResources: add Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).addBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMResources/registerListener/10/DONE");
      }

   }

   public void unregisterListener() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/TDMResources/unregisterListener");
      }

      if (this.mBean != null && this.registered) {
         if (var1) {
            ntrace.doTrace("TDMResources: remove Bean Update Listener: " + this);
         }

         ((AbstractDescriptorBean)this.mBean).removeBeanUpdateListener(this);
      }

      if (var1) {
         ntrace.doTrace("]/TDMResources/unregisterListener/10/DONE");
      }

   }

   public void setTuxedoConnectorName(String var1) {
      myName = var1;
   }

   public String getTuxedoConnectorName() {
      return myName;
   }

   public String[] getViewTbl16Classes() {
      return this.myVtbl16;
   }

   public String[] getViewTbl32Classes() {
      return this.myVtbl32;
   }

   public String[] getFldTbl16Classes() {
      return this.myFtbl16;
   }

   public String[] getFldTbl32Classes() {
      return this.myFtbl32;
   }

   public static TDMResources getGlobalResources() {
      return myGlobalResources;
   }

   public static String getGlobalTpUsrFile() {
      return myGlobalResources == null ? null : myGlobalResources.getTpUsrFile();
   }

   public static String getApplicationPassword() {
      return myGlobalResources == null ? null : PasswordUtils.decryptPassword(key, myGlobalResources.getAppPasswordIV(), myGlobalResources.getAppPassword(), encType);
   }

   public static String getGlobalRemoteMBEncoding() {
      return myGlobalResources == null ? null : myGlobalResources.getRemoteMBEncoding();
   }

   public static String getGlobalMBEncodingMapFile() {
      return myGlobalResources == null ? null : myGlobalResources.getMBEncodingMapFile();
   }

   public static TDMResources create(WTCResourcesMBean var0) throws TPException {
      boolean var1 = ntrace.isMixedTraceEnabled(18);
      if (var1) {
         ntrace.doTrace("[/TDMResources/create/");
      }

      myGlobalResources = new TDMResources();
      String[] var2 = var0.getFldTbl16Classes();
      if (var2 != null && var2.length != 0) {
         try {
            myGlobalResources.setFieldTables(var2, false);
         } catch (TPException var14) {
            Loggable var4 = WTCLogger.logUEconstructTDMResourcesFTLoggable(var14.getMessage());
            var4.log();
            if (var1) {
               ntrace.doTrace("*]/TDMResources/create/10/fld tbl exception");
            }

            throw new TPException(4, var4.getMessage());
         }
      }

      String[] var3 = var0.getFldTbl32Classes();
      if (var3 != null && var3.length != 0) {
         try {
            myGlobalResources.setFieldTables(var3, true);
         } catch (TPException var15) {
            Loggable var5 = WTCLogger.logUEconstructTDMResourcesFTLoggable(var15.getMessage());
            var5.log();
            if (var1) {
               ntrace.doTrace("*]/TDMResources/create/20/fld tbl32 exception");
            }

            throw new TPException(4, var5.getMessage());
         }
      }

      String[] var17 = var0.getViewTbl16Classes();
      if (var17 != null && var17.length != 0) {
         try {
            myGlobalResources.setViewTables(var17, false);
         } catch (TPException var16) {
            Loggable var6 = WTCLogger.logInvalidMBeanAttrLoggable("ViewTbl16Classes", var0.getName());
            var6.log();
            if (var1) {
               ntrace.doTrace("*]/TDMResources/create/30/view tbl exception");
            }

            throw new TPException(4, var6.getMessage());
         }
      }

      String[] var18 = var0.getViewTbl32Classes();
      if (var18 != null && var18.length != 0) {
         try {
            myGlobalResources.setViewTables(var18, true);
         } catch (TPException var13) {
            Loggable var7 = WTCLogger.logInvalidMBeanAttrLoggable("ViewTbl32Classes", var0.getName());
            var7.log();
            if (var1) {
               ntrace.doTrace("*]/TDMResources/create/40/view tbl32 exception");
            }

            throw new TPException(4, var7.getMessage());
         }
      }

      boolean var19 = false;
      boolean var20 = false;
      String var8 = var0.getAppPassword();
      if (var8 != null && var8.length() != 0) {
         var19 = true;
         myGlobalResources.setAppPassword(var8);
         if (var1) {
            ntrace.doTrace("AppPassword is set.");
         }
      }

      String var9 = var0.getAppPasswordIV();
      if (var9 != null && var9.length() != 0) {
         var20 = true;
         myGlobalResources.setAppPasswordIV(var9);
         if (var1) {
            ntrace.doTrace("AppPasswordIV is set.");
         }
      }

      if (var20 != var19) {
         Loggable var21 = WTCLogger.logErrorPasswordInfoLoggable("App");
         var21.log();
         if (var1) {
            ntrace.doTrace("*]/TDMResources/create/50/inconsistent App Password");
         }

         throw new TPException(4, var21.getMessage());
      } else {
         if (var1) {
            ntrace.doTrace("checked App Passwd,PasswdIV.");
         }

         String var10 = var0.getTpUsrFile();
         if (var10 != null) {
            myGlobalResources.setTpUsrFile(var10);
            if (var1) {
               ntrace.doTrace("global TpUsrFile =" + var10);
            }
         } else if (var1) {
            ntrace.doTrace("global TpUsrFile not set");
         }

         String var11 = var0.getRemoteMBEncoding();
         if (var11 != null) {
            myGlobalResources.setRemoteMBEncoding(var11);
            if (var1) {
               ntrace.doTrace("RemoteMBEncoding =" + var11);
            }
         } else if (var1) {
            ntrace.doTrace("RemoteMBEncoding not set");
         }

         String var12 = var0.getMBEncodingMapFile();
         if (var12 != null) {
            myGlobalResources.setMBEncodingMapFile(var12);
            if (var1) {
               ntrace.doTrace("MBEncodingMapFile =" + var12);
            }
         } else if (var1) {
            ntrace.doTrace("MBEncodingMapFile not set");
         }

         myGlobalResources.setMBean(var0);
         if (var1) {
            ntrace.doTrace("]/TDMResources/create/60/success");
         }

         return myGlobalResources;
      }
   }

   public ArrayList assembleConfigObjects() throws TPException {
      WTCResources var1 = new WTCResources();
      var1.setFldTbl16Classes(this.myFtbl16);
      var1.setFldTbl32Classes(this.myFtbl32);
      var1.setViewTbl16Classes(this.myVtbl16);
      var1.setViewTbl32Classes(this.myVtbl32);
      var1.setAppPassword(getApplicationPassword());
      var1.setTpUsrFile(this.myTpUsrFile);
      var1.setRemoteMBEncoding(this.myRemoteMBEncoding);
      var1.setMBEncodingMapFile(this.myMBEncodingMapFile);
      var1.setConfigSource(this);
      this.addConfigObj(var1);
      return this.getConfigObjList();
   }
}
