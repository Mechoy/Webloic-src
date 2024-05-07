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
import weblogic.management.mbeans.custom.SAFAgent;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class SAFAgentMBeanImpl extends DeploymentMBeanImpl implements SAFAgentMBean, Serializable {
   private long _AcknowledgeInterval;
   private long _BytesMaximum;
   private long _BytesThresholdHigh;
   private long _BytesThresholdLow;
   private long _ConversationIdleTimeMaximum;
   private long _DefaultRetryDelayBase;
   private long _DefaultRetryDelayMaximum;
   private double _DefaultRetryDelayMultiplier;
   private long _DefaultTimeToLive;
   private boolean _ForwardingPausedAtStartup;
   private boolean _IncomingPausedAtStartup;
   private JMSSAFMessageLogFileMBean _JMSSAFMessageLogFile;
   private boolean _LoggingEnabled;
   private int _MaximumMessageSize;
   private long _MessageBufferSize;
   private long _MessagesMaximum;
   private long _MessagesThresholdHigh;
   private long _MessagesThresholdLow;
   private String _Name;
   private String _PagingDirectory;
   private boolean _ReceivingPausedAtStartup;
   private Set _ServerNames;
   private String _ServiceType;
   private PersistentStoreMBean _Store;
   private TargetMBean[] _Targets;
   private long _WindowInterval;
   private int _WindowSize;
   private SAFAgent _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SAFAgentMBeanImpl() {
      try {
         this._customizer = new SAFAgent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SAFAgentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new SAFAgent(this);
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
                        SAFAgentMBeanImpl.this.addTarget((TargetMBean)var1);
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
      JMSLegalHelper.validateSAFAgentTargets(var1);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return SAFAgentMBeanImpl.this.getTargets();
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

   public PersistentStoreMBean getStore() {
      return this._Store;
   }

   public String getStoreAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getStore();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isStoreSet() {
      return this._isSet(10);
   }

   public void setStoreAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, PersistentStoreMBean.class, new ReferenceManager.Resolver(this, 10) {
            public void resolveReference(Object var1) {
               try {
                  SAFAgentMBeanImpl.this.setStore((PersistentStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         PersistentStoreMBean var2 = this._Store;
         this._initializeProperty(10);
         this._postSet(10, var2, this._Store);
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

   public void setStore(PersistentStoreMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 10, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SAFAgentMBeanImpl.this.getStore();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      PersistentStoreMBean var3 = this._Store;
      this._Store = var1;
      this._postSet(10, var3, var1);
   }

   public long getBytesMaximum() {
      return this._BytesMaximum;
   }

   public boolean isBytesMaximumSet() {
      return this._isSet(11);
   }

   public void setBytesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BytesMaximum", var1, -1L);
      long var3 = this._BytesMaximum;
      this._BytesMaximum = var1;
      this._postSet(11, var3, var1);
   }

   public long getBytesThresholdHigh() {
      return this._BytesThresholdHigh;
   }

   public boolean isBytesThresholdHighSet() {
      return this._isSet(12);
   }

   public void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BytesThresholdHigh", var1, -1L);
      long var3 = this._BytesThresholdHigh;
      this._BytesThresholdHigh = var1;
      this._postSet(12, var3, var1);
   }

   public long getBytesThresholdLow() {
      return this._BytesThresholdLow;
   }

   public boolean isBytesThresholdLowSet() {
      return this._isSet(13);
   }

   public void setBytesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BytesThresholdLow", var1, -1L);
      long var3 = this._BytesThresholdLow;
      this._BytesThresholdLow = var1;
      this._postSet(13, var3, var1);
   }

   public long getMessagesMaximum() {
      return this._MessagesMaximum;
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(14);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setMessagesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MessagesMaximum", var1, -1L);
      long var3 = this._MessagesMaximum;
      this._MessagesMaximum = var1;
      this._postSet(14, var3, var1);
   }

   public long getMessagesThresholdHigh() {
      return this._MessagesThresholdHigh;
   }

   public boolean isMessagesThresholdHighSet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MessagesThresholdHigh", var1, -1L);
      long var3 = this._MessagesThresholdHigh;
      this._MessagesThresholdHigh = var1;
      this._postSet(15, var3, var1);
   }

   public long getMessagesThresholdLow() {
      return this._MessagesThresholdLow;
   }

   public boolean isMessagesThresholdLowSet() {
      return this._isSet(16);
   }

   public void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MessagesThresholdLow", var1, -1L);
      long var3 = this._MessagesThresholdLow;
      this._MessagesThresholdLow = var1;
      this._postSet(16, var3, var1);
   }

   public int getMaximumMessageSize() {
      return this._MaximumMessageSize;
   }

   public boolean isMaximumMessageSizeSet() {
      return this._isSet(17);
   }

   public void setMaximumMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaximumMessageSize", var1, 0);
      int var2 = this._MaximumMessageSize;
      this._MaximumMessageSize = var1;
      this._postSet(17, var2, var1);
   }

   public long getDefaultRetryDelayBase() {
      return this._DefaultRetryDelayBase;
   }

   public boolean isDefaultRetryDelayBaseSet() {
      return this._isSet(18);
   }

   public void setDefaultRetryDelayBase(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DefaultRetryDelayBase", var1, 1L);
      long var3 = this._DefaultRetryDelayBase;
      this._DefaultRetryDelayBase = var1;
      this._postSet(18, var3, var1);
   }

   public long getDefaultRetryDelayMaximum() {
      return this._DefaultRetryDelayMaximum;
   }

   public boolean isDefaultRetryDelayMaximumSet() {
      return this._isSet(19);
   }

   public void setDefaultRetryDelayMaximum(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DefaultRetryDelayMaximum", var1, 1L);
      long var3 = this._DefaultRetryDelayMaximum;
      this._DefaultRetryDelayMaximum = var1;
      this._postSet(19, var3, var1);
   }

   public double getDefaultRetryDelayMultiplier() {
      return this._DefaultRetryDelayMultiplier;
   }

   public boolean isDefaultRetryDelayMultiplierSet() {
      return this._isSet(20);
   }

   public void setDefaultRetryDelayMultiplier(double var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DefaultRetryDelayMultiplier", var1, 1.0);
      double var3 = this._DefaultRetryDelayMultiplier;
      this._DefaultRetryDelayMultiplier = var1;
      this._postSet(20, var3, var1);
   }

   public String getServiceType() {
      return this._ServiceType;
   }

   public boolean isServiceTypeSet() {
      return this._isSet(21);
   }

   public void setServiceType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Both", "Sending-only", "Receiving-only"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ServiceType", var1, var2);
      String var3 = this._ServiceType;
      this._ServiceType = var1;
      this._postSet(21, var3, var1);
   }

   public int getWindowSize() {
      return this._WindowSize;
   }

   public boolean isWindowSizeSet() {
      return this._isSet(22);
   }

   public void setWindowSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("WindowSize", var1, 1);
      int var2 = this._WindowSize;
      this._WindowSize = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isLoggingEnabled() {
      return this._LoggingEnabled;
   }

   public boolean isLoggingEnabledSet() {
      return this._isSet(23);
   }

   public void setLoggingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._LoggingEnabled;
      this._LoggingEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public long getConversationIdleTimeMaximum() {
      return this._ConversationIdleTimeMaximum;
   }

   public boolean isConversationIdleTimeMaximumSet() {
      return this._isSet(24);
   }

   public void setConversationIdleTimeMaximum(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ConversationIdleTimeMaximum", var1, 0L);
      long var3 = this._ConversationIdleTimeMaximum;
      this._ConversationIdleTimeMaximum = var1;
      this._postSet(24, var3, var1);
   }

   public long getAcknowledgeInterval() {
      return this._AcknowledgeInterval;
   }

   public boolean isAcknowledgeIntervalSet() {
      return this._isSet(25);
   }

   public void setAcknowledgeInterval(long var1) throws InvalidAttributeValueException {
      JMSLegalHelper.validateAcknowledgeIntervalValue(var1);
      long var3 = this._AcknowledgeInterval;
      this._AcknowledgeInterval = var1;
      this._postSet(25, var3, var1);
   }

   public long getDefaultTimeToLive() {
      return this._DefaultTimeToLive;
   }

   public boolean isDefaultTimeToLiveSet() {
      return this._isSet(26);
   }

   public void setDefaultTimeToLive(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DefaultTimeToLive", var1, 0L);
      long var3 = this._DefaultTimeToLive;
      this._DefaultTimeToLive = var1;
      this._postSet(26, var3, var1);
   }

   public boolean isIncomingPausedAtStartup() {
      return this._IncomingPausedAtStartup;
   }

   public boolean isIncomingPausedAtStartupSet() {
      return this._isSet(27);
   }

   public void setIncomingPausedAtStartup(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._IncomingPausedAtStartup;
      this._IncomingPausedAtStartup = var1;
      this._postSet(27, var2, var1);
   }

   public boolean isForwardingPausedAtStartup() {
      return this._ForwardingPausedAtStartup;
   }

   public boolean isForwardingPausedAtStartupSet() {
      return this._isSet(28);
   }

   public void setForwardingPausedAtStartup(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ForwardingPausedAtStartup;
      this._ForwardingPausedAtStartup = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isReceivingPausedAtStartup() {
      return this._ReceivingPausedAtStartup;
   }

   public boolean isReceivingPausedAtStartupSet() {
      return this._isSet(29);
   }

   public void setReceivingPausedAtStartup(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ReceivingPausedAtStartup;
      this._ReceivingPausedAtStartup = var1;
      this._postSet(29, var2, var1);
   }

   public long getMessageBufferSize() {
      return this._MessageBufferSize;
   }

   public boolean isMessageBufferSizeSet() {
      return this._isSet(30);
   }

   public void setMessageBufferSize(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MessageBufferSize", var1, -1L);
      long var3 = this._MessageBufferSize;
      this._MessageBufferSize = var1;
      this._postSet(30, var3, var1);
   }

   public String getPagingDirectory() {
      return this._PagingDirectory;
   }

   public boolean isPagingDirectorySet() {
      return this._isSet(31);
   }

   public void setPagingDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PagingDirectory;
      this._PagingDirectory = var1;
      this._postSet(31, var2, var1);
   }

   public long getWindowInterval() {
      return this._WindowInterval;
   }

   public boolean isWindowIntervalSet() {
      return this._isSet(32);
   }

   public void setWindowInterval(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("WindowInterval", var1, 0L);
      long var3 = this._WindowInterval;
      this._WindowInterval = var1;
      this._postSet(32, var3, var1);
   }

   public JMSSAFMessageLogFileMBean getJMSSAFMessageLogFile() {
      return this._JMSSAFMessageLogFile;
   }

   public boolean isJMSSAFMessageLogFileSet() {
      return this._isSet(33) || this._isAnythingSet((AbstractDescriptorBean)this.getJMSSAFMessageLogFile());
   }

   public void setJMSSAFMessageLogFile(JMSSAFMessageLogFileMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 33)) {
         this._postCreate(var2);
      }

      JMSSAFMessageLogFileMBean var3 = this._JMSSAFMessageLogFile;
      this._JMSSAFMessageLogFile = var1;
      this._postSet(33, var3, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      JMSLegalHelper.validateSAFAgent(this);
      JMSLegalHelper.validateServerBytesValues((SAFAgentMBean)this);
      JMSLegalHelper.validateRetryBaseAndMax(this);
      JMSLegalHelper.validateServerMessagesValues((SAFAgentMBean)this);
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
      return super._isAnythingSet() || this.isJMSSAFMessageLogFileSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 25;
      }

      try {
         switch (var1) {
            case 25:
               this._AcknowledgeInterval = -1L;
               if (var2) {
                  break;
               }
            case 11:
               this._BytesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 12:
               this._BytesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 13:
               this._BytesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 24:
               this._ConversationIdleTimeMaximum = 0L;
               if (var2) {
                  break;
               }
            case 18:
               this._DefaultRetryDelayBase = 20000L;
               if (var2) {
                  break;
               }
            case 19:
               this._DefaultRetryDelayMaximum = 180000L;
               if (var2) {
                  break;
               }
            case 20:
               this._DefaultRetryDelayMultiplier = 1.0;
               if (var2) {
                  break;
               }
            case 26:
               this._DefaultTimeToLive = 0L;
               if (var2) {
                  break;
               }
            case 33:
               this._JMSSAFMessageLogFile = new JMSSAFMessageLogFileMBeanImpl(this, 33);
               this._postCreate((AbstractDescriptorBean)this._JMSSAFMessageLogFile);
               if (var2) {
                  break;
               }
            case 17:
               this._MaximumMessageSize = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 30:
               this._MessageBufferSize = -1L;
               if (var2) {
                  break;
               }
            case 14:
               this._MessagesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 15:
               this._MessagesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 16:
               this._MessagesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 31:
               this._PagingDirectory = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 21:
               this._ServiceType = "Both";
               if (var2) {
                  break;
               }
            case 10:
               this._Store = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 32:
               this._WindowInterval = 0L;
               if (var2) {
                  break;
               }
            case 22:
               this._WindowSize = 10;
               if (var2) {
                  break;
               }
            case 28:
               this._ForwardingPausedAtStartup = false;
               if (var2) {
                  break;
               }
            case 27:
               this._IncomingPausedAtStartup = false;
               if (var2) {
                  break;
               }
            case 23:
               this._LoggingEnabled = true;
               if (var2) {
                  break;
               }
            case 29:
               this._ReceivingPausedAtStartup = false;
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
      return "SAFAgent";
   }

   public void putValue(String var1, Object var2) {
      long var6;
      if (var1.equals("AcknowledgeInterval")) {
         var6 = this._AcknowledgeInterval;
         this._AcknowledgeInterval = (Long)var2;
         this._postSet(25, var6, this._AcknowledgeInterval);
      } else if (var1.equals("BytesMaximum")) {
         var6 = this._BytesMaximum;
         this._BytesMaximum = (Long)var2;
         this._postSet(11, var6, this._BytesMaximum);
      } else if (var1.equals("BytesThresholdHigh")) {
         var6 = this._BytesThresholdHigh;
         this._BytesThresholdHigh = (Long)var2;
         this._postSet(12, var6, this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         var6 = this._BytesThresholdLow;
         this._BytesThresholdLow = (Long)var2;
         this._postSet(13, var6, this._BytesThresholdLow);
      } else if (var1.equals("ConversationIdleTimeMaximum")) {
         var6 = this._ConversationIdleTimeMaximum;
         this._ConversationIdleTimeMaximum = (Long)var2;
         this._postSet(24, var6, this._ConversationIdleTimeMaximum);
      } else if (var1.equals("DefaultRetryDelayBase")) {
         var6 = this._DefaultRetryDelayBase;
         this._DefaultRetryDelayBase = (Long)var2;
         this._postSet(18, var6, this._DefaultRetryDelayBase);
      } else if (var1.equals("DefaultRetryDelayMaximum")) {
         var6 = this._DefaultRetryDelayMaximum;
         this._DefaultRetryDelayMaximum = (Long)var2;
         this._postSet(19, var6, this._DefaultRetryDelayMaximum);
      } else if (var1.equals("DefaultRetryDelayMultiplier")) {
         double var13 = this._DefaultRetryDelayMultiplier;
         this._DefaultRetryDelayMultiplier = (Double)var2;
         this._postSet(20, var13, this._DefaultRetryDelayMultiplier);
      } else if (var1.equals("DefaultTimeToLive")) {
         var6 = this._DefaultTimeToLive;
         this._DefaultTimeToLive = (Long)var2;
         this._postSet(26, var6, this._DefaultTimeToLive);
      } else {
         boolean var11;
         if (var1.equals("ForwardingPausedAtStartup")) {
            var11 = this._ForwardingPausedAtStartup;
            this._ForwardingPausedAtStartup = (Boolean)var2;
            this._postSet(28, var11, this._ForwardingPausedAtStartup);
         } else if (var1.equals("IncomingPausedAtStartup")) {
            var11 = this._IncomingPausedAtStartup;
            this._IncomingPausedAtStartup = (Boolean)var2;
            this._postSet(27, var11, this._IncomingPausedAtStartup);
         } else if (var1.equals("JMSSAFMessageLogFile")) {
            JMSSAFMessageLogFileMBean var12 = this._JMSSAFMessageLogFile;
            this._JMSSAFMessageLogFile = (JMSSAFMessageLogFileMBean)var2;
            this._postSet(33, var12, this._JMSSAFMessageLogFile);
         } else if (var1.equals("LoggingEnabled")) {
            var11 = this._LoggingEnabled;
            this._LoggingEnabled = (Boolean)var2;
            this._postSet(23, var11, this._LoggingEnabled);
         } else {
            int var5;
            if (var1.equals("MaximumMessageSize")) {
               var5 = this._MaximumMessageSize;
               this._MaximumMessageSize = (Integer)var2;
               this._postSet(17, var5, this._MaximumMessageSize);
            } else if (var1.equals("MessageBufferSize")) {
               var6 = this._MessageBufferSize;
               this._MessageBufferSize = (Long)var2;
               this._postSet(30, var6, this._MessageBufferSize);
            } else if (var1.equals("MessagesMaximum")) {
               var6 = this._MessagesMaximum;
               this._MessagesMaximum = (Long)var2;
               this._postSet(14, var6, this._MessagesMaximum);
            } else if (var1.equals("MessagesThresholdHigh")) {
               var6 = this._MessagesThresholdHigh;
               this._MessagesThresholdHigh = (Long)var2;
               this._postSet(15, var6, this._MessagesThresholdHigh);
            } else if (var1.equals("MessagesThresholdLow")) {
               var6 = this._MessagesThresholdLow;
               this._MessagesThresholdLow = (Long)var2;
               this._postSet(16, var6, this._MessagesThresholdLow);
            } else {
               String var9;
               if (var1.equals("Name")) {
                  var9 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var9, this._Name);
               } else if (var1.equals("PagingDirectory")) {
                  var9 = this._PagingDirectory;
                  this._PagingDirectory = (String)var2;
                  this._postSet(31, var9, this._PagingDirectory);
               } else if (var1.equals("ReceivingPausedAtStartup")) {
                  var11 = this._ReceivingPausedAtStartup;
                  this._ReceivingPausedAtStartup = (Boolean)var2;
                  this._postSet(29, var11, this._ReceivingPausedAtStartup);
               } else if (var1.equals("ServerNames")) {
                  Set var10 = this._ServerNames;
                  this._ServerNames = (Set)var2;
                  this._postSet(9, var10, this._ServerNames);
               } else if (var1.equals("ServiceType")) {
                  var9 = this._ServiceType;
                  this._ServiceType = (String)var2;
                  this._postSet(21, var9, this._ServiceType);
               } else if (var1.equals("Store")) {
                  PersistentStoreMBean var8 = this._Store;
                  this._Store = (PersistentStoreMBean)var2;
                  this._postSet(10, var8, this._Store);
               } else if (var1.equals("Targets")) {
                  TargetMBean[] var7 = this._Targets;
                  this._Targets = (TargetMBean[])((TargetMBean[])var2);
                  this._postSet(7, var7, this._Targets);
               } else if (var1.equals("WindowInterval")) {
                  var6 = this._WindowInterval;
                  this._WindowInterval = (Long)var2;
                  this._postSet(32, var6, this._WindowInterval);
               } else if (var1.equals("WindowSize")) {
                  var5 = this._WindowSize;
                  this._WindowSize = (Integer)var2;
                  this._postSet(22, var5, this._WindowSize);
               } else if (var1.equals("customizer")) {
                  SAFAgent var3 = this._customizer;
                  this._customizer = (SAFAgent)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcknowledgeInterval")) {
         return new Long(this._AcknowledgeInterval);
      } else if (var1.equals("BytesMaximum")) {
         return new Long(this._BytesMaximum);
      } else if (var1.equals("BytesThresholdHigh")) {
         return new Long(this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         return new Long(this._BytesThresholdLow);
      } else if (var1.equals("ConversationIdleTimeMaximum")) {
         return new Long(this._ConversationIdleTimeMaximum);
      } else if (var1.equals("DefaultRetryDelayBase")) {
         return new Long(this._DefaultRetryDelayBase);
      } else if (var1.equals("DefaultRetryDelayMaximum")) {
         return new Long(this._DefaultRetryDelayMaximum);
      } else if (var1.equals("DefaultRetryDelayMultiplier")) {
         return new Double(this._DefaultRetryDelayMultiplier);
      } else if (var1.equals("DefaultTimeToLive")) {
         return new Long(this._DefaultTimeToLive);
      } else if (var1.equals("ForwardingPausedAtStartup")) {
         return new Boolean(this._ForwardingPausedAtStartup);
      } else if (var1.equals("IncomingPausedAtStartup")) {
         return new Boolean(this._IncomingPausedAtStartup);
      } else if (var1.equals("JMSSAFMessageLogFile")) {
         return this._JMSSAFMessageLogFile;
      } else if (var1.equals("LoggingEnabled")) {
         return new Boolean(this._LoggingEnabled);
      } else if (var1.equals("MaximumMessageSize")) {
         return new Integer(this._MaximumMessageSize);
      } else if (var1.equals("MessageBufferSize")) {
         return new Long(this._MessageBufferSize);
      } else if (var1.equals("MessagesMaximum")) {
         return new Long(this._MessagesMaximum);
      } else if (var1.equals("MessagesThresholdHigh")) {
         return new Long(this._MessagesThresholdHigh);
      } else if (var1.equals("MessagesThresholdLow")) {
         return new Long(this._MessagesThresholdLow);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PagingDirectory")) {
         return this._PagingDirectory;
      } else if (var1.equals("ReceivingPausedAtStartup")) {
         return new Boolean(this._ReceivingPausedAtStartup);
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("ServiceType")) {
         return this._ServiceType;
      } else if (var1.equals("Store")) {
         return this._Store;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("WindowInterval")) {
         return new Long(this._WindowInterval);
      } else if (var1.equals("WindowSize")) {
         return new Integer(this._WindowSize);
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
                  return 10;
               }
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
            case 7:
            case 8:
            case 9:
            case 10:
            case 14:
            case 17:
            case 18:
            case 21:
            case 25:
            case 29:
            default:
               break;
            case 11:
               if (var1.equals("window-size")) {
                  return 22;
               }
               break;
            case 12:
               if (var1.equals("server-names")) {
                  return 9;
               }

               if (var1.equals("service-type")) {
                  return 21;
               }
               break;
            case 13:
               if (var1.equals("bytes-maximum")) {
                  return 11;
               }
               break;
            case 15:
               if (var1.equals("window-interval")) {
                  return 32;
               }

               if (var1.equals("logging-enabled")) {
                  return 23;
               }
               break;
            case 16:
               if (var1.equals("messages-maximum")) {
                  return 14;
               }

               if (var1.equals("paging-directory")) {
                  return 31;
               }
               break;
            case 19:
               if (var1.equals("bytes-threshold-low")) {
                  return 13;
               }

               if (var1.equals("message-buffer-size")) {
                  return 30;
               }
               break;
            case 20:
               if (var1.equals("acknowledge-interval")) {
                  return 25;
               }

               if (var1.equals("bytes-threshold-high")) {
                  return 12;
               }

               if (var1.equals("default-time-to-live")) {
                  return 26;
               }

               if (var1.equals("maximum-message-size")) {
                  return 17;
               }
               break;
            case 22:
               if (var1.equals("messages-threshold-low")) {
                  return 16;
               }
               break;
            case 23:
               if (var1.equals("messages-threshold-high")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("default-retry-delay-base")) {
                  return 18;
               }

               if (var1.equals("jms-saf-message-log-file")) {
                  return 33;
               }
               break;
            case 26:
               if (var1.equals("incoming-paused-at-startup")) {
                  return 27;
               }
               break;
            case 27:
               if (var1.equals("default-retry-delay-maximum")) {
                  return 19;
               }

               if (var1.equals("receiving-paused-at-startup")) {
                  return 29;
               }
               break;
            case 28:
               if (var1.equals("forwarding-paused-at-startup")) {
                  return 28;
               }
               break;
            case 30:
               if (var1.equals("conversation-idle-time-maximum")) {
                  return 24;
               }

               if (var1.equals("default-retry-delay-multiplier")) {
                  return 20;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 33:
               return new JMSSAFMessageLogFileMBeanImpl.SchemaHelper2();
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
               return "store";
            case 11:
               return "bytes-maximum";
            case 12:
               return "bytes-threshold-high";
            case 13:
               return "bytes-threshold-low";
            case 14:
               return "messages-maximum";
            case 15:
               return "messages-threshold-high";
            case 16:
               return "messages-threshold-low";
            case 17:
               return "maximum-message-size";
            case 18:
               return "default-retry-delay-base";
            case 19:
               return "default-retry-delay-maximum";
            case 20:
               return "default-retry-delay-multiplier";
            case 21:
               return "service-type";
            case 22:
               return "window-size";
            case 23:
               return "logging-enabled";
            case 24:
               return "conversation-idle-time-maximum";
            case 25:
               return "acknowledge-interval";
            case 26:
               return "default-time-to-live";
            case 27:
               return "incoming-paused-at-startup";
            case 28:
               return "forwarding-paused-at-startup";
            case 29:
               return "receiving-paused-at-startup";
            case 30:
               return "message-buffer-size";
            case 31:
               return "paging-directory";
            case 32:
               return "window-interval";
            case 33:
               return "jms-saf-message-log-file";
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
            case 33:
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
      private SAFAgentMBeanImpl bean;

      protected Helper(SAFAgentMBeanImpl var1) {
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
               return "Store";
            case 11:
               return "BytesMaximum";
            case 12:
               return "BytesThresholdHigh";
            case 13:
               return "BytesThresholdLow";
            case 14:
               return "MessagesMaximum";
            case 15:
               return "MessagesThresholdHigh";
            case 16:
               return "MessagesThresholdLow";
            case 17:
               return "MaximumMessageSize";
            case 18:
               return "DefaultRetryDelayBase";
            case 19:
               return "DefaultRetryDelayMaximum";
            case 20:
               return "DefaultRetryDelayMultiplier";
            case 21:
               return "ServiceType";
            case 22:
               return "WindowSize";
            case 23:
               return "LoggingEnabled";
            case 24:
               return "ConversationIdleTimeMaximum";
            case 25:
               return "AcknowledgeInterval";
            case 26:
               return "DefaultTimeToLive";
            case 27:
               return "IncomingPausedAtStartup";
            case 28:
               return "ForwardingPausedAtStartup";
            case 29:
               return "ReceivingPausedAtStartup";
            case 30:
               return "MessageBufferSize";
            case 31:
               return "PagingDirectory";
            case 32:
               return "WindowInterval";
            case 33:
               return "JMSSAFMessageLogFile";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcknowledgeInterval")) {
            return 25;
         } else if (var1.equals("BytesMaximum")) {
            return 11;
         } else if (var1.equals("BytesThresholdHigh")) {
            return 12;
         } else if (var1.equals("BytesThresholdLow")) {
            return 13;
         } else if (var1.equals("ConversationIdleTimeMaximum")) {
            return 24;
         } else if (var1.equals("DefaultRetryDelayBase")) {
            return 18;
         } else if (var1.equals("DefaultRetryDelayMaximum")) {
            return 19;
         } else if (var1.equals("DefaultRetryDelayMultiplier")) {
            return 20;
         } else if (var1.equals("DefaultTimeToLive")) {
            return 26;
         } else if (var1.equals("JMSSAFMessageLogFile")) {
            return 33;
         } else if (var1.equals("MaximumMessageSize")) {
            return 17;
         } else if (var1.equals("MessageBufferSize")) {
            return 30;
         } else if (var1.equals("MessagesMaximum")) {
            return 14;
         } else if (var1.equals("MessagesThresholdHigh")) {
            return 15;
         } else if (var1.equals("MessagesThresholdLow")) {
            return 16;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PagingDirectory")) {
            return 31;
         } else if (var1.equals("ServerNames")) {
            return 9;
         } else if (var1.equals("ServiceType")) {
            return 21;
         } else if (var1.equals("Store")) {
            return 10;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("WindowInterval")) {
            return 32;
         } else if (var1.equals("WindowSize")) {
            return 22;
         } else if (var1.equals("ForwardingPausedAtStartup")) {
            return 28;
         } else if (var1.equals("IncomingPausedAtStartup")) {
            return 27;
         } else if (var1.equals("LoggingEnabled")) {
            return 23;
         } else {
            return var1.equals("ReceivingPausedAtStartup") ? 29 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getJMSSAFMessageLogFile() != null) {
            var1.add(new ArrayIterator(new JMSSAFMessageLogFileMBean[]{this.bean.getJMSSAFMessageLogFile()}));
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
            if (this.bean.isAcknowledgeIntervalSet()) {
               var2.append("AcknowledgeInterval");
               var2.append(String.valueOf(this.bean.getAcknowledgeInterval()));
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

            if (this.bean.isConversationIdleTimeMaximumSet()) {
               var2.append("ConversationIdleTimeMaximum");
               var2.append(String.valueOf(this.bean.getConversationIdleTimeMaximum()));
            }

            if (this.bean.isDefaultRetryDelayBaseSet()) {
               var2.append("DefaultRetryDelayBase");
               var2.append(String.valueOf(this.bean.getDefaultRetryDelayBase()));
            }

            if (this.bean.isDefaultRetryDelayMaximumSet()) {
               var2.append("DefaultRetryDelayMaximum");
               var2.append(String.valueOf(this.bean.getDefaultRetryDelayMaximum()));
            }

            if (this.bean.isDefaultRetryDelayMultiplierSet()) {
               var2.append("DefaultRetryDelayMultiplier");
               var2.append(String.valueOf(this.bean.getDefaultRetryDelayMultiplier()));
            }

            if (this.bean.isDefaultTimeToLiveSet()) {
               var2.append("DefaultTimeToLive");
               var2.append(String.valueOf(this.bean.getDefaultTimeToLive()));
            }

            var5 = this.computeChildHashValue(this.bean.getJMSSAFMessageLogFile());
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

            if (this.bean.isPagingDirectorySet()) {
               var2.append("PagingDirectory");
               var2.append(String.valueOf(this.bean.getPagingDirectory()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            if (this.bean.isServiceTypeSet()) {
               var2.append("ServiceType");
               var2.append(String.valueOf(this.bean.getServiceType()));
            }

            if (this.bean.isStoreSet()) {
               var2.append("Store");
               var2.append(String.valueOf(this.bean.getStore()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isWindowIntervalSet()) {
               var2.append("WindowInterval");
               var2.append(String.valueOf(this.bean.getWindowInterval()));
            }

            if (this.bean.isWindowSizeSet()) {
               var2.append("WindowSize");
               var2.append(String.valueOf(this.bean.getWindowSize()));
            }

            if (this.bean.isForwardingPausedAtStartupSet()) {
               var2.append("ForwardingPausedAtStartup");
               var2.append(String.valueOf(this.bean.isForwardingPausedAtStartup()));
            }

            if (this.bean.isIncomingPausedAtStartupSet()) {
               var2.append("IncomingPausedAtStartup");
               var2.append(String.valueOf(this.bean.isIncomingPausedAtStartup()));
            }

            if (this.bean.isLoggingEnabledSet()) {
               var2.append("LoggingEnabled");
               var2.append(String.valueOf(this.bean.isLoggingEnabled()));
            }

            if (this.bean.isReceivingPausedAtStartupSet()) {
               var2.append("ReceivingPausedAtStartup");
               var2.append(String.valueOf(this.bean.isReceivingPausedAtStartup()));
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
            SAFAgentMBeanImpl var2 = (SAFAgentMBeanImpl)var1;
            this.computeDiff("AcknowledgeInterval", this.bean.getAcknowledgeInterval(), var2.getAcknowledgeInterval(), true);
            this.computeDiff("BytesMaximum", this.bean.getBytesMaximum(), var2.getBytesMaximum(), true);
            this.computeDiff("BytesThresholdHigh", this.bean.getBytesThresholdHigh(), var2.getBytesThresholdHigh(), true);
            this.computeDiff("BytesThresholdLow", this.bean.getBytesThresholdLow(), var2.getBytesThresholdLow(), true);
            this.computeDiff("ConversationIdleTimeMaximum", this.bean.getConversationIdleTimeMaximum(), var2.getConversationIdleTimeMaximum(), true);
            this.computeDiff("DefaultRetryDelayBase", this.bean.getDefaultRetryDelayBase(), var2.getDefaultRetryDelayBase(), true);
            this.computeDiff("DefaultRetryDelayMaximum", this.bean.getDefaultRetryDelayMaximum(), var2.getDefaultRetryDelayMaximum(), true);
            this.computeDiff("DefaultRetryDelayMultiplier", this.bean.getDefaultRetryDelayMultiplier(), var2.getDefaultRetryDelayMultiplier(), true);
            this.computeDiff("DefaultTimeToLive", this.bean.getDefaultTimeToLive(), var2.getDefaultTimeToLive(), true);
            this.computeSubDiff("JMSSAFMessageLogFile", this.bean.getJMSSAFMessageLogFile(), var2.getJMSSAFMessageLogFile());
            this.computeDiff("MaximumMessageSize", this.bean.getMaximumMessageSize(), var2.getMaximumMessageSize(), true);
            this.computeDiff("MessageBufferSize", this.bean.getMessageBufferSize(), var2.getMessageBufferSize(), true);
            this.computeDiff("MessagesMaximum", this.bean.getMessagesMaximum(), var2.getMessagesMaximum(), true);
            this.computeDiff("MessagesThresholdHigh", this.bean.getMessagesThresholdHigh(), var2.getMessagesThresholdHigh(), true);
            this.computeDiff("MessagesThresholdLow", this.bean.getMessagesThresholdLow(), var2.getMessagesThresholdLow(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PagingDirectory", this.bean.getPagingDirectory(), var2.getPagingDirectory(), false);
            this.computeDiff("ServiceType", this.bean.getServiceType(), var2.getServiceType(), false);
            this.computeDiff("Store", this.bean.getStore(), var2.getStore(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("WindowInterval", this.bean.getWindowInterval(), var2.getWindowInterval(), true);
            this.computeDiff("WindowSize", this.bean.getWindowSize(), var2.getWindowSize(), true);
            this.computeDiff("ForwardingPausedAtStartup", this.bean.isForwardingPausedAtStartup(), var2.isForwardingPausedAtStartup(), false);
            this.computeDiff("IncomingPausedAtStartup", this.bean.isIncomingPausedAtStartup(), var2.isIncomingPausedAtStartup(), false);
            this.computeDiff("LoggingEnabled", this.bean.isLoggingEnabled(), var2.isLoggingEnabled(), true);
            this.computeDiff("ReceivingPausedAtStartup", this.bean.isReceivingPausedAtStartup(), var2.isReceivingPausedAtStartup(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SAFAgentMBeanImpl var3 = (SAFAgentMBeanImpl)var1.getSourceBean();
            SAFAgentMBeanImpl var4 = (SAFAgentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcknowledgeInterval")) {
                  var3.setAcknowledgeInterval(var4.getAcknowledgeInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("BytesMaximum")) {
                  var3.setBytesMaximum(var4.getBytesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("BytesThresholdHigh")) {
                  var3.setBytesThresholdHigh(var4.getBytesThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("BytesThresholdLow")) {
                  var3.setBytesThresholdLow(var4.getBytesThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("ConversationIdleTimeMaximum")) {
                  var3.setConversationIdleTimeMaximum(var4.getConversationIdleTimeMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("DefaultRetryDelayBase")) {
                  var3.setDefaultRetryDelayBase(var4.getDefaultRetryDelayBase());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("DefaultRetryDelayMaximum")) {
                  var3.setDefaultRetryDelayMaximum(var4.getDefaultRetryDelayMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("DefaultRetryDelayMultiplier")) {
                  var3.setDefaultRetryDelayMultiplier(var4.getDefaultRetryDelayMultiplier());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("DefaultTimeToLive")) {
                  var3.setDefaultTimeToLive(var4.getDefaultTimeToLive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("JMSSAFMessageLogFile")) {
                  if (var6 == 2) {
                     var3.setJMSSAFMessageLogFile((JMSSAFMessageLogFileMBean)this.createCopy((AbstractDescriptorBean)var4.getJMSSAFMessageLogFile()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("JMSSAFMessageLogFile", var3.getJMSSAFMessageLogFile());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("MaximumMessageSize")) {
                  var3.setMaximumMessageSize(var4.getMaximumMessageSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("MessageBufferSize")) {
                  var3.setMessageBufferSize(var4.getMessageBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("MessagesMaximum")) {
                  var3.setMessagesMaximum(var4.getMessagesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("MessagesThresholdHigh")) {
                  var3.setMessagesThresholdHigh(var4.getMessagesThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("MessagesThresholdLow")) {
                  var3.setMessagesThresholdLow(var4.getMessagesThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PagingDirectory")) {
                  var3.setPagingDirectory(var4.getPagingDirectory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (!var5.equals("ServerNames")) {
                  if (var5.equals("ServiceType")) {
                     var3.setServiceType(var4.getServiceType());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("Store")) {
                     var3.setStoreAsString(var4.getStoreAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("Targets")) {
                     var3.setTargetsAsString(var4.getTargetsAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("WindowInterval")) {
                     var3.setWindowInterval(var4.getWindowInterval());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (var5.equals("WindowSize")) {
                     var3.setWindowSize(var4.getWindowSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("ForwardingPausedAtStartup")) {
                     var3.setForwardingPausedAtStartup(var4.isForwardingPausedAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("IncomingPausedAtStartup")) {
                     var3.setIncomingPausedAtStartup(var4.isIncomingPausedAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  } else if (var5.equals("LoggingEnabled")) {
                     var3.setLoggingEnabled(var4.isLoggingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("ReceivingPausedAtStartup")) {
                     var3.setReceivingPausedAtStartup(var4.isReceivingPausedAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 29);
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
            SAFAgentMBeanImpl var5 = (SAFAgentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AcknowledgeInterval")) && this.bean.isAcknowledgeIntervalSet()) {
               var5.setAcknowledgeInterval(this.bean.getAcknowledgeInterval());
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

            if ((var3 == null || !var3.contains("ConversationIdleTimeMaximum")) && this.bean.isConversationIdleTimeMaximumSet()) {
               var5.setConversationIdleTimeMaximum(this.bean.getConversationIdleTimeMaximum());
            }

            if ((var3 == null || !var3.contains("DefaultRetryDelayBase")) && this.bean.isDefaultRetryDelayBaseSet()) {
               var5.setDefaultRetryDelayBase(this.bean.getDefaultRetryDelayBase());
            }

            if ((var3 == null || !var3.contains("DefaultRetryDelayMaximum")) && this.bean.isDefaultRetryDelayMaximumSet()) {
               var5.setDefaultRetryDelayMaximum(this.bean.getDefaultRetryDelayMaximum());
            }

            if ((var3 == null || !var3.contains("DefaultRetryDelayMultiplier")) && this.bean.isDefaultRetryDelayMultiplierSet()) {
               var5.setDefaultRetryDelayMultiplier(this.bean.getDefaultRetryDelayMultiplier());
            }

            if ((var3 == null || !var3.contains("DefaultTimeToLive")) && this.bean.isDefaultTimeToLiveSet()) {
               var5.setDefaultTimeToLive(this.bean.getDefaultTimeToLive());
            }

            if ((var3 == null || !var3.contains("JMSSAFMessageLogFile")) && this.bean.isJMSSAFMessageLogFileSet() && !var5._isSet(33)) {
               JMSSAFMessageLogFileMBean var4 = this.bean.getJMSSAFMessageLogFile();
               var5.setJMSSAFMessageLogFile((JMSSAFMessageLogFileMBean)null);
               var5.setJMSSAFMessageLogFile(var4 == null ? null : (JMSSAFMessageLogFileMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
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

            if ((var3 == null || !var3.contains("PagingDirectory")) && this.bean.isPagingDirectorySet()) {
               var5.setPagingDirectory(this.bean.getPagingDirectory());
            }

            if ((var3 == null || !var3.contains("ServiceType")) && this.bean.isServiceTypeSet()) {
               var5.setServiceType(this.bean.getServiceType());
            }

            if ((var3 == null || !var3.contains("Store")) && this.bean.isStoreSet()) {
               var5._unSet(var5, 10);
               var5.setStoreAsString(this.bean.getStoreAsString());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if ((var3 == null || !var3.contains("WindowInterval")) && this.bean.isWindowIntervalSet()) {
               var5.setWindowInterval(this.bean.getWindowInterval());
            }

            if ((var3 == null || !var3.contains("WindowSize")) && this.bean.isWindowSizeSet()) {
               var5.setWindowSize(this.bean.getWindowSize());
            }

            if ((var3 == null || !var3.contains("ForwardingPausedAtStartup")) && this.bean.isForwardingPausedAtStartupSet()) {
               var5.setForwardingPausedAtStartup(this.bean.isForwardingPausedAtStartup());
            }

            if ((var3 == null || !var3.contains("IncomingPausedAtStartup")) && this.bean.isIncomingPausedAtStartupSet()) {
               var5.setIncomingPausedAtStartup(this.bean.isIncomingPausedAtStartup());
            }

            if ((var3 == null || !var3.contains("LoggingEnabled")) && this.bean.isLoggingEnabledSet()) {
               var5.setLoggingEnabled(this.bean.isLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("ReceivingPausedAtStartup")) && this.bean.isReceivingPausedAtStartupSet()) {
               var5.setReceivingPausedAtStartup(this.bean.isReceivingPausedAtStartup());
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
         this.inferSubTree(this.bean.getJMSSAFMessageLogFile(), var1, var2);
         this.inferSubTree(this.bean.getStore(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
