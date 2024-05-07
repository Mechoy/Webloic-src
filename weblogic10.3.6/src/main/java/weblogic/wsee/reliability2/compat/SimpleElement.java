package weblogic.wsee.reliability2.compat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

public class SimpleElement {
   SimpleElement _parent;
   QName _name;
   List<SimpleElement> _children;
   Map<QName, List<SimpleElement>> _childNameToChildListMap;
   Map<QName, String> _attrNameToValueMap;
   StringBuffer _content;

   public SimpleElement(QName var1) {
      this._name = var1;
      this._children = new ArrayList();
      this._childNameToChildListMap = new HashMap();
      this._attrNameToValueMap = new HashMap();
      this._content = null;
   }

   public QName getName() {
      return this._name;
   }

   public SimpleElement getParent() {
      return this._parent;
   }

   void setParent(SimpleElement var1) {
      this._parent = var1;
   }

   public List<SimpleElement> getChildren() {
      return new ArrayList(this._children);
   }

   public Map<QName, List<SimpleElement>> getChildNameToChildMap() {
      return new HashMap(this._childNameToChildListMap);
   }

   public SimpleElement getChild(QName var1) {
      return this.getChild(var1, false);
   }

   public List<SimpleElement> getChildren(QName var1) {
      return (List)(!this._childNameToChildListMap.containsKey(var1) ? new ArrayList() : (List)this._childNameToChildListMap.get(var1));
   }

   public SimpleElement getChild(QName var1, boolean var2) {
      if (!this._childNameToChildListMap.containsKey(var1)) {
         if (var2) {
            throw new IllegalStateException("Required child " + var1 + " not found in parent element " + this._name);
         } else {
            return null;
         }
      } else {
         return (SimpleElement)((List)this._childNameToChildListMap.get(var1)).get(0);
      }
   }

   public String getContentForChild(QName var1) {
      try {
         return this.getContentForChild(var1, false);
      } catch (IllegalStateException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public String getContentForChild(QName var1, boolean var2) throws IllegalStateException {
      SimpleElement var3 = this.getChild(var1, var2);
      String var4 = null;
      if (var3 != null) {
         var4 = var3.getContent();
      }

      return var4;
   }

   public void addChild(SimpleElement var1) {
      var1.setParent(this);
      this._children.add(var1);
      Object var2 = (List)this._childNameToChildListMap.get(var1._name);
      if (var2 == null) {
         var2 = new ArrayList();
         this._childNameToChildListMap.put(var1._name, var2);
      }

      ((List)var2).add(var1);
   }

   public Map<QName, String> getAttrs() {
      return new HashMap(this._attrNameToValueMap);
   }

   public void setAttr(QName var1, String var2) {
      this._attrNameToValueMap.put(var1, var2);
   }

   public void setAttr(String var1, String var2, String var3) {
      QName var4 = getQName(var1, var2);
      this.setAttr(var4, var3);
   }

   public String getAttr(QName var1) {
      return (String)this._attrNameToValueMap.get(var1);
   }

   public String getContent() {
      return this._content != null ? this._content.toString() : null;
   }

   public void setContent(String var1) {
      if (var1 != null) {
         this._content = new StringBuffer(var1);
      } else {
         this._content = null;
      }

   }

   public void addContent(String var1) {
      if (var1 != null) {
         if (this._content == null) {
            this._content = new StringBuffer();
         }

         this._content.append(var1);
      }

   }

   public static String getContentForChild(SimpleElement var0, String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return var0.getContentForChild(var3, true);
   }

   public static String getOptionalContentForChild(SimpleElement var0, String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return var0.getContentForChild(var3, false);
   }

   public List<SimpleElement> getChildren(String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return this.getChildren(var3);
   }

   public static SimpleElement getChild(SimpleElement var0, String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return var0.getChild(var3, true);
   }

   public static SimpleElement getOptionalChild(SimpleElement var0, String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return var0.getChild(var3, false);
   }

   public String getAttr(String var1, String var2) {
      QName var3 = getQName(var1, var2);
      return this.getAttr(var3);
   }

   public static void addChild(SimpleElement var0, String var1, String var2, String var3) {
      SimpleElement var4 = createChild(var0, var1, var2);
      new SimpleElement(new QName(var1, var2));
      var4.setContent(var3);
   }

   public static SimpleElement createChild(SimpleElement var0, String var1, String var2) {
      QName var3 = getQName(var1, var2);
      SimpleElement var4 = new SimpleElement(var3);
      var0.addChild(var4);
      return var4;
   }

   private static QName getQName(String var0, String var1) {
      int var2 = var1.indexOf(":");
      QName var3;
      if (var2 > 0) {
         var3 = new QName(var0, var1.substring(var2 + 1), var1.substring(0, var2));
      } else {
         var3 = new QName(var0, var1);
      }

      return var3;
   }
}
