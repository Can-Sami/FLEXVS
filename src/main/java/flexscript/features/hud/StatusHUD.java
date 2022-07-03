package flexscript.features.hud;

import flexscript.features.profitcalculator.ProfitCalculator;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.*;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class StatusHUD extends UIBlock {

    UIComponent displayProfit;
    UIComponent displayString;
    UIComponent displayTimer;

    public StatusHUD(Window window) {
        this.setColor(new Color(36, 37, 39, 85))
                .setX(new PixelConstraint(10, true))
                .setY(new PixelConstraint(10, true))
                .setWidth(new AdditiveConstraint(new ChildBasedMaxSizeConstraint(), new PixelConstraint(20)))
                .setHeight(new AdditiveConstraint(new ChildBasedSizeConstraint(5), new PixelConstraint(10)))
                .setChildOf(window);

        displayProfit = new UIText("Total Profit Made:" + ProfitCalculator.totalProfit, false)
                .bindText(ProfitCalculator.realProfit)
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(5))
                .setChildOf(this);



        displayString = new UIText("Hourly Profit:" + ProfitCalculator.totalProfit, false)
                .bindText(ProfitCalculator.profitHour)
                .setX(new CenterConstraint())
                .setY(new AdditiveConstraint(new PixelConstraint(5), new SiblingConstraint()))
                .setChildOf(this);

        displayTimer = new UIText("Macro Time:" + ProfitCalculator.totalProfit, false)
                .bindText(ProfitCalculator.MacroTime)
                .setX(new CenterConstraint())
                .setY(new AdditiveConstraint(new PixelConstraint(5), new SiblingConstraint()))
                .setChildOf(this);
    }

}
