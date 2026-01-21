package firstrentverdict.model.dtos;

import firstrentverdict.model.verdict.VerdictResult;

/**
 * Return adjusted verdict result along with comparison data.
 */
public record WhatIfResponse(
        VerdictResult result,
        String previousBottleneck,
        String currentBottleneck,
        boolean bottleneckChanged) {
}
