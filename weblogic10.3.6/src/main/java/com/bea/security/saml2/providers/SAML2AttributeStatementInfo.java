package com.bea.security.saml2.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SAML2AttributeStatementInfo {
   private Collection<SAML2AttributeInfo> attributes;

   public SAML2AttributeStatementInfo() {
   }

   public SAML2AttributeStatementInfo(Collection<SAML2AttributeInfo> attrs) {
      this.addAttributeInfo(attrs);
   }

   public void addAttributeInfo(SAML2AttributeInfo attr) {
      if (this.isValid(attr)) {
         if (this.attributes == null) {
            this.attributes = new ArrayList();
         }

         this.attributes.add(attr);
      }

   }

   public void addAttributeInfo(Collection<SAML2AttributeInfo> attrs) {
      if (attrs != null && attrs.size() > 0) {
         if (this.attributes == null) {
            this.attributes = new ArrayList();
         }

         Iterator i$ = attrs.iterator();

         while(i$.hasNext()) {
            SAML2AttributeInfo attr = (SAML2AttributeInfo)i$.next();
            if (this.isValid(attr)) {
               this.attributes.add(attr);
            }
         }
      }

   }

   public Collection<SAML2AttributeInfo> getAttributeInfo() {
      return this.attributes;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      int size = this.attributes == null ? 0 : this.attributes.size();
      builder.append("SAML2AttributeStatement - NumOfAttrs: ").append(size).append("\n");
      if (size < 1) {
         return builder.toString();
      } else {
         Iterator i$ = this.attributes.iterator();

         while(i$.hasNext()) {
            SAML2AttributeInfo attr = (SAML2AttributeInfo)i$.next();
            builder.append(attr).append("\n");
         }

         return builder.toString();
      }
   }

   private boolean isValid(SAML2AttributeInfo attr) {
      String attrName = attr == null ? null : attr.getAttributeName();
      return attrName != null && attrName.trim().length() > 0;
   }
}
