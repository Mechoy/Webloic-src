package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class TModel extends UDDIListObject {
   private TModelKey m_tModelKey = null;
   private AuthorizedName m_authorizedName = null;
   private Operator m_operator = null;
   private Name m_name = null;
   private Descriptions m_descriptions;
   private OverviewDoc m_overviewDoc = null;
   private IdentifierBag m_identifierBag = null;
   private CategoryBag m_categoryBag = null;
   protected boolean m_isHidden = false;

   public TModel() {
   }

   public TModel(TModelKey var1) {
      this.m_tModelKey = var1;
   }

   public TModel(TModelKey var1, String var2) throws UDDIException {
      this.m_tModelKey = var1;
      this.m_name = new Name(var2);
   }

   public TModel(TModel var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_authorizedName != null) {
            this.m_authorizedName = new AuthorizedName(var1.m_authorizedName);
         }

         if (var1.m_tModelKey != null) {
            this.m_tModelKey = new TModelKey(var1.m_tModelKey);
         }

         if (var1.m_name != null) {
            this.m_name = new Name(var1.m_name);
         }

         if (var1.m_operator != null) {
            this.m_operator = new Operator(var1.m_operator.getName());
         }

         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_categoryBag != null) {
            this.m_categoryBag = new CategoryBag(var1.m_categoryBag);
         }

         if (var1.m_identifierBag != null) {
            this.m_identifierBag = new IdentifierBag(var1.m_identifierBag);
         }

         if (var1.m_overviewDoc != null) {
            this.m_overviewDoc = new OverviewDoc(var1.m_overviewDoc);
         }

         this.m_isHidden = var1.m_isHidden;
      }
   }

   public void setAuthorizedName(String var1) {
      if (this.m_authorizedName == null) {
         this.m_authorizedName = new AuthorizedName(var1);
      } else {
         this.m_authorizedName.setName(var1);
      }

   }

   public void setAuthorizedName(AuthorizedName var1) {
      this.m_authorizedName = var1;
   }

   public void setTModelKey(TModelKey var1) {
      this.m_tModelKey = var1;
   }

   public TModelKey getTModelKey() {
      return this.m_tModelKey;
   }

   public void setName(Name var1) {
      if (var1 != null) {
         var1.setLang((Language)null);
      }

      this.m_name = var1;
   }

   public Name getName() {
      return this.m_name;
   }

   public void setOverviewDoc(OverviewDoc var1) {
      this.m_overviewDoc = var1;
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public OverviewDoc getOverviewDoc() {
      return this.m_overviewDoc;
   }

   public Operator getOperator() {
      return this.m_operator;
   }

   public CategoryBag getCategoryBag() {
      return this.m_categoryBag;
   }

   public IdentifierBag getIdentifierBag() {
      return this.m_identifierBag;
   }

   public AuthorizedName getAuthorizedName() {
      return this.m_authorizedName;
   }

   public void setOperator(Operator var1) {
      this.m_operator = var1;
   }

   public void addIdentifier(KeyedReference var1) throws UDDIException {
      if (this.m_identifierBag == null) {
         this.m_identifierBag = new IdentifierBag();
      }

      this.m_identifierBag.add(var1, var1.getName());
   }

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.m_categoryBag == null) {
         this.m_categoryBag = new CategoryBag();
      }

      this.m_categoryBag.add(var1);
   }

   public void addDescription(Description var1) throws UDDIException {
      if (this.m_descriptions == null) {
         this.m_descriptions = new Descriptions();
      }

      this.m_descriptions.add(var1);
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.m_identifierBag = var1;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.m_categoryBag = var1;
   }

   public boolean isHidden() {
      return this.m_isHidden;
   }

   public void setHidden(boolean var1) {
      this.m_isHidden = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModel)) {
         return false;
      } else {
         TModel var2 = (TModel)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_categoryBag, (Object)var2.m_categoryBag);
         var3 &= Util.isEqual((Object)this.m_descriptions, (Object)var2.m_descriptions);
         var3 &= Util.isEqual((Object)this.m_identifierBag, (Object)var2.m_identifierBag);
         var3 &= Util.isEqual((Object)this.m_name, (Object)var2.m_name);
         var3 &= Util.isEqual((Object)this.m_overviewDoc, (Object)var2.m_overviewDoc);
         var3 &= Util.isEqual((Object)this.m_tModelKey, (Object)var2.m_tModelKey);
         var3 &= Util.isEqual((Object)this.m_authorizedName, (Object)var2.m_authorizedName);
         var3 &= Util.isEqual((Object)this.m_operator, (Object)var2.getOperator());
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<tModel tModelKey=\"");
      if (this.m_tModelKey != null) {
         var1.append(this.m_tModelKey.toString());
      }

      var1.append("\"");
      if (this.m_authorizedName != null) {
         var1.append(" authorizedName=\"").append(this.m_authorizedName.toString()).append("\"");
      }

      if (this.m_operator != null) {
         var1.append(" operator=\"").append(this.m_operator.toString()).append("\"");
      }

      var1.append(">");
      if (this.m_name != null) {
         var1.append(this.m_name.toXML());
      }

      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_overviewDoc != null) {
         var1.append(this.m_overviewDoc.toXML());
      }

      if (this.m_identifierBag != null) {
         var1.append(this.m_identifierBag.toXML());
      }

      if (this.m_categoryBag != null) {
         var1.append(this.m_categoryBag.toXML());
      }

      var1.append("</tModel>");
      return var1.toString();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("tModelKey:").append(this.m_tModelKey).append(", ");
      var1.append("authorizedName:").append(this.m_authorizedName).append(", ");
      var1.append("operator:").append(this.m_operator).append(", ");
      var1.append("name:").append(this.m_name).append(", ");
      var1.append("descriptions:").append(this.m_descriptions).append(", ");
      var1.append("overviewDoc:").append(this.m_overviewDoc).append(", ");
      var1.append("identifierBag:").append(this.m_identifierBag).append(", ");
      var1.append("categoryBag:").append(this.m_categoryBag);
      return var1.toString();
   }
}
