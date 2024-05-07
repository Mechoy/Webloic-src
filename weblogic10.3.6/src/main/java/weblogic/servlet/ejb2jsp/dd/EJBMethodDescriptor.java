package weblogic.servlet.ejb2jsp.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.servlet.ejb2jsp.SourceMethodInfo;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class EJBMethodDescriptor implements ToXML {
   private String info;
   private String name;
   private String tagname;
   private String targetType;
   private String returnType;
   private String returnVarName;
   MethodParamDescriptor[] params;
   private boolean enabled;
   private boolean evalOut;

   public EJBMethodDescriptor() {
      this.info = this.name = this.tagname = this.targetType = this.returnType = "";
      this.enabled = true;
      this.params = new MethodParamDescriptor[0];
   }

   public EJBMethodDescriptor(Element var1) throws DOMProcessingException {
      String var2 = null;
      this.info = DOMUtils.getValueByTagName(var1, "info");
      this.name = DOMUtils.getValueByTagName(var1, "name");
      var2 = DOMUtils.getValueByTagName(var1, "enabled");
      if (!"false".equalsIgnoreCase(var2) && !"no".equalsIgnoreCase(var2)) {
         this.enabled = true;
      } else {
         this.enabled = false;
      }

      this.tagname = DOMUtils.getValueByTagName(var1, "tagname");
      this.targetType = DOMUtils.getValueByTagName(var1, "target-type");
      this.returnType = DOMUtils.getValueByTagName(var1, "return-type");
      List var3 = DOMUtils.getOptionalElementsByTagName(var1, "parameter");
      Iterator var4 = var3.iterator();
      int var5 = DOMUtils.getElementCount(var1, "parameter");
      this.params = new MethodParamDescriptor[var5];

      for(int var6 = 0; var4.hasNext(); this.params[var6++] = new MethodParamDescriptor((Element)var4.next())) {
      }

   }

   public String getInfo() {
      return this.info;
   }

   public void setInfo(String var1) {
      this.info = var1;
   }

   public String getSignature() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.getReturnType();
      int var3 = var2.lastIndexOf(46);
      if (var3 > 0) {
         var2 = var2.substring(var3 + 1);
      }

      var1.append(var2);
      String var4 = this.getTargetType();
      var3 = var4.lastIndexOf(46);
      if (var3 > 0) {
         var4 = var4.substring(var3 + 1);
      }

      var1.append(" " + var4 + ".");
      var1.append(this.getName() + "(");
      MethodParamDescriptor[] var5 = this.getParams();

      for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
         String var7 = var5[var6].getType();
         var3 = var7.lastIndexOf(46);
         if (var3 > 0) {
            var7 = var7.substring(var3 + 1);
         }

         var1.append(var7 + " " + var5[var6].getName());
         if (var6 != var5.length - 1) {
            var1.append(',');
         }
      }

      return var1.append(")").toString();
   }

   public void setSignature(String var1) {
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public String getTagName() {
      return this.tagname;
   }

   public void setTagName(String var1) {
      this.tagname = var1;
   }

   public String getTargetType() {
      return this.targetType;
   }

   public void setTargetType(String var1) {
      this.targetType = var1;
   }

   public String getReturnType() {
      return this.returnType;
   }

   public void setReturnType(String var1) {
      this.returnType = var1;
   }

   public String getReturnVarName() {
      return this.returnVarName;
   }

   public void setReturnVarName(String var1) {
      this.returnVarName = var1;
   }

   public MethodParamDescriptor[] getParams() {
      return this.params != null ? this.params : new MethodParamDescriptor[0];
   }

   public void setParams(MethodParamDescriptor[] var1) {
      if (var1 == null) {
         this.params = new MethodParamDescriptor[0];
      } else {
         this.params = (MethodParamDescriptor[])((MethodParamDescriptor[])var1.clone());
      }
   }

   public boolean isEvalOut() {
      return this.evalOut;
   }

   public void setEvalOut(boolean var1) {
      this.evalOut = var1;
   }

   static void p(String var0) {
      System.err.println("[EJBMethDesc]: " + var0);
   }

   public boolean isResolved() {
      MethodParamDescriptor[] var1 = this.getParams();
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (("arg" + var2).equals(var1[var2].getName())) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public String toString() {
      return this.getSignature();
   }

   public void resolveParamNames(SourceMethodInfo var1) {
      MethodParamDescriptor[] var2 = this.getParams();
      String[] var3 = var1.getParams()[1];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var2[var4].setName(var3[var4]);
      }

   }

   public void toXML(XMLWriter var1) {
      var1.println("<method>");
      var1.incrIndent();
      var1.println("<info>" + this.info + "</info>");
      var1.println("<name>" + this.name + "</name>");
      var1.println("<enabled>" + this.isEnabled() + "</enabled>");
      var1.println("<tagname>" + this.tagname + "</tagname>");
      var1.println("<target-type>" + this.targetType + "</target-type>");
      var1.println("<return-type>" + this.returnType + "</return-type>");
      var1.println("<return-variable-name>" + this.returnVarName + "</return-variable-name>");
      var1.println("<eval-out>" + this.isEvalOut() + "</eval-out>");

      for(int var2 = 0; this.params != null && var2 < this.params.length; ++var2) {
         this.params[var2].toXML(var1);
      }

      var1.decrIndent();
      var1.println("</method>");
   }
}
