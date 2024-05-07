package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.ForeignJMSServer;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ForeignJMSServerMBeanImpl extends DeploymentMBeanImpl implements ForeignJMSServerMBean, Serializable {
   private ForeignJMSConnectionFactoryMBean[] _ConnectionFactories;
   private String _ConnectionURL;
   private ForeignJMSDestinationMBean[] _Destinations;
   private ForeignJMSConnectionFactoryMBean[] _ForeignJMSConnectionFactories;
   private ForeignJMSDestinationMBean[] _ForeignJMSDestinations;
   private String _InitialContextFactory;
   private Properties _JNDIProperties;
   private String _JNDIPropertiesCredential;
   private byte[] _JNDIPropertiesCredentialEncrypted;
   private String _Name;
   private TargetMBean[] _Targets;
   private ForeignJMSServer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ForeignJMSServerMBeanImpl() {
      try {
         this._customizer = new ForeignJMSServer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ForeignJMSServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ForeignJMSServer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ForeignJMSDestinationMBean[] getDestinations() {
      return this._customizer.getDestinations();
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

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isDestinationsSet() {
      return this._isSet(9);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
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
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        ForeignJMSServerMBeanImpl.this.addTarget((TargetMBean)var1);
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
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public void setDestinations(ForeignJMSDestinationMBean[] var1) {
      Object var2 = var1 == null ? new ForeignJMSDestinationMBeanImpl[0] : var1;
      this._Destinations = (ForeignJMSDestinationMBean[])var2;
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

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return ForeignJMSServerMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public boolean addDestination(ForeignJMSDestinationMBean var1) {
      return this._customizer.addDestination(var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

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

      return true;
   }

   public boolean removeDestination(ForeignJMSDestinationMBean var1) {
      ForeignJMSDestinationMBean[] var2 = this.getDestinations();
      ForeignJMSDestinationMBean[] var3 = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])this._getHelper()._removeElement(var2, ForeignJMSDestinationMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDestinations(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public ForeignJMSConnectionFactoryMBean[] getConnectionFactories() {
      return this._ConnectionFactories;
   }

   public boolean isConnectionFactoriesSet() {
      return this._isSet(10);
   }

   public void setConnectionFactories(ForeignJMSConnectionFactoryMBean[] var1) {
      Object var2 = var1 == null ? new ForeignJMSConnectionFactoryMBeanImpl[0] : var1;
      this._ConnectionFactories = (ForeignJMSConnectionFactoryMBean[])var2;
   }

   public boolean addConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      return this._customizer.addConnectionFactory(var1);
   }

   public boolean removeConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      ForeignJMSConnectionFactoryMBean[] var2 = this.getConnectionFactories();
      ForeignJMSConnectionFactoryMBean[] var3 = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])this._getHelper()._removeElement(var2, ForeignJMSConnectionFactoryMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setConnectionFactories(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setInitialContextFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("InitialContextFactory", var1);
      String var2 = this.getInitialContextFactory();
      this._customizer.setInitialContextFactory(var1);
      this._postSet(11, var2, var1);
   }

   public String getInitialContextFactory() {
      return this._customizer.getInitialContextFactory();
   }

   public boolean isInitialContextFactorySet() {
      return this._isSet(11);
   }

   public void setConnectionURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getConnectionURL();
      this._customizer.setConnectionURL(var1);
      this._postSet(12, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getConnectionURL() {
      return this._customizer.getConnectionURL();
   }

   public boolean isConnectionURLSet() {
      return this._isSet(12);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setJNDIProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this.getJNDIProperties();
      this._customizer.setJNDIProperties(var1);
      this._postSet(13, var2, var1);
   }

   public Properties getJNDIProperties() {
      return this._customizer.getJNDIProperties();
   }

   public String getJNDIPropertiesAsString() {
      return StringHelper.objectToString(this.getJNDIProperties());
   }

   public boolean isJNDIPropertiesSet() {
      return this._isSet(13);
   }

   public void setJNDIPropertiesAsString(String var1) {
      try {
         this.setJNDIProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public byte[] getJNDIPropertiesCredentialEncrypted() {
      return this._getHelper()._cloneArray(this._JNDIPropertiesCredentialEncrypted);
   }

   public String getJNDIPropertiesCredentialEncryptedAsString() {
      byte[] var1 = this.getJNDIPropertiesCredentialEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isJNDIPropertiesCredentialEncryptedSet() {
      return this._isSet(14);
   }

   public void setJNDIPropertiesCredentialEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setJNDIPropertiesCredentialEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getJNDIPropertiesCredential() {
      byte[] var1 = this.getJNDIPropertiesCredentialEncrypted();
      return var1 == null ? null : this._decrypt("JNDIPropertiesCredential", var1);
   }

   public boolean isJNDIPropertiesCredentialSet() {
      return this.isJNDIPropertiesCredentialEncryptedSet();
   }

   public void setJNDIPropertiesCredential(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setJNDIPropertiesCredentialEncrypted(var1 == null ? null : this._encrypt("JNDIPropertiesCredential", var1));
   }

   public void addForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 16)) {
         ForeignJMSConnectionFactoryMBean[] var2;
         if (this._isSet(16)) {
            var2 = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])this._getHelper()._extendArray(this.getForeignJMSConnectionFactories(), ForeignJMSConnectionFactoryMBean.class, var1));
         } else {
            var2 = new ForeignJMSConnectionFactoryMBean[]{var1};
         }

         try {
            this.setForeignJMSConnectionFactories(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJMSConnectionFactoryMBean[] getForeignJMSConnectionFactories() {
      return this._ForeignJMSConnectionFactories;
   }

   public boolean isForeignJMSConnectionFactoriesSet() {
      return this._isSet(16);
   }

   public void removeForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      this.destroyForeignJMSConnectionFactory(var1);
   }

   public void setForeignJMSConnectionFactories(ForeignJMSConnectionFactoryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJMSConnectionFactoryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 16)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ForeignJMSConnectionFactoryMBean[] var5 = this._ForeignJMSConnectionFactories;
      this._ForeignJMSConnectionFactories = (ForeignJMSConnectionFactoryMBean[])var4;
      this._postSet(16, var5, var4);
   }

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1) {
      ForeignJMSConnectionFactoryMBeanImpl var2 = new ForeignJMSConnectionFactoryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJMSConnectionFactory(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public ForeignJMSConnectionFactoryMBean lookupForeignJMSConnectionFactory(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJMSConnectionFactories).iterator();

      ForeignJMSConnectionFactoryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJMSConnectionFactoryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void destroyForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 16);
         ForeignJMSConnectionFactoryMBean[] var2 = this.getForeignJMSConnectionFactories();
         ForeignJMSConnectionFactoryMBean[] var3 = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])this._getHelper()._removeElement(var2, ForeignJMSConnectionFactoryMBean.class, var1));
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
               this.setForeignJMSConnectionFactories(var3);
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

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1, ForeignJMSConnectionFactoryMBean var2) {
      return this._customizer.createForeignJMSConnectionFactory(var1, var2);
   }

   public void addForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 17)) {
         ForeignJMSDestinationMBean[] var2;
         if (this._isSet(17)) {
            var2 = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])this._getHelper()._extendArray(this.getForeignJMSDestinations(), ForeignJMSDestinationMBean.class, var1));
         } else {
            var2 = new ForeignJMSDestinationMBean[]{var1};
         }

         try {
            this.setForeignJMSDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJMSDestinationMBean[] getForeignJMSDestinations() {
      return this._ForeignJMSDestinations;
   }

   public boolean isForeignJMSDestinationsSet() {
      return this._isSet(17);
   }

   public void removeForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      this.destroyForeignJMSDestination(var1);
   }

   public void setForeignJMSDestinations(ForeignJMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJMSDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 17)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ForeignJMSDestinationMBean[] var5 = this._ForeignJMSDestinations;
      this._ForeignJMSDestinations = (ForeignJMSDestinationMBean[])var4;
      this._postSet(17, var5, var4);
   }

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1) {
      ForeignJMSDestinationMBeanImpl var2 = new ForeignJMSDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJMSDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public ForeignJMSDestinationMBean lookupForeignJMSDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJMSDestinations).iterator();

      ForeignJMSDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJMSDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1, ForeignJMSDestinationMBean var2) {
      return this._customizer.createForeignJMSDestination(var1, var2);
   }

   public void destroyForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 17);
         ForeignJMSDestinationMBean[] var2 = this.getForeignJMSDestinations();
         ForeignJMSDestinationMBean[] var3 = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])this._getHelper()._removeElement(var2, ForeignJMSDestinationMBean.class, var1));
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
               this.setForeignJMSDestinations(var3);
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

   public void useDelegates(ForeignServerBean var1, SubDeploymentMBean var2) {
      this._customizer.useDelegates(var1, var2);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setJNDIPropertiesCredentialEncrypted(byte[] var1) {
      byte[] var2 = this._JNDIPropertiesCredentialEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: JNDIPropertiesCredentialEncrypted of ForeignJMSServerMBean");
      } else {
         this._getHelper()._clearArray(this._JNDIPropertiesCredentialEncrypted);
         this._JNDIPropertiesCredentialEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(14, var2, var1);
      }
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
         if (var1 == 15) {
            this._markSet(14, false);
         }
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._ConnectionFactories = new ForeignJMSConnectionFactoryMBean[0];
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setConnectionURL((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._Destinations = new ForeignJMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 16:
               this._ForeignJMSConnectionFactories = new ForeignJMSConnectionFactoryMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._ForeignJMSDestinations = new ForeignJMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setInitialContextFactory("weblogic.jndi.WLInitialContextFactory");
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setJNDIProperties((Properties)null);
               if (var2) {
                  break;
               }
            case 15:
               this._JNDIPropertiesCredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 14:
               this._JNDIPropertiesCredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
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
      return "ForeignJMSServer";
   }

   public void putValue(String var1, Object var2) {
      ForeignJMSConnectionFactoryMBean[] var9;
      if (var1.equals("ConnectionFactories")) {
         var9 = this._ConnectionFactories;
         this._ConnectionFactories = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])var2);
         this._postSet(10, var9, this._ConnectionFactories);
      } else {
         String var5;
         if (var1.equals("ConnectionURL")) {
            var5 = this._ConnectionURL;
            this._ConnectionURL = (String)var2;
            this._postSet(12, var5, this._ConnectionURL);
         } else {
            ForeignJMSDestinationMBean[] var8;
            if (var1.equals("Destinations")) {
               var8 = this._Destinations;
               this._Destinations = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])var2);
               this._postSet(9, var8, this._Destinations);
            } else if (var1.equals("ForeignJMSConnectionFactories")) {
               var9 = this._ForeignJMSConnectionFactories;
               this._ForeignJMSConnectionFactories = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])var2);
               this._postSet(16, var9, this._ForeignJMSConnectionFactories);
            } else if (var1.equals("ForeignJMSDestinations")) {
               var8 = this._ForeignJMSDestinations;
               this._ForeignJMSDestinations = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])var2);
               this._postSet(17, var8, this._ForeignJMSDestinations);
            } else if (var1.equals("InitialContextFactory")) {
               var5 = this._InitialContextFactory;
               this._InitialContextFactory = (String)var2;
               this._postSet(11, var5, this._InitialContextFactory);
            } else if (var1.equals("JNDIProperties")) {
               Properties var7 = this._JNDIProperties;
               this._JNDIProperties = (Properties)var2;
               this._postSet(13, var7, this._JNDIProperties);
            } else if (var1.equals("JNDIPropertiesCredential")) {
               var5 = this._JNDIPropertiesCredential;
               this._JNDIPropertiesCredential = (String)var2;
               this._postSet(15, var5, this._JNDIPropertiesCredential);
            } else if (var1.equals("JNDIPropertiesCredentialEncrypted")) {
               byte[] var6 = this._JNDIPropertiesCredentialEncrypted;
               this._JNDIPropertiesCredentialEncrypted = (byte[])((byte[])var2);
               this._postSet(14, var6, this._JNDIPropertiesCredentialEncrypted);
            } else if (var1.equals("Name")) {
               var5 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var5, this._Name);
            } else if (var1.equals("Targets")) {
               TargetMBean[] var4 = this._Targets;
               this._Targets = (TargetMBean[])((TargetMBean[])var2);
               this._postSet(7, var4, this._Targets);
            } else if (var1.equals("customizer")) {
               ForeignJMSServer var3 = this._customizer;
               this._customizer = (ForeignJMSServer)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionFactories")) {
         return this._ConnectionFactories;
      } else if (var1.equals("ConnectionURL")) {
         return this._ConnectionURL;
      } else if (var1.equals("Destinations")) {
         return this._Destinations;
      } else if (var1.equals("ForeignJMSConnectionFactories")) {
         return this._ForeignJMSConnectionFactories;
      } else if (var1.equals("ForeignJMSDestinations")) {
         return this._ForeignJMSDestinations;
      } else if (var1.equals("InitialContextFactory")) {
         return this._InitialContextFactory;
      } else if (var1.equals("JNDIProperties")) {
         return this._JNDIProperties;
      } else if (var1.equals("JNDIPropertiesCredential")) {
         return this._JNDIPropertiesCredential;
      } else if (var1.equals("JNDIPropertiesCredentialEncrypted")) {
         return this._JNDIPropertiesCredentialEncrypted;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("InitialContextFactory", "weblogic.jndi.WLInitialContextFactory");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property InitialContextFactory in ForeignJMSServerMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
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
            case 10:
            case 12:
            case 13:
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 11:
               if (var1.equals("destination")) {
                  return 9;
               }
               break;
            case 14:
               if (var1.equals("connection-url")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("jndi-properties")) {
                  return 13;
               }
               break;
            case 18:
               if (var1.equals("connection-factory")) {
                  return 10;
               }
               break;
            case 23:
               if (var1.equals("foreign-jms-destination")) {
                  return 17;
               }

               if (var1.equals("initial-context-factory")) {
                  return 11;
               }
               break;
            case 26:
               if (var1.equals("jndi-properties-credential")) {
                  return 15;
               }
               break;
            case 30:
               if (var1.equals("foreign-jms-connection-factory")) {
                  return 16;
               }
               break;
            case 36:
               if (var1.equals("jndi-properties-credential-encrypted")) {
                  return 14;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 16:
               return new ForeignJMSConnectionFactoryMBeanImpl.SchemaHelper2();
            case 17:
               return new ForeignJMSDestinationMBeanImpl.SchemaHelper2();
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
            case 8:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "destination";
            case 10:
               return "connection-factory";
            case 11:
               return "initial-context-factory";
            case 12:
               return "connection-url";
            case 13:
               return "jndi-properties";
            case 14:
               return "jndi-properties-credential-encrypted";
            case 15:
               return "jndi-properties-credential";
            case 16:
               return "foreign-jms-connection-factory";
            case 17:
               return "foreign-jms-destination";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
               return super.isArray(var1);
            case 9:
               return true;
            case 10:
               return true;
            case 16:
               return true;
            case 17:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 16:
               return true;
            case 17:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private ForeignJMSServerMBeanImpl bean;

      protected Helper(ForeignJMSServerMBeanImpl var1) {
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
            case 8:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "Destinations";
            case 10:
               return "ConnectionFactories";
            case 11:
               return "InitialContextFactory";
            case 12:
               return "ConnectionURL";
            case 13:
               return "JNDIProperties";
            case 14:
               return "JNDIPropertiesCredentialEncrypted";
            case 15:
               return "JNDIPropertiesCredential";
            case 16:
               return "ForeignJMSConnectionFactories";
            case 17:
               return "ForeignJMSDestinations";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionFactories")) {
            return 10;
         } else if (var1.equals("ConnectionURL")) {
            return 12;
         } else if (var1.equals("Destinations")) {
            return 9;
         } else if (var1.equals("ForeignJMSConnectionFactories")) {
            return 16;
         } else if (var1.equals("ForeignJMSDestinations")) {
            return 17;
         } else if (var1.equals("InitialContextFactory")) {
            return 11;
         } else if (var1.equals("JNDIProperties")) {
            return 13;
         } else if (var1.equals("JNDIPropertiesCredential")) {
            return 15;
         } else if (var1.equals("JNDIPropertiesCredentialEncrypted")) {
            return 14;
         } else if (var1.equals("Name")) {
            return 2;
         } else {
            return var1.equals("Targets") ? 7 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getForeignJMSConnectionFactories()));
         var1.add(new ArrayIterator(this.bean.getForeignJMSDestinations()));
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
            if (this.bean.isConnectionFactoriesSet()) {
               var2.append("ConnectionFactories");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getConnectionFactories())));
            }

            if (this.bean.isConnectionURLSet()) {
               var2.append("ConnectionURL");
               var2.append(String.valueOf(this.bean.getConnectionURL()));
            }

            if (this.bean.isDestinationsSet()) {
               var2.append("Destinations");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDestinations())));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getForeignJMSConnectionFactories().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJMSConnectionFactories()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getForeignJMSDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJMSDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isInitialContextFactorySet()) {
               var2.append("InitialContextFactory");
               var2.append(String.valueOf(this.bean.getInitialContextFactory()));
            }

            if (this.bean.isJNDIPropertiesSet()) {
               var2.append("JNDIProperties");
               var2.append(String.valueOf(this.bean.getJNDIProperties()));
            }

            if (this.bean.isJNDIPropertiesCredentialSet()) {
               var2.append("JNDIPropertiesCredential");
               var2.append(String.valueOf(this.bean.getJNDIPropertiesCredential()));
            }

            if (this.bean.isJNDIPropertiesCredentialEncryptedSet()) {
               var2.append("JNDIPropertiesCredentialEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJNDIPropertiesCredentialEncrypted())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
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
            ForeignJMSServerMBeanImpl var2 = (ForeignJMSServerMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionURL", this.bean.getConnectionURL(), var2.getConnectionURL(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ForeignJMSConnectionFactories", this.bean.getForeignJMSConnectionFactories(), var2.getForeignJMSConnectionFactories(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ForeignJMSDestinations", this.bean.getForeignJMSDestinations(), var2.getForeignJMSDestinations(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("InitialContextFactory", this.bean.getInitialContextFactory(), var2.getInitialContextFactory(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIProperties", this.bean.getJNDIProperties(), var2.getJNDIProperties(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIPropertiesCredentialEncrypted", this.bean.getJNDIPropertiesCredentialEncrypted(), var2.getJNDIPropertiesCredentialEncrypted(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ForeignJMSServerMBeanImpl var3 = (ForeignJMSServerMBeanImpl)var1.getSourceBean();
            ForeignJMSServerMBeanImpl var4 = (ForeignJMSServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ConnectionFactories")) {
                  if (var5.equals("ConnectionURL")) {
                     var3.setConnectionURL(var4.getConnectionURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (!var5.equals("Destinations")) {
                     if (var5.equals("ForeignJMSConnectionFactories")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJMSConnectionFactories() == null || var3.getForeignJMSConnectionFactories().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                        }
                     } else if (var5.equals("ForeignJMSDestinations")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJMSDestination((ForeignJMSDestinationMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJMSDestination((ForeignJMSDestinationMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJMSDestinations() == null || var3.getForeignJMSDestinations().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        }
                     } else if (var5.equals("InitialContextFactory")) {
                        var3.setInitialContextFactory(var4.getInitialContextFactory());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     } else if (var5.equals("JNDIProperties")) {
                        var3.setJNDIProperties(var4.getJNDIProperties() == null ? null : (Properties)var4.getJNDIProperties().clone());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                     } else if (!var5.equals("JNDIPropertiesCredential")) {
                        if (var5.equals("JNDIPropertiesCredentialEncrypted")) {
                           var3.setJNDIPropertiesCredentialEncrypted(var4.getJNDIPropertiesCredentialEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                        } else if (var5.equals("Name")) {
                           var3.setName(var4.getName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                        } else if (var5.equals("Targets")) {
                           var3.setTargetsAsString(var4.getTargetsAsString());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
                     }
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
            ForeignJMSServerMBeanImpl var5 = (ForeignJMSServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ConnectionURL")) && this.bean.isConnectionURLSet()) {
               var5.setConnectionURL(this.bean.getConnectionURL());
            }

            int var8;
            if (var2 && (var3 == null || !var3.contains("ForeignJMSConnectionFactories")) && this.bean.isForeignJMSConnectionFactoriesSet() && !var5._isSet(16)) {
               ForeignJMSConnectionFactoryMBean[] var6 = this.bean.getForeignJMSConnectionFactories();
               ForeignJMSConnectionFactoryMBean[] var7 = new ForeignJMSConnectionFactoryMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ForeignJMSConnectionFactoryMBean)((ForeignJMSConnectionFactoryMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setForeignJMSConnectionFactories(var7);
            }

            if (var2 && (var3 == null || !var3.contains("ForeignJMSDestinations")) && this.bean.isForeignJMSDestinationsSet() && !var5._isSet(17)) {
               ForeignJMSDestinationMBean[] var11 = this.bean.getForeignJMSDestinations();
               ForeignJMSDestinationMBean[] var12 = new ForeignJMSDestinationMBean[var11.length];

               for(var8 = 0; var8 < var12.length; ++var8) {
                  var12[var8] = (ForeignJMSDestinationMBean)((ForeignJMSDestinationMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setForeignJMSDestinations(var12);
            }

            if (var2 && (var3 == null || !var3.contains("InitialContextFactory")) && this.bean.isInitialContextFactorySet()) {
               var5.setInitialContextFactory(this.bean.getInitialContextFactory());
            }

            if (var2 && (var3 == null || !var3.contains("JNDIProperties")) && this.bean.isJNDIPropertiesSet()) {
               var5.setJNDIProperties(this.bean.getJNDIProperties());
            }

            if (var2 && (var3 == null || !var3.contains("JNDIPropertiesCredentialEncrypted")) && this.bean.isJNDIPropertiesCredentialEncryptedSet()) {
               byte[] var4 = this.bean.getJNDIPropertiesCredentialEncrypted();
               var5.setJNDIPropertiesCredentialEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
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
         this.inferSubTree(this.bean.getConnectionFactories(), var1, var2);
         this.inferSubTree(this.bean.getDestinations(), var1, var2);
         this.inferSubTree(this.bean.getForeignJMSConnectionFactories(), var1, var2);
         this.inferSubTree(this.bean.getForeignJMSDestinations(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
