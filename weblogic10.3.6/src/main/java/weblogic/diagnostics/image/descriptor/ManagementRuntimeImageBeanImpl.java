package weblogic.diagnostics.image.descriptor;

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
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ManagementRuntimeImageBeanImpl extends AbstractDescriptorBean implements ManagementRuntimeImageBean, Serializable {
   private ServerRuntimeStateBean _ServerRuntimeState;
   private ServerRuntimeStatisticsBean _ServerRuntimeStatistics;
   private static SchemaHelper2 _schemaHelper;

   public ManagementRuntimeImageBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public ManagementRuntimeImageBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public ServerRuntimeStatisticsBean getServerRuntimeStatistics() {
      return this._ServerRuntimeStatistics;
   }

   public boolean isServerRuntimeStatisticsSet() {
      return this._isSet(0) || this._isAnythingSet((AbstractDescriptorBean)this.getServerRuntimeStatistics());
   }

   public void setServerRuntimeStatistics(ServerRuntimeStatisticsBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 0)) {
         this._postCreate(var2);
      }

      ServerRuntimeStatisticsBean var3 = this._ServerRuntimeStatistics;
      this._ServerRuntimeStatistics = var1;
      this._postSet(0, var3, var1);
   }

   public ServerRuntimeStateBean getServerRuntimeState() {
      return this._ServerRuntimeState;
   }

   public boolean isServerRuntimeStateSet() {
      return this._isSet(1) || this._isAnythingSet((AbstractDescriptorBean)this.getServerRuntimeState());
   }

   public void setServerRuntimeState(ServerRuntimeStateBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 1)) {
         this._postCreate(var2);
      }

      ServerRuntimeStateBean var3 = this._ServerRuntimeState;
      this._ServerRuntimeState = var1;
      this._postSet(1, var3, var1);
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
      return super._isAnythingSet() || this.isServerRuntimeStateSet() || this.isServerRuntimeStatisticsSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 1;
      }

      try {
         switch (var1) {
            case 1:
               this._ServerRuntimeState = new ServerRuntimeStateBeanImpl(this, 1);
               this._postCreate((AbstractDescriptorBean)this._ServerRuntimeState);
               if (var2) {
                  break;
               }
            case 0:
               this._ServerRuntimeStatistics = new ServerRuntimeStatisticsBeanImpl(this, 0);
               this._postCreate((AbstractDescriptorBean)this._ServerRuntimeStatistics);
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 20:
               if (var1.equals("server-runtime-state")) {
                  return 1;
               }
               break;
            case 25:
               if (var1.equals("server-runtime-statistics")) {
                  return 0;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 0:
               return new ServerRuntimeStatisticsBeanImpl.SchemaHelper2();
            case 1:
               return new ServerRuntimeStateBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getRootElementName() {
         return "management-runtime-image";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "server-runtime-statistics";
            case 1:
               return "server-runtime-state";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 0:
               return true;
            case 1:
               return true;
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private ManagementRuntimeImageBeanImpl bean;

      protected Helper(ManagementRuntimeImageBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "ServerRuntimeStatistics";
            case 1:
               return "ServerRuntimeState";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ServerRuntimeState")) {
            return 1;
         } else {
            return var1.equals("ServerRuntimeStatistics") ? 0 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getServerRuntimeState() != null) {
            var1.add(new ArrayIterator(new ServerRuntimeStateBean[]{this.bean.getServerRuntimeState()}));
         }

         if (this.bean.getServerRuntimeStatistics() != null) {
            var1.add(new ArrayIterator(new ServerRuntimeStatisticsBean[]{this.bean.getServerRuntimeStatistics()}));
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
            var5 = this.computeChildHashValue(this.bean.getServerRuntimeState());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getServerRuntimeStatistics());
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
            ManagementRuntimeImageBeanImpl var2 = (ManagementRuntimeImageBeanImpl)var1;
            this.computeSubDiff("ServerRuntimeState", this.bean.getServerRuntimeState(), var2.getServerRuntimeState());
            this.computeSubDiff("ServerRuntimeStatistics", this.bean.getServerRuntimeStatistics(), var2.getServerRuntimeStatistics());
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ManagementRuntimeImageBeanImpl var3 = (ManagementRuntimeImageBeanImpl)var1.getSourceBean();
            ManagementRuntimeImageBeanImpl var4 = (ManagementRuntimeImageBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ServerRuntimeState")) {
                  if (var6 == 2) {
                     var3.setServerRuntimeState((ServerRuntimeStateBean)this.createCopy((AbstractDescriptorBean)var4.getServerRuntimeState()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("ServerRuntimeState", (DescriptorBean)var3.getServerRuntimeState());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("ServerRuntimeStatistics")) {
                  if (var6 == 2) {
                     var3.setServerRuntimeStatistics((ServerRuntimeStatisticsBean)this.createCopy((AbstractDescriptorBean)var4.getServerRuntimeStatistics()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("ServerRuntimeStatistics", (DescriptorBean)var3.getServerRuntimeStatistics());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
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
            ManagementRuntimeImageBeanImpl var5 = (ManagementRuntimeImageBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ServerRuntimeState")) && this.bean.isServerRuntimeStateSet() && !var5._isSet(1)) {
               ServerRuntimeStateBean var4 = this.bean.getServerRuntimeState();
               var5.setServerRuntimeState((ServerRuntimeStateBean)null);
               var5.setServerRuntimeState(var4 == null ? null : (ServerRuntimeStateBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("ServerRuntimeStatistics")) && this.bean.isServerRuntimeStatisticsSet() && !var5._isSet(0)) {
               ServerRuntimeStatisticsBean var8 = this.bean.getServerRuntimeStatistics();
               var5.setServerRuntimeStatistics((ServerRuntimeStatisticsBean)null);
               var5.setServerRuntimeStatistics(var8 == null ? null : (ServerRuntimeStatisticsBean)this.createCopy((AbstractDescriptorBean)var8, var2));
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
         this.inferSubTree(this.bean.getServerRuntimeState(), var1, var2);
         this.inferSubTree(this.bean.getServerRuntimeStatistics(), var1, var2);
      }
   }
}
