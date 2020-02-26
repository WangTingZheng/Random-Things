package lumien.randomthings.client.gui.redstoneremote;

import lumien.randomthings.client.gui.GuiContainerBase;
import lumien.randomthings.client.gui.elements.GuiSlotButton;
import lumien.randomthings.container.ContainerEmptyContainer;
import lumien.randomthings.container.inventories.InventoryItem;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageRedstoneRemoteStill;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class GuiRedstoneRemoteStillUse extends GuiContainerBase {
    final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/redstoneRemote/redstoneRemoteUse.png");

    InventoryItem remoteInventory;
    EnumHand using;
    ItemStack remoteStack;

    ArrayList<GuiSlotButton> slotButtons;

    public GuiRedstoneRemoteStillUse(EntityPlayer player, World world, int x, int y, int z)
    {
        super(new ContainerEmptyContainer(player, world, x, y, z));

        this.xSize = 187;
        this.ySize = 41;

        remoteStack = player.getHeldItemMainhand();
        using = EnumHand.MAIN_HAND;
        if (remoteStack == null)
        {
            remoteStack = player.getHeldItemOffhand();
            using = EnumHand.OFF_HAND;
        }

        if (remoteStack != null && remoteStack.getItem() == ModItems.redstoneRemoteStill)
        {
            remoteInventory = new InventoryItem("RedstoneRemoteStill", 18, remoteStack);
        }

        slotButtons = new ArrayList<>();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        for (int i = 0; i < slotButtons.size(); i++)
        {
            GuiSlotButton slotButton = slotButtons.get(i);
            if (slotButton != null && slotButton == button)
            {
                MessageRedstoneRemoteStill message = new MessageRedstoneRemoteStill(using, slotButton.id);
                PacketHandler.INSTANCE.sendToServer(message);

                break;
            }
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        if (remoteInventory != null)
        {
            for (int i = 0; i < 9; i++)
            {
                ItemStack position = remoteInventory.getStackInSlot(i);

                if (position != null && position.getItem() == ModItems.positionFilter)
                {
                    ItemStack camo = remoteInventory.getStackInSlot(i + 9);

                    if (camo == null)
                    {
                        camo = position;
                    }

                    camo = camo.copy();

                    String name = null;
                    NBTTagCompound stackTagCompound = position.getTagCompound();
                    if (stackTagCompound != null && stackTagCompound.hasKey("display", 10))
                    {
                        NBTTagCompound nbttagcompound = stackTagCompound.getCompoundTag("display");

                        if (nbttagcompound.hasKey("Name", 8))
                        {
                            name = nbttagcompound.getString("Name");
                        }
                    }

                    GuiSlotButton button = new GuiSlotButton(i, guiLeft + 5 + i * 20, guiTop + 17, camo, name);
                    this.buttonList.add(button);
                    this.slotButtons.add(button);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRenderer.drawString(I18n.format("item.redstoneRemoteStill.name", new Object[0]), 8, 6, 4210752);

        for (GuiButton guibutton : this.buttonList)
        {
            if (guibutton.isMouseOver())
            {
                guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
    }
}
