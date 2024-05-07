package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.util.Util;

public class PublisherAssertion extends UDDIListObject {
   private BusinessKey m_fromKey = null;
   private BusinessKey m_toKey = null;
   private KeyedReference m_keyedReference = null;
   private boolean m_fromAssertion;
   private boolean m_fromKeyOwner;
   private boolean m_toKeyOwner;
   private String m_id = null;

   public void setFromKey(BusinessKey var1) {
      this.m_fromKey = var1;
   }

   public BusinessKey getFromKey() {
      return this.m_fromKey;
   }

   public void setToKey(BusinessKey var1) {
      this.m_toKey = var1;
   }

   public BusinessKey getToKey() {
      return this.m_toKey;
   }

   public void setID(String var1) {
      this.m_id = var1;
   }

   public String getID() {
      return this.m_id;
   }

   public void setFromKeyOwner() {
      this.m_fromKeyOwner = true;
   }

   public void setToKeyOwner() {
      this.m_toKeyOwner = true;
   }

   public boolean isFromKeyOwner() {
      return this.m_fromKeyOwner;
   }

   public boolean isToKeyOwner() {
      return this.m_toKeyOwner;
   }

   public void setKeyedReference(KeyedReference var1) {
      this.m_keyedReference = var1;
   }

   public KeyedReference getKeyedReference() {
      return this.m_keyedReference;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PublisherAssertion)) {
         return false;
      } else {
         PublisherAssertion var2 = (PublisherAssertion)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_fromKey, (Object)var2.m_fromKey);
         var3 &= Util.isEqual((Object)this.m_toKey, (Object)var2.m_toKey);
         var3 &= Util.isEqual((Object)this.m_keyedReference, (Object)var2.m_keyedReference);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<publisherAssertion>");
      if (this.m_fromKey != null) {
         var1.append("<fromKey>").append(this.m_fromKey.toString()).append("</fromKey>");
      }

      if (this.m_toKey != null) {
         var1.append("<toKey>").append(this.m_toKey.toString()).append("</toKey>");
      }

      if (this.m_keyedReference != null) {
         var1.append(this.m_keyedReference.toXML());
      }

      var1.append("</publisherAssertion>");
      return var1.toString();
   }
}
