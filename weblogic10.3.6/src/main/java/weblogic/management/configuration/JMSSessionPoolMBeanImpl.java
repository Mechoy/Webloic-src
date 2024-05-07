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
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.jms.module.validators.JMSModuleValidator;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSSessionPool;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class JMSSessionPoolMBeanImpl extends ConfigurationMBeanImpl implements JMSSessionPoolMBean, Serializable {
   private String _AcknowledgeMode;
   private JMSConnectionConsumerMBean[] _ConnectionConsumers;
   private String _ConnectionFactory;
   private JMSConnectionConsumerMBean[] _JMSConnectionConsumers;
   private String _ListenerClass;
   private String _Name;
   private int _SessionsMaximum;
   private boolean _Transacted;
   private JMSSessionPool _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSSessionPoolMBeanImpl() {
      try {
         this._customizer = new JMSSessionPool(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSSessionPoolMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSSessionPool(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public JMSConnectionConsumerMBean[] getConnectionConsumers() {
      return this._customizer.getConnectionConsumers();
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

   public boolean isConnectionConsumersSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setConnectionConsumers(JMSConnectionConsumerMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new JMSConnectionConsumerMBeanImpl[0] : var1;
      this._ConnectionConsumers = (JMSConnectionConsumerMBean[])var2;
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

   public boolean addConnectionConsumer(JMSConnectionConsumerMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         JMSConnectionConsumerMBean[] var2 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._extendArray(this.getConnectionConsumers(), JMSConnectionConsumerMBean.class, var1));

         try {
            this.setConnectionConsumers(var2);
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

   public boolean removeConnectionConsumer(JMSConnectionConsumerMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSConnectionConsumerMBean[] var2 = this.getConnectionConsumers();
      JMSConnectionConsumerMBean[] var3 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._removeElement(var2, JMSConnectionConsumerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setConnectionConsumers(var3);
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

   public void addJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         JMSConnectionConsumerMBean[] var2;
         if (this._isSet(8)) {
            var2 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._extendArray(this.getJMSConnectionConsumers(), JMSConnectionConsumerMBean.class, var1));
         } else {
            var2 = new JMSConnectionConsumerMBean[]{var1};
         }

         try {
            this.setJMSConnectionConsumers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSConnectionConsumerMBean[] getJMSConnectionConsumers() {
      return this._JMSConnectionConsumers;
   }

   public boolean isJMSConnectionConsumersSet() {
      return this._isSet(8);
   }

   public void removeJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      this.destroyJMSConnectionConsumer(var1);
   }

   public void setJMSConnectionConsumers(JMSConnectionConsumerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSConnectionConsumerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSConnectionConsumerMBean[] var5 = this._JMSConnectionConsumers;
      this._JMSConnectionConsumers = (JMSConnectionConsumerMBean[])var4;
      this._postSet(8, var5, var4);
   }

   public JMSConnectionConsumerMBean createJMSConnectionConsumer(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSConnectionConsumerMBeanImpl var2 = new JMSConnectionConsumerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSConnectionConsumer(var2);
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

   public void destroyJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         JMSConnectionConsumerMBean[] var2 = this.getJMSConnectionConsumers();
         JMSConnectionConsumerMBean[] var3 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._removeElement(var2, JMSConnectionConsumerMBean.class, var1));
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
               this.setJMSConnectionConsumers(var3);
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

   public JMSConnectionConsumerMBean lookupJMSConnectionConsumer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSConnectionConsumers).iterator();

      JMSConnectionConsumerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSConnectionConsumerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public String getConnectionFactory() {
      return this._ConnectionFactory;
   }

   public boolean isConnectionFactorySet() {
      return this._isSet(9);
   }

   public void setConnectionFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnectionFactory;
      this._ConnectionFactory = var1;
      this._postSet(9, var2, var1);
   }

   public JMSConnectionConsumerMBean createJMSConnectionConsumer(String var1, JMSConnectionConsumerMBean var2) {
      return this._customizer.createJMSConnectionConsumer(var1, var2);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void destroyJMSConnectionConsumer(String var1, JMSConnectionConsumerMBean var2) {
      this._customizer.destroyJMSConnectionConsumer(var1, var2);
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getListenerClass() {
      return this._ListenerClass;
   }

   public boolean isListenerClassSet() {
      return this._isSet(10);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setListenerClass(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ListenerClass;
      this._ListenerClass = var1;
      this._postSet(10, var2, var1);
   }

   public String getAcknowledgeMode() {
      return this._AcknowledgeMode;
   }

   public boolean isAcknowledgeModeSet() {
      return this._isSet(11);
   }

   public void setAcknowledgeMode(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Auto", "Client", "Dups-Ok", "None"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AcknowledgeMode", var1, var2);
      String var3 = this._AcknowledgeMode;
      this._AcknowledgeMode = var1;
      this._postSet(11, var3, var1);
   }

   public int getSessionsMaximum() {
      return this._SessionsMaximum;
   }

   public boolean isSessionsMaximumSet() {
      return this._isSet(12);
   }

   public void setSessionsMaximum(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SessionsMaximum", (long)var1, -1L, 2147483647L);
      JMSModuleValidator.validateSessionPoolSessionsMaximum(var1);
      int var2 = this._SessionsMaximum;
      this._SessionsMaximum = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isTransacted() {
      return this._Transacted;
   }

   public boolean isTransactedSet() {
      return this._isSet(13);
   }

   public void setTransacted(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Transacted;
      this._Transacted = var1;
      this._postSet(13, var2, var1);
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._AcknowledgeMode = "Auto";
               if (var2) {
                  break;
               }
            case 7:
               this._ConnectionConsumers = new JMSConnectionConsumerMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._ConnectionFactory = null;
               if (var2) {
                  break;
               }
            case 8:
               this._JMSConnectionConsumers = new JMSConnectionConsumerMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._ListenerClass = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._SessionsMaximum = -1;
               if (var2) {
                  break;
               }
            case 13:
               this._Transacted = false;
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
      return "JMSSessionPool";
   }

   public void putValue(String var1, Object var2) {
      String var6;
      if (var1.equals("AcknowledgeMode")) {
         var6 = this._AcknowledgeMode;
         this._AcknowledgeMode = (String)var2;
         this._postSet(11, var6, this._AcknowledgeMode);
      } else {
         JMSConnectionConsumerMBean[] var7;
         if (var1.equals("ConnectionConsumers")) {
            var7 = this._ConnectionConsumers;
            this._ConnectionConsumers = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])var2);
            this._postSet(7, var7, this._ConnectionConsumers);
         } else if (var1.equals("ConnectionFactory")) {
            var6 = this._ConnectionFactory;
            this._ConnectionFactory = (String)var2;
            this._postSet(9, var6, this._ConnectionFactory);
         } else if (var1.equals("JMSConnectionConsumers")) {
            var7 = this._JMSConnectionConsumers;
            this._JMSConnectionConsumers = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])var2);
            this._postSet(8, var7, this._JMSConnectionConsumers);
         } else if (var1.equals("ListenerClass")) {
            var6 = this._ListenerClass;
            this._ListenerClass = (String)var2;
            this._postSet(10, var6, this._ListenerClass);
         } else if (var1.equals("Name")) {
            var6 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var6, this._Name);
         } else if (var1.equals("SessionsMaximum")) {
            int var5 = this._SessionsMaximum;
            this._SessionsMaximum = (Integer)var2;
            this._postSet(12, var5, this._SessionsMaximum);
         } else if (var1.equals("Transacted")) {
            boolean var4 = this._Transacted;
            this._Transacted = (Boolean)var2;
            this._postSet(13, var4, this._Transacted);
         } else if (var1.equals("customizer")) {
            JMSSessionPool var3 = this._customizer;
            this._customizer = (JMSSessionPool)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcknowledgeMode")) {
         return this._AcknowledgeMode;
      } else if (var1.equals("ConnectionConsumers")) {
         return this._ConnectionConsumers;
      } else if (var1.equals("ConnectionFactory")) {
         return this._ConnectionFactory;
      } else if (var1.equals("JMSConnectionConsumers")) {
         return this._JMSConnectionConsumers;
      } else if (var1.equals("ListenerClass")) {
         return this._ListenerClass;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("SessionsMaximum")) {
         return new Integer(this._SessionsMaximum);
      } else if (var1.equals("Transacted")) {
         return new Boolean(this._Transacted);
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
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 15:
            case 17:
            case 20:
            case 21:
            case 22:
            default:
               break;
            case 10:
               if (var1.equals("transacted")) {
                  return 13;
               }
               break;
            case 14:
               if (var1.equals("listener-class")) {
                  return 10;
               }
               break;
            case 16:
               if (var1.equals("acknowledge-mode")) {
                  return 11;
               }

               if (var1.equals("sessions-maximum")) {
                  return 12;
               }
               break;
            case 18:
               if (var1.equals("connection-factory")) {
                  return 9;
               }
               break;
            case 19:
               if (var1.equals("connection-consumer")) {
                  return 7;
               }
               break;
            case 23:
               if (var1.equals("jms-connection-consumer")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 8:
               return new JMSConnectionConsumerMBeanImpl.SchemaHelper2();
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
               return "connection-consumer";
            case 8:
               return "jms-connection-consumer";
            case 9:
               return "connection-factory";
            case 10:
               return "listener-class";
            case 11:
               return "acknowledge-mode";
            case 12:
               return "sessions-maximum";
            case 13:
               return "transacted";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 8:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private JMSSessionPoolMBeanImpl bean;

      protected Helper(JMSSessionPoolMBeanImpl var1) {
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
               return "ConnectionConsumers";
            case 8:
               return "JMSConnectionConsumers";
            case 9:
               return "ConnectionFactory";
            case 10:
               return "ListenerClass";
            case 11:
               return "AcknowledgeMode";
            case 12:
               return "SessionsMaximum";
            case 13:
               return "Transacted";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcknowledgeMode")) {
            return 11;
         } else if (var1.equals("ConnectionConsumers")) {
            return 7;
         } else if (var1.equals("ConnectionFactory")) {
            return 9;
         } else if (var1.equals("JMSConnectionConsumers")) {
            return 8;
         } else if (var1.equals("ListenerClass")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SessionsMaximum")) {
            return 12;
         } else {
            return var1.equals("Transacted") ? 13 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getJMSConnectionConsumers()));
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
            if (this.bean.isAcknowledgeModeSet()) {
               var2.append("AcknowledgeMode");
               var2.append(String.valueOf(this.bean.getAcknowledgeMode()));
            }

            if (this.bean.isConnectionConsumersSet()) {
               var2.append("ConnectionConsumers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getConnectionConsumers())));
            }

            if (this.bean.isConnectionFactorySet()) {
               var2.append("ConnectionFactory");
               var2.append(String.valueOf(this.bean.getConnectionFactory()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getJMSConnectionConsumers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSConnectionConsumers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isListenerClassSet()) {
               var2.append("ListenerClass");
               var2.append(String.valueOf(this.bean.getListenerClass()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSessionsMaximumSet()) {
               var2.append("SessionsMaximum");
               var2.append(String.valueOf(this.bean.getSessionsMaximum()));
            }

            if (this.bean.isTransactedSet()) {
               var2.append("Transacted");
               var2.append(String.valueOf(this.bean.isTransacted()));
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
            JMSSessionPoolMBeanImpl var2 = (JMSSessionPoolMBeanImpl)var1;
            this.computeDiff("AcknowledgeMode", this.bean.getAcknowledgeMode(), var2.getAcknowledgeMode(), false);
            this.computeDiff("ConnectionFactory", this.bean.getConnectionFactory(), var2.getConnectionFactory(), false);
            this.computeChildDiff("JMSConnectionConsumers", this.bean.getJMSConnectionConsumers(), var2.getJMSConnectionConsumers(), false);
            this.computeDiff("ListenerClass", this.bean.getListenerClass(), var2.getListenerClass(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("SessionsMaximum", this.bean.getSessionsMaximum(), var2.getSessionsMaximum(), true);
            this.computeDiff("Transacted", this.bean.isTransacted(), var2.isTransacted(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSSessionPoolMBeanImpl var3 = (JMSSessionPoolMBeanImpl)var1.getSourceBean();
            JMSSessionPoolMBeanImpl var4 = (JMSSessionPoolMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcknowledgeMode")) {
                  var3.setAcknowledgeMode(var4.getAcknowledgeMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (!var5.equals("ConnectionConsumers")) {
                  if (var5.equals("ConnectionFactory")) {
                     var3.setConnectionFactory(var4.getConnectionFactory());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("JMSConnectionConsumers")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addJMSConnectionConsumer((JMSConnectionConsumerMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeJMSConnectionConsumer((JMSConnectionConsumerMBean)var2.getRemovedObject());
                     }

                     if (var3.getJMSConnectionConsumers() == null || var3.getJMSConnectionConsumers().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                     }
                  } else if (var5.equals("ListenerClass")) {
                     var3.setListenerClass(var4.getListenerClass());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("SessionsMaximum")) {
                     var3.setSessionsMaximum(var4.getSessionsMaximum());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("Transacted")) {
                     var3.setTransacted(var4.isTransacted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            JMSSessionPoolMBeanImpl var5 = (JMSSessionPoolMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AcknowledgeMode")) && this.bean.isAcknowledgeModeSet()) {
               var5.setAcknowledgeMode(this.bean.getAcknowledgeMode());
            }

            if ((var3 == null || !var3.contains("ConnectionFactory")) && this.bean.isConnectionFactorySet()) {
               var5.setConnectionFactory(this.bean.getConnectionFactory());
            }

            if ((var3 == null || !var3.contains("JMSConnectionConsumers")) && this.bean.isJMSConnectionConsumersSet() && !var5._isSet(8)) {
               JMSConnectionConsumerMBean[] var6 = this.bean.getJMSConnectionConsumers();
               JMSConnectionConsumerMBean[] var7 = new JMSConnectionConsumerMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (JMSConnectionConsumerMBean)((JMSConnectionConsumerMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setJMSConnectionConsumers(var7);
            }

            if ((var3 == null || !var3.contains("ListenerClass")) && this.bean.isListenerClassSet()) {
               var5.setListenerClass(this.bean.getListenerClass());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SessionsMaximum")) && this.bean.isSessionsMaximumSet()) {
               var5.setSessionsMaximum(this.bean.getSessionsMaximum());
            }

            if ((var3 == null || !var3.contains("Transacted")) && this.bean.isTransactedSet()) {
               var5.setTransacted(this.bean.isTransacted());
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
         this.inferSubTree(this.bean.getConnectionConsumers(), var1, var2);
         this.inferSubTree(this.bean.getJMSConnectionConsumers(), var1, var2);
      }
   }
}
