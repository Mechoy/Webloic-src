package weblogic.application.internal.library.util;

public class NodeModificationException extends Exception {
   private final Type type;
   private final RONode node;

   public NodeModificationException(Type var1, RONode var2) {
      super(getExceptionMessage(var1));
      this.type = var1;
      this.node = var2;
   }

   public Type getType() {
      return this.type;
   }

   public RONode getNode() {
      return this.node;
   }

   public int getDepth() {
      return this.getNode().getDepth();
   }

   private static String getExceptionMessage(Type var0) {
      return var0.getMessage();
   }

   public static class Type {
      public static final Type ADDING_VALUE_TO_NON_LEAF_NODE = new Type("Trying to add value to a non-leaf Node");
      public static final Type ADDING_EDGE_TO_LEAF_NODE = new Type("addingEdgeToLeafNodeMessage");
      private final String message;

      private Type(String var1) {
         this.message = var1;
      }

      private String getMessage() {
         return this.message;
      }
   }
}
