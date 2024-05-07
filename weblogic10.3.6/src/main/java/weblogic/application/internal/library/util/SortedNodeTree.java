package weblogic.application.internal.library.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SortedNodeTree<Edge, Value> {
   public static final boolean VALUES_ONLY_IN_LEAF_NODES = true;
   private final Node<Edge, Value> root = new Node((Node)null);

   public Value remove(Edge[] var1) {
      Node var2 = this.getNode(var1);
      if (var2 == null) {
         return null;
      } else {
         Object var3 = var2.getValue();
         this.killOnlyChild(var1, var1.length, var2);
         return var3;
      }
   }

   public Value put(Edge[] var1, Value var2) throws NodeModificationException {
      Node var3 = this.createPathTraversal(var1, 0, this.root);
      if (!var3.isLeafNode()) {
         throw new NodeModificationException(NodeModificationException.Type.ADDING_VALUE_TO_NON_LEAF_NODE, var3);
      } else {
         return var3.setVal(var2);
      }
   }

   public boolean hasElement(Edge[] var1) {
      return this.get(var1) != null;
   }

   public int size() {
      SizeTraversal var1 = new SizeTraversal();
      this.traverse(var1);
      return var1.size();
   }

   public Collection<Value> getAll() {
      ArrayList var1 = new ArrayList();
      this.getAll(var1);
      return var1;
   }

   public void getAll(Collection<Value> var1) {
      this.traverse(new GathererTraversal(var1));
   }

   public String toString() {
      if (this.root.getNumChildren() == 0) {
         return "[]";
      } else {
         StringTraversal var1 = new StringTraversal();
         this.traverse(var1);
         return var1.toString();
      }
   }

   public void traverse(Traversal<Edge, Value> var1) {
      this.traverse(var1, this.root, new ArrayList());
   }

   private void killOnlyChild(Edge[] var1, int var2, Node<Edge, Value> var3) {
      Node var4 = var3.getParent();
      Object var5 = var1[var2 - 1];
      var4.remove(var5);
      if (!var4.isRoot() && var4.getNumChildren() == 0) {
         if (!var4.hasValue()) {
            this.killOnlyChild(var1, var2 - 1, var4);
         } else {
            assert false;
         }
      }

   }

   private Value get(Edge[] var1) {
      Node var2 = this.getNode(var1);
      return var2 == null ? null : var2.getValue();
   }

   private Node<Edge, Value> getNode(Edge[] var1) {
      ExactMatchTraversal var2 = new ExactMatchTraversal(var1);
      this.traverse(var2);
      return var2.getMatchNode();
   }

   private Node<Edge, Value> createPathTraversal(Edge[] var1, int var2, Node<Edge, Value> var3) throws NodeModificationException {
      Node var4 = var3.getOrCreateChild(var1[var2]);
      if (var2 == var1.length - 1) {
         return var4;
      } else if (var4.hasValue() && var4.isLeafNode()) {
         throw new NodeModificationException(NodeModificationException.Type.ADDING_EDGE_TO_LEAF_NODE, var4);
      } else {
         return this.createPathTraversal(var1, var2 + 1, var4);
      }
   }

   private void traverse(Traversal<Edge, Value> var1, Node<Edge, Value> var2, List<Edge> var3) {
      if (var2.isLeafNode()) {
         var1.visitLeaf(var2, var3);
      } else {
         var1.visit(var2, var3);
         Object var4 = var1.getNextEdge(var2);
         if (var4 != null) {
            this.traverse(var1, var2, var4, var3);
         } else {
            Object[] var5 = var1.getNextEdges(var2);
            if (var5 != null) {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  this.traverse(var1, var2, var5[var6], var3);
                  var3.remove(var3.size() - 1);
               }
            }
         }
      }

   }

   private void traverse(Traversal<Edge, Value> var1, Node<Edge, Value> var2, Edge var3, List<Edge> var4) {
      try {
         if (!var2.hasEdge(var3)) {
            this.throwUnknownEdgeException(var3, var2, (ClassCastException)null);
         }
      } catch (ClassCastException var6) {
         this.throwUnknownEdgeException(var3, var2, var6);
      }

      var4.add(var3);
      this.traverse(var1, var2.getChild(var3), var4);
   }

   private void throwUnknownEdgeException(Edge var1, Node<Edge, Value> var2, ClassCastException var3) {
      throw new UnknownEdgeRuntimeException("Unknown edge: " + String.valueOf(var1) + ". Current " + "Node has edges: " + var2.getEdges() + ".", var3);
   }

   private static class UnknownEdgeRuntimeException extends RuntimeException {
      private static final long serialVersionUID = 3240595793558570615L;

      public UnknownEdgeRuntimeException(String var1) {
         super(var1);
      }

      public UnknownEdgeRuntimeException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   private class SizeTraversal extends AbstractTraversal<Edge, Value> implements Traversal<Edge, Value> {
      private int numLeafNodes;

      private SizeTraversal() {
         this.numLeafNodes = 0;
      }

      public Edge[] getNextEdges(RONode<Edge, Value> var1) {
         return (Object[])var1.getEdges().toArray();
      }

      public void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2) {
         if (var1.hasValue()) {
            ++this.numLeafNodes;
         }

      }

      public int size() {
         return this.numLeafNodes;
      }

      // $FF: synthetic method
      SizeTraversal(Object var2) {
         this();
      }
   }

   private class StringTraversal extends AbstractTraversal<Edge, Value> implements Traversal<Edge, Value> {
      private final StringBuffer sb;

      private StringTraversal() {
         this.sb = new StringBuffer();
      }

      public Edge[] getNextEdges(RONode<Edge, Value> var1) {
         return (Object[])var1.getEdges().toArray();
      }

      public void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2) {
         this.sb.append(var2 + " -> " + var1.getValue()).append("\n");
      }

      public String toString() {
         return this.sb.toString();
      }

      // $FF: synthetic method
      StringTraversal(Object var2) {
         this();
      }
   }

   private class GathererTraversal extends AbstractTraversal<Edge, Value> implements Traversal<Edge, Value> {
      private final Collection<Value> rtn;

      public GathererTraversal(Collection<Value> var2) {
         this.rtn = var2;
      }

      public Edge[] getNextEdges(RONode<Edge, Value> var1) {
         return (Object[])var1.getEdges().toArray();
      }

      public void visit(RONode<Edge, Value> var1, List<Edge> var2) {
         if (var1.hasValue()) {
            this.rtn.add(var1.getValue());
         }

      }

      public void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2) {
         if (var1.hasValue()) {
            this.rtn.add(var1.getValue());
         }

      }
   }

   private class ExactMatchTraversal extends AbstractTraversal<Edge, Value> implements Traversal<Edge, Value> {
      private Edge[] edges = null;
      private RONode<Edge, Value> matchNode = null;
      private int edgeIndex = -1;

      public ExactMatchTraversal(Edge[] var2) {
         this.edges = var2;
      }

      public Edge getNextEdge(RONode<Edge, Value> var1) {
         ++this.edgeIndex;
         if (this.edgeIndex == this.edges.length) {
            return null;
         } else {
            return this.edges[this.edgeIndex] != null && var1.hasEdge(this.edges[this.edgeIndex]) ? this.edges[this.edgeIndex] : null;
         }
      }

      private Node<Edge, Value> getMatchNode() {
         return (Node)this.matchNode;
      }

      public void visit(RONode<Edge, Value> var1, List<Edge> var2) {
      }

      public void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2) {
         if (this.edgeIndex == this.edges.length - 1) {
            this.matchNode = var1;
         }

      }
   }
}
