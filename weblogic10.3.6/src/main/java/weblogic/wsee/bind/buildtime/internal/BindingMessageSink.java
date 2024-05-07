package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.logger.Message;
import com.bea.staxb.buildtime.internal.logger.SimpleMessageSink;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BindingMessageSink extends SimpleMessageSink {
   private List<Message> errorMessages = new ArrayList();

   public void log(Message var1) {
      super.log(var1);
      if (var1.getLevel().equals(Level.SEVERE)) {
         this.errorMessages.add(var1);
      }

   }

   public List<Message> getErrorMessages() {
      return this.errorMessages;
   }
}
