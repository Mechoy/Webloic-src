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
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class MachineMBeanImpl extends ConfigurationMBeanImpl implements MachineMBean, Serializable {
   private String[] _Addresses;
   private NodeManagerMBean _NodeManager;
   private static SchemaHelper2 _schemaHelper;

   public MachineMBeanImpl() {
      this._initializeProperty(-1);
   }

   public MachineMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String[] getAddresses() {
      return this._Addresses;
   }

   public boolean isAddressesSet() {
      return this._isSet(7);
   }

   public void setAddresses(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Addresses;
      this._Addresses = var1;
      this._postSet(7, var2, var1);
   }

   public NodeManagerMBean getNodeManager() {
      return this._NodeManager;
   }

   public boolean isNodeManagerSet() {
      return this._isSet(8) || this._isAnythingSet((AbstractDescriptorBean)this.getNodeManager());
   }

   public void setNodeManager(NodeManagerMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 8)) {
         this._postCreate(var2);
      }

      NodeManagerMBean var3 = this._NodeManager;
      this._NodeManager = var1;
      this._postSet(8, var3, var1);
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
      return super._isAnythingSet() || this.isNodeManagerSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._Addresses = new String[0];
               if (var2) {
                  break;
               }
            case 8:
               this._NodeManager = new NodeManagerMBeanImpl(this, 8);
               this._postCreate((AbstractDescriptorBean)this._NodeManager);
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
      return "Machine";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Addresses")) {
         String[] var4 = this._Addresses;
         this._Addresses = (String[])((String[])var2);
         this._postSet(7, var4, this._Addresses);
      } else if (var1.equals("NodeManager")) {
         NodeManagerMBean var3 = this._NodeManager;
         this._NodeManager = (NodeManagerMBean)var2;
         this._postSet(8, var3, this._NodeManager);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Addresses")) {
         return this._Addresses;
      } else {
         return var1.equals("NodeManager") ? this._NodeManager : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("address")) {
                  return 7;
               }
               break;
            case 12:
               if (var1.equals("node-manager")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 8:
               return new NodeManagerMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "address";
            case 8:
               return "node-manager";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 8:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isConfigurable(var1);
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
      private MachineMBeanImpl bean;

      protected Helper(MachineMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Addresses";
            case 8:
               return "NodeManager";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Addresses")) {
            return 7;
         } else {
            return var1.equals("NodeManager") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getNodeManager() != null) {
            var1.add(new ArrayIterator(new NodeManagerMBean[]{this.bean.getNodeManager()}));
         }

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
            if (this.bean.isAddressesSet()) {
               var2.append("Addresses");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAddresses())));
            }

            var5 = this.computeChildHashValue(this.bean.getNodeManager());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
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
            MachineMBeanImpl var2 = (MachineMBeanImpl)var1;
            this.computeDiff("Addresses", this.bean.getAddresses(), var2.getAddresses(), false);
            this.computeSubDiff("NodeManager", this.bean.getNodeManager(), var2.getNodeManager());
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            MachineMBeanImpl var3 = (MachineMBeanImpl)var1.getSourceBean();
            MachineMBeanImpl var4 = (MachineMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Addresses")) {
                  var3.setAddresses(var4.getAddresses());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("NodeManager")) {
                  if (var6 == 2) {
                     var3.setNodeManager((NodeManagerMBean)this.createCopy((AbstractDescriptorBean)var4.getNodeManager()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("NodeManager", var3.getNodeManager());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            MachineMBeanImpl var5 = (MachineMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Addresses")) && this.bean.isAddressesSet()) {
               String[] var4 = this.bean.getAddresses();
               var5.setAddresses(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("NodeManager")) && this.bean.isNodeManagerSet() && !var5._isSet(8)) {
               NodeManagerMBean var8 = this.bean.getNodeManager();
               var5.setNodeManager((NodeManagerMBean)null);
               var5.setNodeManager(var8 == null ? null : (NodeManagerMBean)this.createCopy((AbstractDescriptorBean)var8, var2));
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
         this.inferSubTree(this.bean.getNodeManager(), var1, var2);
      }
   }
}
