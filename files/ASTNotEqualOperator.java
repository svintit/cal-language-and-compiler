/* Generated By:JJTree: Do not edit this line. ASTNotEqualOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTNotEqualOperator extends SimpleNode {
  public ASTNotEqualOperator(int id) {
    super(id);
  }

  public ASTNotEqualOperator(MyParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MyParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=93b9f247e50093aa66b08e7626e65cd7 (do not edit this line) */
