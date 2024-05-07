package weblogic.wsee.bind.buildtime;

import com.bea.staxb.buildtime.internal.logger.Message;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BindingException extends RuntimeException {
   private List<Message> errorMessages = new ArrayList();

   public BindingException(List<Message> var1) {
      this.errorMessages = var1;
   }

   public String getMessage() {
      StringBuilder var1 = new StringBuilder();

      Message var3;
      for(Iterator var2 = this.errorMessages.iterator(); var2.hasNext(); var1.append(var3.getMessage())) {
         var3 = (Message)var2.next();
         if (var1.length() > 0) {
            var1.append("\n");
         }
      }

      return var1.toString();
   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
      Iterator var2 = this.errorMessages.iterator();

      while(var2.hasNext()) {
         Message var3 = (Message)var2.next();
         if (var3.getException() != null) {
            var1.println("Binding exception:");
            var3.getException().printStackTrace(var1);
         }
      }

   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      Iterator var2 = this.errorMessages.iterator();

      while(var2.hasNext()) {
         Message var3 = (Message)var2.next();
         if (var3.getException() != null) {
            var1.println("Binding exception:");
            var3.getException().printStackTrace(var1);
         }
      }

   }
}
