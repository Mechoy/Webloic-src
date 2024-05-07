package weblogic.wsee.tools.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class JsMethod {
   private static final boolean verbose = Verbose.isVerbose(JsMethod.class);
   private QName operationName;
   private String methodName;
   private JsReturnType returnType = new JsReturnType("void");
   private ArrayList<JsParameterType> arguments = new ArrayList();
   private ArrayList<JsFault> faults = new ArrayList();
   private boolean isWrapped = false;
   private boolean isOneWay = false;
   private boolean isGenerateAsync = true;
   private String soapAction;
   private boolean isMimeBinding = false;
   private String style;
   private String use;

   JsMethod() {
   }

   public String getStyle() {
      return this.style;
   }

   public void setStyle(String var1) {
      this.style = var1;
   }

   public String getUse() {
      return this.use;
   }

   public void setUse(String var1) {
      this.use = var1;
   }

   public QName getOperationName() {
      return this.operationName;
   }

   public void setOperationName(QName var1) {
      this.operationName = var1;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public JsReturnType getReturnType() {
      return this.returnType;
   }

   public JsReturnType setReturnType(String var1) {
      this.returnType = new JsReturnType(var1);
      return this.returnType;
   }

   public boolean isWrapped() {
      return this.isWrapped;
   }

   public void setWrapped(boolean var1) {
      this.isWrapped = var1;
   }

   public boolean isOneWay() {
      return this.isOneWay;
   }

   public void setOneWay(boolean var1) {
      this.isOneWay = var1;
   }

   public boolean isGenerateAsync() {
      return this.isGenerateAsync;
   }

   public void setGenerateAsync(boolean var1) {
      this.isGenerateAsync = var1;
   }

   public void setSoapAction(String var1) {
      this.soapAction = var1;
   }

   public String getSoapAction() {
      return this.soapAction;
   }

   public JsParameterType[] getArguments() {
      return (JsParameterType[])((JsParameterType[])this.arguments.toArray(new JsParameterType[this.arguments.size()]));
   }

   public void resetArgument(ArrayList<JsParameterType> var1) {
      this.isMimeBinding = true;
      this.arguments = var1;
   }

   public JsFault[] getFaults() {
      return (JsFault[])((JsFault[])this.faults.toArray(new JsFault[this.faults.size()]));
   }

   public JsParameterType addArgument(String var1, String var2) {
      JsParameterType var3 = new JsParameterType(var2);
      var3.setPartName(var1);
      this.arguments.add(var3);
      return var3;
   }

   public void addJsParameterType(JsParameterType var1) {
      this.arguments.add(var1);
   }

   public JsFault addFault(QName var1, String var2, String var3) {
      JsFault var4 = new JsFault(var1, var2, var3);
      this.faults.add(var4);
      return var4;
   }

   public JsFault addFault(QName var1, String var2, String var3, String var4, List var5) {
      JsFault var6 = new JsFault(var1, var2, var3, var4, var5);
      this.faults.add(var6);
      return var6;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.methodName);
      var1.writeArray("arguments", this.arguments.iterator());
      var1.writeArray("faults", this.faults.iterator());
      var1.writeField("returnType", this.returnType);
      var1.end();
   }

   public String getArgumentString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.arguments.iterator();

      while(var2.hasNext()) {
         JsParameterType var3 = (JsParameterType)var2.next();
         var1.append(var3.getType());
         var1.append(" ");
         var1.append(var3.getParamName());
         if (var2.hasNext()) {
            var1.append(",");
         }
      }

      return var1.toString();
   }

   public String getAnnotatedArgumentString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.arguments.iterator();

      while(var2.hasNext()) {
         JsParameterType var3 = (JsParameterType)var2.next();
         var1.append("@WebParam(name=\"");
         if (var3.isDocStyle() && !this.isWrapped()) {
            QName var4 = var3.getElement();
            if (var4 != null) {
               var1.append(var4.getLocalPart());
               var1.append("\", targetNamespace=\"");
               var1.append(var4.getNamespaceURI());
               var1.append("\", partName=\"");
               var1.append(var3.getPartName());
            } else {
               var1.append(var3.getPartName());
            }
         } else {
            var1.append(var3.getPartName());
         }

         var1.append('"');
         if (var3.getMode() != 0) {
            var1.append(", mode=WebParam.Mode.");
            var1.append(var3.getModeAsString());
         }

         if (var3.isSoapHeader()) {
            var1.append(", header=true");
         }

         var1.append(") ");
         var1.append(var3.getType());
         var1.append(" ");
         var1.append(var3.getParamName());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public String getAnnotatedReturnTypeString() {
      if (this.returnType.getType().equals("void")) {
         return "";
      } else {
         String var1 = this.returnType.getPartName();
         if (this.returnType.isDocStyle() && !this.isWrapped()) {
            QName var2 = this.returnType.getElement();
            return var2 != null ? "@WebResult(name=\"" + var2.getLocalPart() + "\", targetNamespace=\"" + var2.getNamespaceURI() + "\")" : "@WebResult(name=\"" + var1 + "\")";
         } else {
            return !var1.equals("return") ? "@WebResult(name=\"" + var1 + "\")" : "";
         }
      }
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.getOperationName());
      var1.add(this.getMethodName());
      var1.add(this.getReturnType());
      var1.add(this.getArguments());
      var1.add(this.getFaults());
      var1.add(this.isWrapped());
      var1.add(this.isOneWay());
      var1.add(this.isGenerateAsync());
      var1.add(this.getSoapAction());
      var1.add(this.isMimeBinding);
      var1.add(this.getStyle());
      var1.add(this.getUse());
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsMethod)) {
         return false;
      } else {
         JsMethod var2 = (JsMethod)var1;
         boolean var3 = ObjectUtil.equals(this.getOperationName(), var2.getOperationName());
         var3 &= ObjectUtil.equals(this.getMethodName(), var2.getMethodName());
         var3 &= ObjectUtil.equals(this.getReturnType(), var2.getReturnType());
         var3 &= ObjectUtil.equals(this.getArguments(), var2.getArguments());
         var3 &= ObjectUtil.equals(this.getFaults(), var2.getFaults());
         var3 &= ObjectUtil.equals(this.isWrapped(), var2.isWrapped());
         var3 &= ObjectUtil.equals(this.isOneWay(), var2.isOneWay());
         var3 &= ObjectUtil.equals(this.isGenerateAsync(), var2.isGenerateAsync());
         var3 &= ObjectUtil.equals(this.getSoapAction(), var2.getSoapAction());
         var3 &= ObjectUtil.equals(this.isMimeBinding, var2.isMimeBinding);
         var3 &= ObjectUtil.equals(this.getStyle(), var2.getStyle());
         var3 &= ObjectUtil.equals(this.getUse(), var2.getUse());
         return var3;
      }
   }
}
