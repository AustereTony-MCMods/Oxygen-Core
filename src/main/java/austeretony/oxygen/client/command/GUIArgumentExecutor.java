package austeretony.oxygen.client.command;

import java.util.Set;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class GUIArgumentExecutor extends AbstractArgumentExecutor {

    public static final String ACTION_RELOAD_GUI_SETTINGS = "reload-settings";

    public GUIArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {        
        params.add(new ArgumentParameter(ACTION_RELOAD_GUI_SETTINGS));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(ACTION_RELOAD_GUI_SETTINGS)) {
                GUISettings.instance().reload();
                ClientReference.showMessage("oxygen.command.oxygenc.reloadGui");
            }
        }
    }
}
