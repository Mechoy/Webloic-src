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

public class HTTPProxyMBeanImpl extends DeploymentMBeanImpl implements HTTPProxyMBean, Serializable {
   private int _HealthCheckInterval;
   private int _InitialConnections;
   private int _MaxConnections;
   private int _MaxHealthCheckInterval;
   private int _MaxRetries;
   private String _ServerList;
   private static SchemaHelper2 _schemaHelper;

   public HTTPProxyMBeanImpl() {
      this._initializeProperty(-1);
   }

   public HTTPProxyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getInitialConnections() {
      return this._InitialConnections;
   }

   public boolean isInitialConnectionsSet() {
      return this._isSet(9);
   }

   public void setInitialConnections(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InitialConnections", (long)var1, 0L, 65535L);
      int var2 = this._InitialConnections;
      this._InitialConnections = var1;
      this._postSet(9, var2, var1);
   }

   public int getMaxConnections() {
      return this._MaxConnections;
   }

   public boolean isMaxConnectionsSet() {
      return this._isSet(10);
   }

   public void setMaxConnections(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxConnections", (long)var1, 1L, 65535L);
      int var2 = this._MaxConnections;
      this._MaxConnections = var1;
      this._postSet(10, var2, var1);
   }

   public String getServerList() {
      return this._ServerList;
   }

   public boolean isServerListSet() {
      return this._isSet(11);
   }

   public void setServerList(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerList;
      this._ServerList = var1;
      this._postSet(11, var2, var1);
   }

   public int getHealthCheckInterval() {
      return this._HealthCheckInterval;
   }

   public boolean isHealthCheckIntervalSet() {
      return this._isSet(12);
   }

   public void setHealthCheckInterval(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HealthCheckInterval", (long)var1, 1L, 300L);
      int var2 = this._HealthCheckInterval;
      this._HealthCheckInterval = var1;
      this._postSet(12, var2, var1);
   }

   public int getMaxRetries() {
      return this._MaxRetries;
   }

   public boolean isMaxRetriesSet() {
      return this._isSet(13);
   }

   public void setMaxRetries(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMax("MaxRetries", var1, 200);
      int var2 = this._MaxRetries;
      this._MaxRetries = var1;
      this._postSet(13, var2, var1);
   }

   public int getMaxHealthCheckInterval() {
      return this._MaxHealthCheckInterval;
   }

   public boolean isMaxHealthCheckIntervalSet() {
      return this._isSet(14);
   }

   public void setMaxHealthCheckInterval(int var1) {
      int var2 = this._MaxHealthCheckInterval;
      this._MaxHealthCheckInterval = var1;
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._HealthCheckInterval = 5;
               if (var2) {
                  break;
               }
            case 9:
               this._InitialConnections = 0;
               if (var2) {
                  break;
               }
            case 10:
               this._MaxConnections = 100;
               if (var2) {
                  break;
               }
            case 14:
               this._MaxHealthCheckInterval = 60;
               if (var2) {
                  break;
               }
            case 13:
               this._MaxRetries = 3;
               if (var2) {
                  break;
               }
            case 11:
               this._ServerList = null;
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
      return "HTTPProxy";
   }

