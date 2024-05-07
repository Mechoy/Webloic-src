package weblogic.application.internal.library.util;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import weblogic.utils.NestedRuntimeException;

public class Node<Edge, Value> implements RONode<Edge, Value> {
   private final SortedMap<Edge, Node<Edge, Value>> edges = new TreeMap();
   private final Node<Edge, Value> parent;
   private Value value = null;

   Node(Node<Edge, Value> var1) {
      this.parent = var1;
   }

   public boolean isRoot() {
      return this.parent == null;
   }

   public int getDepth() {
      int var1 = 1;

      for(Node var2 = this; !var2.isRoot(); ++var1) {
         var2 = var2.getParent();
      }

      return var1;
   }

   public Node<Edge, Value> remove(Edge var1) {
      return (Node)this.edges.remove(var1);
   }

   public boolean hasEdge(Edge var1) {
      return this.edges.containsKey(var1);
   }

   public Value getValue() {
      return this.value;
   }

   public Value setVal(Value var1) {
      Object var2 = null;
      var2 = this.value;
      this.value = var1;
      return var2;
   }

   public Node<Edge, Value> getParent() {
      return this.parent;
   }

   public Node<Edge, Value> getChild(Edge var1) {
      return (Node)this.edges.get(var1);
   }

   public int getNumChildren() {
      return this.edges.size();
   }

   public Node<Edge, Value> getOrCreateChild(Edge var1) {
      Node var2 = (Node)this.edges.get(var1);
      if (var2 == null) {
         var2 = new Node(this);
         this.edges.put(var1, var2);
      }

      return var2;
   }

   public Set<Edge> getEdges() {
      return this.edges.keySet();
   }

   public boolean isLeafNode() {
      return this.edges.keySet().isEmpty();
   }

   public boolean hasValue() {
      return this.value != null;
   }

   public String toString() {
      return this.isLeafNode() ? "leaf(" + String.valueOf(this.value) + ")" : "not a leaf";
   }

   public boolean hasChildren() {
      return this.getNumChildren() != 0;
   }

   public Edge getHighestEdge() {
      if (this.isLeafNode()) {
         throw new NoEdgesRuntimeException("No edges to traverse");
      } else {
         return this.edges.lastKey();
      }
   }

   private static class NoEdgesRuntimeException extends NestedRuntimeException {
      private static final long serialVersionUID = 4299692877739743461L;

      public NoEdgesRuntimeException(String var1) {
         super(var1);
      }
   }
}
