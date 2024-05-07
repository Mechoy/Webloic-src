package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WTCResourcesMBeanImpl extends ConfigurationMBeanImpl implements WTCResourcesMBean, Serializable {
   private String _AppPassword;
   private String _AppPasswordIV;
   private String[] _FldTbl16Classes;
   private String[] _FldTbl32Classes;
   private String _MBEncodingMapFile;
   private String _RemoteMBEncoding;
   private String _TpUsrFile;
   private String[] _ViewTbl16Classes;
   private String[] _ViewTbl32Classes;
   private static SchemaHelper2 _schemaHelper;

   public WTCResourcesMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCResourcesMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setFldTbl16Classes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._FldTbl16Classes;
      this._FldTbl16Classes = var1;
      this._postSet(7, var2, var1);
   }

   public String[] getFldTbl16Classes() {
      return this._FldTbl16Classes;
   }

   public boolean isFldTbl16ClassesSet() {
      return this._isSet(7);
   }

   public void setFldTbl32Classes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._FldTbl32Classes;
      this._FldTbl32Classes = var1;
      this._postSet(8, var2, var1);
   }

   public String[] getFldTbl32Classes() {
      return this._FldTbl32Classes;
   }

   public boolean isFldTbl32ClassesSet() {
      return this._isSet(8);
   }

   public void setViewTbl16Classes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._ViewTbl16Classes;
      this._ViewTbl16Classes = var1;
      this._postSet(9, var2, var1);
   }

   public String[] getViewTbl16Classes() {
      return this._ViewTbl16Classes;
   }

   public boolean isViewTbl16ClassesSet() {
      return this._isSet(9);
   }

   public void setViewTbl32Classes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._ViewTbl32Classes;
      this._ViewTbl32Classes = var1;
      this._postSet(10, var2, var1);
   }

   public String[] getViewTbl32Classes() {
      return this._ViewTbl32Classes;
   }

   public boolean isViewTbl32ClassesSet() {
      return this._isSet(10);
   }

   public void setAppPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AppPassword;
      this._AppPassword = var1;
      this._postSet(11, var2, var1);
   }

   public String getAppPassword() {
      return this._AppPassword;
   }

   public boolean isAppPasswordSet() {
      return this._isSet(11);
   }

   public void setAppPasswordIV(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AppPasswordIV;
      this._AppPasswordIV = var1;
      this._postSet(12, var2, var1);
   }

   public String getAppPasswordIV() {
      return this._AppPasswordIV;
   }

   public boolean isAppPasswordIVSet() {
      return this._isSet(12);
   }

   public void setTpUsrFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TpUsrFile;
      this._TpUsrFile = var1;
      this._postSet(13, var2, var1);
   }

   public String getTpUsrFile() {
      return this._TpUsrFile;
   }

   public boolean isTpUsrFileSet() {
      return this._isSet(13);
   }

   public void setRemoteMBEncoding(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RemoteMBEncoding;
      this._RemoteMBEncoding = var1;
      this._postSet(14, var2, var1);
   }

   public String getRemoteMBEncoding() {
      return this._RemoteMBEncoding;
   }

   public boolean isRemoteMBEncodingSet() {
      return this._isSet(14);
   }

   public void setMBEncodingMapFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MBEncodingMapFile;
      this._MBEncodingMapFile = var1;
      this._postSet(15, var2, var1);
   }

   public String getMBEncodingMapFile() {
      return this._MBEncodingMapFile;
   }

   public boolean isMBEncodingMapFileSet() {
      return this._isSet(15);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._AppPassword = null;
               if (var2) {
                  break;
               }
            case 12:
               this._AppPasswordIV = null;
               if (var2) {
                  break;
               }
            case 7:
               this._FldTbl16Classes = new String[0];
               if (var2) {
                  break;
               }
            case 8:
               this._FldTbl32Classes = new String[0];
               if (var2) {
                  break;
               }
            case 15:
               this._MBEncodingMapFile = null;
               if (var2) {
                  break;
               }
            case 14:
               this._RemoteMBEncoding = null;
               if (var2) {
                  break;
               }
            case 13:
               this._TpUsrFile = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ViewTbl16Classes = new String[0];
               if (var2) {
                  break;
               }
            case 10:
               this._ViewTbl32Classes = new String[0];
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "WTCResources";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("AppPassword")) {
         var4 = this._AppPassword;
         this._AppPassword = (String)var2;
         this._postSet(11, var4, this._AppPassword);
      } else if (var1.equals("AppPasswordIV")) {
         var4 = this._AppPasswordIV;
         this._AppPasswordIV = (String)var2;
         this._postSet(12, var4, this._AppPasswordIV);
      } else {
         String[] var3;
         if (var1.equals("FldTbl16Classes")) {
            var3 = this._FldTbl16Classes;
            this._FldTbl16Classes = (String[])((String[])var2);
            this._postSet(7, var3, this._FldTbl16Classes);
         } else if (var1.equals("FldTbl32Classes")) {
            var3 = this._FldTbl32Classes;
            this._FldTbl32Classes = (String[])((String[])var2);
            this._postSet(8, var3, this._FldTbl32Classes);
         } else if (var1.equals("MBEncodingMapFile")) {
            var4 = this._MBEncodingMapFile;
            this._MBEncodingMapFile = (String)var2;
            this._postSet(15, var4, this._MBEncodingMapFile);
         } else if (var1.equals("RemoteMBEncoding")) {
            var4 = this._RemoteMBEncoding;
            this._RemoteMBEncoding = (String)var2;
            this._postSet(14, var4, this._RemoteMBEncoding);
         } else if (var1.equals("TpUsrFile")) {
            var4 = this._TpUsrFile;
            this._TpUsrFile = (String)var2;
            this._postSet(13, var4, this._TpUsrFile);
         } else if (var1.equals("ViewTbl16Classes")) {
            var3 = this._ViewTbl16Classes;
            this._ViewTbl16Classes = (String[])((String[])var2);
            this._postSet(9, var3, this._ViewTbl16Classes);
         } else if (var1.equals("ViewTbl32Classes")) {
            var3 = this._ViewTbl32Classes;
            this._ViewTbl32Classes = (String[])((String[])var2);
            this._postSet(10, var3, this._ViewTbl32Classes);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AppPassword")) {
         return this._AppPassword;
      } else if (var1.equals("AppPasswordIV")) {
         return this._AppPasswordIV;
      } else if (var1.equals("FldTbl16Classes")) {
         return this._FldTbl16Classes;
      } else if (var1.equals("FldTbl32Classes")) {
         return this._FldTbl32Classes;
      } else if (var1.equals("MBEncodingMapFile")) {
         return this._MBEncodingMapFile;
      } else if (var1.equals("RemoteMBEncoding")) {
         return this._RemoteMBEncoding;
      } else if (var1.equals("TpUsrFile")) {
         return this._TpUsrFile;
      } else if (var1.equals("ViewTbl16Classes")) {
         return this._ViewTbl16Classes;
      } else {
         return var1.equals("ViewTbl32Classes") ? this._ViewTbl32Classes : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("tp-usr-file")) {
                  return 13;
               }
               break;
            case 12:
               if (var1.equals("app-password")) {
                  return 11;
               }
            case 13:
            case 18:
            case 19:
            default:
               break;
            case 14:
               if (var1.equals("app-passwordiv")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("fld-tbl16-class")) {
                  return 7;
               }

               if (var1.equals("fld-tbl32-class")) {
                  return 8;
               }
               break;
            case 16:
               if (var1.equals("view-tbl16-class")) {
                  return 9;
               }

               if (var1.equals("view-tbl32-class")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("remotemb-encoding")) {
                  return 14;
               }
               break;
            case 20:
               if (var1.equals("mb-encoding-map-file")) {
                  return 15;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "fld-tbl16-class";
            case 8:
               return "fld-tbl32-class";
            case 9:
               return "view-tbl16-class";
            case 10:
               return "view-tbl32-class";
            case 11:
               return "app-password";
            case 12:
               return "app-passwordiv";
            case 13:
               return "tp-usr-file";
            case 14:
               return "remotemb-encoding";
            case 15:
               return "mb-encoding-map-file";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WTCResourcesMBeanImpl bean;

      protected Helper(WTCResourcesMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "FldTbl16Classes";
            case 8:
               return "FldTbl32Classes";
            case 9:
               return "ViewTbl16Classes";
            case 10:
               return "ViewTbl32Classes";
            case 11:
               return "AppPassword";
            case 12:
               return "AppPasswordIV";
            case 13:
               return "TpUsrFile";
            case 14:
               return "RemoteMBEncoding";
            case 15:
               return "MBEncodingMapFile";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AppPassword")) {
            return 11;
         } else if (var1.equals("AppPasswordIV")) {
            return 12;
         } else if (var1.equals("FldTbl16Classes")) {
            return 7;
         } else if (var1.equals("FldTbl32Classes")) {
            return 8;
         } else if (var1.equals("MBEncodingMapFile")) {
            return 15;
         } else if (var1.equals("RemoteMBEncoding")) {
            return 14;
         } else if (var1.equals("TpUsrFile")) {
            return 13;
         } else if (var1.equals("ViewTbl16Classes")) {
            return 9;
         } else {
            return var1.equals("ViewTbl32Classes") ? 10 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isAppPasswordSet()) {
               var2.append("AppPassword");
               var2.append(String.valueOf(this.bean.getAppPassword()));
            }

            if (this.bean.isAppPasswordIVSet()) {
               var2.append("AppPasswordIV");
               var2.append(String.valueOf(this.bean.getAppPasswordIV()));
            }

            if (this.bean.isFldTbl16ClassesSet()) {
               var2.append("FldTbl16Classes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getFldTbl16Classes())));
            }

            if (this.bean.isFldTbl32ClassesSet()) {
               var2.append("FldTbl32Classes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getFldTbl32Classes())));
            }

            if (this.bean.isMBEncodingMapFileSet()) {
               var2.append("MBEncodingMapFile");
               var2.append(String.valueOf(this.bean.getMBEncodingMapFile()));
            }

            if (this.bean.isRemoteMBEncodingSet()) {
               var2.append("RemoteMBEncoding");
               var2.append(String.valueOf(this.bean.getRemoteMBEncoding()));
            }

            if (this.bean.isTpUsrFileSet()) {
               var2.append("TpUsrFile");
               var2.append(String.valueOf(this.bean.getTpUsrFile()));
            }

            if (this.bean.isViewTbl16ClassesSet()) {
               var2.append("ViewTbl16Classes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getViewTbl16Classes())));
            }

            if (this.bean.isViewTbl32ClassesSet()) {
               var2.append("ViewTbl32Classes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getViewTbl32Classes())));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WTCResourcesMBeanImpl var2 = (WTCResourcesMBeanImpl)var1;
            this.computeDiff("AppPassword", this.bean.getAppPassword(), var2.getAppPassword(), true);
            this.computeDiff("AppPasswordIV", this.bean.getAppPasswordIV(), var2.getAppPasswordIV(), true);
            this.computeDiff("FldTbl16Classes", this.bean.getFldTbl16Classes(), var2.getFldTbl16Classes(), true);
            this.computeDiff("FldTbl32Classes", this.bean.getFldTbl32Classes(), var2.getFldTbl32Classes(), true);
            this.computeDiff("MBEncodingMapFile", this.bean.getMBEncodingMapFile(), var2.getMBEncodingMapFile(), true);
            this.computeDiff("RemoteMBEncoding", this.bean.getRemoteMBEncoding(), var2.getRemoteMBEncoding(), true);
            this.computeDiff("TpUsrFile", this.bean.getTpUsrFile(), var2.getTpUsrFile(), true);
            this.computeDiff("ViewTbl16Classes", this.bean.getViewTbl16Classes(), var2.getViewTbl16Classes(), true);
            this.computeDiff("ViewTbl32Classes", this.bean.getViewTbl32Classes(), var2.getViewTbl32Classes(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCResourcesMBeanImpl var3 = (WTCResourcesMBeanImpl)var1.getSourceBean();
            WTCResourcesMBeanImpl var4 = (WTCResourcesMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AppPassword")) {
                  var3.setAppPassword(var4.getAppPassword());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("AppPasswordIV")) {
                  var3.setAppPasswordIV(var4.getAppPasswordIV());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("FldTbl16Classes")) {
                  var3.setFldTbl16Classes(var4.getFldTbl16Classes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("FldTbl32Classes")) {
                  var3.setFldTbl32Classes(var4.getFldTbl32Classes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("MBEncodingMapFile")) {
                  var3.setMBEncodingMapFile(var4.getMBEncodingMapFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("RemoteMBEncoding")) {
                  var3.setRemoteMBEncoding(var4.getRemoteMBEncoding());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("TpUsrFile")) {
                  var3.setTpUsrFile(var4.getTpUsrFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("ViewTbl16Classes")) {
                  var3.setViewTbl16Classes(var4.getViewTbl16Classes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ViewTbl32Classes")) {
                  var3.setViewTbl32Classes(var4.getViewTbl32Classes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            WTCResourcesMBeanImpl var5 = (WTCResourcesMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AppPassword")) && this.bean.isAppPasswordSet()) {
               var5.setAppPassword(this.bean.getAppPassword());
            }

            if ((var3 == null || !var3.contains("AppPasswordIV")) && this.bean.isAppPasswordIVSet()) {
               var5.setAppPasswordIV(this.bean.getAppPasswordIV());
            }

            String[] var4;
            if ((var3 == null || !var3.contains("FldTbl16Classes")) && this.bean.isFldTbl16ClassesSet()) {
               var4 = this.bean.getFldTbl16Classes();
               var5.setFldTbl16Classes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("FldTbl32Classes")) && this.bean.isFldTbl32ClassesSet()) {
               var4 = this.bean.getFldTbl32Classes();
               var5.setFldTbl32Classes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("MBEncodingMapFile")) && this.bean.isMBEncodingMapFileSet()) {
               var5.setMBEncodingMapFile(this.bean.getMBEncodingMapFile());
            }

            if ((var3 == null || !var3.contains("RemoteMBEncoding")) && this.bean.isRemoteMBEncodingSet()) {
               var5.setRemoteMBEncoding(this.bean.getRemoteMBEncoding());
            }

            if ((var3 == null || !var3.contains("TpUsrFile")) && this.bean.isTpUsrFileSet()) {
               var5.setTpUsrFile(this.bean.getTpUsrFile());
            }

            if ((var3 == null || !var3.contains("ViewTbl16Classes")) && this.bean.isViewTbl16ClassesSet()) {
               var4 = this.bean.getViewTbl16Classes();
               var5.setViewTbl16Classes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("ViewTbl32Classes")) && this.bean.isViewTbl32ClassesSet()) {
               var4 = this.bean.getViewTbl32Classes();
               var5.setViewTbl32Classes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
