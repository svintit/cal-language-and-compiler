import java.util.*;

public class STC extends Object {
    private Hashtable<String, LinkedList<String>> ST;
    private Hashtable<String, String> symbolType, typeDescription;

    boolean wwritten = false;
    boolean rread = false;

    public STC() {
        this.ST = new Hashtable<>();
        this.symbolType = new Hashtable<>();
        this.typeDescription = new Hashtable<>();

        ST.put("global", new LinkedList<>());
    }

    public void put(String id, String type, String descr, String scope) {
        LinkedList<String> templl = ST.get(scope);
        if (templl != null) {
            templl.addFirst(id);
        }
        else {
            templl = new LinkedList<>();
            templl.add(id);
            ST.put(scope, templl);
        }
        symbolType.put(id + scope, type);
        typeDescription.put(id + scope, descr);
    }

    public void print() {
        String scope;
        Enumeration t = ST.keys();

        System.out.printf("|%11s | %10s | %10s | %12s|\n", "ID", "Type", "Scope", "Description");
        System.out.println("------------------------------------------------------");
        boolean check = false;

        while(t.hasMoreElements()) {
            scope = (String)t.nextElement();
            LinkedList<String> templl = ST.get(scope);
            
            for(String id : templl) {
                String type = symbolType.get(id + scope);
                String descr = typeDescription.get(id + scope);
                System.out.printf("|%11s | %10s | %10s | %12s|\n", id, type, scope, descr);
                check = true;
            }
            if (check == false) {
                try {
                    t.nextElement();
                }
                catch (NoSuchElementException e) {
                    System.out.println("  *THERE ARE NO SYMBOLS TO OUTPUT FOR SYMBOL TABLE*");
                }
            }
        }
        System.out.println();
    }

    public void write() {
        this.wwritten = true;
    }

    public void read() {
        this.rread = true;
    }
}