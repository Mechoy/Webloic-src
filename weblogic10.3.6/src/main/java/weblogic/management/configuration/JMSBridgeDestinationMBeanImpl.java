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
import weblogic.utils.collections.CombinedIterator;

public class JMSBridgeDestinationMBeanImpl extends BridgeDestinationCommonMBeanImpl implements JMSBridgeDestinationMBean, Serializable {
   private String _ConnectionFactoryJNDIName;
   private String _ConnectionURL;
   private String _DestinationJNDIName;
   private String _DestinationType;
   private String _InitialContextFactory;
   private static SchemaHelper2 _schemaHelper;

   public JMSBridgeDestinationMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JMSBridgeDestinationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getConnectionFactoryJNDIName() {
      return this._ConnectionFactoryJNDIName;
   }

   public boolean isConnectionFactoryJNDINameSet() {
      return this._isSet(12);
   }

   public void setConnectionFactoryJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateNotNullable(this.getConnectionFactoryJNDIName(), var1);
      String var2 = this._ConnectionFactoryJNDIName;
      this._ConnectionFactoryJNDIName = var1;
      this._postSet(12, var2, var1);
   }

   public String getInitialContextFactory() {
      return this._InitialContextFactory;
   }

   public boolean isInitialContextFactorySet() {
      return this._isSet(13);
   }

   public void setInitialContextFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._InitialContextFactory;
      this._InitialContextFactory = var1;
      this._postSet(13, var2, var1);
   }

   public String getConnectionURL() {
      return this._ConnectionURL;
   }

   public boolean isConnectionURLSet() {
      return this._isSet(14);
   }

   public void setConnectionURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnectionURL;
      this._ConnectionURL = var1;
      this._postSet(14, var2, var1);
   }

   public String getDestinationJNDIName() {
      return this._DestinationJNDIName;
   }

   public boolean isDestinationJNDINameSet() {
      return this._isSet(15);
   }

   public void setDestinationJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateNotNullable(this.getDestinationJNDIName(), var1);
      String var2 = this._DestinationJNDIName;
      this._DestinationJNDIName = var1;
      this._postSet(15, var2, var1);
   }

   public String getDestinationType() {
      return this._DestinationType;
   }

   public boolean isDestinationTypeSet() {
      return this._isSet(16);
   }

   public void setDestinationType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Queue", "Topic"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DestinationType", var1, var2);
      String var3 = this._DestinationType;
      this._DestinationType = var1;
      this._postSet(16, var3, var1);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._ConnectionFactoryJNDIName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._ConnectionURL = null;
               if (var2) {
                  break;
               }
            case 15:
               this._DestinationJNDIName = null;
               if (var2) {
                  break;
               }
            case 16:
               this._DestinationType = "Queue";
               if (var2) {
                  break;
               }
            case 13:
               this._InitialContextFactory = "weblogic.jndi.WLInitialContextFactory";
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
      return "JMSBridgeDestination";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("ConnectionFactoryJNDIName")) {
         var3 = this._ConnectionFactoryJNDIName;
         this._ConnectionFactoryJNDIName = (String)var2;
         this._postSet(12, var3, this._ConnectionFactoryJNDIName);
      } else if (var1.equals("ConnectionURL")) {
         var3 = this._ConnectionURL;
         this._ConnectionURL = (String)var2;
         this._postSet(14, var3, this._ConnectionURL);
      } else if (var1.equals("DestinationJNDIName")) {
         var3 = this._DestinationJNDIName;
         this._DestinationJNDIName = (String)var2;
         this._postSet(15, var3, this._DestinationJNDIName);
      } else if (var1.equals("DestinationType")) {
         var3 = this._DestinationType;
         this._DestinationType = (String)var2;
         this._postSet(16, var3, this._DestinationType);
      } else if (var1.equals("InitialContextFactory")) {
         var3 = this._InitialContextFactory;
         this._InitialContextFactory = (String)var2;
         this._postSet(13, var3, this._InitialContextFactory);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionFactoryJNDIName")) {
         return this._ConnectionFactoryJNDIName;
      } else if (var1.equals("ConnectionURL")) {
         return this._ConnectionURL;
      } else if (var1.equals("DestinationJNDIName")) {
         return this._DestinationJNDIName;
      } else if (var1.equals("DestinationType")) {
         return this._DestinationType;
      } else {
         return var1.equals("InitialContextFactory") ? this._InitialContextFactory : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends BridgeDestinationCommonMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 14:
               if (var1.equals("connection-url")) {
                  return 14;
               }
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
               break;
            case 16:
               if (var1.equals("destination-type")) {
                  return 16;
               }
               break;
            case 21:
               if (var1.equals("destination-jndi-name")) {
                  return 15;
               }
               break;
            case 23:
               if (var1.equals("initial-context-factory")) {
                  return 13;
               }
               break;
            case 28:
               if (var1.equals("connection-factory-jndi-name")) {
                  return 12;
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
            case 12:
               return "connection-factory-jndi-name";
            case 13:
               return "initial-context-factory";
            case 14:
               return "connection-url";
            case 15:
               return "destination-jndi-name";
            case 16:
               return "destination-type";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
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

   protected static class Helper extends BridgeDestinationCommonMBeanImpl.Helper {
      private JMSBridgeDestinationMBeanImpl bean;

      protected Helper(JMSBridgeDestinationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 12:
               return "ConnectionFactoryJNDIName";
            case 13:
               return "InitialContextFactory";
            case 14:
               return "ConnectionURL";
            case 15:
               return "DestinationJNDIName";
            case 16:
               return "DestinationType";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionFactoryJNDIName")) {
            return 12;
         } else if (var1.equals("ConnectionURL")) {
            return 14;
         } else if (var1.equals("DestinationJNDIName")) {
            return 15;
         } else if (var1.equals("DestinationType")) {
            return 16;
         } else {
            return var1.equals("InitialContextFactory") ? 13 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionFactoryJNDINameSet()) {
               var2.append("ConnectionFactoryJNDIName");
               var2.append(String.valueOf(this.bean.getConnectionFactoryJNDIName()));
            }

            if (this.bean.isConnectionURLSet()) {
               var2.append("ConnectionURL");
               var2.append(String.valueOf(this.bean.getConnectionURL()));
            }

            if (this.bean.isDestinationJNDINameSet()) {
               var2.append("DestinationJNDIName");
               var2.append(String.valueOf(this.bean.getDestinationJNDIName()));
            }

            if (this.bean.isDestinationTypeSet()) {
               var2.append("DestinationType");
               var2.append(String.valueOf(this.bean.getDestinationType()));
            }

            if (this.bean.isInitialContextFactorySet()) {
               var2.append("InitialContextFactory");
               var2.append(String.valueOf(this.bean.getInitialContextFactory()));
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
            JMSBridgeDestinationMBeanImpl var2 = (JMSBridgeDestinationMBeanImpl)var1;
            this.computeDiff("ConnectionFactoryJNDIName", this.bean.getConnectionFactoryJNDIName(), var2.getConnectionFactoryJNDIName(), false);
            this.computeDiff("ConnectionURL", this.bean.getConnectionURL(), var2.getConnectionURL(), false);
            this.computeDiff("DestinationJNDIName", this.bean.getDestinationJNDIName(), var2.getDestinationJNDIName(), false);
            this.computeDiff("DestinationType", this.bean.getDestinationType(), var2.getDestinationType(), false);
            this.computeDiff("InitialContextFactory", this.bean.getInitialContextFactory(), var2.getInitialContextFactory(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSBridgeDestinationMBeanImpl var3 = (JMSBridgeDestinationMBeanImpl)var1.getSourceBean();
            JMSBridgeDestinationMBeanImpl var4 = (JMSBridgeDestinationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionFactoryJNDIName")) {
                  var3.setConnectionFactoryJNDIName(var4.getConnectionFactoryJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ConnectionURL")) {
                  var3.setConnectionURL(var4.getConnectionURL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("DestinationJNDIName")) {
                  var3.setDestinationJNDIName(var4.getDestinationJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("DestinationType")) {
                  var3.setDestinationType(var4.getDestinationType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("InitialContextFactory")) {
                  var3.setInitialContextFactory(var4.getInitialContextFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            JMSBridgeDestinationMBeanImpl var5 = (JMSBridgeDestinationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ConnectionFactoryJNDIName")) && this.bean.isConnectionFactoryJNDINameSet()) {
               var5.setConnectionFactoryJNDIName(this.bean.getConnectionFactoryJNDIName());
            }

            if ((var3 == null || !var3.contains("ConnectionURL")) && this.bean.isConnectionURLSet()) {
               var5.setConnectionURL(this.bean.getConnectionURL());
            }

            if ((var3 == null || !var3.contains("DestinationJNDIName")) && this.bean.isDestinationJNDINameSet()) {
               var5.setDestinationJNDIName(this.bean.getDestinationJNDIName());
            }

            if ((var3 == null || !var3.contains("DestinationType")) && this.bean.isDestinationTypeSet()) {
               var5.setDestinationType(this.bean.getDestinationType());
            }

            if ((var3 == null || !var3.contains("InitialContextFactory")) && this.bean.isInitialContextFactorySet()) {
               var5.setInitialContextFactory(this.bean.getInitialContextFactory());
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
