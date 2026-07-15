package com.nine.surfaceparticles.api.meta.update;

public record SPUpdateCandidate(
        SPUpdateChannel channel,
        String url,
        String version
) {
}
