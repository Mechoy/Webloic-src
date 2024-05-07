package weblogic.application.internal.library.util;

import java.util.List;

public abstract class AbstractTraversal<Edge, Value> implements Traversal<Edge, Value> {
   public Edge getNextEdge(RONode<Edge, Value> var1) {
      return null;
   }

   public Edge[] getNextEdges(RONode<Edge, Value> var1) {
      return null;
   }

   public void visit(RONode<Edge, Value> var1, List<Edge> var2) {
   }

   public void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2) {
   }
}
