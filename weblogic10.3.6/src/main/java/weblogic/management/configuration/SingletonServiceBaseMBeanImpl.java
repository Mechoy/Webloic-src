package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.SingletonService;
import weblogic.utils.collections.CombinedIterator;

public class SingletonServiceBaseMBeanImpl extends ConfigurationMBeanImpl implements SingletonServiceBaseMBean, Serializable {
   private int _AdditionalMigrationAttempts;
   private ServerMBean _HostingServer;
   private int _MillisToSleepBetweenAttempts;
   private String _Name;
   private ServerMBean _UserPreferredServer;
   private SingletonService _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SingletonServiceBaseMBeanImpl() {
      try {
         this._customizer = new SingletonService(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SingletonServiceBaseMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new SingletonService(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ServerMBean getHostingServer() {
      return this._customizer.getHostingServer();
   }

   public String getHostingServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getHostingServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isHostingServerSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setHostingServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  SingletonServiceBaseMBeanImpl.this.setHostingServer((ServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ServerMBean var2 = this._HostingServer;
         this._initializeProperty(7);
         this._postSet(7, var2, this._HostingServer);
      }

   }

   public void setHostingServer(ServerMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SingletonServiceBaseMBeanImpl.this.getHostingServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this._HostingServer;
      this._HostingServer = var1;
      this._postSet(7, var3, var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public ServerMBean getUserPreferredServer() {
      return this._customizer.getUserPreferredServer();
   }

   public String getUserPreferredServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getUserPreferredServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isUserPreferredServerSet() {
      return this._isSet(8);
   }

   public void setUserPreferredServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  SingletonServiceBaseMBeanImpl.this.setUserPreferredServer((ServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ServerMBean var2 = this._UserPreferredServer;
         this._initializeProperty(8);
         this._postSet(8, var2, this._UserPreferredServer);
      }

   }

   public void setUserPreferredServer(ServerMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SingletonServiceBaseMBeanImpl.this.getUserPreferredServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this.getUserPreferredServer();
      this._customizer.setUserPreferredServer(var1);
      this._postSet(8, var3, var1);
   }

   public int getAdditionalMigrationAttempts() {
      return this._AdditionalMigrationAttempts;
   }

   public boolean isAdditionalMigrationAttemptsSet() {
      return this._isSet(9);
   }

   public void setAdditionalMigrationAttempts(int var1) {
      int var2 = this._AdditionalMigrationAttempts;
      this._AdditionalMigrationAttempts = var1;
      this._postSet(9, var2, var1);
   }

   public int getMillisToSleepBetweenAttempts() {
      return this._MillisToSleepBetweenAttempts;
   }

   public boolean isMillisToSleepBetweenAttemptsSet() {
      return this._isSet(10);
   }

   public void setMillisToSleepBetweenAttempts(int var1) {
      int var2 = this._MillisToSleepBetweenAttempts;
      this._MillisToSleepBetweenAttempts = var1;
      this._postSet(10, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
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
               this._AdditionalMigrationAttempts = 2;
               if (var2) {
                  break;
               }
            case 7:
               this._HostingServer = null;
               if (var2) {
                  break;
               }
            case 10:
               this._MillisToSleepBetweenAttempts = 300000;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setUserPreferredServer((ServerMBean)null);
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
      return "SingletonServiceBase";
   }

   public void putValue(String var1, Object var2) {
      int var6;
      if (var1.equals("AdditionalMigrationAttempts")) {
         var6 = this._AdditionalMigrationAttempts;
         this._AdditionalMigrationAttempts = (Integer)var2;
         this._postSet(9, var6, this._AdditionalMigrationAttempts);
      } else {
         ServerMBean var4;
         if (var1.equals("HostingServer")) {
            var4 = this._HostingServer;
            this._HostingServer = (ServerMBean)var2;
            this._postSet(7, var4, this._HostingServer);
         } else if (var1.equals("MillisToSleepBetweenAttempts")) {
            var6 = this._MillisToSleepBetweenAttempts;
            this._MillisToSleepBetweenAttempts = (Integer)var2;
            this._postSet(10, var6, this._MillisToSleepBetweenAttempts);
         } else if (var1.equals("Name")) {
            String var5 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var5, this._Name);
         } else if (var1.equals("UserPreferredServer")) {
            var4 = this._UserPreferredServer;
            this._UserPreferredServer = (ServerMBean)var2;
            this._postSet(8, var4, this._UserPreferredServer);
         } else if (var1.equals("customizer")) {
            SingletonService var3 = this._customizer;
            this._customizer = (SingletonService)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AdditionalMigrationAttempts")) {
         return new Integer(this._AdditionalMigrationAttempts);
      } else if (var1.equals("HostingServer")) {
         return this._HostingServer;
      } else if (var1.equals("MillisToSleepBetweenAttempts")) {
         return new Integer(this._MillisToSleepBetweenAttempts);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("UserPreferredServer")) {
         return this._UserPreferredServer;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
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
            case 14:
               if (var1.equals("hosting-server")) {
                  return 7;
               }
               break;
            case 21:
               if (var1.equals("user-preferred-server")) {
                  return 8;
               }
               break;
            case 29:
               if (var1.equals("additional-migration-attempts")) {
                  return 9;
               }
               break;
            case 32:
               if (var1.equals("millis-to-sleep-between-attempts")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "hosting-server";
            case 8:
               return "user-preferred-server";
            case 9:
               return "additional-migration-attempts";
            case 10:
               return "millis-to-sleep-between-attempts";
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private SingletonServiceBaseMBeanImpl bean;

      protected Helper(SingletonServiceBaseMBeanImpl var1) {
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
               return "HostingServer";
            case 8:
               return "UserPreferredServer";
            case 9:
               return "AdditionalMigrationAttempts";
            case 10:
               return "MillisToSleepBetweenAttempts";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AdditionalMigrationAttempts")) {
            return 9;
         } else if (var1.equals("HostingServer")) {
            return 7;
         } else if (var1.equals("MillisToSleepBetweenAttempts")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else {
            return var1.equals("UserPreferredServer") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isAdditionalMigrationAttemptsSet()) {
               var2.append("AdditionalMigrationAttempts");
               var2.append(String.valueOf(this.bean.getAdditionalMigrationAttempts()));
            }

            if (this.bean.isHostingServerSet()) {
               var2.append("HostingServer");
               var2.append(String.valueOf(this.bean.getHostingServer()));
            }

            if (this.bean.isMillisToSleepBetweenAttemptsSet()) {
               var2.append("MillisToSleepBetweenAttempts");
               var2.append(String.valueOf(this.bean.getMillisToSleepBetweenAttempts()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isUserPreferredServerSet()) {
               var2.append("UserPreferredServer");
               var2.append(String.valueOf(this.bean.getUserPreferredServer()));
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
            SingletonServiceBaseMBeanImpl var2 = (SingletonServiceBaseMBeanImpl)var1;
            this.computeDiff("AdditionalMigrationAttempts", this.bean.getAdditionalMigrationAttempts(), var2.getAdditionalMigrationAttempts(), false);
            this.computeDiff("HostingServer", this.bean.getHostingServer(), var2.getHostingServer(), true);
            this.computeDiff("MillisToSleepBetweenAttempts", this.bean.getMillisToSleepBetweenAttempts(), var2.getMillisToSleepBetweenAttempts(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("UserPreferredServer", this.bean.getUserPreferredServer(), var2.getUserPreferredServer(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SingletonServiceBaseMBeanImpl var3 = (SingletonServiceBaseMBeanImpl)var1.getSourceBean();
            SingletonServiceBaseMBeanImpl var4 = (SingletonServiceBaseMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AdditionalMigrationAttempts")) {
                  var3.setAdditionalMigrationAttempts(var4.getAdditionalMigrationAttempts());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("HostingServer")) {
                  var3.setHostingServerAsString(var4.getHostingServerAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("MillisToSleepBetweenAttempts")) {
                  var3.setMillisToSleepBetweenAttempts(var4.getMillisToSleepBetweenAttempts());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("UserPreferredServer")) {
                  var3.setUserPreferredServerAsString(var4.getUserPreferredServerAsString());
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
            SingletonServiceBaseMBeanImpl var5 = (SingletonServiceBaseMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AdditionalMigrationAttempts")) && this.bean.isAdditionalMigrationAttemptsSet()) {
               var5.setAdditionalMigrationAttempts(this.bean.getAdditionalMigrationAttempts());
            }

            if ((var3 == null || !var3.contains("HostingServer")) && this.bean.isHostingServerSet()) {
               var5._unSet(var5, 7);
               var5.setHostingServerAsString(this.bean.getHostingServerAsString());
            }

            if ((var3 == null || !var3.contains("MillisToSleepBetweenAttempts")) && this.bean.isMillisToSleepBetweenAttemptsSet()) {
               var5.setMillisToSleepBetweenAttempts(this.bean.getMillisToSleepBetweenAttempts());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("UserPreferredServer")) && this.bean.isUserPreferredServerSet()) {
               var5._unSet(var5, 8);
               var5.setUserPreferredServerAsString(this.bean.getUserPreferredServerAsString());
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
         this.inferSubTree(this.bean.getHostingServer(), var1, var2);
         this.inferSubTree(this.bean.getUserPreferredServer(), var1, var2);
      }
   }
}
