package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSServer;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class JMSServerMBeanImpl extends DeploymentMBeanImpl implements JMSServerMBean, Serializable {
   private boolean _AllowsPersistentDowngrade;
   private String _BlockingSendPolicy;
   private long _BytesMaximum;
   private boolean _BytesPagingEnabled;
   private long _BytesThresholdHigh;
   private long _BytesThresholdLow;
   private String _ConsumptionPausedAtStartup;
   private JMSDestinationMBean[] _Destinations;
   private int _ExpirationScanInterval;
   private boolean _HostingTemporaryDestinations;
   private String _InsertionPausedAtStartup;
   private boolean _JDBCStoreUpgradeEnabled;
   private JMSMessageLogFileMBean _JMSMessageLogFile;
   private JMSQueueMBean[] _JMSQueues;
   private JMSSessionPoolMBean[] _JMSSessionPools;
   private JMSTopicMBean[] _JMSTopics;
   private int _MaximumMessageSize;
   private long _MessageBufferSize;
   private long _MessagesMaximum;
   private boolean _MessagesPagingEnabled;
   private long _MessagesThresholdHigh;
   private long _MessagesThresholdLow;
   private String _Name;
   private int _PagingBlockSize;
   private String _PagingDirectory;
   private boolean _PagingFileLockingEnabled;
   private int _PagingIoBufferSize;
   private long _PagingMaxFileSize;
   private int _PagingMaxWindowBufferSize;
   private int _PagingMinWindowBufferSize;
   private JMSStoreMBean _PagingStore;
   private PersistentStoreMBean _PersistentStore;
   private String _ProductionPausedAtStartup;
   private Set _ServerNames;
   private JMSSessionPoolMBean[] _SessionPools;
   private JMSStoreMBean _Store;
   private boolean _StoreEnabled;
   private TargetMBean[] _Targets;
   private JMSTemplateMBean _TemporaryTemplate;
   private String _TemporaryTemplateName;
   private String _TemporaryTemplateResource;
   private JMSServer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSServerMBeanImpl() {
      try {
         this._customizer = new JMSServer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSServer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
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

   public Set getServerNames() {
      return this._customizer.getServerNames();
   }

   public TargetMBean[] getTargets() {
      return this._Targets;
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isServerNamesSet() {
      return this._isSet(9);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setServerNames(Set var1) throws InvalidAttributeValueException {
      this._ServerNames = var1;
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
                        JMSServerMBeanImpl.this.addTarget((TargetMBean)var1);
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
      JMSLegalHelper.validateSingleServerTargets(var1);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JMSServerMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this._Targets;
      this._Targets = var1;
      this._postSet(7, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2;
         if (this._isSet(7)) {
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

      return true;
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

   public JMSSessionPoolMBean[] getSessionPools() {
      return this._customizer.getSessionPools();
   }

   public boolean isSessionPoolsSet() {
      return this._isSet(10);
   }

   public void setSessionPools(JMSSessionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new JMSSessionPoolMBeanImpl[0] : var1;
      this._customizer.setSessionPools((JMSSessionPoolMBean[])var2);
   }

   public boolean addSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.addSessionPool(var1);
   }

   public boolean removeSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.removeSessionPool(var1);
   }

   public void addJMSSessionPool(JMSSessionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         JMSSessionPoolMBean[] var2;
         if (this._isSet(11)) {
            var2 = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])this._getHelper()._extendArray(this.getJMSSessionPools(), JMSSessionPoolMBean.class, var1));
         } else {
            var2 = new JMSSessionPoolMBean[]{var1};
         }

         try {
            this.setJMSSessionPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSSessionPoolMBean[] getJMSSessionPools() {
      return this._JMSSessionPools;
   }

   public boolean isJMSSessionPoolsSet() {
      return this._isSet(11);
   }

   public void removeJMSSessionPool(JMSSessionPoolMBean var1) {
      this.destroyJMSSessionPool(var1);
   }

   public void setJMSSessionPools(JMSSessionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSSessionPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 11)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSSessionPoolMBean[] var5 = this._JMSSessionPools;
      this._JMSSessionPools = (JMSSessionPoolMBean[])var4;
      this._postSet(11, var5, var4);
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSSessionPoolMBeanImpl var2 = new JMSSessionPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSSessionPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else if (var4 instanceof DistributedManagementException) {
            throw (DistributedManagementException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.createJMSSessionPool(var1, var2);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void destroyJMSSessionPool(JMSSessionPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 11);
         JMSSessionPoolMBean[] var2 = this.getJMSSessionPools();
         JMSSessionPoolMBean[] var3 = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])this._getHelper()._removeElement(var2, JMSSessionPoolMBean.class, var1));
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
               this.setJMSSessionPools(var3);
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

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public JMSSessionPoolMBean lookupJMSSessionPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSSessionPools).iterator();

      JMSSessionPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSSessionPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public JMSDestinationMBean[] getDestinations() {
      return this._customizer.getDestinations();
   }

   public boolean isDestinationsSet() {
      return this._isSet(12);
   }

   public void setDestinations(JMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new JMSDestinationMBeanImpl[0] : var1;
      this._customizer.setDestinations((JMSDestinationMBean[])var2);
   }

   public boolean addDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.addDestination(var1);
   }

   public boolean removeDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.removeDestination(var1);
   }

   public JMSStoreMBean getStore() {
      return this._Store;
   }

   public String getStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isStoreSet() {
      return this._isSet(13);
   }

   public void setStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSStoreMBean.class, new ReferenceManager.Resolver(this, 13) {
            public void resolveReference(Object var1) {
               try {
                  JMSServerMBeanImpl.this.setStore((JMSStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSStoreMBean var2 = this._Store;
         this._initializeProperty(13);
         this._postSet(13, var2, this._Store);
      }

   }

   public void setStore(JMSStoreMBean var1) throws InvalidAttributeValueException {
      JMSStoreMBean var2 = this._Store;
      this._Store = var1;
      this._postSet(13, var2, var1);
   }

   public PersistentStoreMBean getPersistentStore() {
      return this._PersistentStore;
   }

   public String getPersistentStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getPersistentStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isPersistentStoreSet() {
      return this._isSet(14);
   }

   public void setPersistentStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, PersistentStoreMBean.class, new ReferenceManager.Resolver(this, 14) {
            public void resolveReference(Object var1) {
               try {
                  JMSServerMBeanImpl.this.setPersistentStore((PersistentStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         PersistentStoreMBean var2 = this._PersistentStore;
         this._initializeProperty(14);
         this._postSet(14, var2, this._PersistentStore);
      }

   }

   public void setPersistentStore(PersistentStoreMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 14, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JMSServerMBeanImpl.this.getPersistentStore();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      PersistentStoreMBean var3 = this._PersistentStore;
      this._PersistentStore = var1;
      this._postSet(14, var3, var1);
   }

   public boolean getStoreEnabled() {
      return this._StoreEnabled;
   }

   public boolean isStoreEnabledSet() {
      return this._isSet(15);
   }

   public void setStoreEnabled(boolean var1) {
      boolean var2 = this._StoreEnabled;
      this._StoreEnabled = var1;
      this._postSet(15, var2, var1);
   }

   public boolean isAllowsPersistentDowngrade() {
      return this._AllowsPersistentDowngrade;
   }

   public boolean isAllowsPersistentDowngradeSet() {
      return this._isSet(16);
   }

   public void setAllowsPersistentDowngrade(boolean var1) {
      boolean var2 = this._AllowsPersistentDowngrade;
      this._AllowsPersistentDowngrade = var1;
      this._postSet(16, var2, var1);
   }

   public JMSTemplateMBean getTemporaryTemplate() {
      return this._customizer.getTemporaryTemplate();
   }

   public String getTemporaryTemplateAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getTemporaryTemplate();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isTemporaryTemplateSet() {
      return this._isSet(17);
   }

   public void setTemporaryTemplateAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSTemplateMBean.class, new ReferenceManager.Resolver(this, 17) {
            public void resolveReference(Object var1) {
               try {
                  JMSServerMBeanImpl.this.setTemporaryTemplate((JMSTemplateMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSTemplateMBean var2 = this._TemporaryTemplate;
         this._initializeProperty(17);
         this._postSet(17, var2, this._TemporaryTemplate);
      }

   }

   public void setTemporaryTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      JMSTemplateMBean var2 = this.getTemporaryTemplate();
      this._customizer.setTemporaryTemplate(var1);
      this._postSet(17, var2, var1);
   }

   public boolean isHostingTemporaryDestinations() {
      return this._HostingTemporaryDestinations;
   }

   public boolean isHostingTemporaryDestinationsSet() {
      return this._isSet(18);
   }

   public void setHostingTemporaryDestinations(boolean var1) {
      boolean var2 = this._HostingTemporaryDestinations;
      this._HostingTemporaryDestinations = var1;
      this._postSet(18, var2, var1);
   }

   public String getTemporaryTemplateResource() {
      return this._TemporaryTemplateResource;
   }

   public boolean isTemporaryTemplateResourceSet() {
      return this._isSet(19);
   }

   public void setTemporaryTemplateResource(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TemporaryTemplateResource;
      this._TemporaryTemplateResource = var1;
      this._postSet(19, var2, var1);
   }

   public String getTemporaryTemplateName() {
      return this._TemporaryTemplateName;
   }

   public boolean isTemporaryTemplateNameSet() {
      return this._isSet(20);
   }

   public void setTemporaryTemplateName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TemporaryTemplateName;
      this._TemporaryTemplateName = var1;
      this._postSet(20, var2, var1);
   }

   public long getBytesMaximum() {
      return this._BytesMaximum;
   }

   public boolean isBytesMaximumSet() {
      return this._isSet(21);
   }

   public void setBytesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesMaximum;
      this._BytesMaximum = var1;
      this._postSet(21, var3, var1);
   }

   public long getBytesThresholdHigh() {
      return this._BytesThresholdHigh;
   }

   public boolean isBytesThresholdHighSet() {
      return this._isSet(22);
   }

   public void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesThresholdHigh;
      this._BytesThresholdHigh = var1;
      this._postSet(22, var3, var1);
   }

   public long getBytesThresholdLow() {
      return this._BytesThresholdLow;
   }

   public boolean isBytesThresholdLowSet() {
      return this._isSet(23);
   }

   public void setBytesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesThresholdLow;
      this._BytesThresholdLow = var1;
      this._postSet(23, var3, var1);
   }

   public long getMessagesMaximum() {
      return this._MessagesMaximum;
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(24);
   }

   public void setMessagesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesMaximum;
      this._MessagesMaximum = var1;
      this._postSet(24, var3, var1);
   }

   public long getMessagesThresholdHigh() {
      return this._MessagesThresholdHigh;
   }

   public boolean isMessagesThresholdHighSet() {
      return this._isSet(25);
   }

   public void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesThresholdHigh;
      this._MessagesThresholdHigh = var1;
      this._postSet(25, var3, var1);
   }

   public long getMessagesThresholdLow() {
      return this._MessagesThresholdLow;
   }

   public boolean isMessagesThresholdLowSet() {
      return this._isSet(26);
   }

   public void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesThresholdLow;
      this._MessagesThresholdLow = var1;
      this._postSet(26, var3, var1);
   }

   public boolean isJDBCStoreUpgradeEnabled() {
      return this._JDBCStoreUpgradeEnabled;
   }

   public boolean isJDBCStoreUpgradeEnabledSet() {
      return this._isSet(27);
   }

   public void setJDBCStoreUpgradeEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._JDBCStoreUpgradeEnabled;
      this._JDBCStoreUpgradeEnabled = var1;
      this._postSet(27, var2, var1);
   }

   public JMSStoreMBean getPagingStore() {
      return this._PagingStore;
   }

   public String getPagingStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getPagingStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isPagingStoreSet() {
      return this._isSet(28);
   }

   public void setPagingStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSStoreMBean.class, new ReferenceManager.Resolver(this, 28) {
            public void resolveReference(Object var1) {
               try {
                  JMSServerMBeanImpl.this.setPagingStore((JMSStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSStoreMBean var2 = this._PagingStore;
         this._initializeProperty(28);
         this._postSet(28, var2, this._PagingStore);
      }

   }

   public void setPagingStore(JMSStoreMBean var1) throws InvalidAttributeValueException {
      JMSStoreMBean var2 = this._PagingStore;
      this._PagingStore = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isMessagesPagingEnabled() {
      return this._MessagesPagingEnabled;
   }

   public boolean isMessagesPagingEnabledSet() {
      return this._isSet(29);
   }

   public void setMessagesPagingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._MessagesPagingEnabled;
      this._MessagesPagingEnabled = var1;
      this._postSet(29, var2, var1);
   }

   public boolean isBytesPagingEnabled() {
      return this._BytesPagingEnabled;
   }

   public boolean isBytesPagingEnabledSet() {
      return this._isSet(30);
   }

   public void setBytesPagingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._BytesPagingEnabled;
      this._BytesPagingEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public long getMessageBufferSize() {
      return this._MessageBufferSize;
   }

   public boolean isMessageBufferSizeSet() {
      return this._isSet(31);
   }

   public void setMessageBufferSize(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessageBufferSize", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessageBufferSize;
      this._MessageBufferSize = var1;
      this._postSet(31, var3, var1);
   }

   public String getPagingDirectory() {
      return this._PagingDirectory;
   }

   public boolean isPagingDirectorySet() {
      return this._isSet(32);
   }

   public void setPagingDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PagingDirectory;
      this._PagingDirectory = var1;
      this._postSet(32, var2, var1);
   }

   public boolean isPagingFileLockingEnabled() {
      return this._PagingFileLockingEnabled;
   }

   public boolean isPagingFileLockingEnabledSet() {
      return this._isSet(33);
   }

   public void setPagingFileLockingEnabled(boolean var1) {
      boolean var2 = this._PagingFileLockingEnabled;
      this._PagingFileLockingEnabled = var1;
      this._postSet(33, var2, var1);
   }

   public int getPagingMinWindowBufferSize() {
      return this._PagingMinWindowBufferSize;
   }

   public boolean isPagingMinWindowBufferSizeSet() {
      return this._isSet(34);
   }

   public void setPagingMinWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PagingMinWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._PagingMinWindowBufferSize;
      this._PagingMinWindowBufferSize = var1;
      this._postSet(34, var2, var1);
   }

   public int getPagingMaxWindowBufferSize() {
      return this._PagingMaxWindowBufferSize;
   }

   public boolean isPagingMaxWindowBufferSizeSet() {
      return this._isSet(35);
   }

   public void setPagingMaxWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PagingMaxWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._PagingMaxWindowBufferSize;
      this._PagingMaxWindowBufferSize = var1;
      this._postSet(35, var2, var1);
   }

   public int getPagingIoBufferSize() {
      return this._PagingIoBufferSize;
   }

   public boolean isPagingIoBufferSizeSet() {
      return this._isSet(36);
   }

   public void setPagingIoBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PagingIoBufferSize", (long)var1, -1L, 67108864L);
      int var2 = this._PagingIoBufferSize;
      this._PagingIoBufferSize = var1;
      this._postSet(36, var2, var1);
   }

   public long getPagingMaxFileSize() {
      return this._PagingMaxFileSize;
   }

   public boolean isPagingMaxFileSizeSet() {
      return this._isSet(37);
   }

   public void setPagingMaxFileSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("PagingMaxFileSize", var1, 10485760L);
      long var3 = this._PagingMaxFileSize;
      this._PagingMaxFileSize = var1;
      this._postSet(37, var3, var1);
   }

   public int getPagingBlockSize() {
      return this._PagingBlockSize;
   }

   public boolean isPagingBlockSizeSet() {
      return this._isSet(38);
   }

   public void setPagingBlockSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PagingBlockSize", (long)var1, -1L, 8192L);
      int var2 = this._PagingBlockSize;
      this._PagingBlockSize = var1;
      this._postSet(38, var2, var1);
   }

   public void setExpirationScanInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ExpirationScanInterval", (long)var1, 0L, 2147483647L);
      int var2 = this._ExpirationScanInterval;
      this._ExpirationScanInterval = var1;
      this._postSet(39, var2, var1);
   }

   public int getExpirationScanInterval() {
      return this._ExpirationScanInterval;
   }

   public boolean isExpirationScanIntervalSet() {
      return this._isSet(39);
   }

   public int getMaximumMessageSize() {
      return this._MaximumMessageSize;
   }

   public boolean isMaximumMessageSizeSet() {
      return this._isSet(40);
   }

   public void setMaximumMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaximumMessageSize", (long)var1, 0L, 2147483647L);
      int var2 = this._MaximumMessageSize;
      this._MaximumMessageSize = var1;
      this._postSet(40, var2, var1);
   }

   public String getBlockingSendPolicy() {
      return this._BlockingSendPolicy;
   }

   public boolean isBlockingSendPolicySet() {
      return this._isSet(41);
   }

   public void setBlockingSendPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"FIFO", "Preemptive"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("BlockingSendPolicy", var1, var2);
      String var3 = this._BlockingSendPolicy;
      this._BlockingSendPolicy = var1;
      this._postSet(41, var3, var1);
   }

   public void setProductionPausedAtStartup(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ProductionPausedAtStartup;
      this._ProductionPausedAtStartup = var1;
      this._postSet(42, var2, var1);
   }

   public String getProductionPausedAtStartup() {
      return this._ProductionPausedAtStartup;
   }

   public boolean isProductionPausedAtStartupSet() {
      return this._isSet(42);
   }

   public void setInsertionPausedAtStartup(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._InsertionPausedAtStartup;
      this._InsertionPausedAtStartup = var1;
      this._postSet(43, var2, var1);
   }

   public String getInsertionPausedAtStartup() {
      return this._InsertionPausedAtStartup;
   }

   public boolean isInsertionPausedAtStartupSet() {
      return this._isSet(43);
   }

   public void setConsumptionPausedAtStartup(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConsumptionPausedAtStartup;
      this._ConsumptionPausedAtStartup = var1;
      this._postSet(44, var2, var1);
   }

   public String getConsumptionPausedAtStartup() {
      return this._ConsumptionPausedAtStartup;
   }

   public boolean isConsumptionPausedAtStartupSet() {
      return this._isSet(44);
   }

   public void addJMSQueue(JMSQueueMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 45)) {
         JMSQueueMBean[] var2;
         if (this._isSet(45)) {
            var2 = (JMSQueueMBean[])((JMSQueueMBean[])this._getHelper()._extendArray(this.getJMSQueues(), JMSQueueMBean.class, var1));
         } else {
            var2 = new JMSQueueMBean[]{var1};
         }

         try {
            this.setJMSQueues(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSQueueMBean[] getJMSQueues() {
      return this._JMSQueues;
   }

   public boolean isJMSQueuesSet() {
      return this._isSet(45);
   }

   public void removeJMSQueue(JMSQueueMBean var1) {
      this.destroyJMSQueue(var1);
   }

   public void setJMSQueues(JMSQueueMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSQueueMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 45)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSQueueMBean[] var5 = this._JMSQueues;
      this._JMSQueues = (JMSQueueMBean[])var4;
      this._postSet(45, var5, var4);
   }

   public JMSQueueMBean createJMSQueue(String var1) {
      JMSQueueMBeanImpl var2 = new JMSQueueMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSQueue(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2) {
      return this._customizer.createJMSQueue(var1, var2);
   }

   public void destroyJMSQueue(JMSQueueMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 45);
         JMSQueueMBean[] var2 = this.getJMSQueues();
         JMSQueueMBean[] var3 = (JMSQueueMBean[])((JMSQueueMBean[])this._getHelper()._removeElement(var2, JMSQueueMBean.class, var1));
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
               this.setJMSQueues(var3);
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

   public JMSQueueMBean lookupJMSQueue(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSQueues).iterator();

      JMSQueueMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSQueueMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSTopic(JMSTopicMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 46)) {
         JMSTopicMBean[] var2;
         if (this._isSet(46)) {
            var2 = (JMSTopicMBean[])((JMSTopicMBean[])this._getHelper()._extendArray(this.getJMSTopics(), JMSTopicMBean.class, var1));
         } else {
            var2 = new JMSTopicMBean[]{var1};
         }

         try {
            this.setJMSTopics(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSTopicMBean[] getJMSTopics() {
      return this._JMSTopics;
   }

   public boolean isJMSTopicsSet() {
      return this._isSet(46);
   }

   public void removeJMSTopic(JMSTopicMBean var1) {
      this.destroyJMSTopic(var1);
   }

   public void setJMSTopics(JMSTopicMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSTopicMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 46)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSTopicMBean[] var5 = this._JMSTopics;
      this._JMSTopics = (JMSTopicMBean[])var4;
      this._postSet(46, var5, var4);
   }

   public JMSTopicMBean createJMSTopic(String var1) {
      JMSTopicMBeanImpl var2 = new JMSTopicMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSTopic(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2) {
      return this._customizer.createJMSTopic(var1, var2);
   }

   public void destroyJMSTopic(JMSTopicMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 46);
         JMSTopicMBean[] var2 = this.getJMSTopics();
         JMSTopicMBean[] var3 = (JMSTopicMBean[])((JMSTopicMBean[])this._getHelper()._removeElement(var2, JMSTopicMBean.class, var1));
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
               this.setJMSTopics(var3);
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

   public JMSTopicMBean lookupJMSTopic(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSTopics).iterator();

      JMSTopicMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSTopicMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public JMSMessageLogFileMBean getJMSMessageLogFile() {
      return this._JMSMessageLogFile;
   }

   public boolean isJMSMessageLogFileSet() {
      return this._isSet(47) || this._isAnythingSet((AbstractDescriptorBean)this.getJMSMessageLogFile());
   }

   public void setJMSMessageLogFile(JMSMessageLogFileMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 47)) {
         this._postCreate(var2);
      }

      JMSMessageLogFileMBean var3 = this._JMSMessageLogFile;
      this._JMSMessageLogFile = var1;
      this._postSet(47, var3, var1);
   }

   public void useDelegates(DomainMBean var1) {
      this._customizer.useDelegates(var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      JMSLegalHelper.validateJMSServer(this);
      JMSLegalHelper.validateServerBytesValues((JMSServerMBean)this);
      JMSLegalHelper.validateServerMessagesValues((JMSServerMBean)this);
   }

   protected void _preDestroy() {
      this._customizer._preDestroy();
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
      return super._isAnythingSet() || this.isJMSMessageLogFileSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 41;
      }

      try {
         switch (var1) {
            case 41:
               this._BlockingSendPolicy = "FIFO";
               if (var2) {
                  break;
               }
            case 21:
               this._BytesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 22:
               this._BytesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 23:
               this._BytesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 44:
               this._ConsumptionPausedAtStartup = "default";
               if (var2) {
                  break;
               }
            case 12:
               this._Destinations = new JMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 39:
               this._ExpirationScanInterval = 30;
               if (var2) {
                  break;
               }
            case 43:
               this._InsertionPausedAtStartup = "default";
               if (var2) {
                  break;
               }
            case 47:
               this._JMSMessageLogFile = new JMSMessageLogFileMBeanImpl(this, 47);
               this._postCreate((AbstractDescriptorBean)this._JMSMessageLogFile);
               if (var2) {
                  break;
               }
            case 45:
               this._JMSQueues = new JMSQueueMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._JMSSessionPools = new JMSSessionPoolMBean[0];
               if (var2) {
                  break;
               }
            case 46:
               this._JMSTopics = new JMSTopicMBean[0];
               if (var2) {
                  break;
               }
            case 40:
               this._MaximumMessageSize = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 31:
               this._MessageBufferSize = -1L;
               if (var2) {
                  break;
               }
            case 24:
               this._MessagesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 25:
               this._MessagesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 26:
               this._MessagesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 38:
               this._PagingBlockSize = -1;
               if (var2) {
                  break;
               }
            case 32:
               this._PagingDirectory = null;
               if (var2) {
                  break;
               }
            case 36:
               this._PagingIoBufferSize = -1;
               if (var2) {
                  break;
               }
            case 37:
               this._PagingMaxFileSize = 1342177280L;
               if (var2) {
                  break;
               }
            case 35:
               this._PagingMaxWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 34:
               this._PagingMinWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 28:
               this._PagingStore = null;
               if (var2) {
                  break;
               }
            case 14:
               this._PersistentStore = null;
               if (var2) {
                  break;
               }
            case 42:
               this._ProductionPausedAtStartup = "default";
               if (var2) {
                  break;
               }
            case 9:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setSessionPools(new JMSSessionPoolMBean[0]);
               if (var2) {
                  break;
               }
            case 13:
               this._Store = null;
               if (var2) {
                  break;
               }
            case 15:
               this._StoreEnabled = true;
               if (var2) {
                  break;
               }
            case 7:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._customizer.setTemporaryTemplate((JMSTemplateMBean)null);
               if (var2) {
                  break;
               }
            case 20:
               this._TemporaryTemplateName = null;
               if (var2) {
                  break;
               }
            case 19:
               this._TemporaryTemplateResource = null;
               if (var2) {
                  break;
               }
            case 16:
               this._AllowsPersistentDowngrade = false;
               if (var2) {
                  break;
               }
            case 30:
               this._BytesPagingEnabled = false;
               if (var2) {
                  break;
               }
            case 18:
               this._HostingTemporaryDestinations = true;
               if (var2) {
                  break;
               }
            case 27:
               this._JDBCStoreUpgradeEnabled = true;
               if (var2) {
                  break;
               }
            case 29:
               this._MessagesPagingEnabled = false;
               if (var2) {
                  break;
               }
            case 33:
               this._PagingFileLockingEnabled = true;
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
      return "JMSServer";
   }

   public void putValue(String var1, Object var2) {
      boolean var8;
      if (var1.equals("AllowsPersistentDowngrade")) {
         var8 = this._AllowsPersistentDowngrade;
         this._AllowsPersistentDowngrade = (Boolean)var2;
         this._postSet(16, var8, this._AllowsPersistentDowngrade);
      } else {
         String var5;
         if (var1.equals("BlockingSendPolicy")) {
            var5 = this._BlockingSendPolicy;
            this._BlockingSendPolicy = (String)var2;
            this._postSet(41, var5, this._BlockingSendPolicy);
         } else {
            long var14;
            if (var1.equals("BytesMaximum")) {
               var14 = this._BytesMaximum;
               this._BytesMaximum = (Long)var2;
               this._postSet(21, var14, this._BytesMaximum);
            } else if (var1.equals("BytesPagingEnabled")) {
               var8 = this._BytesPagingEnabled;
               this._BytesPagingEnabled = (Boolean)var2;
               this._postSet(30, var8, this._BytesPagingEnabled);
            } else if (var1.equals("BytesThresholdHigh")) {
               var14 = this._BytesThresholdHigh;
               this._BytesThresholdHigh = (Long)var2;
               this._postSet(22, var14, this._BytesThresholdHigh);
            } else if (var1.equals("BytesThresholdLow")) {
               var14 = this._BytesThresholdLow;
               this._BytesThresholdLow = (Long)var2;
               this._postSet(23, var14, this._BytesThresholdLow);
            } else if (var1.equals("ConsumptionPausedAtStartup")) {
               var5 = this._ConsumptionPausedAtStartup;
               this._ConsumptionPausedAtStartup = (String)var2;
               this._postSet(44, var5, this._ConsumptionPausedAtStartup);
            } else if (var1.equals("Destinations")) {
               JMSDestinationMBean[] var18 = this._Destinations;
               this._Destinations = (JMSDestinationMBean[])((JMSDestinationMBean[])var2);
               this._postSet(12, var18, this._Destinations);
            } else {
               int var13;
               if (var1.equals("ExpirationScanInterval")) {
                  var13 = this._ExpirationScanInterval;
                  this._ExpirationScanInterval = (Integer)var2;
                  this._postSet(39, var13, this._ExpirationScanInterval);
               } else if (var1.equals("HostingTemporaryDestinations")) {
                  var8 = this._HostingTemporaryDestinations;
                  this._HostingTemporaryDestinations = (Boolean)var2;
                  this._postSet(18, var8, this._HostingTemporaryDestinations);
               } else if (var1.equals("InsertionPausedAtStartup")) {
                  var5 = this._InsertionPausedAtStartup;
                  this._InsertionPausedAtStartup = (String)var2;
                  this._postSet(43, var5, this._InsertionPausedAtStartup);
               } else if (var1.equals("JDBCStoreUpgradeEnabled")) {
                  var8 = this._JDBCStoreUpgradeEnabled;
                  this._JDBCStoreUpgradeEnabled = (Boolean)var2;
                  this._postSet(27, var8, this._JDBCStoreUpgradeEnabled);
               } else if (var1.equals("JMSMessageLogFile")) {
                  JMSMessageLogFileMBean var17 = this._JMSMessageLogFile;
                  this._JMSMessageLogFile = (JMSMessageLogFileMBean)var2;
                  this._postSet(47, var17, this._JMSMessageLogFile);
               } else if (var1.equals("JMSQueues")) {
                  JMSQueueMBean[] var16 = this._JMSQueues;
                  this._JMSQueues = (JMSQueueMBean[])((JMSQueueMBean[])var2);
                  this._postSet(45, var16, this._JMSQueues);
               } else {
                  JMSSessionPoolMBean[] var10;
                  if (var1.equals("JMSSessionPools")) {
                     var10 = this._JMSSessionPools;
                     this._JMSSessionPools = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])var2);
                     this._postSet(11, var10, this._JMSSessionPools);
                  } else if (var1.equals("JMSTopics")) {
                     JMSTopicMBean[] var15 = this._JMSTopics;
                     this._JMSTopics = (JMSTopicMBean[])((JMSTopicMBean[])var2);
                     this._postSet(46, var15, this._JMSTopics);
                  } else if (var1.equals("MaximumMessageSize")) {
                     var13 = this._MaximumMessageSize;
                     this._MaximumMessageSize = (Integer)var2;
                     this._postSet(40, var13, this._MaximumMessageSize);
                  } else if (var1.equals("MessageBufferSize")) {
                     var14 = this._MessageBufferSize;
                     this._MessageBufferSize = (Long)var2;
                     this._postSet(31, var14, this._MessageBufferSize);
                  } else if (var1.equals("MessagesMaximum")) {
                     var14 = this._MessagesMaximum;
                     this._MessagesMaximum = (Long)var2;
                     this._postSet(24, var14, this._MessagesMaximum);
                  } else if (var1.equals("MessagesPagingEnabled")) {
                     var8 = this._MessagesPagingEnabled;
                     this._MessagesPagingEnabled = (Boolean)var2;
                     this._postSet(29, var8, this._MessagesPagingEnabled);
                  } else if (var1.equals("MessagesThresholdHigh")) {
                     var14 = this._MessagesThresholdHigh;
                     this._MessagesThresholdHigh = (Long)var2;
                     this._postSet(25, var14, this._MessagesThresholdHigh);
                  } else if (var1.equals("MessagesThresholdLow")) {
                     var14 = this._MessagesThresholdLow;
                     this._MessagesThresholdLow = (Long)var2;
                     this._postSet(26, var14, this._MessagesThresholdLow);
                  } else if (var1.equals("Name")) {
                     var5 = this._Name;
                     this._Name = (String)var2;
                     this._postSet(2, var5, this._Name);
                  } else if (var1.equals("PagingBlockSize")) {
                     var13 = this._PagingBlockSize;
                     this._PagingBlockSize = (Integer)var2;
                     this._postSet(38, var13, this._PagingBlockSize);
                  } else if (var1.equals("PagingDirectory")) {
                     var5 = this._PagingDirectory;
                     this._PagingDirectory = (String)var2;
                     this._postSet(32, var5, this._PagingDirectory);
                  } else if (var1.equals("PagingFileLockingEnabled")) {
                     var8 = this._PagingFileLockingEnabled;
                     this._PagingFileLockingEnabled = (Boolean)var2;
                     this._postSet(33, var8, this._PagingFileLockingEnabled);
                  } else if (var1.equals("PagingIoBufferSize")) {
                     var13 = this._PagingIoBufferSize;
                     this._PagingIoBufferSize = (Integer)var2;
                     this._postSet(36, var13, this._PagingIoBufferSize);
                  } else if (var1.equals("PagingMaxFileSize")) {
                     var14 = this._PagingMaxFileSize;
                     this._PagingMaxFileSize = (Long)var2;
                     this._postSet(37, var14, this._PagingMaxFileSize);
                  } else if (var1.equals("PagingMaxWindowBufferSize")) {
                     var13 = this._PagingMaxWindowBufferSize;
                     this._PagingMaxWindowBufferSize = (Integer)var2;
                     this._postSet(35, var13, this._PagingMaxWindowBufferSize);
                  } else if (var1.equals("PagingMinWindowBufferSize")) {
                     var13 = this._PagingMinWindowBufferSize;
                     this._PagingMinWindowBufferSize = (Integer)var2;
                     this._postSet(34, var13, this._PagingMinWindowBufferSize);
                  } else {
                     JMSStoreMBean var9;
                     if (var1.equals("PagingStore")) {
                        var9 = this._PagingStore;
                        this._PagingStore = (JMSStoreMBean)var2;
                        this._postSet(28, var9, this._PagingStore);
                     } else if (var1.equals("PersistentStore")) {
                        PersistentStoreMBean var12 = this._PersistentStore;
                        this._PersistentStore = (PersistentStoreMBean)var2;
                        this._postSet(14, var12, this._PersistentStore);
                     } else if (var1.equals("ProductionPausedAtStartup")) {
                        var5 = this._ProductionPausedAtStartup;
                        this._ProductionPausedAtStartup = (String)var2;
                        this._postSet(42, var5, this._ProductionPausedAtStartup);
                     } else if (var1.equals("ServerNames")) {
                        Set var11 = this._ServerNames;
                        this._ServerNames = (Set)var2;
                        this._postSet(9, var11, this._ServerNames);
                     } else if (var1.equals("SessionPools")) {
                        var10 = this._SessionPools;
                        this._SessionPools = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])var2);
                        this._postSet(10, var10, this._SessionPools);
                     } else if (var1.equals("Store")) {
                        var9 = this._Store;
                        this._Store = (JMSStoreMBean)var2;
                        this._postSet(13, var9, this._Store);
                     } else if (var1.equals("StoreEnabled")) {
                        var8 = this._StoreEnabled;
                        this._StoreEnabled = (Boolean)var2;
                        this._postSet(15, var8, this._StoreEnabled);
                     } else if (var1.equals("Targets")) {
                        TargetMBean[] var7 = this._Targets;
                        this._Targets = (TargetMBean[])((TargetMBean[])var2);
                        this._postSet(7, var7, this._Targets);
                     } else if (var1.equals("TemporaryTemplate")) {
                        JMSTemplateMBean var6 = this._TemporaryTemplate;
                        this._TemporaryTemplate = (JMSTemplateMBean)var2;
                        this._postSet(17, var6, this._TemporaryTemplate);
                     } else if (var1.equals("TemporaryTemplateName")) {
                        var5 = this._TemporaryTemplateName;
                        this._TemporaryTemplateName = (String)var2;
                        this._postSet(20, var5, this._TemporaryTemplateName);
                     } else if (var1.equals("TemporaryTemplateResource")) {
                        var5 = this._TemporaryTemplateResource;
                        this._TemporaryTemplateResource = (String)var2;
                        this._postSet(19, var5, this._TemporaryTemplateResource);
                     } else if (var1.equals("customizer")) {
                        JMSServer var3 = this._customizer;
                        this._customizer = (JMSServer)var2;
                     } else {
                        super.putValue(var1, var2);
                     }
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllowsPersistentDowngrade")) {
         return new Boolean(this._AllowsPersistentDowngrade);
      } else if (var1.equals("BlockingSendPolicy")) {
         return this._BlockingSendPolicy;
      } else if (var1.equals("BytesMaximum")) {
         return new Long(this._BytesMaximum);
      } else if (var1.equals("BytesPagingEnabled")) {
         return new Boolean(this._BytesPagingEnabled);
      } else if (var1.equals("BytesThresholdHigh")) {
         return new Long(this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         return new Long(this._BytesThresholdLow);
      } else if (var1.equals("ConsumptionPausedAtStartup")) {
         return this._ConsumptionPausedAtStartup;
      } else if (var1.equals("Destinations")) {
         return this._Destinations;
      } else if (var1.equals("ExpirationScanInterval")) {
         return new Integer(this._ExpirationScanInterval);
      } else if (var1.equals("HostingTemporaryDestinations")) {
         return new Boolean(this._HostingTemporaryDestinations);
      } else if (var1.equals("InsertionPausedAtStartup")) {
         return this._InsertionPausedAtStartup;
      } else if (var1.equals("JDBCStoreUpgradeEnabled")) {
         return new Boolean(this._JDBCStoreUpgradeEnabled);
      } else if (var1.equals("JMSMessageLogFile")) {
         return this._JMSMessageLogFile;
      } else if (var1.equals("JMSQueues")) {
         return this._JMSQueues;
      } else if (var1.equals("JMSSessionPools")) {
         return this._JMSSessionPools;
      } else if (var1.equals("JMSTopics")) {
         return this._JMSTopics;
      } else if (var1.equals("MaximumMessageSize")) {
         return new Integer(this._MaximumMessageSize);
      } else if (var1.equals("MessageBufferSize")) {
         return new Long(this._MessageBufferSize);
      } else if (var1.equals("MessagesMaximum")) {
         return new Long(this._MessagesMaximum);
      } else if (var1.equals("MessagesPagingEnabled")) {
         return new Boolean(this._MessagesPagingEnabled);
      } else if (var1.equals("MessagesThresholdHigh")) {
         return new Long(this._MessagesThresholdHigh);
      } else if (var1.equals("MessagesThresholdLow")) {
         return new Long(this._MessagesThresholdLow);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PagingBlockSize")) {
         return new Integer(this._PagingBlockSize);
      } else if (var1.equals("PagingDirectory")) {
         return this._PagingDirectory;
      } else if (var1.equals("PagingFileLockingEnabled")) {
         return new Boolean(this._PagingFileLockingEnabled);
      } else if (var1.equals("PagingIoBufferSize")) {
         return new Integer(this._PagingIoBufferSize);
      } else if (var1.equals("PagingMaxFileSize")) {
         return new Long(this._PagingMaxFileSize);
      } else if (var1.equals("PagingMaxWindowBufferSize")) {
         return new Integer(this._PagingMaxWindowBufferSize);
      } else if (var1.equals("PagingMinWindowBufferSize")) {
         return new Integer(this._PagingMinWindowBufferSize);
      } else if (var1.equals("PagingStore")) {
         return this._PagingStore;
      } else if (var1.equals("PersistentStore")) {
         return this._PersistentStore;
      } else if (var1.equals("ProductionPausedAtStartup")) {
         return this._ProductionPausedAtStartup;
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("SessionPools")) {
         return this._SessionPools;
      } else if (var1.equals("Store")) {
         return this._Store;
      } else if (var1.equals("StoreEnabled")) {
         return new Boolean(this._StoreEnabled);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TemporaryTemplate")) {
         return this._TemporaryTemplate;
      } else if (var1.equals("TemporaryTemplateName")) {
         return this._TemporaryTemplateName;
      } else if (var1.equals("TemporaryTemplateResource")) {
         return this._TemporaryTemplateResource;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 5:
               if (var1.equals("store")) {
                  return 13;
               }
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
            case 7:
            case 8:
            case 10:
            case 14:
            case 15:
            case 25:
            default:
               break;
            case 9:
               if (var1.equals("jms-queue")) {
                  return 45;
               }

               if (var1.equals("jms-topic")) {
                  return 46;
               }
               break;
            case 11:
               if (var1.equals("destination")) {
                  return 12;
               }
               break;
            case 12:
               if (var1.equals("paging-store")) {
                  return 28;
               }

               if (var1.equals("server-names")) {
                  return 9;
               }

               if (var1.equals("session-pool")) {
                  return 10;
               }
               break;
            case 13:
               if (var1.equals("bytes-maximum")) {
                  return 21;
               }

               if (var1.equals("store-enabled")) {
                  return 15;
               }
               break;
            case 16:
               if (var1.equals("jms-session-pool")) {
                  return 11;
               }

               if (var1.equals("messages-maximum")) {
                  return 24;
               }

               if (var1.equals("paging-directory")) {
                  return 32;
               }

               if (var1.equals("persistent-store")) {
                  return 14;
               }
               break;
            case 17:
               if (var1.equals("paging-block-size")) {
                  return 38;
               }
               break;
            case 18:
               if (var1.equals("temporary-template")) {
                  return 17;
               }
               break;
            case 19:
               if (var1.equals("bytes-threshold-low")) {
                  return 23;
               }

               if (var1.equals("message-buffer-size")) {
                  return 31;
               }
               break;
            case 20:
               if (var1.equals("blocking-send-policy")) {
                  return 41;
               }

               if (var1.equals("bytes-threshold-high")) {
                  return 22;
               }

               if (var1.equals("jms-message-log-file")) {
                  return 47;
               }

               if (var1.equals("maximum-message-size")) {
                  return 40;
               }

               if (var1.equals("paging-max-file-size")) {
                  return 37;
               }

               if (var1.equals("bytes-paging-enabled")) {
                  return 30;
               }
               break;
            case 21:
               if (var1.equals("paging-io-buffer-size")) {
                  return 36;
               }
               break;
            case 22:
               if (var1.equals("messages-threshold-low")) {
                  return 26;
               }
               break;
            case 23:
               if (var1.equals("messages-threshold-high")) {
                  return 25;
               }

               if (var1.equals("temporary-template-name")) {
                  return 20;
               }

               if (var1.equals("messages-paging-enabled")) {
                  return 29;
               }
               break;
            case 24:
               if (var1.equals("expiration-scan-interval")) {
                  return 39;
               }
               break;
            case 26:
               if (var1.equals("jdbc-store-upgrade-enabled")) {
                  return 27;
               }
               break;
            case 27:
               if (var1.equals("insertion-paused-at-startup")) {
                  return 43;
               }

               if (var1.equals("temporary-template-resource")) {
                  return 19;
               }

               if (var1.equals("allows-persistent-downgrade")) {
                  return 16;
               }

               if (var1.equals("paging-file-locking-enabled")) {
                  return 33;
               }
               break;
            case 28:
               if (var1.equals("production-paused-at-startup")) {
                  return 42;
               }
               break;
            case 29:
               if (var1.equals("consumption-paused-at-startup")) {
                  return 44;
               }

               if (var1.equals("paging-max-window-buffer-size")) {
                  return 35;
               }

               if (var1.equals("paging-min-window-buffer-size")) {
                  return 34;
               }
               break;
            case 30:
               if (var1.equals("hosting-temporary-destinations")) {
                  return 18;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 11:
               return new JMSSessionPoolMBeanImpl.SchemaHelper2();
            case 45:
               return new JMSQueueMBeanImpl.SchemaHelper2();
            case 46:
               return new JMSTopicMBeanImpl.SchemaHelper2();
            case 47:
               return new JMSMessageLogFileMBeanImpl.SchemaHelper2();
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
               return "server-names";
            case 10:
               return "session-pool";
            case 11:
               return "jms-session-pool";
            case 12:
               return "destination";
            case 13:
               return "store";
            case 14:
               return "persistent-store";
            case 15:
               return "store-enabled";
            case 16:
               return "allows-persistent-downgrade";
            case 17:
               return "temporary-template";
            case 18:
               return "hosting-temporary-destinations";
            case 19:
               return "temporary-template-resource";
            case 20:
               return "temporary-template-name";
            case 21:
               return "bytes-maximum";
            case 22:
               return "bytes-threshold-high";
            case 23:
               return "bytes-threshold-low";
            case 24:
               return "messages-maximum";
            case 25:
               return "messages-threshold-high";
            case 26:
               return "messages-threshold-low";
            case 27:
               return "jdbc-store-upgrade-enabled";
            case 28:
               return "paging-store";
            case 29:
               return "messages-paging-enabled";
            case 30:
               return "bytes-paging-enabled";
            case 31:
               return "message-buffer-size";
            case 32:
               return "paging-directory";
            case 33:
               return "paging-file-locking-enabled";
            case 34:
               return "paging-min-window-buffer-size";
            case 35:
               return "paging-max-window-buffer-size";
            case 36:
               return "paging-io-buffer-size";
            case 37:
               return "paging-max-file-size";
            case 38:
               return "paging-block-size";
            case 39:
               return "expiration-scan-interval";
            case 40:
               return "maximum-message-size";
            case 41:
               return "blocking-send-policy";
            case 42:
               return "production-paused-at-startup";
            case 43:
               return "insertion-paused-at-startup";
            case 44:
               return "consumption-paused-at-startup";
            case 45:
               return "jms-queue";
            case 46:
               return "jms-topic";
            case 47:
               return "jms-message-log-file";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 45:
               return true;
            case 46:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 11:
               return true;
            case 45:
               return true;
            case 46:
               return true;
            case 47:
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
      private JMSServerMBeanImpl bean;

      protected Helper(JMSServerMBeanImpl var1) {
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
               return "ServerNames";
            case 10:
               return "SessionPools";
            case 11:
               return "JMSSessionPools";
            case 12:
               return "Destinations";
            case 13:
               return "Store";
            case 14:
               return "PersistentStore";
            case 15:
               return "StoreEnabled";
            case 16:
               return "AllowsPersistentDowngrade";
            case 17:
               return "TemporaryTemplate";
            case 18:
               return "HostingTemporaryDestinations";
            case 19:
               return "TemporaryTemplateResource";
            case 20:
               return "TemporaryTemplateName";
            case 21:
               return "BytesMaximum";
            case 22:
               return "BytesThresholdHigh";
            case 23:
               return "BytesThresholdLow";
            case 24:
               return "MessagesMaximum";
            case 25:
               return "MessagesThresholdHigh";
            case 26:
               return "MessagesThresholdLow";
            case 27:
               return "JDBCStoreUpgradeEnabled";
            case 28:
               return "PagingStore";
            case 29:
               return "MessagesPagingEnabled";
            case 30:
               return "BytesPagingEnabled";
            case 31:
               return "MessageBufferSize";
            case 32:
               return "PagingDirectory";
            case 33:
               return "PagingFileLockingEnabled";
            case 34:
               return "PagingMinWindowBufferSize";
            case 35:
               return "PagingMaxWindowBufferSize";
            case 36:
               return "PagingIoBufferSize";
            case 37:
               return "PagingMaxFileSize";
            case 38:
               return "PagingBlockSize";
            case 39:
               return "ExpirationScanInterval";
            case 40:
               return "MaximumMessageSize";
            case 41:
               return "BlockingSendPolicy";
            case 42:
               return "ProductionPausedAtStartup";
            case 43:
               return "InsertionPausedAtStartup";
            case 44:
               return "ConsumptionPausedAtStartup";
            case 45:
               return "JMSQueues";
            case 46:
               return "JMSTopics";
            case 47:
               return "JMSMessageLogFile";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BlockingSendPolicy")) {
            return 41;
         } else if (var1.equals("BytesMaximum")) {
            return 21;
         } else if (var1.equals("BytesThresholdHigh")) {
            return 22;
         } else if (var1.equals("BytesThresholdLow")) {
            return 23;
         } else if (var1.equals("ConsumptionPausedAtStartup")) {
            return 44;
         } else if (var1.equals("Destinations")) {
            return 12;
         } else if (var1.equals("ExpirationScanInterval")) {
            return 39;
         } else if (var1.equals("InsertionPausedAtStartup")) {
            return 43;
         } else if (var1.equals("JMSMessageLogFile")) {
            return 47;
         } else if (var1.equals("JMSQueues")) {
            return 45;
         } else if (var1.equals("JMSSessionPools")) {
            return 11;
         } else if (var1.equals("JMSTopics")) {
            return 46;
         } else if (var1.equals("MaximumMessageSize")) {
            return 40;
         } else if (var1.equals("MessageBufferSize")) {
            return 31;
         } else if (var1.equals("MessagesMaximum")) {
            return 24;
         } else if (var1.equals("MessagesThresholdHigh")) {
            return 25;
         } else if (var1.equals("MessagesThresholdLow")) {
            return 26;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PagingBlockSize")) {
            return 38;
         } else if (var1.equals("PagingDirectory")) {
            return 32;
         } else if (var1.equals("PagingIoBufferSize")) {
            return 36;
         } else if (var1.equals("PagingMaxFileSize")) {
            return 37;
         } else if (var1.equals("PagingMaxWindowBufferSize")) {
            return 35;
         } else if (var1.equals("PagingMinWindowBufferSize")) {
            return 34;
         } else if (var1.equals("PagingStore")) {
            return 28;
         } else if (var1.equals("PersistentStore")) {
            return 14;
         } else if (var1.equals("ProductionPausedAtStartup")) {
            return 42;
         } else if (var1.equals("ServerNames")) {
            return 9;
         } else if (var1.equals("SessionPools")) {
            return 10;
         } else if (var1.equals("Store")) {
            return 13;
         } else if (var1.equals("StoreEnabled")) {
            return 15;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TemporaryTemplate")) {
            return 17;
         } else if (var1.equals("TemporaryTemplateName")) {
            return 20;
         } else if (var1.equals("TemporaryTemplateResource")) {
            return 19;
         } else if (var1.equals("AllowsPersistentDowngrade")) {
            return 16;
         } else if (var1.equals("BytesPagingEnabled")) {
            return 30;
         } else if (var1.equals("HostingTemporaryDestinations")) {
            return 18;
         } else if (var1.equals("JDBCStoreUpgradeEnabled")) {
            return 27;
         } else if (var1.equals("MessagesPagingEnabled")) {
            return 29;
         } else {
            return var1.equals("PagingFileLockingEnabled") ? 33 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getJMSMessageLogFile() != null) {
            var1.add(new ArrayIterator(new JMSMessageLogFileMBean[]{this.bean.getJMSMessageLogFile()}));
         }

         var1.add(new ArrayIterator(this.bean.getJMSQueues()));
         var1.add(new ArrayIterator(this.bean.getJMSSessionPools()));
         var1.add(new ArrayIterator(this.bean.getJMSTopics()));
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
            if (this.bean.isBlockingSendPolicySet()) {
               var2.append("BlockingSendPolicy");
               var2.append(String.valueOf(this.bean.getBlockingSendPolicy()));
            }

            if (this.bean.isBytesMaximumSet()) {
               var2.append("BytesMaximum");
               var2.append(String.valueOf(this.bean.getBytesMaximum()));
            }

            if (this.bean.isBytesThresholdHighSet()) {
               var2.append("BytesThresholdHigh");
               var2.append(String.valueOf(this.bean.getBytesThresholdHigh()));
            }

            if (this.bean.isBytesThresholdLowSet()) {
               var2.append("BytesThresholdLow");
               var2.append(String.valueOf(this.bean.getBytesThresholdLow()));
            }

            if (this.bean.isConsumptionPausedAtStartupSet()) {
               var2.append("ConsumptionPausedAtStartup");
               var2.append(String.valueOf(this.bean.getConsumptionPausedAtStartup()));
            }

            if (this.bean.isDestinationsSet()) {
               var2.append("Destinations");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDestinations())));
            }

            if (this.bean.isExpirationScanIntervalSet()) {
               var2.append("ExpirationScanInterval");
               var2.append(String.valueOf(this.bean.getExpirationScanInterval()));
            }

            if (this.bean.isInsertionPausedAtStartupSet()) {
               var2.append("InsertionPausedAtStartup");
               var2.append(String.valueOf(this.bean.getInsertionPausedAtStartup()));
            }

            var5 = this.computeChildHashValue(this.bean.getJMSMessageLogFile());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getJMSQueues().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSQueues()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSSessionPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSSessionPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSTopics().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSTopics()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isMaximumMessageSizeSet()) {
               var2.append("MaximumMessageSize");
               var2.append(String.valueOf(this.bean.getMaximumMessageSize()));
            }

            if (this.bean.isMessageBufferSizeSet()) {
               var2.append("MessageBufferSize");
               var2.append(String.valueOf(this.bean.getMessageBufferSize()));
            }

            if (this.bean.isMessagesMaximumSet()) {
               var2.append("MessagesMaximum");
               var2.append(String.valueOf(this.bean.getMessagesMaximum()));
            }

            if (this.bean.isMessagesThresholdHighSet()) {
               var2.append("MessagesThresholdHigh");
               var2.append(String.valueOf(this.bean.getMessagesThresholdHigh()));
            }

            if (this.bean.isMessagesThresholdLowSet()) {
               var2.append("MessagesThresholdLow");
               var2.append(String.valueOf(this.bean.getMessagesThresholdLow()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPagingBlockSizeSet()) {
               var2.append("PagingBlockSize");
               var2.append(String.valueOf(this.bean.getPagingBlockSize()));
            }

            if (this.bean.isPagingDirectorySet()) {
               var2.append("PagingDirectory");
               var2.append(String.valueOf(this.bean.getPagingDirectory()));
            }

            if (this.bean.isPagingIoBufferSizeSet()) {
               var2.append("PagingIoBufferSize");
               var2.append(String.valueOf(this.bean.getPagingIoBufferSize()));
            }

            if (this.bean.isPagingMaxFileSizeSet()) {
               var2.append("PagingMaxFileSize");
               var2.append(String.valueOf(this.bean.getPagingMaxFileSize()));
            }

            if (this.bean.isPagingMaxWindowBufferSizeSet()) {
               var2.append("PagingMaxWindowBufferSize");
               var2.append(String.valueOf(this.bean.getPagingMaxWindowBufferSize()));
            }

            if (this.bean.isPagingMinWindowBufferSizeSet()) {
               var2.append("PagingMinWindowBufferSize");
               var2.append(String.valueOf(this.bean.getPagingMinWindowBufferSize()));
            }

            if (this.bean.isPagingStoreSet()) {
               var2.append("PagingStore");
               var2.append(String.valueOf(this.bean.getPagingStore()));
            }

            if (this.bean.isPersistentStoreSet()) {
               var2.append("PersistentStore");
               var2.append(String.valueOf(this.bean.getPersistentStore()));
            }

            if (this.bean.isProductionPausedAtStartupSet()) {
               var2.append("ProductionPausedAtStartup");
               var2.append(String.valueOf(this.bean.getProductionPausedAtStartup()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            if (this.bean.isSessionPoolsSet()) {
               var2.append("SessionPools");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSessionPools())));
            }

            if (this.bean.isStoreSet()) {
               var2.append("Store");
               var2.append(String.valueOf(this.bean.getStore()));
            }

            if (this.bean.isStoreEnabledSet()) {
               var2.append("StoreEnabled");
               var2.append(String.valueOf(this.bean.getStoreEnabled()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTemporaryTemplateSet()) {
               var2.append("TemporaryTemplate");
               var2.append(String.valueOf(this.bean.getTemporaryTemplate()));
            }

            if (this.bean.isTemporaryTemplateNameSet()) {
               var2.append("TemporaryTemplateName");
               var2.append(String.valueOf(this.bean.getTemporaryTemplateName()));
            }

            if (this.bean.isTemporaryTemplateResourceSet()) {
               var2.append("TemporaryTemplateResource");
               var2.append(String.valueOf(this.bean.getTemporaryTemplateResource()));
            }

            if (this.bean.isAllowsPersistentDowngradeSet()) {
               var2.append("AllowsPersistentDowngrade");
               var2.append(String.valueOf(this.bean.isAllowsPersistentDowngrade()));
            }

            if (this.bean.isBytesPagingEnabledSet()) {
               var2.append("BytesPagingEnabled");
               var2.append(String.valueOf(this.bean.isBytesPagingEnabled()));
            }

            if (this.bean.isHostingTemporaryDestinationsSet()) {
               var2.append("HostingTemporaryDestinations");
               var2.append(String.valueOf(this.bean.isHostingTemporaryDestinations()));
            }

            if (this.bean.isJDBCStoreUpgradeEnabledSet()) {
               var2.append("JDBCStoreUpgradeEnabled");
               var2.append(String.valueOf(this.bean.isJDBCStoreUpgradeEnabled()));
            }

            if (this.bean.isMessagesPagingEnabledSet()) {
               var2.append("MessagesPagingEnabled");
               var2.append(String.valueOf(this.bean.isMessagesPagingEnabled()));
            }

            if (this.bean.isPagingFileLockingEnabledSet()) {
               var2.append("PagingFileLockingEnabled");
               var2.append(String.valueOf(this.bean.isPagingFileLockingEnabled()));
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
            JMSServerMBeanImpl var2 = (JMSServerMBeanImpl)var1;
            this.computeDiff("BlockingSendPolicy", this.bean.getBlockingSendPolicy(), var2.getBlockingSendPolicy(), true);
            this.computeDiff("BytesMaximum", this.bean.getBytesMaximum(), var2.getBytesMaximum(), true);
            this.computeDiff("BytesThresholdHigh", this.bean.getBytesThresholdHigh(), var2.getBytesThresholdHigh(), true);
            this.computeDiff("BytesThresholdLow", this.bean.getBytesThresholdLow(), var2.getBytesThresholdLow(), true);
            this.computeDiff("ConsumptionPausedAtStartup", this.bean.getConsumptionPausedAtStartup(), var2.getConsumptionPausedAtStartup(), false);
            this.computeDiff("ExpirationScanInterval", this.bean.getExpirationScanInterval(), var2.getExpirationScanInterval(), true);
            this.computeDiff("InsertionPausedAtStartup", this.bean.getInsertionPausedAtStartup(), var2.getInsertionPausedAtStartup(), false);
            this.computeSubDiff("JMSMessageLogFile", this.bean.getJMSMessageLogFile(), var2.getJMSMessageLogFile());
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSQueues", this.bean.getJMSQueues(), var2.getJMSQueues(), false);
            }

            this.computeChildDiff("JMSSessionPools", this.bean.getJMSSessionPools(), var2.getJMSSessionPools(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSTopics", this.bean.getJMSTopics(), var2.getJMSTopics(), true);
            }

            this.computeDiff("MaximumMessageSize", this.bean.getMaximumMessageSize(), var2.getMaximumMessageSize(), true);
            this.computeDiff("MessageBufferSize", this.bean.getMessageBufferSize(), var2.getMessageBufferSize(), true);
            this.computeDiff("MessagesMaximum", this.bean.getMessagesMaximum(), var2.getMessagesMaximum(), true);
            this.computeDiff("MessagesThresholdHigh", this.bean.getMessagesThresholdHigh(), var2.getMessagesThresholdHigh(), true);
            this.computeDiff("MessagesThresholdLow", this.bean.getMessagesThresholdLow(), var2.getMessagesThresholdLow(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PagingBlockSize", this.bean.getPagingBlockSize(), var2.getPagingBlockSize(), false);
            this.computeDiff("PagingDirectory", this.bean.getPagingDirectory(), var2.getPagingDirectory(), false);
            this.computeDiff("PagingIoBufferSize", this.bean.getPagingIoBufferSize(), var2.getPagingIoBufferSize(), false);
            this.computeDiff("PagingMaxFileSize", this.bean.getPagingMaxFileSize(), var2.getPagingMaxFileSize(), false);
            this.computeDiff("PagingMaxWindowBufferSize", this.bean.getPagingMaxWindowBufferSize(), var2.getPagingMaxWindowBufferSize(), false);
            this.computeDiff("PagingMinWindowBufferSize", this.bean.getPagingMinWindowBufferSize(), var2.getPagingMinWindowBufferSize(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PagingStore", this.bean.getPagingStore(), var2.getPagingStore(), false);
            }

            this.computeDiff("PersistentStore", this.bean.getPersistentStore(), var2.getPersistentStore(), false);
            this.computeDiff("ProductionPausedAtStartup", this.bean.getProductionPausedAtStartup(), var2.getProductionPausedAtStartup(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Store", this.bean.getStore(), var2.getStore(), false);
            }

            this.computeDiff("StoreEnabled", this.bean.getStoreEnabled(), var2.getStoreEnabled(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TemporaryTemplate", this.bean.getTemporaryTemplate(), var2.getTemporaryTemplate(), false);
            }

            this.computeDiff("TemporaryTemplateName", this.bean.getTemporaryTemplateName(), var2.getTemporaryTemplateName(), true);
            this.computeDiff("TemporaryTemplateResource", this.bean.getTemporaryTemplateResource(), var2.getTemporaryTemplateResource(), true);
            this.computeDiff("AllowsPersistentDowngrade", this.bean.isAllowsPersistentDowngrade(), var2.isAllowsPersistentDowngrade(), true);
            this.computeDiff("BytesPagingEnabled", this.bean.isBytesPagingEnabled(), var2.isBytesPagingEnabled(), false);
            this.computeDiff("HostingTemporaryDestinations", this.bean.isHostingTemporaryDestinations(), var2.isHostingTemporaryDestinations(), true);
            this.computeDiff("JDBCStoreUpgradeEnabled", this.bean.isJDBCStoreUpgradeEnabled(), var2.isJDBCStoreUpgradeEnabled(), false);
            this.computeDiff("MessagesPagingEnabled", this.bean.isMessagesPagingEnabled(), var2.isMessagesPagingEnabled(), false);
            this.computeDiff("PagingFileLockingEnabled", this.bean.isPagingFileLockingEnabled(), var2.isPagingFileLockingEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSServerMBeanImpl var3 = (JMSServerMBeanImpl)var1.getSourceBean();
            JMSServerMBeanImpl var4 = (JMSServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BlockingSendPolicy")) {
                  var3.setBlockingSendPolicy(var4.getBlockingSendPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 41);
               } else if (var5.equals("BytesMaximum")) {
                  var3.setBytesMaximum(var4.getBytesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("BytesThresholdHigh")) {
                  var3.setBytesThresholdHigh(var4.getBytesThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("BytesThresholdLow")) {
                  var3.setBytesThresholdLow(var4.getBytesThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("ConsumptionPausedAtStartup")) {
                  var3.setConsumptionPausedAtStartup(var4.getConsumptionPausedAtStartup());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 44);
               } else if (!var5.equals("Destinations")) {
                  if (var5.equals("ExpirationScanInterval")) {
                     var3.setExpirationScanInterval(var4.getExpirationScanInterval());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                  } else if (var5.equals("InsertionPausedAtStartup")) {
                     var3.setInsertionPausedAtStartup(var4.getInsertionPausedAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 43);
                  } else if (var5.equals("JMSMessageLogFile")) {
                     if (var6 == 2) {
                        var3.setJMSMessageLogFile((JMSMessageLogFileMBean)this.createCopy((AbstractDescriptorBean)var4.getJMSMessageLogFile()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("JMSMessageLogFile", var3.getJMSMessageLogFile());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 47);
                  } else if (var5.equals("JMSQueues")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addJMSQueue((JMSQueueMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeJMSQueue((JMSQueueMBean)var2.getRemovedObject());
                     }

                     if (var3.getJMSQueues() == null || var3.getJMSQueues().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                     }
                  } else if (var5.equals("JMSSessionPools")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addJMSSessionPool((JMSSessionPoolMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeJMSSessionPool((JMSSessionPoolMBean)var2.getRemovedObject());
                     }

                     if (var3.getJMSSessionPools() == null || var3.getJMSSessionPools().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     }
                  } else if (var5.equals("JMSTopics")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addJMSTopic((JMSTopicMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeJMSTopic((JMSTopicMBean)var2.getRemovedObject());
                     }

                     if (var3.getJMSTopics() == null || var3.getJMSTopics().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                     }
                  } else if (var5.equals("MaximumMessageSize")) {
                     var3.setMaximumMessageSize(var4.getMaximumMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                  } else if (var5.equals("MessageBufferSize")) {
                     var3.setMessageBufferSize(var4.getMessageBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (var5.equals("MessagesMaximum")) {
                     var3.setMessagesMaximum(var4.getMessagesMaximum());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (var5.equals("MessagesThresholdHigh")) {
                     var3.setMessagesThresholdHigh(var4.getMessagesThresholdHigh());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (var5.equals("MessagesThresholdLow")) {
                     var3.setMessagesThresholdLow(var4.getMessagesThresholdLow());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("PagingBlockSize")) {
                     var3.setPagingBlockSize(var4.getPagingBlockSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                  } else if (var5.equals("PagingDirectory")) {
                     var3.setPagingDirectory(var4.getPagingDirectory());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (var5.equals("PagingIoBufferSize")) {
                     var3.setPagingIoBufferSize(var4.getPagingIoBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                  } else if (var5.equals("PagingMaxFileSize")) {
                     var3.setPagingMaxFileSize(var4.getPagingMaxFileSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                  } else if (var5.equals("PagingMaxWindowBufferSize")) {
                     var3.setPagingMaxWindowBufferSize(var4.getPagingMaxWindowBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                  } else if (var5.equals("PagingMinWindowBufferSize")) {
                     var3.setPagingMinWindowBufferSize(var4.getPagingMinWindowBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                  } else if (var5.equals("PagingStore")) {
                     var3.setPagingStoreAsString(var4.getPagingStoreAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("PersistentStore")) {
                     var3.setPersistentStoreAsString(var4.getPersistentStoreAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("ProductionPausedAtStartup")) {
                     var3.setProductionPausedAtStartup(var4.getProductionPausedAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                  } else if (!var5.equals("ServerNames") && !var5.equals("SessionPools")) {
                     if (var5.equals("Store")) {
                        var3.setStoreAsString(var4.getStoreAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                     } else if (var5.equals("StoreEnabled")) {
                        var3.setStoreEnabled(var4.getStoreEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                     } else if (var5.equals("Targets")) {
                        var3.setTargetsAsString(var4.getTargetsAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                     } else if (var5.equals("TemporaryTemplate")) {
                        var3.setTemporaryTemplateAsString(var4.getTemporaryTemplateAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                     } else if (var5.equals("TemporaryTemplateName")) {
                        var3.setTemporaryTemplateName(var4.getTemporaryTemplateName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                     } else if (var5.equals("TemporaryTemplateResource")) {
                        var3.setTemporaryTemplateResource(var4.getTemporaryTemplateResource());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                     } else if (var5.equals("AllowsPersistentDowngrade")) {
                        var3.setAllowsPersistentDowngrade(var4.isAllowsPersistentDowngrade());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                     } else if (var5.equals("BytesPagingEnabled")) {
                        var3.setBytesPagingEnabled(var4.isBytesPagingEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                     } else if (var5.equals("HostingTemporaryDestinations")) {
                        var3.setHostingTemporaryDestinations(var4.isHostingTemporaryDestinations());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("JDBCStoreUpgradeEnabled")) {
                        var3.setJDBCStoreUpgradeEnabled(var4.isJDBCStoreUpgradeEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                     } else if (var5.equals("MessagesPagingEnabled")) {
                        var3.setMessagesPagingEnabled(var4.isMessagesPagingEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                     } else if (var5.equals("PagingFileLockingEnabled")) {
                        var3.setPagingFileLockingEnabled(var4.isPagingFileLockingEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
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
            JMSServerMBeanImpl var5 = (JMSServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BlockingSendPolicy")) && this.bean.isBlockingSendPolicySet()) {
               var5.setBlockingSendPolicy(this.bean.getBlockingSendPolicy());
            }

            if ((var3 == null || !var3.contains("BytesMaximum")) && this.bean.isBytesMaximumSet()) {
               var5.setBytesMaximum(this.bean.getBytesMaximum());
            }

            if ((var3 == null || !var3.contains("BytesThresholdHigh")) && this.bean.isBytesThresholdHighSet()) {
               var5.setBytesThresholdHigh(this.bean.getBytesThresholdHigh());
            }

            if ((var3 == null || !var3.contains("BytesThresholdLow")) && this.bean.isBytesThresholdLowSet()) {
               var5.setBytesThresholdLow(this.bean.getBytesThresholdLow());
            }

            if ((var3 == null || !var3.contains("ConsumptionPausedAtStartup")) && this.bean.isConsumptionPausedAtStartupSet()) {
               var5.setConsumptionPausedAtStartup(this.bean.getConsumptionPausedAtStartup());
            }

            if ((var3 == null || !var3.contains("ExpirationScanInterval")) && this.bean.isExpirationScanIntervalSet()) {
               var5.setExpirationScanInterval(this.bean.getExpirationScanInterval());
            }

            if ((var3 == null || !var3.contains("InsertionPausedAtStartup")) && this.bean.isInsertionPausedAtStartupSet()) {
               var5.setInsertionPausedAtStartup(this.bean.getInsertionPausedAtStartup());
            }

            if ((var3 == null || !var3.contains("JMSMessageLogFile")) && this.bean.isJMSMessageLogFileSet() && !var5._isSet(47)) {
               JMSMessageLogFileMBean var4 = this.bean.getJMSMessageLogFile();
               var5.setJMSMessageLogFile((JMSMessageLogFileMBean)null);
               var5.setJMSMessageLogFile(var4 == null ? null : (JMSMessageLogFileMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            int var8;
            if (var2 && (var3 == null || !var3.contains("JMSQueues")) && this.bean.isJMSQueuesSet() && !var5._isSet(45)) {
               JMSQueueMBean[] var6 = this.bean.getJMSQueues();
               JMSQueueMBean[] var7 = new JMSQueueMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (JMSQueueMBean)((JMSQueueMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setJMSQueues(var7);
            }

            if ((var3 == null || !var3.contains("JMSSessionPools")) && this.bean.isJMSSessionPoolsSet() && !var5._isSet(11)) {
               JMSSessionPoolMBean[] var11 = this.bean.getJMSSessionPools();
               JMSSessionPoolMBean[] var13 = new JMSSessionPoolMBean[var11.length];

               for(var8 = 0; var8 < var13.length; ++var8) {
                  var13[var8] = (JMSSessionPoolMBean)((JMSSessionPoolMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setJMSSessionPools(var13);
            }

            if (var2 && (var3 == null || !var3.contains("JMSTopics")) && this.bean.isJMSTopicsSet() && !var5._isSet(46)) {
               JMSTopicMBean[] var12 = this.bean.getJMSTopics();
               JMSTopicMBean[] var14 = new JMSTopicMBean[var12.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (JMSTopicMBean)((JMSTopicMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setJMSTopics(var14);
            }

            if ((var3 == null || !var3.contains("MaximumMessageSize")) && this.bean.isMaximumMessageSizeSet()) {
               var5.setMaximumMessageSize(this.bean.getMaximumMessageSize());
            }

            if ((var3 == null || !var3.contains("MessageBufferSize")) && this.bean.isMessageBufferSizeSet()) {
               var5.setMessageBufferSize(this.bean.getMessageBufferSize());
            }

            if ((var3 == null || !var3.contains("MessagesMaximum")) && this.bean.isMessagesMaximumSet()) {
               var5.setMessagesMaximum(this.bean.getMessagesMaximum());
            }

            if ((var3 == null || !var3.contains("MessagesThresholdHigh")) && this.bean.isMessagesThresholdHighSet()) {
               var5.setMessagesThresholdHigh(this.bean.getMessagesThresholdHigh());
            }

            if ((var3 == null || !var3.contains("MessagesThresholdLow")) && this.bean.isMessagesThresholdLowSet()) {
               var5.setMessagesThresholdLow(this.bean.getMessagesThresholdLow());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PagingBlockSize")) && this.bean.isPagingBlockSizeSet()) {
               var5.setPagingBlockSize(this.bean.getPagingBlockSize());
            }

            if ((var3 == null || !var3.contains("PagingDirectory")) && this.bean.isPagingDirectorySet()) {
               var5.setPagingDirectory(this.bean.getPagingDirectory());
            }

            if ((var3 == null || !var3.contains("PagingIoBufferSize")) && this.bean.isPagingIoBufferSizeSet()) {
               var5.setPagingIoBufferSize(this.bean.getPagingIoBufferSize());
            }

            if ((var3 == null || !var3.contains("PagingMaxFileSize")) && this.bean.isPagingMaxFileSizeSet()) {
               var5.setPagingMaxFileSize(this.bean.getPagingMaxFileSize());
            }

            if ((var3 == null || !var3.contains("PagingMaxWindowBufferSize")) && this.bean.isPagingMaxWindowBufferSizeSet()) {
               var5.setPagingMaxWindowBufferSize(this.bean.getPagingMaxWindowBufferSize());
            }

            if ((var3 == null || !var3.contains("PagingMinWindowBufferSize")) && this.bean.isPagingMinWindowBufferSizeSet()) {
               var5.setPagingMinWindowBufferSize(this.bean.getPagingMinWindowBufferSize());
            }

            if (var2 && (var3 == null || !var3.contains("PagingStore")) && this.bean.isPagingStoreSet()) {
               var5._unSet(var5, 28);
               var5.setPagingStoreAsString(this.bean.getPagingStoreAsString());
            }

            if ((var3 == null || !var3.contains("PersistentStore")) && this.bean.isPersistentStoreSet()) {
               var5._unSet(var5, 14);
               var5.setPersistentStoreAsString(this.bean.getPersistentStoreAsString());
            }

            if ((var3 == null || !var3.contains("ProductionPausedAtStartup")) && this.bean.isProductionPausedAtStartupSet()) {
               var5.setProductionPausedAtStartup(this.bean.getProductionPausedAtStartup());
            }

            if (var2 && (var3 == null || !var3.contains("Store")) && this.bean.isStoreSet()) {
               var5._unSet(var5, 13);
               var5.setStoreAsString(this.bean.getStoreAsString());
            }

            if ((var3 == null || !var3.contains("StoreEnabled")) && this.bean.isStoreEnabledSet()) {
               var5.setStoreEnabled(this.bean.getStoreEnabled());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("TemporaryTemplate")) && this.bean.isTemporaryTemplateSet()) {
               var5._unSet(var5, 17);
               var5.setTemporaryTemplateAsString(this.bean.getTemporaryTemplateAsString());
            }

            if ((var3 == null || !var3.contains("TemporaryTemplateName")) && this.bean.isTemporaryTemplateNameSet()) {
               var5.setTemporaryTemplateName(this.bean.getTemporaryTemplateName());
            }

            if ((var3 == null || !var3.contains("TemporaryTemplateResource")) && this.bean.isTemporaryTemplateResourceSet()) {
               var5.setTemporaryTemplateResource(this.bean.getTemporaryTemplateResource());
            }

            if ((var3 == null || !var3.contains("AllowsPersistentDowngrade")) && this.bean.isAllowsPersistentDowngradeSet()) {
               var5.setAllowsPersistentDowngrade(this.bean.isAllowsPersistentDowngrade());
            }

            if ((var3 == null || !var3.contains("BytesPagingEnabled")) && this.bean.isBytesPagingEnabledSet()) {
               var5.setBytesPagingEnabled(this.bean.isBytesPagingEnabled());
            }

            if ((var3 == null || !var3.contains("HostingTemporaryDestinations")) && this.bean.isHostingTemporaryDestinationsSet()) {
               var5.setHostingTemporaryDestinations(this.bean.isHostingTemporaryDestinations());
            }

            if ((var3 == null || !var3.contains("JDBCStoreUpgradeEnabled")) && this.bean.isJDBCStoreUpgradeEnabledSet()) {
               var5.setJDBCStoreUpgradeEnabled(this.bean.isJDBCStoreUpgradeEnabled());
            }

            if ((var3 == null || !var3.contains("MessagesPagingEnabled")) && this.bean.isMessagesPagingEnabledSet()) {
               var5.setMessagesPagingEnabled(this.bean.isMessagesPagingEnabled());
            }

            if ((var3 == null || !var3.contains("PagingFileLockingEnabled")) && this.bean.isPagingFileLockingEnabledSet()) {
               var5.setPagingFileLockingEnabled(this.bean.isPagingFileLockingEnabled());
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
         this.inferSubTree(this.bean.getDestinations(), var1, var2);
         this.inferSubTree(this.bean.getJMSMessageLogFile(), var1, var2);
         this.inferSubTree(this.bean.getJMSQueues(), var1, var2);
         this.inferSubTree(this.bean.getJMSSessionPools(), var1, var2);
         this.inferSubTree(this.bean.getJMSTopics(), var1, var2);
         this.inferSubTree(this.bean.getPagingStore(), var1, var2);
         this.inferSubTree(this.bean.getPersistentStore(), var1, var2);
         this.inferSubTree(this.bean.getSessionPools(), var1, var2);
         this.inferSubTree(this.bean.getStore(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
         this.inferSubTree(this.bean.getTemporaryTemplate(), var1, var2);
      }
   }
}
