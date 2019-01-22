import java.util.*;

public class TypeCheckVisitor implements MyParserVisitor {
    private static String scope = "global";

    // In order as asked for.
    private static boolean declared_in_scope = true;
    private static boolean no_dup_id = true;
    private static boolean correct_assigned_type = true;
    private static boolean correct_arith_arg = true;
    private static boolean correct_bool_arg = true;
    private static LinkedList<String> id_without_func = new LinkedList<>();

    private static boolean correct_func_arg_number = true;
    private static Hashtable<String, Integer> param_in_func = new Hashtable<>();

    private static boolean written_to = true;
    private static boolean read_from = true;
    
    private static boolean all_func_called = true;
    private static Hashtable<String, Integer> call_func = new Hashtable<>();
    
    private void WriteOrRead (Object var, Object data, String op) {
        Hashtable ht = (Hashtable) data;
        Hashtable scopeHT = (Hashtable)ht.get(scope);
        /*
        STC stc = (STC)scopeHT.get(var);

        if (stc == null) {
            Hashtable scopeGlobal = (Hashtable)ht.get("global");
            stc = (STC)scopeGlobal.get(var);

            if (stc == null) {
                return;
            }
        }

        if (op.equals("write")) {
            stc.write();
        }
        else if (op.equals("read")) {
            stc.read();
        }
        else {
            System.out.println("*ERROR* That is not a permitted operation.");
        }
        */
    }

    private void ReadCheck (SimpleNode node, Object data) {
        SimpleNode sn0 = (SimpleNode)node.jjtGetChild(0);
        if (sn0.toString().equals("Identifier")) {
            WriteOrRead(sn0.jjtGetValue(), data, "read");
        }

        SimpleNode sn1 = (SimpleNode)node.jjtGetChild(1);
        if (sn1.toString().equals("Identifier")) {
            WriteOrRead(sn1.jjtGetValue(), data, "read");
        }
    }

    private DataType EqualOpType(SimpleNode node, Object data) {
        DataType dt0 = (DataType)node.jjtGetChild(0).jjtAccept(this, data);
        DataType dt1 = (DataType)node.jjtGetChild(1).jjtAccept(this, data);

        if ((dt0 != DataType.Number) && (dt1 != DataType.Number)) {
            correct_assigned_type = false;
            System.out.printf("------ \n");
            System.out.printf("*FAIL* - Argument *%s* is assigned a value of incorrect type. ", node);
            System.out.printf("*%s* != *%s*\n", dt0, dt1);
            return DataType.TypeUnknown;
        }
        else {
            return DataType.Boolean;
        }
    }

    private DataType ArithOpType(SimpleNode node, Object data) {
        DataType dt0 = (DataType)node.jjtGetChild(0).jjtAccept(this, data);
        DataType dt1 = (DataType)node.jjtGetChild(1).jjtAccept(this, data);

        if ((dt0 != DataType.Number) && (dt1 != DataType.Number)) {
            correct_arith_arg = false;
            System.out.printf("------ \n");
            System.out.printf("*FAIL* - Argument *%s* is assigned a value of incorrect type.", node);
            System.out.printf("*%s* != *%s*\n", dt0, dt1);
            return DataType.TypeUnknown;
        }
        else {
            return DataType.Number;
        }
    }

    private DataType RelationOpType(SimpleNode node, Object data) {
        DataType dt0 = (DataType)node.jjtGetChild(0).jjtAccept(this, data);
        DataType dt1 = (DataType)node.jjtGetChild(1).jjtAccept(this, data);

        if ((dt0 != DataType.Number) && (dt1 != DataType.Number)) {
            correct_bool_arg = false;
            System.out.printf("------ ");
            System.out.printf("*FAIL* - Argument *%s* is assigned a value of incorrect type. ", node);
            System.out.printf("*%s* != *%s*\n", dt0, dt1);
            return DataType.TypeUnknown;
        }
        else {
            return DataType.Boolean;
        }
    }

    public Object visit(SimpleNode node, Object data) {
        throw new RuntimeException("Visit SimpleNode");
    }

    public Object visit(ASTProgram node, Object data) {
        node.childrenAccept(this, data);

        if (declared_in_scope) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All ID's declared before use.\n");
        }

        Hashtable ht = (Hashtable)data;
        Enumeration ht_keys = ht.keys();

