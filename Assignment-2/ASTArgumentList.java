/* Generated By:JJTree: Do not edit this line. ASTArgumentList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArgumentList extends SimpleNode {
  public ASTArgumentList(int id) {
    super(id);
  }

  public ASTArgumentList(MyParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MyParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=43a85b8a7f824a2d1e68ed0c083c2efc (do not edit this line) */