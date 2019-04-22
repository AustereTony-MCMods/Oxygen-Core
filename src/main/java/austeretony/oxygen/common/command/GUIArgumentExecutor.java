package austeretony.oxygen.common.command;

import java.util.Set;

import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.core.api.ClientReference;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class GUIArgumentExecutor extends AbstractArgumentExecutor {

    public GUIArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {        
        params.add(new ArgumentParameter(CommandOxygenClient.ACTION_RELOAD_GUI_SETTINGS));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandOxygenClient.ACTION_RELOAD_GUI_SETTINGS)) {
                GUISettings.instance().reload();
                ClientReference.showMessage("oxygen.command.oxygenc.reloadGui");
            }
        }
    }
}
