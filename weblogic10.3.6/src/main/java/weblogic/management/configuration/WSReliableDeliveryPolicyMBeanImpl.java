package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WSReliableDeliveryPolicyMBeanImpl extends ConfigurationMBeanImpl implements WSReliableDeliveryPolicyMBean, Serializable {
   private int _DefaultRetryCount;
   private int _DefaultRetryInterval;
   private int _DefaultTimeToLive;
   private JMSServerMBean _JMSServer;
   private JMSStoreMBean _Store;
   private static SchemaHelper2 _schemaHelper;

   public WSReliableDeliveryPolicyMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WSReliableDeliveryPolicyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public JMSStoreMBean getStore() {
      return this._Store;
   }

   public String getStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isStoreSet() {
      return this._isSet(7);
   }

   public void setStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSStoreMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  WSReliableDeliveryPolicyMBeanImpl.this.setStore((JMSStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSStoreMBean var2 = this._Store;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Store);
      }

   }

   public void setStore(JMSStoreMBean var1) throws InvalidAttributeValueException {
      JMSStoreMBean var2 = this._Store;
      this._Store = var1;
      this._postSet(7, var2, var1);
   }

   public JMSServerMBean getJMSServer() {
      return this._JMSServer;
   }

   public String getJMSServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getJMSServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isJMSServerSet() {
      return this._isSet(8);
   }

   public void setJMSServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSServerMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  WSReliableDeliveryPolicyMBeanImpl.this.setJMSServer((JMSServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSServerMBean var2 = this._JMSServer;
         this._initializeProperty(8);
         this._postSet(8, var2, this._JMSServer);
      }

   }

   public void setJMSServer(JMSServerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WSReliableDeliveryPolicyMBeanImpl.this.getJMSServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JMSServerMBean var3 = this._JMSServer;
      this._JMSServer = var1;
      this._postSet(8, var3, var1);
   }

   public int getDefaultRetryCount() {
      return this._DefaultRetryCount;
   }

   public boolean isDefaultRetryCountSet() {
      return this._isSet(9);
   }

   public void setDefaultRetryCount(int var1) throws InvalidAttributeValueException {
      int var2 = this._DefaultRetryCount;
      this._DefaultRetryCount = var1;
      this._postSet(9, var2, var1);
   }

   public int getDefaultRetryInterval() {
      return this._DefaultRetryInterval;
   }

   public boolean isDefaultRetryIntervalSet() {
      return this._isSet(10);
   }

   public void setDefaultRetryInterval(int var1) throws InvalidAttributeValueException {
      int var2 = this._DefaultRetryInterval;
      this._DefaultRetryInterval = var1;
      this._postSet(10, var2, var1);
   }

   public int getDefaultTimeToLive() {
      return this._DefaultTimeToLive;
   }

   public boolean isDefaultTimeToLiveSet() {
      return this._isSet(11);
   }

   public void setDefaultTimeToLive(int var1) throws InvalidAttributeValueException {
      int var2 = this._DefaultTimeToLive;
      this._DefaultTimeToLive = var1;
      this._postSet(11, var2, var1);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._DefaultRetryCount = 10;
               if (var2) {
                  break;
               }
            case 10:
               this._DefaultRetryInterval = 6;
               if (var2) {
                  break;
               }
            case 11:
               this._DefaultTimeToLive = 360;
               if (var2) {
                  break;
               }
            case 8:
               this._JMSServer = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Store = null;
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
      return "WSReliableDeliveryPolicy";
   }

   public void putValue(String var1, Object var2) {
      int var5;
      if (var1.equals("DefaultRetryCount")) {
         var5 = this._DefaultRetryCount;
         this._DefaultRetryCount = (Integer)var2;
         this._postSet(9, var5, this._DefaultRetryCount);
      } else if (var1.equals("DefaultRetryInterval")) {
         var5 = this._DefaultRetryInterval;
         this._DefaultRetryInterval = (Integer)var2;
         this._postSet(10, var5, this._DefaultRetryInterval);
      } else if (var1.equals("DefaultTimeToLive")) {
         var5 = this._DefaultTimeToLive;
         this._DefaultTimeToLive = (Integer)var2;
         this._postSet(11, var5, this._DefaultTimeToLive);
      } else if (var1.equals("JMSServer")) {
         JMSServerMBean var4 = this._JMSServer;
         this._JMSServer = (JMSServerMBean)var2;
         this._postSet(8, var4, this._JMSServer);
      } else if (var1.equals("Store")) {
         JMSStoreMBean var3 = this._Store;
         this._Store = (JMSStoreMBean)var2;
         this._postSet(7, var3, this._Store);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DefaultRetryCount")) {
         return new Integer(this._DefaultRetryCount);
      } else if (var1.equals("DefaultRetryInterval")) {
         return new Integer(this._DefaultRetryInterval);
      } else if (var1.equals("DefaultTimeToLive")) {
         return new Integer(this._DefaultTimeToLive);
      } else if (var1.equals("JMSServer")) {
         return this._JMSServer;
      } else {
         return var1.equals("Store") ? this._Store : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 5:
               if (var1.equals("store")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("jms-server")) {
                  return 8;
               }
               break;
            case 19:
               if (var1.equals("default-retry-count")) {
                  return 9;
               }
               break;
            case 20:
               if (var1.equals("default-time-to-live")) {
                  return 11;
               }
               break;
            case 22:
               if (var1.equals("default-retry-interval")) {
                  return 10;
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
               return "store";
            case 8:
               return "jms-server";
            case 9:
               return "default-retry-count";
            case 10:
               return "default-retry-interval";
            case 11:
               return "default-time-to-live";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
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
      private WSReliableDeliveryPolicyMBeanImpl bean;

      protected Helper(WSReliableDeliveryPolicyMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Store";
            case 8:
               return "JMSServer";
            case 9:
               return "DefaultRetryCount";
            case 10:
               return "DefaultRetryInterval";
            case 11:
               return "DefaultTimeToLive";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DefaultRetryCount")) {
            return 9;
         } else if (var1.equals("DefaultRetryInterval")) {
            return 10;
         } else if (var1.equals("DefaultTimeToLive")) {
            return 11;
         } else if (var1.equals("JMSServer")) {
            return 8;
         } else {
            return var1.equals("Store") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isDefaultRetryCountSet()) {
               var2.append("DefaultRetryCount");
               var2.append(String.valueOf(this.bean.getDefaultRetryCount()));
            }

            if (this.bean.isDefaultRetryIntervalSet()) {
               var2.append("DefaultRetryInterval");
               var2.append(String.valueOf(this.bean.getDefaultRetryInterval()));
            }

            if (this.bean.isDefaultTimeToLiveSet()) {
               var2.append("DefaultTimeToLive");
               var2.append(String.valueOf(this.bean.getDefaultTimeToLive()));
            }

            if (this.bean.isJMSServerSet()) {
               var2.append("JMSServer");
               var2.append(String.valueOf(this.bean.getJMSServer()));
            }

            if (this.bean.isStoreSet()) {
               var2.append("Store");
               var2.append(String.valueOf(this.bean.getStore()));
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
            WSReliableDeliveryPolicyMBeanImpl var2 = (WSReliableDeliveryPolicyMBeanImpl)var1;
            this.computeDiff("DefaultRetryCount", this.bean.getDefaultRetryCount(), var2.getDefaultRetryCount(), false);
            this.computeDiff("DefaultRetryInterval", this.bean.getDefaultRetryInterval(), var2.getDefaultRetryInterval(), false);
            this.computeDiff("DefaultTimeToLive", this.bean.getDefaultTimeToLive(), var2.getDefaultTimeToLive(), false);
            this.computeDiff("JMSServer", this.bean.getJMSServer(), var2.getJMSServer(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Store", this.bean.getStore(), var2.getStore(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WSReliableDeliveryPolicyMBeanImpl var3 = (WSReliableDeliveryPolicyMBeanImpl)var1.getSourceBean();
            WSReliableDeliveryPolicyMBeanImpl var4 = (WSReliableDeliveryPolicyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DefaultRetryCount")) {
                  var3.setDefaultRetryCount(var4.getDefaultRetryCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("DefaultRetryInterval")) {
                  var3.setDefaultRetryInterval(var4.getDefaultRetryInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DefaultTimeToLive")) {
                  var3.setDefaultTimeToLive(var4.getDefaultTimeToLive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("JMSServer")) {
                  var3.setJMSServerAsString(var4.getJMSServerAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Store")) {
                  var3.setStoreAsString(var4.getStoreAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            WSReliableDeliveryPolicyMBeanImpl var5 = (WSReliableDeliveryPolicyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DefaultRetryCount")) && this.bean.isDefaultRetryCountSet()) {
               var5.setDefaultRetryCount(this.bean.getDefaultRetryCount());
            }

            if ((var3 == null || !var3.contains("DefaultRetryInterval")) && this.bean.isDefaultRetryIntervalSet()) {
               var5.setDefaultRetryInterval(this.bean.getDefaultRetryInterval());
            }

            if ((var3 == null || !var3.contains("DefaultTimeToLive")) && this.bean.isDefaultTimeToLiveSet()) {
               var5.setDefaultTimeToLive(this.bean.getDefaultTimeToLive());
            }

            if ((var3 == null || !var3.contains("JMSServer")) && this.bean.isJMSServerSet()) {
               var5._unSet(var5, 8);
               var5.setJMSServerAsString(this.bean.getJMSServerAsString());
            }

            if (var2 && (var3 == null || !var3.contains("Store")) && this.bean.isStoreSet()) {
               var5._unSet(var5, 7);
               var5.setStoreAsString(this.bean.getStoreAsString());
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
         this.inferSubTree(this.bean.getJMSServer(), var1, var2);
         this.inferSubTree(this.bean.getStore(), var1, var2);
      }
   }
}
