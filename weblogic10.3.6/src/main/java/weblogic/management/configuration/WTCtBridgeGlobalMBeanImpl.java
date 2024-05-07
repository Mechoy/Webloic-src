package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WTCtBridgeGlobalMBeanImpl extends ConfigurationMBeanImpl implements WTCtBridgeGlobalMBean, Serializable {
   private String _AllowNonStandardTypes;
   private String _DefaultReplyDeliveryMode;
   private String _DeliveryModeOverride;
   private String _JmsFactory;
   private String _JmsToTuxPriorityMap;
   private String _JndiFactory;
   private int _Retries;
   private int _RetryDelay;
   private int _Timeout;
   private String _Transactional;
   private String _TuxErrorQueue;
   private String _TuxFactory;
   private String _TuxToJmsPriorityMap;
   private String _UserId;
   private String _WlsErrorDestination;
   private static SchemaHelper2 _schemaHelper;

   public WTCtBridgeGlobalMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCtBridgeGlobalMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setTransactional(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Yes", "No"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("Transactional", var1, var2);
      String var3 = this._Transactional;
      this._Transactional = var1;
      this._postSet(7, var3, var1);
   }

   public String getTransactional() {
      return this._Transactional;
   }

   public boolean isTransactionalSet() {
      return this._isSet(7);
   }

   public void setTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("Timeout", (long)var1, 0L, 2147483647L);
      int var2 = this._Timeout;
      this._Timeout = var1;
      this._postSet(8, var2, var1);
   }

   public int getTimeout() {
      return this._Timeout;
   }

   public boolean isTimeoutSet() {
      return this._isSet(8);
   }

   public void setRetries(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("Retries", (long)var1, 0L, 2147483647L);
      int var2 = this._Retries;
      this._Retries = var1;
      this._postSet(9, var2, var1);
   }

   public int getRetries() {
      return this._Retries;
   }

   public boolean isRetriesSet() {
      return this._isSet(9);
   }

   public void setRetryDelay(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RetryDelay", (long)var1, 0L, 2147483647L);
      int var2 = this._RetryDelay;
      this._RetryDelay = var1;
      this._postSet(10, var2, var1);
   }

   public int getRetryDelay() {
      return this._RetryDelay;
   }

   public boolean isRetryDelaySet() {
      return this._isSet(10);
   }

   public void setWlsErrorDestination(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WlsErrorDestination;
      this._WlsErrorDestination = var1;
      this._postSet(11, var2, var1);
   }

   public String getWlsErrorDestination() {
      return this._WlsErrorDestination;
   }

   public boolean isWlsErrorDestinationSet() {
      return this._isSet(11);
   }

   public void setTuxErrorQueue(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TuxErrorQueue;
      this._TuxErrorQueue = var1;
      this._postSet(12, var2, var1);
   }

   public String getTuxErrorQueue() {
      return this._TuxErrorQueue;
   }

   public boolean isTuxErrorQueueSet() {
      return this._isSet(12);
   }

   public void setDeliveryModeOverride(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"PERSIST", "NONPERSIST"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DeliveryModeOverride", var1, var2);
      String var3 = this._DeliveryModeOverride;
      this._DeliveryModeOverride = var1;
      this._postSet(13, var3, var1);
   }

   public String getDeliveryModeOverride() {
      return this._DeliveryModeOverride;
   }

   public boolean isDeliveryModeOverrideSet() {
      return this._isSet(13);
   }

   public void setDefaultReplyDeliveryMode(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"PERSIST", "NONPERSIST", "DEFAULT"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultReplyDeliveryMode", var1, var2);
      String var3 = this._DefaultReplyDeliveryMode;
      this._DefaultReplyDeliveryMode = var1;
      this._postSet(14, var3, var1);
   }

   public String getDefaultReplyDeliveryMode() {
      return this._DefaultReplyDeliveryMode;
   }

   public boolean isDefaultReplyDeliveryModeSet() {
      return this._isSet(14);
   }

   public void setUserId(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserId;
      this._UserId = var1;
      this._postSet(15, var2, var1);
   }

   public String getUserId() {
      return this._UserId;
   }

   public boolean isUserIdSet() {
      return this._isSet(15);
   }

   public void setAllowNonStandardTypes(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Yes", "No"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AllowNonStandardTypes", var1, var2);
      String var3 = this._AllowNonStandardTypes;
      this._AllowNonStandardTypes = var1;
      this._postSet(16, var3, var1);
   }

   public String getAllowNonStandardTypes() {
      return this._AllowNonStandardTypes;
   }

   public boolean isAllowNonStandardTypesSet() {
      return this._isSet(16);
   }

   public void setJndiFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("JndiFactory", var1);
      String var2 = this._JndiFactory;
      this._JndiFactory = var1;
      this._postSet(17, var2, var1);
   }

   public String getJndiFactory() {
      return this._JndiFactory;
   }

   public boolean isJndiFactorySet() {
      return this._isSet(17);
   }

   public void setJmsFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("JmsFactory", var1);
      String var2 = this._JmsFactory;
      this._JmsFactory = var1;
      this._postSet(18, var2, var1);
   }

   public String getJmsFactory() {
      return this._JmsFactory;
   }

   public boolean isJmsFactorySet() {
      return this._isSet(18);
   }

   public void setTuxFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("TuxFactory", var1);
      String var2 = this._TuxFactory;
      this._TuxFactory = var1;
      this._postSet(19, var2, var1);
   }

   public String getTuxFactory() {
      return this._TuxFactory;
   }

   public boolean isTuxFactorySet() {
      return this._isSet(19);
   }

   public void setJmsToTuxPriorityMap(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JmsToTuxPriorityMap;
      this._JmsToTuxPriorityMap = var1;
      this._postSet(20, var2, var1);
   }

   public String getJmsToTuxPriorityMap() {
      return this._JmsToTuxPriorityMap;
   }

   public boolean isJmsToTuxPriorityMapSet() {
      return this._isSet(20);
   }

   public void setTuxToJmsPriorityMap(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TuxToJmsPriorityMap;
      this._TuxToJmsPriorityMap = var1;
      this._postSet(21, var2, var1);
   }

   public String getTuxToJmsPriorityMap() {
      return this._TuxToJmsPriorityMap;
   }

   public boolean isTuxToJmsPriorityMapSet() {
      return this._isSet(21);
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
         var1 = 16;
      }

      try {
         switch (var1) {
            case 16:
               this._AllowNonStandardTypes = "NO";
               if (var2) {
                  break;
               }
            case 14:
               this._DefaultReplyDeliveryMode = "DEFAULT";
               if (var2) {
                  break;
               }
            case 13:
               this._DeliveryModeOverride = "NONPERSIST";
               if (var2) {
                  break;
               }
            case 18:
               this._JmsFactory = "weblogic.jms.XAConnectionFactory";
               if (var2) {
                  break;
               }
            case 20:
               this._JmsToTuxPriorityMap = null;
               if (var2) {
                  break;
               }
            case 17:
               this._JndiFactory = "weblogic.jndi.WLInitialContextFactory";
               if (var2) {
                  break;
               }
            case 9:
               this._Retries = 0;
               if (var2) {
                  break;
               }
            case 10:
               this._RetryDelay = 10;
               if (var2) {
                  break;
               }
            case 8:
               this._Timeout = 60;
               if (var2) {
                  break;
               }
            case 7:
               this._Transactional = "NO";
               if (var2) {
                  break;
               }
            case 12:
               this._TuxErrorQueue = null;
               if (var2) {
                  break;
               }
            case 19:
               this._TuxFactory = "tuxedo.services.TuxedoConnection";
               if (var2) {
                  break;
               }
            case 21:
               this._TuxToJmsPriorityMap = null;
               if (var2) {
                  break;
               }
            case 15:
               this._UserId = null;
               if (var2) {
                  break;
               }
            case 11:
               this._WlsErrorDestination = null;
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
      return "WTCtBridgeGlobal";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("AllowNonStandardTypes")) {
         var3 = this._AllowNonStandardTypes;
         this._AllowNonStandardTypes = (String)var2;
         this._postSet(16, var3, this._AllowNonStandardTypes);
      } else if (var1.equals("DefaultReplyDeliveryMode")) {
         var3 = this._DefaultReplyDeliveryMode;
         this._DefaultReplyDeliveryMode = (String)var2;
         this._postSet(14, var3, this._DefaultReplyDeliveryMode);
      } else if (var1.equals("DeliveryModeOverride")) {
         var3 = this._DeliveryModeOverride;
         this._DeliveryModeOverride = (String)var2;
         this._postSet(13, var3, this._DeliveryModeOverride);
      } else if (var1.equals("JmsFactory")) {
         var3 = this._JmsFactory;
         this._JmsFactory = (String)var2;
         this._postSet(18, var3, this._JmsFactory);
      } else if (var1.equals("JmsToTuxPriorityMap")) {
         var3 = this._JmsToTuxPriorityMap;
         this._JmsToTuxPriorityMap = (String)var2;
         this._postSet(20, var3, this._JmsToTuxPriorityMap);
      } else if (var1.equals("JndiFactory")) {
         var3 = this._JndiFactory;
         this._JndiFactory = (String)var2;
         this._postSet(17, var3, this._JndiFactory);
      } else {
         int var4;
         if (var1.equals("Retries")) {
            var4 = this._Retries;
            this._Retries = (Integer)var2;
            this._postSet(9, var4, this._Retries);
         } else if (var1.equals("RetryDelay")) {
            var4 = this._RetryDelay;
            this._RetryDelay = (Integer)var2;
            this._postSet(10, var4, this._RetryDelay);
         } else if (var1.equals("Timeout")) {
            var4 = this._Timeout;
            this._Timeout = (Integer)var2;
            this._postSet(8, var4, this._Timeout);
         } else if (var1.equals("Transactional")) {
            var3 = this._Transactional;
            this._Transactional = (String)var2;
            this._postSet(7, var3, this._Transactional);
         } else if (var1.equals("TuxErrorQueue")) {
            var3 = this._TuxErrorQueue;
            this._TuxErrorQueue = (String)var2;
            this._postSet(12, var3, this._TuxErrorQueue);
         } else if (var1.equals("TuxFactory")) {
            var3 = this._TuxFactory;
            this._TuxFactory = (String)var2;
            this._postSet(19, var3, this._TuxFactory);
         } else if (var1.equals("TuxToJmsPriorityMap")) {
            var3 = this._TuxToJmsPriorityMap;
            this._TuxToJmsPriorityMap = (String)var2;
            this._postSet(21, var3, this._TuxToJmsPriorityMap);
         } else if (var1.equals("UserId")) {
            var3 = this._UserId;
            this._UserId = (String)var2;
            this._postSet(15, var3, this._UserId);
         } else if (var1.equals("WlsErrorDestination")) {
            var3 = this._WlsErrorDestination;
            this._WlsErrorDestination = (String)var2;
            this._postSet(11, var3, this._WlsErrorDestination);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllowNonStandardTypes")) {
         return this._AllowNonStandardTypes;
      } else if (var1.equals("DefaultReplyDeliveryMode")) {
         return this._DefaultReplyDeliveryMode;
      } else if (var1.equals("DeliveryModeOverride")) {
         return this._DeliveryModeOverride;
      } else if (var1.equals("JmsFactory")) {
         return this._JmsFactory;
      } else if (var1.equals("JmsToTuxPriorityMap")) {
         return this._JmsToTuxPriorityMap;
      } else if (var1.equals("JndiFactory")) {
         return this._JndiFactory;
      } else if (var1.equals("Retries")) {
         return new Integer(this._Retries);
      } else if (var1.equals("RetryDelay")) {
         return new Integer(this._RetryDelay);
      } else if (var1.equals("Timeout")) {
         return new Integer(this._Timeout);
      } else if (var1.equals("Transactional")) {
         return this._Transactional;
      } else if (var1.equals("TuxErrorQueue")) {
         return this._TuxErrorQueue;
      } else if (var1.equals("TuxFactory")) {
         return this._TuxFactory;
      } else if (var1.equals("TuxToJmsPriorityMap")) {
         return this._TuxToJmsPriorityMap;
      } else if (var1.equals("UserId")) {
         return this._UserId;
      } else {
         return var1.equals("WlsErrorDestination") ? this._WlsErrorDestination : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      String[] var0;
      try {
         var0 = new String[]{"Yes", "No"};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("AllowNonStandardTypes", "NO", var0);
      } catch (IllegalArgumentException var5) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property AllowNonStandardTypes in WTCtBridgeGlobalMBean" + var5.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("JmsFactory", "weblogic.jms.XAConnectionFactory");
      } catch (IllegalArgumentException var4) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property JmsFactory in WTCtBridgeGlobalMBean" + var4.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("JndiFactory", "weblogic.jndi.WLInitialContextFactory");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property JndiFactory in WTCtBridgeGlobalMBean" + var3.getMessage());
      }

      try {
         var0 = new String[]{"Yes", "No"};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("Transactional", "NO", var0);
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property Transactional in WTCtBridgeGlobalMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("TuxFactory", "tuxedo.services.TuxedoConnection");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property TuxFactory in WTCtBridgeGlobalMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("retries")) {
                  return 9;
               }

               if (var1.equals("timeout")) {
                  return 8;
               }

               if (var1.equals("user-id")) {
                  return 15;
               }
            case 8:
            case 9:
            case 10:
            case 14:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 25:
            case 26:
            default:
               break;
            case 11:
               if (var1.equals("jms-factory")) {
                  return 18;
               }

               if (var1.equals("retry-delay")) {
                  return 10;
               }

               if (var1.equals("tux-factory")) {
                  return 19;
               }
               break;
            case 12:
               if (var1.equals("jndi-factory")) {
                  return 17;
               }
               break;
            case 13:
               if (var1.equals("transactional")) {
                  return 7;
               }
               break;
            case 15:
               if (var1.equals("tux-error-queue")) {
                  return 12;
               }
               break;
            case 21:
               if (var1.equals("wls-error-destination")) {
                  return 11;
               }
               break;
            case 22:
               if (var1.equals("delivery-mode-override")) {
                  return 13;
               }
               break;
            case 23:
               if (var1.equals("jms-to-tux-priority-map")) {
                  return 20;
               }

               if (var1.equals("tux-to-jms-priority-map")) {
                  return 21;
               }
               break;
            case 24:
               if (var1.equals("allow-non-standard-types")) {
                  return 16;
               }
               break;
            case 27:
               if (var1.equals("default-reply-delivery-mode")) {
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
            case 7:
               return "transactional";
            case 8:
               return "timeout";
            case 9:
               return "retries";
            case 10:
               return "retry-delay";
            case 11:
               return "wls-error-destination";
            case 12:
               return "tux-error-queue";
            case 13:
               return "delivery-mode-override";
            case 14:
               return "default-reply-delivery-mode";
            case 15:
               return "user-id";
            case 16:
               return "allow-non-standard-types";
            case 17:
               return "jndi-factory";
            case 18:
               return "jms-factory";
            case 19:
               return "tux-factory";
            case 20:
               return "jms-to-tux-priority-map";
            case 21:
               return "tux-to-jms-priority-map";
            default:
               return super.getElementName(var1);
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WTCtBridgeGlobalMBeanImpl bean;

      protected Helper(WTCtBridgeGlobalMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Transactional";
            case 8:
               return "Timeout";
            case 9:
               return "Retries";
            case 10:
               return "RetryDelay";
            case 11:
               return "WlsErrorDestination";
            case 12:
               return "TuxErrorQueue";
            case 13:
               return "DeliveryModeOverride";
            case 14:
               return "DefaultReplyDeliveryMode";
            case 15:
               return "UserId";
            case 16:
               return "AllowNonStandardTypes";
            case 17:
               return "JndiFactory";
            case 18:
               return "JmsFactory";
            case 19:
               return "TuxFactory";
            case 20:
               return "JmsToTuxPriorityMap";
            case 21:
               return "TuxToJmsPriorityMap";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AllowNonStandardTypes")) {
            return 16;
         } else if (var1.equals("DefaultReplyDeliveryMode")) {
            return 14;
         } else if (var1.equals("DeliveryModeOverride")) {
            return 13;
         } else if (var1.equals("JmsFactory")) {
            return 18;
         } else if (var1.equals("JmsToTuxPriorityMap")) {
            return 20;
         } else if (var1.equals("JndiFactory")) {
            return 17;
         } else if (var1.equals("Retries")) {
            return 9;
         } else if (var1.equals("RetryDelay")) {
            return 10;
         } else if (var1.equals("Timeout")) {
            return 8;
         } else if (var1.equals("Transactional")) {
            return 7;
         } else if (var1.equals("TuxErrorQueue")) {
            return 12;
         } else if (var1.equals("TuxFactory")) {
            return 19;
         } else if (var1.equals("TuxToJmsPriorityMap")) {
            return 21;
         } else if (var1.equals("UserId")) {
            return 15;
         } else {
            return var1.equals("WlsErrorDestination") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isAllowNonStandardTypesSet()) {
               var2.append("AllowNonStandardTypes");
               var2.append(String.valueOf(this.bean.getAllowNonStandardTypes()));
            }

            if (this.bean.isDefaultReplyDeliveryModeSet()) {
               var2.append("DefaultReplyDeliveryMode");
               var2.append(String.valueOf(this.bean.getDefaultReplyDeliveryMode()));
            }

            if (this.bean.isDeliveryModeOverrideSet()) {
               var2.append("DeliveryModeOverride");
               var2.append(String.valueOf(this.bean.getDeliveryModeOverride()));
            }

            if (this.bean.isJmsFactorySet()) {
               var2.append("JmsFactory");
               var2.append(String.valueOf(this.bean.getJmsFactory()));
            }

            if (this.bean.isJmsToTuxPriorityMapSet()) {
               var2.append("JmsToTuxPriorityMap");
               var2.append(String.valueOf(this.bean.getJmsToTuxPriorityMap()));
            }

            if (this.bean.isJndiFactorySet()) {
               var2.append("JndiFactory");
               var2.append(String.valueOf(this.bean.getJndiFactory()));
            }

            if (this.bean.isRetriesSet()) {
               var2.append("Retries");
               var2.append(String.valueOf(this.bean.getRetries()));
            }

            if (this.bean.isRetryDelaySet()) {
               var2.append("RetryDelay");
               var2.append(String.valueOf(this.bean.getRetryDelay()));
            }

            if (this.bean.isTimeoutSet()) {
               var2.append("Timeout");
               var2.append(String.valueOf(this.bean.getTimeout()));
            }

            if (this.bean.isTransactionalSet()) {
               var2.append("Transactional");
               var2.append(String.valueOf(this.bean.getTransactional()));
            }

            if (this.bean.isTuxErrorQueueSet()) {
               var2.append("TuxErrorQueue");
               var2.append(String.valueOf(this.bean.getTuxErrorQueue()));
            }

            if (this.bean.isTuxFactorySet()) {
               var2.append("TuxFactory");
               var2.append(String.valueOf(this.bean.getTuxFactory()));
            }

            if (this.bean.isTuxToJmsPriorityMapSet()) {
               var2.append("TuxToJmsPriorityMap");
               var2.append(String.valueOf(this.bean.getTuxToJmsPriorityMap()));
            }

            if (this.bean.isUserIdSet()) {
               var2.append("UserId");
               var2.append(String.valueOf(this.bean.getUserId()));
            }

            if (this.bean.isWlsErrorDestinationSet()) {
               var2.append("WlsErrorDestination");
               var2.append(String.valueOf(this.bean.getWlsErrorDestination()));
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
            WTCtBridgeGlobalMBeanImpl var2 = (WTCtBridgeGlobalMBeanImpl)var1;
            this.computeDiff("AllowNonStandardTypes", this.bean.getAllowNonStandardTypes(), var2.getAllowNonStandardTypes(), true);
            this.computeDiff("DefaultReplyDeliveryMode", this.bean.getDefaultReplyDeliveryMode(), var2.getDefaultReplyDeliveryMode(), true);
            this.computeDiff("DeliveryModeOverride", this.bean.getDeliveryModeOverride(), var2.getDeliveryModeOverride(), true);
            this.computeDiff("JmsFactory", this.bean.getJmsFactory(), var2.getJmsFactory(), true);
            this.computeDiff("JmsToTuxPriorityMap", this.bean.getJmsToTuxPriorityMap(), var2.getJmsToTuxPriorityMap(), true);
            this.computeDiff("JndiFactory", this.bean.getJndiFactory(), var2.getJndiFactory(), true);
            this.computeDiff("Retries", this.bean.getRetries(), var2.getRetries(), true);
            this.computeDiff("RetryDelay", this.bean.getRetryDelay(), var2.getRetryDelay(), true);
            this.computeDiff("Timeout", this.bean.getTimeout(), var2.getTimeout(), true);
            this.computeDiff("Transactional", this.bean.getTransactional(), var2.getTransactional(), true);
            this.computeDiff("TuxErrorQueue", this.bean.getTuxErrorQueue(), var2.getTuxErrorQueue(), true);
            this.computeDiff("TuxFactory", this.bean.getTuxFactory(), var2.getTuxFactory(), true);
            this.computeDiff("TuxToJmsPriorityMap", this.bean.getTuxToJmsPriorityMap(), var2.getTuxToJmsPriorityMap(), true);
            this.computeDiff("UserId", this.bean.getUserId(), var2.getUserId(), true);
            this.computeDiff("WlsErrorDestination", this.bean.getWlsErrorDestination(), var2.getWlsErrorDestination(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCtBridgeGlobalMBeanImpl var3 = (WTCtBridgeGlobalMBeanImpl)var1.getSourceBean();
            WTCtBridgeGlobalMBeanImpl var4 = (WTCtBridgeGlobalMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AllowNonStandardTypes")) {
                  var3.setAllowNonStandardTypes(var4.getAllowNonStandardTypes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("DefaultReplyDeliveryMode")) {
                  var3.setDefaultReplyDeliveryMode(var4.getDefaultReplyDeliveryMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("DeliveryModeOverride")) {
                  var3.setDeliveryModeOverride(var4.getDeliveryModeOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("JmsFactory")) {
                  var3.setJmsFactory(var4.getJmsFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("JmsToTuxPriorityMap")) {
                  var3.setJmsToTuxPriorityMap(var4.getJmsToTuxPriorityMap());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("JndiFactory")) {
                  var3.setJndiFactory(var4.getJndiFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("Retries")) {
                  var3.setRetries(var4.getRetries());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("RetryDelay")) {
                  var3.setRetryDelay(var4.getRetryDelay());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Timeout")) {
                  var3.setTimeout(var4.getTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Transactional")) {
                  var3.setTransactional(var4.getTransactional());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("TuxErrorQueue")) {
                  var3.setTuxErrorQueue(var4.getTuxErrorQueue());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("TuxFactory")) {
                  var3.setTuxFactory(var4.getTuxFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("TuxToJmsPriorityMap")) {
                  var3.setTuxToJmsPriorityMap(var4.getTuxToJmsPriorityMap());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("UserId")) {
                  var3.setUserId(var4.getUserId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("WlsErrorDestination")) {
                  var3.setWlsErrorDestination(var4.getWlsErrorDestination());
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
            WTCtBridgeGlobalMBeanImpl var5 = (WTCtBridgeGlobalMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AllowNonStandardTypes")) && this.bean.isAllowNonStandardTypesSet()) {
               var5.setAllowNonStandardTypes(this.bean.getAllowNonStandardTypes());
            }

            if ((var3 == null || !var3.contains("DefaultReplyDeliveryMode")) && this.bean.isDefaultReplyDeliveryModeSet()) {
               var5.setDefaultReplyDeliveryMode(this.bean.getDefaultReplyDeliveryMode());
            }

            if ((var3 == null || !var3.contains("DeliveryModeOverride")) && this.bean.isDeliveryModeOverrideSet()) {
               var5.setDeliveryModeOverride(this.bean.getDeliveryModeOverride());
            }

            if ((var3 == null || !var3.contains("JmsFactory")) && this.bean.isJmsFactorySet()) {
               var5.setJmsFactory(this.bean.getJmsFactory());
            }

            if ((var3 == null || !var3.contains("JmsToTuxPriorityMap")) && this.bean.isJmsToTuxPriorityMapSet()) {
               var5.setJmsToTuxPriorityMap(this.bean.getJmsToTuxPriorityMap());
            }

            if ((var3 == null || !var3.contains("JndiFactory")) && this.bean.isJndiFactorySet()) {
               var5.setJndiFactory(this.bean.getJndiFactory());
            }

            if ((var3 == null || !var3.contains("Retries")) && this.bean.isRetriesSet()) {
               var5.setRetries(this.bean.getRetries());
            }

            if ((var3 == null || !var3.contains("RetryDelay")) && this.bean.isRetryDelaySet()) {
               var5.setRetryDelay(this.bean.getRetryDelay());
            }

            if ((var3 == null || !var3.contains("Timeout")) && this.bean.isTimeoutSet()) {
               var5.setTimeout(this.bean.getTimeout());
            }

            if ((var3 == null || !var3.contains("Transactional")) && this.bean.isTransactionalSet()) {
               var5.setTransactional(this.bean.getTransactional());
            }

            if ((var3 == null || !var3.contains("TuxErrorQueue")) && this.bean.isTuxErrorQueueSet()) {
               var5.setTuxErrorQueue(this.bean.getTuxErrorQueue());
            }

            if ((var3 == null || !var3.contains("TuxFactory")) && this.bean.isTuxFactorySet()) {
               var5.setTuxFactory(this.bean.getTuxFactory());
            }

            if ((var3 == null || !var3.contains("TuxToJmsPriorityMap")) && this.bean.isTuxToJmsPriorityMapSet()) {
               var5.setTuxToJmsPriorityMap(this.bean.getTuxToJmsPriorityMap());
            }

            if ((var3 == null || !var3.contains("UserId")) && this.bean.isUserIdSet()) {
               var5.setUserId(this.bean.getUserId());
            }

            if ((var3 == null || !var3.contains("WlsErrorDestination")) && this.bean.isWlsErrorDestinationSet()) {
               var5.setWlsErrorDestination(this.bean.getWlsErrorDestination());
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
