/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.connector.network.translators.inventory.updater;

import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.TranslatorsInit;
import org.geysermc.connector.network.translators.inventory.InventoryTranslator;

public class CursorInventoryUpdater extends InventoryUpdater {
    @Override
    public void updateInventory(InventoryTranslator translator, GeyserSession session, Inventory inventory) {
        super.updateInventory(translator, session, inventory);

        for (int i = 0; i < translator.size; i++) {
            final int bedrockSlot = translator.javaSlotToBedrock(i);
            if (bedrockSlot == 50)
                continue;
            InventorySlotPacket slotPacket = new InventorySlotPacket();
            slotPacket.setContainerId(ContainerId.CURSOR);
            slotPacket.setSlot(bedrockSlot);
            slotPacket.setItem(TranslatorsInit.getItemTranslator().translateToBedrock(inventory.getItem(i)));
            session.getUpstream().sendPacket(slotPacket);
        }
    }

    @Override
    public boolean updateSlot(InventoryTranslator translator, GeyserSession session, Inventory inventory, int javaSlot) {
        if (super.updateSlot(translator, session, inventory, javaSlot))
            return true;

        InventorySlotPacket slotPacket = new InventorySlotPacket();
        slotPacket.setContainerId(ContainerId.CURSOR);
        slotPacket.setSlot(translator.javaSlotToBedrock(javaSlot));
        slotPacket.setItem(TranslatorsInit.getItemTranslator().translateToBedrock(inventory.getItem(javaSlot)));
        session.getUpstream().sendPacket(slotPacket);
        return true;
    }
}
