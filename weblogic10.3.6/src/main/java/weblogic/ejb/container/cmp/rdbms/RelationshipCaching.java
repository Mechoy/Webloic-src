package weblogic.ejb.container.cmp.rdbms;

import java.util.ArrayList;
import java.util.List;

public final class RelationshipCaching {
   private String cachingName;
   private List cachingElements = new ArrayList();

   public void setCachingName(String var1) {
      this.cachingName = var1;
   }

   public String getCachingName() {
      return this.cachingName;
   }

   public void addCachingElement(CachingElement var1) {
      this.cachingElements.add(var1);
   }

   public List getCachingElements() {
      return this.cachingElements;
   }

   public String toString() {
      return "[RelationshipCaching name:" + this.cachingName + " cachingElements:" + this.cachingElements + "]";
   }

   public static class CachingElement {
      private String cmrField;
      private String groupName;
      private List cachingElements = new ArrayList();

      public void setCmrField(String var1) {
         this.cmrField = var1;
      }

      public String getCmrField() {
         return this.cmrField;
      }

      public void setGroupName(String var1) {
         this.groupName = var1;
      }

      public String getGroupName() {
         return this.groupName;
      }

      public void addCachingElement(CachingElement var1) {
         this.cachingElements.add(var1);
      }

      public List getCachingElements() {
         return this.cachingElements;
      }

      public String toString() {
         return "[CachingElement cmrField:" + this.cmrField + " group name:" + this.groupName + " cachingElements:" + this.cachingElements + "]";
      }
   }
}
