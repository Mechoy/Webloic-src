package weblogic.xml.security.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.utils.collections.ArraySet;
import weblogic.utils.collections.Stack;

public class ScopedNamespaceContext {
   public static final String DEFAULT_NAMESPACE = "";
   private final Stack scopeStack;
   private LLNamespaceContext current;

   public ScopedNamespaceContext() {
      this((LLNamespaceContext)null, new Stack());
   }

   private ScopedNamespaceContext(LLNamespaceContext var1, Stack var2) {
      this.current = null;
      this.current = var1;
      this.scopeStack = var2;
   }

   public void clear() {
      this.scopeStack.clear();
      this.current = null;
   }

   public String getNamespaceURI(String var1) {
      return this.current == null ? null : this.current.getNamespaceURI(var1);
   }

   public String getPrefix(String var1) {
      return this.current == null ? null : this.current.getPrefix(var1);
   }

   public Iterator getPrefixes(String var1) {
      return this.current == null ? Collections.EMPTY_SET.iterator() : this.current.getPrefixes(var1);
   }

   public boolean scopeOpened() {
      return !this.scopeStack.isEmpty();
   }

   public void openScope() {
      this.scopeStack.push(this.current);
   }

   public void closeScope() {
      if (!this.scopeOpened()) {
         throw new IllegalStateException("no scope currently open");
      } else {
         LLNamespaceContext var1 = (LLNamespaceContext)this.scopeStack.pop();
         this.current = var1;
      }
   }

   public void bindNamespace(String var1, String var2) {
      if (var1 != "" && var1 != null) {
         this.bindNamespaceInternal(var1, var2);
      } else {
         throw new IllegalArgumentException("Cannot use empty string or null as a prefix; use bindDefaultNamespace() to bind the default namespace");
      }
   }

   private void bindNamespaceInternal(String var1, String var2) {
      if (!this.scopeOpened()) {
         throw new IllegalStateException("no scope currently open");
      } else if (var2 == null) {
         throw new IllegalArgumentException("null namespace not allowed");
      } else {
         this.current = new LLNamespaceContext(var1, var2, this.current);
      }
   }

   public void unbindNamespace(String var1) {
      this.bindNamespace(var1, (String)null);
   }

   public void bindDefaultNamespace(String var1) {
      this.bindNamespaceInternal("", var1);
   }

   public void unbindDefaultNamespace() {
      this.bindDefaultNamespace((String)null);
   }

   public int getDepth() {
      return this.scopeStack.size();
   }

   public ScopedNamespaceContext copy() {
      return new ScopedNamespaceContext(this.current, (Stack)this.scopeStack.clone());
   }

   public Map getNamespaceMap() {
      return getNamespaceMap(this.current);
   }

   private static final Map getNamespaceMap(LLNamespaceContext var0) {
      if (var0 == null) {
         return new HashMap();
      } else {
         Map var1 = getNamespaceMap(var0.predecessor);
         var1.put(var0.prefix, var0.namespace);
         return var1;
      }
   }

   private static class LLNamespaceContext {
      private final String prefix;
      private final String namespace;
      private final LLNamespaceContext predecessor;

      public LLNamespaceContext(String var1, String var2, LLNamespaceContext var3) {
         this.prefix = var1;
         this.namespace = var2;
         this.predecessor = var3;
      }

      public final String getNamespace() {
         return this.namespace;
      }

      public final String getPrefix() {
         return this.prefix;
      }

      protected final LLNamespaceContext getPredecessor() {
         return this.predecessor;
      }

      public String getNamespaceURI(String var1) {
         for(LLNamespaceContext var2 = this; var2 != null; var2 = var2.predecessor) {
            if (var1.equals(var2.prefix)) {
               return var2.namespace;
            }
         }

         return null;
      }

      public String getPrefix(String var1) {
         for(LLNamespaceContext var2 = this; var2 != null; var2 = var2.predecessor) {
            if (var1.equals(var2.namespace)) {
               return var2.prefix;
            }
         }

         return null;
      }

      public Iterator getPrefixes(String var1) {
         Set var2 = getPrefixSet(var1, this);
         return var2.iterator();
      }

      protected static final Set getPrefixSet(String var0, LLNamespaceContext var1) {
         Stack var2 = new Stack(16);

         LLNamespaceContext var3;
         for(var3 = var1; var3 != null; var3 = var3.predecessor) {
            var2.push(var3);
         }

         ArraySet var4 = new ArraySet(4);

         while(!var2.isEmpty()) {
            var3 = (LLNamespaceContext)var2.pop();
            if (var0.equals(var3.namespace)) {
               var4.add(var3.prefix);
            } else {
               var4.remove(var3.prefix);
            }
         }

         return var4;
      }
   }
}
