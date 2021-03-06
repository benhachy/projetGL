package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
        this.getClasses().verifyListClass(compiler);
        this.getClasses().verifyListClassMembers(compiler);
        this.getClasses().verifyListClassBody(compiler);
        this.getMain().verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        //add addsp pour la table des methodes et des variables globals
        int addSP = compiler.getNumberOfClass()+compiler.getNumberOfMethods()+Identifier.ordreIdentifier+2;
        compiler.addInstruction(new ADDSP(addSP));
        compiler.addComment("Main program");
        classes.creerTableMethodes(compiler);
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        classes.genCodeInitializationEtMethodes(compiler);
        codeGenEquals(compiler);
        ajouterMessageErreur(compiler, new Label("pile_pleine"), "Error: pile pleine");
        ajouterMessageErreur(compiler, new Label("Overflow_error"), "Error: Overflow during arithmetic operation");
        ajouterMessageErreur(compiler, new Label("division_zero"),"Error: Division by zero" );
        ajouterMessageErreur(compiler, new Label("io_error"),"Error: Input/Output error" );
        ajouterMessageErreur(compiler, new Label("print_Error"),"Error: print float only in hexa form" );
        ajouterMessageErreur(compiler, new Label("deferencement.null"),"Erreur : dereferencement de null" );
        

        // for (Symbol symb : Identifier.positionVariables.keySet()) {
        //     System.out.println(symb+" "+Identifier.positionVariables.get(symb).toString());
        // }
    }

    private void codeGenEquals(DecacCompiler compiler){
        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new PUSH(Register.getR(2)));
        compiler.addInstruction(new PUSH(Register.getR(3)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB), Register.getR(2)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3,Register.LB), Register.getR(3)));
        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        Label loadTrue = new Label("code.Object.equals.loadTrueEQ");
        Label finCmp = new Label("code.Object.equals.finComparationEQ");
        compiler.addInstruction(new BEQ(loadTrue));
        new IntLiteral(0).codeGenExpr(compiler,0);
        compiler.addInstruction(new BRA(finCmp));
        compiler.addLabel(loadTrue);
        new IntLiteral(1).codeGenExpr(compiler,0);
        compiler.addLabel(finCmp);
        compiler.addInstruction(new POP(Register.getR(3)));
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new RTS());
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
