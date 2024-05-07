package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.diagnostics.watch.WatchValidator;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WLDFSMTPNotificationBeanImpl extends WLDFNotificationBeanImpl implements WLDFSMTPNotificationBean, Serializable {
   private String _Body;
   private String _MailSessionJNDIName;
   private String[] _Recipients;
   private String _Subject;
   private static SchemaHelper2 _schemaHelper;

   public WLDFSMTPNotificationBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFSMTPNotificationBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getMailSessionJNDIName() {
      return this._MailSessionJNDIName;
   }

   public boolean isMailSessionJNDINameSet() {
      return this._isSet(2);
   }

   public void setMailSessionJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("MailSessionJNDIName", var1);
      LegalChecks.checkNonNull("MailSessionJNDIName", var1);
      String var2 = this._MailSessionJNDIName;
      this._MailSessionJNDIName = var1;
      this._postSet(2, var2, var1);
   }

   public String getSubject() {
      return this._Subject;
   }

   public boolean isSubjectSet() {
      return this._isSet(3);
   }

   public void setSubject(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Subject;
      this._Subject = var1;
      this._postSet(3, var2, var1);
   }

   public String getBody() {
      return this._Body;
   }

   public boolean isBodySet() {
      return this._isSet(4);
   }

   public void setBody(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Body;
      this._Body = var1;
      this._postSet(4, var2, var1);
   }

   public String[] getRecipients() {
      return this._Recipients;
   }

   public boolean isRecipientsSet() {
      return this._isSet(5);
   }

   public void setRecipients(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      WatchValidator.validateRecipients(var1);
      String[] var2 = this._Recipients;
      this._Recipients = var1;
      this._postSet(5, var2, var1);
   }

   public void addRecipient(String var1) {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(5)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getRecipients(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setRecipients(var2);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void removeRecipient(String var1) {
      String[] var2 = this.getRecipients();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setRecipients(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      LegalChecks.checkIsSet("MailSessionJNDIName", this.isMailSessionJNDINameSet());
      LegalChecks.checkIsSet("Recipients", this.isRecipientsSet());
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
         var1 = 4;
      }

      try {
         switch (var1) {
            case 4:
               this._Body = null;
               if (var2) {
                  break;
               }
            case 2:
               this._MailSessionJNDIName = null;
               if (var2) {
                  break;
               }
            case 5:
               this._Recipients = new String[0];
               if (var2) {
                  break;
               }
            case 3:
               this._Subject = null;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics/1.0/weblogic-diagnostics.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends WLDFNotificationBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("body")) {
                  return 4;
               }
               break;
            case 7:
               if (var1.equals("subject")) {
                  return 3;
               }
               break;
            case 9:
               if (var1.equals("recipient")) {
                  return 5;
               }
               break;
            case 22:
               if (var1.equals("mail-session-jndi-name")) {
                  return 2;
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
               return "mail-session-jndi-name";
            case 3:
               return "subject";
            case 4:
               return "body";
            case 5:
               return "recipient";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 5:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 0:
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

   protected static class Helper extends WLDFNotificationBeanImpl.Helper {
      private WLDFSMTPNotificationBeanImpl bean;

      protected Helper(WLDFSMTPNotificationBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "MailSessionJNDIName";
            case 3:
               return "Subject";
            case 4:
               return "Body";
            case 5:
               return "Recipients";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Body")) {
            return 4;
         } else if (var1.equals("MailSessionJNDIName")) {
            return 2;
         } else if (var1.equals("Recipients")) {
            return 5;
         } else {
            return var1.equals("Subject") ? 3 : super.getPropertyIndex(var1);
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
            if (this.bean.isBodySet()) {
               var2.append("Body");
               var2.append(String.valueOf(this.bean.getBody()));
            }

            if (this.bean.isMailSessionJNDINameSet()) {
               var2.append("MailSessionJNDIName");
               var2.append(String.valueOf(this.bean.getMailSessionJNDIName()));
            }

            if (this.bean.isRecipientsSet()) {
               var2.append("Recipients");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getRecipients())));
            }

            if (this.bean.isSubjectSet()) {
               var2.append("Subject");
               var2.append(String.valueOf(this.bean.getSubject()));
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
            WLDFSMTPNotificationBeanImpl var2 = (WLDFSMTPNotificationBeanImpl)var1;
            this.computeDiff("Body", this.bean.getBody(), var2.getBody(), true);
            this.computeDiff("MailSessionJNDIName", this.bean.getMailSessionJNDIName(), var2.getMailSessionJNDIName(), true);
            this.computeDiff("Recipients", this.bean.getRecipients(), var2.getRecipients(), true);
            this.computeDiff("Subject", this.bean.getSubject(), var2.getSubject(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFSMTPNotificationBeanImpl var3 = (WLDFSMTPNotificationBeanImpl)var1.getSourceBean();
            WLDFSMTPNotificationBeanImpl var4 = (WLDFSMTPNotificationBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Body")) {
                  var3.setBody(var4.getBody());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("MailSessionJNDIName")) {
                  var3.setMailSessionJNDIName(var4.getMailSessionJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Recipients")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(var2.getAddedObject());
                     var3.addRecipient((String)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeRecipient((String)var2.getRemovedObject());
                  }

                  if (var3.getRecipients() == null || var3.getRecipients().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 5);
                  }
               } else if (var5.equals("Subject")) {
                  var3.setSubject(var4.getSubject());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
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
            WLDFSMTPNotificationBeanImpl var5 = (WLDFSMTPNotificationBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Body")) && this.bean.isBodySet()) {
               var5.setBody(this.bean.getBody());
            }

            if ((var3 == null || !var3.contains("MailSessionJNDIName")) && this.bean.isMailSessionJNDINameSet()) {
               var5.setMailSessionJNDIName(this.bean.getMailSessionJNDIName());
            }

            if ((var3 == null || !var3.contains("Recipients")) && this.bean.isRecipientsSet()) {
               String[] var4 = this.bean.getRecipients();
               var5.setRecipients(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Subject")) && this.bean.isSubjectSet()) {
               var5.setSubject(this.bean.getSubject());
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
