package weblogic.xml.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.AssertionError;

public class ProcessingContext extends Node {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private boolean referenced = false;
   private final Map bound = new HashMap();

   public ProcessingContext(String var1) throws XMLProcessingException {
      super(var1);
   }

   public ProcessingContext(ProcessingContext var1, String var2) throws XMLProcessingException {
      super(var1, var2);
   }

   public ProcessingContext newTextNode() {
      ProcessingContext var1 = this.newElementNode("#text");
      var1.referenced = true;
      return var1;
   }

   public ProcessingContext newElementNode(String var1) {
      ProcessingContext var2 = null;

      try {
         var2 = new ProcessingContext(this, var1);
      } catch (XMLProcessingException var4) {
         throw new AssertionError(var4);
      }

      var2.bound.putAll(this.getAllBoundObjects());
      return var2;
   }

   public void addBoundObject(Object var1, String var2) {
      this.bound.put(var2, var1);
   }

   public void addBoundObjects(Map var1) {
      this.bound.putAll(var1);
   }

   public Object getBoundObject(String var1) {
      return this.bound.get(var1);
   }

   public Map getAllBoundObjects() {
      return this.bound;
   }

   public void setReferenced(boolean var1) {
      this.referenced = var1;
   }

   public boolean referenced() {
      return this.referenced;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("name = " + this.name);
      var1.append("\npath = " + this.path);
      var1.append("\nvalue = " + this.value);
      var1.append("\nbound objects = ");
      Iterator var2 = this.bound.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = this.bound.get(var3);
         var1.append("\n\t" + var3 + " = " + var4.getClass().getName() + "[" + var4.hashCode() + "]");
      }

      return var1.toString();
   }
}
