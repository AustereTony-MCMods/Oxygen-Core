package austeretony.alternateui.screen.core;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс базового раздела, используемого в качестве внутреннего раздела ГПИ (без создания собственного).
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIBaseSection extends AbstractGUISection {

    public GUIBaseSection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    protected void init() {}

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}
}
