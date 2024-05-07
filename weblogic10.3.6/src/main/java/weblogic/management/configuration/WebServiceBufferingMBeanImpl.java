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
import weblogic.j2ee.descriptor.wl.validators.WseeConfigBeanValidator;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WebServiceBufferingMBeanImpl extends ConfigurationMBeanImpl implements WebServiceBufferingMBean, Serializable {
   private int _RetryCount;
   private String _RetryDelay;
   private WebServiceRequestBufferingQueueMBean _WebServiceRequestBufferingQueue;
   private WebServiceResponseBufferingQueueMBean _WebServiceResponseBufferingQueue;
   private static SchemaHelper2 _schemaHelper;

   public WebServiceBufferingMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServiceBufferingMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public WebServiceRequestBufferingQueueMBean getWebServiceRequestBufferingQueue() {
      return this._WebServiceRequestBufferingQueue;
   }

   public boolean isWebServiceRequestBufferingQueueSet() {
      return this._isSet(7) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServiceRequestBufferingQueue());
   }

   public void setWebServiceRequestBufferingQueue(WebServiceRequestBufferingQueueMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 7)) {
         this._postCreate(var2);
      }

      WebServiceRequestBufferingQueueMBean var3 = this._WebServiceRequestBufferingQueue;
      this._WebServiceRequestBufferingQueue = var1;
      this._postSet(7, var3, var1);
   }

   public WebServiceResponseBufferingQueueMBean getWebServiceResponseBufferingQueue() {
      return this._WebServiceResponseBufferingQueue;
   }

   public boolean isWebServiceResponseBufferingQueueSet() {
      return this._isSet(8) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServiceResponseBufferingQueue());
   }

   public void setWebServiceResponseBufferingQueue(WebServiceResponseBufferingQueueMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 8)) {
         this._postCreate(var2);
      }

      WebServiceResponseBufferingQueueMBean var3 = this._WebServiceResponseBufferingQueue;
      this._WebServiceResponseBufferingQueue = var1;
      this._postSet(8, var3, var1);
   }

   public int getRetryCount() {
      return this._RetryCount;
   }

   public boolean isRetryCountSet() {
      return this._isSet(9);
   }

   public void setRetryCount(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("RetryCount", var1, 0);
      int var2 = this._RetryCount;
      this._RetryCount = var1;
      this._postSet(9, var2, var1);
   }

   public String getRetryDelay() {
      return this._RetryDelay;
   }

   public boolean isRetryDelaySet() {
      return this._isSet(10);
   }

   public void setRetryDelay(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateRetryDelay(var1);
      String var2 = this._RetryDelay;
      this._RetryDelay = var1;
      this._postSet(10, var2, var1);
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
      return super._isAnythingSet() || this.isWebServiceRequestBufferingQueueSet() || this.isWebServiceResponseBufferingQueueSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._RetryCount = 3;
               if (var2) {
                  break;
               }
            case 10:
               this._RetryDelay = "P0DT30S";
               if (var2) {
                  break;
               }
            case 7:
               this._WebServiceRequestBufferingQueue = new WebServiceRequestBufferingQueueMBeanImpl(this, 7);
               this._postCreate((AbstractDescriptorBean)this._WebServiceRequestBufferingQueue);
               if (var2) {
                  break;
               }
            case 8:
               this._WebServiceResponseBufferingQueue = new WebServiceResponseBufferingQueueMBeanImpl(this, 8);
               this._postCreate((AbstractDescriptorBean)this._WebServiceResponseBufferingQueue);
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
      return "WebServiceBuffering";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("RetryCount")) {
         int var6 = this._RetryCount;
         this._RetryCount = (Integer)var2;
         this._postSet(9, var6, this._RetryCount);
      } else if (var1.equals("RetryDelay")) {
         String var5 = this._RetryDelay;
         this._RetryDelay = (String)var2;
         this._postSet(10, var5, this._RetryDelay);
      } else if (var1.equals("WebServiceRequestBufferingQueue")) {
         WebServiceRequestBufferingQueueMBean var4 = this._WebServiceRequestBufferingQueue;
         this._WebServiceRequestBufferingQueue = (WebServiceRequestBufferingQueueMBean)var2;
         this._postSet(7, var4, this._WebServiceRequestBufferingQueue);
      } else if (var1.equals("WebServiceResponseBufferingQueue")) {
         WebServiceResponseBufferingQueueMBean var3 = this._WebServiceResponseBufferingQueue;
         this._WebServiceResponseBufferingQueue = (WebServiceResponseBufferingQueueMBean)var2;
         this._postSet(8, var3, this._WebServiceResponseBufferingQueue);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("RetryCount")) {
         return new Integer(this._RetryCount);
      } else if (var1.equals("RetryDelay")) {
         return this._RetryDelay;
      } else if (var1.equals("WebServiceRequestBufferingQueue")) {
         return this._WebServiceRequestBufferingQueue;
      } else {
         return var1.equals("WebServiceResponseBufferingQueue") ? this._WebServiceResponseBufferingQueue : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("retry-count")) {
                  return 9;
               }

               if (var1.equals("retry-delay")) {
                  return 10;
               }
               break;
            case 35:
               if (var1.equals("web-service-request-buffering-queue")) {
                  return 7;
               }
               break;
            case 36:
               if (var1.equals("web-service-response-buffering-queue")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new WebServiceRequestBufferingQueueMBeanImpl.SchemaHelper2();
            case 8:
               return new WebServiceResponseBufferingQueueMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "web-service-request-buffering-queue";
            case 8:
               return "web-service-response-buffering-queue";
            case 9:
               return "retry-count";
            case 10:
               return "retry-delay";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServiceBufferingMBeanImpl bean;

      protected Helper(WebServiceBufferingMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "WebServiceRequestBufferingQueue";
            case 8:
               return "WebServiceResponseBufferingQueue";
            case 9:
               return "RetryCount";
            case 10:
               return "RetryDelay";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("RetryCount")) {
            return 9;
         } else if (var1.equals("RetryDelay")) {
            return 10;
         } else if (var1.equals("WebServiceRequestBufferingQueue")) {
            return 7;
         } else {
            return var1.equals("WebServiceResponseBufferingQueue") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getWebServiceRequestBufferingQueue() != null) {
            var1.add(new ArrayIterator(new WebServiceRequestBufferingQueueMBean[]{this.bean.getWebServiceRequestBufferingQueue()}));
         }

         if (this.bean.getWebServiceResponseBufferingQueue() != null) {
            var1.add(new ArrayIterator(new WebServiceResponseBufferingQueueMBean[]{this.bean.getWebServiceResponseBufferingQueue()}));
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
            if (this.bean.isRetryCountSet()) {
               var2.append("RetryCount");
               var2.append(String.valueOf(this.bean.getRetryCount()));
            }

            if (this.bean.isRetryDelaySet()) {
               var2.append("RetryDelay");
               var2.append(String.valueOf(this.bean.getRetryDelay()));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServiceRequestBufferingQueue());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServiceResponseBufferingQueue());
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
            WebServiceBufferingMBeanImpl var2 = (WebServiceBufferingMBeanImpl)var1;
            this.computeDiff("RetryCount", this.bean.getRetryCount(), var2.getRetryCount(), true);
            this.computeDiff("RetryDelay", this.bean.getRetryDelay(), var2.getRetryDelay(), true);
            this.computeSubDiff("WebServiceRequestBufferingQueue", this.bean.getWebServiceRequestBufferingQueue(), var2.getWebServiceRequestBufferingQueue());
            this.computeSubDiff("WebServiceResponseBufferingQueue", this.bean.getWebServiceResponseBufferingQueue(), var2.getWebServiceResponseBufferingQueue());
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServiceBufferingMBeanImpl var3 = (WebServiceBufferingMBeanImpl)var1.getSourceBean();
            WebServiceBufferingMBeanImpl var4 = (WebServiceBufferingMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("RetryCount")) {
                  var3.setRetryCount(var4.getRetryCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("RetryDelay")) {
                  var3.setRetryDelay(var4.getRetryDelay());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("WebServiceRequestBufferingQueue")) {
                  if (var6 == 2) {
                     var3.setWebServiceRequestBufferingQueue((WebServiceRequestBufferingQueueMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServiceRequestBufferingQueue()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServiceRequestBufferingQueue", var3.getWebServiceRequestBufferingQueue());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("WebServiceResponseBufferingQueue")) {
                  if (var6 == 2) {
                     var3.setWebServiceResponseBufferingQueue((WebServiceResponseBufferingQueueMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServiceResponseBufferingQueue()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServiceResponseBufferingQueue", var3.getWebServiceResponseBufferingQueue());
                  }

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
            WebServiceBufferingMBeanImpl var5 = (WebServiceBufferingMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("RetryCount")) && this.bean.isRetryCountSet()) {
               var5.setRetryCount(this.bean.getRetryCount());
            }

            if ((var3 == null || !var3.contains("RetryDelay")) && this.bean.isRetryDelaySet()) {
               var5.setRetryDelay(this.bean.getRetryDelay());
            }

            if ((var3 == null || !var3.contains("WebServiceRequestBufferingQueue")) && this.bean.isWebServiceRequestBufferingQueueSet() && !var5._isSet(7)) {
               WebServiceRequestBufferingQueueMBean var4 = this.bean.getWebServiceRequestBufferingQueue();
               var5.setWebServiceRequestBufferingQueue((WebServiceRequestBufferingQueueMBean)null);
               var5.setWebServiceRequestBufferingQueue(var4 == null ? null : (WebServiceRequestBufferingQueueMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("WebServiceResponseBufferingQueue")) && this.bean.isWebServiceResponseBufferingQueueSet() && !var5._isSet(8)) {
               WebServiceResponseBufferingQueueMBean var8 = this.bean.getWebServiceResponseBufferingQueue();
               var5.setWebServiceResponseBufferingQueue((WebServiceResponseBufferingQueueMBean)null);
               var5.setWebServiceResponseBufferingQueue(var8 == null ? null : (WebServiceResponseBufferingQueueMBean)this.createCopy((AbstractDescriptorBean)var8, var2));
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
         this.inferSubTree(this.bean.getWebServiceRequestBufferingQueue(), var1, var2);
         this.inferSubTree(this.bean.getWebServiceResponseBufferingQueue(), var1, var2);
      }
   }
}
