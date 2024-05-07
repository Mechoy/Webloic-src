package weblogic.wsee.tools.source;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class JsBinding {
   private String name;
   private String namespaceURI;
   private String packageName;
   private ArrayList<JsMethod> methods = new ArrayList();
   private static final boolean verbose = Verbose.isVerbose(JsBinding.class);

   public String getName() {
      return this.name;
   }

   public String getQualifiedName() {
      StringBuilder var1 = new StringBuilder();
      if (!StringUtil.isEmpty(this.packageName)) {
         var1.append(this.packageName);
         var1.append(".");
      }

      var1.append(this.name);
      return var1.toString();
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setNamespaceURI(String var1) {
      this.namespaceURI = var1;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public JsMethod[] getMethods() {
      return (JsMethod[])this.methods.toArray(new JsMethod[this.methods.size()]);
   }

   public JsMethod addMethod(QName var1) {
      JsMethod var2 = new JsMethod();
      var2.setOperationName(var1);
      this.methods.add(var2);
      return var2;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeArray("methods", this.methods.iterator());
      var1.end();
   }

   public JsMethod getMethod(QName var1) {
      Iterator var2 = this.methods.iterator();

      JsMethod var3;
      do {
         if (!var2.hasNext()) {
            throw new IllegalArgumentException("Method not found: " + var1);
         }

         var3 = (JsMethod)var2.next();
      } while(!var3.getOperationName().equals(var1));

      return var3;
   }

   public JsMethod getMethod(String var1) {
      Iterator var2 = this.methods.iterator();

      JsMethod var3;
      do {
         if (!var2.hasNext()) {
            throw new IllegalArgumentException("Method not found: " + var1);
         }

         var3 = (JsMethod)var2.next();
      } while(!var3.getMethodName().equals(var1));

      return var3;
   }

   public boolean isStyleMixed() {
      if (this.methods.isEmpty()) {
         return false;
      } else {
         boolean var1 = ((JsMethod)this.methods.get(0)).isWrapped();

         for(int var2 = 1; var2 < this.methods.size(); ++var2) {
            if (((JsMethod)this.methods.get(var2)).isWrapped() != var1) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isBindingEqual(JsBinding var1) {
      boolean var2 = ObjectUtil.equals(this.name, var1.name);
      var2 = var2 && ObjectUtil.equals(this.namespaceURI, var1.namespaceURI);
      var2 = var2 && ObjectUtil.equals(this.packageName, var1.packageName);
      var2 = var2 && this.methods.equals(this.methods);
      return var2;
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.name);
      var1.add(this.namespaceURI);
      var1.add(this.packageName);
      var1.add(this.methods);
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsBinding)) {
         return false;
      } else {
         JsBinding var2 = (JsBinding)var1;
         return this.isBindingEqual(var2);
      }
   }
}
