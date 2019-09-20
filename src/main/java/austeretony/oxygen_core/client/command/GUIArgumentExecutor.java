package austeretony.oxygen_core.client.command;

import java.util.Set;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class GUIArgumentExecutor extends AbstractArgumentExecutor {

    public static final String ACTION_RELOAD_GUI_SETTINGS = "reload-settings";

    public GUIArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<ArgumentParameter> params) {        
        params.add(new ArgumentParameterImpl(ACTION_RELOAD_GUI_SETTINGS));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(ACTION_RELOAD_GUI_SETTINGS)) {
                GUISettings.get().reload();
                ClientReference.showChatMessage("oxygen.command.oxygenc.reloadGui");
            }
        }
    }
}