        while (ht_keys.hasMoreElements()) {
            String scope = (String)ht_keys.nextElement();
            Hashtable ht_scope = (Hashtable)ht.get(scope);
            Enumeration in_key = ht_scope.keys();

            while (in_key.hasMoreElements()) {
                String id = (String)in_key.nextElement();
                
                if (ht_scope.contains(id)) {
                    System.out.printf("------\n");
                    System.out.printf("*FAIL* - ID *%s* is already defined in scope *%s*\n", id, scope);
                    no_dup_id = false;
                }
            }
        }

        if (no_dup_id) {
            System.out.printf("------\n");
            System.out.printf("*PASS* - All ID's only declared once in each scope.\n");
        }

        if (correct_assigned_type) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All functions assigned a variable of correct type.\n");
        }

        if (correct_arith_arg) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All arithmetic operations are correct.\n");
        }

        if (correct_bool_arg) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All boolean operations are correct.\n");
        }

        if (id_without_func.size() == 0) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All invoked identifiers have a function.\n");
        }
        else {
            for (String id: id_without_func) {
                System.out.print("------\n");
                System.out.printf("*FAIL* - ID *%s* does not have a function.\n", id);
            }
        }

        if (correct_func_arg_number) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All functions are called with the correct number of arguments.\n");
        }

        if (written_to) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All variables were written to.\n");
        }

        if (read_from) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All variables were read from.\n");
        }

        for (String fs : call_func.keySet()) {
            if (call_func.get(fs) == 0) {
                System.out.print("------\n");
                System.out.printf("*FAIL* - The function *%s* was declared but never called.\n", fs);
                all_func_called = false;
            }
        }

        if (all_func_called) {
            System.out.printf("------\n");
            System.out.print("*PASS* - All functions were called.\n");
        }

        System.out.printf("------\n");

        return DataType.Program;
    }
    
    public Object visit(ASTVariableDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return DataType.VariableDeclaration;
    }

    public Object visit(ASTConstantDeclaration node, Object data) {
        node.childrenAccept(this, data);
        int idi = 0;
        int typei = 1;
        int vali = 2;

        while (vali < node.jjtGetNumChildren()) {
            SimpleNode sn0 = (SimpleNode)node.jjtGetChild(0);
            DataType dt1 = (DataType)node.jjtGetChild(1).jjtAccept(this, data);
            DataType dt2 = (DataType)node.jjtGetChild(2).jjtAccept(this, data);

            if (dt1 != dt2) {
                correct_assigned_type = false;
                System.out.printf("------\n");
                System.out.printf("*FAIL* - Constant *%s* is assigned a value of incorrect type. ", sn0.jjtGetValue());
                System.out.printf("*%s* != *%s*\n", dt1, dt2);
            }
            WriteOrRead(sn0.jjtGetValue(), data, "write");

            idi += 3;
            typei += 3;
            vali += 3;
        }

        return DataType.ConstantDeclaration;
    }

    public Object visit(ASTConstantAssign node, Object data) {
        DataType dt = (DataType)node.jjtGetChild(0).jjtAccept(this, data);
        return dt;
    }

    public Object visit(ASTFunction node, Object data) {
        String val = (String)node.value;
        scope = val;
        SimpleNode sn2 = (SimpleNode)node.jjtGetChild(1);

        call_func.put(val, 0);
        param_in_func.put(val, sn2.jjtGetNumChildren()/2);

        node.childrenAccept(this, data);
        return DataType.Function;
    }

    public Object visit(ASTFunctionReturn node, Object data) {
        node.childrenAccept(this, data);
        scope = "global";
        return DataType.FunctionReturn;
    }
    
    public Object visit(ASTType node, Object data) {
        String s = (String)node.value;
        if (s.equals("boolean")) {
            return DataType.Boolean;
        }
        else if (s.equals("integer")) {
            return DataType.Number;
        }
        else if (s.equals("void")) {
            return DataType.TypeVoid;
        }
        return DataType.TypeUnknown;
    }
    
    public Object visit(ASTParameterList node, Object data) {
        int i = 0;

        while (i < node.jjtGetNumChildren()) {
            if (i % 2 == 0) {
                SimpleNode sni = (SimpleNode)node.jjtGetChild(i);
                WriteOrRead(sni.jjtGetValue(), data, "write");
            }
            i++;
        }

        node.childrenAccept(this, data);
        return DataType.ParameterList;
    }
    
    public Object visit(ASTMain node, Object data) {
        scope = "main";
        node.childrenAccept(this, data);

        scope = "global";
        return DataType.Main;
    }

    public Object visit(ASTStatement node, Object data) {
        String type  = (String)node.value;
        if(type == ":=") {
            SimpleNode sn0 = (SimpleNode)node.jjtGetChild(0);
            SimpleNode sn1 = (SimpleNode)node.jjtGetChild(1);
            DataType sn0DataType = (DataType)sn0.jjtAccept(this, data);
            DataType sn1DataType = (DataType)sn1.jjtAccept(this, data);
            /*
            if (sn0DataType != sn1DataType) {
                correct_func_arg_number = false;
                System.out.printf("*FAIL* - Parameter *%s* is assigned a value of incorrect type.\n", sn0.value);
                System.out.printf("------ ");
                System.out.printf("*%s* != *%s*\n", sn0DataType, sn1DataType);
            }
            */
            if(sn1.toString().equals("Identifier")) {
                WriteOrRead(sn1.jjtGetValue(), data, "write");
            }
        }

        Hashtable ht = (Hashtable) data;
        int children = node.jjtGetNumChildren();
        int i = 0;

        while (i < children) {
            SimpleNode sni = (SimpleNode)node.jjtGetChild(i);
            if (sni.toString().equals("ArgumentList")) {
                SimpleNode sni_1 = (SimpleNode)node.jjtGetChild(i - 1);
                if (!ht.containsKey(sni_1.value)) {
                    String id = (String)sni_1.value;
                    id_without_func.add(id);
                }         
                if (param_in_func.containsKey(sni_1.value)) {
                    if (param_in_func.get(sni_1.value) != sni.jjtGetNumChildren()) {
                        correct_func_arg_number = false;
                        System.out.printf("------ \n");
                        System.out.printf("*FAIL* - Parameter *%s* is assigned incorrect number of parameters. ", sni.value);
                        System.out.printf("*%s* != *%s*\n", param_in_func.get(sni_1.value), sni.jjtGetNumChildren());
                    } 
                }       
            }
            i++;
        }
        node.childrenAccept(this, data);
        return DataType.Statement;
    }

    public Object visit(ASTPlusOperator node, Object data) {
        DataType dt = ArithOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTMinusOperator node, Object data) {
        DataType dt = ArithOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTBitOrOperator node, Object data) {
        DataType dt = EqualOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTBitAndOperator node, Object data) {
        DataType dt = EqualOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTEqualOperator node, Object data) {
        DataType dt = EqualOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTNotEqualOperator node, Object data) {
        DataType dt = EqualOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTLessThanOperator node, Object data) {
        DataType dt = RelationOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTLessOrEqualOperator node, Object data) {
        DataType dt = RelationOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTGreaterThanOperator node, Object data) {
        DataType dt = RelationOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTGreaterOrEqualOperator node, Object data) {
        DataType dt = RelationOpType(node, data);
        ReadCheck(node, data);
        return dt;
    }

    public Object visit(ASTArgumentList node, Object data) {
        node.childrenAccept(this, data);
        return DataType.ArgumentList;
    }

    public Object visit(ASTIdentifier node, Object data) {
        Hashtable ht = (Hashtable) data;
        String val = (String)node.jjtGetValue();

        if(ht.containsKey(val)) {
            SimpleNode sn = (SimpleNode)node.jjtGetParent();
            if (sn.toString().equals("Statement")) {
                if (call_func.containsKey(val)) {
                    call_func.put(val, (call_func.get(val) + 1));
                }
                return DataType.Function;
            }
        }

        Hashtable scopeHT = (Hashtable)ht.get(scope);
        /*
        STC stc = (STC)scopeHT.get(val);
        
        if (stc == null) {
            Hashtable globalScope = (Hashtable) ht.get("global");
            stc = (STC) globalScope.get(val);

            if (scope.equals("global") || stc == null) {
                if (!id_without_func.contains(val)) {
                    System.out.printf("------\n");
                    System.out.printf("*FAIL* - Variable *%s* has not been defined in scope *%s*\n", val, scope);
                    declared_in_scope = false;
                }
                return DataType.TypeUnknown;
            }
        }
        */


        return DataType.TypeUnknown;
    }

    public Object visit(ASTBoolean node, Object data) {
        return DataType.Boolean;
    }

    public Object visit(ASTNumber node, Object data) {
        return DataType.Number;
    }
}

