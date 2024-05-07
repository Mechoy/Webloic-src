package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class StartupClassMBeanImpl extends ClassDeploymentMBeanImpl implements StartupClassMBean, Serializable {
   private boolean _FailureIsFatal;
   private boolean _LoadAfterAppsRunning;
   private boolean _LoadBeforeAppActivation;
   private boolean _LoadBeforeAppDeployments;
   private static SchemaHelper2 _schemaHelper;

   public StartupClassMBeanImpl() {
      this._initializeProperty(-1);
   }

   public StartupClassMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean getFailureIsFatal() {
      return this._FailureIsFatal;
   }

   public boolean isFailureIsFatalSet() {
      return this._isSet(11);
   }

   public void setFailureIsFatal(boolean var1) {
      boolean var2 = this._FailureIsFatal;
      this._FailureIsFatal = var1;
      this._postSet(11, var2, var1);
   }

   public boolean getLoadBeforeAppDeployments() {
      return this._LoadBeforeAppDeployments;
   }

   public boolean isLoadBeforeAppDeploymentsSet() {
      return this._isSet(12);
   }

   public void setLoadBeforeAppDeployments(boolean var1) {
      boolean var2 = this._LoadBeforeAppDeployments;
      this._LoadBeforeAppDeployments = var1;
      this._postSet(12, var2, var1);
   }

   public boolean getLoadBeforeAppActivation() {
      return this._LoadBeforeAppActivation;
   }

   public boolean isLoadBeforeAppActivationSet() {
      return this._isSet(13);
   }

   public void setLoadBeforeAppActivation(boolean var1) {
      boolean var2 = this._LoadBeforeAppActivation;
      this._LoadBeforeAppActivation = var1;
      this._postSet(13, var2, var1);
   }

   public boolean getLoadAfterAppsRunning() {
      return this._LoadAfterAppsRunning;
   }

   public boolean isLoadAfterAppsRunningSet() {
      return this._isSet(14);
   }

   public void setLoadAfterAppsRunning(boolean var1) {
      boolean var2 = this._LoadAfterAppsRunning;
      this._LoadAfterAppsRunning = var1;
      this._postSet(14, var2, var1);
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
               this._FailureIsFatal = false;
               if (var2) {
                  break;
               }
            case 14:
               this._LoadAfterAppsRunning = false;
               if (var2) {
                  break;
               }
            case 13:
               this._LoadBeforeAppActivation = false;
               if (var2) {
                  break;
               }
            case 12:
               this._LoadBeforeAppDeployments = false;
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
      return "StartupClass";
   }

   public void putValue(String var1, Object var2) {
      boolean var3;
      if (var1.equals("FailureIsFatal")) {
         var3 = this._FailureIsFatal;
         this._FailureIsFatal = (Boolean)var2;
         this._postSet(11, var3, this._FailureIsFatal);
      } else if (var1.equals("LoadAfterAppsRunning")) {
         var3 = this._LoadAfterAppsRunning;
         this._LoadAfterAppsRunning = (Boolean)var2;
         this._postSet(14, var3, this._LoadAfterAppsRunning);
      } else if (var1.equals("LoadBeforeAppActivation")) {
         var3 = this._LoadBeforeAppActivation;
         this._LoadBeforeAppActivation = (Boolean)var2;
         this._postSet(13, var3, this._LoadBeforeAppActivation);
      } else if (var1.equals("LoadBeforeAppDeployments")) {
         var3 = this._LoadBeforeAppDeployments;
         this._LoadBeforeAppDeployments = (Boolean)var2;
         this._postSet(12, var3, this._LoadBeforeAppDeployments);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("FailureIsFatal")) {
         return new Boolean(this._FailureIsFatal);
      } else if (var1.equals("LoadAfterAppsRunning")) {
         return new Boolean(this._LoadAfterAppsRunning);
      } else if (var1.equals("LoadBeforeAppActivation")) {
         return new Boolean(this._LoadBeforeAppActivation);
      } else {
         return var1.equals("LoadBeforeAppDeployments") ? new Boolean(this._LoadBeforeAppDeployments) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ClassDeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 16:
               if (var1.equals("failure-is-fatal")) {
                  return 11;
               }
               break;
            case 23:
               if (var1.equals("load-after-apps-running")) {
                  return 14;
               }
               break;
            case 26:
               if (var1.equals("load-before-app-activation")) {
                  return 13;
               }
               break;
            case 27:
               if (var1.equals("load-before-app-deployments")) {
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
            case 11:
               return "failure-is-fatal";
            case 12:
               return "load-before-app-deployments";
            case 13:
               return "load-before-app-activation";
            case 14:
               return "load-after-apps-running";
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

   protected static class Helper extends ClassDeploymentMBeanImpl.Helper {
      private StartupClassMBeanImpl bean;

      protected Helper(StartupClassMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 11:
               return "FailureIsFatal";
            case 12:
               return "LoadBeforeAppDeployments";
            case 13:
               return "LoadBeforeAppActivation";
            case 14:
               return "LoadAfterAppsRunning";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("FailureIsFatal")) {
            return 11;
         } else if (var1.equals("LoadAfterAppsRunning")) {
            return 14;
         } else if (var1.equals("LoadBeforeAppActivation")) {
            return 13;
         } else {
            return var1.equals("LoadBeforeAppDeployments") ? 12 : super.getPropertyIndex(var1);
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
            if (this.bean.isFailureIsFatalSet()) {
               var2.append("FailureIsFatal");
               var2.append(String.valueOf(this.bean.getFailureIsFatal()));
            }

            if (this.bean.isLoadAfterAppsRunningSet()) {
               var2.append("LoadAfterAppsRunning");
               var2.append(String.valueOf(this.bean.getLoadAfterAppsRunning()));
            }

            if (this.bean.isLoadBeforeAppActivationSet()) {
               var2.append("LoadBeforeAppActivation");
               var2.append(String.valueOf(this.bean.getLoadBeforeAppActivation()));
            }

            if (this.bean.isLoadBeforeAppDeploymentsSet()) {
               var2.append("LoadBeforeAppDeployments");
               var2.append(String.valueOf(this.bean.getLoadBeforeAppDeployments()));
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
            StartupClassMBeanImpl var2 = (StartupClassMBeanImpl)var1;
            this.computeDiff("FailureIsFatal", this.bean.getFailureIsFatal(), var2.getFailureIsFatal(), false);
            this.computeDiff("LoadAfterAppsRunning", this.bean.getLoadAfterAppsRunning(), var2.getLoadAfterAppsRunning(), false);
            this.computeDiff("LoadBeforeAppActivation", this.bean.getLoadBeforeAppActivation(), var2.getLoadBeforeAppActivation(), false);
            this.computeDiff("LoadBeforeAppDeployments", this.bean.getLoadBeforeAppDeployments(), var2.getLoadBeforeAppDeployments(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            StartupClassMBeanImpl var3 = (StartupClassMBeanImpl)var1.getSourceBean();
            StartupClassMBeanImpl var4 = (StartupClassMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("FailureIsFatal")) {
                  var3.setFailureIsFatal(var4.getFailureIsFatal());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("LoadAfterAppsRunning")) {
                  var3.setLoadAfterAppsRunning(var4.getLoadAfterAppsRunning());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("LoadBeforeAppActivation")) {
                  var3.setLoadBeforeAppActivation(var4.getLoadBeforeAppActivation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("LoadBeforeAppDeployments")) {
                  var3.setLoadBeforeAppDeployments(var4.getLoadBeforeAppDeployments());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
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
            StartupClassMBeanImpl var5 = (StartupClassMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("FailureIsFatal")) && this.bean.isFailureIsFatalSet()) {
               var5.setFailureIsFatal(this.bean.getFailureIsFatal());
            }

            if ((var3 == null || !var3.contains("LoadAfterAppsRunning")) && this.bean.isLoadAfterAppsRunningSet()) {
               var5.setLoadAfterAppsRunning(this.bean.getLoadAfterAppsRunning());
            }

            if ((var3 == null || !var3.contains("LoadBeforeAppActivation")) && this.bean.isLoadBeforeAppActivationSet()) {
               var5.setLoadBeforeAppActivation(this.bean.getLoadBeforeAppActivation());
            }

            if ((var3 == null || !var3.contains("LoadBeforeAppDeployments")) && this.bean.isLoadBeforeAppDeploymentsSet()) {
               var5.setLoadBeforeAppDeployments(this.bean.getLoadBeforeAppDeployments());
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
