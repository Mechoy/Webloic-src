package weblogic.wsee.util;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;

public class ExceptionInfo {
   private String simpleExceptionWrapperClassName = null;
   private String onWireClassName = null;
   private List constructorElementNames = null;

   ExceptionInfo(String var1, String var2, List var3) {
      this.simpleExceptionWrapperClassName = var1;
      this.onWireClassName = var2;
      this.constructorElementNames = var3;
   }

   public boolean isSimpleException() {
      return this.getSimpleExceptionWrapperClassName() != null;
   }

   public String getJsr109ExceptionClassName() {
      return this.isSimpleException() ? this.getSimpleExceptionWrapperClassName() : this.getOnWireClassName();
   }

   public String getSimpleExceptionWrapperClassName() {
      return this.simpleExceptionWrapperClassName;
   }

   public String getOnWireClassName() {
      return this.onWireClassName;
   }

   public List getConstructorElementNames() {
      return this.constructorElementNames;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ExceptionInfo  ---------------------------\n");
      var1.append("  isSimpleException = '").append(this.isSimpleException()).append("'\n");
      var1.append("  simpleExceptionWrapperClassname = '").append(this.simpleExceptionWrapperClassName).append("'\n");
      var1.append("  onWireClassName = '").append(this.onWireClassName).append("'\n");
      var1.append("  jsr109ExceptionClassName = '").append(this.getJsr109ExceptionClassName()).append("'\n");
      var1.append("  constructorElementNames\n");
      Iterator var2 = this.constructorElementNames.iterator();

      while(var2.hasNext()) {
         var1.append("    '").append((QName)var2.next()).append("'\n");
      }

      var1.append("   ---------------------------------------\n");
      return var1.toString();
   }
}
