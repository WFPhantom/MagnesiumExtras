package vice.rubidium_extras.mixins.SodiumConfig;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.Options;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;
import vice.rubidium_extras.mixins.BorderlessFullscreen.MainWindowAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionsMixin
{
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    //@Inject(at = @At("HEAD"), method = "experimental", remap = false, cancellable = true)

    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;

    //private static void experimental(CallbackInfoReturnable<OptionPage> cir)
    @Inject(
            method = "advanced",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false,
            cancellable = true
    )
    private static void Inject(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups)
    {
//        groups.removeIf((optionGroup) ->
//            optionGroup
//                    .getOptions()
//                    .stream()
//                    .anyMatch((option) -> Objects.equals(option.getName(), "Display FPS"))
//        );

        Option<MagnesiumExtrasConfig.Complexity> displayFps =  OptionImpl.createBuilder(MagnesiumExtrasConfig.Complexity.class, sodiumOpts)
                .setName(Component.nullToEmpty("Display FPS"))
                .setTooltip(Component.nullToEmpty("Displays the current FPS. Advanced mode also displays minimum FPS, as well as 15 second average FPS, which are more useful for judging performance."))
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.Complexity.class, new Component[] {
                        Component.nullToEmpty("Off"),
                        Component.nullToEmpty("Simple"),
                        Component.nullToEmpty("Advanced")
                }))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.fpsCounterMode.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.Complexity.valueOf(MagnesiumExtrasConfig.fpsCounterMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("FPS Display Position"))
                .setTooltip(Component.nullToEmpty("Offsets the FPS display a few pixels"))
                .setControl((option) -> {
                    return new SliderControl(option, 4, 64, 2, ControlValueFormatter.translateVariable("Pixels"));
                })
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.fpsCounterPosition.set(value),
                        (opts) -> MagnesiumExtrasConfig.fpsCounterPosition.get())
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsPos)
                .build());




        OptionImpl<SodiumGameOptions, Boolean> totalDarkness = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty("True Darkness"))
                .setTooltip(Component.nullToEmpty("Makes the rest of the world more realistically dark. Does not effect daytime or torch light."))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.trueDarknessEnabled.set(value),
                        (options) -> MagnesiumExtrasConfig.trueDarknessEnabled.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<MagnesiumExtrasConfig.DarknessOption> totalDarknessSetting =  OptionImpl.createBuilder(MagnesiumExtrasConfig.DarknessOption.class, sodiumOpts)
                .setName(Component.nullToEmpty("True Darkness Mode"))
                .setTooltip(Component.nullToEmpty("Controls how dark is considered true darkness."))
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.DarknessOption.class, new Component[] {
                        Component.nullToEmpty("Pitch Black"),
                        Component.nullToEmpty("Really Dark"),
                        Component.nullToEmpty("Dark"),
                        Component.nullToEmpty("Dim")
                }))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.darknessOption.set(value),
                        (opts) -> MagnesiumExtrasConfig.darknessOption.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(totalDarkness)
                .add(totalDarknessSetting)
                .build());





