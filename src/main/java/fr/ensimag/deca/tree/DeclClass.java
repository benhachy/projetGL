package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.io.PrintStream;



/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl03
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {
    AbstractIdentifier identifier;
    AbstractIdentifier classExtension;
    ListDeclField   feildDecl;
    ListDeclMethod  methodDecl;

    public DeclClass(AbstractIdentifier identifier,AbstractIdentifier classExtension,ListDeclField  feildDecl,ListDeclMethod  methodDecl){
        this.identifier= identifier;
        this.classExtension=classExtension;
        this.feildDecl= feildDecl;
        this.methodDecl=methodDecl;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    public AbstractIdentifier getIdentifier(){
        return identifier;
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        ClassType c = new ClassType(identifier.getName(),getLocation(),null);
        TypeDefinition superClass = compiler.get(classExtension.getName());
        if(superClass == null)
        {
            throw new ContextualError("la super classe "+ classExtension.getName()  +" n'est déjà définie", getLocation());
        }
        
        try{
            classExtension.setDefinition(superClass);
            identifier.setDefinition((new ClassDefinition(c,getLocation(),classExtension.getClassDefinition())));
            compiler.declare(identifier.getName(), identifier.getClassDefinition());
        }
        catch( DoubleDefException e)
        {
            throw new ContextualError("la classe "+ identifier.getName()  +" est déjà définie", getLocation());
        }
        catch( DecacInternalError e)
        {
            throw new ContextualError(classExtension.getName()  +" n'est pas une class", getLocation());
        }
        // EnvironmentExp envExpF = new EnvironmentExp(null);
        // for(AbstractDeclField f : feildDecl.getList())
        // {
        //     f.verifyFeild(compiler,envExpF,classExtension.getClassDefinition(),identifier.getClassDefinition());
        // }
        // compiler.setEvn(identifier.getName(),envExpF);
        // for(AbstractDeclMethod f : methodDecl.getList())
        // {
        //     f.verifyMethod(compiler,envExpF,identifier.getClassDefinition());
        // }
        // compiler.setEvn(identifier.getName(),envExpF);
    }


    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        EnvironmentExp envExpF = new EnvironmentExp(null);
        for(AbstractDeclField f : feildDecl.getList())
        {
            f.verifyFeild(compiler,envExpF,classExtension.getClassDefinition(),identifier.getClassDefinition());
        }
        for(AbstractDeclMethod f : methodDecl.getList())
        {
            f.verifyMethod(compiler,envExpF,identifier.getClassDefinition());
        }
        compiler.setEvn(identifier.getName(),envExpF);
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        EnvironmentExp envExpR = new EnvironmentExp(null);
        for(AbstractDeclMethod f : methodDecl.getList())
        {
            f.verifyMethod(compiler,envExpR,identifier.getClassDefinition());
        }
        envExpR.empiler(compiler.getEnv(identifier.getName()));
        compiler.setEvn(identifier.getName(),envExpR);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        identifier.prettyPrint(s, prefix, false);
        classExtension.prettyPrint(s, prefix, false);
        feildDecl.prettyPrint(s, prefix, false);
        methodDecl.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        identifier.iter(f);
        classExtension.iter(f);
        feildDecl.iter(f);
        methodDecl.iter(f);
    }

    public void insertionClassTableMethodes(DecacCompiler compiler){
        //on verifie si la class extend object

        compiler.addComment("Code de la table des méthodes de "+identifier.getName());

        if(classExtension.getName().getName().equals("Object")){
            //si la class extends object on insert un pointure vers 
            compiler.addInstruction(new LEA(new RegisterOffset(1,Register.GB),Register.getR(0)));
        }else{
            //chercher la address de la super class dans la table des methodes
            compiler.addInstruction(new LEA(new RegisterOffset(Identifier.posGBIdentificateur.get(classExtension.getName()),Register.GB),Register.getR(0)));
        }
        //mettre l'address ver la super class dans la derner address disponible
        compiler.addInstruction(new STORE(Register.getR(0),new RegisterOffset(Register.positionGB,Register.GB)));
        Identifier.posGBIdentificateur.put(identifier.getName(),Register.positionGB);
        Register.updatePosGB();
        //insertion des etiquetes des methodes de la super class
        /*for (AbstractDeclMethod  methode : classExtension.getList()) {
            methode.creerEtStockerLabel(compiler,this);
        }*/
        //insertion des etiquetes des methodes
        //ClassDefinition superClass = compiler.get(classExtension.getName());
        for (AbstractDeclMethod  methode : methodDecl.getList()) {
             methode.creerEtStockerLabel(compiler,this);
        }
        //cherche les methodes du super class pour les inserer aussi
        //comment faire pour le surcharge des methodes???
    }

    public void genCodeInitializationChampsEtMethodes(DecacCompiler compiler){
        
        compiler.addComment("Initialisation des champs de "+identifier.getName());
        Label label = new Label("init."+identifier.getName());
        compiler.addLabel(label);

        //on verifie si la class herite d'un autre class
        if(!classExtension.getName().getName().equals("Object")){
            compiler.addComment("Appel de l'initialisation des champs hérités de "+classExtension.getName().getName());
            compiler.addInstruction(new PUSH(Register.getR(1)));
            Label labelInitSuper = new Label("init."+classExtension.getName().getName());
            compiler.addInstruction(new BSR(labelInitSuper));
            compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
        }
        int nmChamps = feildDecl.getList().size();
        //on verifie les debordements de la pile
        compiler.addInstruction(new TSTO(new ImmediateInteger(nmChamps+1)));
        compiler.addInstruction(new BOV(new Label("pile_pleine")));
        int pos = 1;
        for (AbstractDeclField champ : feildDecl.getList()) {
            //pour chaque champ on verifie le type
            //appres on les mett sur le registre R0
            if(champ.getType().getType().isFloat()){
                new FloatLiteral(0).codeGenExpr(compiler,0);
            }else if(champ.getType().getType().isInt()){
                new IntLiteral(0).codeGenExpr(compiler,0);
            }else if(champ.getType().getType().isBoolean()){
                new BooleanLiteral(false).codeGenExpr(compiler,0);
            }else if(champ.getType().getType().isClass()){
                //c'est un objet
                compiler.addInstruction(new LOAD(new NullOperand(),Register.getR(0)));
            }
            //on charge l'address de le objet sur le registre R1
            compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB), Register.getR(1)));
            compiler.addInstruction(new STORE(Register.getR(0),new RegisterOffset(-pos,Register.getR(1))));
            //appres on charge la valeur par defaut de cette type
            //a la fin on fait l'insertion du valeur dans la pille
            pos++;
        }
        compiler.addInstruction(new RTS());
        for (AbstractDeclMethod methode : methodDecl.getList()) {
            methode.genCodeMethode(compiler,this);
        }
    }

}
