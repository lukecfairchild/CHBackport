package com.kookster.chbackport;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.annotations.api;
import com.laytonsmith.annotations.breakable;
import com.laytonsmith.annotations.core;
import com.laytonsmith.annotations.hide;
import com.laytonsmith.annotations.noboilerplate;
import com.laytonsmith.annotations.nolinking;
import com.laytonsmith.annotations.noprofile;
import com.laytonsmith.annotations.seealso;
import com.laytonsmith.annotations.unbreakable;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Globals;
import com.laytonsmith.core.LogLevel;
import com.laytonsmith.core.MethodScriptCompiler;
import com.laytonsmith.core.Optimizable;
import com.laytonsmith.core.ParseTree;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.compiler.FileOptions;
import com.laytonsmith.core.compiler.keywords.InKeyword;
import com.laytonsmith.core.constructs.Auto;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CByteArray;
import com.laytonsmith.core.constructs.CClassType;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CDouble;
import com.laytonsmith.core.constructs.CFunction;
import com.laytonsmith.core.constructs.CIClosure;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CKeyword;
import com.laytonsmith.core.constructs.CLabel;
import com.laytonsmith.core.constructs.CMutablePrimitive;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CSlice;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.IVariable;
import com.laytonsmith.core.constructs.IVariableList;
import com.laytonsmith.core.constructs.InstanceofUtil;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CREIncludeException;
import com.laytonsmith.core.exceptions.CRE.CREIndexOverflowException;
import com.laytonsmith.core.exceptions.CRE.CREInsufficientArgumentsException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidProcedureException;
import com.laytonsmith.core.exceptions.CRE.CRERangeException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.CancelCommandException;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.core.exceptions.ConfigCompileGroupException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.exceptions.FunctionReturnException;
import com.laytonsmith.core.exceptions.LoopBreakException;
import com.laytonsmith.core.exceptions.LoopContinueException;
import com.laytonsmith.core.natives.interfaces.ArrayAccess;
import com.laytonsmith.tools.docgen.templates.ArrayIteration;
import com.laytonsmith.tools.docgen.templates.Loops;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Functions {

    @api
    @seealso({com.laytonsmith.tools.docgen.templates.Closures.class})
    public static class executeas extends AbstractFunction {

        @Override
        public String getName() {
            return "executeas";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        @Override
        public String docs() {
            return "mixed {player, label, [values...,] closure} Executes the given closure in the context of a given"
                    + " player. A closure that runs player(), for instance, would return the specified player's name."
                    + " The label argument sets the permission label that this closure will use. If null is given,"
                    + " the current label will be used, like with execute().";
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public boolean isRestricted() {
            return true;
        }

        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            if(!(args[args.length - 1] instanceof CClosure)) {
                throw new CRECastException("Only a closure (created from the closure function) can be sent to executeas()", t);
            }
            Construct[] vals = new Construct[args.length - 3];
            System.arraycopy(args, 2, vals, 0, args.length - 3);
            CClosure closure = (CClosure) args[args.length - 1];
            CommandHelperEnvironment cEnv = closure.getEnv().getEnv(CommandHelperEnvironment.class);
            GlobalEnv gEnv = closure.getEnv().getEnv(GlobalEnv.class);

            MCCommandSender originalSender = cEnv.GetCommandSender();
            cEnv.SetCommandSender(Static.GetPlayer(args[0].val(), t));

            String originalLabel = gEnv.GetLabel();
            if(!(args[1] instanceof CNull)) {
                gEnv.SetLabel(args[1].val());
            }

            try {
                closure.execute(vals);
            } catch (FunctionReturnException e) {
                return e.getReturn();
            } finally {
                cEnv.SetCommandSender(originalSender);
                gEnv.SetLabel(originalLabel);
            }
            return CVoid.VOID;
        }

        @Override
        public CHVersion since() {
            return CHVersion.V3_3_2;
        }
    }
}
