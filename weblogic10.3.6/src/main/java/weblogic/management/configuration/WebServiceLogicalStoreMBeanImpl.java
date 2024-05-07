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

public class WebServiceLogicalStoreMBeanImpl extends ConfigurationMBeanImpl implements WebServiceLogicalStoreMBean, Serializable {
   private String _CleanerInterval;
   private String _DefaultMaximumObjectLifetime;
   private String _Name;
   private String _PersistenceStrategy;
   private String _PhysicalStoreName;
   private String _RequestBufferingQueueJndiName;
   private String _ResponseBufferingQueueJndiName;
   private static SchemaHelper2 _schemaHelper;

   public WebServiceLogicalStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServiceLogicalStoreMBeanImpl(DescriptorBean var1, int var2) {
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
      WseeConfigBeanValidator.validateLogicalStoreName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public String getPersistenceStrategy() {
      return this._PersistenceStrategy;
   }

   public boolean isPersistenceStrategySet() {
      return this._isSet(7);
   }

   public void setPersistenceStrategy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"LOCAL_ACCESS_ONLY", "IN_MEMORY"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("PersistenceStrategy", var1, var2);
      String var3 = this._PersistenceStrategy;
      this._PersistenceStrategy = var1;
      this._postSet(7, var3, var1);
   }

   public void setCleanerInterval(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateCleanerInterval(var1);
      String var2 = this._CleanerInterval;
      this._CleanerInterval = var1;
      this._postSet(8, var2, var1);
   }

   public String getCleanerInterval() {
      return this._CleanerInterval;
   }

   public boolean isCleanerIntervalSet() {
      return this._isSet(8);
   }

   public void setDefaultMaximumObjectLifetime(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateDefaultMaximumObjectLifetime(var1);
      String var2 = this._DefaultMaximumObjectLifetime;
      this._DefaultMaximumObjectLifetime = var1;
      this._postSet(9, var2, var1);
   }

   public String getDefaultMaximumObjectLifetime() {
      return this._DefaultMaximumObjectLifetime;
   }

   public boolean isDefaultMaximumObjectLifetimeSet() {
      return this._isSet(9);
   }

