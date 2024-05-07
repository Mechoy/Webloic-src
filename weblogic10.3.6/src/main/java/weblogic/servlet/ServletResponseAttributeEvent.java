package weblogic.servlet;

import java.util.EventObject;
import javax.servlet.ServletResponse;

public class ServletResponseAttributeEvent extends EventObject {
   public static final String ATTR_ENCODING = "ENCODING";
   private String name;
   private Object value;

   public ServletResponseAttributeEvent(ServletResponse var1, String var2, Object var3) {
      super(var1);
      this.name = var2;
      this.value = var3;
   }

   public ServletResponse getResponse() {
      return (ServletResponse)this.getSource();
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }
}
