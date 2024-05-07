package weblogic.application.internal.library.util;

import java.util.Set;

public interface RONode<Edge, Value> {
   int getDepth();

   boolean hasEdge(Edge var1);

   Value getValue();

   Set<Edge> getEdges();

   Edge getHighestEdge();

   boolean isLeafNode();

   boolean hasValue();

   boolean hasChildren();

   int getNumChildren();
}