   public void putValue(String var1, Object var2) {
      int var4;
      if (var1.equals("HealthCheckInterval")) {
         var4 = this._HealthCheckInterval;
         this._HealthCheckInterval = (Integer)var2;
         this._postSet(12, var4, this._HealthCheckInterval);
      } else if (var1.equals("InitialConnections")) {
         var4 = this._InitialConnections;
         this._InitialConnections = (Integer)var2;
         this._postSet(9, var4, this._InitialConnections);
      } else if (var1.equals("MaxConnections")) {
         var4 = this._MaxConnections;
         this._MaxConnections = (Integer)var2;
         this._postSet(10, var4, this._MaxConnections);
      } else if (var1.equals("MaxHealthCheckInterval")) {
         var4 = this._MaxHealthCheckInterval;
         this._MaxHealthCheckInterval = (Integer)var2;
         this._postSet(14, var4, this._MaxHealthCheckInterval);
      } else if (var1.equals("MaxRetries")) {
         var4 = this._MaxRetries;
         this._MaxRetries = (Integer)var2;
         this._postSet(13, var4, this._MaxRetries);
      } else if (var1.equals("ServerList")) {
         String var3 = this._ServerList;
         this._ServerList = (String)var2;
         this._postSet(11, var3, this._ServerList);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("HealthCheckInterval")) {
         return new Integer(this._HealthCheckInterval);
      } else if (var1.equals("InitialConnections")) {
         return new Integer(this._InitialConnections);
      } else if (var1.equals("MaxConnections")) {
         return new Integer(this._MaxConnections);
      } else if (var1.equals("MaxHealthCheckInterval")) {
         return new Integer(this._MaxHealthCheckInterval);
      } else if (var1.equals("MaxRetries")) {
         return new Integer(this._MaxRetries);
      } else {
         return var1.equals("ServerList") ? this._ServerList : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("max-retries")) {
                  return 13;
               }

               if (var1.equals("server-list")) {
                  return 11;
               }
            case 12:
            case 13:
            case 14:
            case 16:
            case 17:
            case 18:
            case 20:
            case 22:
            case 23:
            case 24:
            default:
               break;
            case 15:
               if (var1.equals("max-connections")) {
                  return 10;
               }
               break;
            case 19:
               if (var1.equals("initial-connections")) {
                  return 9;
               }
               break;
            case 21:
               if (var1.equals("health-check-interval")) {
                  return 12;
               }
               break;
            case 25:
               if (var1.equals("max-health-check-interval")) {
                  return 14;
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
            case 9:
               return "initial-connections";
            case 10:
               return "max-connections";
            case 11:
               return "server-list";
            case 12:
               return "health-check-interval";
            case 13:
               return "max-retries";
            case 14:
               return "max-health-check-interval";
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private HTTPProxyMBeanImpl bean;

      protected Helper(HTTPProxyMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "InitialConnections";
            case 10:
               return "MaxConnections";
            case 11:
               return "ServerList";
            case 12:
               return "HealthCheckInterval";
            case 13:
               return "MaxRetries";
            case 14:
               return "MaxHealthCheckInterval";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("HealthCheckInterval")) {
            return 12;
         } else if (var1.equals("InitialConnections")) {
            return 9;
         } else if (var1.equals("MaxConnections")) {
            return 10;
         } else if (var1.equals("MaxHealthCheckInterval")) {
            return 14;
         } else if (var1.equals("MaxRetries")) {
            return 13;
         } else {
            return var1.equals("ServerList") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isHealthCheckIntervalSet()) {
               var2.append("HealthCheckInterval");
               var2.append(String.valueOf(this.bean.getHealthCheckInterval()));
            }

            if (this.bean.isInitialConnectionsSet()) {
               var2.append("InitialConnections");
               var2.append(String.valueOf(this.bean.getInitialConnections()));
            }

            if (this.bean.isMaxConnectionsSet()) {
               var2.append("MaxConnections");
               var2.append(String.valueOf(this.bean.getMaxConnections()));
            }

            if (this.bean.isMaxHealthCheckIntervalSet()) {
               var2.append("MaxHealthCheckInterval");
               var2.append(String.valueOf(this.bean.getMaxHealthCheckInterval()));
            }

            if (this.bean.isMaxRetriesSet()) {
               var2.append("MaxRetries");
               var2.append(String.valueOf(this.bean.getMaxRetries()));
            }

            if (this.bean.isServerListSet()) {
               var2.append("ServerList");
               var2.append(String.valueOf(this.bean.getServerList()));
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
            HTTPProxyMBeanImpl var2 = (HTTPProxyMBeanImpl)var1;
            this.computeDiff("HealthCheckInterval", this.bean.getHealthCheckInterval(), var2.getHealthCheckInterval(), true);
            this.computeDiff("InitialConnections", this.bean.getInitialConnections(), var2.getInitialConnections(), false);
            this.computeDiff("MaxConnections", this.bean.getMaxConnections(), var2.getMaxConnections(), false);
            this.computeDiff("MaxHealthCheckInterval", this.bean.getMaxHealthCheckInterval(), var2.getMaxHealthCheckInterval(), false);
            this.computeDiff("MaxRetries", this.bean.getMaxRetries(), var2.getMaxRetries(), true);
            this.computeDiff("ServerList", this.bean.getServerList(), var2.getServerList(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            HTTPProxyMBeanImpl var3 = (HTTPProxyMBeanImpl)var1.getSourceBean();
            HTTPProxyMBeanImpl var4 = (HTTPProxyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("HealthCheckInterval")) {
                  var3.setHealthCheckInterval(var4.getHealthCheckInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("InitialConnections")) {
                  var3.setInitialConnections(var4.getInitialConnections());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MaxConnections")) {
                  var3.setMaxConnections(var4.getMaxConnections());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("MaxHealthCheckInterval")) {
                  var3.setMaxHealthCheckInterval(var4.getMaxHealthCheckInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("MaxRetries")) {
                  var3.setMaxRetries(var4.getMaxRetries());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("ServerList")) {
                  var3.setServerList(var4.getServerList());
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
            HTTPProxyMBeanImpl var5 = (HTTPProxyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("HealthCheckInterval")) && this.bean.isHealthCheckIntervalSet()) {
               var5.setHealthCheckInterval(this.bean.getHealthCheckInterval());
            }

            if ((var3 == null || !var3.contains("InitialConnections")) && this.bean.isInitialConnectionsSet()) {
               var5.setInitialConnections(this.bean.getInitialConnections());
            }

            if ((var3 == null || !var3.contains("MaxConnections")) && this.bean.isMaxConnectionsSet()) {
               var5.setMaxConnections(this.bean.getMaxConnections());
            }

            if ((var3 == null || !var3.contains("MaxHealthCheckInterval")) && this.bean.isMaxHealthCheckIntervalSet()) {
               var5.setMaxHealthCheckInterval(this.bean.getMaxHealthCheckInterval());
            }

            if ((var3 == null || !var3.contains("MaxRetries")) && this.bean.isMaxRetriesSet()) {
               var5.setMaxRetries(this.bean.getMaxRetries());
            }

            if ((var3 == null || !var3.contains("ServerList")) && this.bean.isServerListSet()) {
               var5.setServerList(this.bean.getServerList());
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
