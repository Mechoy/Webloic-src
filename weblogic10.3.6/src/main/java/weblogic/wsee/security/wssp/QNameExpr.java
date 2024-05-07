package weblogic.wsee.security.wssp;

public interface QNameExpr {
   String getLocalName();

   String getNamespaceUri();

   boolean isOptional();
}
