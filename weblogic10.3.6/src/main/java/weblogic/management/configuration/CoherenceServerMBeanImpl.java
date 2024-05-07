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
import weblogic.management.mbeans.custom.CoherenceServer;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class CoherenceServerMBeanImpl extends ManagedExternalServerMBeanImpl implements CoherenceServerMBean, Serializable {
   private CoherenceClusterSystemResourceMBean _CoherenceClusterSystemResource;
   private CoherenceServerStartMBean _CoherenceServerStart;
   private ManagedExternalServerStartMBean _ManagedExternalServerStart;
   private String _Name;
   private String _UnicastListenAddress;
   private int _UnicastListenPort;
   private boolean _UnicastPortAutoAdjust;
   private CoherenceServer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public CoherenceServerMBeanImpl() {
      try {
         this._customizer = new CoherenceServer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public CoherenceServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new CoherenceServer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      return this._customizer.getName();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 14, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return CoherenceServerMBeanImpl.this.getCoherenceClusterSystemResource();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      CoherenceClusterSystemResourceMBean var3 = this._CoherenceClusterSystemResource;
      this._CoherenceClusterSystemResource = var1;
      this._postSet(14, var3, var1);
   }

   public CoherenceClusterSystemResourceMBean getCoherenceClusterSystemResource() {
      return this._CoherenceClusterSystemResource;
   }

   public String getCoherenceClusterSystemResourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCoherenceClusterSystemResource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isCoherenceClusterSystemResourceSet() {
      return this._isSet(14);
   }

   public void setCoherenceClusterSystemResourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, CoherenceClusterSystemResourceMBean.class, new ReferenceManager.Resolver(this, 14) {
            public void resolveReference(Object var1) {
               try {
                  CoherenceServerMBeanImpl.this.setCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         CoherenceClusterSystemResourceMBean var2 = this._CoherenceClusterSystemResource;
         this._initializeProperty(14);
         this._postSet(14, var2, this._CoherenceClusterSystemResource);
      }

   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public String getUnicastListenAddress() {
      return this._UnicastListenAddress;
   }

   public boolean isUnicastListenAddressSet() {
      return this._isSet(15);
   }

   public void setUnicastListenAddress(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UnicastListenAddress;
      this._UnicastListenAddress = var1;
      this._postSet(15, var2, var1);
   }

   public int getUnicastListenPort() {
      return this._UnicastListenPort;
   }

   public boolean isUnicastListenPortSet() {
      return this._isSet(16);
   }

   public void setUnicastListenPort(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("UnicastListenPort", (long)var1, 1L, 65535L);
      int var2 = this._UnicastListenPort;
      this._UnicastListenPort = var1;
      this._postSet(16, var2, var1);
   }

   public boolean isUnicastPortAutoAdjust() {
      return this._UnicastPortAutoAdjust;
   }

   public boolean isUnicastPortAutoAdjustSet() {
      return this._isSet(17);
   }

   public void setUnicastPortAutoAdjust(boolean var1) {
      boolean var2 = this._UnicastPortAutoAdjust;
      this._UnicastPortAutoAdjust = var1;
      this._postSet(17, var2, var1);
   }

   public CoherenceServerStartMBean getCoherenceServerStart() {
      return this._CoherenceServerStart;
   }

   public boolean isCoherenceServerStartSet() {
      return this._isSet(18) || this._isAnythingSet((AbstractDescriptorBean)this.getCoherenceServerStart());
   }

   public void setCoherenceServerStart(CoherenceServerStartMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 18)) {
         this._postCreate(var2);
      }

      CoherenceServerStartMBean var3 = this._CoherenceServerStart;
      this._CoherenceServerStart = var1;
      this._postSet(18, var3, var1);
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

   public ManagedExternalServerStartMBean getManagedExternalServerStart() {
      return this._customizer.getManagedExternalServerStart();
   }

   public boolean isManagedExternalServerStartSet() {
      return this._isSet(13);
   }

   public void setManagedExternalServerStart(ManagedExternalServerStartMBean var1) throws InvalidAttributeValueException {
      this._ManagedExternalServerStart = var1;
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
      return super._isAnythingSet() || this.isCoherenceServerStartSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._CoherenceClusterSystemResource = null;
               if (var2) {
                  break;
               }
            case 18:
               this._CoherenceServerStart = new CoherenceServerStartMBeanImpl(this, 18);
               this._postCreate((AbstractDescriptorBean)this._CoherenceServerStart);
               if (var2) {
                  break;
               }
            case 13:
               this._ManagedExternalServerStart = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._UnicastListenAddress = null;
               if (var2) {
                  break;
               }
            case 16:
               this._UnicastListenPort = 0;
               if (var2) {
                  break;
               }
            case 17:
               this._UnicastPortAutoAdjust = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
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
      return "CoherenceServer";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CoherenceClusterSystemResource")) {
         CoherenceClusterSystemResourceMBean var9 = this._CoherenceClusterSystemResource;
         this._CoherenceClusterSystemResource = (CoherenceClusterSystemResourceMBean)var2;
         this._postSet(14, var9, this._CoherenceClusterSystemResource);
      } else if (var1.equals("CoherenceServerStart")) {
         CoherenceServerStartMBean var8 = this._CoherenceServerStart;
         this._CoherenceServerStart = (CoherenceServerStartMBean)var2;
         this._postSet(18, var8, this._CoherenceServerStart);
      } else if (var1.equals("ManagedExternalServerStart")) {
         ManagedExternalServerStartMBean var7 = this._ManagedExternalServerStart;
         this._ManagedExternalServerStart = (ManagedExternalServerStartMBean)var2;
         this._postSet(13, var7, this._ManagedExternalServerStart);
      } else {
         String var6;
         if (var1.equals("Name")) {
            var6 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var6, this._Name);
         } else if (var1.equals("UnicastListenAddress")) {
            var6 = this._UnicastListenAddress;
            this._UnicastListenAddress = (String)var2;
            this._postSet(15, var6, this._UnicastListenAddress);
         } else if (var1.equals("UnicastListenPort")) {
            int var5 = this._UnicastListenPort;
            this._UnicastListenPort = (Integer)var2;
            this._postSet(16, var5, this._UnicastListenPort);
         } else if (var1.equals("UnicastPortAutoAdjust")) {
            boolean var4 = this._UnicastPortAutoAdjust;
            this._UnicastPortAutoAdjust = (Boolean)var2;
            this._postSet(17, var4, this._UnicastPortAutoAdjust);
         } else if (var1.equals("customizer")) {
            CoherenceServer var3 = this._customizer;
            this._customizer = (CoherenceServer)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CoherenceClusterSystemResource")) {
         return this._CoherenceClusterSystemResource;
      } else if (var1.equals("CoherenceServerStart")) {
         return this._CoherenceServerStart;
      } else if (var1.equals("ManagedExternalServerStart")) {
         return this._ManagedExternalServerStart;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("UnicastListenAddress")) {
         return this._UnicastListenAddress;
      } else if (var1.equals("UnicastListenPort")) {
         return new Integer(this._UnicastListenPort);
      } else if (var1.equals("UnicastPortAutoAdjust")) {
         return new Boolean(this._UnicastPortAutoAdjust);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ManagedExternalServerMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 19:
               if (var1.equals("unicast-listen-port")) {
                  return 16;
               }
               break;
            case 22:
               if (var1.equals("coherence-server-start")) {
                  return 18;
               }

               if (var1.equals("unicast-listen-address")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("unicast-port-auto-adjust")) {
                  return 17;
               }
               break;
            case 29:
               if (var1.equals("managed-external-server-start")) {
                  return 13;
               }
               break;
            case 33:
               if (var1.equals("coherence-cluster-system-resource")) {
                  return 14;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 18:
               return new CoherenceServerStartMBeanImpl.SchemaHelper2();
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
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            default:
               return super.getElementName(var1);
            case 13:
               return "managed-external-server-start";
            case 14:
               return "coherence-cluster-system-resource";
            case 15:
               return "unicast-listen-address";
            case 16:
               return "unicast-listen-port";
            case 17:
               return "unicast-port-auto-adjust";
            case 18:
               return "coherence-server-start";
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 18:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 11:
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ManagedExternalServerMBeanImpl.Helper {
      private CoherenceServerMBeanImpl bean;

      protected Helper(CoherenceServerMBeanImpl var1) {
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
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            default:
               return super.getPropertyName(var1);
            case 13:
               return "ManagedExternalServerStart";
            case 14:
               return "CoherenceClusterSystemResource";
            case 15:
               return "UnicastListenAddress";
            case 16:
               return "UnicastListenPort";
            case 17:
               return "UnicastPortAutoAdjust";
            case 18:
               return "CoherenceServerStart";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CoherenceClusterSystemResource")) {
            return 14;
         } else if (var1.equals("CoherenceServerStart")) {
            return 18;
         } else if (var1.equals("ManagedExternalServerStart")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("UnicastListenAddress")) {
            return 15;
         } else if (var1.equals("UnicastListenPort")) {
            return 16;
         } else {
            return var1.equals("UnicastPortAutoAdjust") ? 17 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getCoherenceServerStart() != null) {
            var1.add(new ArrayIterator(new CoherenceServerStartMBean[]{this.bean.getCoherenceServerStart()}));
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
            if (this.bean.isCoherenceClusterSystemResourceSet()) {
               var2.append("CoherenceClusterSystemResource");
               var2.append(String.valueOf(this.bean.getCoherenceClusterSystemResource()));
            }

            var5 = this.computeChildHashValue(this.bean.getCoherenceServerStart());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isManagedExternalServerStartSet()) {
               var2.append("ManagedExternalServerStart");
               var2.append(String.valueOf(this.bean.getManagedExternalServerStart()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isUnicastListenAddressSet()) {
               var2.append("UnicastListenAddress");
               var2.append(String.valueOf(this.bean.getUnicastListenAddress()));
            }

            if (this.bean.isUnicastListenPortSet()) {
               var2.append("UnicastListenPort");
               var2.append(String.valueOf(this.bean.getUnicastListenPort()));
            }

            if (this.bean.isUnicastPortAutoAdjustSet()) {
               var2.append("UnicastPortAutoAdjust");
               var2.append(String.valueOf(this.bean.isUnicastPortAutoAdjust()));
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
            CoherenceServerMBeanImpl var2 = (CoherenceServerMBeanImpl)var1;
            this.computeDiff("CoherenceClusterSystemResource", this.bean.getCoherenceClusterSystemResource(), var2.getCoherenceClusterSystemResource(), false);
            this.computeSubDiff("CoherenceServerStart", this.bean.getCoherenceServerStart(), var2.getCoherenceServerStart());
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("UnicastListenAddress", this.bean.getUnicastListenAddress(), var2.getUnicastListenAddress(), true);
            this.computeDiff("UnicastListenPort", this.bean.getUnicastListenPort(), var2.getUnicastListenPort(), true);
            this.computeDiff("UnicastPortAutoAdjust", this.bean.isUnicastPortAutoAdjust(), var2.isUnicastPortAutoAdjust(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CoherenceServerMBeanImpl var3 = (CoherenceServerMBeanImpl)var1.getSourceBean();
            CoherenceServerMBeanImpl var4 = (CoherenceServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CoherenceClusterSystemResource")) {
                  var3.setCoherenceClusterSystemResourceAsString(var4.getCoherenceClusterSystemResourceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CoherenceServerStart")) {
                  if (var6 == 2) {
                     var3.setCoherenceServerStart((CoherenceServerStartMBean)this.createCopy((AbstractDescriptorBean)var4.getCoherenceServerStart()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("CoherenceServerStart", var3.getCoherenceServerStart());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (!var5.equals("ManagedExternalServerStart")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("UnicastListenAddress")) {
                     var3.setUnicastListenAddress(var4.getUnicastListenAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("UnicastListenPort")) {
                     var3.setUnicastListenPort(var4.getUnicastListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("UnicastPortAutoAdjust")) {
                     var3.setUnicastPortAutoAdjust(var4.isUnicastPortAutoAdjust());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            CoherenceServerMBeanImpl var5 = (CoherenceServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CoherenceClusterSystemResource")) && this.bean.isCoherenceClusterSystemResourceSet()) {
               var5._unSet(var5, 14);
               var5.setCoherenceClusterSystemResourceAsString(this.bean.getCoherenceClusterSystemResourceAsString());
            }

            if ((var3 == null || !var3.contains("CoherenceServerStart")) && this.bean.isCoherenceServerStartSet() && !var5._isSet(18)) {
               CoherenceServerStartMBean var4 = this.bean.getCoherenceServerStart();
               var5.setCoherenceServerStart((CoherenceServerStartMBean)null);
               var5.setCoherenceServerStart(var4 == null ? null : (CoherenceServerStartMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("UnicastListenAddress")) && this.bean.isUnicastListenAddressSet()) {
               var5.setUnicastListenAddress(this.bean.getUnicastListenAddress());
            }

            if ((var3 == null || !var3.contains("UnicastListenPort")) && this.bean.isUnicastListenPortSet()) {
               var5.setUnicastListenPort(this.bean.getUnicastListenPort());
            }

            if ((var3 == null || !var3.contains("UnicastPortAutoAdjust")) && this.bean.isUnicastPortAutoAdjustSet()) {
               var5.setUnicastPortAutoAdjust(this.bean.isUnicastPortAutoAdjust());
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
         this.inferSubTree(this.bean.getCoherenceClusterSystemResource(), var1, var2);
         this.inferSubTree(this.bean.getCoherenceServerStart(), var1, var2);
         this.inferSubTree(this.bean.getManagedExternalServerStart(), var1, var2);
      }
   }
}
