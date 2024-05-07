package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
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
import weblogic.j2ee.descriptor.wl.validators.WseeConfigBeanValidator;
import weblogic.management.ManagementException;
import weblogic.utils.collections.CombinedIterator;

public class WebServicePhysicalStoreMBeanImpl extends ConfigurationMBeanImpl implements WebServicePhysicalStoreMBean, Serializable {
   private String _Location;
   private String _Name;
   private String _StoreType;
   private String _SynchronousWritePolicy;
   private static SchemaHelper2 _schemaHelper;

   public WebServicePhysicalStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServicePhysicalStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validatePhysicalStoreName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public void setStoreType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"FILE", "JDBC"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StoreType", var1, var2);
      String var3 = this._StoreType;
      this._StoreType = var1;
      this._postSet(7, var3, var1);
   }

   public String getStoreType() {
      return this._StoreType;
   }

   public boolean isStoreTypeSet() {
      return this._isSet(7);
   }

   public String getLocation() {
      return this._Location;
   }

   public boolean isLocationSet() {
      return this._isSet(8);
   }

   public void setLocation(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Location;
      this._Location = var1;
      this._postSet(8, var2, var1);
   }

   public String getSynchronousWritePolicy() {
      return this._SynchronousWritePolicy;
   }

   public boolean isSynchronousWritePolicySet() {
      return this._isSet(9);
   }

   public void setSynchronousWritePolicy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"DISABLED", "CACHE_FLUSH", "DIRECT_WRITE"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SynchronousWritePolicy", var1, var2);
      String var3 = this._SynchronousWritePolicy;
      this._SynchronousWritePolicy = var1;
      this._postSet(9, var3, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._Location = null;
               if (var2) {
                  break;
               }
            case 2:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 7:
               this._StoreType = "FILE";
               if (var2) {
                  break;
               }
            case 9:
               this._SynchronousWritePolicy = "CACHE_FLUSH";
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "WebServicePhysicalStore";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("Location")) {
         var3 = this._Location;
         this._Location = (String)var2;
         this._postSet(8, var3, this._Location);
      } else if (var1.equals("Name")) {
         var3 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var3, this._Name);
      } else if (var1.equals("StoreType")) {
         var3 = this._StoreType;
         this._StoreType = (String)var2;
         this._postSet(7, var3, this._StoreType);
      } else if (var1.equals("SynchronousWritePolicy")) {
         var3 = this._SynchronousWritePolicy;
         this._SynchronousWritePolicy = (String)var2;
         this._postSet(9, var3, this._SynchronousWritePolicy);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Location")) {
         return this._Location;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("StoreType")) {
         return this._StoreType;
      } else {
         return var1.equals("SynchronousWritePolicy") ? this._SynchronousWritePolicy : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 8:
               if (var1.equals("location")) {
                  return 8;
               }
               break;
            case 10:
               if (var1.equals("store-type")) {
                  return 7;
               }
               break;
            case 24:
               if (var1.equals("synchronous-write-policy")) {
                  return 9;
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "store-type";
            case 8:
               return "location";
            case 9:
               return "synchronous-write-policy";
         }
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServicePhysicalStoreMBeanImpl bean;

      protected Helper(WebServicePhysicalStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "StoreType";
            case 8:
               return "Location";
            case 9:
               return "SynchronousWritePolicy";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Location")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("StoreType")) {
            return 7;
         } else {
            return var1.equals("SynchronousWritePolicy") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isLocationSet()) {
               var2.append("Location");
               var2.append(String.valueOf(this.bean.getLocation()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isStoreTypeSet()) {
               var2.append("StoreType");
               var2.append(String.valueOf(this.bean.getStoreType()));
            }

            if (this.bean.isSynchronousWritePolicySet()) {
               var2.append("SynchronousWritePolicy");
               var2.append(String.valueOf(this.bean.getSynchronousWritePolicy()));
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
            WebServicePhysicalStoreMBeanImpl var2 = (WebServicePhysicalStoreMBeanImpl)var1;
            this.computeDiff("Location", this.bean.getLocation(), var2.getLocation(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("StoreType", this.bean.getStoreType(), var2.getStoreType(), true);
            this.computeDiff("SynchronousWritePolicy", this.bean.getSynchronousWritePolicy(), var2.getSynchronousWritePolicy(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServicePhysicalStoreMBeanImpl var3 = (WebServicePhysicalStoreMBeanImpl)var1.getSourceBean();
            WebServicePhysicalStoreMBeanImpl var4 = (WebServicePhysicalStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Location")) {
                  var3.setLocation(var4.getLocation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("StoreType")) {
                  var3.setStoreType(var4.getStoreType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("SynchronousWritePolicy")) {
                  var3.setSynchronousWritePolicy(var4.getSynchronousWritePolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            WebServicePhysicalStoreMBeanImpl var5 = (WebServicePhysicalStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Location")) && this.bean.isLocationSet()) {
               var5.setLocation(this.bean.getLocation());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("StoreType")) && this.bean.isStoreTypeSet()) {
               var5.setStoreType(this.bean.getStoreType());
            }

            if ((var3 == null || !var3.contains("SynchronousWritePolicy")) && this.bean.isSynchronousWritePolicySet()) {
               var5.setSynchronousWritePolicy(this.bean.getSynchronousWritePolicy());
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
