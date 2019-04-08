package austeretony.oxygen.common.api.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen.common.command.IArgumentExecutor;
import austeretony.oxygen.common.command.IArgumentParameter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractArgumentExecutor implements IArgumentExecutor {

    public final String name;

    private Set<IArgumentParameter> params;    

    public AbstractArgumentExecutor(String argument) {
        this.name = argument;
    }

    public AbstractArgumentExecutor(String argument, boolean hasParams) {
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
            throw new CommandException("oxygen.command.exception.unnecessaryParams", args[1]);
        if (this.hasParams() && args.length == 1)
            throw new CommandException("oxygen.command.exception.noParams");
        if (this.hasParams()) {
            Map<String, IArgumentParameter> defParamsMap = new HashMap<String, IArgumentParameter>(this.params.size());
            for (IArgumentParameter param : this.params)
                defParamsMap.put(param.getParameterName(), param);
            Set<IArgumentParameter> paramsSet = new HashSet<IArgumentParameter>(this.params.size());
            IArgumentParameter param;
            String 
            arg, 
            nextArg = null;
            for (int i = 1; i < args.length; i++) {
                arg = args[i];
                nextArg = null;
                if (args.length > i + 1)
                    nextArg = args[i + 1];
                if (defParamsMap.containsKey(arg)) {
                    param = defParamsMap.get(arg);
                    if (param.hasValue()) {
                        if (nextArg != null && !defParamsMap.containsKey(nextArg)) {
                            param.setValue(nextArg);
                            paramsSet.add(param);
                        } else
                            throw new CommandException("oxygen.command.exception.wrongParamValue", arg, nextArg == null ? "<no value>" : nextArg);
                    } else {
                        paramsSet.add(param);
                        if (nextArg != null && !defParamsMap.containsKey(nextArg))
                            throw new CommandException("oxygen.command.exception.unnecessaryParamValue", arg, nextArg);
                    }
                }
            }
            this.execute(server, sender, paramsSet);
        } else
            this.execute(server, sender, null);
    }

    public static EntityPlayerMP getPlayerByUsername(MinecraftServer server, String username) throws CommandException {
        EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUsername(username);
        if (playerMP != null)
            return playerMP;
        else
            throw new CommandException("oxygen.command.exception.playerNotFound", username);
    }
}
