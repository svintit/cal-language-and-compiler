/* Generated By:JJTree: Do not edit this line. ASTLessThanOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTLessThanOperator extends SimpleNode {
  public ASTLessThanOperator(int id) {
    super(id);
  }

  public ASTLessThanOperator(MyParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MyParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=db549c641f21b10379b6fafaaf940e4a (do not edit this line) */
