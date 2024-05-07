package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.SingletonService;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class SingletonServiceAppScopedMBeanImpl extends SingletonServiceBaseMBeanImpl implements SingletonServiceAppScopedMBean, Serializable {
   private String _ClassName;
   private String _CompatibilityName;
   private ServerMBean _HostingServer;
   private String _ModuleType;
   private String _Name;
   private SubDeploymentMBean[] _SubDeployments;
   private TargetMBean[] _Targets;
   private ServerMBean _UserPreferredServer;
   private SingletonService _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SingletonServiceAppScopedMBeanImpl() {
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

   public SingletonServiceAppScopedMBeanImpl(DescriptorBean var1, int var2) {
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

   public String getClassName() {
      if (!this._isSet(15)) {
         try {
            return this.getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._ClassName;
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

   public boolean isClassNameSet() {
      return this._isSet(15);
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
                  SingletonServiceAppScopedMBeanImpl.this.setHostingServer((ServerMBean)var1);
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

   public void addSubDeployment(SubDeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         SubDeploymentMBean[] var2;
         if (this._isSet(14)) {
            var2 = (SubDeploymentMBean[])((SubDeploymentMBean[])this._getHelper()._extendArray(this.getSubDeployments(), SubDeploymentMBean.class, var1));
         } else {
            var2 = new SubDeploymentMBean[]{var1};
         }

         try {
            this.setSubDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SubDeploymentMBean[] getSubDeployments() {
      return this._SubDeployments;
   }

   public boolean isSubDeploymentsSet() {
      return this._isSet(14);
   }

   public void removeSubDeployment(SubDeploymentMBean var1) {
      this.destroySubDeployment(var1);
   }

   public void setClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("ClassName", var1);
      String var2 = this._ClassName;
      this._ClassName = var1;
      this._postSet(15, var2, var1);
   }

   public void setHostingServer(ServerMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SingletonServiceAppScopedMBeanImpl.this.getHostingServer();
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
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setSubDeployments(SubDeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SubDeploymentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 14)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SubDeploymentMBean[] var5 = this._SubDeployments;
      this._SubDeployments = (SubDeploymentMBean[])var4;
      this._postSet(14, var5, var4);
   }

   public SubDeploymentMBean createSubDeployment(String var1) {
      SubDeploymentMBeanImpl var2 = new SubDeploymentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSubDeployment(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public TargetMBean[] getTargets() {
      if (!this._isSet(11)) {
         try {
            return DomainTargetHelper.getDefaultTargets(this, this.getValue("Targets"));
         } catch (NullPointerException var2) {
         }
      }

      return this._Targets;
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public ServerMBean getUserPreferredServer() {
      return this._customizer.getUserPreferredServer();
   }

   public String getUserPreferredServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getUserPreferredServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isTargetsSet() {
      return this._isSet(11);
   }

   public boolean isUserPreferredServerSet() {
      return this._isSet(8);
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 11) {
                  public void resolveReference(Object var1) {
                     try {
                        SingletonServiceAppScopedMBeanImpl.this.addTarget((TargetMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(11);
         this._postSet(11, var2, this._Targets);
      }
   }

   public void setUserPreferredServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  SingletonServiceAppScopedMBeanImpl.this.setUserPreferredServer((ServerMBean)var1);
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

   public SubDeploymentMBean lookupSubDeployment(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SubDeployments).iterator();

      SubDeploymentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SubDeploymentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));
      DomainTargetHelper.validateTargets(this);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 11, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return SingletonServiceAppScopedMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this._Targets;
      this._Targets = var1;
      this._postSet(11, var5, var1);
   }

   public void setUserPreferredServer(ServerMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SingletonServiceAppScopedMBeanImpl.this.getUserPreferredServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this.getUserPreferredServer();
      this._customizer.setUserPreferredServer(var1);
      this._postSet(8, var3, var1);
   }

   public void addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         TargetMBean[] var2;
         if (this._isSet(11)) {
            var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));
         } else {
            var2 = new TargetMBean[]{var1};
         }

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public void destroySubDeployment(SubDeploymentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 14);
         SubDeploymentMBean[] var2 = this.getSubDeployments();
         SubDeploymentMBean[] var3 = (SubDeploymentMBean[])((SubDeploymentMBean[])this._getHelper()._removeElement(var2, SubDeploymentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setSubDeployments(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            }

            if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public String getModuleType() {
      return this._ModuleType;
   }

   public boolean isModuleTypeSet() {
      return this._isSet(12);
   }

   public void setModuleType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ModuleType;
      this._ModuleType = var1;
      this._postSet(12, var2, var1);
   }

   public String getCompatibilityName() {
      return this._CompatibilityName;
   }

   public boolean isCompatibilityNameSet() {
      return this._isSet(13);
   }

   public void setCompatibilityName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityName;
      this._CompatibilityName = var1;
      this._postSet(13, var2, var1);
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
         var1 = 15;
      }

      try {
         switch (var1) {
            case 15:
               this._ClassName = null;
               if (var2) {
                  break;
               }
            case 13:
               this._CompatibilityName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._HostingServer = null;
               if (var2) {
                  break;
               }
            case 12:
               this._ModuleType = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 14:
               this._SubDeployments = new SubDeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._Targets = new TargetMBean[0];
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
            case 9:
            case 10:
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
      return "SingletonServiceAppScoped";
   }

   public void putValue(String var1, Object var2) {
      String var7;
      if (var1.equals("ClassName")) {
         var7 = this._ClassName;
         this._ClassName = (String)var2;
         this._postSet(15, var7, this._ClassName);
      } else if (var1.equals("CompatibilityName")) {
         var7 = this._CompatibilityName;
         this._CompatibilityName = (String)var2;
         this._postSet(13, var7, this._CompatibilityName);
      } else {
         ServerMBean var4;
         if (var1.equals("HostingServer")) {
            var4 = this._HostingServer;
            this._HostingServer = (ServerMBean)var2;
            this._postSet(7, var4, this._HostingServer);
         } else if (var1.equals("ModuleType")) {
            var7 = this._ModuleType;
            this._ModuleType = (String)var2;
            this._postSet(12, var7, this._ModuleType);
         } else if (var1.equals("Name")) {
            var7 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var7, this._Name);
         } else if (var1.equals("SubDeployments")) {
            SubDeploymentMBean[] var6 = this._SubDeployments;
            this._SubDeployments = (SubDeploymentMBean[])((SubDeploymentMBean[])var2);
            this._postSet(14, var6, this._SubDeployments);
         } else if (var1.equals("Targets")) {
            TargetMBean[] var5 = this._Targets;
            this._Targets = (TargetMBean[])((TargetMBean[])var2);
            this._postSet(11, var5, this._Targets);
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
      if (var1.equals("ClassName")) {
         return this._ClassName;
      } else if (var1.equals("CompatibilityName")) {
         return this._CompatibilityName;
      } else if (var1.equals("HostingServer")) {
         return this._HostingServer;
      } else if (var1.equals("ModuleType")) {
         return this._ModuleType;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("SubDeployments")) {
         return this._SubDeployments;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("UserPreferredServer")) {
         return this._UserPreferredServer;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SingletonServiceBaseMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 11;
               }
               break;
            case 10:
               if (var1.equals("class-name")) {
                  return 15;
               }
               break;
            case 11:
               if (var1.equals("module-type")) {
                  return 12;
               }
               break;
            case 14:
               if (var1.equals("hosting-server")) {
                  return 7;
               }

               if (var1.equals("sub-deployment")) {
                  return 14;
               }
               break;
            case 18:
               if (var1.equals("compatibility-name")) {
                  return 13;
               }
               break;
            case 21:
               if (var1.equals("user-preferred-server")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 14:
               return new SubDeploymentMBeanImpl.SchemaHelper2();
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
            case 9:
            case 10:
            default:
               return super.getElementName(var1);
            case 7:
               return "hosting-server";
            case 8:
               return "user-preferred-server";
            case 11:
               return "target";
            case 12:
               return "module-type";
            case 13:
               return "compatibility-name";
            case 14:
               return "sub-deployment";
            case 15:
               return "class-name";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 11:
               return true;
            case 14:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 14:
               return true;
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

   protected static class Helper extends SingletonServiceBaseMBeanImpl.Helper {
      private SingletonServiceAppScopedMBeanImpl bean;

      protected Helper(SingletonServiceAppScopedMBeanImpl var1) {
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
            case 9:
            case 10:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "HostingServer";
            case 8:
               return "UserPreferredServer";
            case 11:
               return "Targets";
            case 12:
               return "ModuleType";
            case 13:
               return "CompatibilityName";
            case 14:
               return "SubDeployments";
            case 15:
               return "ClassName";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ClassName")) {
            return 15;
         } else if (var1.equals("CompatibilityName")) {
            return 13;
         } else if (var1.equals("HostingServer")) {
            return 7;
         } else if (var1.equals("ModuleType")) {
            return 12;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SubDeployments")) {
            return 14;
         } else if (var1.equals("Targets")) {
            return 11;
         } else {
            return var1.equals("UserPreferredServer") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getSubDeployments()));
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
            if (this.bean.isClassNameSet()) {
               var2.append("ClassName");
               var2.append(String.valueOf(this.bean.getClassName()));
            }

            if (this.bean.isCompatibilityNameSet()) {
               var2.append("CompatibilityName");
               var2.append(String.valueOf(this.bean.getCompatibilityName()));
            }

            if (this.bean.isHostingServerSet()) {
               var2.append("HostingServer");
               var2.append(String.valueOf(this.bean.getHostingServer()));
            }

            if (this.bean.isModuleTypeSet()) {
               var2.append("ModuleType");
               var2.append(String.valueOf(this.bean.getModuleType()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getSubDeployments().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSubDeployments()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isUserPreferredServerSet()) {
               var2.append("UserPreferredServer");
               var2.append(String.valueOf(this.bean.getUserPreferredServer()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            SingletonServiceAppScopedMBeanImpl var2 = (SingletonServiceAppScopedMBeanImpl)var1;
            this.computeDiff("ClassName", this.bean.getClassName(), var2.getClassName(), false);
            this.computeDiff("CompatibilityName", this.bean.getCompatibilityName(), var2.getCompatibilityName(), false);
            this.computeDiff("HostingServer", this.bean.getHostingServer(), var2.getHostingServer(), true);
            this.computeDiff("ModuleType", this.bean.getModuleType(), var2.getModuleType(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeChildDiff("SubDeployments", this.bean.getSubDeployments(), var2.getSubDeployments(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("UserPreferredServer", this.bean.getUserPreferredServer(), var2.getUserPreferredServer(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SingletonServiceAppScopedMBeanImpl var3 = (SingletonServiceAppScopedMBeanImpl)var1.getSourceBean();
            SingletonServiceAppScopedMBeanImpl var4 = (SingletonServiceAppScopedMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ClassName")) {
                  var3.setClassName(var4.getClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("CompatibilityName")) {
                  var3.setCompatibilityName(var4.getCompatibilityName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("HostingServer")) {
                  var3.setHostingServerAsString(var4.getHostingServerAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("ModuleType")) {
                  var3.setModuleType(var4.getModuleType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("SubDeployments")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addSubDeployment((SubDeploymentMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSubDeployment((SubDeploymentMBean)var2.getRemovedObject());
                  }

                  if (var3.getSubDeployments() == null || var3.getSubDeployments().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  }
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            SingletonServiceAppScopedMBeanImpl var5 = (SingletonServiceAppScopedMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ClassName")) && this.bean.isClassNameSet()) {
               var5.setClassName(this.bean.getClassName());
            }

            if ((var3 == null || !var3.contains("CompatibilityName")) && this.bean.isCompatibilityNameSet()) {
               var5.setCompatibilityName(this.bean.getCompatibilityName());
            }

            if ((var3 == null || !var3.contains("HostingServer")) && this.bean.isHostingServerSet()) {
               var5._unSet(var5, 7);
               var5.setHostingServerAsString(this.bean.getHostingServerAsString());
            }

            if ((var3 == null || !var3.contains("ModuleType")) && this.bean.isModuleTypeSet()) {
               var5.setModuleType(this.bean.getModuleType());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SubDeployments")) && this.bean.isSubDeploymentsSet() && !var5._isSet(14)) {
               SubDeploymentMBean[] var6 = this.bean.getSubDeployments();
               SubDeploymentMBean[] var7 = new SubDeploymentMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (SubDeploymentMBean)((SubDeploymentMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setSubDeployments(var7);
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 11);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if ((var3 == null || !var3.contains("UserPreferredServer")) && this.bean.isUserPreferredServerSet()) {
               var5._unSet(var5, 8);
               var5.setUserPreferredServerAsString(this.bean.getUserPreferredServerAsString());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getHostingServer(), var1, var2);
         this.inferSubTree(this.bean.getSubDeployments(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
         this.inferSubTree(this.bean.getUserPreferredServer(), var1, var2);
      }
   }
}
