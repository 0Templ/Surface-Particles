package com.nine.surfaceparticles.compat.modmenu;

import com.nine.surfaceparticles.api.meta.distribution.Distribution;
import com.nine.surfaceparticles.api.meta.update.SPUpdateCandidate;
import com.nine.surfaceparticles.api.meta.update.SPUpdateChannel;
import com.nine.surfaceparticles.api.meta.update.SPUpdateResult;
import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateInfo;
import net.minecraft.network.chat.Component;

public class SPUpdateInfo implements UpdateInfo {

    public static SPUpdateInfo NONE = new SPUpdateInfo(false, Component.empty(), "", UpdateChannel.RELEASE);

    private final boolean updateAvailable;
    private final Component updateMessage;
    private final String url;
    private final UpdateChannel channel;

    public SPUpdateInfo(
            boolean updateAvailable,
            Component updateMessage,
            String url,
            UpdateChannel channel
    ) {
        this.updateAvailable = updateAvailable;
        this.updateMessage = updateMessage;
        this.url = url;
        this.channel = channel;
    }

    public static SPUpdateInfo from(SPUpdateResult result) {
        var updates = result.candidates();
        if (!result.updateAvailable() || updates == null || updates.isEmpty()) {
            return NONE;
        }
        SPUpdateCandidate upd = updates.get(Distribution.MODRINTH);
        if (upd == null) {
            var first = updates.entrySet().stream().findFirst();
            if (first.isPresent()) {
                upd = first.get().getValue();
            }
        }
        if (upd == null) return NONE;
        return new SPUpdateInfo(
                true,
                Component.literal(upd.version()),
                upd.url(),
                toModMenuChannel(upd.channel())
        );
    }

    private static UpdateChannel toModMenuChannel(SPUpdateChannel channel) {
        return switch (channel) {
            case RELEASE -> UpdateChannel.RELEASE;
            case BETA -> UpdateChannel.BETA;
            case ALPHA -> UpdateChannel.ALPHA;
        };
    }

    @Override
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @Override
    public Component getUpdateMessage() {
        return updateMessage;
    }

    @Override
    public String getDownloadLink() {
        return url;
    }

    @Override
    public UpdateChannel getUpdateChannel() {
        return channel;
    }

}
