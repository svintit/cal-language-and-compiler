import java.util.*;
import java.io.*;

public class ThreeAddressCode implements MyParserVisitor {
    private static int label = 1;
    private static int temp_label = 1;
    private static PrintWriter writer;

    private Object goToOperationChild(SimpleNode node, Object data) {
        String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
        String s1 = (String)node.jjtGetChild(1).jjtAccept(this, data);
        String e1 = "t" + temp_label;
        temp_label++;

        String e2 = "   " + e1 + " := " + s0 + " " + node.value + " " + s1;
        writer.println(e2);
        System.out.println(e2);

        return (Object)e1;
    }

    public Object visit(SimpleNode node, Object data) {
        throw new RuntimeException("Visit SimpleNode"); 
    }

    public Object visit(ASTProgram node, Object data) {
        try {
            writer = new PrintWriter("ThreeAddressCode.ir");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        node.childrenAccept(this, data);
        writer.close();
        System.out.println();
        return DataType.TypeUnknown;
    }

    public Object visit(ASTVariableDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return DataType.TypeUnknown;
    }

    public Object visit(ASTConstantDeclaration node, Object data) {
        for (int i = 2; i < node.jjtGetNumChildren(); i += 3) {
            String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
            String s2 = (String)node.jjtGetChild(2).jjtAccept(this, data);

            String e = "   " + s0 + " " + s2;
            writer.println(e);
            System.out.println(e);
        }
        writer.println("   goto Main");
        System.out.println("   goto Main");

        return DataType.TypeUnknown;
    }
    
    public Object visit(ASTConstantAssign node, Object data) {
        String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
        return node.value + " " + s0;
    }

    public Object visit(ASTFunction node, Object data) {
        writer.println("function " + node.value);
        System.out.println("function " + node.value);
        node.childrenAccept(this, data);
        return DataType.TypeUnknown;
    }

    public Object visit(ASTFunctionReturn node, Object data) {
        String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
        String e = "   return " + s0;
        writer.println(e);
        System.out.println(e);
        return (Object)e;
    }

    public Object visit(ASTType node, Object data) {
        return node.value;
    }

    public Object visit(ASTParameterList node, Object data) {
        node.childrenAccept(this, data);
        return DataType.ParameterList;
    }

    public Object visit(ASTMain node, Object data) {
        writer.println("");
        System.out.println("");
        writer.println("Main:");
        System.out.println("Main:");
        node.childrenAccept(this, data);
        return DataType.TypeUnknown;
    }

    public Object visit(ASTStatement node, Object data) {
        String type = (String)node.value;
        if (type == ":=") {
            String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
            String s1 = (String)node.jjtGetChild(1).jjtAccept(this, data);

            String e = "   " + s0 + " " + node.value + " " + s1;

            writer.println(e);
            System.out.println(e);

            return (Object)e;
        }
        else if (type == "while") {
            String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);

            String loop = "label" + label;
            writer.println("label" + label + ":");
            System.out.println("label" + label + ":");
            label++;

            String e = "   if" + s0 + " goto label" + label;
            writer.println(e);
            System.out.println(e);
            label++;
            writer.println("   goto label" + label);
            System.out.println("   goto label" + label);
            writer.println("label" + (label - 1) + ":");
            System.out.println("label" + (label - 1) + ":");

            int end = label;
            label++;

            node.jjtGetChild(1).jjtAccept(this, data);

            writer.println("   goto " + loop);
            System.out.println("   goto " + loop);
            writer.println("label" + end + ":");
            System.out.println("label" + end + ":");

            return (Object)e;
        }
        else if (type == "if") {
            SimpleNode sn0 = (SimpleNode)node.jjtGetChild(0);

            int i = 0;

            if (sn0.toString() == "Identifier") {
                Integer arg1 = (Integer)node.jjtGetChild(1).jjtAccept(this, data);
                String e = "   temp" + temp_label + ":= call " + sn0.value + ", " + arg1;

                int else_label = label;
                String e2 = " ifFalse temp" + temp_label + " goto label" + label;
                label++;
                temp_label++;

                writer.println(e);
                System.out.println(e);
                writer.println(e2);
                System.out.println(e2);

                node.jjtGetChild(2).jjtAccept(this, data);
                writer.println("   goto label" + label);
                System.out.println("   goto label" + label);
                int end = label;
                label++;

                writer.println("label" + else_label + ":");
                System.out.println("label" + else_label + ":");
                node.jjtGetChild(3).jjtAccept(this, data);
                writer.println("label" + end + ":");
                System.out.println("label" + end + ":");

                return DataType.TypeUnknown;
            }
            else {
                String s0 = (String)node.jjtGetChild(0).jjtAccept(this, data);
                int else_label = label;
                String ee = "   ifFalse " + s0 + " goto label" + label;

                writer.println(ee);
                System.out.println(ee);
                label++;

                node.jjtGetChild(1).jjtAccept(this, data);
                writer.println("   goto label" + label);
                System.out.println("   goto label" + label);
                int end = label;
                label++;

                writer.println("label" + else_label + ":");
                System.out.println("label" + else_label + ":");
                node.jjtGetChild(2).jjtAccept(this, data);
                writer.println("label" + end + ":");
                System.out.println("label" + end + ":");

                return DataType.TypeUnknown;
            }
        }
        else if (type == "Process_Call") {
            SimpleNode sn0 = (SimpleNode)node.jjtGetChild(0);
            String i1 = (String)node.jjtGetChild(0).jjtAccept(this, data);
            String e = "   call " + sn0 + ", " + i1;

            writer.println(e);
            System.out.println(e);
            
            return DataType.TypeUnknown;
        }

        node.childrenAccept(this, data);
        return DataType.TypeUnknown;
    }

    public Object visit(ASTPlusOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTMinusOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTBitOrOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTBitAndOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTEqualOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTNotEqualOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTLessThanOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTLessOrEqualOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTGreaterThanOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTGreaterOrEqualOperator node, Object data) {
        return goToOperationChild(node, data);
    }

    public Object visit(ASTArgumentList node, Object data) {
        int i = 0;
        for (i = 0; i < node.jjtGetNumChildren(); i++) {
            String si = (String)node.jjtGetChild(i).jjtAccept(this, data);
            String e = "   parameter " + si;
            writer.println(e);
            System.out.println(e);
        }
        return (Object)i;
    }

    public Object visit(ASTIdentifier node, Object data) {
        return node.value;
    }

    public Object visit(ASTBoolean node, Object data) {
        return node.value;
    }

    public Object visit(ASTNumber node, Object data) {
        return node.value;
    }
}