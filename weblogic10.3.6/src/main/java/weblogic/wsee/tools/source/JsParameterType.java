package weblogic.wsee.tools.source;

import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class JsParameterType extends JsTypeBase {
   public static final int IN = 0;
   public static final int OUT = 1;
   public static final int INOUT = 2;
   private static final boolean verbose = Verbose.isVerbose(JsParameterType.class);
   private String nonHolderType;
   private String paramName;
   private int mode = 0;
   private boolean isSoapHeader = false;
   private QName element = null;
   private boolean docStyle = true;

   JsParameterType(String var1) {
      this.setType(var1);
   }

   public void setParamName(String var1) {
      this.paramName = var1;
   }

   public String getParamName() {
      return this.paramName;
   }

   public String getModeAsString() {
      switch (this.mode) {
         case 0:
            return "IN";
         case 1:
            return "OUT";
         case 2:
            return "INOUT";
         default:
            throw new AssertionError("unknown parameter mode " + this.mode);
      }
   }

   public int getMode() {
      return this.mode;
   }

   public void setMode(int var1) {
      this.mode = var1;
      switch (var1) {
         case 0:
         case 1:
         case 2:
            return;
         default:
            throw new IllegalArgumentException("Invalide parameter mode value " + var1);
      }
   }

   public boolean isSoapHeader() {
      return this.isSoapHeader;
   }

   public void setSoapHeader(boolean var1) {
      this.isSoapHeader = var1;
   }

   public String getNonHolderType() {
      return this.nonHolderType;
   }

   public void setNonHolderType(String var1) {
      this.nonHolderType = var1;
   }

   public void setElement(QName var1) {
      this.element = var1;
   }

   public QName getElement() {
      return this.element;
   }

   public boolean isDocStyle() {
      return this.docStyle;
   }

   public void setDocStyle(boolean var1) {
      this.docStyle = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("part name", this.getPartName());
      var1.writeField("type", this.getType());
      var1.end();
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(super.hashCode());
      var1.add(this.nonHolderType);
      var1.add(this.paramName);
      var1.add(this.mode);
      var1.add(this.isSoapHeader);
      var1.add(this.element);
      var1.add(this.docStyle);
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsParameterType)) {
         return false;
      } else {
         JsParameterType var2 = (JsParameterType)var1;
         boolean var3 = super.equals(var1);
         var3 = var3 && ObjectUtil.equals(this.nonHolderType, var2.nonHolderType);
         var3 = var3 && ObjectUtil.equals(this.paramName, var2.paramName);
         var3 = var3 && this.mode == var2.mode;
         var3 = var3 && this.isSoapHeader == var2.isSoapHeader;
         var3 = var3 && ObjectUtil.equals(this.element, var2.element);
         var3 = var3 && this.docStyle == var2.docStyle;
         return var3;
      }
   }
}
