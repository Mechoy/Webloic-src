package weblogic.wsee.ws;

import weblogic.wsee.util.ToStringWriter;

public class WsParameterType extends WsType {
   private Class javaHolderType;

   WsParameterType(String var1, int var2) {
      super(var1, var2);
   }

   public void setHeader(boolean var1) {
      this.isHeader = var1;
   }

   public Class getJavaHolderType() {
      return this.javaHolderType;
   }

   public void setJavaHolderType(Class var1) {
      this.javaHolderType = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.getName());
      var1.writeField("mode", this.getModeAsString());
      var1.writeField("javaType", this.getJavaType());
      var1.writeField("xmlType", this.getXmlName());
      var1.writeField("isHeader", this.isHeader);
      var1.end();
   }
}
