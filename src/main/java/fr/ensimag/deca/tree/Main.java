package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import static org.mockito.ArgumentMatchers.nullable;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl03
 * @date 01/01/2022
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        SymbolTable.Symbol name  = null ;
        //SymbolTable symbol = new SymbolTable.Symbol("void") ; 
        VoidType retour = new VoidType(name);
       // VoidType retour = new VoidType( symbol.create("void"));
        EnvironmentExp localEnv = new EnvironmentExp(null);
        if(declVariables != null)
        {
            declVariables.verifyListDeclVariable(compiler, localEnv, null);
        }
        if(insts != null)
        {
            insts.verifyListInst(compiler,localEnv, null, retour);
        }
        
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        LOG.debug("verify Main: end");
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        declVariables.codeGenListDeclVar(compiler);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);

    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
