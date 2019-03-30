package austeretony.oxygen.common.api.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen.common.command.IArgumentExecutor;
import austeretony.oxygen.common.command.IArgumentParameter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class ArgumentExecutor implements IArgumentExecutor {

    public final String name;

    private Set<IArgumentParameter> params;    

    public ArgumentExecutor(String argument) {
        this.name = argument;
    }

    public ArgumentExecutor(String argument, boolean hasParams) {
        this(argument);
        if (hasParams) 
            this.params = new HashSet<IArgumentParameter>();
        this.getParams(this.params);
    }

    @Override
    public String getArgumentName() {
        return this.name;
    }

    @Override
    public boolean hasParams() {
        return this.params != null;
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!this.hasParams() && args.length > 1)
            return;//TODO ERROR defined unnecessary parameters
        if (this.hasParams()) {
            Map<String, IArgumentParameter> defParamsMap = new HashMap<String, IArgumentParameter>(this.params.size());
            for (IArgumentParameter param : this.params)
                defParamsMap.put(param.getParameterName(), param);
            Map<String, IArgumentParameter> paramsMap = new HashMap<String, IArgumentParameter>(this.params.size());
            IArgumentParameter param;
            String 
            arg, 
            nextArg = null;
            for (int i = 1; i < args.length; i++) {
                arg = args[i];
                if (args.length > i + 1)
                    nextArg = args[i + 1];
                if (defParamsMap.containsKey(arg)) {
                    param = defParamsMap.get(arg);
                    if (param.hasValue()) {
                        if (nextArg != null && !defParamsMap.containsKey(nextArg)) {
                            param.setValue(nextArg);
                            paramsMap.put(param.getBaseName(), param);
                        } else {
                            //TODO ERROR wrong parameter value
                        }                     
                    } else if (nextArg == null || defParamsMap.containsKey(nextArg)) {
                        paramsMap.put(param.getBaseName(), param);
                    } else {
                        //TODO ERROR defined unnecessary parameter
                    }
                }
            }
            this.execute(server, sender, paramsMap);
        } else {
            this.execute(server, sender, null);
        }
    }
}
