package com.nine.surfaceparticles.api.meta.update;

import com.nine.surfaceparticles.api.meta.distribution.Distribution;

import java.util.Map;

public record SPUpdateResult(
        SPUpdateStatus status,
        Map<Distribution, SPUpdateCandidate> candidates
) {

    public static final SPUpdateResult CHECKING = new SPUpdateResult(SPUpdateStatus.CHECKING, null);
    public static final SPUpdateResult NONE = new SPUpdateResult(SPUpdateStatus.NONE, null);
    public static final SPUpdateResult FAILED = new SPUpdateResult(SPUpdateStatus.FAILED, null);

    public static SPUpdateResult available(Map<Distribution, SPUpdateCandidate> candidates) {
        return new SPUpdateResult(SPUpdateStatus.AVAILABLE, candidates);
    }

    public boolean updateAvailable() {
        return status == SPUpdateStatus.AVAILABLE && candidates != null && !candidates.isEmpty();
    }
}
