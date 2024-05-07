package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.InvalidCompletionStatusException;
import weblogic.auddi.util.Util;

public class AssertionStatusItem extends UDDIListObject {
   private BusinessKey m_fromKey = null;
   private boolean m_fromKeyAsserted;
   private boolean m_fromKeyOwned;
   private BusinessKey m_toKey = null;
   private boolean m_toKeyAsserted;
   private boolean m_toKeyOwned;
   private KeyedReference m_keyedReference = null;
   private CompletionStatus m_completionStatus = null;

   public void setAll(PublisherAssertion var1) {
      this.setFromKey(var1.getFromKey());
      this.setToKey(var1.getToKey());
      this.setKeyedReference(var1.getKeyedReference());
      if (var1.isFromKeyOwner()) {
         this.setFromKeyAsserted(true);
      } else {
         this.setToKeyAsserted(true);
      }

   }

   public void setFromKey(BusinessKey var1) {
      this.m_fromKey = var1;
   }

   public BusinessKey getFromKey() {
      return this.m_fromKey;
   }

   public void setToKey(BusinessKey var1) {
      this.m_toKey = var1;
   }

   public boolean getToKeyOwnership() {
      return this.m_toKeyOwned;
   }

   public boolean isToKeyAsserted() {
      return this.m_toKeyAsserted;
   }

   public void setToKeyAsserted(boolean var1) {
      this.m_toKeyAsserted = var1;
   }

   public void setToKeyOwnership(boolean var1) {
      this.m_toKeyOwned = var1;
   }

   public void setCompletionStatus(String var1) throws InvalidCompletionStatusException {
      this.m_completionStatus = new CompletionStatus(var1);
   }

   public void setCompletionStatus(CompletionStatus var1) {
      this.m_completionStatus = var1;
   }

   public CompletionStatus getCompletionStatus() {
      return this.m_completionStatus;
   }

   public BusinessKey getToKey() {
      return this.m_toKey;
   }

   public void setFromKeyAsserted(boolean var1) {
      this.m_fromKeyAsserted = var1;
   }

   public boolean isFromKeyAsserted() {
      return this.m_fromKeyAsserted;
   }

   public boolean getFromKeyOwnership() {
      return this.m_fromKeyOwned;
   }

   public void setFromKeyOwnership(boolean var1) {
      this.m_fromKeyOwned = var1;
   }

   public void setKeyedReference(KeyedReference var1) {
      this.m_keyedReference = var1;
   }

   public KeyedReference getKeyedReference() {
      return this.m_keyedReference;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AssertionStatusItem)) {
         return false;
      } else {
         AssertionStatusItem var2 = (AssertionStatusItem)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_fromKey, (Object)var2.m_fromKey);
         var3 &= Util.isEqual((Object)this.m_toKey, (Object)var2.m_toKey);
         var3 &= Util.isEqual((Object)this.m_keyedReference, (Object)var2.m_keyedReference);
         var3 &= this.m_fromKeyOwned == var2.m_fromKeyOwned;
         var3 &= this.m_toKeyOwned == var2.m_toKeyOwned;
         var3 &= Util.isEqual((Object)this.m_completionStatus, (Object)var2.m_completionStatus);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<assertionStatusItem completionStatus=\"" + this.m_completionStatus + "\">");
      if (this.m_fromKey != null) {
         var1.append("<fromKey>" + this.m_fromKey.toString() + "</fromKey>");
      }

      if (this.m_toKey != null) {
         var1.append("<toKey>" + this.m_toKey.toString() + "</toKey>");
      }

      if (this.m_keyedReference != null) {
         var1.append(this.m_keyedReference.toXML());
      }

      var1.append("<keysOwned>");
      if (this.m_fromKeyOwned) {
         var1.append("<fromKey>" + this.m_fromKey.toString() + "</fromKey>");
      }

      if (this.m_toKeyOwned) {
         var1.append("<toKey>" + this.m_toKey.toString() + "</toKey>");
      }

      var1.append("</keysOwned>");
      var1.append("</assertionStatusItem>");
      return var1.toString();
   }

   public String toString() {
      String var1 = this.toXML();
      return var1;
   }
}