//        Option<MagnesiumExtrasConfig.Quality> fadeInQuality =  OptionImpl.createBuilder(MagnesiumExtrasConfig.Quality.class, sodiumOpts)
//                .setName(Component.nullToEmpty("Chunk Fade In Quality"))
//                .setTooltip(Component.nullToEmpty("Controls how fast chunks fade in. No performance hit, Fancy simply takes longer, but looks a bit cooler."))
//                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.Quality.class, new Component[] {
//                        Component.nullToEmpty("Off"),
//                        Component.nullToEmpty("Fast"),
//                        Component.nullToEmpty("Fancy")
//                }))
//                .setBinding(
//                        (opts, value) -> MagnesiumExtrasConfig.fadeInQuality.set(value.toString()),
//                        (opts) -> MagnesiumExtrasConfig.Quality.valueOf(MagnesiumExtrasConfig.fadeInQuality.get()))
//                .setImpact(OptionImpact.LOW)
//                .build();

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty("Enable fog"))
                .setTooltip(Component.nullToEmpty("Toggles off all fog in the overworld."))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.fog.set(value),
                        (options) -> MagnesiumExtrasConfig.fog.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> hideJEI = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty("Hide JEI Until Searching"))
                .setTooltip(Component.nullToEmpty("Toggles off JEI items unless you search for something. Press space to search for everything."))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.hideJEI.set(value),
                        (options) -> MagnesiumExtrasConfig.hideJEI.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Cloud Height"))
                .setTooltip(Component.nullToEmpty("Raises cloud height."))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> {
                            MagnesiumExtrasConfig.cloudHeight.set(value);
                        },
                        (options) ->  MagnesiumExtrasConfig.cloudHeight.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup.createBuilder()
                //.add(fadeInQuality)
                .add(fog)
                .add(cloudHeight)
                .build());





        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty("Enable Max Entity Distance"))
                .setTooltip(Component.nullToEmpty("Toggles off entity culling."))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.enableDistanceChecks.set(value),
                        (options) -> MagnesiumExtrasConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build()
        );





        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Max Entity Distance"))
                .setTooltip(Component.nullToEmpty("Hides and does not tick entities beyond this many blocks. Huge performance increase, especially around modded farms."))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Vertical Entity Distance"))
                .setTooltip(Component.nullToEmpty("Hides and does not tick entities underneath this many blocks, improving performance above caves. This should ideally be set lower than the horizontal distance."))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> MagnesiumExtrasConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );





        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Max Tile Distance"))
                .setTooltip(Component.nullToEmpty("Hides block entities beyond this many blocks. Huge performance increase, especially around lots of modded machines."))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(MagnesiumExtrasConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Vertical Tile Distance"))
                .setTooltip(Component.nullToEmpty("Hides block entities underneath this many blocks, improving performance above caves (if you have your machines in caves, for some reason). This should ideally be set lower than the horizontal distance."))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceY.set(value ),
                        (options) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );
    }


    @Inject(
            method = "general",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false,
            cancellable = true
    )
    private static void InjectGeneral(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups)
    {
        OptionImpl<Options, MagnesiumExtrasConfig.FullscreenMode> fullscreenMode = OptionImpl.createBuilder( MagnesiumExtrasConfig.FullscreenMode.class, vanillaOpts)
                .setName(Component.nullToEmpty("Fullscreen Mode"))
                .setTooltip(Component.nullToEmpty("Windowed - the game will display in a small window.\nBorderless - the game will be fullscreened, and locked to your monitor's refresh rate, but allow you to tab out easily.\nFullscreen - the game will display in native fullscreen mode."))
                .setControl((opt) -> new CyclingControl<>(opt, MagnesiumExtrasConfig.FullscreenMode.class, new Component[] {
                        Component.nullToEmpty("Windowed"),
                        Component.nullToEmpty("Borderless"),
                        Component.nullToEmpty("Fullscreen")
                }))
                .setBinding(
                        (opts, value) -> {
                            MagnesiumExtrasConfig.fullScreenMode.set(value);
                            opts.fullscreen = value != MagnesiumExtrasConfig.FullscreenMode.WINDOWED;

                            Minecraft client = Minecraft.getInstance();
                            Window window = client.getWindow();
                            if (window != null && window.isFullscreen() != opts.fullscreen)
                            {
                                window.toggleFullScreen();
                                opts.fullscreen = window.isFullscreen();
                            }

                            if (window != null && opts.fullscreen)
                            {
                                ((MainWindowAccessor) (Object) window).setDirty(true);
                                window.changeFullscreenVideoMode();
                            }
                        },
                        (opts) -> MagnesiumExtrasConfig.fullScreenMode.get())
                .build();

        ReplaceOption(groups, "Fullscreen", fullscreenMode);
    }


    private static void ReplaceOption(List<OptionGroup> groups, String name, Option<?> replaceOption)
    {
        List<OptionGroup> newList = new ArrayList<>();

        for (OptionGroup optionGroup : groups)
        {

            OptionGroup.Builder builder = OptionGroup.createBuilder();

            for (Option<?> option : optionGroup.getOptions())
            {
                builder.add(Objects.equals(option.getName().getString(), name) ? replaceOption : option);
            }

            newList.add(builder.build());
        }

        groups.clear();
        groups.addAll(newList);
    }

    @ModifyConstant(method = "advanced", remap = false, constant = @Constant(stringValue = "sodium.options.pages.advanced"))
    private static String ChangeCategoryName(String old) {
        return "Extras";
    }
}