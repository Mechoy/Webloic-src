package weblogic.management.context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import javax.security.auth.Subject;

public class JMXContextImpl implements JMXContext {
   private static final long serialVersionUID = -6085646368327683332L;
   private static final int VERSION = 1;
   private Locale locale;
   private Subject subject;

   public Locale getLocale() {
      return this.locale;
   }

   public void setLocale(Locale var1) {
      this.locale = var1;
   }

   public Subject getSubject() {
      return this.subject;
   }

   public void setSubject(Subject var1) {
      this.subject = var1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeInt(1);
      var1.writeObject(this.locale);
      if (this.subject != null) {
         var1.writeBoolean(true);
         var1.writeObject(this.subject);
      } else {
         var1.writeBoolean(false);
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      this.locale = (Locale)var1.readObject();
      if (var1.readBoolean()) {
         this.subject = (Subject)var1.readObject();
      }

   }
}
