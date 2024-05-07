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
import weblogic.management.DistributedManagementException;
import weblogic.utils.collections.CombinedIterator;

public class DeploymentConfigurationMBeanImpl extends ConfigurationMBeanImpl implements DeploymentConfigurationMBean, Serializable {
   private int _MaxAppVersions;
   private boolean _RemoteDeployerEJBEnabled;
   private boolean _RestageOnlyOnRedeploy;
   private static SchemaHelper2 _schemaHelper;

   public DeploymentConfigurationMBeanImpl() {
      this._initializeProperty(-1);
   }

   public DeploymentConfigurationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getMaxAppVersions() {
      return this._MaxAppVersions;
   }

   public boolean isMaxAppVersionsSet() {
      return this._isSet(7);
   }

   public void setMaxAppVersions(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxAppVersions", (long)var1, 1L, 65534L);
      int var2 = this._MaxAppVersions;
      this._MaxAppVersions = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isRemoteDeployerEJBEnabled() {
      return this._RemoteDeployerEJBEnabled;
   }

   public boolean isRemoteDeployerEJBEnabledSet() {
      return this._isSet(8);
   }

   public void setRemoteDeployerEJBEnabled(boolean var1) {
      boolean var2 = this._RemoteDeployerEJBEnabled;
      this._RemoteDeployerEJBEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isRestageOnlyOnRedeploy() {
      return this._RestageOnlyOnRedeploy;
   }

   public boolean isRestageOnlyOnRedeploySet() {
      return this._isSet(9);
   }

   public void setRestageOnlyOnRedeploy(boolean var1) {
      boolean var2 = this._RestageOnlyOnRedeploy;
      this._RestageOnlyOnRedeploy = var1;
      this._postSet(9, var2, var1);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._MaxAppVersions = 2;
               if (var2) {
                  break;
               }
            case 8:
               this._RemoteDeployerEJBEnabled = false;
               if (var2) {
                  break;
               }
            case 9:
               this._RestageOnlyOnRedeploy = false;
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
      return "DeploymentConfiguration";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("MaxAppVersions")) {
         int var4 = this._MaxAppVersions;
         this._MaxAppVersions = (Integer)var2;
         this._postSet(7, var4, this._MaxAppVersions);
      } else {
         boolean var3;
         if (var1.equals("RemoteDeployerEJBEnabled")) {
            var3 = this._RemoteDeployerEJBEnabled;
            this._RemoteDeployerEJBEnabled = (Boolean)var2;
            this._postSet(8, var3, this._RemoteDeployerEJBEnabled);
         } else if (var1.equals("RestageOnlyOnRedeploy")) {
            var3 = this._RestageOnlyOnRedeploy;
            this._RestageOnlyOnRedeploy = (Boolean)var2;
            this._postSet(9, var3, this._RestageOnlyOnRedeploy);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("MaxAppVersions")) {
         return new Integer(this._MaxAppVersions);
      } else if (var1.equals("RemoteDeployerEJBEnabled")) {
         return new Boolean(this._RemoteDeployerEJBEnabled);
      } else {
         return var1.equals("RestageOnlyOnRedeploy") ? new Boolean(this._RestageOnlyOnRedeploy) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 16:
               if (var1.equals("max-app-versions")) {
                  return 7;
               }
               break;
            case 24:
               if (var1.equals("restage-only-on-redeploy")) {
                  return 9;
               }
               break;
            case 26:
               if (var1.equals("remote-deployerejb-enabled")) {
                  return 8;
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
               return "max-app-versions";
            case 8:
               return "remote-deployerejb-enabled";
            case 9:
               return "restage-only-on-redeploy";
            default:
               return super.getElementName(var1);
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
      private DeploymentConfigurationMBeanImpl bean;

      protected Helper(DeploymentConfigurationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MaxAppVersions";
            case 8:
               return "RemoteDeployerEJBEnabled";
            case 9:
               return "RestageOnlyOnRedeploy";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxAppVersions")) {
            return 7;
         } else if (var1.equals("RemoteDeployerEJBEnabled")) {
            return 8;
         } else {
            return var1.equals("RestageOnlyOnRedeploy") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxAppVersionsSet()) {
               var2.append("MaxAppVersions");
               var2.append(String.valueOf(this.bean.getMaxAppVersions()));
            }

            if (this.bean.isRemoteDeployerEJBEnabledSet()) {
               var2.append("RemoteDeployerEJBEnabled");
               var2.append(String.valueOf(this.bean.isRemoteDeployerEJBEnabled()));
            }

            if (this.bean.isRestageOnlyOnRedeploySet()) {
               var2.append("RestageOnlyOnRedeploy");
               var2.append(String.valueOf(this.bean.isRestageOnlyOnRedeploy()));
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
            DeploymentConfigurationMBeanImpl var2 = (DeploymentConfigurationMBeanImpl)var1;
            this.computeDiff("MaxAppVersions", this.bean.getMaxAppVersions(), var2.getMaxAppVersions(), true);
            this.computeDiff("RemoteDeployerEJBEnabled", this.bean.isRemoteDeployerEJBEnabled(), var2.isRemoteDeployerEJBEnabled(), false);
            this.computeDiff("RestageOnlyOnRedeploy", this.bean.isRestageOnlyOnRedeploy(), var2.isRestageOnlyOnRedeploy(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DeploymentConfigurationMBeanImpl var3 = (DeploymentConfigurationMBeanImpl)var1.getSourceBean();
            DeploymentConfigurationMBeanImpl var4 = (DeploymentConfigurationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxAppVersions")) {
                  var3.setMaxAppVersions(var4.getMaxAppVersions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RemoteDeployerEJBEnabled")) {
                  var3.setRemoteDeployerEJBEnabled(var4.isRemoteDeployerEJBEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("RestageOnlyOnRedeploy")) {
                  var3.setRestageOnlyOnRedeploy(var4.isRestageOnlyOnRedeploy());
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
            DeploymentConfigurationMBeanImpl var5 = (DeploymentConfigurationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxAppVersions")) && this.bean.isMaxAppVersionsSet()) {
               var5.setMaxAppVersions(this.bean.getMaxAppVersions());
            }

            if ((var3 == null || !var3.contains("RemoteDeployerEJBEnabled")) && this.bean.isRemoteDeployerEJBEnabledSet()) {
               var5.setRemoteDeployerEJBEnabled(this.bean.isRemoteDeployerEJBEnabled());
            }

            if ((var3 == null || !var3.contains("RestageOnlyOnRedeploy")) && this.bean.isRestageOnlyOnRedeploySet()) {
               var5.setRestageOnlyOnRedeploy(this.bean.isRestageOnlyOnRedeploy());
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
