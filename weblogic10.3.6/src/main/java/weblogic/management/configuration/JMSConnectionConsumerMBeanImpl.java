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
import weblogic.utils.collections.CombinedIterator;

public class JMSConnectionConsumerMBeanImpl extends ConfigurationMBeanImpl implements JMSConnectionConsumerMBean, Serializable {
   private String _Destination;
   private int _MessagesMaximum;
   private String _Selector;
   private static SchemaHelper2 _schemaHelper;

   public JMSConnectionConsumerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JMSConnectionConsumerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getMessagesMaximum() {
      return this._MessagesMaximum;
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(7);
   }

   public void setMessagesMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesMaximum", (long)var1, 1L, 2147483647L);
      int var2 = this._MessagesMaximum;
      this._MessagesMaximum = var1;
      this._postSet(7, var2, var1);
   }

   public String getSelector() {
      return this._Selector;
   }

   public boolean isSelectorSet() {
      return this._isSet(8);
   }

   public void setSelector(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Selector;
      this._Selector = var1;
      this._postSet(8, var2, var1);
   }

   public String getDestination() {
      return this._Destination;
   }

   public boolean isDestinationSet() {
      return this._isSet(9);
   }

   public void setDestination(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Destination;
      this._Destination = var1;
      this._postSet(9, var2, var1);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._Destination = null;
               if (var2) {
                  break;
               }
            case 7:
               this._MessagesMaximum = 10;
               if (var2) {
                  break;
               }
            case 8:
               this._Selector = null;
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
      return "JMSConnectionConsumer";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("Destination")) {
         var3 = this._Destination;
         this._Destination = (String)var2;
         this._postSet(9, var3, this._Destination);
      } else if (var1.equals("MessagesMaximum")) {
         int var4 = this._MessagesMaximum;
         this._MessagesMaximum = (Integer)var2;
         this._postSet(7, var4, this._MessagesMaximum);
      } else if (var1.equals("Selector")) {
         var3 = this._Selector;
         this._Selector = (String)var2;
         this._postSet(8, var3, this._Selector);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Destination")) {
         return this._Destination;
      } else if (var1.equals("MessagesMaximum")) {
         return new Integer(this._MessagesMaximum);
      } else {
         return var1.equals("Selector") ? this._Selector : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("selector")) {
                  return 8;
               }
               break;
            case 11:
               if (var1.equals("destination")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("messages-maximum")) {
                  return 7;
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
               return "messages-maximum";
            case 8:
               return "selector";
            case 9:
               return "destination";
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
      private JMSConnectionConsumerMBeanImpl bean;

      protected Helper(JMSConnectionConsumerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MessagesMaximum";
            case 8:
               return "Selector";
            case 9:
               return "Destination";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Destination")) {
            return 9;
         } else if (var1.equals("MessagesMaximum")) {
            return 7;
         } else {
            return var1.equals("Selector") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isDestinationSet()) {
               var2.append("Destination");
               var2.append(String.valueOf(this.bean.getDestination()));
            }

            if (this.bean.isMessagesMaximumSet()) {
               var2.append("MessagesMaximum");
               var2.append(String.valueOf(this.bean.getMessagesMaximum()));
            }

            if (this.bean.isSelectorSet()) {
               var2.append("Selector");
               var2.append(String.valueOf(this.bean.getSelector()));
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
            JMSConnectionConsumerMBeanImpl var2 = (JMSConnectionConsumerMBeanImpl)var1;
            this.computeDiff("Destination", this.bean.getDestination(), var2.getDestination(), false);
            this.computeDiff("MessagesMaximum", this.bean.getMessagesMaximum(), var2.getMessagesMaximum(), true);
            this.computeDiff("Selector", this.bean.getSelector(), var2.getSelector(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSConnectionConsumerMBeanImpl var3 = (JMSConnectionConsumerMBeanImpl)var1.getSourceBean();
            JMSConnectionConsumerMBeanImpl var4 = (JMSConnectionConsumerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Destination")) {
                  var3.setDestination(var4.getDestination());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MessagesMaximum")) {
                  var3.setMessagesMaximum(var4.getMessagesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Selector")) {
                  var3.setSelector(var4.getSelector());
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
            JMSConnectionConsumerMBeanImpl var5 = (JMSConnectionConsumerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Destination")) && this.bean.isDestinationSet()) {
               var5.setDestination(this.bean.getDestination());
            }

            if ((var3 == null || !var3.contains("MessagesMaximum")) && this.bean.isMessagesMaximumSet()) {
               var5.setMessagesMaximum(this.bean.getMessagesMaximum());
            }

            if ((var3 == null || !var3.contains("Selector")) && this.bean.isSelectorSet()) {
               var5.setSelector(this.bean.getSelector());
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