   public void setRequestBufferingQueueJndiName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateRequestBufferingQueueJndiName(var1);
      String var2 = this._RequestBufferingQueueJndiName;
      this._RequestBufferingQueueJndiName = var1;
      this._postSet(10, var2, var1);
   }

   public String getRequestBufferingQueueJndiName() {
      return this._RequestBufferingQueueJndiName;
   }

   public boolean isRequestBufferingQueueJndiNameSet() {
      return this._isSet(10);
   }

   public void setResponseBufferingQueueJndiName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateResponseBufferingQueueJndiName(var1);
      String var2 = this._ResponseBufferingQueueJndiName;
      this._ResponseBufferingQueueJndiName = var1;
      this._postSet(11, var2, var1);
   }

   public String getResponseBufferingQueueJndiName() {
      return this._ResponseBufferingQueueJndiName;
   }

   public boolean isResponseBufferingQueueJndiNameSet() {
      return this._isSet(11);
   }

   public void setPhysicalStoreName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PhysicalStoreName;
      this._PhysicalStoreName = var1;
      this._postSet(12, var2, var1);
   }

   public String getPhysicalStoreName() {
      return this._PhysicalStoreName;
   }

   public boolean isPhysicalStoreNameSet() {
      return this._isSet(12);
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
               this._CleanerInterval = "PT10M";
               if (var2) {
                  break;
               }
            case 9:
               this._DefaultMaximumObjectLifetime = "P1D";
               if (var2) {
                  break;
               }
            case 2:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 7:
               this._PersistenceStrategy = "LOCAL_ACCESS_ONLY";
               if (var2) {
                  break;
               }
            case 12:
               this._PhysicalStoreName = "";
               if (var2) {
                  break;
               }
            case 10:
               this._RequestBufferingQueueJndiName = "";
               if (var2) {
                  break;
               }
            case 11:
               this._ResponseBufferingQueueJndiName = null;
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
      return "WebServiceLogicalStore";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("CleanerInterval")) {
         var3 = this._CleanerInterval;
         this._CleanerInterval = (String)var2;
         this._postSet(8, var3, this._CleanerInterval);
      } else if (var1.equals("DefaultMaximumObjectLifetime")) {
         var3 = this._DefaultMaximumObjectLifetime;
         this._DefaultMaximumObjectLifetime = (String)var2;
         this._postSet(9, var3, this._DefaultMaximumObjectLifetime);
      } else if (var1.equals("Name")) {
         var3 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var3, this._Name);
      } else if (var1.equals("PersistenceStrategy")) {
         var3 = this._PersistenceStrategy;
         this._PersistenceStrategy = (String)var2;
         this._postSet(7, var3, this._PersistenceStrategy);
      } else if (var1.equals("PhysicalStoreName")) {
         var3 = this._PhysicalStoreName;
         this._PhysicalStoreName = (String)var2;
         this._postSet(12, var3, this._PhysicalStoreName);
      } else if (var1.equals("RequestBufferingQueueJndiName")) {
         var3 = this._RequestBufferingQueueJndiName;
         this._RequestBufferingQueueJndiName = (String)var2;
         this._postSet(10, var3, this._RequestBufferingQueueJndiName);
      } else if (var1.equals("ResponseBufferingQueueJndiName")) {
         var3 = this._ResponseBufferingQueueJndiName;
         this._ResponseBufferingQueueJndiName = (String)var2;
         this._postSet(11, var3, this._ResponseBufferingQueueJndiName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CleanerInterval")) {
         return this._CleanerInterval;
      } else if (var1.equals("DefaultMaximumObjectLifetime")) {
         return this._DefaultMaximumObjectLifetime;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PersistenceStrategy")) {
         return this._PersistenceStrategy;
      } else if (var1.equals("PhysicalStoreName")) {
         return this._PhysicalStoreName;
      } else if (var1.equals("RequestBufferingQueueJndiName")) {
         return this._RequestBufferingQueueJndiName;
      } else {
         return var1.equals("ResponseBufferingQueueJndiName") ? this._ResponseBufferingQueueJndiName : super.getValue(var1);
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
            case 16:
               if (var1.equals("cleaner-interval")) {
                  return 8;
               }
               break;
            case 19:
               if (var1.equals("physical-store-name")) {
                  return 12;
               }
               break;
            case 20:
               if (var1.equals("persistence-strategy")) {
                  return 7;
               }
               break;
            case 31:
               if (var1.equals("default-maximum-object-lifetime")) {
                  return 9;
               }
               break;
            case 33:
               if (var1.equals("request-buffering-queue-jndi-name")) {
                  return 10;
               }
               break;
            case 34:
               if (var1.equals("response-buffering-queue-jndi-name")) {
                  return 11;
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
               return "persistence-strategy";
            case 8:
               return "cleaner-interval";
            case 9:
               return "default-maximum-object-lifetime";
            case 10:
               return "request-buffering-queue-jndi-name";
            case 11:
               return "response-buffering-queue-jndi-name";
            case 12:
               return "physical-store-name";
         }
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServiceLogicalStoreMBeanImpl bean;

      protected Helper(WebServiceLogicalStoreMBeanImpl var1) {
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
               return "PersistenceStrategy";
            case 8:
               return "CleanerInterval";
            case 9:
               return "DefaultMaximumObjectLifetime";
            case 10:
               return "RequestBufferingQueueJndiName";
            case 11:
               return "ResponseBufferingQueueJndiName";
            case 12:
               return "PhysicalStoreName";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CleanerInterval")) {
            return 8;
         } else if (var1.equals("DefaultMaximumObjectLifetime")) {
            return 9;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PersistenceStrategy")) {
            return 7;
         } else if (var1.equals("PhysicalStoreName")) {
            return 12;
         } else if (var1.equals("RequestBufferingQueueJndiName")) {
            return 10;
         } else {
            return var1.equals("ResponseBufferingQueueJndiName") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isCleanerIntervalSet()) {
               var2.append("CleanerInterval");
               var2.append(String.valueOf(this.bean.getCleanerInterval()));
            }

            if (this.bean.isDefaultMaximumObjectLifetimeSet()) {
               var2.append("DefaultMaximumObjectLifetime");
               var2.append(String.valueOf(this.bean.getDefaultMaximumObjectLifetime()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPersistenceStrategySet()) {
               var2.append("PersistenceStrategy");
               var2.append(String.valueOf(this.bean.getPersistenceStrategy()));
            }

            if (this.bean.isPhysicalStoreNameSet()) {
               var2.append("PhysicalStoreName");
               var2.append(String.valueOf(this.bean.getPhysicalStoreName()));
            }

            if (this.bean.isRequestBufferingQueueJndiNameSet()) {
               var2.append("RequestBufferingQueueJndiName");
               var2.append(String.valueOf(this.bean.getRequestBufferingQueueJndiName()));
            }

            if (this.bean.isResponseBufferingQueueJndiNameSet()) {
               var2.append("ResponseBufferingQueueJndiName");
               var2.append(String.valueOf(this.bean.getResponseBufferingQueueJndiName()));
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
            WebServiceLogicalStoreMBeanImpl var2 = (WebServiceLogicalStoreMBeanImpl)var1;
            this.computeDiff("CleanerInterval", this.bean.getCleanerInterval(), var2.getCleanerInterval(), false);
            this.computeDiff("DefaultMaximumObjectLifetime", this.bean.getDefaultMaximumObjectLifetime(), var2.getDefaultMaximumObjectLifetime(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PersistenceStrategy", this.bean.getPersistenceStrategy(), var2.getPersistenceStrategy(), false);
            this.computeDiff("PhysicalStoreName", this.bean.getPhysicalStoreName(), var2.getPhysicalStoreName(), true);
            this.computeDiff("RequestBufferingQueueJndiName", this.bean.getRequestBufferingQueueJndiName(), var2.getRequestBufferingQueueJndiName(), false);
            this.computeDiff("ResponseBufferingQueueJndiName", this.bean.getResponseBufferingQueueJndiName(), var2.getResponseBufferingQueueJndiName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServiceLogicalStoreMBeanImpl var3 = (WebServiceLogicalStoreMBeanImpl)var1.getSourceBean();
            WebServiceLogicalStoreMBeanImpl var4 = (WebServiceLogicalStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CleanerInterval")) {
                  var3.setCleanerInterval(var4.getCleanerInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("DefaultMaximumObjectLifetime")) {
                  var3.setDefaultMaximumObjectLifetime(var4.getDefaultMaximumObjectLifetime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PersistenceStrategy")) {
                  var3.setPersistenceStrategy(var4.getPersistenceStrategy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("PhysicalStoreName")) {
                  var3.setPhysicalStoreName(var4.getPhysicalStoreName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("RequestBufferingQueueJndiName")) {
                  var3.setRequestBufferingQueueJndiName(var4.getRequestBufferingQueueJndiName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ResponseBufferingQueueJndiName")) {
                  var3.setResponseBufferingQueueJndiName(var4.getResponseBufferingQueueJndiName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            WebServiceLogicalStoreMBeanImpl var5 = (WebServiceLogicalStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CleanerInterval")) && this.bean.isCleanerIntervalSet()) {
               var5.setCleanerInterval(this.bean.getCleanerInterval());
            }

            if ((var3 == null || !var3.contains("DefaultMaximumObjectLifetime")) && this.bean.isDefaultMaximumObjectLifetimeSet()) {
               var5.setDefaultMaximumObjectLifetime(this.bean.getDefaultMaximumObjectLifetime());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PersistenceStrategy")) && this.bean.isPersistenceStrategySet()) {
               var5.setPersistenceStrategy(this.bean.getPersistenceStrategy());
            }

            if ((var3 == null || !var3.contains("PhysicalStoreName")) && this.bean.isPhysicalStoreNameSet()) {
               var5.setPhysicalStoreName(this.bean.getPhysicalStoreName());
            }

            if ((var3 == null || !var3.contains("RequestBufferingQueueJndiName")) && this.bean.isRequestBufferingQueueJndiNameSet()) {
               var5.setRequestBufferingQueueJndiName(this.bean.getRequestBufferingQueueJndiName());
            }

            if ((var3 == null || !var3.contains("ResponseBufferingQueueJndiName")) && this.bean.isResponseBufferingQueueJndiNameSet()) {
               var5.setResponseBufferingQueueJndiName(this.bean.getResponseBufferingQueueJndiName());
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
