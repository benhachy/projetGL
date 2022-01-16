package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl03
 * @date 01/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        
        this.getMain().verifyMain(compiler);
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("Main program");
        compiler.addInstruction(new ADDSP(Identifier.identificateurs.size()+2));
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        ajouterMessageErreur(compiler, new Label("pile_pleine"), "Error: pile pleine");
        ajouterMessageErreur(compiler, new Label("Overflow_error"), "Error: Overflow during arithmetic operation");
        ajouterMessageErreur(compiler, new Label("division_zero"),"Error: Division by zero" );
        ajouterMessageErreur(compiler, new Label("io_error"),"Error: Input/Output error" );
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    private void ajouterMessageErreur(DecacCompiler compiler,Label label,String msg){
        compiler.addLabel(label);
        compiler.addInstruction(new WSTR(msg));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
