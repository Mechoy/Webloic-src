package weblogic.security.providers.saml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SAMLAttributeStatementInfo {
   private ArrayList<SAMLAttributeInfo> attributes = null;

   public SAMLAttributeStatementInfo() {
   }

   public SAMLAttributeStatementInfo(Collection<SAMLAttributeInfo> attrs) {
      if (attrs != null && attrs.size() > 0) {
         this.attributes = new ArrayList();
         this.addToAttributes(attrs);
      }

   }

   public Collection<SAMLAttributeInfo> getAttributeInfo() {
      return this.attributes;
   }

   public void setAttributeInfo(Collection<SAMLAttributeInfo> attrs) {
      if (attrs != null && attrs.size() > 0) {
         if (this.attributes == null) {
            this.attributes = new ArrayList();
         }

         this.addToAttributes(attrs);
      }

   }

   public void addAttributeInfo(SAMLAttributeInfo info) {
      if (info != null && info.getAttributeName() != null && !info.getAttributeName().equals("") && info.getAttributeNamespace() != null && !info.getAttributeNamespace().equals("")) {
         if (this.attributes == null) {
            this.attributes = new ArrayList();
         }

         this.attributes.add(info);
      }

   }

   private void addToAttributes(Collection<SAMLAttributeInfo> attrs) {
      if (attrs != null && attrs.size() > 0) {
         if (this.attributes == null) {
            this.attributes = new ArrayList();
         }

         Iterator i$ = attrs.iterator();

         while(i$.hasNext()) {
            SAMLAttributeInfo info = (SAMLAttributeInfo)i$.next();
            this.addAttributeInfo(info);
         }
      }

   }
}
