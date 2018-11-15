package com.kookster.chbackport;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.events.BoundEvent;
import com.laytonsmith.core.events.Event;
import com.laytonsmith.core.exceptions.CRE.CREBindException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREIOException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;

import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.exceptions.FunctionReturnException;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.CHVersion;

import java.io.IOException;


public class Functions {

    @api(environments = CommandHelperEnvironment.class)
    public static class executeas extends AbstractFunction {

        public String getName() {
            return "executeas";
        }

        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        public String docs() {
            return "mixed {player, label, [values...,] closure} Executes the given closure in the context of a given"
                    + " player. A closure that runs player(), for instance, would return the specified player's name."
                    + " The label argument sets the permission label that this closure will use. If null is given,"
                    + " the current label will be used, like with execute().";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        public Boolean runAsync() {
            return null;
        }

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
