package weblogic.wsee.tools.source;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;

public class JsFault {
   private QName faultMessage;
   private String partName;
   private String exceptionClass;
   private String singleBuiltinExceptionWrapperClass;
   private List constructorElementNames;

   public JsFault(QName var1, String var2, String var3) {
      this.partName = var2;
      this.faultMessage = var1;
      this.exceptionClass = var3;
      this.constructorElementNames = new ArrayList(0);
   }

   public JsFault(QName var1, String var2, String var3, String var4, List var5) {
      this.partName = var2;
      this.faultMessage = var1;
      this.exceptionClass = var3;
      if (var5 == null) {
         throw new IllegalArgumentException("JsFault constructor for faultMessage='" + var1 + "', partName='" + var2 + "', exceptionClass='" + var3 + "' called with null constructorElementNames parameter passed in. ");
      } else {
         this.constructorElementNames = var5;
         this.singleBuiltinExceptionWrapperClass = var4;
      }
   }

   public String getPartName() {
      return this.partName;
   }

   public QName getFaultMessage() {
      return this.faultMessage;
   }

   public String getExceptionClass() {
      return this.exceptionClass;
   }

   public List getConstructorElementNames() {
      return this.constructorElementNames;
   }

   public String getSingleBuiltinExceptionWrapperClass() {
      return this.singleBuiltinExceptionWrapperClass;
   }

   public boolean isSingleBuiltinException() {
      return this.singleBuiltinExceptionWrapperClass != null && this.singleBuiltinExceptionWrapperClass.length() > 0;
   }

   public String getJsr109MappingFileExceptionClass() {
      return this.isSingleBuiltinException() ? this.getSingleBuiltinExceptionWrapperClass() : this.getExceptionClass();
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.faultMessage);
      var1.add(this.partName);
      var1.add(this.exceptionClass);
      var1.add(this.singleBuiltinExceptionWrapperClass);
      var1.add(this.constructorElementNames);
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsFault)) {
         return false;
      } else {
         JsFault var2 = (JsFault)var1;
         boolean var3 = ObjectUtil.equals(this.faultMessage, var2.faultMessage);
         var3 = var3 && ObjectUtil.equals(this.partName, var2.partName);
         var3 = var3 && ObjectUtil.equals(this.exceptionClass, var2.exceptionClass);
         var3 = var3 && ObjectUtil.equals(this.singleBuiltinExceptionWrapperClass, var2.singleBuiltinExceptionWrapperClass);
         var3 = var3 && ObjectUtil.equals(this.constructorElementNames, var2.constructorElementNames);
         return var3;
      }
   }
}
