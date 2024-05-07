package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;

public final class WLNameParser implements NameParser, Externalizable {
   private static final long serialVersionUID = 8031032025200514222L;
   private static final Properties defaultProps = new Properties();
   private Properties nameProps;

   public WLNameParser() {
      this.nameProps = defaultProps;
   }

   public WLNameParser(String var1) {
      this.nameProps = (Properties)defaultProps.clone();
      if (var1.length() > 0) {
         this.nameProps.put("jndi.syntax.separator", "" + var1.charAt(0));
         if (var1.length() > 1) {
            this.nameProps.put("jndi.syntax.separator2", "" + var1.charAt(1));
         }
      } else {
         this.nameProps.put("jndi.syntax.ignorecase", "false");
         this.nameProps.put("jndi.syntax.direction", "flat");
      }

   }

   public Name parse(String var1) throws NamingException {
      return new CompoundName(var1, this.nameProps);
   }

   public static Name defaultParse(String var0) throws NamingException {
      return new CompoundName(var0, defaultProps);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (var1 instanceof WLObjectOutput) {
         ((WLObjectOutput)var1).writeProperties(this.nameProps);
      } else {
         var1.writeObject(this.nameProps);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      if (var1 instanceof WLObjectInput) {
         this.nameProps = ((WLObjectInput)var1).readProperties();
      } else {
         this.nameProps = (Properties)var1.readObject();
      }

   }

   static {
      defaultProps.put("jndi.syntax.direction", "left_to_right");
      defaultProps.put("jndi.syntax.separator", ".");
      defaultProps.put("jndi.syntax.ignorecase", "false");
      defaultProps.put("jndi.syntax.escape", "\\");
      defaultProps.put("jndi.syntax.beginquote", "\"");
      defaultProps.put("jndi.syntax.endquote", "\"");
      defaultProps.put("jndi.syntax.beginquote2", "'");
      defaultProps.put("jndi.syntax.endquote2", "'");
      defaultProps.put("jndi.syntax.separator.ava", ",");
      defaultProps.put("jndi.syntax.separator.typeval", "=");
   }
}
