package weblogic.management.security.pk;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.security.ProviderMBeanImpl;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class KeyStoreMBeanImpl extends ProviderMBeanImpl implements KeyStoreMBean, Serializable {
   private String _PrivateKeyStoreLocation;
   private String _PrivateKeyStorePassPhrase;
   private byte[] _PrivateKeyStorePassPhraseEncrypted;
   private String _RootCAKeyStoreLocation;
   private String _RootCAKeyStorePassPhrase;
   private byte[] _RootCAKeyStorePassPhraseEncrypted;
   private String _Type;
   private static SchemaHelper2 _schemaHelper;

   public KeyStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public KeyStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getPrivateKeyStorePassPhrase() {
      byte[] var1 = this.getPrivateKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("PrivateKeyStorePassPhrase", var1);
   }

   public boolean isPrivateKeyStorePassPhraseSet() {
      return this.isPrivateKeyStorePassPhraseEncryptedSet();
   }

   public void setPrivateKeyStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPrivateKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("PrivateKeyStorePassPhrase", var1));
   }

   public byte[] getPrivateKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._PrivateKeyStorePassPhraseEncrypted);
   }

   public String getPrivateKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getPrivateKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPrivateKeyStorePassPhraseEncryptedSet() {
      return this._isSet(9);
   }

   public void setPrivateKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPrivateKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getRootCAKeyStorePassPhrase() {
      byte[] var1 = this.getRootCAKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("RootCAKeyStorePassPhrase", var1);
   }

   public boolean isRootCAKeyStorePassPhraseSet() {
      return this.isRootCAKeyStorePassPhraseEncryptedSet();
   }

   public void setRootCAKeyStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setRootCAKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("RootCAKeyStorePassPhrase", var1));
   }

   public byte[] getRootCAKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._RootCAKeyStorePassPhraseEncrypted);
   }

   public String getRootCAKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getRootCAKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isRootCAKeyStorePassPhraseEncryptedSet() {
      return this._isSet(11);
   }

   public void setRootCAKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setRootCAKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getPrivateKeyStoreLocation() {
      return this._PrivateKeyStoreLocation;
   }

   public boolean isPrivateKeyStoreLocationSet() {
      return this._isSet(12);
   }

   public void setPrivateKeyStoreLocation(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PrivateKeyStoreLocation;
      this._PrivateKeyStoreLocation = var1;
      this._postSet(12, var2, var1);
   }

   public String getRootCAKeyStoreLocation() {
      return this._RootCAKeyStoreLocation;
   }

   public boolean isRootCAKeyStoreLocationSet() {
      return this._isSet(13);
   }

   public void setRootCAKeyStoreLocation(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RootCAKeyStoreLocation;
      this._RootCAKeyStoreLocation = var1;
      this._postSet(13, var2, var1);
   }

   public String getType() {
      return this._Type;
   }

   public boolean isTypeSet() {
      return this._isSet(14);
   }

   public void setType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Type = var1;
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPrivateKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._PrivateKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PrivateKeyStorePassPhraseEncrypted of KeyStoreMBean");
      } else {
         this._getHelper()._clearArray(this._PrivateKeyStorePassPhraseEncrypted);
         this._PrivateKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(9, var2, var1);
      }
   }

   public void setRootCAKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._RootCAKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: RootCAKeyStorePassPhraseEncrypted of KeyStoreMBean");
      } else {
         this._getHelper()._clearArray(this._RootCAKeyStorePassPhraseEncrypted);
         this._RootCAKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(11, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 8) {
            this._markSet(9, false);
         }

         if (var1 == 10) {
            this._markSet(11, false);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._PrivateKeyStoreLocation = null;
               if (var2) {
                  break;
               }
            case 8:
               this._PrivateKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 9:
               this._PrivateKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 13:
               this._RootCAKeyStoreLocation = null;
               if (var2) {
                  break;
               }
            case 10:
               this._RootCAKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 11:
               this._RootCAKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 14:
               this._Type = "jks";
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.pk.KeyStoreMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("type")) {
                  return 14;
               }
               break;
            case 25:
               if (var1.equals("rootca-key-store-location")) {
                  return 13;
               }
               break;
            case 26:
               if (var1.equals("private-key-store-location")) {
                  return 12;
               }
               break;
            case 28:
               if (var1.equals("rootca-key-store-pass-phrase")) {
                  return 10;
               }
               break;
            case 29:
               if (var1.equals("private-key-store-pass-phrase")) {
                  return 8;
               }
               break;
            case 38:
               if (var1.equals("rootca-key-store-pass-phrase-encrypted")) {
                  return 11;
               }
               break;
            case 39:
               if (var1.equals("private-key-store-pass-phrase-encrypted")) {
                  return 9;
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
            case 8:
               return "private-key-store-pass-phrase";
            case 9:
               return "private-key-store-pass-phrase-encrypted";
            case 10:
               return "rootca-key-store-pass-phrase";
            case 11:
               return "rootca-key-store-pass-phrase-encrypted";
            case 12:
               return "private-key-store-location";
            case 13:
               return "rootca-key-store-location";
            case 14:
               return "type";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
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
   }

   protected static class Helper extends ProviderMBeanImpl.Helper {
      private KeyStoreMBeanImpl bean;

      protected Helper(KeyStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "PrivateKeyStorePassPhrase";
            case 9:
               return "PrivateKeyStorePassPhraseEncrypted";
            case 10:
               return "RootCAKeyStorePassPhrase";
            case 11:
               return "RootCAKeyStorePassPhraseEncrypted";
            case 12:
               return "PrivateKeyStoreLocation";
            case 13:
               return "RootCAKeyStoreLocation";
            case 14:
               return "Type";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("PrivateKeyStoreLocation")) {
            return 12;
         } else if (var1.equals("PrivateKeyStorePassPhrase")) {
            return 8;
         } else if (var1.equals("PrivateKeyStorePassPhraseEncrypted")) {
            return 9;
         } else if (var1.equals("RootCAKeyStoreLocation")) {
            return 13;
         } else if (var1.equals("RootCAKeyStorePassPhrase")) {
            return 10;
         } else if (var1.equals("RootCAKeyStorePassPhraseEncrypted")) {
            return 11;
         } else {
            return var1.equals("Type") ? 14 : super.getPropertyIndex(var1);
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
            if (this.bean.isPrivateKeyStoreLocationSet()) {
               var2.append("PrivateKeyStoreLocation");
               var2.append(String.valueOf(this.bean.getPrivateKeyStoreLocation()));
            }

            if (this.bean.isPrivateKeyStorePassPhraseSet()) {
               var2.append("PrivateKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getPrivateKeyStorePassPhrase()));
            }

            if (this.bean.isPrivateKeyStorePassPhraseEncryptedSet()) {
               var2.append("PrivateKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPrivateKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isRootCAKeyStoreLocationSet()) {
               var2.append("RootCAKeyStoreLocation");
               var2.append(String.valueOf(this.bean.getRootCAKeyStoreLocation()));
            }

            if (this.bean.isRootCAKeyStorePassPhraseSet()) {
               var2.append("RootCAKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getRootCAKeyStorePassPhrase()));
            }

            if (this.bean.isRootCAKeyStorePassPhraseEncryptedSet()) {
               var2.append("RootCAKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getRootCAKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isTypeSet()) {
               var2.append("Type");
               var2.append(String.valueOf(this.bean.getType()));
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
            KeyStoreMBeanImpl var2 = (KeyStoreMBeanImpl)var1;
            this.computeDiff("PrivateKeyStoreLocation", this.bean.getPrivateKeyStoreLocation(), var2.getPrivateKeyStoreLocation(), false);
            this.computeDiff("PrivateKeyStorePassPhraseEncrypted", this.bean.getPrivateKeyStorePassPhraseEncrypted(), var2.getPrivateKeyStorePassPhraseEncrypted(), false);
            this.computeDiff("RootCAKeyStoreLocation", this.bean.getRootCAKeyStoreLocation(), var2.getRootCAKeyStoreLocation(), false);
            this.computeDiff("RootCAKeyStorePassPhraseEncrypted", this.bean.getRootCAKeyStorePassPhraseEncrypted(), var2.getRootCAKeyStorePassPhraseEncrypted(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            KeyStoreMBeanImpl var3 = (KeyStoreMBeanImpl)var1.getSourceBean();
            KeyStoreMBeanImpl var4 = (KeyStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("PrivateKeyStoreLocation")) {
                  var3.setPrivateKeyStoreLocation(var4.getPrivateKeyStoreLocation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (!var5.equals("PrivateKeyStorePassPhrase")) {
                  if (var5.equals("PrivateKeyStorePassPhraseEncrypted")) {
                     var3.setPrivateKeyStorePassPhraseEncrypted(var4.getPrivateKeyStorePassPhraseEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("RootCAKeyStoreLocation")) {
                     var3.setRootCAKeyStoreLocation(var4.getRootCAKeyStoreLocation());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (!var5.equals("RootCAKeyStorePassPhrase")) {
                     if (var5.equals("RootCAKeyStorePassPhraseEncrypted")) {
                        var3.setRootCAKeyStorePassPhraseEncrypted(var4.getRootCAKeyStorePassPhraseEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     } else if (!var5.equals("Type")) {
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
            KeyStoreMBeanImpl var5 = (KeyStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("PrivateKeyStoreLocation")) && this.bean.isPrivateKeyStoreLocationSet()) {
               var5.setPrivateKeyStoreLocation(this.bean.getPrivateKeyStoreLocation());
            }

            byte[] var4;
            if ((var3 == null || !var3.contains("PrivateKeyStorePassPhraseEncrypted")) && this.bean.isPrivateKeyStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getPrivateKeyStorePassPhraseEncrypted();
               var5.setPrivateKeyStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("RootCAKeyStoreLocation")) && this.bean.isRootCAKeyStoreLocationSet()) {
               var5.setRootCAKeyStoreLocation(this.bean.getRootCAKeyStoreLocation());
            }

            if ((var3 == null || !var3.contains("RootCAKeyStorePassPhraseEncrypted")) && this.bean.isRootCAKeyStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getRootCAKeyStorePassPhraseEncrypted();
               var5.setRootCAKeyStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
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
