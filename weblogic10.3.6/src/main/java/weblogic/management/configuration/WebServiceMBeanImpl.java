package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WebServiceMBeanImpl extends ConfigurationMBeanImpl implements WebServiceMBean, Serializable {
   private String _CallbackQueue;
   private String _CallbackQueueMDBRunAsPrincipalName;
   private String _JmsConnectionFactory;
   private String _MessagingQueue;
   private String _MessagingQueueMDBRunAsPrincipalName;
   private WebServiceBufferingMBean _WebServiceBuffering;
   private WebServicePersistenceMBean _WebServicePersistence;
   private WebServiceReliabilityMBean _WebServiceReliability;
   private static SchemaHelper2 _schemaHelper;

   public WebServiceMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServiceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setJmsConnectionFactory(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JmsConnectionFactory;
      this._JmsConnectionFactory = var1;
      this._postSet(7, var2, var1);
   }

   public String getJmsConnectionFactory() {
      return this._JmsConnectionFactory;
   }

   public boolean isJmsConnectionFactorySet() {
      return this._isSet(7);
   }

   public void setMessagingQueue(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MessagingQueue;
      this._MessagingQueue = var1;
      this._postSet(8, var2, var1);
   }

   public String getMessagingQueue() {
      return this._MessagingQueue;
   }

   public boolean isMessagingQueueSet() {
      return this._isSet(8);
   }

   public void setMessagingQueueMDBRunAsPrincipalName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MessagingQueueMDBRunAsPrincipalName;
      this._MessagingQueueMDBRunAsPrincipalName = var1;
      this._postSet(9, var2, var1);
   }

   public String getMessagingQueueMDBRunAsPrincipalName() {
      return this._MessagingQueueMDBRunAsPrincipalName;
   }

   public boolean isMessagingQueueMDBRunAsPrincipalNameSet() {
      return this._isSet(9);
   }

   public void setCallbackQueue(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CallbackQueue;
      this._CallbackQueue = var1;
      this._postSet(10, var2, var1);
   }

   public String getCallbackQueue() {
      return this._CallbackQueue;
   }

   public boolean isCallbackQueueSet() {
      return this._isSet(10);
   }

   public void setCallbackQueueMDBRunAsPrincipalName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CallbackQueueMDBRunAsPrincipalName;
      this._CallbackQueueMDBRunAsPrincipalName = var1;
      this._postSet(11, var2, var1);
   }

   public String getCallbackQueueMDBRunAsPrincipalName() {
      return this._CallbackQueueMDBRunAsPrincipalName;
   }

   public boolean isCallbackQueueMDBRunAsPrincipalNameSet() {
      return this._isSet(11);
   }

   public WebServicePersistenceMBean getWebServicePersistence() {
      return this._WebServicePersistence;
   }

   public boolean isWebServicePersistenceSet() {
      return this._isSet(12) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServicePersistence());
   }

   public void setWebServicePersistence(WebServicePersistenceMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 12)) {
         this._postCreate(var2);
      }

      WebServicePersistenceMBean var3 = this._WebServicePersistence;
      this._WebServicePersistence = var1;
      this._postSet(12, var3, var1);
   }

   public WebServiceBufferingMBean getWebServiceBuffering() {
      return this._WebServiceBuffering;
   }

   public boolean isWebServiceBufferingSet() {
      return this._isSet(13) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServiceBuffering());
   }

   public void setWebServiceBuffering(WebServiceBufferingMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 13)) {
         this._postCreate(var2);
      }

      WebServiceBufferingMBean var3 = this._WebServiceBuffering;
      this._WebServiceBuffering = var1;
      this._postSet(13, var3, var1);
   }

   public WebServiceReliabilityMBean getWebServiceReliability() {
      return this._WebServiceReliability;
   }

   public boolean isWebServiceReliabilitySet() {
      return this._isSet(14) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServiceReliability());
   }

   public void setWebServiceReliability(WebServiceReliabilityMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 14)) {
         this._postCreate(var2);
      }

      WebServiceReliabilityMBean var3 = this._WebServiceReliability;
      this._WebServiceReliability = var1;
      this._postSet(14, var3, var1);
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
      return super._isAnythingSet() || this.isWebServiceBufferingSet() || this.isWebServicePersistenceSet() || this.isWebServiceReliabilitySet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._CallbackQueue = "weblogic.wsee.DefaultCallbackQueue";
               if (var2) {
                  break;
               }
            case 11:
               this._CallbackQueueMDBRunAsPrincipalName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._JmsConnectionFactory = "weblogic.jms.XAConnectionFactory";
               if (var2) {
                  break;
               }
            case 8:
               this._MessagingQueue = "weblogic.wsee.DefaultQueue";
               if (var2) {
                  break;
               }
            case 9:
               this._MessagingQueueMDBRunAsPrincipalName = null;
               if (var2) {
                  break;
               }
            case 13:
               this._WebServiceBuffering = new WebServiceBufferingMBeanImpl(this, 13);
               this._postCreate((AbstractDescriptorBean)this._WebServiceBuffering);
               if (var2) {
                  break;
               }
            case 12:
               this._WebServicePersistence = new WebServicePersistenceMBeanImpl(this, 12);
               this._postCreate((AbstractDescriptorBean)this._WebServicePersistence);
               if (var2) {
                  break;
               }
            case 14:
               this._WebServiceReliability = new WebServiceReliabilityMBeanImpl(this, 14);
               this._postCreate((AbstractDescriptorBean)this._WebServiceReliability);
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
      return "WebService";
   }

   public void putValue(String var1, Object var2) {
      String var6;
      if (var1.equals("CallbackQueue")) {
         var6 = this._CallbackQueue;
         this._CallbackQueue = (String)var2;
         this._postSet(10, var6, this._CallbackQueue);
      } else if (var1.equals("CallbackQueueMDBRunAsPrincipalName")) {
         var6 = this._CallbackQueueMDBRunAsPrincipalName;
         this._CallbackQueueMDBRunAsPrincipalName = (String)var2;
         this._postSet(11, var6, this._CallbackQueueMDBRunAsPrincipalName);
      } else if (var1.equals("JmsConnectionFactory")) {
         var6 = this._JmsConnectionFactory;
         this._JmsConnectionFactory = (String)var2;
         this._postSet(7, var6, this._JmsConnectionFactory);
      } else if (var1.equals("MessagingQueue")) {
         var6 = this._MessagingQueue;
         this._MessagingQueue = (String)var2;
         this._postSet(8, var6, this._MessagingQueue);
      } else if (var1.equals("MessagingQueueMDBRunAsPrincipalName")) {
         var6 = this._MessagingQueueMDBRunAsPrincipalName;
         this._MessagingQueueMDBRunAsPrincipalName = (String)var2;
         this._postSet(9, var6, this._MessagingQueueMDBRunAsPrincipalName);
      } else if (var1.equals("WebServiceBuffering")) {
         WebServiceBufferingMBean var5 = this._WebServiceBuffering;
         this._WebServiceBuffering = (WebServiceBufferingMBean)var2;
         this._postSet(13, var5, this._WebServiceBuffering);
      } else if (var1.equals("WebServicePersistence")) {
         WebServicePersistenceMBean var4 = this._WebServicePersistence;
         this._WebServicePersistence = (WebServicePersistenceMBean)var2;
         this._postSet(12, var4, this._WebServicePersistence);
      } else if (var1.equals("WebServiceReliability")) {
         WebServiceReliabilityMBean var3 = this._WebServiceReliability;
         this._WebServiceReliability = (WebServiceReliabilityMBean)var2;
         this._postSet(14, var3, this._WebServiceReliability);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CallbackQueue")) {
         return this._CallbackQueue;
      } else if (var1.equals("CallbackQueueMDBRunAsPrincipalName")) {
         return this._CallbackQueueMDBRunAsPrincipalName;
      } else if (var1.equals("JmsConnectionFactory")) {
         return this._JmsConnectionFactory;
      } else if (var1.equals("MessagingQueue")) {
         return this._MessagingQueue;
      } else if (var1.equals("MessagingQueueMDBRunAsPrincipalName")) {
         return this._MessagingQueueMDBRunAsPrincipalName;
      } else if (var1.equals("WebServiceBuffering")) {
         return this._WebServiceBuffering;
      } else if (var1.equals("WebServicePersistence")) {
         return this._WebServicePersistence;
      } else {
         return var1.equals("WebServiceReliability") ? this._WebServiceReliability : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 14:
               if (var1.equals("callback-queue")) {
                  return 10;
               }
               break;
            case 15:
               if (var1.equals("messaging-queue")) {
                  return 8;
               }
               break;
            case 21:
               if (var1.equals("web-service-buffering")) {
                  return 13;
               }
               break;
            case 22:
               if (var1.equals("jms-connection-factory")) {
                  return 7;
               }
               break;
            case 23:
               if (var1.equals("web-service-persistence")) {
                  return 12;
               }

               if (var1.equals("web-service-reliability")) {
                  return 14;
               }
               break;
            case 39:
               if (var1.equals("callback-queuemdb-run-as-principal-name")) {
                  return 11;
               }
               break;
            case 40:
               if (var1.equals("messaging-queuemdb-run-as-principal-name")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 12:
               return new WebServicePersistenceMBeanImpl.SchemaHelper2();
            case 13:
               return new WebServiceBufferingMBeanImpl.SchemaHelper2();
            case 14:
               return new WebServiceReliabilityMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "jms-connection-factory";
            case 8:
               return "messaging-queue";
            case 9:
               return "messaging-queuemdb-run-as-principal-name";
            case 10:
               return "callback-queue";
            case 11:
               return "callback-queuemdb-run-as-principal-name";
            case 12:
               return "web-service-persistence";
            case 13:
               return "web-service-buffering";
            case 14:
               return "web-service-reliability";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 12:
               return true;
            case 13:
               return true;
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServiceMBeanImpl bean;

      protected Helper(WebServiceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "JmsConnectionFactory";
            case 8:
               return "MessagingQueue";
            case 9:
               return "MessagingQueueMDBRunAsPrincipalName";
            case 10:
               return "CallbackQueue";
            case 11:
               return "CallbackQueueMDBRunAsPrincipalName";
            case 12:
               return "WebServicePersistence";
            case 13:
               return "WebServiceBuffering";
            case 14:
               return "WebServiceReliability";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CallbackQueue")) {
            return 10;
         } else if (var1.equals("CallbackQueueMDBRunAsPrincipalName")) {
            return 11;
         } else if (var1.equals("JmsConnectionFactory")) {
            return 7;
         } else if (var1.equals("MessagingQueue")) {
            return 8;
         } else if (var1.equals("MessagingQueueMDBRunAsPrincipalName")) {
            return 9;
         } else if (var1.equals("WebServiceBuffering")) {
            return 13;
         } else if (var1.equals("WebServicePersistence")) {
            return 12;
         } else {
            return var1.equals("WebServiceReliability") ? 14 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getWebServiceBuffering() != null) {
            var1.add(new ArrayIterator(new WebServiceBufferingMBean[]{this.bean.getWebServiceBuffering()}));
         }

         if (this.bean.getWebServicePersistence() != null) {
            var1.add(new ArrayIterator(new WebServicePersistenceMBean[]{this.bean.getWebServicePersistence()}));
         }

         if (this.bean.getWebServiceReliability() != null) {
            var1.add(new ArrayIterator(new WebServiceReliabilityMBean[]{this.bean.getWebServiceReliability()}));
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
            if (this.bean.isCallbackQueueSet()) {
               var2.append("CallbackQueue");
               var2.append(String.valueOf(this.bean.getCallbackQueue()));
            }

            if (this.bean.isCallbackQueueMDBRunAsPrincipalNameSet()) {
               var2.append("CallbackQueueMDBRunAsPrincipalName");
               var2.append(String.valueOf(this.bean.getCallbackQueueMDBRunAsPrincipalName()));
            }

            if (this.bean.isJmsConnectionFactorySet()) {
               var2.append("JmsConnectionFactory");
               var2.append(String.valueOf(this.bean.getJmsConnectionFactory()));
            }

            if (this.bean.isMessagingQueueSet()) {
               var2.append("MessagingQueue");
               var2.append(String.valueOf(this.bean.getMessagingQueue()));
            }

            if (this.bean.isMessagingQueueMDBRunAsPrincipalNameSet()) {
               var2.append("MessagingQueueMDBRunAsPrincipalName");
               var2.append(String.valueOf(this.bean.getMessagingQueueMDBRunAsPrincipalName()));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServiceBuffering());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServicePersistence());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServiceReliability());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
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
            WebServiceMBeanImpl var2 = (WebServiceMBeanImpl)var1;
            this.computeDiff("CallbackQueue", this.bean.getCallbackQueue(), var2.getCallbackQueue(), true);
            this.computeDiff("CallbackQueueMDBRunAsPrincipalName", this.bean.getCallbackQueueMDBRunAsPrincipalName(), var2.getCallbackQueueMDBRunAsPrincipalName(), true);
            this.computeDiff("JmsConnectionFactory", this.bean.getJmsConnectionFactory(), var2.getJmsConnectionFactory(), true);
            this.computeDiff("MessagingQueue", this.bean.getMessagingQueue(), var2.getMessagingQueue(), true);
            this.computeDiff("MessagingQueueMDBRunAsPrincipalName", this.bean.getMessagingQueueMDBRunAsPrincipalName(), var2.getMessagingQueueMDBRunAsPrincipalName(), true);
            this.computeSubDiff("WebServiceBuffering", this.bean.getWebServiceBuffering(), var2.getWebServiceBuffering());
            this.computeSubDiff("WebServicePersistence", this.bean.getWebServicePersistence(), var2.getWebServicePersistence());
            this.computeSubDiff("WebServiceReliability", this.bean.getWebServiceReliability(), var2.getWebServiceReliability());
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServiceMBeanImpl var3 = (WebServiceMBeanImpl)var1.getSourceBean();
            WebServiceMBeanImpl var4 = (WebServiceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CallbackQueue")) {
                  var3.setCallbackQueue(var4.getCallbackQueue());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("CallbackQueueMDBRunAsPrincipalName")) {
                  var3.setCallbackQueueMDBRunAsPrincipalName(var4.getCallbackQueueMDBRunAsPrincipalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("JmsConnectionFactory")) {
                  var3.setJmsConnectionFactory(var4.getJmsConnectionFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("MessagingQueue")) {
                  var3.setMessagingQueue(var4.getMessagingQueue());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("MessagingQueueMDBRunAsPrincipalName")) {
                  var3.setMessagingQueueMDBRunAsPrincipalName(var4.getMessagingQueueMDBRunAsPrincipalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("WebServiceBuffering")) {
                  if (var6 == 2) {
                     var3.setWebServiceBuffering((WebServiceBufferingMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServiceBuffering()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServiceBuffering", var3.getWebServiceBuffering());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("WebServicePersistence")) {
                  if (var6 == 2) {
                     var3.setWebServicePersistence((WebServicePersistenceMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServicePersistence()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServicePersistence", var3.getWebServicePersistence());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("WebServiceReliability")) {
                  if (var6 == 2) {
                     var3.setWebServiceReliability((WebServiceReliabilityMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServiceReliability()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServiceReliability", var3.getWebServiceReliability());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
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
            WebServiceMBeanImpl var5 = (WebServiceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CallbackQueue")) && this.bean.isCallbackQueueSet()) {
               var5.setCallbackQueue(this.bean.getCallbackQueue());
            }

            if ((var3 == null || !var3.contains("CallbackQueueMDBRunAsPrincipalName")) && this.bean.isCallbackQueueMDBRunAsPrincipalNameSet()) {
               var5.setCallbackQueueMDBRunAsPrincipalName(this.bean.getCallbackQueueMDBRunAsPrincipalName());
            }

            if ((var3 == null || !var3.contains("JmsConnectionFactory")) && this.bean.isJmsConnectionFactorySet()) {
               var5.setJmsConnectionFactory(this.bean.getJmsConnectionFactory());
            }

            if ((var3 == null || !var3.contains("MessagingQueue")) && this.bean.isMessagingQueueSet()) {
               var5.setMessagingQueue(this.bean.getMessagingQueue());
            }

            if ((var3 == null || !var3.contains("MessagingQueueMDBRunAsPrincipalName")) && this.bean.isMessagingQueueMDBRunAsPrincipalNameSet()) {
               var5.setMessagingQueueMDBRunAsPrincipalName(this.bean.getMessagingQueueMDBRunAsPrincipalName());
            }

            if ((var3 == null || !var3.contains("WebServiceBuffering")) && this.bean.isWebServiceBufferingSet() && !var5._isSet(13)) {
               WebServiceBufferingMBean var4 = this.bean.getWebServiceBuffering();
               var5.setWebServiceBuffering((WebServiceBufferingMBean)null);
               var5.setWebServiceBuffering(var4 == null ? null : (WebServiceBufferingMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("WebServicePersistence")) && this.bean.isWebServicePersistenceSet() && !var5._isSet(12)) {
               WebServicePersistenceMBean var8 = this.bean.getWebServicePersistence();
               var5.setWebServicePersistence((WebServicePersistenceMBean)null);
               var5.setWebServicePersistence(var8 == null ? null : (WebServicePersistenceMBean)this.createCopy((AbstractDescriptorBean)var8, var2));
            }

            if ((var3 == null || !var3.contains("WebServiceReliability")) && this.bean.isWebServiceReliabilitySet() && !var5._isSet(14)) {
               WebServiceReliabilityMBean var9 = this.bean.getWebServiceReliability();
               var5.setWebServiceReliability((WebServiceReliabilityMBean)null);
               var5.setWebServiceReliability(var9 == null ? null : (WebServiceReliabilityMBean)this.createCopy((AbstractDescriptorBean)var9, var2));
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
         this.inferSubTree(this.bean.getWebServiceBuffering(), var1, var2);
         this.inferSubTree(this.bean.getWebServicePersistence(), var1, var2);
         this.inferSubTree(this.bean.getWebServiceReliability(), var1, var2);
      }
   }
}
