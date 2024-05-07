package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class KeyedReference extends UDDIBagObject implements Serializable {
   private TModelKey m_tModelKey;
   private String m_keyName;
   private String m_keyValue;
   private boolean m_standard = false;

   public KeyedReference(TModelKey var1, String var2, String var3) throws FatalErrorException {
      if (var3 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "keyValue"));
      } else {
         this.m_tModelKey = var1;
         this.m_keyName = this.truncateString(var2, 255);
         this.m_keyValue = this.truncateString(var3, 255);
      }
   }

   public KeyedReference(KeyedReference var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_tModelKey != null) {
            this.m_tModelKey = new TModelKey(var1.m_tModelKey);
         }

         this.m_keyName = var1.m_keyName;
         this.m_keyValue = var1.m_keyValue;
         this.m_standard = var1.m_standard;
      }
   }

   public String getKey() {
      String var1 = "";
      if (this.m_tModelKey != null) {
         var1 = var1 + this.m_tModelKey.toString();
      }

      if (this.m_keyValue != null) {
         var1 = var1 + this.m_keyValue;
      }

      return var1;
   }

   public String getName() {
      return this.m_keyName;
   }

   public String getValue() {
      return this.m_keyValue;
   }

   public TModelKey getTModelKey() {
      return this.m_tModelKey;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof KeyedReference)) {
         return false;
      } else {
         KeyedReference var2 = (KeyedReference)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.getName(), (Object)var2.getName());
         var3 &= Util.isEqual((Object)this.getValue(), (Object)var2.getValue());
         var3 &= Util.isEqual((Object)this.getTModelKey(), (Object)var2.getTModelKey());
         return var3;
      }
   }

   public int hashCode() {
      int var1 = 17;
      if (this.m_tModelKey != null) {
         var1 = 37 * var1 + this.m_tModelKey.hashCode();
      }

      if (this.m_keyName != null) {
         var1 = 37 * var1 + this.m_keyName.hashCode();
      }

      if (this.m_keyValue != null) {
         var1 = 37 * var1 + this.m_keyValue.hashCode();
      }

      return var1;
   }

   public boolean isStandard() {
      return this.m_standard;
   }

   public void setStandard(boolean var1) {
      this.m_standard = var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<keyedReference");
      if (this.m_tModelKey != null) {
         var1.append(" tModelKey=\"").append(this.m_tModelKey.toString()).append("\"");
      }

      if (this.m_keyName != null) {
         var1.append(" keyName=\"").append(this.fixStringForXML(this.m_keyName)).append("\"");
      }

      if (this.m_keyValue != null) {
         var1.append(" keyValue=\"").append(this.fixStringForXML(this.m_keyValue)).append("\"");
      }

      var1.append(">");
      var1.append("</keyedReference>");
      return var1.toString();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("tModelKey : " + this.m_tModelKey + ", ");
      var1.append("  keyName : " + this.m_keyName + ", ");
      var1.append(" keyValue : " + this.m_keyValue);
      return var1.toString();
   }
}
