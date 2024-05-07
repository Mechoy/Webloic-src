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
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.jms.module.validators.JMSModuleValidator;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSConnectionFactory;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JMSConnectionFactoryMBeanImpl extends DeploymentMBeanImpl implements JMSConnectionFactoryMBean, Serializable {
   private String _AcknowledgePolicy;
   private boolean _AllowCloseInOnMessage;
   private String _ClientId;
   private String _DefaultDeliveryMode;
   private int _DefaultPriority;
   private long _DefaultRedeliveryDelay;
   private long _DefaultTimeToDeliver;
   private long _DefaultTimeToLive;
   private boolean _FlowControlEnabled;
   private int _FlowInterval;
   private int _FlowMaximum;
   private int _FlowMinimum;
   private int _FlowSteps;
   private String _JNDIName;
   private boolean _LoadBalancingEnabled;
   private int _MessagesMaximum;
   private String _Name;
   private String _Notes;
   private String _OverrunPolicy;
   private long _SendTimeout;
   private boolean _ServerAffinityEnabled;
   private TargetMBean[] _Targets;
   private long _TransactionTimeout;
   private boolean _UserTransactionsEnabled;
   private boolean _XAConnectionFactoryEnabled;
   private boolean _XAServerEnabled;
   private JMSConnectionFactory _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSConnectionFactoryMBeanImpl() {
      try {
         this._customizer = new JMSConnectionFactory(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSConnectionFactoryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSConnectionFactory(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getJNDIName() {
      return this._customizer.getJNDIName();
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

   public boolean isJNDINameSet() {
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
                        JMSConnectionFactoryMBeanImpl.this.addTarget((TargetMBean)var1);
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

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      JMSModuleValidator.validateCFJNDIName(var1);
      String var2 = this.getJNDIName();
      this._customizer.setJNDIName(var1);
      this._postSet(9, var2, var1);
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
                  return JMSConnectionFactoryMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
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

   public String getClientId() {
      return this._customizer.getClientId();
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isClientIdSet() {
      return this._isSet(10);
   }

   public boolean isNotesSet() {
      return this._isSet(3);
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

   public void setClientId(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getClientId();
      this._customizer.setClientId(var1);
      this._postSet(10, var2, var1);
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
   }

   public int getDefaultPriority() {
      return this._customizer.getDefaultPriority();
   }

   public boolean isDefaultPrioritySet() {
      return this._isSet(11);
   }

   public void setDefaultPriority(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultPriority", (long)var1, 0L, 9L);
      int var2 = this.getDefaultPriority();
      this._customizer.setDefaultPriority(var1);
      this._postSet(11, var2, var1);
   }

   public long getDefaultTimeToDeliver() {
      return this._customizer.getDefaultTimeToDeliver();
   }

   public boolean isDefaultTimeToDeliverSet() {
      return this._isSet(12);
   }

   public void setDefaultTimeToDeliver(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultTimeToDeliver", var1, 0L, Long.MAX_VALUE);
      long var3 = this.getDefaultTimeToDeliver();
      this._customizer.setDefaultTimeToDeliver(var1);
      this._postSet(12, var3, var1);
   }

   public long getDefaultTimeToLive() {
      return this._customizer.getDefaultTimeToLive();
   }

   public boolean isDefaultTimeToLiveSet() {
      return this._isSet(13);
   }

   public void setDefaultTimeToLive(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultTimeToLive", var1, 0L, Long.MAX_VALUE);
      long var3 = this.getDefaultTimeToLive();
      this._customizer.setDefaultTimeToLive(var1);
      this._postSet(13, var3, var1);
   }

   public long getSendTimeout() {
      return this._customizer.getSendTimeout();
   }

   public boolean isSendTimeoutSet() {
      return this._isSet(14);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setSendTimeout(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SendTimeout", var1, 0L, Long.MAX_VALUE);
      long var3 = this.getSendTimeout();
      this._customizer.setSendTimeout(var1);
      this._postSet(14, var3, var1);
   }

   public String getDefaultDeliveryMode() {
      return this._customizer.getDefaultDeliveryMode();
   }

   public boolean isDefaultDeliveryModeSet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setDefaultDeliveryMode(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Persistent", "Non-Persistent"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultDeliveryMode", var1, var2);
      String var3 = this.getDefaultDeliveryMode();
      this._customizer.setDefaultDeliveryMode(var1);
      this._postSet(15, var3, var1);
   }

   public long getDefaultRedeliveryDelay() {
      return this._customizer.getDefaultRedeliveryDelay();
   }

   public boolean isDefaultRedeliveryDelaySet() {
      return this._isSet(16);
   }

   public void setDefaultRedeliveryDelay(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultRedeliveryDelay", var1, 0L, Long.MAX_VALUE);
      long var3 = this.getDefaultRedeliveryDelay();
      this._customizer.setDefaultRedeliveryDelay(var1);
      this._postSet(16, var3, var1);
   }

   public long getTransactionTimeout() {
      return this._customizer.getTransactionTimeout();
   }

   public boolean isTransactionTimeoutSet() {
      return this._isSet(17);
   }

   public void setTransactionTimeout(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TransactionTimeout", var1, 0L, 2147483647L);
      long var3 = this.getTransactionTimeout();
      this._customizer.setTransactionTimeout(var1);
      this._postSet(17, var3, var1);
   }

   public boolean isUserTransactionsEnabled() {
      return this._customizer.isUserTransactionsEnabled();
   }

   public boolean isUserTransactionsEnabledSet() {
      return this._isSet(18);
   }

   public void setUserTransactionsEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isUserTransactionsEnabled();
      this._customizer.setUserTransactionsEnabled(var1);
      this._postSet(18, var2, var1);
   }

   public boolean getAllowCloseInOnMessage() {
      return this._customizer.getAllowCloseInOnMessage();
   }

   public boolean isAllowCloseInOnMessageSet() {
      return this._isSet(19);
   }

   public void setAllowCloseInOnMessage(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getAllowCloseInOnMessage();
      this._customizer.setAllowCloseInOnMessage(var1);
      this._postSet(19, var2, var1);
   }

   public int getMessagesMaximum() {
      return this._customizer.getMessagesMaximum();
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(20);
   }

   public void setMessagesMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesMaximum", (long)var1, -1L, 2147483647L);
      int var2 = this.getMessagesMaximum();
      this._customizer.setMessagesMaximum(var1);
      this._postSet(20, var2, var1);
   }

   public String getOverrunPolicy() {
      return this._customizer.getOverrunPolicy();
   }

   public boolean isOverrunPolicySet() {
      return this._isSet(21);
   }

   public void setOverrunPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"KeepOld", "KeepNew"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("OverrunPolicy", var1, var2);
      String var3 = this.getOverrunPolicy();
      this._customizer.setOverrunPolicy(var1);
      this._postSet(21, var3, var1);
   }

   public boolean isXAConnectionFactoryEnabled() {
      return this._customizer.isXAConnectionFactoryEnabled();
   }

   public boolean isXAConnectionFactoryEnabledSet() {
      return this._isSet(22);
   }

   public void setXAConnectionFactoryEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isXAConnectionFactoryEnabled();
      this._customizer.setXAConnectionFactoryEnabled(var1);
      this._postSet(22, var2, var1);
   }

   public String getAcknowledgePolicy() {
      return this._customizer.getAcknowledgePolicy();
   }

   public boolean isAcknowledgePolicySet() {
      return this._isSet(23);
   }

   public void setAcknowledgePolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"All", "Previous"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AcknowledgePolicy", var1, var2);
      String var3 = this.getAcknowledgePolicy();
      this._customizer.setAcknowledgePolicy(var1);
      this._postSet(23, var3, var1);
   }

   public int getFlowMinimum() {
      return this._customizer.getFlowMinimum();
   }

   public boolean isFlowMinimumSet() {
      return this._isSet(24);
   }

   public void setFlowMinimum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FlowMinimum", (long)var1, 1L, 2147483647L);
      int var2 = this.getFlowMinimum();
      this._customizer.setFlowMinimum(var1);
      this._postSet(24, var2, var1);
   }

   public int getFlowMaximum() {
      return this._customizer.getFlowMaximum();
   }

   public boolean isFlowMaximumSet() {
      return this._isSet(25);
   }

   public void setFlowMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FlowMaximum", (long)var1, 1L, 2147483647L);
      int var2 = this.getFlowMaximum();
      this._customizer.setFlowMaximum(var1);
      this._postSet(25, var2, var1);
   }

   public int getFlowInterval() {
      return this._customizer.getFlowInterval();
   }

   public boolean isFlowIntervalSet() {
      return this._isSet(26);
   }

   public void setFlowInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FlowInterval", (long)var1, 0L, 2147483647L);
      int var2 = this.getFlowInterval();
      this._customizer.setFlowInterval(var1);
      this._postSet(26, var2, var1);
   }

   public int getFlowSteps() {
      return this._customizer.getFlowSteps();
   }

   public boolean isFlowStepsSet() {
      return this._isSet(27);
   }

   public void setFlowSteps(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FlowSteps", (long)var1, 1L, 2147483647L);
      int var2 = this.getFlowSteps();
      this._customizer.setFlowSteps(var1);
      this._postSet(27, var2, var1);
   }

   public boolean isFlowControlEnabled() {
      return this._FlowControlEnabled;
   }

   public boolean isFlowControlEnabledSet() {
      return this._isSet(28);
   }

   public void setFlowControlEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isFlowControlEnabled();
      this._customizer.setFlowControlEnabled(var1);
      this._postSet(28, var2, var1);
   }

   public boolean isLoadBalancingEnabled() {
      return this._LoadBalancingEnabled;
   }

   public boolean isLoadBalancingEnabledSet() {
      return this._isSet(29);
   }

   public void setLoadBalancingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isLoadBalancingEnabled();
      this._customizer.setLoadBalancingEnabled(var1);
      this._postSet(29, var2, var1);
   }

   public boolean isServerAffinityEnabled() {
      return this._ServerAffinityEnabled;
   }

   public boolean isServerAffinityEnabledSet() {
      return this._isSet(30);
   }

   public void setServerAffinityEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isServerAffinityEnabled();
      this._customizer.setServerAffinityEnabled(var1);
      this._postSet(30, var2, var1);
   }

   public boolean isXAServerEnabled() {
      return this._customizer.isXAServerEnabled();
   }

   public boolean isXAServerEnabledSet() {
      return this._isSet(31);
   }

   public void setXAServerEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isXAServerEnabled();
      this._customizer.setXAServerEnabled(var1);
      this._postSet(31, var2, var1);
   }

   public void useDelegates(JMSConnectionFactoryBean var1, SubDeploymentMBean var2) {
      this._customizer.useDelegates(var1, var2);
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
         var1 = 23;
      }

      try {
         switch (var1) {
            case 23:
               this._customizer.setAcknowledgePolicy("All");
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setAllowCloseInOnMessage(false);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setClientId((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setDefaultDeliveryMode("Persistent");
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setDefaultPriority(4);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setDefaultRedeliveryDelay(0L);
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setDefaultTimeToDeliver(0L);
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setDefaultTimeToLive(0L);
               if (var2) {
                  break;
               }
            case 26:
               this._customizer.setFlowInterval(60);
               if (var2) {
                  break;
               }
            case 25:
               this._customizer.setFlowMaximum(500);
               if (var2) {
                  break;
               }
            case 24:
               this._customizer.setFlowMinimum(50);
               if (var2) {
                  break;
               }
            case 27:
               this._customizer.setFlowSteps(10);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setMessagesMaximum(10);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._customizer.setNotes((String)null);
               if (var2) {
                  break;
               }
            case 21:
               this._customizer.setOverrunPolicy("KeepOld");
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setSendTimeout(10L);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 17:
               this._customizer.setTransactionTimeout(3600L);
               if (var2) {
                  break;
               }
            case 28:
               this._customizer.setFlowControlEnabled(true);
               if (var2) {
                  break;
               }
            case 29:
               this._customizer.setLoadBalancingEnabled(true);
               if (var2) {
                  break;
               }
            case 30:
               this._customizer.setServerAffinityEnabled(true);
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setUserTransactionsEnabled(false);
               if (var2) {
                  break;
               }
            case 22:
               this._customizer.setXAConnectionFactoryEnabled(false);
               if (var2) {
                  break;
               }
            case 31:
               this._customizer.setXAServerEnabled(false);
               if (var2) {
                  break;
               }
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
      return "JMSConnectionFactory";
   }

   public void putValue(String var1, Object var2) {
      String var8;
      if (var1.equals("AcknowledgePolicy")) {
         var8 = this._AcknowledgePolicy;
         this._AcknowledgePolicy = (String)var2;
         this._postSet(23, var8, this._AcknowledgePolicy);
      } else {
         boolean var5;
         if (var1.equals("AllowCloseInOnMessage")) {
            var5 = this._AllowCloseInOnMessage;
            this._AllowCloseInOnMessage = (Boolean)var2;
            this._postSet(19, var5, this._AllowCloseInOnMessage);
         } else if (var1.equals("ClientId")) {
            var8 = this._ClientId;
            this._ClientId = (String)var2;
            this._postSet(10, var8, this._ClientId);
         } else if (var1.equals("DefaultDeliveryMode")) {
            var8 = this._DefaultDeliveryMode;
            this._DefaultDeliveryMode = (String)var2;
            this._postSet(15, var8, this._DefaultDeliveryMode);
         } else {
            int var9;
            if (var1.equals("DefaultPriority")) {
               var9 = this._DefaultPriority;
               this._DefaultPriority = (Integer)var2;
               this._postSet(11, var9, this._DefaultPriority);
            } else {
               long var6;
               if (var1.equals("DefaultRedeliveryDelay")) {
                  var6 = this._DefaultRedeliveryDelay;
                  this._DefaultRedeliveryDelay = (Long)var2;
                  this._postSet(16, var6, this._DefaultRedeliveryDelay);
               } else if (var1.equals("DefaultTimeToDeliver")) {
                  var6 = this._DefaultTimeToDeliver;
                  this._DefaultTimeToDeliver = (Long)var2;
                  this._postSet(12, var6, this._DefaultTimeToDeliver);
               } else if (var1.equals("DefaultTimeToLive")) {
                  var6 = this._DefaultTimeToLive;
                  this._DefaultTimeToLive = (Long)var2;
                  this._postSet(13, var6, this._DefaultTimeToLive);
               } else if (var1.equals("FlowControlEnabled")) {
                  var5 = this._FlowControlEnabled;
                  this._FlowControlEnabled = (Boolean)var2;
                  this._postSet(28, var5, this._FlowControlEnabled);
               } else if (var1.equals("FlowInterval")) {
                  var9 = this._FlowInterval;
                  this._FlowInterval = (Integer)var2;
                  this._postSet(26, var9, this._FlowInterval);
               } else if (var1.equals("FlowMaximum")) {
                  var9 = this._FlowMaximum;
                  this._FlowMaximum = (Integer)var2;
                  this._postSet(25, var9, this._FlowMaximum);
               } else if (var1.equals("FlowMinimum")) {
                  var9 = this._FlowMinimum;
                  this._FlowMinimum = (Integer)var2;
                  this._postSet(24, var9, this._FlowMinimum);
               } else if (var1.equals("FlowSteps")) {
                  var9 = this._FlowSteps;
                  this._FlowSteps = (Integer)var2;
                  this._postSet(27, var9, this._FlowSteps);
               } else if (var1.equals("JNDIName")) {
                  var8 = this._JNDIName;
                  this._JNDIName = (String)var2;
                  this._postSet(9, var8, this._JNDIName);
               } else if (var1.equals("LoadBalancingEnabled")) {
                  var5 = this._LoadBalancingEnabled;
                  this._LoadBalancingEnabled = (Boolean)var2;
                  this._postSet(29, var5, this._LoadBalancingEnabled);
               } else if (var1.equals("MessagesMaximum")) {
                  var9 = this._MessagesMaximum;
                  this._MessagesMaximum = (Integer)var2;
                  this._postSet(20, var9, this._MessagesMaximum);
               } else if (var1.equals("Name")) {
                  var8 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var8, this._Name);
               } else if (var1.equals("Notes")) {
                  var8 = this._Notes;
                  this._Notes = (String)var2;
                  this._postSet(3, var8, this._Notes);
               } else if (var1.equals("OverrunPolicy")) {
                  var8 = this._OverrunPolicy;
                  this._OverrunPolicy = (String)var2;
                  this._postSet(21, var8, this._OverrunPolicy);
               } else if (var1.equals("SendTimeout")) {
                  var6 = this._SendTimeout;
                  this._SendTimeout = (Long)var2;
                  this._postSet(14, var6, this._SendTimeout);
               } else if (var1.equals("ServerAffinityEnabled")) {
                  var5 = this._ServerAffinityEnabled;
                  this._ServerAffinityEnabled = (Boolean)var2;
                  this._postSet(30, var5, this._ServerAffinityEnabled);
               } else if (var1.equals("Targets")) {
                  TargetMBean[] var7 = this._Targets;
                  this._Targets = (TargetMBean[])((TargetMBean[])var2);
                  this._postSet(7, var7, this._Targets);
               } else if (var1.equals("TransactionTimeout")) {
                  var6 = this._TransactionTimeout;
                  this._TransactionTimeout = (Long)var2;
                  this._postSet(17, var6, this._TransactionTimeout);
               } else if (var1.equals("UserTransactionsEnabled")) {
                  var5 = this._UserTransactionsEnabled;
                  this._UserTransactionsEnabled = (Boolean)var2;
                  this._postSet(18, var5, this._UserTransactionsEnabled);
               } else if (var1.equals("XAConnectionFactoryEnabled")) {
                  var5 = this._XAConnectionFactoryEnabled;
                  this._XAConnectionFactoryEnabled = (Boolean)var2;
                  this._postSet(22, var5, this._XAConnectionFactoryEnabled);
               } else if (var1.equals("XAServerEnabled")) {
                  var5 = this._XAServerEnabled;
                  this._XAServerEnabled = (Boolean)var2;
                  this._postSet(31, var5, this._XAServerEnabled);
               } else if (var1.equals("customizer")) {
                  JMSConnectionFactory var3 = this._customizer;
                  this._customizer = (JMSConnectionFactory)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcknowledgePolicy")) {
         return this._AcknowledgePolicy;
      } else if (var1.equals("AllowCloseInOnMessage")) {
         return new Boolean(this._AllowCloseInOnMessage);
      } else if (var1.equals("ClientId")) {
         return this._ClientId;
      } else if (var1.equals("DefaultDeliveryMode")) {
         return this._DefaultDeliveryMode;
      } else if (var1.equals("DefaultPriority")) {
         return new Integer(this._DefaultPriority);
      } else if (var1.equals("DefaultRedeliveryDelay")) {
         return new Long(this._DefaultRedeliveryDelay);
      } else if (var1.equals("DefaultTimeToDeliver")) {
         return new Long(this._DefaultTimeToDeliver);
      } else if (var1.equals("DefaultTimeToLive")) {
         return new Long(this._DefaultTimeToLive);
      } else if (var1.equals("FlowControlEnabled")) {
         return new Boolean(this._FlowControlEnabled);
      } else if (var1.equals("FlowInterval")) {
         return new Integer(this._FlowInterval);
      } else if (var1.equals("FlowMaximum")) {
         return new Integer(this._FlowMaximum);
      } else if (var1.equals("FlowMinimum")) {
         return new Integer(this._FlowMinimum);
      } else if (var1.equals("FlowSteps")) {
         return new Integer(this._FlowSteps);
      } else if (var1.equals("JNDIName")) {
         return this._JNDIName;
      } else if (var1.equals("LoadBalancingEnabled")) {
         return new Boolean(this._LoadBalancingEnabled);
      } else if (var1.equals("MessagesMaximum")) {
         return new Integer(this._MessagesMaximum);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("OverrunPolicy")) {
         return this._OverrunPolicy;
      } else if (var1.equals("SendTimeout")) {
         return new Long(this._SendTimeout);
      } else if (var1.equals("ServerAffinityEnabled")) {
         return new Boolean(this._ServerAffinityEnabled);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TransactionTimeout")) {
         return new Long(this._TransactionTimeout);
      } else if (var1.equals("UserTransactionsEnabled")) {
         return new Boolean(this._UserTransactionsEnabled);
      } else if (var1.equals("XAConnectionFactoryEnabled")) {
         return new Boolean(this._XAConnectionFactoryEnabled);
      } else if (var1.equals("XAServerEnabled")) {
         return new Boolean(this._XAServerEnabled);
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
               if (var1.equals("notes")) {
                  return 3;
               }
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
            case 7:
            case 8:
            case 11:
            case 15:
            case 26:
            case 27:
            case 28:
            default:
               break;
            case 9:
               if (var1.equals("client-id")) {
                  return 10;
               }

               if (var1.equals("jndi-name")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("flow-steps")) {
                  return 27;
               }
               break;
            case 12:
               if (var1.equals("flow-maximum")) {
                  return 25;
               }

               if (var1.equals("flow-minimum")) {
                  return 24;
               }

               if (var1.equals("send-timeout")) {
                  return 14;
               }
               break;
            case 13:
               if (var1.equals("flow-interval")) {
                  return 26;
               }
               break;
            case 14:
               if (var1.equals("overrun-policy")) {
                  return 21;
               }
               break;
            case 16:
               if (var1.equals("default-priority")) {
                  return 11;
               }

               if (var1.equals("messages-maximum")) {
                  return 20;
               }
               break;
            case 17:
               if (var1.equals("xa-server-enabled")) {
                  return 31;
               }
               break;
            case 18:
               if (var1.equals("acknowledge-policy")) {
                  return 23;
               }
               break;
            case 19:
               if (var1.equals("transaction-timeout")) {
                  return 17;
               }
               break;
            case 20:
               if (var1.equals("default-time-to-live")) {
                  return 13;
               }

               if (var1.equals("flow-control-enabled")) {
                  return 28;
               }
               break;
            case 21:
               if (var1.equals("default-delivery-mode")) {
                  return 15;
               }
               break;
            case 22:
               if (var1.equals("load-balancing-enabled")) {
                  return 29;
               }
               break;
            case 23:
               if (var1.equals("default-time-to-deliver")) {
                  return 12;
               }

               if (var1.equals("server-affinity-enabled")) {
                  return 30;
               }
               break;
            case 24:
               if (var1.equals("default-redelivery-delay")) {
                  return 16;
               }
               break;
            case 25:
               if (var1.equals("allow-close-in-on-message")) {
                  return 19;
               }

               if (var1.equals("user-transactions-enabled")) {
                  return 18;
               }
               break;
            case 29:
               if (var1.equals("xa-connection-factory-enabled")) {
                  return 22;
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
               return "notes";
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "jndi-name";
            case 10:
               return "client-id";
            case 11:
               return "default-priority";
            case 12:
               return "default-time-to-deliver";
            case 13:
               return "default-time-to-live";
            case 14:
               return "send-timeout";
            case 15:
               return "default-delivery-mode";
            case 16:
               return "default-redelivery-delay";
            case 17:
               return "transaction-timeout";
            case 18:
               return "user-transactions-enabled";
            case 19:
               return "allow-close-in-on-message";
            case 20:
               return "messages-maximum";
            case 21:
               return "overrun-policy";
            case 22:
               return "xa-connection-factory-enabled";
            case 23:
               return "acknowledge-policy";
            case 24:
               return "flow-minimum";
            case 25:
               return "flow-maximum";
            case 26:
               return "flow-interval";
            case 27:
               return "flow-steps";
            case 28:
               return "flow-control-enabled";
            case 29:
               return "load-balancing-enabled";
            case 30:
               return "server-affinity-enabled";
            case 31:
               return "xa-server-enabled";
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
      private JMSConnectionFactoryMBeanImpl bean;

      protected Helper(JMSConnectionFactoryMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
               return "Notes";
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "JNDIName";
            case 10:
               return "ClientId";
            case 11:
               return "DefaultPriority";
            case 12:
               return "DefaultTimeToDeliver";
            case 13:
               return "DefaultTimeToLive";
            case 14:
               return "SendTimeout";
            case 15:
               return "DefaultDeliveryMode";
            case 16:
               return "DefaultRedeliveryDelay";
            case 17:
               return "TransactionTimeout";
            case 18:
               return "UserTransactionsEnabled";
            case 19:
               return "AllowCloseInOnMessage";
            case 20:
               return "MessagesMaximum";
            case 21:
               return "OverrunPolicy";
            case 22:
               return "XAConnectionFactoryEnabled";
            case 23:
               return "AcknowledgePolicy";
            case 24:
               return "FlowMinimum";
            case 25:
               return "FlowMaximum";
            case 26:
               return "FlowInterval";
            case 27:
               return "FlowSteps";
            case 28:
               return "FlowControlEnabled";
            case 29:
               return "LoadBalancingEnabled";
            case 30:
               return "ServerAffinityEnabled";
            case 31:
               return "XAServerEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcknowledgePolicy")) {
            return 23;
         } else if (var1.equals("AllowCloseInOnMessage")) {
            return 19;
         } else if (var1.equals("ClientId")) {
            return 10;
         } else if (var1.equals("DefaultDeliveryMode")) {
            return 15;
         } else if (var1.equals("DefaultPriority")) {
            return 11;
         } else if (var1.equals("DefaultRedeliveryDelay")) {
            return 16;
         } else if (var1.equals("DefaultTimeToDeliver")) {
            return 12;
         } else if (var1.equals("DefaultTimeToLive")) {
            return 13;
         } else if (var1.equals("FlowInterval")) {
            return 26;
         } else if (var1.equals("FlowMaximum")) {
            return 25;
         } else if (var1.equals("FlowMinimum")) {
            return 24;
         } else if (var1.equals("FlowSteps")) {
            return 27;
         } else if (var1.equals("JNDIName")) {
            return 9;
         } else if (var1.equals("MessagesMaximum")) {
            return 20;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else if (var1.equals("OverrunPolicy")) {
            return 21;
         } else if (var1.equals("SendTimeout")) {
            return 14;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TransactionTimeout")) {
            return 17;
         } else if (var1.equals("FlowControlEnabled")) {
            return 28;
         } else if (var1.equals("LoadBalancingEnabled")) {
            return 29;
         } else if (var1.equals("ServerAffinityEnabled")) {
            return 30;
         } else if (var1.equals("UserTransactionsEnabled")) {
            return 18;
         } else if (var1.equals("XAConnectionFactoryEnabled")) {
            return 22;
         } else {
            return var1.equals("XAServerEnabled") ? 31 : super.getPropertyIndex(var1);
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
            if (this.bean.isAcknowledgePolicySet()) {
               var2.append("AcknowledgePolicy");
               var2.append(String.valueOf(this.bean.getAcknowledgePolicy()));
            }

            if (this.bean.isAllowCloseInOnMessageSet()) {
               var2.append("AllowCloseInOnMessage");
               var2.append(String.valueOf(this.bean.getAllowCloseInOnMessage()));
            }

            if (this.bean.isClientIdSet()) {
               var2.append("ClientId");
               var2.append(String.valueOf(this.bean.getClientId()));
            }

            if (this.bean.isDefaultDeliveryModeSet()) {
               var2.append("DefaultDeliveryMode");
               var2.append(String.valueOf(this.bean.getDefaultDeliveryMode()));
            }

            if (this.bean.isDefaultPrioritySet()) {
               var2.append("DefaultPriority");
               var2.append(String.valueOf(this.bean.getDefaultPriority()));
            }

            if (this.bean.isDefaultRedeliveryDelaySet()) {
               var2.append("DefaultRedeliveryDelay");
               var2.append(String.valueOf(this.bean.getDefaultRedeliveryDelay()));
            }

            if (this.bean.isDefaultTimeToDeliverSet()) {
               var2.append("DefaultTimeToDeliver");
               var2.append(String.valueOf(this.bean.getDefaultTimeToDeliver()));
            }

            if (this.bean.isDefaultTimeToLiveSet()) {
               var2.append("DefaultTimeToLive");
               var2.append(String.valueOf(this.bean.getDefaultTimeToLive()));
            }

            if (this.bean.isFlowIntervalSet()) {
               var2.append("FlowInterval");
               var2.append(String.valueOf(this.bean.getFlowInterval()));
            }

            if (this.bean.isFlowMaximumSet()) {
               var2.append("FlowMaximum");
               var2.append(String.valueOf(this.bean.getFlowMaximum()));
            }

            if (this.bean.isFlowMinimumSet()) {
               var2.append("FlowMinimum");
               var2.append(String.valueOf(this.bean.getFlowMinimum()));
            }

            if (this.bean.isFlowStepsSet()) {
               var2.append("FlowSteps");
               var2.append(String.valueOf(this.bean.getFlowSteps()));
            }

            if (this.bean.isJNDINameSet()) {
               var2.append("JNDIName");
               var2.append(String.valueOf(this.bean.getJNDIName()));
            }

            if (this.bean.isMessagesMaximumSet()) {
               var2.append("MessagesMaximum");
               var2.append(String.valueOf(this.bean.getMessagesMaximum()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isOverrunPolicySet()) {
               var2.append("OverrunPolicy");
               var2.append(String.valueOf(this.bean.getOverrunPolicy()));
            }

            if (this.bean.isSendTimeoutSet()) {
               var2.append("SendTimeout");
               var2.append(String.valueOf(this.bean.getSendTimeout()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTransactionTimeoutSet()) {
               var2.append("TransactionTimeout");
               var2.append(String.valueOf(this.bean.getTransactionTimeout()));
            }

            if (this.bean.isFlowControlEnabledSet()) {
               var2.append("FlowControlEnabled");
               var2.append(String.valueOf(this.bean.isFlowControlEnabled()));
            }

            if (this.bean.isLoadBalancingEnabledSet()) {
               var2.append("LoadBalancingEnabled");
               var2.append(String.valueOf(this.bean.isLoadBalancingEnabled()));
            }

            if (this.bean.isServerAffinityEnabledSet()) {
               var2.append("ServerAffinityEnabled");
               var2.append(String.valueOf(this.bean.isServerAffinityEnabled()));
            }

            if (this.bean.isUserTransactionsEnabledSet()) {
               var2.append("UserTransactionsEnabled");
               var2.append(String.valueOf(this.bean.isUserTransactionsEnabled()));
            }

            if (this.bean.isXAConnectionFactoryEnabledSet()) {
               var2.append("XAConnectionFactoryEnabled");
               var2.append(String.valueOf(this.bean.isXAConnectionFactoryEnabled()));
            }

            if (this.bean.isXAServerEnabledSet()) {
               var2.append("XAServerEnabled");
               var2.append(String.valueOf(this.bean.isXAServerEnabled()));
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
            JMSConnectionFactoryMBeanImpl var2 = (JMSConnectionFactoryMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AcknowledgePolicy", this.bean.getAcknowledgePolicy(), var2.getAcknowledgePolicy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AllowCloseInOnMessage", this.bean.getAllowCloseInOnMessage(), var2.getAllowCloseInOnMessage(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ClientId", this.bean.getClientId(), var2.getClientId(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultDeliveryMode", this.bean.getDefaultDeliveryMode(), var2.getDefaultDeliveryMode(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultPriority", this.bean.getDefaultPriority(), var2.getDefaultPriority(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultRedeliveryDelay", this.bean.getDefaultRedeliveryDelay(), var2.getDefaultRedeliveryDelay(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultTimeToDeliver", this.bean.getDefaultTimeToDeliver(), var2.getDefaultTimeToDeliver(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultTimeToLive", this.bean.getDefaultTimeToLive(), var2.getDefaultTimeToLive(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FlowInterval", this.bean.getFlowInterval(), var2.getFlowInterval(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FlowMaximum", this.bean.getFlowMaximum(), var2.getFlowMaximum(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FlowMinimum", this.bean.getFlowMinimum(), var2.getFlowMinimum(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FlowSteps", this.bean.getFlowSteps(), var2.getFlowSteps(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIName", this.bean.getJNDIName(), var2.getJNDIName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesMaximum", this.bean.getMessagesMaximum(), var2.getMessagesMaximum(), true);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("OverrunPolicy", this.bean.getOverrunPolicy(), var2.getOverrunPolicy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SendTimeout", this.bean.getSendTimeout(), var2.getSendTimeout(), true);
            }

            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TransactionTimeout", this.bean.getTransactionTimeout(), var2.getTransactionTimeout(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FlowControlEnabled", this.bean.isFlowControlEnabled(), var2.isFlowControlEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoadBalancingEnabled", this.bean.isLoadBalancingEnabled(), var2.isLoadBalancingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ServerAffinityEnabled", this.bean.isServerAffinityEnabled(), var2.isServerAffinityEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("UserTransactionsEnabled", this.bean.isUserTransactionsEnabled(), var2.isUserTransactionsEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XAConnectionFactoryEnabled", this.bean.isXAConnectionFactoryEnabled(), var2.isXAConnectionFactoryEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XAServerEnabled", this.bean.isXAServerEnabled(), var2.isXAServerEnabled(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSConnectionFactoryMBeanImpl var3 = (JMSConnectionFactoryMBeanImpl)var1.getSourceBean();
            JMSConnectionFactoryMBeanImpl var4 = (JMSConnectionFactoryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcknowledgePolicy")) {
                  var3.setAcknowledgePolicy(var4.getAcknowledgePolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("AllowCloseInOnMessage")) {
                  var3.setAllowCloseInOnMessage(var4.getAllowCloseInOnMessage());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("ClientId")) {
                  var3.setClientId(var4.getClientId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DefaultDeliveryMode")) {
                  var3.setDefaultDeliveryMode(var4.getDefaultDeliveryMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("DefaultPriority")) {
                  var3.setDefaultPriority(var4.getDefaultPriority());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("DefaultRedeliveryDelay")) {
                  var3.setDefaultRedeliveryDelay(var4.getDefaultRedeliveryDelay());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("DefaultTimeToDeliver")) {
                  var3.setDefaultTimeToDeliver(var4.getDefaultTimeToDeliver());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("DefaultTimeToLive")) {
                  var3.setDefaultTimeToLive(var4.getDefaultTimeToLive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("FlowInterval")) {
                  var3.setFlowInterval(var4.getFlowInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("FlowMaximum")) {
                  var3.setFlowMaximum(var4.getFlowMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("FlowMinimum")) {
                  var3.setFlowMinimum(var4.getFlowMinimum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("FlowSteps")) {
                  var3.setFlowSteps(var4.getFlowSteps());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("JNDIName")) {
                  var3.setJNDIName(var4.getJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MessagesMaximum")) {
                  var3.setMessagesMaximum(var4.getMessagesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Notes")) {
                  var3.setNotes(var4.getNotes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("OverrunPolicy")) {
                  var3.setOverrunPolicy(var4.getOverrunPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("SendTimeout")) {
                  var3.setSendTimeout(var4.getSendTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("TransactionTimeout")) {
                  var3.setTransactionTimeout(var4.getTransactionTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("FlowControlEnabled")) {
                  var3.setFlowControlEnabled(var4.isFlowControlEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("LoadBalancingEnabled")) {
                  var3.setLoadBalancingEnabled(var4.isLoadBalancingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("ServerAffinityEnabled")) {
                  var3.setServerAffinityEnabled(var4.isServerAffinityEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("UserTransactionsEnabled")) {
                  var3.setUserTransactionsEnabled(var4.isUserTransactionsEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("XAConnectionFactoryEnabled")) {
                  var3.setXAConnectionFactoryEnabled(var4.isXAConnectionFactoryEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("XAServerEnabled")) {
                  var3.setXAServerEnabled(var4.isXAServerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
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
            JMSConnectionFactoryMBeanImpl var5 = (JMSConnectionFactoryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("AcknowledgePolicy")) && this.bean.isAcknowledgePolicySet()) {
               var5.setAcknowledgePolicy(this.bean.getAcknowledgePolicy());
            }

            if (var2 && (var3 == null || !var3.contains("AllowCloseInOnMessage")) && this.bean.isAllowCloseInOnMessageSet()) {
               var5.setAllowCloseInOnMessage(this.bean.getAllowCloseInOnMessage());
            }

            if (var2 && (var3 == null || !var3.contains("ClientId")) && this.bean.isClientIdSet()) {
               var5.setClientId(this.bean.getClientId());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultDeliveryMode")) && this.bean.isDefaultDeliveryModeSet()) {
               var5.setDefaultDeliveryMode(this.bean.getDefaultDeliveryMode());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultPriority")) && this.bean.isDefaultPrioritySet()) {
               var5.setDefaultPriority(this.bean.getDefaultPriority());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultRedeliveryDelay")) && this.bean.isDefaultRedeliveryDelaySet()) {
               var5.setDefaultRedeliveryDelay(this.bean.getDefaultRedeliveryDelay());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultTimeToDeliver")) && this.bean.isDefaultTimeToDeliverSet()) {
               var5.setDefaultTimeToDeliver(this.bean.getDefaultTimeToDeliver());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultTimeToLive")) && this.bean.isDefaultTimeToLiveSet()) {
               var5.setDefaultTimeToLive(this.bean.getDefaultTimeToLive());
            }

            if (var2 && (var3 == null || !var3.contains("FlowInterval")) && this.bean.isFlowIntervalSet()) {
               var5.setFlowInterval(this.bean.getFlowInterval());
            }

            if (var2 && (var3 == null || !var3.contains("FlowMaximum")) && this.bean.isFlowMaximumSet()) {
               var5.setFlowMaximum(this.bean.getFlowMaximum());
            }

            if (var2 && (var3 == null || !var3.contains("FlowMinimum")) && this.bean.isFlowMinimumSet()) {
               var5.setFlowMinimum(this.bean.getFlowMinimum());
            }

            if (var2 && (var3 == null || !var3.contains("FlowSteps")) && this.bean.isFlowStepsSet()) {
               var5.setFlowSteps(this.bean.getFlowSteps());
            }

            if (var2 && (var3 == null || !var3.contains("JNDIName")) && this.bean.isJNDINameSet()) {
               var5.setJNDIName(this.bean.getJNDIName());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesMaximum")) && this.bean.isMessagesMaximumSet()) {
               var5.setMessagesMaximum(this.bean.getMessagesMaximum());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
            }

            if (var2 && (var3 == null || !var3.contains("OverrunPolicy")) && this.bean.isOverrunPolicySet()) {
               var5.setOverrunPolicy(this.bean.getOverrunPolicy());
            }

            if (var2 && (var3 == null || !var3.contains("SendTimeout")) && this.bean.isSendTimeoutSet()) {
               var5.setSendTimeout(this.bean.getSendTimeout());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("TransactionTimeout")) && this.bean.isTransactionTimeoutSet()) {
               var5.setTransactionTimeout(this.bean.getTransactionTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("FlowControlEnabled")) && this.bean.isFlowControlEnabledSet()) {
               var5.setFlowControlEnabled(this.bean.isFlowControlEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("LoadBalancingEnabled")) && this.bean.isLoadBalancingEnabledSet()) {
               var5.setLoadBalancingEnabled(this.bean.isLoadBalancingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("ServerAffinityEnabled")) && this.bean.isServerAffinityEnabledSet()) {
               var5.setServerAffinityEnabled(this.bean.isServerAffinityEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("UserTransactionsEnabled")) && this.bean.isUserTransactionsEnabledSet()) {
               var5.setUserTransactionsEnabled(this.bean.isUserTransactionsEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("XAConnectionFactoryEnabled")) && this.bean.isXAConnectionFactoryEnabledSet()) {
               var5.setXAConnectionFactoryEnabled(this.bean.isXAConnectionFactoryEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("XAServerEnabled")) && this.bean.isXAServerEnabledSet()) {
               var5.setXAServerEnabled(this.bean.isXAServerEnabled());
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
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
