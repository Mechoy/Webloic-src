package weblogic.application.internal.library.util;

import java.util.List;

public interface Traversal<Edge, Value> {
   void visit(RONode<Edge, Value> var1, List<Edge> var2);

   void visitLeaf(RONode<Edge, Value> var1, List<Edge> var2);

   Edge getNextEdge(RONode<Edge, Value> var1);

   Edge[] getNextEdges(RONode<Edge, Value> var1);
}
