package vice.rubidium_extras;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
//import org.lwjgl.glfw.GLFW;
import net.minecraft.world.level.block.PressurePlateBlock;
import vice.rubidium_extras.features.Keybinding.KeyboardInput;

public enum WiZoom
{
    INSTANCE;

    public static final Minecraft MC = Minecraft.getInstance();

    private final double defaultLevel = 3;
    private Double currentLevel;
    private Double defaultMouseSensitivity;



    public double changeFovBasedOnZoom(double fov)
    {
        Options gameOptions = MC.options;
        double Sens = MC.options.sensitivity().get();
        if(currentLevel == null)
            currentLevel = defaultLevel;

        if(!KeyboardInput.zoomKey.isDown())
        {
            currentLevel = defaultLevel;

            if(defaultMouseSensitivity != null)
            {
                Sens = defaultMouseSensitivity;
                defaultMouseSensitivity = null;
            }

            return fov;
        }

        if(defaultMouseSensitivity == null)
            defaultMouseSensitivity = Sens;

        // Adjust mouse sensitivity in relation to zoom level.
        // (fov / currentLevel) / fov is a value between 0.02 (50x zoom)
        // and 1 (no zoom).

        Sens = defaultMouseSensitivity * (fov / currentLevel / fov);

        return fov / currentLevel;
    }

    public void onMouseScroll(double amount)
    {
        if(!KeyboardInput.zoomKey.isDown())
            return;

        if(currentLevel == null)
            currentLevel = defaultLevel;

        if(amount > 0)
            currentLevel *= 1.1;
        else if(amount < 0)
            currentLevel *= 0.9;

        currentLevel = Mth.clamp(currentLevel, 1, 50);
    }
}