package weblogic.xml.util;

import java.util.Hashtable;
import java.util.Stack;

public class NameSpaceContext {
   Hashtable current = new Hashtable();
   Stack contexts = new Stack();

   public final void addNameSpace(Atom var1, Atom var2) {
      this.current.put(var1, var2);
   }

   public final Atom findNameSpace(Atom var1) {
      return (Atom)this.current.get(var1);
   }

   public final void push() {
      this.contexts.push(this.current);
      this.current = (Hashtable)this.current.clone();
   }

   public final void pop() {
      this.current = (Hashtable)this.contexts.pop();
   }
}
